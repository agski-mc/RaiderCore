package org.raider.raidercore.functions;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagString;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.raider.raidercore.utils.ArmorType;

public class LeatherArmorTest implements Listener {
    public static void getArmor(Player player, ArmorType type){
        ItemStack is = new ItemStack(type.getMaterial() );
        net.minecraft.server.v1_8_R3.ItemStack netItem = CraftItemStack.asNMSCopy(is);
        if(!netItem.hasTag()){
            netItem.setTag(new NBTTagCompound());
        }
        NBTTagCompound nbt = netItem.getTag();
        int amt;
        if(type.getMaterial() == Material.LEATHER_HELMET){
            amt = 3;
        }else if(type.getMaterial() == Material.LEATHER_CHESTPLATE){
            amt = 8;
        }else if(type.getMaterial() == Material.LEATHER_LEGGINGS){
            amt = 6;
        }else{
            amt = 3;
        }
        NBTTagList nbtList = new NBTTagList();
        NBTTagCompound toughness = new NBTTagCompound();
        toughness.set("AttributeName", new NBTTagString("generic.armorToughness"));
        toughness.set("Name", new NBTTagString("armorToughness"));
        toughness.set("Slot", new NBTTagString(type.getSlotName()));
        toughness.set("Amount", new NBTTagInt(amt));
        toughness.set("Operation", new NBTTagInt(0));
        toughness.set("UUIDLeast", new NBTTagInt(894654));
        toughness.set("UUIDMost", new NBTTagInt(2872));
        NBTTagCompound armor = new NBTTagCompound();
        armor.set("AttributeName", new NBTTagString("generic.armor"));
        armor.set("Name", new NBTTagString("armor"));
        armor.set("Slot", new NBTTagString(type.getSlotName()));
        armor.set("Amount", new NBTTagInt(amt));
        armor.set("Operation", new NBTTagInt(0));
        armor.set("UUIDLeast", new NBTTagInt(894654));
        armor.set("UUIDMost", new NBTTagInt(2872));
        nbtList.add(armor);
        nbtList.add(toughness);
        if(nbt.hasKey("AttributeModifiers")){
            NBTTagList current =(NBTTagList) nbt.get("AttributeModifiers");
            for(int i = 0; i < current.size(); i++){
                NBTTagCompound compound = current.get(i);
                if(compound.hasKey("Name") && compound.getString("Name").equals("armor")) continue;
                nbtList.add(compound);
            }

        }

        nbt.set("AttributeModifiers", nbtList);
        netItem.setTag(nbt);
        is = CraftItemStack.asBukkitCopy(netItem);
        ItemMeta im = is.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        is.setItemMeta(im);
        player.getInventory().addItem(is);
    }

    
}
