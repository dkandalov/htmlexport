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
package htmlexport.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: dima
 * Date: Dec 8, 2008
 * Time: 9:25:10 PM
 */
public class ExportOptions {
    private static final ExportFrom DEFAULT_EXPORT_FROM = ExportFrom.SELECTION;
    private static final ExportTo DEFAULT_EXPORT_TO = ExportTo.CLIPBOARD;
    private static final ClipboardExportType DEFAULT_EXPORT_TYPE = ClipboardExportType.PLAIN_TEXT;
    private static final String DEFAULT_OUTPUT_DIRECTORY = null;
    private static final LineNumbering DEFAULT_LINE_NUMBERING = LineNumbering.AS_IN_EDITOR;
    private static final boolean DEFAULT_SHOW_BORDER = false;
    private static final String DEFAULT_PRE_TAG_ATTRIBUTES = "font-family:monospace;";
    private static final boolean DEFUALT_OPTIMIZE_STYLES = true;

    public enum ExportFrom {
        SELECTION,
        OPENED_FILE
    }

    public enum ExportTo {
        DISK,
        CLIPBOARD
    }

    public enum ClipboardExportType {
        PLAIN_TEXT,
        PASTABLE_HTML,
        PLAIN_AND_PASTABLE_HTML
    }

    public enum HtmlExportType {
        FULL_HTML,
        INLINED_STYLES
    }

    public enum LineNumbering {
        NO_NUMBERS,
        AS_IN_EDITOR,
        START_FROM_ONE
    }

    private final ExportFrom exportFrom;
    private final ExportTo exportTo;
    private final ClipboardExportType clipboardExportType;
    private final String outputDirectory;
    private final LineNumbering lineNumbering;
    private final boolean optimizeStyles;
    private final boolean showBorder;
    private final String preTagAttributes;

    public static ExportOptions getDefaults() {
        return new ExportOptions(
                DEFAULT_EXPORT_FROM,
                DEFAULT_EXPORT_TO,
                DEFAULT_EXPORT_TYPE,
                DEFAULT_OUTPUT_DIRECTORY,
                DEFAULT_LINE_NUMBERING,
                DEFAULT_SHOW_BORDER,
                DEFAULT_PRE_TAG_ATTRIBUTES
        );
    }

    public ExportOptions(ExportFrom exportFrom, ExportTo exportTo, ClipboardExportType clipboardExportType, String outputDirectory,
                         LineNumbering lineNumbering, boolean showBorder, String preTagAttributes) {
        this(exportFrom, exportTo, clipboardExportType, outputDirectory, lineNumbering, DEFUALT_OPTIMIZE_STYLES, showBorder, preTagAttributes);
    }

    // this constructor exists only to disable style optimization for unit tests
    ExportOptions(ExportFrom exportFrom, ExportTo exportTo, ClipboardExportType clipboardExportType, String outputDirectory,
                  LineNumbering lineNumbering, boolean optimizeStyles, boolean showBorder, String preTagAttributes) {
        this.exportFrom = (exportFrom == null ? DEFAULT_EXPORT_FROM : exportFrom);
        this.exportTo = (exportTo == null ? DEFAULT_EXPORT_TO : exportTo);
        this.clipboardExportType = (clipboardExportType == null ? DEFAULT_EXPORT_TYPE : clipboardExportType);
        this.outputDirectory = outputDirectory;
        this.lineNumbering = (lineNumbering == null ? DEFAULT_LINE_NUMBERING : lineNumbering);
        this.optimizeStyles = optimizeStyles;
        this.showBorder = showBorder;
        this.preTagAttributes = (preTagAttributes == null ? DEFAULT_PRE_TAG_ATTRIBUTES : preTagAttributes);
    }

    @Nullable
    public String getOutputDirectory() {
        return outputDirectory;
    }

    public ExportFrom getExportFrom() {
        return exportFrom;
    }

    public ExportTo getExportTo() {
        return exportTo;
    }

    public ClipboardExportType getClipboardExportType() {
        return clipboardExportType;
    }
    
    public HtmlExportType getHtmlExportType() {
        switch (exportTo) {
            case CLIPBOARD:
                return HtmlExportType.INLINED_STYLES;
            case DISK:
                return HtmlExportType.FULL_HTML;
            default:
                throw new IllegalStateException();
        }
    }

    public LineNumbering getLineNumbering() {
        return lineNumbering;
    }

    public boolean optimizeStyles() {
        return optimizeStyles;
    }

    public boolean showBorder() {
        return showBorder;
    }

    @NotNull
    public String getPreTagAttributes() {
        return preTagAttributes;
    }


    @Override
    public String toString() {
        return "ExportOptions{" +
                "exportFrom=" + exportFrom +
                ", exportTo=" + exportTo +
                ", outputDirectory='" + outputDirectory + '\'' +
                ", lineNumbering=" + lineNumbering +
                ", optimizeStyles=" + optimizeStyles +
                '}';
    }
}
