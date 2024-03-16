package org.rankup.rankupsystem;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.rankup.rankupsystem.commands.RankUpCommand;
import org.rankup.rankupsystem.commands.RanksCommand;
import org.rankup.rankupsystem.commands.RanksItem;
import org.rankup.rankupsystem.expansion.RankExpansion;
import org.rankup.rankupsystem.files.DataManager;
import org.rankup.rankupsystem.files.ItemsManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class RankupSystem extends JavaPlugin implements Listener {

    public DataManager data;

    public ItemsManager items;
    FileConfiguration file;
    File cFile;

    public final List<RewardsItems> rewardsItems = new ArrayList<>();

    private static Economy eco;

    @Override
    public void onEnable() {
        this.data = new DataManager(this);
        this.items = new ItemsManager(this);
        getServer().getPluginManager().registerEvents(this,this);

        this.file = getConfig();
        this.file.options().copyDefaults(true);
        this.cFile = new File(getDataFolder(), "config.yml");
        saveDefaultConfig();

        boolean firstime = file.getBoolean("firstime");
        boolean firstitemtime = items.getConfig().getBoolean("firstime");

        getCommand("rankitem").setExecutor(new RanksItem(this));
        getCommand("ranks").setExecutor(new RanksCommand(this));
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
        }
        new RankExpansion(this).register();
        if (firstime) {
            for (int i = 1; i < 51; i++) {
                file.set("Ranks." + i + ".name", i);
                file.set("Ranks." + i + ".money", 100 * i);
            }
            saveConfig();
        }
        /*if (firstitemtime) {
            items.getConfig().createSection("Rewards");
            for (int i = 1; i < 51; i++) {
                items.getConfig().set("Rewards." + i + ".material" , "DIAMOND_AXE");
                items.getConfig().set("Rewards." + i + ".name", "&cThe Axe");
                items.getConfig().set("Rewards." + i + ".amount", 1);
            }
            items.saveConfig();
        }

         */
        file.set("firstime", false);
        saveConfig();
        items.getConfig().set("firstime", false);
        items.saveConfig();

        getCommand("rankup").setExecutor(new RankUpCommand(this, items.getConfig()));
    }
    @EventHandler
    public void onClick(final InventoryClickEvent e)
    {
        if (e.getInventory().getTitle().equals(ChatColor.RED + "Ranks menu")){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrag(final InventoryDragEvent e) {
            if (e.getInventory().getTitle().equals(ChatColor.RED + "Ranks menu"))
            {
                e.setCancelled(true);
            }
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("rankreload")) {

            data.reloadConfig();
            if (sender instanceof Player) {
                if (!sender.hasPermission("ranks.reload"))
                    return false;
                sender.sendMessage("Configs reloading!");
            } else if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender) {
                System.out.println("Configs reloading!");
            }
            file = YamlConfiguration.loadConfiguration(this.cFile);
            reloadConfig();
            items.reloadConfig();
        }
        return false;
    }


    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        eco = rsp.getProvider();
        return eco != null;
    }


    public Economy getEco() {
        return eco;
    }
}
