package mod.syconn.hero.core;

import mod.syconn.hero.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

public class ModDamageTypes {

    public static final ResourceKey<DamageType> MJOLNIR = ResourceKey.create(Registries.DAMAGE_TYPE, Constants.withId("mjolnir"));

    public static DamageSource mjolnir(Entity causer, Entity thrower) {
        return new DamageSource(causer.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(MJOLNIR), causer, thrower);
    }}
