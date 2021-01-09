package net.pseudow.listeners;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.pseudow.AmazingPet;
import net.pseudow.receivers.ReceiverType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GuiListeners implements Listener {
    private final AmazingPet amazingPet;

    public GuiListeners(AmazingPet amazingPet) {
        this.amazingPet = amazingPet;
        this.amazingPet.getServer().getPluginManager().registerEvents(this, amazingPet);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(amazingPet.getAmazingPetCommand().hasInventoryChanged()) return;

        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null)
            return;

        if (!(event.getWhoClicked() instanceof Player))
            return;

        Inventory inventory = event.getInventory();
        if(inventory.getTitle().equalsIgnoreCase(amazingPet.getCore().getConfigMessage("cosmetic-menu")) && inventory.getSize() == 36) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();

            if(itemStack.getType().equals(Material.STONE_BUTTON))
                return;

            if(itemStack.getType().equals(Material.DARK_OAK_DOOR_ITEM))
                player.closeInventory();

            if(itemStack.getType().equals(Material.BARRIER)) {
                player.sendMessage(amazingPet.getCore().getConfigMessage(amazingPet.getCore().getPetManager().removePlayersPet(player) ? "remove-pet" : "dont-have-pet"));
            } else if(itemStack.getType().equals(Material.REDSTONE_COMPARATOR)) {
                player.sendMessage(amazingPet.getCore().getConfigMessage(amazingPet.getCore().getPetManager().switchPetFlank(player) ? "change-animal-flank" : "dont-have-pet"));
            } else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(amazingPet.getCore().getConfigMessage("see-all-pets"))) {
                player.sendMessage(amazingPet.getCore().getConfigMessage(amazingPet.getCore().getReceiverManager().setReceiver(player, ReceiverType.ALL_PETS) ? "see-all-pets-selected" : "param-already-saved"));
            } else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(amazingPet.getCore().getConfigMessage("dont-see-any-pets"))) {
                player.sendMessage(amazingPet.getCore().getConfigMessage(amazingPet.getCore().getReceiverManager().setReceiver(player, ReceiverType.NO_PETS) ? "dont-see-any-pets-selected" : "param-already-saved"));
            } else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(amazingPet.getCore().getConfigMessage("see-only-my-pet"))) {
            	player.sendMessage(amazingPet.getCore().getConfigMessage(amazingPet.getCore().getReceiverManager().setReceiver(player, ReceiverType.ONLY_MINE) ? "see-only-my-pet-selected" : "param-already-saved"));
            } else if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(amazingPet.getCore().getConfigMessage("see-all-pets-except-mine"))) {
            	player.sendMessage(amazingPet.getCore().getConfigMessage(amazingPet.getCore().getReceiverManager().setReceiver(player, ReceiverType.ALL_BUT_NOT_MINE) ? "see-all-pets-except-mine-selected" : "param-already-saved"));
            } else if(itemStack.getType().equals(Material.NAME_TAG)) {
                player.closeInventory();
                
                TextComponent before = new TextComponent(amazingPet.getCore().getConfigMessage("change-command-text"));
                TextComponent text = new TextComponent(amazingPet.getCore().getConfigMessage("change-command-text-click"));
                text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/amazingpet rename <newname>"));
                TextComponent after = new TextComponent(amazingPet.getCore().getConfigMessage("change-command-text-click-and"));
                
                before.addExtra(text);
                before.addExtra(after);

                player.spigot().sendMessage(before);

            } else {
                    amazingPet.getCore().getPetManager().getPetTypes().stream().filter(pet -> pet.getIcon().getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName()))
                            .forEach(pet -> {
                                if (!player.hasPermission("amazingpet.spawn." + pet.toString().toLowerCase())) {
                                    player.sendMessage(amazingPet.getCore().getConfigMessage("permission-missing"));
                                    player.closeInventory();
                                    return;
                                }
                                amazingPet.getCore().getPetManager().setPlayerPet(pet.getConsumer().createPet(player));
                            });
                }
            }
        }

}
