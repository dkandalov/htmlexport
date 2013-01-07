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

/**
 * User: dima
 * Date: Jan 4, 2009
 * Time: 6:13:00 PM
 */
public class ExportOptionsTestBuilder {
	private final ExportOptions.ExportFrom exportFrom;
	private final ExportOptions.ExportTo exportTo;
    private ExportOptions.ClipboardExportType clipboardExportType;
	private String outputDirectory;
	private ExportOptions.LineNumbering lineNumbering;
    private boolean optimizeStyles;
    private boolean showBorder;
    private String preTagAttributes;

    public ExportOptionsTestBuilder() {
        this(ExportOptions.ExportFrom.OPENED_FILE, ExportOptions.ExportTo.CLIPBOARD);
    }

    public ExportOptionsTestBuilder(final ExportOptions.ExportFrom exportFrom, final ExportOptions.ExportTo exportTo) {
		this.exportFrom = exportFrom;
		this.exportTo = exportTo;
        this.lineNumbering = ExportOptions.LineNumbering.NO_NUMBERS;
        this.clipboardExportType = ExportOptions.ClipboardExportType.PLAIN_TEXT;
        this.preTagAttributes = "";
	}

	public ExportOptionsTestBuilder showLineNumbers(ExportOptions.LineNumbering lineNumbering) {
		this.lineNumbering = lineNumbering;
		return this;
	}

	public ExportOptionsTestBuilder outputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
		return this;
	}

    public ExportOptionsTestBuilder optimizeStyles() {
        this.optimizeStyles = true;
        return this;
    }

    public ExportOptionsTestBuilder showBorder() {
        this.showBorder = true;
        return this;
    }

    public ExportOptionsTestBuilder withPreTagAttributes(String attributes) {
        this.preTagAttributes = attributes;
        return this;
    }

    public ExportOptionsTestBuilder pasteToClipboardAs(ExportOptions.ClipboardExportType clipboardExportType) {
        this.clipboardExportType = clipboardExportType;
        return this;
    }

	public ExportOptions build() {
		return new ExportOptions(
                exportFrom, exportTo, clipboardExportType,
                outputDirectory, lineNumbering,
                optimizeStyles, showBorder, preTagAttributes
        );
	}

}
