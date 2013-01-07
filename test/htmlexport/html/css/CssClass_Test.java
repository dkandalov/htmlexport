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
package htmlexport.html.css;

import com.intellij.openapi.editor.markup.TextAttributes;
import htmlexport.TestUtils;
import static org.junit.Assert.*;
import org.junit.Test;

import java.awt.*;

/**
 * User: dima
 * Date: Nov 30, 2008
 * Time: 6:44:53 PM
 */
public class CssClass_Test {
    @Test
    public void shouldBeConstructedFromTextAttributes() {
        TextAttributes textAttributes = TestUtils.textAttributes(null, null, Font.PLAIN);
        CssClass cssClass = new CssClass("s", textAttributes);
        assertEquals(".s{color:rgb(0,0,0);font-weight:normal;font-style:normal;}", cssClass.getDeclaration());

        textAttributes = TestUtils.textAttributes(Color.RED, Color.CYAN, Font.BOLD);
        cssClass = new CssClass("s", textAttributes);
        assertEquals(".s{color:rgb(255,0,0);background-color:rgb(0,255,255);font-weight:bold;font-style:normal;}", cssClass.getDeclaration());
    }
}
