/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cnaude.purpleirc.Commands;

import com.cnaude.purpleirc.PurpleBot;
import com.cnaude.purpleirc.PurpleIRC;
import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 *
 * @author cnaude
 */
public class Send implements IRCCommandInterface {

    private final PurpleIRC plugin;
    private final String usage = "([bot]) ([channel]) [message]";
    private final String desc = "Send a message to an IRC channel.";
    private final String name = "send";
    private final String fullUsage = ChatColor.WHITE + "Usage: " + ChatColor.GOLD + "/irc " + name + " " + usage; 

    /**
     *
     * @param plugin
     */
    public Send(PurpleIRC plugin) {
        this.plugin = plugin;
    }

    /**
     *
     * @param sender
     * @param args
     */
    @Override
    public void dispatch(CommandSender sender, String[] args) {
        if (args.length >= 2) {
            int msgIdx = 1;
            String channelName = null;
            List<PurpleBot> myBots = new ArrayList<PurpleBot>();
            if (plugin.ircBots.containsKey(args[1])) {
                myBots.add(plugin.ircBots.get(args[1]));
                msgIdx = 2;
                if (args.length >= 3) {
                    if (plugin.ircBots.get(args[1]).isValidChannel(args[2])) {
                        channelName = args[2];
                    }
                }
            } else {
                myBots.addAll(plugin.ircBots.values());
            }
            for (PurpleBot ircBot : myBots) {
                String msg = "";
                for (int i = msgIdx; i < args.length; i++) {
                    msg = msg + " " + args[i];
                }
                if (channelName == null) {
                    for (String c : ircBot.botChannels) {
                        if (sender instanceof ProxiedPlayer) {
                            ircBot.gameChat((ProxiedPlayer) sender, c, msg.substring(1));
                        } else {
                            ircBot.consoleChat(c, msg.substring(1));
                        }
                    }
                } else {
                    if (sender instanceof ProxiedPlayer) {
                        ircBot.gameChat((ProxiedPlayer) sender, channelName, msg.substring(1));
                    } else {
                        ircBot.consoleChat(channelName, msg.substring(1));
                    }
                }

            }
        } else {
            sender.sendMessage(fullUsage);
        }
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String desc() {
        return desc;
    }

    @Override
    public String usage() {
        return usage;
    }
}