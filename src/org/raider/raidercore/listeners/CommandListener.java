package org.raider.raidercore.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import static org.raider.raidercore.RaiderCore.disallowCommand;

public class CommandListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onCommandExecute(PlayerCommandPreprocessEvent event){
        String[] command = event.getMessage().split(" ");
        if(disallowCommand(command[0])) {
            event.getPlayer().sendMessage(ChatColor.RED + "This command must be executed through console.");
            event.setCancelled(true);
        }
    }
}
