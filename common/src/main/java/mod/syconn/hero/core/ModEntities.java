package mod.syconn.hero.core;

import dev.architectury.registry.registries.DeferredRegister;
import mod.syconn.hero.common.entity.ThrownMjolnir;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.AbstractArrow;

import java.util.function.Supplier;

import static mod.syconn.hero.Constants.MOD_ID;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(MOD_ID, Registries.ENTITY_TYPE);

    public static final Supplier<EntityType<ThrownMjolnir>> MJOLNIR_ENTITY_TYPE = registerProjectile("thrown_mjolnir", ThrownMjolnir::new, MobCategory.MISC);
    
    private static <T extends Mob> Supplier<EntityType<T>> registerEntity(String name, EntityType.EntityFactory<T> entity, float width, float height, MobCategory mobCategory) {
        return ENTITIES.register(name, () -> EntityType.Builder.of(entity,mobCategory).sized(width, height).build(name));
    }

    private static <T extends AbstractArrow> Supplier<EntityType<T>> registerProjectile(String name, EntityType.EntityFactory<T> entity, MobCategory mobCategory) {
        return ENTITIES.register(name, () -> EntityType.Builder.of(entity,mobCategory).sized(1f, 1f).clientTrackingRange(4).updateInterval(10).build(name));
    }
}
