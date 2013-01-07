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
package htmlexport.html.css;

import com.intellij.openapi.editor.markup.TextAttributes;

/**
 * User: dima
 * Date: Nov 30, 2008
 * Time: 6:32:23 PM
 */
class CssClass {
    private final String declaration;
    private final String name;

    public CssClass(final String name, final TextAttributes textAttributes) {
        this.name = name;
        this.declaration = textAttributesToDeclaration(textAttributes);
    }

    private String textAttributesToDeclaration(final TextAttributes textAttributes) {
        TextAttributesToStyleConverter converter = new TextAttributesToStyleConverter(textAttributes);
        return "." + name + "{" + converter.foreground() + converter.background() + converter.fontWeight() + converter.fontStyle() + "}";
    }

    public String getDeclaration() {
        return declaration;
    }

    public String getName() {
        return name;
    }
}
