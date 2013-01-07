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

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import htmlexport.common.TextAttributesRange;
import static htmlexport.TestUtils.range;
import static htmlexport.TestUtils.range;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.awt.*;

import com.intellij.openapi.editor.markup.TextAttributes;

/**
 * User: dima
 * Date: Jan 4, 2009
 * Time: 9:47:34 PM
 */
public class MostUsedAttributesFinder_Test {
    @Test
    public void shouldFindAndRemoveMostUsedAttributesRange() {
        List<TextAttributesRange> attributesRangeList = new ArrayList<TextAttributesRange>(Arrays.asList(
                range(0, 1, Color.BLACK),
                range(1, 2, Color.RED),
                range(2, 3, Color.RED),
                range(3, 4, Color.BLACK),
                range(4, 5, Color.BLACK),
                range(5, 6, Color.BLUE)
        ));
        TextAttributes mostUsed = MostUsedAttributesFinder.findAndRemoveFrom(attributesRangeList);

        assertEquals(range(0, 1, Color.BLACK).getTextAttributes(), mostUsed);
        assertEquals(3, attributesRangeList.size());
        assertEquals(range(1, 2, Color.RED), attributesRangeList.get(0));
        assertEquals(range(2, 3, Color.RED), attributesRangeList.get(1));
        assertEquals(range(5, 6, Color.BLUE), attributesRangeList.get(2));
    }
}
