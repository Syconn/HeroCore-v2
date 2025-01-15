package mod.syconn.hero.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class RenderUtil {

    public static void blit(GuiGraphics graphics, ResourceLocation atlasLocation, int x, int y, int blitOffset, float uOffset, float vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight, float red, float green, float blue, float alpha) {
        blit(graphics, atlasLocation, x, x + uWidth, y, y + vHeight, blitOffset, uWidth, vHeight, uOffset, vOffset, textureWidth, textureHeight, red, green, blue, alpha);
    }

    public static void blit(GuiGraphics graphics, ResourceLocation atlasLocation, int x1, int x2, int y1, int y2, int blitOffset, int uWidth, int vHeight, float uOffset, float vOffset, int textureWidth, int textureHeight, float red, float green, float blue, float alpha) {
        innerBlit(graphics, atlasLocation, x1, x2, y1, y2, blitOffset, (uOffset + 0.0F) / (float)textureWidth, (uOffset + (float)uWidth) / (float) textureWidth, (vOffset + 0.0F) / (float) textureHeight, (vOffset + (float)vHeight) / (float)textureHeight, red, green, blue, alpha);
    }

    public static void innerBlit(GuiGraphics graphics, ResourceLocation atlasLocation, int x1, int x2, int y1, int y2, int blitOffset, float minU, float maxU, float minV, float maxV, float red, float green, float blue, float alpha) {
        RenderSystem.setShaderTexture(0, atlasLocation);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.enableBlend();
        Matrix4f matrix4f = graphics.pose().last().pose();
        BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferBuilder.addVertex(matrix4f, (float)x1, (float)y1, (float)blitOffset).setColor(red, green, blue, alpha).setUv(minU, minV);
        bufferBuilder.addVertex(matrix4f, (float)x1, (float)y2, (float)blitOffset).setColor(red, green, blue, alpha).setUv(minU, maxV);
        bufferBuilder.addVertex(matrix4f, (float)x2, (float)y2, (float)blitOffset).setColor(red, green, blue, alpha).setUv(maxU, maxV);
        bufferBuilder.addVertex(matrix4f, (float)x2, (float)y1, (float)blitOffset).setColor(red, green, blue, alpha).setUv(maxU, minV);
        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
        RenderSystem.disableBlend();
    }
}
