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

import htmlexport.TestUtils;
import static htmlexport.TestUtils.*;
import htmlexport.common.TextAttributesRange;
import static org.junit.Assert.*;
import org.junit.Test;

import java.awt.*;
import static java.util.Arrays.*;
import java.util.List;

/**
 * User: dima
 * Date: Dec 1, 2008
 * Time: 2:06:59 PM
 */
public class HtmlGenerator_Test {
    @Test
    public void shouldApplyAttributesRangesToText_When_CompleteFileHtmlGeneratorIsUsed() {
        List<TextAttributesRange> attributesRangeList = asList(
                range(1, 2, Color.RED, Font.PLAIN)
        );
        HtmlGenerator generator = new CompleteFileHtmlGenerator("", "12345", attributesRangeList, false, null, 1, false);

        String actual = generator.generate();
        String expected = TestUtils.loadCodeFromFile("html/CompleteFileHtmlGenerator_Test_expected.html");
        assertEquals(expected, actual);
    }

    @Test
    public void shouldApplyAttributesRangesToText_When_SnippetHtmlGeneratorIsUsed() {
        List<TextAttributesRange> attributesRangeList = asList(
                range(1, 2, Color.RED, Font.PLAIN)
        );
        HtmlGenerator generator = new SnippetHtmlGenerator("12345", attributesRangeList, false, null, 1, false);

        String actual = generator.generate();
        String expected = TestUtils.loadCodeFromFile("html/SnippetHtmlGenerator_Test_expected.html");
        assertEquals(expected, actual);
    }
}
