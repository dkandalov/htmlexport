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

import static htmlexport.common.ExportOptions.ExportFrom.*;
import static htmlexport.common.ExportOptions.ExportTo.*;
import static htmlexport.common.ExportOptions.LineNumbering.*;
import htmlexport.common.ExportOptions;
import htmlexport.common.ExportOptionsTestBuilder;

import javax.swing.*;
import java.awt.*;

/**
 * User: dima
 * Date: Dec 10, 2008
 * Time: 1:35:05 PM
 */
public class ExportDialogPanel_Playground {
    public static void main(String[] args) {
        ExportOptions exportOptions = new ExportOptionsTestBuilder(SELECTION, CLIPBOARD).
		        showLineNumbers(AS_IN_EDITOR).build();
        DummyProjectState dummyActions = new DummyProjectState();
        ExportDialogPanel dialogPanel = new ExportDialogPanel(exportOptions, dummyActions, new DummyPanel());

        final JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.getContentPane().add(dialogPanel.getContentPane());
        jFrame.pack();
        jFrame.setVisible(true);
    }

    private static class DummyProjectState extends ProjectState {
        public DummyProjectState() {
            super(null);
        }

        @Override
        public String getDefaultExportDir() {
            return "";
        }

        @Override
        public boolean editorHasNoSelection() {
            return true;
        }

        @Override
        public String getOpenedFileName() {
            return "asdf.asdfasdf";
        }
    }

    private static class DummyPanel extends JPanel implements PathSelectionPanel {
        private DummyPanel() {
            add(new JTextField("aaaaaaaaaaaaa"));
            add(new JButton("..."));
        }

        @Override
        public void setText(final String text) {
        }

        @Override
        public String getText() {
            return "";
        }

        @Override
        public void setEnabled(final boolean enabled) {
            for (Component component : getComponents()) {
                component.setEnabled(enabled);
            }
        }
    }
}
