package net.pseudow.reflection.entities;

import net.pseudow.reflection.Reflection;
import org.bukkit.Location;
import org.bukkit.util.EulerAngle;

import java.lang.reflect.Constructor;
import java.util.Objects;

public class EntityArmorStand extends EntityLiving {
    private final Object entityArmorStand;
    private final Constructor<?> vector3f;

    public EntityArmorStand(Location location) {
        super(Reflection.getNMSClass("EntityArmorStand"), location);
        this.entityArmorStand = Objects.requireNonNull(Reflection.getNMSClass("EntityArmorStand")).cast(getEntityLiving());
        this.vector3f = Reflection.getConstructor(Objects.requireNonNull(Reflection.getNMSClass("Vector3f")),  float.class, float.class, float.class);
    }

    public void setBasePlate(boolean option) {
        Reflection.invokeMethod(entityArmorStand, "setBasePlate", option);
    }

    public void setSmall(boolean option) {
        Reflection.invokeMethod(entityArmorStand, "setSmall", option);
    }

    public void setArms(boolean option) {
        Reflection.invokeMethod(entityArmorStand, "setArms", option);
    }

    public void setBodyPose(EulerAngle eulerAngle){
        Reflection.invokeMethod(entityArmorStand, "setBodyPose", Reflection.callConstructor(vector3f,
                (float) eulerAngle.getX(),
                (float) eulerAngle.getY(),
                (float) eulerAngle.getZ()
        ));
    }

    public void setHeadPose(EulerAngle eulerAngle){
        Reflection.invokeMethod(entityArmorStand, "setHeadPose", Reflection.callConstructor(vector3f,
                (float) eulerAngle.getX(),
                (float) eulerAngle.getY(),
                (float) eulerAngle.getZ()
        ));
    }

    public void setRightArmPose(EulerAngle eulerAngle){
        Reflection.invokeMethod(entityArmorStand, "setRightArmPose", Reflection.callConstructor(vector3f,
                (float) eulerAngle.getX(),
                (float) eulerAngle.getY(),
                (float) eulerAngle.getZ()
        ));
    }

    public void setLeftArmPose(EulerAngle eulerAngle){
        Reflection.invokeMethod(entityArmorStand, "setLeftArmPose", Reflection.callConstructor(vector3f,
                (float) eulerAngle.getX(),
                (float) eulerAngle.getY(),
                (float) eulerAngle.getZ()
        ));
    }

    public void setLeftLegPose(EulerAngle eulerAngle){
        Reflection.invokeMethod(entityArmorStand, "setLeftLegPose", Reflection.callConstructor(vector3f,
                (float) eulerAngle.getX(),
                (float) eulerAngle.getY(),
                (float) eulerAngle.getZ()
        ));
    }

    public void setRightLegPose(EulerAngle eulerAngle){
        Reflection.invokeMethod(entityArmorStand, "setRightLegPose", Reflection.callConstructor(vector3f,
                (float) eulerAngle.getX(),
                (float) eulerAngle.getY(),
                (float) eulerAngle.getZ()
        ));
    }

}
