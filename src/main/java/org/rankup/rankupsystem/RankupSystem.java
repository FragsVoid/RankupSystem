package org.rankup.rankupsystem;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.rankup.rankupsystem.commands.RankUpCommand;
import org.rankup.rankupsystem.commands.RanksCommand;
import org.rankup.rankupsystem.expansion.RankExpansion;
import org.rankup.rankupsystem.files.DataManager;

import java.util.Objects;

//setconfig
//saveConfig()
public final class RankupSystem extends JavaPlugin implements Listener {

    public DataManager data;
    public FileConfiguration file = getConfig();

    private static Economy eco;
    @Override
    public void onEnable() {
        this.data = new DataManager(this);
        getServer().getPluginManager().registerEvents(this,this);
        saveDefaultConfig();
        for (int i = 0; i < 50; i++) {
            file.addDefault("money." + i, 100 * i);
        }
        getCommand("ranks").setExecutor(new RanksCommand(this));
        getCommand("rankup").setExecutor(new RankUpCommand(this));

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
        }
        new RankExpansion(this).register();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (!player.hasPlayedBefore()) {
            data.getConfig().set(player.getDisplayName(), 1);
            data.saveConfig();
        }
    }

    @EventHandler
    public void onMenuClick(InventoryInteractEvent e) {
        Inventory inv = e.getInventory();

        if (Objects.equals(inv.getName(), "Ranks menu")) {
            e.setCancelled(true);
        }
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


    public FileConfiguration getConfig() {
        return file;
    }

    public Economy getEco() {
        return eco;
    }
}
