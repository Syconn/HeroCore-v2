package mod.syconn.hero.utils.client.shaders;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import mod.syconn.hero.client.HeroClient;
import mod.syconn.hero.utils.Constants;
import mod.syconn.hero.utils.client.TintedResourceLocation;
import mod.syconn.hero.utils.generic.ColorUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class FlameRenderer {

    public static final int FLAME = ColorUtil.packHsv(0.08f, 0.9f, 1.0f);

    private static RenderType getFlameShader(ResourceLocation texture) {
        return RenderType.create(Constants.withId("muzzle_flash2").toString(), DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, true,
                RenderType.CompositeState.builder().setShaderState(RenderStateShard.POSITION_COLOR_TEX_SHADER).setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                        .setCullState(new RenderStateShard.CullStateShard(false)).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING).createCompositeState(true));
    }

    private static RenderType getFlameShaderAdditive(ResourceLocation texture) {
        return RenderType.create(Constants.withId("flame_additive").toString(), DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, true,
                RenderType.CompositeState.builder().setShaderState(RenderStateShard.POSITION_COLOR_TEX_SHADER).setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                        .setCullState(new RenderStateShard.CullStateShard(false)).setTransparencyState(RenderStateShard.LIGHTNING_TRANSPARENCY).setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING).createCompositeState(true)
        );
    }

    private static final ResourceLocation[] ID_FLAME = new ResourceLocation[]{
            Constants.withId("textures/entity/flame/flame_4.png"),
            Constants.withId("textures/entity/flame/flame_0.png"),
            Constants.withId("textures/entity/flame/flame_1.png"),
            Constants.withId("textures/entity/flame/flame_2.png"),
            Constants.withId("textures/entity/flame/flame_3.png")
    };

    public static void renderFlame(PoseStack poseStack, MultiBufferSource buffer, float shotTime, int light, int overlay, int color) {
        var opacity = 1.0f;
        var flashRadius = 0.25f;
        int frame = (int) Math.floor(shotTime) % ID_FLAME.length;

        flashRadius = 0.28f;
        renderFlamePass(poseStack, buffer, frame, color, flashRadius, opacity, overlay, light, false);
        flashRadius = 0.12f;
        renderFlamePass(poseStack, buffer, frame, color, flashRadius, opacity * 0.9f, overlay, light, true);
    }

    private static void renderFlamePass(PoseStack poseStack, MultiBufferSource buffer, int frame, int hsvColor, float flashRadius, float opacity, int overlay, int light, boolean additive) {
        var rgb = ColorUtil.hsvToRgbInt(ColorUtil.hsvGetH(hsvColor), ColorUtil.hsvGetS(hsvColor), ColorUtil.hsvGetV(hsvColor));
        var tinted = new TintedResourceLocation(ID_FLAME[frame], ColorUtil.argbToAbgr(rgb), ColorUtil.TintMode.Overlay);
        var texture = HeroClient.tintedTextureProvider.getId("flame/" + ColorUtil.toResourceId(rgb) + "/" + frame, () -> ID_FLAME[frame], () -> tinted);
        var vc = buffer.getBuffer(additive ? getFlameShaderAdditive(texture) : getFlameShader(texture));

        ShaderBuffer.RENDER.init(vc, poseStack.last(), 1, 1, 1, opacity, overlay, light);
        var maxU = 30f / 32f;
        var maxV = 15f / 32f;
        var length = flashRadius * (additive ? 2.0f : 4.0f);

        // vertical
        ShaderBuffer.RENDER.vertex(0, -flashRadius, -0.2f, 0, 0, 1, maxU, 0);
        ShaderBuffer.RENDER.vertex(0, -flashRadius, -0.2f + length, 0, 0, 1, 0, 0);
        ShaderBuffer.RENDER.vertex(0, flashRadius, -0.2f + length, 0, 0, 1, 0, maxV);
        ShaderBuffer.RENDER.vertex(0, flashRadius, -0.2f, 0, 0, 1, maxU, maxV);

        // horizontal
        ShaderBuffer.RENDER.vertex(-flashRadius, 0, -0.2f, 0, 0, 1, maxU, 0);
        ShaderBuffer.RENDER.vertex(-flashRadius, 0, -0.2f + length, 0, 0, 1, 0, 0);
        ShaderBuffer.RENDER.vertex(flashRadius, 0, -0.2f + length, 0, 0, 1, 0, maxV);
        ShaderBuffer.RENDER.vertex(flashRadius, 0, -0.2f, 0, 0, 1, maxU, maxV);
    }
}
