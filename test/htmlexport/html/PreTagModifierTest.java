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

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import htmlexport.common.ExportOptions;
import htmlexport.common.ExportOptionsTestBuilder;

/**
 * User: dima
 * Date: Jan 26, 2009
 * Time: 12:43:22 AM
 */
public class PreTagModifierTest {
    @Test
    public void shouldAddBorderToPreTagWithAttributes() {
        String initialHtml = "<pre style=\"color:black;\">\nsome code\n</pre>";
        String expectedHtml = "<pre style=\"color:black;border:thin solid;padding:5px;\">\nsome code\n</pre>";

        ExportOptions exportOptions = new ExportOptionsTestBuilder().showBorder().build();
        PreTagModifier modifier = new PreTagModifier(exportOptions);
        String actual = modifier.applyTo(initialHtml);

        assertEquals(expectedHtml, actual);
    }

    @Test
    public void shouldAddBorderToPreTagWithoutAttributes() {
        String initialHtml = "<pre>\nsome code\n</pre>";
        String expectedHtml = "<pre style=\"border:thin solid;padding:5px;\">\nsome code\n</pre>";

        ExportOptions exportOptions = new ExportOptionsTestBuilder().showBorder().build();
        PreTagModifier modifier = new PreTagModifier(exportOptions);
        String actual = modifier.applyTo(initialHtml);

        assertEquals(expectedHtml, actual);
    }

    @Test
    public void shouldAddAttributesToPreTag() {
        String initialHtml = "<pre>\nsome code\n</pre>";
        String expectedHtml = "<pre style=\"font-family:monospace;\">\nsome code\n</pre>";

        ExportOptions exportOptions = new ExportOptionsTestBuilder().withPreTagAttributes("font-family:monospace;").build();
        PreTagModifier modifier = new PreTagModifier(exportOptions);
        String actual = modifier.applyTo(initialHtml);

        assertEquals(expectedHtml, actual);
    }

    @Test
    public void shouldPreservePreAttributesIfThereAreNoAttributesAndBorder() {
        String initialHtml = "<pre style=\"color:black;\">\nsome code\n</pre>";
        String expectedHtml = "<pre style=\"color:black;\">\nsome code\n</pre>";

        ExportOptions exportOptions = new ExportOptionsTestBuilder().withPreTagAttributes("").build();
        PreTagModifier modifier = new PreTagModifier(exportOptions);
        String actual = modifier.applyTo(initialHtml);

        assertEquals(expectedHtml, actual);
    }
    
    @Test
    public void shouldPreserveEmptyPreAttributesIfThereAreNoAttributesAndBorder() {
        String initialHtml = "<pre>\nsome code\n</pre>";
        String expectedHtml = "<pre>\nsome code\n</pre>";

        ExportOptions exportOptions = new ExportOptionsTestBuilder().withPreTagAttributes("").build();
        PreTagModifier modifier = new PreTagModifier(exportOptions);
        String actual = modifier.applyTo(initialHtml);

        assertEquals(expectedHtml, actual);
    }
}
