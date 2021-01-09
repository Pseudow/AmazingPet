package net.pseudow.reflection.entities;

import net.pseudow.reflection.Reflection;
import org.bukkit.Location;

import java.util.Objects;

public class EntityZombie extends EntityLiving {
    private final Object entityZombie;

    public EntityZombie(Location location) {
        super(Reflection.getNMSClass("EntityZombie"), location);
        this.entityZombie = Objects.requireNonNull(Reflection.getNMSClass("EntityZombie")).cast(getEntityLiving());
    }

    public void setBaby(boolean option) {
        Reflection.invokeMethod(entityZombie, "setBaby", option);
    }
}
