/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package htmlexport;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.openapi.editor.impl.DocumentMarkupModel;
import com.intellij.openapi.editor.markup.MarkupModel;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import htmlexport.common.ExportOptions;
import htmlexport.common.TextAttributesMerger;
import htmlexport.common.TextAttributesRange;
import htmlexport.html.CompleteFileHtmlGenerator;
import htmlexport.html.HtmlGenerator;
import htmlexport.html.PreTagModifier;
import htmlexport.html.SnippetHtmlGenerator;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static htmlexport.common.ExportOptions.ExportFrom.SELECTION;
import static htmlexport.common.ExportOptions.LineNumbering.NO_NUMBERS;
import static htmlexport.common.Utils.getOpenedTextEditor;
import static htmlexport.common.Utils.getVirtualFileFromOpenedEditor;

/**
 * User: dima
 * Date: Dec 8, 2008
 * Time: 10:39:19 PM
 */
class Exporter {
    private final Project project;
    private final Editor textEditor;
    private final Document document;

    private final ExportOptions exportOptions;

    public Exporter(@NotNull final ExportOptions exportOptions, @NotNull final Project project) {
        this.exportOptions = exportOptions;
        this.project = project;

        textEditor = getOpenedTextEditor(project);
        document = textEditor.getDocument();
    }

    public void doExport() throws IOException {
        String html = generateHtml();
        writeOutput(html);
    }

    private void writeOutput(final String output) throws IOException {
        switch (exportOptions.getExportTo()) {
            case DISK:
                String path = exportOptions.getOutputDirectory();
                String filename = getCurrentVirtualFileName();
                new OutputWriter(output, path, filename).writeOutput();
                break;
            case CLIPBOARD:
                // TODO2 should run in a separate thread to avoid com.intellij.Patches.SUN_BUG_ID_4818143
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                ExportOptions.ClipboardExportType exportType = exportOptions.getClipboardExportType();

                if (exportType == ExportOptions.ClipboardExportType.PLAIN_TEXT) {
                    // use CopyPasteManager in this case, otherwise plain text is not pasted into IntelliJ
                    CopyPasteManager copyPasteManager = CopyPasteManager.getInstance();
                    copyPasteManager.setContents(new StringSelection(output));
                } else {
                    ClipboardHelper.publish(clipboard, exportType, output);
                }
                break;
            default:
                throw new IllegalStateException();
        }
    }

    private String generateHtml() {
        HtmlGenerator htmlGenerator = createHtmlGenerator();
        String html = htmlGenerator.generate();

        PreTagModifier preTagModifier = new PreTagModifier(exportOptions);
        html = preTagModifier.applyTo(html);

        return html;
    }

    private HtmlGenerator createHtmlGenerator() {
        String text = getEditorText();
        List<TextAttributesRange> mergedAttributes = getMergedTextAttributes();
        TextAttributes lineNumbersAttributes = getLineNumbersColor();
        int firstLineNumber = getFirstLineNumber();

        switch (exportOptions.getHtmlExportType()) {
            case FULL_HTML:
                return new CompleteFileHtmlGenerator(
		                getCurrentVirtualFileName(),
		                text,
		                mergedAttributes,
                        exportOptions.getLineNumbering() != NO_NUMBERS,
		                lineNumbersAttributes,
		                firstLineNumber,
                        exportOptions.optimizeStyles()
                );
            case INLINED_STYLES:
                return new SnippetHtmlGenerator(
		                text,
		                mergedAttributes,
                        exportOptions.getLineNumbering() != NO_NUMBERS,
		                lineNumbersAttributes,
		                firstLineNumber,
                        exportOptions.optimizeStyles()
                );
            default:
                throw new IllegalStateException();
        }
    }

    private List<TextAttributesRange> getMergedTextAttributes() {
        List<TextAttributesRange> attributes = collectTextAttributes();
        switch (exportOptions.getExportFrom()) {
            case SELECTION:
                TextAttributesFilter attributesFilter = new TextAttributesFilter(
                        textEditor.getSelectionModel().getSelectionStart(),
                        textEditor.getSelectionModel().getSelectionEnd());
                attributes = attributesFilter.removeAttributesNotInSelection(attributes);
                break;
            case OPENED_FILE:
                // do nothing
                break;
            default:
                throw new IllegalStateException();
        }

        String text = getEditorText();
        return new TextAttributesMerger(attributes, text).merge();
    }

    private String getEditorText() {
        switch (exportOptions.getExportFrom()) {
            case SELECTION:
                String text = textEditor.getSelectionModel().getSelectedText();
                if (text == null)
                    throw new IllegalStateException("Sorry, it's a programming error. " +
                            "It shouldn't be possible to export selection if there is no selected text");
                return text;
            case OPENED_FILE:
                return document.getText();
            default:
                throw new IllegalStateException();
        }
    }

    private int getFirstLineNumber() {
        int firstLineNumber;
        if (exportOptions.getLineNumbering() == NO_NUMBERS) {
            firstLineNumber = 1;
        } else {
            switch (exportOptions.getLineNumbering()) {
                case AS_IN_EDITOR:
                    if (exportOptions.getExportFrom() == SELECTION) {
                        int lineNumber = document.getLineNumber(textEditor.getSelectionModel().getSelectionStart());
                        firstLineNumber = lineNumber + 1;
                    } else {
                        firstLineNumber = 1;
                    }
                    break;
                case START_FROM_ONE:
                    firstLineNumber = 1;
                    break;
                default:
                    throw new IllegalStateException();
            }
        }
        return firstLineNumber;
    }

    private TextAttributes getLineNumbersColor() {
        EditorColorsScheme colorsScheme = textEditor.getColorsScheme();
        Color lineNumbersColor = colorsScheme.getColor(EditorColors.LINE_NUMBERS_COLOR);
        return new TextAttributes(lineNumbersColor, null, null, null, 0);
    }

    private String getCurrentVirtualFileName() {
        VirtualFile file = getVirtualFileFromOpenedEditor(project);
        return file.getName();
    }

    private List<TextAttributesRange> collectTextAttributes() {
        List<TextAttributesRange> attributesList = new ArrayList<TextAttributesRange>();

        // TODO2 don't use EditorEx
        EditorHighlighter editorHighlighter = ((EditorEx) textEditor).getHighlighter();
        HighlighterIterator i = editorHighlighter.createIterator(0);
        while (!i.atEnd()) {
            attributesList.add(new TextAttributesRange(i));
            i.advance();
        }

        MarkupModel documentModel = DocumentMarkupModel.forDocument(document, project, false);
        RangeHighlighter[] highlighters = documentModel.getAllHighlighters();
        // assume that highlighters are already ordered by layer index, therefore no need to sort them
        for (RangeHighlighter highlighter : highlighters) {
            attributesList.add(new TextAttributesRange(highlighter));
        }

        highlighters = textEditor.getMarkupModel().getAllHighlighters();
        for (RangeHighlighter highlighter : highlighters) {
            attributesList.add(new TextAttributesRange(highlighter));
        }

        return attributesList;
    }

}
