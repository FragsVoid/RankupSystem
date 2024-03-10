package org.rankup.rankupsystem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class RewardsItems {


    private final RankupSystem plugin;
    private final Map<Enchantment, Integer> enchantmentToLevelMap = new HashMap<>();



    public RewardsItems(RankupSystem plugin) {
        this.plugin = plugin;
    }


    public ItemStack make(Player player) {
        int rank = plugin.data.getConfig().getInt(player.getDisplayName()) + 1;
        int amount = plugin.items.getConfig().getInt("Rewards." + rank + ".amount");
        Material material;
        String customName = plugin.items.getConfig().getString("Rewards." + rank + ".name");
        try {
            material = Material.valueOf(plugin.items.getConfig().getString
                    ("Rewards." + rank +".material"));
        } catch (Exception e) {
            material = Material.AIR;
        }

        ConfigurationSection enchantmentSection = plugin.items.getConfig().getConfigurationSection(
                "Rewards." + rank + ".enchantments");
        if (enchantmentSection != null) {
            for (String enchantmentKey : enchantmentSection.getKeys(false)) {
                Enchantment enchantment = Enchantment.getByName(enchantmentKey);
                if (enchantment != null) {
                    enchantmentToLevelMap.put(enchantment, enchantmentSection.getInt(enchantmentKey));
                }

            }
        }



        ItemStack itemStack = new ItemStack(material, amount);

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null)
            return null;
        if (customName != null)
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', customName));

        if (!enchantmentToLevelMap.isEmpty()) {
            for (Map.Entry<Enchantment, Integer> enchantEntry : enchantmentToLevelMap.entrySet()){
                meta.addEnchant(enchantEntry.getKey(),enchantEntry.getValue(),true);
            }
        }
        itemStack.setItemMeta(meta);

        return itemStack;
    }
}
