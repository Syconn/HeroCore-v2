package mod.syconn.hero.features.ironman.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.syconn.hero.client.particle.TrailParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class IronmanTrailParticle extends SimpleAnimatedParticle {
    private final Vector3f fromColor;
    private final Vector3f toColor;

    protected IronmanTrailParticle(ClientLevel level, TrailParticleOptions options, SpriteSet spriteSet, double x, double y, double z, double dx, double dy, double dz) {
        super(level, x, y, z, spriteSet, 0.0125F);
        this.xd = dx;
        this.yd = dy;
        this.zd = dz;

        this.quadSize *= 0.75F;
        this.lifetime = 8;
        this.quadSize = this.quadSize * 1.5f;
        this.setFadeColor(15916745);
        this.setSpriteFromAge(sprites);

        var f = this.random.nextFloat() * 0.4F + 0.6F;
        this.fromColor = this.randomizeColor(options.getFromColor(), f);
        this.toColor = this.randomizeColor(options.getToColor(), f);
    }

    private Vector3f randomizeColor(Vector3f vector, float multiplier) {
        return new Vector3f(this.randomColor(vector.x(), multiplier), this.randomColor(vector.y(), multiplier), this.randomColor(vector.z(), multiplier));
    }

    private float randomColor(float cordMultiplier, float multiplier) {
        return (this.random.nextFloat() * 0.2F + 0.8F) * cordMultiplier * multiplier;
    }

    private void lerpColors(float partialTick) {
        var f = (this.age + partialTick) / (this.lifetime + 1.0F);
        var vector3f = new Vector3f(this.fromColor).lerp(this.toColor, f);
        this.rCol = vector3f.x();
        this.gCol = vector3f.y();
        this.bCol = vector3f.z();
    }

    @Override
    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().move(x, y, z));
        this.setLocationFromBoundingbox();
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        this.lerpColors(partialTicks);
        super.render(buffer, renderInfo, partialTicks);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Provider implements ParticleProvider<TrailParticleOptions> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(TrailParticleOptions options, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new IronmanTrailParticle(level, options, this.sprites, x, y, z, dx, dy, dz);
        }
    }
}
