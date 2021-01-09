package net.pseudow.pets;

import net.pseudow.Core;
import net.pseudow.events.PlayerChangePetNameEvent;
import net.pseudow.events.PlayerRemovePetEvent;
import net.pseudow.events.PlayerSpawnPetEvent;
import net.pseudow.pets.list.*;
import net.pseudow.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PetManager implements IPetManager {
    private final HashMap<Player, IPet> playersPet;
    private final List<PetType> petTypes;
    private final Core core;

    public PetManager(Core core) {
        this.playersPet = new HashMap<>();
        this.petTypes = new ArrayList<>();
        this.core = core;

        this.petTypes.add(new PetType(core,"PIG", PetType.Rarity.COMMON, (player) -> new PigPet(player, core.getAmazingPet()),
                new ItemBuilder(new ItemStack(Material.MONSTER_EGG, 1, (byte) 90))));
        this.petTypes.add(new PetType(core, "CHICKEN", PetType.Rarity.COMMON,
                (player) -> new ChickenPet(player, core.getAmazingPet()), new ItemBuilder(new ItemStack(Material.MONSTER_EGG, 1, (byte) 93))));
        this.petTypes.add(new PetType(core,"SHEEP", PetType.Rarity.COMMON, (player) -> new SheepPet(player, core.getAmazingPet()),
                new ItemBuilder(new ItemStack(Material.MONSTER_EGG, 1, (byte) 91))));
        this.petTypes.add(new PetType(core,"COW", PetType.Rarity.COMMON, (player) -> new CowPet(player, core.getAmazingPet()),
                new ItemBuilder(new ItemStack(Material.MONSTER_EGG, 1, (byte) 92))));
        this.petTypes.add(new PetType(core,"ZOMBIE", PetType.Rarity.RARE, (player) -> new ZombiePet(player, core.getAmazingPet()),
                new ItemBuilder(new ItemStack(Material.MONSTER_EGG, 1, (byte) 54))));
        this.petTypes.add(new PetType(core,"MUSHROOM_COW", PetType.Rarity.RARE, (player) -> new MushCowPet(player, core.getAmazingPet()),
                new ItemBuilder(new ItemStack(Material.MONSTER_EGG, 1, (byte) 96))));
        this.petTypes.add(new PetType(core,"RAINBOW_SHEEP", PetType.Rarity.RARE, (player) -> new RainbowSheepPet(player, core.getAmazingPet()),
                new ItemBuilder(new ItemStack(Material.MONSTER_EGG, 1, (byte) 91))));
        this.petTypes.add(new PetType(core,"FROG", PetType.Rarity.EPIC, (player) -> new FrogPet(player, core.getAmazingPet()),
                new ItemBuilder(Material.WATER_LILY)));
    }

    public void disable() {
        core.getReceiverManager().getReceivers().forEach((player, type) -> playersPet.values().forEach(pet -> pet.sendDestroyPacket(player)));
    }

    @Override
    public void setPlayerPet(IPet pet) {
        if(pet.getOwner() == null) return;

        if(!(pet instanceof AbstractPet) && !core.getAmazingPet().PREMIUM_VERSION) {
            pet.getOwner().sendMessage("§cErreur, vous ne pouvez pas ajouter de pet via cette version ! Pour cela vous devez utiliser la version payante !");
            return;
        }

        PlayerSpawnPetEvent event = new PlayerSpawnPetEvent(pet.getOwner(), pet);
        core.getAmazingPet().getServer().getPluginManager().callEvent(event);

        if(!event.isCancelled()) {
            if(playersPet.containsKey(pet.getOwner()))
                core.getReceiverManager().getReceivers().forEach((player, type) -> playersPet.remove(pet.getOwner()).sendDestroyPacket(player));

            playersPet.put(pet.getOwner(), pet);
            core.getReceiverManager().getReceivers().forEach((player, type) -> {
                if(core.shouldSendPet(player, type, pet)) pet.sendInitPacket(player);
            });

            pet.getOwner().sendMessage(core.getConfigMessage("spawn-pet").replace("%petname%", pet.getName()));
        } else {
            pet.getOwner().sendMessage(core.getConfigMessage("permission-missing"));
        }
    }

    @Override
    public boolean removePlayersPet(Player owner) {
        if(!playersPet.containsKey(owner)) return false;

        PlayerRemovePetEvent event = new PlayerRemovePetEvent(owner, playersPet.get(owner));
        core.getAmazingPet().getServer().getPluginManager().callEvent(event);

        if(!event.isCancelled()) {
            core.getReceiverManager().getReceivers().forEach((player, type) -> playersPet.remove(owner).sendDestroyPacket(player));

            owner.sendMessage(core.getConfigMessage("remove-pet"));
        } else {
            owner.sendMessage(core.getConfigMessage("permission-missing"));
        }

        return true;
    }

    @Override
    public boolean setName(Player player, String name) {
        if(!playersPet.containsKey(player)) return false;

        PlayerChangePetNameEvent event = new PlayerChangePetNameEvent(player, name);
        core.getAmazingPet().getServer().getPluginManager().callEvent(event);

        if(!event.isCancelled()) {
            playersPet.get(player).setName(name);
            player.sendMessage(core.getConfigMessage("pet-name-modification"));
        } else {
            player.sendMessage(core.getConfigMessage("permission-missing"));
        }

        return true;
    }

    @Override
    public boolean switchPetFlank(Player player) {
        if(!playersPet.containsKey(player)) return false;

        IPet abstractPet = playersPet.get(player);

        if(abstractPet.isRightFlank())
            abstractPet.setRightFlank(false);

        if(!abstractPet.isRightFlank())
            abstractPet.setRightFlank(true);

        return true;
    }

    @Override
    public HashMap<Player, IPet> getPlayersPet() {
        return playersPet;
    }

    @Override
    public List<PetType> getPetTypes() {
        return petTypes;
    }
}
