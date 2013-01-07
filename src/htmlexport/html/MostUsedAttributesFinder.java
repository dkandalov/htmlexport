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

import htmlexport.common.TextAttributesRange;

import java.util.*;

import com.intellij.openapi.editor.markup.TextAttributes;
import org.jetbrains.annotations.Nullable;

/**
 * User: dima
 * Date: Jan 4, 2009
 * Time: 9:14:11 PM
 */
class MostUsedAttributesFinder {
    private final List<TextAttributesRange> attributesRangeList;

    @Nullable
    public static TextAttributes findAndRemoveFrom(List<TextAttributesRange> attributesRangeList) {
        MostUsedAttributesFinder finder = new MostUsedAttributesFinder(attributesRangeList);
        TextAttributes mostUsedAttributes = finder.findMostUsedAttributesRange();
        finder.removeAttributesRangeFrom(mostUsedAttributes);

        return mostUsedAttributes;
    }

    private MostUsedAttributesFinder(List<TextAttributesRange> attributesRangeList) {
        this.attributesRangeList = attributesRangeList;
    }

    @Nullable
    private TextAttributes findMostUsedAttributesRange() {
        Map<TextAttributes, Integer> rangesUsage = new HashMap<TextAttributes, Integer>();
        for (TextAttributesRange textAttributesRange : attributesRangeList) {
            TextAttributes textAttributes = textAttributesRange.getTextAttributes();

            // skip styles with some background color, because background will affect new line characters
            if (textAttributes.getBackgroundColor() != null) continue;

            if (!rangesUsage.containsKey(textAttributes)) {
                rangesUsage.put(textAttributes, 0);
            }
            Integer timesUsed = rangesUsage.get(textAttributes);
            rangesUsage.put(textAttributes, timesUsed + 1);
        }

        TextAttributes result = null;
        int maxUsage = 0;
        for (Map.Entry<TextAttributes, Integer> entry : rangesUsage.entrySet()) {
            if (entry.getValue() > maxUsage) {
                maxUsage = entry.getValue();
                result = entry.getKey();
            }
        }
        return result;
    }

    private void removeAttributesRangeFrom(@Nullable TextAttributes mostUsedAttributes) {
        if (mostUsedAttributes == null) return;
        
        Iterator<TextAttributesRange> i = attributesRangeList.iterator();
        while (i.hasNext()) {
            TextAttributesRange textAttributesRange = i.next();
            if (mostUsedAttributes.equals(textAttributesRange.getTextAttributes())) {
                i.remove();
            }
        }
    }
}
