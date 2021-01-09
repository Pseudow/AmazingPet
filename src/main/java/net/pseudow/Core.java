package net.pseudow;

import net.pseudow.pets.IPet;
import net.pseudow.pets.PetManager;
import net.pseudow.receivers.ReceiverManager;
import net.pseudow.receivers.ReceiverType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Core extends AmazingPetAPI {
    private final ReceiverManager receiverManager;
    private final PetManager petManager;

    private final AmazingPet amazingPet;

    public Core(AmazingPet amazingPet) {
        super(amazingPet);
        this.amazingPet = amazingPet;

        this.petManager = new PetManager(this);
        this.receiverManager = new ReceiverManager(this);
    }

    @Override
    public PetManager getPetManager() {
        return petManager;
    }

    @Override
    public ReceiverManager getReceiverManager() {
        return receiverManager;
    }

    @Override
    public String getConfigMessage(String path) {
        return getPlugin().getConfig().getString("messages." + path).replace("&", "§");
    }

    @Override
    public String getPetName(String petName) {
        return getPlugin().getConfig().getString("pets." + petName.toUpperCase()).replace("&", "§");
    }

    @Override
    public void setGuiOnCommand(Inventory inventory) {
        amazingPet.getAmazingPetCommand().setInventory(inventory);
    }

    @Override
    public boolean shouldSendPet(Player player, ReceiverType type, IPet pet) {
        return ((type == ReceiverType.ALL_PETS) || (type == ReceiverType.ONLY_MINE && pet.getOwner() == player) || (type == ReceiverType.ALL_BUT_NOT_MINE && pet.getOwner() != player));
    }

    public AmazingPet getAmazingPet() { return amazingPet; }

    public void disable() {
        petManager.disable();
    }
}
