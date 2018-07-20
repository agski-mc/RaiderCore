package org.raider.raidercore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.raider.raidercore.RaiderCore;

import java.lang.reflect.Field;

public class CmdSettings implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!commandSender.hasPermission("raidercore.settings")){
            commandSender.sendMessage(ChatColor.RED + "No Permission.");
            return true;
        }
        try {
            for (Field f : RaiderCore.get().getClass().getDeclaredFields()) {
                if(f.getName().equals("i")) continue;
                f.setAccessible(true);
                commandSender.sendMessage(ChatColor.YELLOW + f.getName() + " : " + f.get(RaiderCore.get()).toString());
                f.setAccessible(false);
            }
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }

        return true;
    }
}
