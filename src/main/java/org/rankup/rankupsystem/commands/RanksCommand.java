package org.rankup.rankupsystem.commands;

import com.avaje.ebeaninternal.server.core.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.rankup.rankupsystem.RankupSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RanksCommand implements CommandExecutor {

    private final RankupSystem plugin;

    public RanksCommand(RankupSystem plugin) {
        this.plugin = plugin;
    }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            Inventory inv = Bukkit.createInventory(player, 54, ChatColor.RED + "Ranks menu");
            for (int i = 1; i < 51; i++) {
                if(plugin.data.getConfig().getInt(player.getDisplayName()) >= i) {
                    ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, i, (short) 5);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                            plugin.getConfig().getString("name-green-rank").replace("{number}",
                                    String.valueOf(i))));
                    List<String> list = new ArrayList<>();
                    for (String lore : plugin.getConfig().getStringList("lore-green-rank")) {
                        list.add(ChatColor.translateAlternateColorCodes('&',
                                lore).replace("{money}", String.valueOf(plugin.getConfig().getDouble("money."
                                + i))).replace("{item}",ChatColor.translateAlternateColorCodes('&',
                                        plugin.items.getConfig().getString("Rewards." + i + ".name"))));
                    }
                    meta.setLore(list);
                    item.setItemMeta(meta);
                    inv.addItem(item);
                } else {
                    ItemStack item2 = new ItemStack(Material.STAINED_GLASS_PANE, i, (short) 14);
                    ItemMeta meta2 = item2.getItemMeta();
                    meta2.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                            plugin.getConfig().getString("name-red-rank").replace("{number}",
                                    String.valueOf(i))));
                    List<String> list = new ArrayList<>();
                    for (String lore : plugin.getConfig().getStringList("lore-red-rank")) {
                        list.add(ChatColor.translateAlternateColorCodes('&',
                                lore).replace("{money}", String.valueOf(plugin.getConfig().getDouble("money."
                                + i))).replace("{item}",ChatColor.translateAlternateColorCodes('&',
                                plugin.items.getConfig().getString("Rewards." + i + ".name"))));
                    }
                    meta2.setLore(list);
                    item2.setItemMeta(meta2);
                    inv.addItem(item2);

                }
            }
            player.openInventory(inv);
            return true;
        }

        return false;
    }
}
