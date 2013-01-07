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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * User: dima
 * Date: Dec 7, 2008
 * Time: 3:23:23 PM
 */
class OutputWriter {
    private final String output;
    private final String path;
    private final String filename;

    public OutputWriter(final String output, final String path, final String filename) {
        this.output = output;
        this.path = path;
        this.filename = filename;
    }

    public void writeOutput() throws IOException {
        //noinspection ResultOfMethodCallIgnored
        new File(path).mkdirs();

        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(path + "/" + getExportFileName(), "rw");
            randomAccessFile.setLength(0);
            randomAccessFile.writeChars(output);
        } finally {
            try {
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException e) {
                // do nothing
            }
        }
    }

    private String getExportFileName() {
        return filename + ".html";
    }
}
