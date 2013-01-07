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

import htmlexport.common.ExportOptions;

import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

import org.jetbrains.annotations.NotNull;

class ExportDialogPanel {
    // TODO2 use resources?
    private static final String ENABLED_CLIPBOARD_EXPORT_TITLE = "Clipboard export type";
    private static final String DISABLED_CLIPBOARD_EXPORT_TITLE = "Clipboard export type (disable for disk export)";

    private JPanel contentPane;
    private JRadioButton openedFileRadioButton;
    private JRadioButton selectionRadioButton;
    private JRadioButton clipboardRadioButton;
    private JRadioButton directoryRadioButton;
    private final JPanel pathPanel;
    private JRadioButton noLineNumbersRadioButton;
	private JRadioButton numberAsInEditorRadioButton;
	private JRadioButton numberingFromOneRadioButton;
    private JCheckBox showBorderCheckBox;
    private JTextField attributesTextField;
    private JRadioButton plainTextRadioButton;
    private JRadioButton asPastableHtmlRadioButton;
    private JRadioButton bothAsTextAndRadioButton;
    private JPanel clipboardExportTypePanel;
    private final ExportOptions exportOptions;
    private final ProjectState projectState;

    public ExportDialogPanel(@NotNull final ExportOptions exportOptions, final ProjectState projectState,
                             final PathSelectionPanel pathPanel) {
        this.projectState = projectState;
        this.pathPanel = (JPanel) pathPanel;
        this.exportOptions = exportOptions;

        loadGUIStateFromExportOptions();
        addListeners();

        openedFileRadioButton.setText(openedFileRadioButton.getText() + " (" + projectState.getOpenedFileName() + ")");
    }

    private void addListeners() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                keepGUIInvariants();
            }
        };
        clipboardRadioButton.addActionListener(actionListener);
        directoryRadioButton.addActionListener(actionListener);
    }

    private void keepGUIInvariants() {
        getPathPanel().setEnabled(directoryRadioButton.isSelected());
        if (projectState.editorHasNoSelection()) {
            openedFileRadioButton.setSelected(true);
            selectionRadioButton.setSelected(false);
            selectionRadioButton.setEnabled(false);
            selectionRadioButton.setText(selectionRadioButton.getText());
        }

        boolean useExportToClipboard = clipboardRadioButton.isSelected();
        plainTextRadioButton.setEnabled(useExportToClipboard);
        asPastableHtmlRadioButton.setEnabled(useExportToClipboard);
        bothAsTextAndRadioButton.setEnabled(useExportToClipboard);
        clipboardExportTypePanel.setEnabled(useExportToClipboard);
        TitledBorder border = (TitledBorder) clipboardExportTypePanel.getBorder();
        if (useExportToClipboard) {
            border.setTitle(ENABLED_CLIPBOARD_EXPORT_TITLE);
        } else {
            border.setTitle(DISABLED_CLIPBOARD_EXPORT_TITLE);
        }
    }

    private void loadGUIStateFromExportOptions() {
        switch (exportOptions.getExportFrom()) {
            case OPENED_FILE:
                openedFileRadioButton.setSelected(true);
                break;
            case SELECTION:
                selectionRadioButton.setSelected(true);
                break;
            default:
                throw new IllegalStateException();
        }

        switch (exportOptions.getExportTo()) {
            case CLIPBOARD:
                clipboardRadioButton.setSelected(true);
                break;
            case DISK:
                directoryRadioButton.setSelected(true);
                break;
            default:
                throw new IllegalStateException();
        }

        switch (exportOptions.getClipboardExportType()) {
            case PLAIN_TEXT:
                plainTextRadioButton.setSelected(true);
                break;
            case PASTABLE_HTML:
                asPastableHtmlRadioButton.setSelected(true);
                break;
            case PLAIN_AND_PASTABLE_HTML:
                bothAsTextAndRadioButton.setSelected(true);
                break;
            default:
                throw new IllegalStateException();
        }

        if (exportOptions.getOutputDirectory() != null) {
            getPathPanel().setText(exportOptions.getOutputDirectory());
        } else {
            getPathPanel().setText(projectState.getDefaultExportDir());
        }

        switch (exportOptions.getLineNumbering()) {
            case AS_IN_EDITOR:
                numberAsInEditorRadioButton.setSelected(true);
                break;
            case START_FROM_ONE:
                numberingFromOneRadioButton.setSelected(true);
                break;
            case NO_NUMBERS:
                noLineNumbersRadioButton.setSelected(true);
                break;
            default:
                throw new IllegalStateException();
        }

        showBorderCheckBox.setSelected(exportOptions.showBorder());
        attributesTextField.setText(exportOptions.getPreTagAttributes());

        keepGUIInvariants();
    }

    public ExportOptions getExportOptionsFromGUIState() {
        ExportOptions.ExportFrom exportFrom;
        if (openedFileRadioButton.isSelected()) {
            exportFrom = ExportOptions.ExportFrom.OPENED_FILE;
        } else if (selectionRadioButton.isSelected()) {
            exportFrom = ExportOptions.ExportFrom.SELECTION;
        } else {
            throw new IllegalStateException();
        }

        ExportOptions.ExportTo exportTo;
        if (clipboardRadioButton.isSelected()) {
            exportTo = ExportOptions.ExportTo.CLIPBOARD;
        } else if (directoryRadioButton.isSelected()) {
            exportTo = ExportOptions.ExportTo.DISK;
        } else {
            throw new IllegalStateException();
        }

        ExportOptions.ClipboardExportType clipboardExportType;
        if (plainTextRadioButton.isSelected()) {
            clipboardExportType = ExportOptions.ClipboardExportType.PLAIN_TEXT;
        } else if (asPastableHtmlRadioButton.isSelected()) {
            clipboardExportType = ExportOptions.ClipboardExportType.PASTABLE_HTML;
        } else if (bothAsTextAndRadioButton.isSelected()) {
            clipboardExportType = ExportOptions.ClipboardExportType.PLAIN_AND_PASTABLE_HTML;
        } else {
            throw new IllegalStateException();
        }

        String outputDirectory = getPathPanel().getText();

	    ExportOptions.LineNumbering lineNumbering;
        if (noLineNumbersRadioButton.isSelected()) {
		    lineNumbering = ExportOptions.LineNumbering.NO_NUMBERS;
	    } else if (numberAsInEditorRadioButton.isSelected()) {
		    lineNumbering = ExportOptions.LineNumbering.AS_IN_EDITOR;
	    } else if (numberingFromOneRadioButton.isSelected()) {
		    lineNumbering = ExportOptions.LineNumbering.START_FROM_ONE;
	    } else {
		    throw new IllegalStateException();
	    }

        boolean showBorder = showBorderCheckBox.isSelected();
        String preTagAttributes = attributesTextField.getText();

        return new ExportOptions(
                exportFrom, exportTo, clipboardExportType, outputDirectory, lineNumbering, showBorder, preTagAttributes
        );
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    private PathSelectionPanel getPathPanel() {
        return (PathSelectionPanel) pathPanel;
    }

    @SuppressWarnings({"EmptyMethod"})
    private void createUIComponents() {
    }
}
