package net.pseudow.commands;

import net.pseudow.AmazingPet;
import net.pseudow.pets.PetType;
import net.pseudow.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class AmazingPetCommand implements CommandExecutor {
    private Inventory firstInventory;
    private Inventory inventory;

    private final Map<String, Action> subCommand;
    private final AmazingPet plugin;

    public AmazingPetCommand(AmazingPet plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("amazingpet").setExecutor(this);
        this.subCommand = new HashMap<>();

        this.subCommand.put("delete", (player, args) ->
                player.sendMessage(plugin.getCore().getConfigMessage(plugin.getCore().getPetManager().removePlayersPet(player) ? "pet-removed" : "dont-have-pet")));

        this.subCommand.put("rename", (player, args) -> {
            if(args.length > 1) {
                player.sendMessage(plugin.getCore().getConfigMessage(plugin.getCore().getPetManager().setName(player, args[1]) ? "pet-name-modification" : "dont-have-pet").replaceAll("%petname%", args[1]));
            } else player.sendMessage(plugin.getCore().getConfigMessage("invalid-command-need-name")); });

        setupInventory();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!(commandSender instanceof Player)) return true;
        Player player = (Player) commandSender;

        if(args.length == 0) {
            if(hasInventoryChanged()) {
                player.openInventory(inventory);
            } else {
                player.openInventory(firstInventory);
            }
            return true;
        }

        Action action = subCommand.get(args[0]);
        if(action == null) {
            player.sendMessage(plugin.getCore().getConfigMessage("command-doesnt-exist"));
            return true;
        }
        action.fire(player, args);

        return true;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    private void setupInventory() {
        inventory = null;
        firstInventory = Bukkit.createInventory(null, 36, plugin.getCore().getConfigMessage("cosmetic-menu"));

        ItemStack background = new ItemBuilder(Material.STAINED_GLASS_PANE).durability((short) 13).displayname("§r").build();

        firstInventory.setItem(0, new ItemBuilder(Material.INK_SACK).durability((short) 10).displayname(plugin.getCore().getConfigMessage("see-all-pets")).build());
        firstInventory.setItem(1, new ItemBuilder(Material.INK_SACK).durability((short) 14).displayname(plugin.getCore().getConfigMessage("see-all-pets-except-mine")).build());
        firstInventory.setItem(2, new ItemBuilder(Material.INK_SACK).durability((short) 13).displayname(plugin.getCore().getConfigMessage("see-only-my-pet")).build());
        firstInventory.setItem(3, new ItemBuilder(Material.INK_SACK).durability((short) 11).displayname(plugin.getCore().getConfigMessage("dont-see-any-pets")).build());
        firstInventory.setItem(4, background);
        firstInventory.setItem(5, new ItemBuilder(Material.NAME_TAG).displayname(plugin.getCore().getConfigMessage("change-pet-name")).build());
        firstInventory.setItem(6, new ItemBuilder(Material.REDSTONE_COMPARATOR).displayname(plugin.getCore().getConfigMessage("change-animal-flank")).build());
        firstInventory.setItem(7, background);
        firstInventory.setItem(8, new ItemBuilder(Material.DARK_OAK_DOOR_ITEM).displayname(plugin.getCore().getConfigMessage("close-menu")).build());

        for (int i = 9; i < 18; i++) {
            firstInventory.setItem(i, background);
        }

        for (int i = 18; i < firstInventory.getSize(); i++) {
            firstInventory.setItem(i, new ItemBuilder(Material.STONE_BUTTON).displayname(plugin.getCore().getConfigMessage("button-message")).build());
        }

        firstInventory.setItem(18, new ItemBuilder(Material.BARRIER).displayname(plugin.getCore().getConfigMessage("remove-animal")).build());

        int begin = 19;
        for(PetType petType : plugin.getCore().getPetManager().getPetTypes()) {
            firstInventory.setItem(begin, petType.getIcon());
            begin++;
        }
    }

    public boolean hasInventoryChanged() {
        return !(inventory == null) && !Arrays.equals(inventory.getContents(), firstInventory.getContents());
    }
}
