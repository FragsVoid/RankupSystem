package org.rankup.rankupsystem.expansion;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.rankup.rankupsystem.RankupSystem;

public class RankExpansion extends PlaceholderExpansion {

    private final RankupSystem plugin;

    private int getRank(Player player) {
        return plugin.data.getConfig().getInt(player.getDisplayName());
    }

    public RankExpansion(RankupSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "rankup";
    }

    @Override
    public @NotNull String getAuthor() {
        return "frags";
    }

    @Override
    public @NotNull String getVersion() {
        return null;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null)
            return "";
        if (params.equals("rank")) {
            return String.valueOf(getRank(player));
        } else if (params.equals("money")) {
            return "money." + getRank(player);
        }
        return null;
    }

}
