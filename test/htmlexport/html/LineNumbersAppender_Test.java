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
import static org.junit.Assert.*;
import org.junit.Test;

import java.awt.*;

import htmlexport.html.attributeappenders.AttributeAppender;

/**
 * User: dima
 * Date: Dec 28, 2008
 * Time: 1:55:08 PM
 */
public class LineNumbersAppender_Test {
    @Test
    public void shouldInsertLineNumbersIntoOutput() {
        // setup
        OutputAppender stubOutputAppender = new OutputAppender() {
            @Override
            public void writeTo(final StringBuilder output) {
                output.append(
                        "aaa\n" +
                        "    bbb\n" +
                        "        ccc");
            }
        };
	    final TextAttributes lineNumbersAttributes = new TextAttributes(Color.CYAN, null, null, null, 0);
	    final AttributeAppender dummyAttributeAppender = new AttributeAppender() {
		    @Override
		    public void appendAttributeTo(final StringBuilder output, final TextAttributes textAttributes) {}
	    };
	    LineNumbersAppender appender = new LineNumbersAppender(stubOutputAppender, lineNumbersAttributes, dummyAttributeAppender, 1);

        // exercise
        StringBuilder output = new StringBuilder();
        appender.writeTo(output);

        // verify
        assertEquals(
                "<span>1    </span>aaa\n" +
                "<span>2    </span>    bbb\n" +
                "<span>3    </span>        ccc",
                output.toString());
    }

}
