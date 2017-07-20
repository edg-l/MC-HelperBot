package com.ryozuki.helperbot;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener implements Listener {
    Main plugin = null;

    public ChatListener(Main plugin) {
        this.plugin = plugin;
    }
	public HashMap<String, BukkitRunnable> task = new HashMap<>();
	public HashMap<String, Double> cooldown = new HashMap<>();

    @SuppressWarnings("unused")
	@EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) throws IOException{
        String msg = e.getMessage();
        Player p = e.getPlayer();
        Location ploc = p.getLocation();
        GameMode gm = p.getGameMode();
        ConsoleCommandSender cs = Bukkit.getConsoleSender();
        
        if(cooldown.containsKey(p.getName())){
			p.sendMessage(plugin.getConfig().getString("Cooldown_Message").replace("&", "§").replace("{cooldown}", cooldown.get(p.getName());
			e.setCancelled(true);
			return;
		}
        
        boolean ignore = true;

        plugin.ReadQA().forEach((x, y) -> {
            Pattern pattern = Pattern.compile(x.toLowerCase(), Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(ignore ? msg.toLowerCase().replace("?", "") : msg.toLowerCase());
            
            String finalY;
            finalY = y.toString().replace("{player}", p.getName())
            		.replace("{playerX}", ploc.getX() + "")
            		.replace("{playerY}", ploc.getY() + "")
            		.replace("{playerZ}", ploc.getZ() + "")
            		.replace("{playerWorld}", p.getWorld().getName())
            		.replace("{playerXP}", p.getExp() + "")
            		.replace("{playerHP}", p.getHealth() + "")
            		.replace("{gamemode}", gm.toString());
	    	
            if(matcher.find()) {
            	Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            		@Override
        			public void run() {
            			if (y.contains("playercmd>")) {
            		        String[] Y = y.split(">");
            		        String y1 = Y[0];
            		        String y2 = Y[1];
            		    	Bukkit.dispatchCommand(p, y2.toString());
            		    } else if (y.contains("consolecmd>")) {
            		        String[] Y = y.split(">");
            		        String y1 = Y[0];
            		        String y2 = Y[1];
            		    	Bukkit.dispatchCommand(p, y2.toString());
            		    } else if (y.contains("privatemsg>")) {
            		        String[] Y = finalY.split(">");
            		        String finalY = Y[0];
            		        String finalY1 = Y[1];
            		    	p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getBotName() + finalY1.toString()));
            		    } else {
            		    	Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.getBotName() + finalY.toString()));
            		    }
            		}
            	}, 10L);
				cooldown.put(p.getName(), plugin.getConfig().getInt("Cooldown");
				task.put(p.getName(), new BukkitRunnable() {

					@Override
					public void run() {
						if(cooldown.get(p.getName()) != 0.0){
							cooldown.put(p.getName(), cooldown.get(p.getName()) - 0.5);
						}else{
							cooldown.remove(p.getName());
							task.remove(p.getName());
							this.cancel();
						}
					}
				});
				
				task.get(p.getName()).runTaskTimer(plugin, 10, 10);
                return;
            }
        });
    }
}
