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
package htmlexport.common;

import static htmlexport.TestUtils.*;
import static org.junit.Assert.*;
import org.junit.Test;

import java.awt.*;
import static java.util.Arrays.*;
import java.util.*;

/**
 * User: dima
 * Date: Dec 2, 2008
 * Time: 1:44:24 PM
 */
public class TextAttributesMerger_Test {
    private static final int LONGEST_RANGE_IN_TESTS = 100; // it's 100 "just in case"
    private static final Color NO_COLOR = null;

	@Test
    public void shouldProcessNotIntersectingTextAttributes() {
        TextAttributesMerger merger = new TextAttributesMerger(asList(
                range(0, 2, Color.BLACK), range(2, 4, Color.RED)
        ), createStubCode());
        java.util.List<TextAttributesRange> mergedRangeList = merger.merge();
        assertEquals(range(0, 2, Color.BLACK), mergedRangeList.get(0));
        assertEquals(range(2, 4, Color.RED), mergedRangeList.get(1));
    }
    
	@Test
    public void shouldProcessNotIntersectingTextAttributesWithGaps() {
        TextAttributesMerger merger = new TextAttributesMerger(asList(
                range(0, 2, Color.BLACK), range(2, 4, Color.RED)
        ), createStubCode());
        java.util.List<TextAttributesRange> mergedRangeList = merger.merge();
        assertEquals(range(0, 2, Color.BLACK), mergedRangeList.get(0));
        assertEquals(range(2, 4, Color.RED), mergedRangeList.get(1));
    }

	@Test
    public void shouldMergeTextAttributesForeground() {
        TextAttributesMerger merger = new TextAttributesMerger(asList(
                range(0, 2, NO_COLOR, Font.PLAIN),
                range(0, 2, Color.RED, Font.PLAIN)
        ), createStubCode());
        java.util.List<TextAttributesRange> mergedRangeList = merger.merge();
        assertEquals(range(0, 2, Color.RED, Font.PLAIN), mergedRangeList.get(0));
    }

    @Test
    public void shouldMergeTextAttributesBackground() {
        TextAttributesMerger merger = new TextAttributesMerger(asList(
                range(0, 2, NO_COLOR, NO_COLOR, Font.PLAIN),
                range(0, 2, Color.RED, Color.BLUE, Font.PLAIN)
        ), createStubCode());
        java.util.List<TextAttributesRange> mergedRangeList = merger.merge();
        assertEquals(range(0, 2, Color.RED, Color.BLUE, Font.PLAIN), mergedRangeList.get(0));
    }

    @Test
    public void shouldMergeTextAttributesFonts() {
        TextAttributesMerger merger = new TextAttributesMerger(asList(
                range(0, 2, NO_COLOR, Font.PLAIN),
                range(0, 2, NO_COLOR, Font.ITALIC)
        ), createStubCode());
        java.util.List<TextAttributesRange> mergedRangeList = merger.merge();
        assertEquals(range(0, 2, NO_COLOR, Font.ITALIC), mergedRangeList.get(0));
    }
    
    @Test
    public void shouldMergeTextAttributesFontsWhenAttributesAreInMiddleOfCode() {
        TextAttributesMerger merger = new TextAttributesMerger(asList(
                range(2, 5, NO_COLOR, Font.PLAIN),
                range(2, 5, NO_COLOR, Font.ITALIC)
        ), createStubCode());
        java.util.List<TextAttributesRange> mergedRangeList = merger.merge();
        assertEquals(range(2, 5, NO_COLOR, Font.ITALIC), mergedRangeList.get(0));
    }

    @Test
    public void shouldSplitRangesWhenRangeOverlapsLineSeparator() {
        String code = "-aa\nbbb-----";
        TextAttributesMerger merger = new TextAttributesMerger(asList(
                range(1, 7, NO_COLOR)
        ), code);

        java.util.List<TextAttributesRange> mergedRangeList = merger.merge();
        assertEquals(range(1, 3, NO_COLOR), mergedRangeList.get(0));
        assertEquals(range(4, 7, NO_COLOR), mergedRangeList.get(1));
    }

    @Test
    public void shouldSplitRangesWhenRangeOverlapsSeveralLineSeparators() {
        String code = "aaa\n\n\nbbb";
        TextAttributesMerger merger = new TextAttributesMerger(asList(
                range(0, 9, NO_COLOR)
        ), code);

        java.util.List<TextAttributesRange> mergedRangeList = merger.merge();
        assertEquals(range(0, 3, NO_COLOR), mergedRangeList.get(0));
        assertEquals(range(6, 9, NO_COLOR), mergedRangeList.get(1));
    }

    @Test
    public void shouldSplitRangesWhenRangeEndsOnLineSeparator() {
        String code = "aaa\nbbb";
        TextAttributesMerger merger = new TextAttributesMerger(asList(
                range(0, 4, NO_COLOR, Font.PLAIN),
                range(4, 7, NO_COLOR, Font.BOLD)
        ), code);

        java.util.List<TextAttributesRange> mergedRangeList = merger.merge();
        assertEquals(range(0, 3, NO_COLOR, Font.PLAIN), mergedRangeList.get(0));
        assertEquals(range(4, 7, NO_COLOR, Font.BOLD), mergedRangeList.get(1));
    }

    @Test
    public void shouldNotFailGivenEmptyRangeList() {
        TextAttributesMerger merger = new TextAttributesMerger(new ArrayList<TextAttributesRange>(), "");
        merger.merge();
    }

    private static String createStubCode() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < LONGEST_RANGE_IN_TESTS; i++) {
            result.append(" ");
        }
        return result.toString();
    }
}
