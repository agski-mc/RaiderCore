package org.raider.raidercore.patches;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.material.Crops;
import org.raider.raidercore.RaiderCore;

public class CropDecayDisable implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onCropDecay(BlockPhysicsEvent event){
        if(event.isCancelled()) return;
        if(event.getBlock().getState().getData() instanceof Crops){
            ApplicableRegionSet regionset = WorldGuardPlugin.inst().getRegionManager(event.getBlock().getWorld()).getApplicableRegions(event.getBlock().getLocation());
            if(regionset.getRegions().stream().anyMatch(RaiderCore::disallowCropDecay)){
                event.setCancelled(true);
            }
        }
    }
}
