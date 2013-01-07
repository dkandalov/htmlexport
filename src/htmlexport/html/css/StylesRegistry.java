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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: dima
 * Date: Nov 30, 2008
 * Time: 6:03:13 PM
 */
public class StylesRegistry {
    private final Map<TextAttributes, CssClass> attributesToCssMap = new HashMap<TextAttributes, CssClass>();
	private final CssNamesGenerator cssNamesGenerator = new CssNamesGenerator();

	@NotNull
    public static StylesRegistry createFromTextAttributes(final List<TextAttributes> attributesList) {
        return new StylesRegistry(attributesList);
    }

    private StylesRegistry(final List<TextAttributes> attributesList) {
        for (TextAttributes textAttributes : attributesList) {
	        registerTextAttributes(textAttributes);
        }
    }

	public void registerTextAttributes(final TextAttributes textAttributes) {
		if (attributesToCssMap.containsKey(textAttributes)) return;

		String cssClassName = cssNamesGenerator.generateCssClassName();
		CssClass cssClass = new CssClass(cssClassName, textAttributes);
		attributesToCssMap.put(textAttributes, cssClass);
	}

	@NotNull
    public String getCssClassForTextAttributes(final TextAttributes textAttributes) {
        CssClass cssClass = attributesToCssMap.get(textAttributes);
        assert cssClass != null;
        return cssClass.getName();
    }

	@NotNull
    public List<String> getStyleDeclarations() {
        final List<String> result = new ArrayList<String>();
        for (CssClass cssClass : attributesToCssMap.values()) {
            result.add(cssClass.getDeclaration());
        }
        return result;
    }

	private static class CssNamesGenerator {
        private int counter;

        public String generateCssClassName() {
            counter++;
            return "s" + counter;
        }
    }

}
