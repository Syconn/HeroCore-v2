package mod.syconn.hero.registrar;

import mod.syconn.hero.Constants;
import mod.syconn.hero.entity.ThrownMjolnir;
import mod.syconn.hero.extra.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.AbstractArrow;

import java.util.function.Supplier;

public class EntityRegistrar {

    public static final Supplier<EntityType<ThrownMjolnir>> MJOLNIR_ENTITY_TYPE = registerProjectile("thrown_mjolnir", ThrownMjolnir::new, MobCategory.MISC);

    public static void init() {}

    private static <T extends Mob> Supplier<EntityType<T>> registerEntity(String name, EntityType.EntityFactory<T> entity, float width, float height, MobCategory mobCategory) {
        return Services.REGISTRAR.registerEntity(name, () -> EntityType.Builder.of(entity,mobCategory).sized(width, height).build(EntityId(name)));
    }

    private static <T extends AbstractArrow> Supplier<EntityType<T>> registerProjectile(String name, EntityType.EntityFactory<T> entity, MobCategory mobCategory) {
        return Services.REGISTRAR.registerEntity(name, () -> EntityType.Builder.of(entity,mobCategory).sized(1f, 1f).clientTrackingRange(4).updateInterval(10).build(EntityId(name)));
    }

    private static ResourceKey<EntityType<?>> EntityId(String name) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Constants.withId(name));
    }
}
