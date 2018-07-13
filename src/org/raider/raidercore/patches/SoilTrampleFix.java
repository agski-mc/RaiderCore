package org.raider.raidercore.patches;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.raider.raidercore.RaiderCore;

public class SoilTrampleFix implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onSoilChange(BlockFadeEvent event){
        if(event.isCancelled()) return;
        ApplicableRegionSet pr = WorldGuardPlugin.inst().getRegionManager(event.getBlock().getWorld()).getApplicableRegions(event.getBlock().getLocation());
        if(event.getNewState().getType() == Material.SOIL && pr.getRegions().stream().anyMatch(RaiderCore::disallowSoilTrampling)){
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event){
        if(event.isCancelled()) return;
        ApplicableRegionSet pr = WorldGuardPlugin.inst().getRegionManager(event.getClickedBlock().getWorld()).getApplicableRegions(event.getClickedBlock().getLocation());
        if(event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.SOIL && pr.getRegions().stream().anyMatch(RaiderCore::disallowSoilTrampling)){
            event.setCancelled(true);
        }
    }
}
