package net.pseudow.reflection.entities;

import net.pseudow.reflection.Reflection;
import org.bukkit.Location;

import java.util.Objects;

public class EntityAgeable extends EntityLiving {
    private final Object entityAgeable;

    public EntityAgeable(Class<?> entityClass, Location location) {
        super(entityClass, location);
        this.entityAgeable = Objects.requireNonNull(Reflection.getNMSClass("EntityAgeable")).cast(getEntityLiving());
    }

    public void setAge(int age) {
        Reflection.invokeMethod(entityAgeable, "setAge", age);
    }
}
