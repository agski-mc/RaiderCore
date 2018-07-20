package org.raider.raidercore.patches;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.ps.PS;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.raider.raidercore.RaiderCore;

public class NoTNTPatch implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event){
        if(event.isCancelled()) return;
        if(RaiderCore.isExplosionsDisabled()){
            event.setCancelled(true);
            return;
        }
        if(event.getEntity() instanceof TNTPrimed) {
            ApplicableRegionSet pr = WorldGuardPlugin.inst().getRegionManager(event.getLocation().getWorld()).getApplicableRegions(event.getLocation());
            if (pr.getRegions().stream().anyMatch(RaiderCore::disallowExplosion)) {
                Faction explodedIn = BoardColl.get().getFactionAt(PS.valueOf(event.getLocation()));
                TNTPrimed tnt = (TNTPrimed)event.getEntity();
                if(!tnt.getSource().isValid()) {
                    event.setCancelled(true);
                    return;
                }
                Faction sentFrom = BoardColl.get().getFactionAt(PS.valueOf(tnt.getSource().getLocation()));

                if(explodedIn != sentFrom && !explodedIn.isNone()){
                    event.setCancelled(true);
                }
            }
        }
    }
}
