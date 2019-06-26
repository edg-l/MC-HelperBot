package xyz.ryozuki.helperbot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().hasPermission("helperbot.answer"))
            return;

        Player player = event.getPlayer();
        String msg = event.getMessage().toLowerCase();

        Bukkit.getScheduler().runTaskLater(HelperBot.getInstance(), () -> HelperBot.getInstance().getQuestions().forEach((question) -> {
            if (question.canAnswer(player) && question.matches(msg)) {
                if (question.isBroadcasted()) {
                    HelperBot.getInstance().getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            HelperBot.getInstance().getBotName() + " " + question.getAnswer(player)));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            HelperBot.getInstance().getBotName() + " " + question.getAnswer(player)));
                }
            }
        }), 10L);
    }
}
