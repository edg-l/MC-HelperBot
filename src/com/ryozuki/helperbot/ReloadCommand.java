package com.ryozuki.helperbot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class ReloadCommand implements CommandExecutor {
    private Main plugin;

    ReloadCommand(Main plugin) {
        this.plugin = plugin;
    }
    ConsoleCommandSender cs = Bukkit.getConsoleSender();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        plugin.reloadConfig();
        plugin.saveConfig();
        plugin.setConfig(plugin.getConfig());

        if(commandSender instanceof Player) {
            commandSender.sendMessage(ChatColor.GREEN + "[HelperBot] Successfully reloaded");
        }
        else {
            cs.sendMessage(ChatColor.GREEN + "[HelperBot] Successfully reloaded");
        }

        return true;
    }
}
