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

import htmlexport.common.TextAttributesRange;
import static htmlexport.TestUtils.*;
import static org.junit.Assert.*;
import org.junit.Test;

import static java.util.Arrays.*;
import java.util.List;

/**
 * User: dima
 * Date: Dec 13, 2008
 * Time: 12:08:30 PM
 */
public class TextAttributesSelectionFilter_Test {
    @Test
    public void shouldShiftRangesAccordingToSelection() {
        // setup
        List<TextAttributesRange> rangeList = asList(
                range(3, 6)
        );

        // exercise
        TextAttributesFilter attributesFilter = new TextAttributesFilter(3, 6);
        List<TextAttributesRange> actualList = attributesFilter.removeAttributesNotInSelection(rangeList);

        // verify
        List<TextAttributesRange> expectedList = asList(range(0, 3));
        assertEquals(expectedList, actualList);
    }
    
    @Test
    public void shouldExcludeRangesOutsideOfSelection() {
        // setup
        List<TextAttributesRange> rangeList = asList(
                range(0, 3),
                range(3, 6),
                range(6, 10)
        );

        // exercise
        TextAttributesFilter attributesFilter = new TextAttributesFilter(3, 6);
        List<TextAttributesRange> actualList = attributesFilter.removeAttributesNotInSelection(rangeList);

        // verify
        List<TextAttributesRange> expectedList = asList(range(0, 3));
        assertEquals(expectedList, actualList);
    }
    
    @Test
    public void shouldCutRanges() {
        // setup
        List<TextAttributesRange> rangeList = asList(
                range(0, 5),
                range(4, 10),
                range(0, 10)
        );
        TextAttributesFilter attributesFilter = new TextAttributesFilter(3, 6);

        // exercise
        List<TextAttributesRange> actualList = attributesFilter.removeAttributesNotInSelection(rangeList);

        // verify
        List<TextAttributesRange> expectedList = asList(
                range(0, 2),
                range(1, 3),
                range(0, 3)
        );
        assertEquals(expectedList, actualList);
    }
}
