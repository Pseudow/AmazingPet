package net.pseudow.pets;

import net.pseudow.AmazingPet;
import net.pseudow.reflection.Reflection;
import net.pseudow.reflection.entities.EntityArmorStand;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public abstract class AbstractPet implements IPet {
    private final EntityArmorStand entity;
    private final Player owner;
    private Location location;
    private boolean invisible;
    private boolean rightFlank;

    protected AmazingPet amazingPet;

	public AbstractPet(Player owner, String name, AmazingPet amazingPet) {
        this.owner = owner;
        this.amazingPet = amazingPet;

        this.entity = new EntityArmorStand(owner.getLocation());

        this.entity.setBasePlate(true);
        this.entity.setInvisible(true);
        this.entity.setSmall(true);

        this.invisible = true;
        this.rightFlank = true;

        setLocation(owner.getLocation());
        setName(name);
    }

    public Player getOwner() {
        return owner;
    }

    public Location getLocation() {
        return location;
    }

    public void setAMInvisible(boolean option) {
	    this.invisible = option;

        if(invisible) entity.setCustomNameVisible(false);
        if(!invisible) entity.setCustomNameVisible(true);
        amazingPet.getCore().getReceiverManager().getReceivers().forEach((player, type) -> {
            if(amazingPet.getCore().shouldSendPet(player, type, this)) { Reflection.sendPacket(player, entity.getMetadataPacket()); }});
    }

    public void setLocation(Location location) {
        this.location = location;
        entity.setLocation(location);
    }

    public void setRightFlank(boolean option) {
	    this.rightFlank = option;
    }

    public boolean isRightFlank() {
	    return rightFlank;
    }
    
    public boolean isInvisible() {
		return invisible;
	}

    public abstract void setInvisible(boolean option);

    public void update() {
        amazingPet.getCore().getReceiverManager().getReceivers().forEach((player, type) -> {
            if(!amazingPet.getCore().shouldSendPet(player, type, this)) return;

            Location newLocation = getOwner().getLocation();

            if(rightFlank)
                newLocation.add(location.getDirection().setY(0).normalize().multiply(1.3f).crossProduct(new Vector(0., 1., 0.)).add(new Vector(0., 1.1, 0.)));

            setLocation(newLocation);
            Reflection.sendPacket(player, entity.getTeleportPacket());

            if (amazingPet.PET_INVISIBLE_ON_SNEAK) {
                if (owner.isSneaking()) {
                    setInvisible(true);
                    setAMInvisible(true);
                }

                if (!owner.isSneaking()) {
                    setInvisible(false);
                    setAMInvisible(false);
                }
            }
        });
    }

    public void sendInitPacket(Player player) {
        Reflection.sendPacket(player, entity.getSpawnPacket());
    }

    public void sendDestroyPacket(Player player) {
        Reflection.sendPacket(player, entity.getDestroyPacket());
    }

    public void setName(String name){
        entity.setCustomNameVisible(true);
        entity.setCustomName(name.replace("&", "§"));

        amazingPet.getCore().getReceiverManager().getReceivers().forEach((player, type) -> {
    		if(!amazingPet.getCore().shouldSendPet(player, type, this)) return;
            Reflection.sendPacket(player, entity.getMetadataPacket());
        });
    }

    public EntityArmorStand getArmorStand() {
        return this.entity;
    }

    public String getName() {
        return entity.getCustomName();
    }
}
