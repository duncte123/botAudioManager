/*
 * MIT License
 *
 * Copyright (c) 2017 Frederik Ar. Mikkelsen
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
 *
 */

package fredboat.audio.player;

import com.afollestad.ason.Ason;
import lavalink.client.io.Lavalink;
import lavalink.client.io.Link;
import lavalink.client.player.IPlayer;
import lavalink.client.player.LavaplayerPlayerWrapper;
import me.duncte123.botAudioManager.AudioUtilsClass;
import me.duncte123.botAudioManager.BotType;
import me.duncte123.botAudioManager.DuncteBotMainClass;
import me.duncte123.botAudioManager.audioManagers.LavalinkNode;
import me.duncte123.botAudioManager.config.Config;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


/**
 * This class has been taken from
 * https://github.com/Frederikam/FredBoat/blob/master/FredBoat/src/main/java/fredboat/audio/player/LavalinkManager.java\
 * and has been modified to fit my needs
 */
public class LavalinkManager {

    public static final LavalinkManager ins = new LavalinkManager();

    private DuncteBotMainClass mainClass = null;
    private Config config = null;
    private AudioUtilsClass audioUtilsClass;

    private LavalinkManager() {
    }

    private Lavalink lavalink = null;

    public void start(DuncteBotMainClass mainClass, Config config, AudioUtilsClass audioUtilsClass) {
        this.mainClass = mainClass;
        this.config = config;
        this.audioUtilsClass = audioUtilsClass;
        if (!isEnabled()) return;

        String userId = getIdFromToken(this.config.getString("discord.token"));

        lavalink = new Lavalink(
                userId,
                this.config.getInt("discord.totalShards", 1),
                shardId -> this.mainClass.getBotType().equals(BotType.SHARDMANAGER) ?
                        this.mainClass.getShardManager().getShardById(shardId) : this.mainClass.getFakeOneShard(shardId)
        );
        List<LavalinkNode> defaultNodes = new ArrayList<>();
        defaultNodes.add(new LavalinkNode(new Ason("{\"wsurl\": \"ws://localhost\",\"pass\": \"youshallnotpass\"}")));
        List<Ason> nodes = this.config.getArray("lavalink.nodes", defaultNodes);
        List<LavalinkNode> nodeList = new ArrayList<>();
        nodes.forEach(it -> nodeList.add(new LavalinkNode(it)));

        nodeList.forEach(it ->
                lavalink.addNode(it.getWsURI(), it.getPass())
        );

        /*try {
            lavalink.addNode(
                    new URI(AirUtils.config.getString("lavalink.wsurl", "ws://localhost")),
                    AirUtils.config.getString("lavalink.pass", "youshalnotpass")
            );
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }*/
    }

    public boolean isEnabled() {
        return this.config.getBoolean("lavalink.enable", false);
    }

    public IPlayer createPlayer(String guildId) {
        return isEnabled()
                ? lavalink.getLink(guildId).getPlayer()
                : new LavaplayerPlayerWrapper(audioUtilsClass.getInstance().getPlayerManager().createPlayer());
    }

    public void openConnection(VoiceChannel channel) {
        if (isEnabled()) {
            lavalink.getLink(channel.getGuild()).connect(channel);
        } else {
            channel.getGuild().getAudioManager().openAudioConnection(channel);
        }
    }

    public boolean isConnected(Guild g) {
        return isEnabled() ?
                lavalink.getLink(g).getState() == Link.State.CONNECTED :
                g.getAudioManager().isConnected();
    }

    public void closeConnection(Guild guild) {
        if (isEnabled()) {
            lavalink.getLink(guild).disconnect();
        } else {
            guild.getAudioManager().closeAudioConnection();
        }
    }

    public VoiceChannel getConnectedChannel(@Nonnull Guild guild) {
        //NOTE: never use the local audio manager, since the audio connection may be remote
        // there is also no reason to look the channel up remotely from lavalink, if we have access to a real guild
        // object here, since we can use the voice state of ourselves (and lavalink 1.x is buggy in keeping up with the
        // current voice channel if the bot is moved around in the client)
        return guild.getSelfMember().getVoiceState().getChannel();
    }

    public Lavalink getLavalink() {
        return lavalink;
    }

    /**
     * This is a simple util function that extracts the bot id from the token
     * @param token the token of your bot
     * @return the client id of the bot
     */
    private String getIdFromToken(String token) {

        return new String(
                Base64.getDecoder().decode(
                        token.split("\\.")[0]
                )
        );
    }
}