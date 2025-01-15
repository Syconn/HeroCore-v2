package mod.syconn.hero.fabric.datagen;

import mod.syconn.hero.core.ModDamageTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;

import java.util.concurrent.CompletableFuture;

import static mod.syconn.hero.Constants.MOD_ID;

public class DamageTypeGen extends FabricDynamicRegistryProvider {

    public DamageTypeGen(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.addAll(registries.lookupOrThrow(Registries.DAMAGE_TYPE));
    }

    public String getName() {
        return MOD_ID + ":damage_types";
    }

    public static void bootstrapDamageTypes(BootstrapContext<DamageType> context) {
        context.register(ModDamageTypes.MJOLNIR, new DamageType("mjolnir", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1f, DamageEffects.HURT, DeathMessageType.DEFAULT));
    }
}
