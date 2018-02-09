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

import fredboat.audio.player.LavalinkManager;
import lavalink.client.player.IPlayer;
import net.dv8tion.jda.core.entities.Guild;

public class GuildMusicManager {

    /**
     * This is our player
     */
    public final IPlayer player;

    /**
     * This is the scheduler
     */
    public final TrackScheduler scheduler;

    /**
     * This is what actually sends the audio
     */
    private final AudioPlayerSenderHandler sendHandler;

    /**
     * Constructor
     *
     * @param g The guild that we wannt the manager for
     */
    public GuildMusicManager(Guild g) {
        player = LavalinkManager.ins.createPlayer(g.getId());
        scheduler = new TrackScheduler(player);
        sendHandler = new AudioPlayerSenderHandler(player);
        player.addListener(scheduler);
    }

    /**
     * This will get our sendings handler
     *
     * @return The {@link AudioPlayerSenderHandler thing} that sends our audio
     */
    public AudioPlayerSenderHandler getSendHandler() {
        return sendHandler;
    }
}
