package org.raider.raidercore.patches;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Recipe;

import java.util.Iterator;

public class HopperCraftPatch{
    public static void patchHoppers(){
        Iterator<Recipe> recipeIterator = Bukkit.recipeIterator();
        Recipe recipe;
        while(recipeIterator.hasNext()){
            recipe = recipeIterator.next();
            if(recipe.getResult().getType() == Material.HOPPER){
                recipeIterator.remove();
            }
        }

    }
}
