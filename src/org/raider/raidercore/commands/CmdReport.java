package org.raider.raidercore.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdReport implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players may run this command.");
            return true;
        }
        if (!sender.hasPermission("report.report")) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return true;
        }
        if (args.length == 0 || args.length == 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /report (Player) (Reason)");
            return true;
        }
        if (!Bukkit.getServer().getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }
        Player player = Bukkit.getPlayer(args[0]);
        String s = "";
        for(int i = 1; i < args.length; i++){
            s = s.concat(args[i]);
            if(i == args.length -1){
                continue;
            }
            s = s.concat(" ");
        }
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (player.hasPermission("report.view")) {
                p.sendMessage(ChatColor.AQUA + "Player " + player.getName() + " has been reported for " + s);
            }
        }
        return true;
    }
}
