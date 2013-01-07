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
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.*;

import htmlexport.html.css.TextAttributesToStyleConverter;

/**
 * User: dima
 * Date: Dec 11, 2008
 * Time: 2:34:19 PM
 */
public class TextAttributesToCssConverter_Test {
    @Test
    public void shouldUseForeground() {
        TextAttributes textAttributes = new TextAttributes();
        textAttributes.setForegroundColor(Color.LIGHT_GRAY);
        TextAttributesToStyleConverter converter = new TextAttributesToStyleConverter(textAttributes);

        assertEquals("color:rgb(192,192,192);", converter.foreground());
    }

    @Test
    public void shouldUseBackground() {
        TextAttributes textAttributes = new TextAttributes();
        textAttributes.setBackgroundColor(Color.LIGHT_GRAY);
        TextAttributesToStyleConverter converter = new TextAttributesToStyleConverter(textAttributes);

        assertEquals("background-color:rgb(192,192,192);", converter.background());
    }
}
