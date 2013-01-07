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
package htmlexport.html;

import htmlexport.common.ExportOptions;

/**
 * User: dima
 * Date: Jan 26, 2009
 * Time: 12:36:06 AM
 */
public class PreTagModifier {
    private static final String BORDER_ATTRIBUTES = "border:thin solid;padding:5px;";

    private final ExportOptions exportOptions;

    public PreTagModifier(ExportOptions exportOptions) {
        this.exportOptions = exportOptions;
    }

    public String applyTo(String html) {
        String preAttributes = createAttributes();

        if (preAttributes.isEmpty()) {
            return html;
        }
        if (html.contains(BodyAppender.PRE_WITH_STYLE_ATTRIBUTE)) {
            return html.replaceFirst("(<pre style=\".*?)\"", "$1" + preAttributes + "\"");
        }
        if (html.contains(BodyAppender.PRE_WITHOUT_ATTRIBUTES)) {
            return html.replaceAll(BodyAppender.PRE_WITHOUT_ATTRIBUTES, "<pre style=\"" + preAttributes + "\">");
        }
        throw new IllegalStateException("Sorry, it's a programming error.");
    }

    private String createAttributes() {
        StringBuilder preAttributes = new StringBuilder();

        if (exportOptions.showBorder()) {
            preAttributes.append(BORDER_ATTRIBUTES);
        }
        String additionalAttributes = exportOptions.getPreTagAttributes();
        if (!additionalAttributes.isEmpty()) {
            preAttributes.append(additionalAttributes);
        }
        return preAttributes.toString();
    }
}
