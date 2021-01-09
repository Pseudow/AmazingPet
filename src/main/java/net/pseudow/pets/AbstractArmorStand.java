package net.pseudow.pets;

import net.pseudow.AmazingPet;
import net.pseudow.reflection.Reflection;
import net.pseudow.reflection.entities.EntityLiving;
import net.pseudow.utils.Structure;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbstractArmorStand extends AbstractPet {
    private final HashMap<EntityLiving.EnumItemSlot, ItemStack> armorStandItems;
    private final List<Structure> structures;

    @Deprecated
    public AbstractArmorStand(Player owner, String name, AmazingPet amazingPet) {
        super(owner, name, amazingPet);

        getArmorStand().setSmall(true);
        getArmorStand().setArms(true);
        getArmorStand().setBasePlate(true);
        getArmorStand().setInvisible(false);

        this.armorStandItems = new HashMap<>();
        this.structures = new ArrayList<>();
    }

    @Override
    public void sendInitPacket(Player player) {
        Reflection.sendPacket(player, getArmorStand().getSpawnPacket());

        for(Map.Entry<EntityLiving.EnumItemSlot, ItemStack> entry : armorStandItems.entrySet())
            Reflection.sendPacket(player, getArmorStand().getEquipmentPacket(entry.getKey(), entry.getValue()));

        for(Structure structure : structures)
            structure.sendInitPacket(player);
    }

    @Override
    public void sendDestroyPacket(Player player) {
        super.sendDestroyPacket(player);

        for(Structure structure : structures)
            structure.sendDestroyPacket(player);
    }

    public void setArmorStandEquipment(EntityLiving.EnumItemSlot slot, ItemStack itemStack) {
       armorStandItems.put(slot, itemStack);
    }

    public Structure addStructure(Vector vector) {
        Structure structure = new Structure(getLocation(), vector);
        this.structures.add(structure);
        return structure;
    }

    @Override
    public void setAMInvisible(boolean option) { }

    @Override
    public void setInvisible(boolean option) {
        if(option == getArmorStand().isInvisible())
            return;


        amazingPet.getCore().getReceiverManager().getReceivers().forEach((player, type) -> {
            if(amazingPet.getCore().shouldSendPet(player, type, this)) {
                if(option)
                    sendDestroyPacket(player);
                if(!option)
                    sendInitPacket(player);
            }});
    }

    @Override
    public void update() {
        super.update();

        for(Structure structure : structures) {
            Location location = getLocation();
            location.add(structure.getVector());
            amazingPet.getCore().getReceiverManager().getReceivers().forEach((player, type) -> {
                if(amazingPet.getCore().shouldSendPet(player, type, this))
                    Reflection.sendPacket(player, structure.getArmorStand().getTeleportPacket());
            });
        }
    }
}
