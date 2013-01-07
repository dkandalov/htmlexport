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

import htmlexport.html.css.TextAttributesToStyleConverter;
import com.intellij.openapi.editor.markup.TextAttributes;
import org.jetbrains.annotations.Nullable;

/**
 * User: dima
 * Date: Dec 18, 2008
 * Time: 2:30:10 PM
 */
class BodyAppender implements OutputAppender {
    // this constant is in this class to create explicit language-level dependency
    public static final String PRE_WITH_STYLE_ATTRIBUTE = "<pre style=\"";
    public static final String PRE_WITHOUT_ATTRIBUTES = "<pre>";

    private final OutputAppender codeAppender;
    private final TextAttributes mostUsedAttributes;

    public BodyAppender(final OutputAppender codeAppender, @Nullable TextAttributes mostUsedAttributes) {
        this.codeAppender = codeAppender;
        this.mostUsedAttributes = mostUsedAttributes;
    }

    @Override
    public void writeTo(final StringBuilder output) {
        String mostUsedStyle = "";
        if (mostUsedAttributes != null) {
            TextAttributesToStyleConverter converter = new TextAttributesToStyleConverter(mostUsedAttributes);
            mostUsedStyle = " style=\"" + converter.foreground() + converter.background() + converter.fontWeight() +
                    converter.fontStyle() + "\"";
        }

        output.append("<pre").append(mostUsedStyle).append(">\n");
        codeAppender.writeTo(output);
        output.append("\n");
        output.append("</pre>\n");
    }
}
