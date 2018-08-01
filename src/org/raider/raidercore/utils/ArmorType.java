package org.raider.raidercore.utils;

import org.bukkit.Material;

public enum ArmorType {
    BOOTS(Material.LEATHER_BOOTS, "boots", "feet"),
    LEGGINGS(Material.LEATHER_LEGGINGS, "leggings", "legs"),
    CHESTPLATE(Material.LEATHER_CHESTPLATE, "chestplate", "chest"),
    HELMET(Material.LEATHER_HELMET, "helmet", "head");
    private Material material;
    private String name;
    private String slotName;
    ArmorType(Material material, String name, String slotName){
        this.material = material;
        this.name = name;
        this.slotName = slotName;
    }

    public Material getMaterial(){
        return material;
    }

    public String getName(){
        return name;
    }

    public static ArmorType fromString(String str){
        for(ArmorType a : values()){
            if(a.getName().equalsIgnoreCase(str)){
                return a;
            }
        }
        return null;
    }

    public String getSlotName(){
        return slotName;
    }

}
