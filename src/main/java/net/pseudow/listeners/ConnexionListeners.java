package net.pseudow.listeners;

import net.pseudow.AmazingPet;
import net.pseudow.receivers.ReceiverType;
import net.pseudow.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnexionListeners implements Listener {
    private final AmazingPet amazingPet;

    public ConnexionListeners(AmazingPet amazingPet) {
        this.amazingPet = amazingPet;
        this.amazingPet.getServer().getPluginManager().registerEvents(this, amazingPet);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        amazingPet.getCore().getReceiverManager().setReceiver(player, amazingPet.RECEIVER_ACTIVE_ON_JOIN ? ReceiverType.ALL_PETS : ReceiverType.ONLY_MINE);

        if(amazingPet.getConfig().getBoolean("item-hotbar"))
            player.getInventory().setItem(amazingPet.getConfig().getInt("item-hotbar-slot"),
                    new ItemBuilder(Material.ENDER_CHEST).displayname(amazingPet.getConfig().getString("item-hotbar-displayname")).build());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        amazingPet.getCore().getPetManager().removePlayersPet(player);
        amazingPet.getCore().getReceiverManager().removeReceiver(player);
        amazingPet.getCore().getPetManager().getPlayersPet().values().forEach(pet -> pet.sendDestroyPacket(player));
    }

}
