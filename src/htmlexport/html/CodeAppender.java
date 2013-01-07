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

import com.intellij.openapi.util.text.StringUtil;
import htmlexport.common.TextAttributesRange;
import htmlexport.html.attributeappenders.AttributeAppender;

import java.util.List;

/**
 * User: dima
 * Date: Dec 18, 2008
 * Time: 2:54:03 PM
 */
class CodeAppender implements OutputAppender {
    private final String codeAsText;
    private final List<TextAttributesRange> attributesList;
    private final AttributeAppender attributeAppender;

    public CodeAppender(final String codeAsText, final List<TextAttributesRange> attributesList,
                               final AttributeAppender attributeAppender) {
        this.codeAsText = codeAsText;
        this.attributesList = attributesList;
        this.attributeAppender = attributeAppender;
    }

    @Override
    public void writeTo(final StringBuilder output) {
        output.append(convertCodeToHtml());
    }

    private CharSequence convertCodeToHtml() {
        if (attributesList.isEmpty()) return codeAsText;

        StringBuilder result = new StringBuilder();

        int lastIndex = 0;
        for (TextAttributesRange textAttributesRange : attributesList) {
            String codeSnippet = codeAsText.substring(textAttributesRange.getFrom(), textAttributesRange.getTo());
            codeSnippet = StringUtil.escapeXml(codeSnippet);

            if (textAttributesRange.getFrom() > lastIndex) {
                String codeWithoutDecoration = codeAsText.substring(lastIndex, textAttributesRange.getFrom());
                result.append(StringUtil.escapeXml(codeWithoutDecoration));
            }

            appendCode(result, textAttributesRange, codeSnippet);

            lastIndex = textAttributesRange.getTo();
        }

        TextAttributesRange lastTextAttributes = attributesList.get(attributesList.size() - 1);
        if (lastTextAttributes.getTo() < codeAsText.length()) {
            result.append(codeAsText.substring(lastTextAttributes.getTo()));
        }

        return result;
    }

    private void appendCode(final StringBuilder output, final TextAttributesRange textAttributesRange, final String codeSnippet) {
        output.append("<span");
        attributeAppender.appendAttributeTo(output, textAttributesRange.getTextAttributes());
        output.append(">").append(codeSnippet).append("</span>");
    }
}
