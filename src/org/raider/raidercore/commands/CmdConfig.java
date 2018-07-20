package org.raider.raidercore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.raider.raidercore.RaiderCore;

public class CmdConfig implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!commandSender.hasPermission("raidercore.reload")){
            commandSender.sendMessage(ChatColor.RED + "No Permission.");
            return true;
        }

        RaiderCore.get().loadConfig();
        commandSender.sendMessage(ChatColor.GREEN + "RaiderCore config reloaded.");
        return true;
    }
}
