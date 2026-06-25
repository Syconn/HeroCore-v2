package mod.syconn.hero.features.ironman.client.screen;

import mod.syconn.hero.features.ironman.server.menu.SuitDisplayMenu;
import mod.syconn.hero.utils.Constants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.DyeColor;

public class SuitDisplayScreen extends AbstractContainerScreen<SuitDisplayMenu> {

    private static final ResourceLocation SCREEN = Constants.withId("textures/gui/suit_display.png");
    private static final ResourceLocation ARC = Constants.withId("textures/gui/arc_reactor.png");

    public SuitDisplayScreen(SuitDisplayMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(SCREEN, i, j, 0, 0, this.imageWidth, this.imageHeight);

        if (minecraft != null && minecraft.player != null) {
            int arcX = 84, arcY = 39;
            int renderX = i + arcX + 4, renderY = j + arcY + 4;
            var time = (minecraft.player.tickCount + partialTick) * 0.1f;
            var arcSize = 256;
//            var glow = 0.92f + 0.08f * Mth.sin(time); // TODO ADD BACK
            var glow = 1.0f;
            var scale = 0.35f;
            var pose = guiGraphics.pose();
            var color = DyeColor.GRAY.getFireworkColor(); // TODO PULL COLOR OR THIS
            float r = ((color >> 16) & 0xFF) / 255f, g = ((color >> 8) & 0xFF) / 255f, b = (color & 0xFF) / 255f;

            pose.pushPose();
            pose.translate(renderX, renderY, 0);
            pose.scale(scale, scale, 1);
            pose.translate(-arcSize / 2f, -arcSize / 2f, 0);
            guiGraphics.setColor(r * glow, g * glow, b * glow, 1f);
            guiGraphics.blit(ARC, 0, 0, 0, 0, arcSize, arcSize, arcSize, arcSize);
            guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            pose.popPose();
        }
    }
}
