package mod.syconn.hero.extra.core;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class Events {

    public record LivingEntityFallEvent(LivingEntity entity, float distance, float damageMultiplier, boolean cancel) {}

    public record PlayerTickEvent(Player player) {}

}
