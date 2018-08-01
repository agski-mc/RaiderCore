package org.raider.raidercore.commands;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;

public class CmdCheckAttributes implements CommandExecutor {
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

        Player player = (Player)commandSender;
        if(player.getItemInHand() == null){
            commandSender.sendMessage("denied");
            return true;
        }

        NBTTagCompound nbt = CraftItemStack.asNMSCopy(player.getItemInHand()).getTag();
        if(nbt == null){
            commandSender.sendMessage("denied");
            return true;
        }

        NBTTagList attributes = (NBTTagList)nbt.get("AttributeModifiers");
        if(attributes == null){
            commandSender.sendMessage("denied");
            return true;
        }

        for(int i = 0; i < attributes.size(); i++){
            NBTTagCompound a = attributes.get(i);
            commandSender.sendMessage(ChatColor.YELLOW + a.getString("AttributeName") + " " + a.getInt("Amount"));
        }
        commandSender.sendMessage("done");
        return true;
    }
}
