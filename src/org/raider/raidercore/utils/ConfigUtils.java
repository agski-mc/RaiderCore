package org.raider.raidercore.utils;

import org.bukkit.configuration.Configuration;
import org.raider.raidercore.RaiderCore;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getServer;
import static org.raider.raidercore.RaiderCore.getConfigVersion;

public final class ConfigUtils {
    public static void updateConfig(){
        //Method only works when lines are added, not changed.
        //Method still needs to be tested.
        //NullPointerException for wahtever reason, fix at some point line 26
        try {
            Configuration config = RaiderCore.get().getConfig().getDefaults();
            if(config == null){
                System.out.println("DefaultConfig is null");
                throw new NullPointerException("defaultconfig is null.");
            }
            int storedVersion = config.getInt("config-version");
            if (getConfigVersion() == storedVersion) {
                return;
            }
        }catch(NullPointerException e){
            e.printStackTrace();
            System.out.println("Null while getting config versions.");
        }
        RaiderCore.get().getConfig().set("config-version", RaiderCore.get().getConfig().getDefaults().getInt("config-version"));
        RaiderCore.get().loadConfig();

        File currentConfig = new File(RaiderCore.get().getConfig().getCurrentPath());
        File storedConfig = new File(RaiderCore.get().getConfig().getDefaults().getCurrentPath());
        try {
            BufferedReader br = new BufferedReader(new FileReader(storedConfig));
            BufferedReader br1 = new BufferedReader(new FileReader(currentConfig));
            BufferedWriter bw = new BufferedWriter(new FileWriter(currentConfig));
            List<String> currentConfigLines = br1.lines().collect(Collectors.toList());
            br.lines().filter(s -> !currentConfigLines.contains(s)).collect(Collectors.toList()).forEach(s ->{
                try{
                    bw.write(s);
                }catch(IOException e){
                    e.printStackTrace();
                    getServer().getLogger().info("Error while updating config file.");
                }
            });

            RaiderCore.get().loadConfig();
        }catch(FileNotFoundException e){
            e.printStackTrace();
            getServer().getLogger().info("Error while updating files: cannot find files.");
        }catch(IOException e){
            e.printStackTrace();
            getServer().getLogger().info("Error while updating config: cannot parse current config.");
        }
    }
}
