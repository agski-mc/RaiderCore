package org.raider.raidercore.patches;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import static org.raider.raidercore.RaiderCore.getMobTargetting;
import static org.raider.raidercore.RaiderCore.getCreeperTargetting;

public class HostileMobsPatch implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onMobTarget(EntityTargetLivingEntityEvent event){
        if(event.isCancelled()) return;
        if(getCreeperTargetting() && event.getTarget() instanceof Player && event.getEntityType() == EntityType.CREEPER){
            event.setCancelled(true);
        }

        if(getMobTargetting() && event.getTarget() instanceof Player){
            event.setCancelled(true);
        }
    }
}
