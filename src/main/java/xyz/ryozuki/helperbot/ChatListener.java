package xyz.ryozuki.helperbot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final HelperBot plugin;

    public ChatListener(HelperBot plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().hasPermission("helperbot.answer"))
            return;

        Player player = event.getPlayer();
        String msg = event.getMessage();

        Bukkit.getScheduler().runTaskLater(plugin, () ->
                plugin.getQuestions().forEach(question -> {
                    if (question.canAnswer(player) && question.matches(msg)) {
                        if (question.isBroadcasted()) {
                            plugin.getServer().broadcastMessage(getAnswerMessage(player, question));
                        } else {
                            player.sendMessage(getAnswerMessage(player, question));
                        }
                        if (!question.isBroadcastQuestion()) {
                            event.setCancelled(true);
                        }
                    }
                }), 10L);
    }

    private String getAnswerMessage(Player player, Question question) {
        return ChatColor.translateAlternateColorCodes('&',
                String.format("%s %s",
                        plugin.getBotName(),
                        question.getAnswer(player, plugin.isPlaceHolderApiEnabled())
                )
        );
    }
}
