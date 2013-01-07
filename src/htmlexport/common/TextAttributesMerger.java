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

import com.intellij.openapi.editor.markup.TextAttributes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * User: dima
 * Date: Nov 30, 2008
 * Time: 4:19:44 PM
 */
public class TextAttributesMerger {
    /**
     * This constant is used to make logical coupling between
     * {@link htmlexport.html.LineNumbersAppender} and this class more explicit.
     * <p/>
     * (It seems that IntelliJ converts line separators into "\n" on file loading,
     * therefore, OS-specific separators are not considered.)
     */
    public static final String LINE_SEPARATOR = "\n";

    private final List<TextAttributesRange> attributeRanges;
    private final String codeAsText;
    private final int firstRangeFrom;
    private final int lastRangeTo;

    public TextAttributesMerger(final List<TextAttributesRange> attributeRanges, final String codeAsText) {
        this.attributeRanges = attributeRanges;
        this.codeAsText = codeAsText;

        firstRangeFrom = findFirstRangeFrom();
        lastRangeTo = findLastRangeTo();
    }

    public List<TextAttributesRange> merge() {
        if (attributeRanges.isEmpty()) return Collections.emptyList();

        List<TextAttributes> granularAttributes = granularizeRangesAndMergeTextProperties();
        return glueAttributesIntoRanges(granularAttributes);
    }

    private List<TextAttributes> granularizeRangesAndMergeTextProperties() {
        List<TextAttributes> granularAttributes = new ArrayList<TextAttributes>(lastRangeTo - firstRangeFrom);
        for (int i = 0; i < lastRangeTo - firstRangeFrom; i++) {
            granularAttributes.add(null);
        }

        for (TextAttributesRange attributesRange : attributeRanges) {
            TextAttributes currentAttribute = attributesRange.getTextAttributes();
            for (int i = attributesRange.getFrom() - firstRangeFrom; i < attributesRange.getTo() - firstRangeFrom; i++) {
                if (granularAttributes.get(i) != null) {
                    TextAttributes mergedAttribute = mergeTextAttributes(granularAttributes.get(i), currentAttribute);
                    granularAttributes.set(i, mergedAttribute);
                } else {
                    granularAttributes.set(i, currentAttribute);
                }
            }
        }

        return granularAttributes;
    }

    private List<TextAttributesRange> glueAttributesIntoRanges(List<TextAttributes> granularAttributes) {
        return new GlueAttributesIntoRanges(granularAttributes, firstRangeFrom, codeAsText).invoke();
    }

    private int findFirstRangeFrom() {
        int result = Integer.MAX_VALUE;
        for (TextAttributesRange attribute : attributeRanges) {
            if (attribute.getFrom() < result) {
                result = attribute.getFrom();
            }
        }
        return result;
    }

    private int findLastRangeTo() {
        int result = 0;
        for (TextAttributesRange attribute : attributeRanges) {
            if (attribute.getTo() > result) {
                result = attribute.getTo();
            }
        }
        return result;
    }

    @NotNull
    private static TextAttributes mergeTextAttributes(final TextAttributes attrs1, final TextAttributes attrs2) {
        return TextAttributes.merge(attrs1, attrs2);
    }

    private static class GlueAttributesIntoRanges {
        private final int firstRangeFrom;
        private final String codeAsText;
        private final List<TextAttributes> granularAttributes;
        private final List<TextAttributesRange> resultRanges;

        public GlueAttributesIntoRanges(List<TextAttributes> granularAttributes, int firstRangeFrom, String codeAsText) {
            this.granularAttributes = granularAttributes;
            this.firstRangeFrom = firstRangeFrom;
            this.codeAsText = codeAsText;

            resultRanges = new ArrayList<TextAttributesRange>();
        }

        public List<TextAttributesRange> invoke() {
            TextAttributes lastAttributes = null;
            int rangeFrom = 0;

            granularAttributes.add(null);
            for (int i = 0; i < granularAttributes.size(); i++) {
                TextAttributes attributes = granularAttributes.get(i);

                if (lastAttributes == null || lastAttributeCoversLineSeparator(i - 1)) {
                    // use -1 to exclude line separator from style
                    addRange(rangeFrom, i - 1, lastAttributes);
                    rangeFrom = i;
                } else if (!lastAttributes.equals(attributes)) {
                    addRange(rangeFrom, i, lastAttributes);
                    rangeFrom = i;
                }
                lastAttributes = attributes;
            }
            return resultRanges;
        }

        private boolean lastAttributeCoversLineSeparator(int i) {
            return codeAsText.substring(firstRangeFrom + i, firstRangeFrom + i + 1).equals(LINE_SEPARATOR);
        }

        private void addRange(int fromIndex, int toIndex, TextAttributes textAttributes) {
            if (fromIndex < toIndex) { // condition to skip empty ranges (can happen if there are several line separators in a row)
                TextAttributesRange range = new TextAttributesRange(textAttributes, fromIndex + firstRangeFrom, toIndex + firstRangeFrom);
                resultRanges.add(range);
            }
        }
    }
}
