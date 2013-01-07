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

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.project.Project;
import static htmlexport.common.Utils.*;

/**
 * User: dima
 * Date: Dec 11, 2008
 * Time: 1:43:10 AM
 */
class ProjectState {
    private final Project project;

    public ProjectState(final Project project) {
        this.project = project;
    }

    public String getDefaultExportDir() {
        VirtualFile baseDir = project.getBaseDir();
        if (baseDir == null) return "";
        return baseDir.getPath() + "/htmlExport";
    }

    public boolean editorHasNoSelection() {
        Editor textEditor = getOpenedTextEditor(project);
        SelectionModel selectionModel = textEditor.getSelectionModel();
        return selectionModel.getSelectedText() == null;
    }

    public String getOpenedFileName() {
        VirtualFile file = getVirtualFileFromOpenedEditor(project);
        return file.getName();
    }
}
