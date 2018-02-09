/*
 * MIT License
 *
 * Copyright (c) 2018 Duncan Sterken
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.duncte123.botAudioManager.config;

import com.afollestad.ason.Ason;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.commons.text.translate.UnicodeUnescaper;

import java.io.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ConfigLoader {

    /**
     * This will attempt to load the config and create it if it is not there
     *
     * @param file the file to load
     * @return the loaded config
     * @throws Exception if something goes wrong
     */
    public static Config getConfig(final File file) throws Exception {
        if (!file.exists()) {
            file.createNewFile();
            final FileWriter writer = new FileWriter(file);
            writer.write("{}");
            writer.close();
        }
        return new MainConfig(file);
    }

    public static class MainConfig extends Config {

        private final File configFile;

        MainConfig(final File file) throws Exception {
            super(null, new Ason(Files.toString(file, Charsets.UTF_8)));
            this.configFile = file;
        }

        @Override
        public File getConfigFile() {
            return this.configFile;
        }

        @Override
        public void save() throws Exception {
            try {
                final BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(this.configFile), "UTF-8"));
                new UnicodeUnescaper().translate(
                        this.config.toString(4), writer);
                writer.close();
            } catch (final IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
    }
}