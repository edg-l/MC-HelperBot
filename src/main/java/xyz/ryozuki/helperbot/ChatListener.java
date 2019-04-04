package xyz.ryozuki.helperbot;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener implements Listener {
    private HelperBot plugin;

    ChatListener(HelperBot plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().hasPermission("helperbot.answer"))
            return;

        String msg = event.getMessage().toLowerCase();

        boolean ignore = plugin.shouldIgnoreQuestionMark();

        plugin.getQuestions().forEach((x, y) -> {
            Pattern pattern = Pattern.compile(x.toLowerCase(), Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(ignore ? msg.replace("?", "") : msg);
            if (matcher.matches()) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
                        () -> plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                plugin.getBotName() + y)),
                        10L);
            }
        });


    }
}
