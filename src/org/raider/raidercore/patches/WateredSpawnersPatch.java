package org.raider.raidercore.patches;

import de.dustplanet.util.SilkUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import static org.raider.raidercore.RaiderCore.get;
import static org.raider.raidercore.RaiderCore.getWateredSpawnersPatchRadius;

import java.util.HashSet;

public class WateredSpawnersPatch implements Listener {
    private SilkUtil silk = SilkUtil.hookIntoSilkSpanwers();
    private static HashSet<Location> locations = new HashSet<>();

    @EventHandler()
    public void onEntityExplode(EntityExplodeEvent event){
        if(event.isCancelled()) return;
        if(event.getEntityType() == EntityType.PRIMED_TNT || event.getEntityType() == EntityType.CREEPER){
            final Location loc = event.getLocation();
            if(locations.contains(loc)) return;
            locations.add(loc);
            for(int x = loc.getBlockX() -getWateredSpawnersPatchRadius(); x <= loc.getBlockX() +getWateredSpawnersPatchRadius(); x++) {
                for (int y = loc.getBlockY() - getWateredSpawnersPatchRadius(); y <= loc.getBlockY() + getWateredSpawnersPatchRadius(); y++) {
                    for (int z = loc.getBlockZ() - getWateredSpawnersPatchRadius(); z <= loc.getBlockZ() + getWateredSpawnersPatchRadius(); z++) {
                        if (loc.getWorld().getBlockAt(x, y, z).getType() == Material.MOB_SPAWNER) {
                            Block block = loc.getWorld().getBlockAt(x, y, z);
                            CreatureSpawner cs = (CreatureSpawner) block.getState();
                            ItemStack itemStack = silk.newSpawnerItem(cs.getSpawnedType().getTypeId(), silk.getCustomSpawnerName(cs.getSpawnedType().getName()), 1, false);
                            block.breakNaturally();
                            block.getLocation().getWorld().dropItemNaturally(block.getLocation(), itemStack);
                        } else {
                            if (loc.getWorld().getBlockAt(x, y, z).getType() == Material.LAVA || loc.getWorld().getBlockAt(x, y, z).getType() == Material.STATIONARY_LAVA) {
                                loc.getWorld().getBlockAt(x, y, z).breakNaturally();
                            }
                        }
                    }
                }
            }
            BukkitRunnable br = new BukkitRunnable() {
                @Override
                public void run() {
                    locations.remove(loc);
                }
            };

            br.runTaskTimer(get(), 5, 0);

        }
    }
}
