package mod.syconn.hero.client.screen.overlay;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import mod.syconn.hero.core.ModItems;
import mod.syconn.hero.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

public class IronmanOverlay {

    private static final Minecraft minecraft = Minecraft.getInstance();
    private static final ResourceLocation VIGNETTE_LOCATION = new ResourceLocation("textures/misc/vignette.png");

    public static void renderOverlay(GuiGraphics graphics, float tickDelta) {
        Player player = minecraft.player;
        if (player != null && player.getItemBySlot(EquipmentSlot.HEAD).is(ModItems.MARK_42_HELMET.get())) {
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderUtil.blit(graphics, VIGNETTE_LOCATION, 0, 0, -90, 0.0F, 0.0F, graphics.guiWidth(), graphics.guiHeight(), graphics.guiWidth(), graphics.guiHeight(), 1.0F, 1.0F, 255.0F / 4, 1.0F / 4);
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.defaultBlendFunc();
        }
    }
}
