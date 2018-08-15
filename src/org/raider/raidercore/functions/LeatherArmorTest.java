package org.raider.raidercore.functions;

import net.minecraft.server.v1_8_R3.ItemArmor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.raider.raidercore.utils.ArmorType;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class LeatherArmorTest implements Listener {
    public static void getArmor(Player player, ArmorType type){
        ItemStack is = new ItemStack(type.getMaterial() );
        net.minecraft.server.v1_8_R3.ItemStack netItem = CraftItemStack.asNMSCopy(is);
        int amt;
        if(type.getMaterial() == Material.LEATHER_HELMET){
            amt = 3;
        }else if(type.getMaterial() == Material.LEATHER_CHESTPLATE){
            amt = 8;
        }else if(type.getMaterial() == Material.LEATHER_LEGGINGS){
            amt = 3;
        }else{
            amt = 6;
        }

        //find the right field cause this is not it
        ItemArmor armor = (ItemArmor)netItem.getItem();
        //fix the reflection logic for removing the final field, GOOGLE!
        try {
            Field field = armor.getClass().getDeclaredField("c");
            field.setAccessible(true);
            field.set(armor, 20);
            field.setAccessible(false);
        }catch(NoSuchFieldException | IllegalAccessException e){
            e.printStackTrace();
        }
        //just making sure it set!
        netItem.setItem(armor);
        System.out.println(((ItemArmor)netItem.getItem()).c);
        is = CraftItemStack.asBukkitCopy(netItem);
        player.getInventory().addItem(is);
    }

    
}
