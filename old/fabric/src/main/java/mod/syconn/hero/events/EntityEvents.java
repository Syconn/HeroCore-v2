package mod.syconn.hero.events;

import mod.syconn.hero.extra.core.Events;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class EntityEvents {

    public static Event<LivingEntityFallCallback> FALL_EVENT = EventFactory.createArrayBacked(LivingEntityFallCallback.class, (listeners) ->
            (livingEntity, distance, damageMultiplier, cancelled) -> {
                for (LivingEntityFallCallback listener : listeners) return listener.fall(livingEntity, distance, damageMultiplier, cancelled);
                return new Events.LivingEntityFallEvent(livingEntity, distance, damageMultiplier, cancelled);
    });

    public static Event<EntityTickCallback> PLAYER_TICK = EventFactory.createArrayBacked(EntityTickCallback.class, listeners -> (entity) -> {
        for (EntityTickCallback callback : listeners) callback.tick(entity);
    });

    public interface LivingEntityFallCallback {
        Events.LivingEntityFallEvent fall(LivingEntity livingEntity, float distance, float damageMultiplier, boolean cancelled);
    }

    public interface EntityTickCallback {
        void tick(Player player);
    }
}
