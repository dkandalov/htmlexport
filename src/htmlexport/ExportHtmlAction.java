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

import com.intellij.CommonBundle;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import htmlexport.common.ExportOptions;
import htmlexport.common.Utils;
import static htmlexport.common.Utils.getProject;
import static htmlexport.common.Utils.noOpenedProject;
import htmlexport.gui.ExportDialog;

import javax.swing.*;
import java.io.IOException;

/**
 * User: dima
 * Date: Nov 11, 2008
 * Time: 10:56:57 PM
 */
public class ExportHtmlAction extends AnAction {
    private boolean shouldPerformExport;

    @Override
    public void actionPerformed(final AnActionEvent event) {
        Project project = Utils.getProject(event.getDataContext());

        askUserForExportOptions(project);
        if (!shouldPerformExport) return;
        Exporter exporter = new Exporter(Settings.getExportOptions(), project);
        try {
            exporter.doExport();
        } catch (IOException e) {
            showErrorDialog(e);
        }
    }

    @Override
    public void update(final AnActionEvent event) {
        if (noOpenedProject(event.getDataContext())) {
            event.getPresentation().setEnabled(false);
            return;
        }

        Project project = getProject(event.getDataContext());
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        Editor textEditor = fileEditorManager.getSelectedTextEditor();
        event.getPresentation().setEnabled(textEditor != null);
    }

    private void askUserForExportOptions(final Project project) {
        ExportOptions exportOptions = ExportDialog.askUserForExportOptions(Settings.getExportOptions(), project);
        shouldPerformExport = (exportOptions != null);
        if (shouldPerformExport) {
            Settings.setExportOptions(exportOptions);
        }
    }

    private void showErrorDialog(final IOException e) {
        JOptionPane.showMessageDialog(
                null,
                e.getClass().getSimpleName() + ": " + e.getMessage(),
                CommonBundle.getErrorTitle(),
                JOptionPane.ERROR_MESSAGE
        );
    }
}
