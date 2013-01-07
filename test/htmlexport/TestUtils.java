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

import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.util.io.FileUtil;
import htmlexport.common.TextAttributesRange;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * User: dima
 * Date: Dec 2, 2008
 * Time: 1:45:36 PM
 */
public class TestUtils {
    public static final String TEST_DIR = "test/htmlexport/";

    private TestUtils() {
    }

    public static TextAttributes textAttributes(final Color foreground, final Color background, final int font) {
        final TextAttributes textAttributes;
        textAttributes = new TextAttributes();
        textAttributes.setFontType(font);
        textAttributes.setForegroundColor(foreground);
        textAttributes.setBackgroundColor(background);
        return textAttributes;
    }

    public static TextAttributesRange range(final int from, final int to) {
        return range(from, to, Color.BLACK, null, Font.PLAIN);
    }

    public static TextAttributesRange range(final int from, final int to, final Color color, final int fontStyle) {
        return range(from, to, color, null, fontStyle);
    }

    public static TextAttributesRange range(final int from, final int to, final Color color) {
        return range(from, to, color, null, Font.PLAIN);
    }

    public static TextAttributesRange range(final int from, final int to, final Color foreground,
                                            final Color background, final int fontStyle) {
        TextAttributes textAttributes = textAttributes(foreground, background, fontStyle);
        return new TextAttributesRange(textAttributes, from, to);
    }

    public static String loadCodeFromFile(final String filename) {
        try {
            String pathToFile = TEST_DIR + filename;
            FileInputStream inputStream = new FileInputStream(pathToFile);
            return FileUtil.loadTextAndClose(new InputStreamReader(inputStream));
        } catch (IOException e) {
            throw new AssertionError("Couldn't load text from " + filename);
        }
    }
}
