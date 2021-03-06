/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cnaude.purpleirc.Commands;

import com.cnaude.purpleirc.PurpleIRC;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import org.pircbotx.Channel;

/**
 *
 * @author cnaude
 */
public class Leave implements IRCCommandInterface {

    private final PurpleIRC plugin;
    private final String usage = "[bot] [channel]";
    private final String desc = "Leave IRC channel.";
    private final String name = "leave";
    private final String fullUsage = ChatColor.WHITE + "Usage: " + ChatColor.GOLD + "/irc " + name + " " + usage;

    /**
     *
     * @param plugin
     */
    public Leave(PurpleIRC plugin) {
        this.plugin = plugin;
    }

    /**
     *
     * @param sender
     * @param args
     */
    @Override
    public void dispatch(CommandSender sender, String[] args) {
        if (args.length >= 3) {
            String bot = args[1];
            String channelName = args[2];
            String reason = "";
            if (args.length >= 4) {
                for (int i = 3; i < args.length; i++) {
                    reason = reason + " " + args[i];
                }
            }
            if (plugin.ircBots.containsKey(bot)) {
                if (plugin.ircBots.get(bot).isConnected()) {
                    for (Channel channel : plugin.ircBots.get(bot).getChannels()) {
                        if (channel.getName().equalsIgnoreCase(channelName)) {
                            channel.send().part(reason);
                            sender.sendMessage(new TextComponent(ChatColor.WHITE + "Leaving " + channelName + "..."));
                            return;
                        }
                    }
                    sender.sendMessage(new TextComponent(ChatColor.WHITE + "Channel " + channelName + " is not valid."));
                } else {
                    sender.sendMessage(new TextComponent(ChatColor.RED + "Not connected."));
                }
            } else {
                sender.sendMessage(new TextComponent(plugin.invalidBotName.replace("%BOT%", bot)));
            }
        } else {
            sender.sendMessage(new TextComponent(fullUsage));
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
