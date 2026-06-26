package mod.syconn.hero.client.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.syconn.hero.core.ModParticles;
import net.minecraft.core.particles.DustParticleOptionsBase;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.Locale;

public class TrailParticleOptions extends ParticleType<TrailParticleOptions> implements ParticleOptions {

    public static final Codec<TrailParticleOptions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.VECTOR3F.fieldOf("fromColor").forGetter(dustColorTransitionOptions -> dustColorTransitionOptions.color),
            ExtraCodecs.VECTOR3F.fieldOf("toColor").forGetter(dustColorTransitionOptions -> dustColorTransitionOptions.toColor)).apply(instance, TrailParticleOptions::new)
    );

    @SuppressWarnings("deprecation")
    public static final ParticleOptions.Deserializer<TrailParticleOptions> DESERIALIZER = new ParticleOptions.Deserializer<>() {

        @Override
        public @NotNull TrailParticleOptions fromCommand(ParticleType<TrailParticleOptions> particleType, StringReader reader) throws CommandSyntaxException {
            Vector3f vector3f = DustParticleOptionsBase.readVector3f(reader);
            Vector3f vector3f2 = DustParticleOptionsBase.readVector3f(reader);
            return new TrailParticleOptions(vector3f, vector3f2);
        }

        @Override
        public @NotNull TrailParticleOptions fromNetwork(ParticleType<TrailParticleOptions> particleType, FriendlyByteBuf buffer) {
            Vector3f vector3f = DustParticleOptionsBase.readVector3f(buffer);
            Vector3f vector3f2 = DustParticleOptionsBase.readVector3f(buffer);
            return new TrailParticleOptions(vector3f, vector3f2);
        }
    };

    private final Vector3f color;
    private final Vector3f toColor;

    public TrailParticleOptions(Vector3f color, Vector3f toColor) {
        super(true, DESERIALIZER);
        this.color = color;
        this.toColor = toColor;
    }

    public Vector3f getFromColor() {
        return this.color;
    }

    public Vector3f getToColor() {
        return this.toColor;
    }

    @Override
    public @NotNull ParticleType<?> getType() {
        return ModParticles.IRONMAN_TRAIL.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeFloat(this.color.x());
        buffer.writeFloat(this.color.y());
        buffer.writeFloat(this.color.z());
        buffer.writeFloat(this.toColor.x());
        buffer.writeFloat(this.toColor.y());
        buffer.writeFloat(this.toColor.z());
    }

    @Override
    public @NotNull Codec<TrailParticleOptions> codec() {
        return CODEC;
    }

    @Override
    public @NotNull String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %.2f", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()), this.color.x(), this.color.y(), this.color.z(), this.toColor.x(), this.toColor.y(), this.toColor.z());
    }
}
