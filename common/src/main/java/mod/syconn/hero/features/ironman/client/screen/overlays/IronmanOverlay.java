package mod.syconn.hero.features.ironman.client.screen.overlays;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mod.syconn.hero.features.heros.interfaces.IHeroHolder;
import mod.syconn.hero.features.ironman.Ironman;
import mod.syconn.hero.features.ironman.abilities.FlightAbility;
import mod.syconn.hero.features.ironman.abilities.VisorAbility;
import mod.syconn.hero.utils.Constants;
import mod.syconn.hero.utils.generic.GraphicsUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.levelgen.Heightmap;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class IronmanOverlay {

    private static final Minecraft minecraft = Minecraft.getInstance();
    private static final ResourceLocation VIGNETTE_LOCATION = new ResourceLocation("textures/misc/vignette.png");
    private static final ResourceLocation IRON_MAN = Constants.withId("textures/gui/hud/ironman_hud.png");

    public static void renderOverlay(GuiGraphics graphics, float tickDelta) {
        Player player = getCameraPlayer();
        if (player instanceof IHeroHolder holder) {
            var ironman = holder.hero$getManager().getType(Ironman.class);
            if (ironman.getAbility(VisorAbility.class).usable(player)) {
                // Blue Glint
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                GraphicsUtil.blit(graphics, VIGNETTE_LOCATION, 0, 0, -90, 0.0F, 0.0F, graphics.guiWidth(), graphics.guiHeight(), graphics.guiWidth(), graphics.guiHeight(), 1.0F, 1.0F, 255.0F, 0.9F);
                RenderSystem.depthMask(true);
                RenderSystem.enableDepthTest();
                graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.defaultBlendFunc();

                // Hover Math
                var height = player.level().getHeight(Heightmap.Types.MOTION_BLOCKING, player.getBlockX(), player.getBlockZ());
                var maxHeight = player.level().getHeight(Heightmap.Types.MOTION_BLOCKING, player.getBlockX(), player.getBlockZ()) + Constants.CONFIG.serverSettings.maxHoverHeight.get();
                int playerY = (int) player.getY();

                // Text
                PoseStack poseStack = graphics.pose(); // MOVEMENT SPEED?
                poseStack.pushPose();
                poseStack.scale(0.5f, 0.5f, 0.5f);
                graphics.drawString(minecraft.font, "Suit Information", 5, 5, DyeColor.LIGHT_BLUE.getTextColor()); // TODO ADD AN ACTUALLY SPACING SYSTEM
                if (ironman.getAbility(FlightAbility.class).getMode() == FlightAbility.FlightMode.HOVER) graphics.drawString(minecraft.font, "Hovering " + Math.max(playerY - height, 0) + " blocks / " + (maxHeight - height) + " blocks", 5, 20, DyeColor.LIGHT_BLUE.getTextColor());
//                graphics.drawString(minecraft.font, "Status:", 5, 20, DyeColor.LIGHT_BLUE.getTextColor());
    //            graphics.drawString(minecraft.font, online ? "Online" : "Offline", 42, 20, online ? DyeColor.GREEN.getTextColor() : DyeColor.RED.getTextColor());

    //            int percentage = EnergyUtil.getSuitPercentage(player);
    //            int percent_color = percentage >= 75 ? DyeColor.GREEN.getTextColor() : percentage >= 30 ? DyeColor.YELLOW.getTextColor() : DyeColor.RED.getTextColor();
                graphics.drawString(minecraft.font, "Energy:", 5, 35, DyeColor.LIGHT_BLUE.getTextColor());
    //            graphics.drawString(minecraft.font, percentage + "%", 47, 35, percent_color);
                graphics.drawString(minecraft.font, "Flight Mode: ", 5, 50, DyeColor.LIGHT_BLUE.getTextColor());
                graphics.drawString(minecraft.font, ironman.getAbility(FlightAbility.class).getMode().toString(), 65, 50, DyeColor.GREEN.getTextColor()); // percent_color
//                renderPlayerHologram(graphics, 45, graphics.guiHeight() * 2 - 30, 50, player);
                renderCombatHotbar(graphics);

                poseStack.popPose();
            }
        }
    }

    private static void renderPlayerHologram(GuiGraphics guiGraphics, int x, int y, float scale, LivingEntity entity) {
        var player = Minecraft.getInstance().player;

        // Save original rotations
        float oldBodyRot = entity.yBodyRot;
        float oldBodyRotO = entity.yBodyRotO;
        float oldYaw = entity.getYRot();
        float oldPitch = entity.getXRot();
        float oldHeadRot = entity.yHeadRot;
        float oldHeadRotO = entity.yHeadRotO;

        try {
            float cameraYaw = Minecraft.getInstance().gameRenderer.getMainCamera().getYRot();
            float relativeYaw = Mth.wrapDegrees(player.getYRot() - cameraYaw);
            float bodyYaw = 180.0F - relativeYaw;
            float headOffset = player.yHeadRot - player.yBodyRot;
            entity.yBodyRot = bodyYaw;
            entity.yBodyRotO = bodyYaw;
            entity.setYRot(bodyYaw);
            entity.setXRot(player.getXRot());
            entity.yHeadRot = bodyYaw + headOffset;
            entity.yHeadRotO = bodyYaw + headOffset;

            float hurtLevel = 5.0F - entity.getHealth() / entity.getMaxHealth() * 4.0F;
            guiGraphics.setColor(hurtLevel, 1.0F, 4.0F, 1.0F);
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(x, y, 50);
            guiGraphics.pose().mulPoseMatrix(new Matrix4f().scaling(scale, scale, -scale));
            guiGraphics.pose().mulPose(new Quaternionf().rotateZ((float) Math.PI));
            Lighting.setupForEntityInInventory();
            EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
            dispatcher.setRenderShadow(false);
            dispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, guiGraphics.pose(), guiGraphics.bufferSource(), 15728880);
            guiGraphics.flush();
            dispatcher.setRenderShadow(true);
            guiGraphics.pose().popPose();

            Lighting.setupFor3DItems();
            guiGraphics.setColor(1F, 1F, 1F, 1F);
        } finally {
            // Restore original rotations
            entity.yBodyRot = oldBodyRot;
            entity.yBodyRotO = oldBodyRotO;
            entity.setYRot(oldYaw);
            entity.setXRot(oldPitch);
            entity.yHeadRot = oldHeadRot;
            entity.yHeadRotO = oldHeadRotO;
        }

    }

    private static void renderCombatHotbar(GuiGraphics graphics) {

    }

    private static Player getCameraPlayer() {
        return !(minecraft.getCameraEntity() instanceof Player) ? null : (Player) minecraft.getCameraEntity();
    }
}
