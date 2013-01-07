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

import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.util.xmlb.annotations.Transient;
import com.intellij.util.xmlb.XmlSerializerUtil;
import htmlexport.common.ExportOptions;
import org.jetbrains.annotations.NotNull;

/**
 * User: dima
 * Date: Jan 14, 2009
 * Time: 12:56:34 AM
 */
@State(name = "Settings", storages = {@Storage(id = "main", file = "$APP_CONFIG$/htmlexport.xml")})
public class Settings implements PersistentStateComponent<Settings> {
    public ExportOptions.ExportFrom exportFrom;
    public ExportOptions.ExportTo exportTo;
    public ExportOptions.ClipboardExportType clipboardExportType;
    public ExportOptions.LineNumbering lineNumbering;
    public boolean showBorder;
    public String preTagAttributes;

    @Transient
    private ExportOptions exportOptions = ExportOptions.getDefaults();

    public static Settings getInstance() {
        return ServiceManager.getService(Settings.class);
    }

    @NotNull
    public static ExportOptions getExportOptions() {
        return getInstance().exportOptions;
    }

    public static void setExportOptions(@NotNull ExportOptions exportOptions) {
        getInstance().exportOptions = exportOptions;
    }

    @Override
    public Settings getState() {
        exportFrom = exportOptions.getExportFrom();
        exportTo = exportOptions.getExportTo();
        clipboardExportType = exportOptions.getClipboardExportType();
        lineNumbering = exportOptions.getLineNumbering();
        showBorder = exportOptions.showBorder();
        preTagAttributes = exportOptions.getPreTagAttributes();
        return this;
    }

    @Override
    public void loadState(Settings settings) {
        XmlSerializerUtil.copyBean(settings, this);

        String dontStoreOutputDirectory = null;
        exportOptions = new ExportOptions(
                exportFrom, exportTo, clipboardExportType,
                dontStoreOutputDirectory, lineNumbering,
                showBorder, preTagAttributes);
    }
}
