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

import com.intellij.openapi.editor.markup.TextAttributes;
import htmlexport.common.TextAttributesMerger;
import htmlexport.html.attributeappenders.AttributeAppender;

/**
 * User: dima
 * Date: Dec 27, 2008
 * Time: 11:12:37 PM
 */
class LineNumbersAppender implements OutputAppender {
    private final OutputAppender bodyAppender;
	private final TextAttributes lineNumbersAttributes;
	private final AttributeAppender attributeAppender;
	private int lineNumber;

	public LineNumbersAppender(final OutputAppender bodyAppender, final TextAttributes lineNumbersAttributes,
                               final AttributeAppender attributeAppender, final int firstLineNumber) {
        this.bodyAppender = bodyAppender;
	    this.lineNumbersAttributes = lineNumbersAttributes;
	    this.attributeAppender = attributeAppender;
		this.lineNumber = firstLineNumber;
	}

	@Override
    public void writeTo(final StringBuilder output) {
        StringBuilder tempOutput = new StringBuilder();
        bodyAppender.writeTo(tempOutput);

        tempOutput = addLineNumbers(tempOutput);

        output.append(tempOutput);
    }

    private StringBuilder addLineNumbers(final StringBuilder output) {
        output.insert(0, getNextLineNumber());
        
        int lastIndex = 0;
        while (true) {
            int i = output.indexOf(TextAttributesMerger.LINE_SEPARATOR, lastIndex);
            if (i == -1) break;

	        CharSequence text = getNextLineNumber();
            output.insert(i + 1, text);
            lastIndex = i + 1 + text.length();
        }
        return output;
    }

    private CharSequence getNextLineNumber() {
	    String lineNumberAsString = String.format("%-5d", lineNumber);
	    lineNumber++;

	    StringBuilder text = new StringBuilder();
	    text.append("<span");
	    attributeAppender.appendAttributeTo(text, lineNumbersAttributes);
	    text.append(">");
	    text.append(lineNumberAsString);
	    text.append("</span>");

	    return text;
    }
}
