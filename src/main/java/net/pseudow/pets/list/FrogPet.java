package net.pseudow.pets.list;

import fr.mrmicky.fastparticle.ParticleType;
import net.pseudow.AmazingPet;
import net.pseudow.pets.AbstractArmorStand;
import net.pseudow.pets.animations.AnimatedPet;
import net.pseudow.reflection.entities.EntityLiving;
import net.pseudow.utils.ItemBuilder;
import net.pseudow.utils.ItemUtils;
import net.pseudow.utils.PairList;
import net.pseudow.utils.ParticleUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class FrogPet extends AbstractArmorStand implements AnimatedPet {
    public FrogPet(Player owner, AmazingPet amazingPet) {
        super(owner, amazingPet.getCore().getPetName("FROG"), amazingPet);

        getArmorStand().setLeftArmPose(new EulerAngle(20, 45, 0));
        getArmorStand().setRightArmPose(new EulerAngle(-45, 20, 0));
        getArmorStand().setRightLegPose(new EulerAngle(30, 0, 0));
        getArmorStand().setLeftLegPose(new EulerAngle(-25, 0, -5));

        setArmorStandEquipment(EntityLiving.EnumItemSlot.BOOTS, new ItemBuilder(Material.LEATHER_BOOTS).color(Color.GREEN).build());
        setArmorStandEquipment(EntityLiving.EnumItemSlot.LEGGINS, new ItemBuilder(Material.LEATHER_LEGGINGS).color(Color.GREEN).build());
        setArmorStandEquipment(EntityLiving.EnumItemSlot.CHESTPLATE, new ItemBuilder(Material.LEATHER_CHESTPLATE).color(Color.GREEN).build());
        setArmorStandEquipment(EntityLiving.EnumItemSlot.HELMET,
                ItemUtils.getSkull("http://textures.minecraft.net/texture/b554e67fd2f39232f097d0f2ada71be7db8d6080ea7fda63506ab81d472c5eb"));
    }

    @Override
    public void animate() {
        amazingPet.getCore().getReceiverManager().getReceivers().forEach((player, type) -> {
            if (!amazingPet.getCore().shouldSendPet(player, type, this)) return;
            if (player.getWorld() != getOwner().getWorld()) return;
            ParticleUtils.sendParticles((time) -> new PairList<>(getLocation().toVector().add(new Vector(Math.cos(time) / 2., 0, Math.sin(time) / 2.)), ParticleType.DRIP_WATER), player, 1);
        });
    }
}
