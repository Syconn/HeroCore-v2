package mod.syconn.hero.utils.client.shaders;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import mod.syconn.hero.utils.Constants;
import mod.syconn.hero.utils.generic.ColorUtil;
import mod.syconn.hero.utils.generic.ModelUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

public class PlasmaRenderer {

    public static final RenderType PLASMA = RenderType.create(Constants.MOD + ":plasma", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, true,
            RenderType.CompositeState.builder().setLayeringState(RenderStateShard.LayeringStateShard.VIEW_OFFSET_Z_LAYERING).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
            .setShaderState(RenderStateShard.RENDERTYPE_LIGHTNING_SHADER).createCompositeState(true));

    public static void renderPlasma(PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, boolean unstable, float length, float lengthScalar, float radius, boolean cap, int glowHsv, boolean bolt) {
        final VertexConsumer vc = bufferSource.getBuffer(PLASMA);
        var totalLength = length * lengthScalar * 1.3f;
        var shake = (0.2f) * 0.004f;

        if (unstable) shake *= 2;
        double dX = (float) Constants.RANDOM.nextGaussian() * shake;
        double dY = (float) Constants.RANDOM.nextGaussian() * shake;
        poseStack.translate(dX, 0, dY);

        ShaderBuffer.RENDER.init(vc, poseStack.last(), 1, 1, 1, 1, overlay, light);
        render(totalLength, radius, ColorUtil.hsvGetH(glowHsv), ColorUtil.hsvGetS(glowHsv), ColorUtil.hsvGetV(glowHsv), unstable, cap, bolt);
    }

    private static void render(float length, float radius, float glowHue, float glowSat, float glowVal, boolean unstable, boolean cap, boolean bolt) {
        if (length == 0) return;

        var thicknessBottom = radius * 0.018f;
        var thicknessTop = radius * (cap ? 0.012f : thicknessBottom);

        var mL = 0;
        var xL = 14;

        var deltaThickness = radius * 0.0028f;
        var minOutputLayer = mL * thicknessBottom / deltaThickness;
        var globalTime = ((System.currentTimeMillis()) % Integer.MAX_VALUE) / 5f;

        for (var layer = mL; layer <= xL; layer++) {
            var time = ((System.currentTimeMillis() - layer * 10) % Integer.MAX_VALUE) / 200f;
            var noise = (float) Constants.SIMPLEX.getValue(0, time);

            var hueOffset = unstable ? (noise * 0.025f) : 0;

            var x = ModelUtil.remap(layer, mL, xL, minOutputLayer, 60);
            var alpha = ColorUtil.getAlpha(x);
            if (alpha < 16 / 255f) continue;

            var color = ColorUtil.hsvToRgbInt(ColorUtil.getHue(glowHue + hueOffset, x), ColorUtil.getSaturation(x, glowSat), ColorUtil.getValue(x, glowVal));
            ShaderBuffer.RENDER.setColor(color, (int)(255 * alpha));
            var layerThickness = deltaThickness * layer;

            var bottom = bolt ? -layerThickness : 0;

            if (layer > 0) {
                ShaderBuffer.RENDER.invertCull(true);
                ShaderBuffer.RENDER.drawSolidBoxSkewTaper(thicknessTop + layerThickness, thicknessBottom + layerThickness, 0, length + layerThickness, 0, 0, bottom, 0);
                ShaderBuffer.RENDER.invertCull(false);
            } else {
                final var segments = unstable ? 35 : 1;
                final var dSegments = 1f / segments;
                final var dLength = length / segments;

                final var dLengthTime = 5;

                for (var i = 0; i < segments; i++) {
                    var topThicknessLerp = Mth.lerp(dSegments * (i + 1), thicknessBottom, thicknessTop);
                    var bottomThicknessLerp = Mth.lerp(dSegments * i, thicknessBottom, thicknessTop);
                    var dTTop = unstable ? (float) Constants.SIMPLEX.getValue(globalTime, dLengthTime * dLength * (i + 1)) * 0.0085f : 0;
                    var dTBottom = unstable ? (float) Constants.SIMPLEX.getValue(globalTime, dLengthTime * dLength * i) * 0.0085f : 0;

                    noise = (float) Constants.SIMPLEX.getValue(globalTime, 3 * dLength * i);
                    color = ColorUtil.hsvToRgbInt(0, (unstable ? (0.07f - noise * 0.07f) : 0) * glowSat, ColorUtil.getValue(x, glowVal) - 0.12f);
                    ShaderBuffer.RENDER.setColor(color, (int)(255 * ColorUtil.getAlpha(x)));
                    ShaderBuffer.RENDER.drawSolidBoxSkewTaper(topThicknessLerp + dTTop, bottomThicknessLerp + dTBottom, 0, dLength * (i + 1), 0, 0, dLength * i, 0);
                }
            }
        }
    }

    public static void renderLayer(RenderBuffers buffers) {
        buffers.outlineBufferSource().getBuffer(PLASMA).endVertex();
    }
}
