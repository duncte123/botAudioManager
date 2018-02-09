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

import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import lavalink.client.player.IPlayer;
import lavalink.client.player.LavaplayerPlayerWrapper;
import net.dv8tion.jda.core.audio.AudioSendHandler;

public class AudioPlayerSenderHandler implements AudioSendHandler {

    /**
     * This is our audio player
     */
    private final IPlayer audioPlayer;

    /**
     * I don't know what this does but it seems important
     */
    private AudioFrame lastFrame;

    AudioPlayerSenderHandler(IPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    /**
     * Checks if the player can provide the song
     *
     * @return true if we can provide something
     */
    @Override
    public boolean canProvide() {
        LavaplayerPlayerWrapper lavaplayerPlayer = (LavaplayerPlayerWrapper) audioPlayer;
        if (lastFrame == null) {
            lastFrame = lavaplayerPlayer.provide();
        }
        return lastFrame != null;
    }

    /**
     * This <em>should</em> gives us our audio
     *
     * @return The audio in some nice bytes
     */
    @Override
    public byte[] provide20MsAudio() {
        LavaplayerPlayerWrapper lavaplayerPlayer = (LavaplayerPlayerWrapper) audioPlayer;
        if (lastFrame == null) {
            lastFrame = lavaplayerPlayer.provide();
        }

        byte[] data = lastFrame != null ? lastFrame.data : null;
        lastFrame = null;
        return data;
    }

    /**
     * "Checks" if this audio is opus
     *
     * @return always true
     */
    @Override
    public boolean isOpus() {
        return true;
    }
}
