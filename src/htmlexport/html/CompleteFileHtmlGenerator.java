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
import static htmlexport.common.TextAttributesRange.*;
import htmlexport.html.css.StylesRegistry;
import htmlexport.html.attributeappenders.AttributeAppender;
import htmlexport.html.attributeappenders.ClassAttributeAppender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * User: dima
 * Date: Nov 30, 2008
 * Time: 4:07:12 PM
 */
public class CompleteFileHtmlGenerator implements HtmlGenerator {
    private final List<OutputAppender> outputAppenders = new ArrayList<OutputAppender>();

    public CompleteFileHtmlGenerator(final String filename, @NotNull final String codeAsText,
                                     final List<TextAttributesRange> attributesRangeList, final boolean showLineNumbers,
                                     final TextAttributes lineNumbersAttributes, final int firstLineNumber, boolean optimizeStyles) {
        TextAttributes mostUsedAttributes = null;
        if (optimizeStyles) {
            mostUsedAttributes = MostUsedAttributesFinder.findAndRemoveFrom(attributesRangeList);
        }

        final StylesRegistry stylesRegistry = StylesRegistry.createFromTextAttributes(extractTextAttributes(attributesRangeList));
        final AttributeAppender attributeAppender = new ClassAttributeAppender(stylesRegistry);
        OutputAppender codeAppender = new CodeAppender(codeAsText, attributesRangeList, attributeAppender);

        if (showLineNumbers) {
	        stylesRegistry.registerTextAttributes(lineNumbersAttributes);
	        codeAppender = new LineNumbersAppender(codeAppender, lineNumbersAttributes, attributeAppender, firstLineNumber);
        }

        OutputAppender bodyAppender = new BodyAppender(codeAppender, mostUsedAttributes);

        outputAppenders.add(new HeaderAppender(filename, stylesRegistry));
        outputAppenders.add(bodyAppender);
        outputAppenders.add(new FooterAppender());
    }

    public String generate() {
        final StringBuilder output = new StringBuilder();

        for (OutputAppender outputAppender : outputAppenders) {
            outputAppender.writeTo(output);
        }

        return output.toString();
    }
}
