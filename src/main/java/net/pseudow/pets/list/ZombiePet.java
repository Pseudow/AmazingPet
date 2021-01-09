package net.pseudow.pets.list;

import fr.mrmicky.fastparticle.ParticleType;
import net.pseudow.AmazingPet;
import net.pseudow.pets.AbstractPet;
import net.pseudow.pets.animations.AnimatedPet;
import net.pseudow.reflection.Reflection;
import net.pseudow.reflection.entities.EntityZombie;
import net.pseudow.utils.PairList;
import net.pseudow.utils.ParticleUtils;
import net.pseudow.utils.RotationUtils;
import org.bukkit.entity.Player;

public class ZombiePet extends AbstractPet implements AnimatedPet {
    private final EntityZombie entityZombie;
    private int cooldown;

    public ZombiePet(Player owner, AmazingPet amazingPet) {
        super(owner, amazingPet.getCore().getPetName("ZOMBIE"), amazingPet);

        this.entityZombie = new EntityZombie(owner.getLocation());
        this.entityZombie.setBaby(true);
        this.entityZombie.setCustomNameVisible(false);
        this.entityZombie.setLocation(getLocation());

        cooldown = 20;
    }

    @Override
    public void setInvisible(boolean option) {
        this.entityZombie.setInvisible(option);

        amazingPet.getCore().getReceiverManager().getReceivers().forEach((player, type) -> { if(amazingPet.getCore().shouldSendPet(player, type, this)) { Reflection.sendPacket(player, entityZombie.getMetadataPacket()); }});
    }

    @Override
    public void sendInitPacket(Player player) {
        Reflection.sendPacket(player, entityZombie.getSpawnPacket());
        super.sendInitPacket(player);
    }

    @Override
    public void sendDestroyPacket(Player player) {
        Reflection.sendPacket(player, entityZombie.getDestroyPacket());
        super.sendDestroyPacket(player);
    }

    @Override
    public void update() {
        amazingPet.getCore().getReceiverManager().getReceivers().forEach((player,type) -> {
            if (!amazingPet.getCore().shouldSendPet(player, type, this)) return;
            Reflection.sendPacket(player, entityZombie.getHeadPacket(RotationUtils.getCompressedAngle(getLocation().getYaw())));
            entityZombie.setLocation(getLocation());
            Reflection.sendPacket(player, entityZombie.getTeleportPacket());
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
                ParticleUtils.sendParticles((time) -> new PairList<>(getLocation().clone().add(0, 1.2, 0).toVector(), ParticleType.VILLAGER_ANGRY), player, 1);
            });

            cooldown = 20;
        }
    }
}
