package com.ryozuki.helperbot;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener implements Listener {
    Main plugin = null;

    public ChatListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        if(!event.getPlayer().hasPermission("helperbot.answer"))
            return;

        String msg = event.getMessage();

        boolean ignore = plugin.getIgnore();

        plugin.getQA().forEach((x, y) -> {
            Pattern pattern = Pattern.compile(x.toLowerCase());
            Matcher matcher = pattern.matcher(ignore ? msg.toLowerCase().replace("?", "") : msg.toLowerCase());

            if(matcher.matches()) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
                        () -> plugin.getServer().broadcastMessage( ChatColor.UNDERLINE.toString() + ChatColor.AQUA
                                + plugin.getBotName() + y.toString()),
                        10L);

                return;
            }
        });

    }
}
