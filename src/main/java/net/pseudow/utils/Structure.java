package net.pseudow.utils;

import net.pseudow.reflection.Reflection;
import net.pseudow.reflection.entities.EntityArmorStand;
import net.pseudow.reflection.entities.EntityLiving;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class Structure {
    private final HashMap<EntityLiving.EnumItemSlot, ItemStack> armorStandItems;
    private final EntityArmorStand armorStand;
    private final Vector vector;

    public Structure(Location location, Vector vector) {
        this.vector = vector;
        this.armorStand = new EntityArmorStand(location);
        this.armorStand.setLocation(location);
        this.armorStandItems = new HashMap<>();
    }

    public Vector getVector() {
        return vector;
    }

    public EntityArmorStand getArmorStand() {
        return armorStand;
    }

    public void equip(EntityLiving.EnumItemSlot slot, ItemStack itemStack) {
        armorStandItems.put(slot, itemStack);
    }

    public void sendInitPacket(Player player) {
        Reflection.sendPacket(player, armorStand.getSpawnPacket());

        for(Map.Entry<EntityLiving.EnumItemSlot, ItemStack> entry : armorStandItems.entrySet())
            Reflection.sendPacket(player, armorStand.getEquipmentPacket(entry.getKey(), entry.getValue()));
    }

    public void sendDestroyPacket(Player player) {
        Reflection.sendPacket(player, armorStand.getDestroyPacket());
    }
}
