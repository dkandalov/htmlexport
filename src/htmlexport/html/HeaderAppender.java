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

import htmlexport.html.css.StylesRegistry;

import java.util.List;

/**
 * User: dima
 * Date: Dec 18, 2008
 * Time: 2:29:26 PM
 */
class HeaderAppender implements OutputAppender {
    private final String filename;
    private final StylesRegistry stylesRegistry;

    public HeaderAppender(final String filename, final StylesRegistry stylesRegistry) {
        this.filename = filename;
        this.stylesRegistry = stylesRegistry;
    }

    @Override
    public void writeTo(final StringBuilder output) {
        output.append(
                "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "<title>").append(filename).append("</title>\n" +
                        "<style type=\"text/css\">\n").append(styleDeclarations()).append("</style>\n" +
                        "</head>\n" +
                        "<body>\n");
    }

    private CharSequence styleDeclarations() {
        StringBuilder result = new StringBuilder();
        List<String> styles = stylesRegistry.getStyleDeclarations();
        for (String style : styles) {
            result.append(style).append("\n");
        }
        return result;
    }
}
