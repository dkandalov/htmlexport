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
import java.util.ArrayList;

/**
 * User: dima
 * Date: Dec 2, 2008
 * Time: 1:44:24 PM
 */
public class TextAttributesMerger_IntervalSplitting_Test {
    private static final int LONGER_RANGE_IN_TESTS = 100;
    private java.util.List<TextAttributesRange> initialRanges;
    private int colorIndex;

    /**
     * <pre>
     * |----|
     * |---------|
     *   |----|
     * </pre>
     */
    @Test
    public void shouldMergeNextRangeAfterMergingTwoOverlappingRanges() {
        ranges(
                0, 3,
                0, 6,
                2, 5,
                4, 100 // additional range to expose use case
        );
        shouldMergeInto(
                0, 2,
                2, 4,
                4, 100
        );
    }

    /**
     * <pre>
     * |--|
     * |-----|
     *    |--|
     * </pre>
     */
    @Test
    public void shouldMergeNextRangeAfterMergingTwoOverlappingRanges2() {
        ranges(
                0, 3,
                0, 4,
                3, 4,
                4, 100 // additional range to expose use case
        );
        shouldMergeInto(
                0, 3,
                3, 4,
                4, 100
        );
    }

    /**
     * <pre>
     * |-----|
     * |---|
     * </pre>
     */
    @Test
    public void shouldSplitIntoTwoWhenRangesHaveTheSameStartPoint() {
        ranges(
                0, 5,
                0, 3
        );
        shouldMergeInto(
                0, 3,
                3, 5
        );
    }

    /**
     * <pre>
     * |-------|
     * |-----|
     * </pre>
     */
    @Test
    public void shouldSplitIntoTwoWhenRangesHaveTheSameStartPoint2() {
        ranges(
                0, 7,
                0, 5
        );
        shouldMergeInto(
                0, 5,
                5, 7
        );
    }

    /**
     * <pre>
     * |---|
     * |---|
     * </pre>
     */
    @Test
    public void shouldMergeRangesWithSameInterval() {
        ranges(
                0, 3,
                0, 3
        );
        shouldMergeInto(
                0, 3
        );
    }

    /**
     * <pre>
     * |-----|
     *   |---|
     * </pre>
     */
    @Test
    public void shouldSplitRangesIntoTwoWhenOneOverlapesAnotherAndTheyHaveTheSameEndPoint() {
        ranges(
                0, 5,
                2, 5
        );
        shouldMergeInto(
                0, 2,
                2, 5
        );
    }

    /**
     * <pre>
     * |--------|
     *   |---|
     * </pre>
     */
    @Test
    public void shouldSplitRangesWhenOneIsContainedWithinAnother() {
        ranges(
                0, 5,
                1, 3
        );
        shouldMergeInto(
                0, 1,
                1, 3,
                3, 5
        );
    }

    /**
     * <pre>
     * |---|
     *   |---|
     * </pre>
     */
    @Test
    public void shouldSplitIntoThreeRangesWhenOneOverlapesAnother() {
        ranges(
                0, 2,
                1, 3
        );
        shouldMergeInto(
                0, 1,
                1, 3
        );
    }

    /**
     * <pre>
     * |---|
     *     |---|
     * </pre>
     */
    @Test
    public void shouldLeaveRangesAsTheyAreWhenTheyDoNotOverlap() {
        ranges(
                0, 2,
                2, 4
        );
        shouldMergeInto(
                0, 2,
                2, 4
        );
    }

    private void shouldMergeInto(int... rangePoints) {
        if (rangePoints.length % 2 != 0) throw new IllegalArgumentException();

        TextAttributesMerger merger = new TextAttributesMerger(initialRanges, createStubCode());
        java.util.List<TextAttributesRange> mergedRangeList = merger.merge();

        assertEquals("should have correct length", rangePoints.length / 2, mergedRangeList.size());

        for (int i = 0; i < rangePoints.length / 2; i++) {
            int from = rangePoints[i * 2];
            int to = rangePoints[(i * 2) + 1];
            TextAttributesRange actual = mergedRangeList.get(i);
            Color actualColor = actual.getTextAttributes().getForegroundColor();
            // take color from actual value so that to make assertion "color blind" and focused on ranges
            TextAttributesRange expected = range(from, to, actualColor, Font.PLAIN);
            assertEquals(expected, actual);
        }
    }

    private static String createStubCode() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < LONGER_RANGE_IN_TESTS; i++) {
            result.append(" ");
        }
        return result.toString();
    }

    private void ranges(int... rangePoints) {
        if (rangePoints.length % 2 != 0) throw new IllegalArgumentException();

        initialRanges = new ArrayList<TextAttributesRange>();

        for (int i = 0; i < rangePoints.length / 2; i++) {
            int from = rangePoints[i * 2];
            int to = rangePoints[(i * 2) + 1];
            initialRanges.add(range(from, to, nextColor(), Font.PLAIN));
        }
    }

    private Color nextColor() {
        colorIndex++;
        return new Color(colorIndex, 0, 0);
    }
}