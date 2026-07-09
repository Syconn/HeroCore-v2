package mod.syconn.hero.utils.client;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

public class ExpandedParticleType extends ParticleType<ExpandedParticleType> implements ParticleOptions {

    @SuppressWarnings("deprecation")
    private static final Deserializer<ExpandedParticleType> DESERIALIZER = new Deserializer<>() {

        @Override
        public @NotNull ExpandedParticleType fromCommand(ParticleType<ExpandedParticleType> particleType, StringReader stringReader) {
            return (ExpandedParticleType)particleType;
        }

        @Override
        public @NotNull ExpandedParticleType fromNetwork(ParticleType<ExpandedParticleType> particleType, FriendlyByteBuf packetByteBuf) {
            return (ExpandedParticleType)particleType;
        }
    };

    private final Codec<ExpandedParticleType> codec = Codec.unit(this::getType);

    public ExpandedParticleType(boolean alwaysShow) {
        super(alwaysShow, DESERIALIZER);
    }

    @Override
    public @NotNull ExpandedParticleType getType()
    {
        return this;
    }

    @Override
    public @NotNull Codec<ExpandedParticleType> codec()
    {
        return this.codec;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
    }

    @Override
    public @NotNull String writeToString() {
        return BuiltInRegistries.PARTICLE_TYPE.getKey(this).toString();
    }
}
