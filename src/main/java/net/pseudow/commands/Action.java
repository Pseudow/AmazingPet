package net.pseudow.commands;

import org.bukkit.entity.Player;

public interface Action {
    void fire(Player player, String[] args);
}
