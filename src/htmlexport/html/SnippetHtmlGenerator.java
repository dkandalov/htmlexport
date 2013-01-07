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

import com.intellij.openapi.editor.markup.TextAttributes;
import htmlexport.common.TextAttributesRange;
import htmlexport.html.attributeappenders.AttributeAppender;
import htmlexport.html.attributeappenders.StyleAttributeAppender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * User: dima
 * Date: Dec 19, 2008
 * Time: 3:32:08 PM
 */
public class SnippetHtmlGenerator implements HtmlGenerator {
    private final OutputAppender outputAppender;

    public SnippetHtmlGenerator(@NotNull final String codeAsText, final List<TextAttributesRange> attributesRangeList,
                                final boolean showLineNumbers, final TextAttributes lineNumbersAttributes, final int firstLineNumber,
                                boolean optimizeStyles) {
        TextAttributes mostUsedAttributes = null;
        if (optimizeStyles) {
            mostUsedAttributes = MostUsedAttributesFinder.findAndRemoveFrom(attributesRangeList);
        }

        final AttributeAppender attributeAppender = new StyleAttributeAppender();
        OutputAppender codeAppender = new CodeAppender(codeAsText, attributesRangeList, attributeAppender);

        if (showLineNumbers) {
            codeAppender = new LineNumbersAppender(codeAppender, lineNumbersAttributes, attributeAppender, firstLineNumber);
        }

        outputAppender = new BodyAppender(codeAppender, mostUsedAttributes);
    }

    public String generate() {
        final StringBuilder output = new StringBuilder();
        outputAppender.writeTo(output);
        return output.toString();
    }
}
