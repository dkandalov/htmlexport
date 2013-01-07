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

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.LightCodeInsightTestCase;
import htmlexport.common.ExportOptions;
import htmlexport.common.ExportOptionsTestBuilder;
import htmlexport.common.Utils;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;

import static htmlexport.TestUtils.TEST_DIR;
import static htmlexport.TestUtils.loadCodeFromFile;
import static htmlexport.common.ExportOptions.ExportFrom.OPENED_FILE;
import static htmlexport.common.ExportOptions.ExportFrom.SELECTION;
import static htmlexport.common.ExportOptions.ExportTo.CLIPBOARD;
import static htmlexport.common.ExportOptions.ExportTo.DISK;
import static htmlexport.common.ExportOptions.LineNumbering.AS_IN_EDITOR;

/**
 * User: dima
 * Date: Nov 20, 2008
 * Time: 10:13:03 PM
 */
public class ExportHtml_FuncTest extends LightCodeInsightTestCase {
    private static final boolean OVERWRITE_EXPECTED = false;
    private static final String ACTION_ID = "HtmlExport";
    private static final String FUNCTEST_INPUT = "functest_input/";
    private static final String FUNCTEST_OUTPUT = "functest_output/";
    private static final String FUNCTEST_EXPECTED = "functest_expected/";

    private AnAction action;
    private AnActionEvent actionEvent;
    private FileEditorManager fileEditorManager;
    private VirtualFile myVirtualFile;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        action = ActionManager.getInstance().getAction(ACTION_ID);
        actionEvent = new AnActionEvent(
                null,
                getCurrentEditorDataContext(),
                "",
                action.getTemplatePresentation(),
                ActionManager.getInstance(),
                0
        );
        fileEditorManager = FileEditorManager.getInstance(Utils.getProject(actionEvent.getDataContext()));
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        File outputFolder = new File(TEST_DIR + FUNCTEST_OUTPUT);
        for (File file : outputFolder.listFiles()) {
            FileUtil.delete(file);
        }
    }

    public void testActionShouldBeDisabledIfThereIsNoOpenedFile() {
        assertTrue(actionEvent.getPresentation().isEnabled());
        action.update(actionEvent);
        assertFalse(actionEvent.getPresentation().isEnabled());
    }

    public void testShouldExportOpenedFileToDisk() throws IOException {
        // setup
        createDummyFileAndOpenInEditor(FUNCTEST_INPUT, "Annotation.java");
        ExportOptions exportOptions = new ExportOptionsTestBuilder(OPENED_FILE, DISK).
		        outputDirectory(TEST_DIR + FUNCTEST_OUTPUT).build();
        Exporter exporter = createExporter(exportOptions);

        // exercise
        exporter.doExport();

        // verify
        String actualCode = loadCodeFromFile(FUNCTEST_OUTPUT + "Annotation.java.html");
        assertEqualsToFile(actualCode, FUNCTEST_EXPECTED + "Annotation.java.html");
    }

    public void testShouldExportOpenedFileToDisk2() throws IOException {
        // setup
        createDummyFileAndOpenInEditor(FUNCTEST_INPUT, "SomeClass.java");
        ExportOptions exportOptions = new ExportOptionsTestBuilder(OPENED_FILE, DISK).
			    outputDirectory(TEST_DIR + FUNCTEST_OUTPUT).build();
        Exporter exporter = createExporter(exportOptions);

        // exercise
        exporter.doExport();

        // verify
        String actualCode = loadCodeFromFile(FUNCTEST_OUTPUT + "SomeClass.java.html");
        assertEqualsToFile(actualCode, FUNCTEST_EXPECTED + "SomeClass.java.html");
    }

    public void testShouldExportOpenedFileToClipboard() throws IOException {
        // setup
        createDummyFileAndOpenInEditor(FUNCTEST_INPUT, "Annotation.java");
        ExportOptions exportOptions = new ExportOptionsTestBuilder(OPENED_FILE, DISK).
	            outputDirectory(TEST_DIR + FUNCTEST_OUTPUT).build();
        Exporter exporter = createExporter(exportOptions);

        // exercise
        exporter.doExport();

        // verify
        String actualCode = loadCodeFromFile(FUNCTEST_OUTPUT + "Annotation.java.html");
        assertEqualsToFile(actualCode, FUNCTEST_EXPECTED + "Annotation.java.html");
    }

    public void testShouldExportSelectionToClipboard() throws IOException {
        // setup
        createDummyFileAndOpenInEditor(FUNCTEST_INPUT, "Annotation.java");
        ExportOptions exportOptions = new ExportOptionsTestBuilder(SELECTION, DISK).
		        outputDirectory(TEST_DIR + FUNCTEST_OUTPUT).build();
        Exporter exporter = createExporter(exportOptions);

        // exercise
        Editor fileEditor = fileEditorManager.getSelectedTextEditor();
        assertNotNull(fileEditor);
        fileEditor.getSelectionModel().setSelection(0, 34);
        exporter.doExport();

        // verify
        String actualCode = loadCodeFromFile(FUNCTEST_OUTPUT + "Annotation.java.html");
        assertEqualsToFile(actualCode, FUNCTEST_EXPECTED + "Annotation_selection.java.html");
    }

    public void testShouldExportFileWithInlinedStylesToClipboard() throws IOException {
        // setup
        createDummyFileAndOpenInEditor(FUNCTEST_INPUT, "SomeClass.java");
        ExportOptions exportOptions = new ExportOptionsTestBuilder(OPENED_FILE, CLIPBOARD).
		        outputDirectory(TEST_DIR + FUNCTEST_OUTPUT).build();
        Exporter exporter = createExporter(exportOptions);

        // exercise
        exporter.doExport();

        // verify
        String actualCode = getClipboardContent();
        assertEqualsToFile(actualCode, FUNCTEST_EXPECTED + "SomeClass_inlinedstyles.java.html");
    }

    public void testShouldExportFileWithLineNumbersToClipboard() throws IOException {
        // setup
        createDummyFileAndOpenInEditor(FUNCTEST_INPUT, "SomeClass.java");
        ExportOptions exportOptions = new ExportOptionsTestBuilder(OPENED_FILE, DISK).
		        outputDirectory(TEST_DIR + FUNCTEST_OUTPUT).showLineNumbers(AS_IN_EDITOR).build();
        Exporter exporter = createExporter(exportOptions);

        // exercise
        exporter.doExport();

        // verify
        String actualCode = loadCodeFromFile(FUNCTEST_OUTPUT + "SomeClass.java.html");
        assertEqualsToFile(actualCode, FUNCTEST_EXPECTED + "SomeClass_linenumbers.java.html");
    }

	public void testShouldExportSelectionToClipboardWithLineNumbersAsInEditor() throws IOException {
	    // setup
	    createDummyFileAndOpenInEditor(FUNCTEST_INPUT, "SomeClass.java");
	    ExportOptions exportOptions = new ExportOptionsTestBuilder(SELECTION, DISK).
			    outputDirectory(TEST_DIR + FUNCTEST_OUTPUT).
			    showLineNumbers(AS_IN_EDITOR).build();
	    Exporter exporter = createExporter(exportOptions);

	    // exercise
	    Editor fileEditor = fileEditorManager.getSelectedTextEditor();
	    assertNotNull(fileEditor);
	    fileEditor.getSelectionModel().setSelection(121, 234);
	    exporter.doExport();

	    // verify
        String actualCode = loadCodeFromFile(FUNCTEST_OUTPUT + "SomeClass.java.html");
        assertEqualsToFile(actualCode, FUNCTEST_EXPECTED + "SomeClass_selection_linenumbers.java.html");
    }

    public void testShouldExportWithMostUsedStyleAsDefault() throws IOException {
        // setup
        createDummyFileAndOpenInEditor(FUNCTEST_INPUT, "SomeClass.java");
        ExportOptions exportOptions = new ExportOptionsTestBuilder(OPENED_FILE, CLIPBOARD).
		        outputDirectory(TEST_DIR + FUNCTEST_OUTPUT).optimizeStyles().build();
        Exporter exporter = createExporter(exportOptions);

        // exercise
        exporter.doExport();

        // verify
        String actualCode = getClipboardContent();
        assertEqualsToFile(actualCode, FUNCTEST_EXPECTED + "SomeClass_defaultstyles.java.html");
    }

    private static void assertEqualsToFile(String actualCode, String fileWithExpectedCode) {
        String expectedCode = loadCodeFromFile(fileWithExpectedCode);
        //noinspection ConstantConditions
        if (OVERWRITE_EXPECTED && (!expectedCode.equals(actualCode))) {
            try {
                File file = new File(TEST_DIR + fileWithExpectedCode);
                FileUtil.writeToFile(file, actualCode.getBytes());
                return;
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
        }
        assertEquals(expectedCode, actualCode);
    }

//    protected DataContext getCurrentEditorDataContext() {
//        final DataContext defaultContext = super.getCurrentEditorDataContext();
//        return new DataContext() {
//            @Nullable
//            public Object getData(@NonNls String dataId) {
//                //noinspection deprecation
//                if (dataId.equals(DataConstants.VIRTUAL_FILE)) return myVirtualFile;
//                return defaultContext.getData(dataId);
//            }
//        };
//    }

    private static String getClipboardContent() {
        try {
            CopyPasteManager copyPasteManager = CopyPasteManager.getInstance();
            String actualCode = (String) copyPasteManager.getContents().getTransferData(DataFlavor.stringFlavor);
            actualCode = new String(actualCode.getBytes("UTF-16"));
            return actualCode;
        } catch (UnsupportedFlavorException e) {
            throw new AssertionError("couldn't get clipboard content");
        } catch (IOException e) {
            throw new AssertionError("couldn't get clipboard content");
        }
    }

    private void createDummyFileAndOpenInEditor(final String path, final String inputFilename) {
        String code = loadCodeFromFile(path + inputFilename);
        PsiFile dummyFile = createFile(inputFilename, code);

        VirtualFile virtualFile = dummyFile.getVirtualFile();
        assert virtualFile != null;
        FileEditor[] fileEditors = fileEditorManager.openFile(virtualFile, true);

        myVirtualFile = virtualFile;
        myEditor = fileEditorManager.getSelectedTextEditor();

        assertEquals(1, fileEditors.length);
    }

    private Exporter createExporter(ExportOptions exportOptions) {
        return new Exporter(exportOptions, Utils.getProject(actionEvent.getDataContext()));
    }
}
