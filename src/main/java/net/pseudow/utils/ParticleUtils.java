package net.pseudow.utils;

import fr.mrmicky.fastparticle.FastParticle;
import fr.mrmicky.fastparticle.ParticleType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.function.Function;

public class ParticleUtils {
    public static void sendParticles(Function<Long, PairList<Vector, ParticleType>> statement, Player player, int count) {
        statement.apply(System.currentTimeMillis()).forEach((e) -> {
            Vector v = e.getKey();
            FastParticle.spawnParticle(player, e.getValue(), v.toLocation(player.getWorld()), count);
        });
    }
}
