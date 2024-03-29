package org.rankup.rankupsystem.commands;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.rankup.rankupsystem.RankupSystem;
import org.rankup.rankupsystem.RewardsItems;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RankUpCommand implements CommandExecutor {

    private final RankupSystem plugin;


    public RankUpCommand(RankupSystem plugin, FileConfiguration lootConfig) {
        this.plugin = plugin;
        ConfigurationSection itemsSection = lootConfig.getConfigurationSection("Rewards");
        if (itemsSection == null)
            System.out.println("Rewards are null, it wont work if you delete it!");
        plugin.rewardsItems.add(new RewardsItems(plugin));
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player) {
            Economy eco = plugin.getEco();
            Player player = (Player) commandSender;
            int rank = plugin.data.getConfig().getInt(player.getDisplayName()) + 1;
            if (eco.getBalance(player) < plugin.getConfig().getDouble("money." + rank)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        plugin.getConfig().getString("not-enough-money")));
            } else if (plugin.data.getConfig().getInt(player.getDisplayName()) != 50) {
                if (plugin.items.getConfig().getBoolean("enabled")) {
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes(
                                '&',
                                plugin.getConfig().getString("Inventory-full")));
                        return false;
                    }
                    giveItem(player);
                }
                plugin.data.getConfig().set(player.getDisplayName(), rank);
                plugin.data.saveConfig();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        plugin.getConfig().getString("next-rank")));
                eco.withdrawPlayer(player, plugin.getConfig().getDouble("money." + rank));
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        plugin.getConfig().getString("maximum-rank")));
            }

        }


        return false;
    }


    public void giveItem(Player player) {
        plugin.rewardsItems.add(new RewardsItems(plugin));

        RewardsItems randomItem = plugin.rewardsItems.get(plugin.data.getConfig().getInt
                (player.getDisplayName()));

        ItemStack itemStack = randomItem.make(player);
        player.getInventory().addItem(itemStack);

    }
}
