package mod.syconn.hero.core;

import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.DeferredSupplier;
import mod.syconn.hero.client.particle.IronmanTrailParticle;
import mod.syconn.hero.client.particle.TrailParticleOptions;
import mod.syconn.hero.utils.Constants;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(Constants.MOD, Registries.PARTICLE_TYPE);

    public static final DeferredSupplier<TrailParticleOptions> IRONMAN_TRAIL = register(Constants.withId("ironman_trail"), IronmanTrailParticle.Provider::new);

    private static DeferredSupplier<TrailParticleOptions> register(ResourceLocation location, ParticleProviderRegistry.DeferredParticleProvider<TrailParticleOptions> particleProvider) {
        var type = PARTICLES.register(location, () -> new TrailParticleOptions(new Vector3f(), new Vector3f()));
        ParticleProviderRegistry.register(type, particleProvider);
        return type;
    }
}
