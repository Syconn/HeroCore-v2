package mod.syconn.hero.client.screen.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;

public class IronmanOverlay {

    private static final Minecraft minecraft = Minecraft.getInstance();

    public static void renderOverlay(GuiGraphics graphics, float tickDelta) {
        Player player = minecraft.player;
        if (player != null) {
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            TextureAtlasSprite textureatlassprite = minecraft.getBlockRenderer().getBlockModelShaper().getParticleIcon(Blocks.NETHER_PORTAL.defaultBlockState());
            graphics.blit(0, 0, -90, graphics.guiWidth(), graphics.guiHeight(), textureatlassprite);
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
