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

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

/**
 * User: dima
 * Date: Dec 12, 2008
 * Time: 12:13:00 AM
 */
public class Utils {
    private Utils() {}

    public static boolean noOpenedProject(final DataContext dataContext) {
        return (PlatformDataKeys.PROJECT.getData(dataContext) == null);
    }

    @NotNull
    public static Project getProject(final DataContext dataContext) {
        Project project = PlatformDataKeys.PROJECT.getData(dataContext);
        if (project == null) {
            throw new IllegalStateException("Couldn't get project instance (project is null)");
        }
        return project;
    }

    @NotNull
    public static Editor getOpenedTextEditor(final Project project) {
        // use FileEditorManager rather than LangDataKeys.EDITOR.getData(dataContext) because
        // LangDataKeys.EDITOR seems to be null for some time after new editor is opened
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        Editor result = fileEditorManager.getSelectedTextEditor();
        if (result == null) {
            throw new IllegalStateException("Couldn't get text editor instance");
        }
        return result;
   }

    @NotNull
    public static VirtualFile getVirtualFileFromOpenedEditor(final Project project) {
        // use PsiDocumentManager rather than LangDataKeys.VIRTUAL_FILE.getData(dataContext) because
        // LangDataKeys.VIRTUAL_FILE seems to be null for some time after new editor is opened
        PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(project);
        PsiFile psiFile = psiDocumentManager.getPsiFile(getOpenedTextEditor(project).getDocument());
        if (psiFile == null) {
            throw new IllegalStateException("Couldn't get current psi file");
        }

        VirtualFile file = psiFile.getVirtualFile();
        if (file == null) {
            throw new IllegalStateException("Couldn't get current virtual file");
        }
        return file;
    }
}
