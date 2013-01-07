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

import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * User: dima
 * Date: Nov 30, 2008
 * Time: 4:08:09 PM
 */
public class TextAttributesRange {
    private final TextAttributes textAttributes;
    private final int from;
    private final int to;

    public TextAttributesRange(final RangeHighlighter rangeHighlighter) {
        this(rangeHighlighter.getTextAttributes(), rangeHighlighter.getStartOffset(), rangeHighlighter.getEndOffset());
    }

    public TextAttributesRange(final HighlighterIterator highlighterIterator) {
        this(highlighterIterator.getTextAttributes(), highlighterIterator.getStart(), highlighterIterator.getEnd());
    }

	/**
	 * @param textAttributes text attributes
	 * @param from starting point of range (inclusive)
	 * @param to   ending point of range (exclusive)
	 */
	public TextAttributesRange(final TextAttributes textAttributes, final int from, final int to) {
		this.textAttributes = textAttributes;
		this.from = from;
		this.to = to;
	}

	@Nullable
    public TextAttributes getTextAttributes() {
        return textAttributes;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public static List<TextAttributes> extractTextAttributes(final List<TextAttributesRange> attributesRangeList) {
        final List<TextAttributes> result = new ArrayList<TextAttributes>();
        for (TextAttributesRange textAttributesRange : attributesRangeList) {
            result.add(textAttributesRange.getTextAttributes());
        }
        return result;
    }

    @Override
    public String toString() {
        return "TextAttributesRange{" +
                "from=" + from +
                ", to=" + to +
                ", textAttributes=" + textAttributes +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final TextAttributesRange that = (TextAttributesRange) o;

        if (from != that.from) return false;
        if (to != that.to) return false;
        //noinspection RedundantIfStatement
        if (textAttributes != null ? !textAttributes.equals(that.textAttributes) : that.textAttributes != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = textAttributes != null ? textAttributes.hashCode() : 0;
        result = 31 * result + from;
        result = 31 * result + to;
        return result;
    }
}
