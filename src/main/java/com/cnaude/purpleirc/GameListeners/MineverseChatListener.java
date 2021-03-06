package com.cnaude.purpleirc.GameListeners;

import com.cnaude.purpleirc.MvChatMessage;
import com.cnaude.purpleirc.PurpleBot;
import com.cnaude.purpleirc.PurpleIRC;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.IOException;

public class MineverseChatListener
        implements Listener
{
    final private PurpleIRC plugin;

    public MineverseChatListener(PurpleIRC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void receievePluginMessage(PluginMessageEvent event) throws IOException
    {
        if (!event.getTag().equalsIgnoreCase("BungeeCord")) {
            return;
        }
        byte[] bytes = event.getData();

        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        MvChatMessage cm = new MvChatMessage(in);

        ProxiedPlayer player = null;
        cm.setMessage(ChatColor.translateAlternateColorCodes('&', cm.getMessage()));
        for (ServerInfo server : plugin.getProxy().getServers().values()) {
            for (ProxiedPlayer p : server.getPlayers()) {
                if (p.getName().equals(cm.getSender())) {
                    player = p;
                }
            }
        }
        if (player != null) {
            if (player.hasPermission("irc.message.gamechat")) {
                for (PurpleBot ircBot : plugin.ircBots.values()) {
                    ircBot.mvChat(player, cm);
                }
            }
        }
    }
}
