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

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lavalink.client.player.IPlayer;
import lavalink.client.player.event.AudioEventAdapterWrapped;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TrackScheduler extends AudioEventAdapterWrapped {

    /**
     * This stores our queue
     */
    public final Queue<AudioTrack> queue;

    /**
     * Hey look at that, it's our player
     */
    private final IPlayer player;

    /**
     * This is the last playing track
     */
    private AudioTrack lastTrack;

    //private final GuildMusicManager guildMusicManager;

    /**
     * Are we repeating the track
     */
    private boolean repeating = false;


    /**
     * Are we repeating playlists
     */
    private boolean repeatPlayList = false;

    //private LavaplayerPlayerWrapper lavaplayerPlayer;

    /**
     * This instantiates our player
     *
     * @param player Our audio player
     */
    TrackScheduler(IPlayer player) {
        this.player = player;
        this.queue = new LinkedList<>();
        //this.guildMusicManager = guildMusicManager;
    }

    /**
     * Queue a track
     *
     * @param track The {@link AudioTrack AudioTrack} to queue
     */
    public void queue(AudioTrack track) {
        if(player.getPlayingTrack() != null) {
            queue.offer(track);
        } else {
            player.playTrack(track);
        }
    }

    /**
     * Starts the next track
     */
    public void nextTrack() {
        player.playTrack(queue.poll());
    }

    /**
     * Gets run when a track ends
     *
     * @param player    The {@link AudioPlayer AudioTrack} for that guild
     * @param track     The {@link AudioTrack AudioTrack} that ended
     * @param endReason Why did this track end?
     */
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        this.lastTrack = track;

        if (endReason.mayStartNext) {
            if (repeating) {
                if (!repeatPlayList) {
                    player.playTrack(lastTrack.makeClone());
                } else {
                    queue(lastTrack.makeClone());
                }
            } else {
                nextTrack();
            }
        }
    }

    /**
     * This will tell you if the player is repeating
     *
     * @return true if the player is set to repeat
     */
    public boolean isRepeating() {
        return repeating;
    }

    /**
     * This will tell you if the player is repeating playlists
     *
     * @return true if the player is set to repeat playlists
     */
    public boolean isRepeatingPlaylists() {
        return repeatPlayList;
    }

    /**
     * tell the player if needs to repeat
     *
     * @param repeating if the player needs to repeat
     */
    public void setRepeating(boolean repeating) {
        this.repeating = repeating;
    }

    /**
     * tell the player if needs to repeat playlists
     *
     * @param repeatingPlaylists if the player needs to repeat playlists
     */
    public void setRepeatingPlaylists(boolean repeatingPlaylists) {
        this.repeatPlayList = repeatingPlaylists;
    }

    /**
     * Shuffles the player
     */
    public void shuffle() {
        Collections.shuffle((List<?>) queue);
    }

}
