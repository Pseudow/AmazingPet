package net.pseudow.pets.list;

import fr.mrmicky.fastparticle.ParticleType;
import net.pseudow.AmazingPet;
import net.pseudow.pets.AbstractPet;
import net.pseudow.pets.animations.AnimatedPet;
import net.pseudow.reflection.Reflection;
import net.pseudow.reflection.entities.EntityAgeable;
import net.pseudow.utils.PairList;
import net.pseudow.utils.ParticleUtils;
import net.pseudow.utils.RotationUtils;
import org.bukkit.entity.Player;

public class MushCowPet extends AbstractPet implements AnimatedPet {
    private final EntityAgeable entityAgeable;
    private int cooldown;

    public MushCowPet(Player owner, AmazingPet amazingPet) {
        super(owner, amazingPet.getCore().getPetName("MUSHROOM_COW"), amazingPet);

        this.entityAgeable = new EntityAgeable(Reflection.getNMSClass("EntityMushroomCow"), owner.getLocation());
        this.entityAgeable.setAge(-1);
        this.entityAgeable.setCustomNameVisible(false);
        this.entityAgeable.setLocation(getLocation());

        cooldown = 20;
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

    @Override
    public void animate() {
        cooldown--;

        if(cooldown == 0) {
            amazingPet.getCore().getReceiverManager().getReceivers().forEach((player, type) -> {
                if (!amazingPet.getCore().shouldSendPet(player, type, this)) return;
                if (player.getWorld() != getOwner().getWorld()) return;
                ParticleUtils.sendParticles((time) -> new PairList<>(getLocation().clone().add(0, 1.2, 0).toVector(), ParticleType.HEART), player, 1);
            });

            cooldown = 20;
        }
    }
}

