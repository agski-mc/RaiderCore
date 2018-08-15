package org.raider.raidercore.listeners;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class XpBottleListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event){
        if(event.isCancelled())return;
        if(event.getPlayer().getItemInHand().getType() == Material.EXP_BOTTLE){
            ItemStack item = event.getPlayer().getItemInHand();
            if(item.getItemMeta().hasLore() && item.getItemMeta().getLore().size() == 2){
                net.minecraft.server.v1_8_R3.ItemStack netItem = CraftItemStack.asNMSCopy(item);
                if(!netItem.hasTag()){
                    return;
                }

                NBTTagCompound nbt = netItem.getTag();
                if(!nbt.hasKey("xpamount")){
                    return;
                }

                event.setCancelled(true);
                int amount = nbt.getInt("xpamount");
                if(item.getAmount() == 1){
                    event.getPlayer().setItemInHand(null);
                }else if(item.getAmount() != 0){
                    event.getPlayer().getItemInHand().setAmount(item.getAmount()-1);
                }

                event.getPlayer().setLevel(event.getPlayer().getLevel() + amount);
                event.getPlayer().sendMessage(ChatColor.GREEN + "You have been given " + amount + " levels from an xp bottle.");
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onThrowEvent(ProjectileLaunchEvent event){
        if(event.getEntity() instanceof ThrownExpBottle){
            Projectile proj = event.getEntity();
            if(proj.getShooter() instanceof Player){
                Player player = (Player)proj.getShooter();
                ItemStack item = player.getItemInHand();
                if(item.getItemMeta().hasLore() && item.getItemMeta().getLore().size() == 2){
                    net.minecraft.server.v1_8_R3.ItemStack netItem = CraftItemStack.asNMSCopy(item);
                    if(!netItem.hasTag()){
                        return;
                    }

                    NBTTagCompound nbt = netItem.getTag();
                    if(!nbt.hasKey("xpamount")){
                        return;
                    }

                    event.setCancelled(true);
                    int amount = nbt.getInt("xpamount");
                    if(item.getAmount() == 1){
                        player.setItemInHand(null);
                    }else if(item.getAmount() != 0){
                        player.getItemInHand().setAmount(item.getAmount()-1);
                    }

                    player.setLevel(player.getLevel() + amount);
                    player.sendMessage(ChatColor.GREEN + "You have been given " + amount + " levels from an xp bottle.");
                }
            }

        }
    }
}