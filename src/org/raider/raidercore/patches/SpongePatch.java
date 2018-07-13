package org.raider.raidercore.patches;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import static org.raider.raidercore.RaiderCore.getSpongePatchRadius;

public class SpongePatch implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onBlockPhysics(BlockFromToEvent event){
        if(event.isCancelled()) return;
        Block block = event.getBlock();
        if (block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER || block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA) {
            for(int x = -getSpongePatchRadius(); x <= getSpongePatchRadius(); ++x) {
                for(int y = -getSpongePatchRadius(); y <= getSpongePatchRadius(); ++y) {
                    for(int z = -getSpongePatchRadius(); z <= getSpongePatchRadius(); ++z) {
                        Location loc = new Location(block.getWorld(), (double)(block.getX() + x), (double)(block.getY() + y), (double)(block.getZ() + z));
                        Block blocktotest = loc.getBlock();
                        if (blocktotest.getType() == Material.SPONGE) {
                            event.setCancelled(true);
                            block.setType(Material.AIR);
                            event.getToBlock().setType(Material.AIR);
                        }
                    }
                }
            }
        }
    }
}
