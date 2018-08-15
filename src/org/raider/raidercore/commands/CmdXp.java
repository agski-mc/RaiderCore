package org.raider.raidercore.commands;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CmdXp implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        boolean canXpSelf = commandSender.hasPermission("raidercore.cmdxp.self");

        if (!canXpSelf) {
            commandSender.sendMessage(ChatColor.RED + "No permission.");
            return true;
        }
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage(ChatColor.RED + "Only players may execute this command.");
            return true;
        }

        if (strings.length == 0) {
            Player player = (Player) commandSender;
            commandSender.sendMessage(ChatColor.AQUA + "You have " + player.getLevel() + " levels.");
            return true;
        }

        if (strings.length > 1) {
            commandSender.sendMessage(ChatColor.RED + "Usage: /xp <Amount>");
            return true;
        }
        Player player = (Player) commandSender;
        try {
            int amount = Integer.parseInt(strings[0]);
            if (player.getLevel() < amount) {
                commandSender.sendMessage(ChatColor.RED + "You do not have enough xp for this.");
                return true;
            }

            ItemStack bottle = getBottle(player, amount);
            Map<Integer, ItemStack> map = player.getInventory().addItem(bottle);
            for (ItemStack i : map.values()) {
                player.getLocation().getWorld().dropItemNaturally(player.getLocation(), i);
            }
            player.setLevel(player.getLevel() - amount);
            player.sendMessage(ChatColor.GREEN + "You have been given a xp bottle with " + amount + " levels.");
        } catch (NumberFormatException e) {
            commandSender.sendMessage(ChatColor.RED + "Argument 2 is invalid, must be a number.");
            return true;
        }
        return true;
    }


    private ItemStack getBottle(Player withdraw, int amount) {
        ItemStack itemstack = new ItemStack(Material.EXP_BOTTLE);
        ItemMeta im = itemstack.getItemMeta();
        List<String> lore = im.hasLore() ? im.getLore() : new ArrayList<>();
        lore.add(ChatColor.AQUA + "Withdrawn by: " + withdraw.getName());
        lore.add(ChatColor.YELLOW + "Amount: " + amount + " levels");
        im.setLore(lore);
        itemstack.setItemMeta(im);
        net.minecraft.server.v1_8_R3.ItemStack netItem = CraftItemStack.asNMSCopy(itemstack);
        NBTTagCompound nbt = netItem.hasTag() ? netItem.getTag() : new NBTTagCompound();
        nbt.set("xpamount", new NBTTagInt(amount));
        netItem.setTag(nbt);
        return CraftItemStack.asBukkitCopy(netItem);
    }
}
