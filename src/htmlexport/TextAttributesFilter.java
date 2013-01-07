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

import java.util.List;
import java.util.ArrayList;

/**
 * User: dima
 * Date: Dec 13, 2008
 * Time: 12:05:20 PM
 */
class TextAttributesFilter {
    private final int selectionStart;
    private final int selectionEnd;
    private final int selectionLength;

    public TextAttributesFilter(final int selectionStart, final int selectionEnd) {
        this.selectionStart = selectionStart;
        this.selectionEnd = selectionEnd;
        this.selectionLength = selectionEnd - selectionStart;
    }

    public List<TextAttributesRange> removeAttributesNotInSelection(final List<TextAttributesRange> attributes) {
        List<TextAttributesRange> result = new ArrayList<TextAttributesRange>();

        for (TextAttributesRange attribute : attributes) {
            if (startPointWithinSelection(attribute) || endPointWithinSelection(attribute) || containsSelection(attribute)) {
                int from = attribute.getFrom() - selectionStart;
                int to = attribute.getTo() - selectionStart;
                if (from < 0) from = 0;
                if (to >= selectionLength) to = selectionLength;

                TextAttributesRange shiftedRange = new TextAttributesRange(
                        attribute.getTextAttributes(),
                        from,
                        to);
                result.add(shiftedRange);
            }
        }
        return result;
    }

    private boolean containsSelection(final TextAttributesRange attribute) {
        return (attribute.getFrom() <= selectionStart && attribute.getTo() > selectionEnd);
    }

    private boolean endPointWithinSelection(final TextAttributesRange attribute) {
        return (attribute.getTo() > selectionStart && attribute.getTo() < selectionEnd);
    }

    private boolean startPointWithinSelection(final TextAttributesRange attribute) {
        return (attribute.getFrom() >= selectionStart && attribute.getFrom() < selectionEnd);
    }
}
