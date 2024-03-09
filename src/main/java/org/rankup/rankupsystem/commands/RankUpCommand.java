package org.rankup.rankupsystem.commands;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.rankup.rankupsystem.RankupSystem;

public class RankUpCommand implements CommandExecutor {

    private final RankupSystem plugin;

    public RankUpCommand(RankupSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player) {
            Economy eco = plugin.getEco();
            Player player = (Player) commandSender;
            int rank = plugin.data.getConfig().getInt(player.getDisplayName()) + 1;
            if (eco.getBalance(player) < plugin.getConfig().getDouble("money." + rank)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        plugin.getConfig().getString("dinero-insuficiente")));
            } else if (plugin.data.getConfig().getInt(player.getDisplayName()) != 50) {
                plugin.data.getConfig().set(player.getDisplayName(), rank);
                plugin.data.saveConfig();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        plugin.getConfig().getString("rango-siguiente")));
                eco.withdrawPlayer(player, plugin.getConfig().getDouble("money." + rank));
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        plugin.getConfig().getString("rango-maximo")));
            }

        }


        return false;
    }
}
