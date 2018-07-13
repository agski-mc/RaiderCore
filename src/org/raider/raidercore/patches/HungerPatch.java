package org.raider.raidercore.patches;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HungerPatch implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onHungerChange(FoodLevelChangeEvent event){
        if(event.isCancelled()) return;
        event.setCancelled(true);
    }
}
