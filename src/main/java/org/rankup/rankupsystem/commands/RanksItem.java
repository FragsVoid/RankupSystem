package org.rankup.rankupsystem.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.rankup.rankupsystem.RankupSystem;
import sun.text.resources.cldr.ka.FormatData_ka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RanksItem implements CommandExecutor {

    private final RankupSystem plugin;

    public RanksItem(RankupSystem plugin) {
        this.plugin = plugin;
    }

    private final Map<Enchantment, Integer> enchantmentToLevelMap = new HashMap<>();


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (commandSender instanceof Player) {
          Player player = (Player) commandSender;
          ItemStack item = player.getItemInHand();
          ItemStack air = new ItemStack(Material.AIR);


          if (player.hasPermission("ranks.set")) {
            if (args.length == 0) {
                player.sendMessage(ChatColor.RED + "Wrong usage! /rankitem set <Rank>");
                return false;
            } else if (args.length == 1) {
                player.sendMessage(ChatColor.RED + "Wrong usage! /rankitem set <Rank>");
            }else if (args.length == 2) {
                if (item == null || item.equals(air)) {
                    player.sendMessage("Your item in hand is null!");
                    return false;
                }
                player.sendMessage(ChatColor.GREEN + "Your item has been set as a reward to the rank " +
                        Integer.valueOf(args[1]));

                Material mat = player.getInventory().getItemInHand().getType();
                int amount = player.getInventory().getItemInHand().getAmount();
                String name = player.getInventory().getItemInHand().getItemMeta().getDisplayName();
                if (!player.getInventory().getItemInHand().getEnchantments().isEmpty()) {
                    for (Map.Entry<Enchantment, Integer> ench :
                            player.getInventory().getItemInHand().getEnchantments().entrySet()) {
                        int level = ench.getValue();
                        Enchantment enchantment = ench.getKey();
                        plugin.items.getConfig().set("Rewards." + args[1] + ".enchantments." +
                                        enchantment.getName(), level);
                    }
                } else {
                    plugin.items.getConfig().set("Rewards." + args[1] + ".enchantments", null);
                }
                    plugin.items.getConfig().set("Rewards." + args[1] + ".material", mat.toString());
                    plugin.items.getConfig().set("Rewards." + args[1] + ".name", name);
                    plugin.items.getConfig().set("Rewards." + args[1] + ".amount", amount);
                    plugin.items.saveConfig();
            }
          }
        }

        return false;
    }
}
