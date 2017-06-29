package com.ryozuki.helperbot;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BotNameCommand implements CommandExecutor {
    private Main main = null;

    BotNameCommand(Main plugin) {
        this.main = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length < 1) {
            commandSender.sendMessage(ChatColor.RED + "Usage: /setbotname <name>");
            return false;
        }
        String newName = String.join(" ", strings).replace("\"", "");
        main.setBotName(newName);
        commandSender.sendMessage(ChatColor.BOLD.toString() + ChatColor.GREEN + "Succesfully set the name to: \""
                + newName + "\"");
        return true;
    }
}

