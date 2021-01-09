package net.pseudow.pets.list;

import net.pseudow.AmazingPet;
import net.pseudow.pets.AbstractPet;
import net.pseudow.reflection.Reflection;
import net.pseudow.reflection.entities.EntityAgeable;
import net.pseudow.utils.RotationUtils;
import org.bukkit.entity.Player;

public class ChickenPet extends AbstractPet {
    private final EntityAgeable entityAgeable;

    public ChickenPet(Player owner, AmazingPet amazingPet) {
        super(owner, amazingPet.getCore().getPetName("CHICKEN"), amazingPet);

        this.entityAgeable = new EntityAgeable(Reflection.getNMSClass("EntityChicken"), owner.getLocation());
        this.entityAgeable.setCustomNameVisible(false);
        this.entityAgeable.setLocation(getLocation());
    }

    @Override
    public void setInvisible(boolean option) {
        this.entityAgeable.setInvisible(option);

        amazingPet.getCore().getReceiverManager().getReceivers().forEach((player, type) -> { if(amazingPet.getCore().shouldSendPet(player, type, this)) { Reflection.sendPacket(player, entityAgeable.getMetadataPacket()); }});
    }

    @Override
    public void sendInitPacket(Player player) {
        Reflection.sendPacket(player, entityAgeable.getSpawnPacket());
        super.sendInitPacket(player);
    }

    @Override
    public void sendDestroyPacket(Player player) {
        Reflection.sendPacket(player, entityAgeable.getDestroyPacket());
        super.sendDestroyPacket(player);
    }

    @Override
    public void update() {
        amazingPet.getCore().getReceiverManager().getReceivers().forEach((player,type) -> {
            if (!amazingPet.getCore().shouldSendPet(player, type, this)) return;
            Reflection.sendPacket(player, entityAgeable.getHeadPacket(RotationUtils.getCompressedAngle(getLocation().getYaw())));
            entityAgeable.setLocation(getLocation());
            Reflection.sendPacket(player, entityAgeable.getTeleportPacket());
        });
        super.update();
    }
}
