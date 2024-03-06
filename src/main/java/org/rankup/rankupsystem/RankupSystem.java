package org.rankup.rankupsystem;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.rankup.rankupsystem.files.DataManager;

//setconfig
//saveConfig()
public final class RankupSystem extends JavaPlugin implements Listener {

    public DataManager data;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.data = new DataManager(this);
        getServer().getPluginManager().registerEvents(this,this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        data.getConfig().set(player.getDisplayName(), 1);
        data.saveConfig();
    }


}
