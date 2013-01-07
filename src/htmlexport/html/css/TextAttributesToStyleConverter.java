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

import java.awt.*;

/**
 * User: dima
 * Date: Dec 1, 2008
 * Time: 1:52:39 PM
 */
public class TextAttributesToStyleConverter {
    private final TextAttributes textAttributes;

    public TextAttributesToStyleConverter(final TextAttributes textAttributes) {
        this.textAttributes = textAttributes;
    }

    public String foreground() {
        Color color = textAttributes.getForegroundColor();
        if (color == null) {
            color = Color.BLACK;
        }
        return "color:rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ");";
    }

    public String background() {
        Color color = textAttributes.getBackgroundColor();
        if (color == null) {
            return "";
        }
        return "background-color:rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ");";
    }

    public String fontWeight() {
        boolean isBold = (textAttributes.getFontType() & Font.BOLD) != 0;
        return "font-weight:" + (isBold ? "bold" : "normal") + ";";
    }

    public String fontStyle() {
        boolean isItalic = (textAttributes.getFontType() & Font.ITALIC) != 0;
        return "font-style:" + (isItalic ? "italic" : "normal") + ";";
    }
}
