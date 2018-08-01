package org.raider.raidercore;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.raider.raidercore.commands.*;
import org.raider.raidercore.listeners.CommandListener;
import org.raider.raidercore.patches.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.raider.raidercore.patches.HopperCraftPatch.patchHoppers;

public class RaiderCore extends JavaPlugin {
    private static int CONFIG_VERSION = 1;

    private static boolean ENABLE_REPORT_COMMAND = false;

    private static boolean DISABLE_HOPPER_CRAFTING = false;

    private static boolean DISABLE_CREEPER_PLAYER_TARGETING = false;

    private static boolean DISABLE_ALL_MOB_PLAYER_TARGETING = false;

    private static boolean DISABLE_HUNGER = false;

    private static boolean ENABLE_SPAWNER_SPONGE = false;

    private static boolean ENABLE_WATEREDSPAWNERSPATCH = false;

    private static int SPAWNERSPONGE_RADIUS = 3;

    private static int WATEREDSPAWNERSPATCH_RADIUS = 3;

    private static int SPONGEPATCH_RADIUS = 3;

    private static boolean ENABLE_SPONGEPATCH = false;

    private static boolean DISABLE_EXPLOSIONS = false;

    private static Set<ProtectedRegion> noCropDecay = new HashSet<>();

    private static Set<ProtectedRegion> noSoilTrampling = new HashSet<>();

    private static Set<String> disallowCommands = new HashSet<>();

    private static Set<ProtectedRegion> factionTnt = new HashSet<>();

    private static RaiderCore i;
    public static RaiderCore get(){
        return i;
    }
    public void onEnable(){
        saveDefaultConfig();
        loadConfig();
        loadPatches();
        getServer().getPluginCommand("rsettings").setExecutor(new CmdSettings());
        getServer().getPluginCommand("rconfig").setExecutor(new CmdConfig());
        getServer().getPluginCommand("testarmor").setExecutor(new CmdTestLeatherArmor());
        getServer().getPluginCommand("rattributes").setExecutor(new CmdCheckAttributes());
        i = this;
    }

    public void onDisable(){
        //lets ensure that our config is saved.
        this.saveConfig();
    }

    //GETTERS

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

    public static boolean isExplosionsDisabled(){
        return DISABLE_EXPLOSIONS;
    }

    public static boolean isFactionTntEnabled(){
        return !factionTnt.isEmpty();
    }

    //SETTERS

    public static void setConfigVersion(int value){
        if(CONFIG_VERSION == value)return;
        CONFIG_VERSION = value;
        get().getConfig().set("config-version", value);
    }

    public static void setCreeperTargetting(boolean value){
        DISABLE_CREEPER_PLAYER_TARGETING = value;
    }

    public static void setMobTargetting(boolean value){
        DISABLE_ALL_MOB_PLAYER_TARGETING = value;
    }

    public static void setSpongePatchRadius(int value){
        SPONGEPATCH_RADIUS = value;
    }

    public static void setWateredSpawnersPatchRadius(int value){
        WATEREDSPAWNERSPATCH_RADIUS = value;
    }

    public static void setSpawnerSpongeRadius(int value){
        SPAWNERSPONGE_RADIUS = value;
    }

    public static void toggleAllowCropDecay(ProtectedRegion pr){
        if(noCropDecay.contains(pr)){
            noCropDecay.remove(pr);
        }else{
            noCropDecay.add(pr);
        }
    }

    public static void toggleAllowCommand(String command){
        if(disallowCommands.contains(command)){
            disallowCommands.remove(command);
        }else{
            disallowCommands.add(command);
        }
    }

    public static void setExplosionsDisabled(boolean val){
        DISABLE_EXPLOSIONS = val;
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
        if(!factionTnt.isEmpty() || DISABLE_EXPLOSIONS){
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
        DISABLE_EXPLOSIONS = file.getBoolean("disable-explosions");

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
