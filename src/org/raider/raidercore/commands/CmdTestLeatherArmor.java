package org.raider.raidercore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.raider.raidercore.functions.LeatherArmorTest;
import org.raider.raidercore.utils.ArmorType;

public class CmdTestLeatherArmor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!commandSender.isOp()){
            commandSender.sendMessage("denied");
            return true;
        }

        if(!(commandSender instanceof Player)){
            commandSender.sendMessage("denied");
            return true;
        }

        if(strings.length == 0){
            commandSender.sendMessage("denied");
            return true;
        }

        if(ArmorType.fromString(strings[0]) == null){
            commandSender.sendMessage("denied");
            return true;
        }

        Player player = (Player)commandSender;
        ArmorType type = ArmorType.fromString(strings[0]);
        LeatherArmorTest.getArmor(player,type);
        commandSender.sendMessage("success");
        return true;
    }
}
