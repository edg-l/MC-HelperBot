package xyz.ryozuki.helperbot;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class CommandHandler implements CommandExecutor {
    private HelperBot plugin;
    static String[] commands = new String[] {"setname", "reload"};

    CommandHandler(HelperBot plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length < 1) {
            sender.sendMessage("§aSubcommands: " + String.join(", ", commands));
            return true;
        }
        String subcmd = args[0];
        args = (String[]) ArrayUtils.remove(args, 0);

        String noPermissionText = "§4You don't have permission to execute this command.§r";

        switch (subcmd) {
            case "setname": {
                if(!sender.hasPermission("helperbot.setname")) {
                    sender.sendMessage(noPermissionText);
                    break;
                }
                if(args.length < 1) {
                    sender.sendMessage("§cMissing argument <name>");
                    break;
                }
                plugin.setBotName(String.join(" ", args));
                sender.sendMessage(
                        String.format(
                                "§aBot name succesfully set to:§r '%s§r'",
                                ChatColor.translateAlternateColorCodes('&',
                                        plugin.getBotName())
                        )
                );
                break;
            }
            case "reload": {
                if(!sender.hasPermission("helperbot.reload")) {
                    sender.sendMessage(noPermissionText);
                    break;
                }
                plugin.reloadConfig();
                plugin.reloadQuestions();
                sender.sendMessage("§aSuccesfully reloaded.§r");
                break;
            }
            default: {
                sender.sendMessage("§cUnknown command. Available commands: " + String.join(", ", commands));
            }

        }
        return true;
    }
}

