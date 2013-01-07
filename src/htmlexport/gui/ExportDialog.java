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
package htmlexport.gui;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.fileChooser.FileTextField;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.project.Project;
import htmlexport.common.Utils;
import htmlexport.common.ExportOptions;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * User: dima
 * Date: Dec 14, 2008
 * Time: 10:37:21 PM
 */
public class ExportDialog extends DialogWrapper {
    private static final String EXPORT_DIALOG_TITLE = "Export to HTML";
    private static final String PATH_PANEL_TITLE = "Select output directory";
    private static final String PATH_PANEL_DESCRIPTION = "Exported HTML will be saved in this directory";

    private final ExportDialogPanel exportDialogPanel;


    @Nullable
    public static ExportOptions askUserForExportOptions(@NotNull final ExportOptions previousExportOptions,
                                                        final Project project) {
        ProjectState projectState = new ProjectState(project);
        ExportDialog dialog = new ExportDialog(previousExportOptions, project, projectState);
        dialog.show();
        if (dialog.isOK()) {
            return dialog.getResult();
        } else {
            return null;
        }
    }

    private ExportDialog(@NotNull final ExportOptions previousExportOptions, final Project project, final ProjectState projectState) {
        super(project, true);

        PathSelectionPanel pathPanel = createPathPanel(project);
        exportDialogPanel = new ExportDialogPanel(previousExportOptions, projectState, pathPanel);

        setTitle(EXPORT_DIALOG_TITLE);
        init();
    }

    private ExportOptions getResult() {
        return exportDialogPanel.getExportOptionsFromGUIState();
    }

    @Override
    protected JComponent createCenterPanel() {
        return exportDialogPanel.getContentPane();
    }

    @Override
    protected Action[] createActions() {
        return new Action[]{getOKAction(), getCancelAction()};
    }

    private PathSelectionPanel createPathPanel(final Project project) {
        FileTextField filetextfield = FileChooserFactory.getInstance().createFileTextField(
                FileChooserDescriptorFactory.createSingleFolderDescriptor(),
                myDisposable
        );
        final MyTextFieldWithBrowseButton pathPanel = new MyTextFieldWithBrowseButton(filetextfield);
        pathPanel.addBrowseFolderListener(
                PATH_PANEL_TITLE,
                PATH_PANEL_DESCRIPTION,
                project,
                FileChooserDescriptorFactory.createSingleFolderDescriptor());

        return pathPanel;
    }

    private static class MyTextFieldWithBrowseButton extends TextFieldWithBrowseButton implements PathSelectionPanel {
        public MyTextFieldWithBrowseButton(final FileTextField filetextfield) {
            super(filetextfield.getField());
        }

        @Override
        public void setEnabled(final boolean isEnabled) {
            getTextField().setEnabled(isEnabled);
            getButton().setEnabled(isEnabled);
        }
    }
}
