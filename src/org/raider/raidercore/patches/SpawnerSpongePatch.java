package org.raider.raidercore.patches;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import static org.raider.raidercore.RaiderCore.getSpawnerSpongeRadius;

public class SpawnerSpongePatch implements Listener {
    int radius = getSpawnerSpongeRadius() -2;
    @EventHandler(ignoreCancelled = true)
    public void antiSpawnerWatering(BlockFromToEvent e) {
        if(e.isCancelled()) return;
        Block block = e.getBlock();
        if (block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER || block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA) {
            for(int x = -radius; x <= radius; ++x) {
                for(int y = -radius; y <= radius; ++y) {
                    for(int z = -radius; z <= radius; ++z) {
                        Location loc = new Location(block.getWorld(), (double)(block.getX() + x), (double)(block.getY() + y), (double)(block.getZ() + z));
                        Block blocktotest = loc.getBlock();
                        if (blocktotest.getType() == Material.MOB_SPAWNER) {
                            e.setCancelled(true);
                            block.setType(Material.AIR);
                            e.getToBlock().setType(Material.AIR);
                        }
                    }
                }
            }
        }

    }
}
