package mod.syconn.hero.core;

import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.syconn.hero.client.particle.TrailParticleOptions;
import mod.syconn.hero.features.ironman.client.particles.IronmanTrailParticle;
import mod.syconn.hero.utils.Constants;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import org.joml.Vector3f;

//? if forge {
/*import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
*///? }

//? if fabric {
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
//? }

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(Constants.MOD, Registries.PARTICLE_TYPE);

    public static final RegistrySupplier<TrailParticleOptions> IRONMAN_TRAIL = PARTICLES.register("ironman_trail", () -> new TrailParticleOptions(new Vector3f(), new Vector3f()));

    //? if forge
    //@OnlyIn(Dist.CLIENT)
    //? if fabric
    @Environment(EnvType.CLIENT)
    public static void registerParticleProviders() {
        ParticleProviderRegistry.register(IRONMAN_TRAIL, IronmanTrailParticle.Provider::new);
    }
}
