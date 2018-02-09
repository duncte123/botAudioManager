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

package me.duncte123.botAudioManager.audioManagers;

import com.afollestad.ason.Ason;

import java.net.URI;
import java.net.URISyntaxException;

public class LavalinkNode {

    private final String wsUrl;
    private final String pass;

    public LavalinkNode(Ason ason) {
        this.wsUrl = ason.getString("wsurl");
        this.pass = ason.getString("pass");
    }

    public String getPass() {
        return pass;
    }

    public String getWsUrl() {
        return wsUrl;
    }

    public URI getWsURI() {
        try {
            return new URI(wsUrl);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
