package net.pseudow.updater;

import net.pseudow.AmazingPet;
import net.pseudow.pets.IPet;
import net.pseudow.pets.animations.AnimatedPet;

public class Updater implements Runnable {
    private final AmazingPet plugin;

    public Updater(AmazingPet plugin) {
        this.plugin = plugin;
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 0L, 1L);
    }

    @Override
    public void run() {
        plugin.getCore().getPetManager().getPlayersPet().values().forEach(IPet::update);
        plugin.getCore().getPetManager().getPlayersPet().values().stream().filter(pet -> pet instanceof AnimatedPet && !pet.isInvisible()).forEach(pet -> ((AnimatedPet) pet).animate());
    }
}