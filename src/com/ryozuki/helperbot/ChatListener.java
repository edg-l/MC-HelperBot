package com.ryozuki.helperbot;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener implements Listener {
    Main plugin = null;

    public ChatListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) throws IOException{

        if(!event.getPlayer().hasPermission("helperbot.answer"))
            return;

        String msg = event.getMessage();

        boolean ignore = plugin.getIgnore();

        plugin.ReadQA().forEach((x, y) -> {
            Pattern pattern = Pattern.compile(x.toLowerCase(), Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(ignore ? msg.toLowerCase().replace("?", "") : msg.toLowerCase());
            if(matcher.find()) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
                        () -> plugin.getServer().broadcastMessage( ChatColor.translateAlternateColorCodes('&',
                                plugin.getColor() + plugin.getBotName() + y.toString())),
                        10L);

                return;
            }
        });


    }
}
