package org.raider.raidercore;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.raider.raidercore.commands.CmdReport;
import org.raider.raidercore.listeners.CommandListener;
import org.raider.raidercore.patches.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.raider.raidercore.patches.HopperCraftPatch.patchHoppers;

public class RaiderCore extends JavaPlugin {
    public static int CONFIG_VERSION = 1;

    private static boolean ENABLE_REPORT_COMMAND = false;

    private static boolean DISABLE_HOPPER_CRAFTING = false;

    public static boolean DISABLE_CREEPER_PLAYER_TARGETING = false;

    public static boolean DISABLE_ALL_MOB_PLAYER_TARGETING = false;

    private static boolean DISABLE_HUNGER = false;

    private static boolean ENABLE_SPAWNER_SPONGE = false;

    private static boolean ENABLE_WATEREDSPAWNERSPATCH = false;

    public static int SPAWNERSPONGE_RADIUS = 3;

    public static int WATEREDSPAWNERSPATCH_RADIUS = 3;

    public static int SPONGEPATCH_RADIUS = 3;

    private static boolean ENABLE_SPONGEPATCH = false;

    public static Set<ProtectedRegion> noCropDecay = new HashSet<>();

    public static Set<ProtectedRegion> noSoilTrampling = new HashSet<>();

    public static Set<String> disallowCommands = new HashSet<>();

    public static Set<ProtectedRegion> factionTnt = new HashSet<>();

    private static RaiderCore i;
    public static RaiderCore get(){
        return i;
    }
    public void onEnable(){
        saveDefaultConfig();
        loadConfig();
        loadPatches();
        i = this;
    }

    public static int getConfigVersion(){
        return CONFIG_VERSION;
    }

    public static boolean getCreeperTargetting(){
        return DISABLE_CREEPER_PLAYER_TARGETING;
    }

    public static boolean getMobTargetting(){
        return DISABLE_ALL_MOB_PLAYER_TARGETING;
    }

    public static int getSpongePatchRadius(){
        return SPONGEPATCH_RADIUS;
    }

    public static int getWateredSpawnersPatchRadius(){
        return WATEREDSPAWNERSPATCH_RADIUS;
    }

    public static int getSpawnerSpongeRadius(){
        return SPAWNERSPONGE_RADIUS;
    }

    public static boolean disallowCropDecay(ProtectedRegion pr){
        return noCropDecay.contains(pr);
    }

    public static boolean disallowSoilTrampling(ProtectedRegion pr){
        return noSoilTrampling.contains(pr);
    }

    public static boolean disallowCommand(String str){
        return disallowCommands.contains(str);
    }

    public static boolean disallowExplosion(ProtectedRegion pr){
        return factionTnt.contains(pr);
    }

    private void loadPatches(){
        PluginManager pm = getServer().getPluginManager();
        if(DISABLE_CREEPER_PLAYER_TARGETING || DISABLE_ALL_MOB_PLAYER_TARGETING)
            pm.registerEvents(new HostileMobsPatch(), this);
        if(ENABLE_SPONGEPATCH)
            pm.registerEvents(new SpongePatch(), this);
        if(!noSoilTrampling.isEmpty())
            pm.registerEvents(new SoilTrampleFix(), this);
        if(ENABLE_SPAWNER_SPONGE)
            pm.registerEvents(new SpawnerSpongePatch(), this);
        if(!disallowCommands.isEmpty())
            pm.registerEvents(new CommandListener(), this);
        if(DISABLE_HUNGER)
            pm.registerEvents(new HungerPatch(), this);
        if(!noCropDecay.isEmpty())
            pm.registerEvents(new CropDecayDisable(), this);
        if(ENABLE_REPORT_COMMAND)
            getServer().getPluginCommand("report").setExecutor(new CmdReport());
        if(ENABLE_WATEREDSPAWNERSPATCH)
            pm.registerEvents(new WateredSpawnersPatch(), this);
        if(DISABLE_HOPPER_CRAFTING)
            patchHoppers();
        if(!factionTnt.isEmpty()){
            pm.registerEvents(new NoTNTPatch(), this);
        }
    }

    public void loadConfig(){
        FileConfiguration file = this.getConfig();

        ENABLE_REPORT_COMMAND = file.getBoolean("report-command");
        DISABLE_HOPPER_CRAFTING = file.getBoolean("disable-hopper-crafting");
        DISABLE_ALL_MOB_PLAYER_TARGETING = file.getBoolean("disable-all-mob-player-targeting");
        DISABLE_CREEPER_PLAYER_TARGETING = file.getBoolean("disable-creeper-player-targeting");
        DISABLE_HUNGER = file.getBoolean("disable-hunger");
        ENABLE_SPAWNER_SPONGE = file.getBoolean("enable-spawner-sponge");
        ENABLE_WATEREDSPAWNERSPATCH = file.getBoolean("enable-wateredspawnerspatch");
        ENABLE_SPONGEPATCH = file.getBoolean("enable-spongepatch");
        SPONGEPATCH_RADIUS = file.getInt("spongepatch-radius") - 4;
        SPAWNERSPONGE_RADIUS = file.getInt("spawnersponge-radius");
        WATEREDSPAWNERSPATCH_RADIUS = file.getInt("wateredspawnerspatch-radius");
        CONFIG_VERSION = file.getInt("config-version");

        List<String> cropdecay = file.getStringList("disable-crop-decay-regions");

        for(String s:cropdecay){
            String[] str = s.split(":");
            if(str.length != 2){
                getServer().getLogger().info(Arrays.toString(str) + " error parsing region for cropdecay.");
                continue;
            }
            if(Bukkit.getWorlds().stream().noneMatch(w -> w.getName().equals(str[1]))){
                Bukkit.getLogger().info("Invalid world for disable-crop-decay-regions : " + str[1] + " for region " + str[0] + ".");
                continue;
            }

            RegionManager rm = WorldGuardPlugin.inst().getRegionManager(Bukkit.getWorld(str[1]));

            if(!rm.hasRegion(str[0])){
                Bukkit.getLogger().info("Invalid region name for disable-crop-decay-regions : " + str[0] + " for world " + str[1] + ".");
                continue;
            }

            if(noCropDecay.contains(rm.getRegion(str[0]))){
                continue;
            }

            noCropDecay.add(rm.getRegion(str[0]));
        }

        disallowCommands.addAll(file.getStringList("disable-commands"));

        List<String> notrample = file.getStringList("disable-soil-trampling-regions");

        for(String s: notrample){
            String[] str = s.split(":");
            if(str.length != 2){
                getServer().getLogger().info(Arrays.toString(str) + " error parsing region for notrample.");
                continue;
            }
            if(Bukkit.getWorlds().stream().noneMatch(w -> w.getName().equals(str[1]))){
                Bukkit.getLogger().info("Invalid world for disable-soil-trampling-regions : " + str[1] + "for region " + str[0] + ".");
                continue;
            }

            RegionManager rm = WorldGuardPlugin.inst().getRegionManager(Bukkit.getWorld(str[1]));

            if(!rm.hasRegion(str[0])){
                Bukkit.getLogger().info("Invalid region name for disable-soil-trampling-regions : " + str[0] + "for world " + str[1] + ".");
                continue;
            }

            if(noSoilTrampling.contains(rm.getRegion(str[0]))){
                continue;
            }

            noSoilTrampling.add(rm.getRegion(str[0]));
        }

        List<String> noTnt = file.getStringList("faction-based-tnt");

        for(String s: noTnt){
            String[] str = s.split(":");
            if(str.length != 2){
                getServer().getLogger().info(Arrays.toString(str) + " error parsing region for notnt.");
            }
            if(Bukkit.getWorlds().stream().noneMatch(w -> w.getName().equals(str[1]))){
                Bukkit.getLogger().info("Invalid world for faction-based-tnt : " + str[1] + " for region " + str[0] + ".");
                continue;
            }

            RegionManager rm = WorldGuardPlugin.inst().getRegionManager(Bukkit.getWorld(str[1]));

            if(!rm.hasRegion(str[0])){
                Bukkit.getLogger().info("Invalid region name for faction-based-tnt : " + str[0] + " for world " + str[1] + ".");
                continue;
            }

            if(factionTnt.contains(rm.getRegion(str[0]))){
                continue;
            }

            factionTnt.add(rm.getRegion(str[0]));
        }
    }

    public static void unloadConfig(){
        noCropDecay.clear();
        noSoilTrampling.clear();
        disallowCommands.clear();
        factionTnt.clear();
    }
}