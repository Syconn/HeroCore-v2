package mod.syconn.hero.client.screen.overlay;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mod.syconn.hero.common.components.EnergyComponent;
import mod.syconn.hero.common.components.SuitComponent;
import mod.syconn.hero.core.ModItems;
import mod.syconn.hero.util.ItemUtil;
import mod.syconn.hero.util.RenderUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class IronmanOverlay {

    private static final Minecraft minecraft = Minecraft.getInstance();
    private static final ResourceLocation VIGNETTE_LOCATION = ResourceLocation.withDefaultNamespace("textures/misc/vignette.png");

    public static void renderOverlay(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Player player = minecraft.player;
        if (player != null && player.getItemBySlot(EquipmentSlot.HEAD).is(ModItems.MARK_42_HELMET.get())) {
            boolean online = ItemUtil.isWearingIronManSuit(player);

            // Blue Glint
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderUtil.blit(graphics, VIGNETTE_LOCATION, 0, 0, -90, 0.0F, 0.0F, graphics.guiWidth(), graphics.guiHeight(), graphics.guiWidth(), graphics.guiHeight(), 1.0F, 1.0F, 255.0F / 2, 0.35F);
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.defaultBlendFunc();

            // Text
            PoseStack poseStack = graphics.pose();
            poseStack.pushPose();
            poseStack.scale(0.5f, 0.5f, 0.5f);
            graphics.drawString(minecraft.font, "Suit Information", 5, 5, DyeColor.LIGHT_BLUE.getTextColor());
            graphics.drawString(minecraft.font, "Status:", 5, 20, DyeColor.LIGHT_BLUE.getTextColor());
            graphics.drawString(minecraft.font, online ? "Online" : "Offline", 42, 20, online ? DyeColor.GREEN.getTextColor() : DyeColor.RED.getTextColor());
            if (online) {
                SuitComponent settings = SuitComponent.from(player);
                int percentage = EnergyComponent.getSuitPercentage(player);
                int percent_color = percentage >= 75 ? DyeColor.GREEN.getTextColor() : percentage >= 30 ? DyeColor.YELLOW.getTextColor() : DyeColor.RED.getTextColor();
                graphics.drawString(minecraft.font, "Energy:", 5, 35, DyeColor.LIGHT_BLUE.getTextColor());
                graphics.drawString(minecraft.font, percentage + "%", 47, 35, percent_color);
                graphics.drawString(minecraft.font, "Flight Mode: ", 5, 50, DyeColor.LIGHT_BLUE.getTextColor());
                graphics.drawString(minecraft.font, settings.flightMode().name(), 65, 50, percent_color);
                renderPlayerHologram(graphics, 35, graphics.guiHeight() * 2 - 30, 30, player);
            }
            poseStack.popPose();
        }
    }

    public static void renderPlayerHologram(GuiGraphics guiGraphics, int x, int y, float scale, LivingEntity entity) {
        float f = (float) Math.atan(0);
        float g = (float) Math.atan(0);
        Quaternionf quaternionf = new Quaternionf().rotateZ((float) Math.PI);
        quaternionf.mul(new Quaternionf().rotateX(g * 20.0F * (float) (Math.PI / 180.0)));
        float h = entity.yBodyRot;
        float i = entity.getYRot();
        float j = entity.getXRot();
        float k = entity.yHeadRotO;
        float l = entity.yHeadRot;
        entity.yBodyRot = 180.0F + f * 20.0F;
        entity.setYRot(180.0F + f * 40.0F);
        entity.setXRot(-g * 20.0F);
        entity.yHeadRot = entity.getYRot();
        entity.yHeadRotO = entity.getYRot();

        float hurt_level = 5.0f - entity.getHealth() / entity.getMaxHealth() * 4.0f;
        guiGraphics.setColor(hurt_level, 1.0F, 4.0F, 1.0F);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 50);
        guiGraphics.pose().mulPose(new Matrix4f().scaling(scale, scale, -scale));
        guiGraphics.pose().mulPose(new Quaternionf().rotateZ((float) Math.PI));
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        entityRenderDispatcher.setRenderShadow(false);
        RenderSystem.runAsFancy(() -> entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, guiGraphics.pose(), guiGraphics.bufferSource(), 15728880));
        guiGraphics.flush();
        entityRenderDispatcher.setRenderShadow(true);
        guiGraphics.pose().popPose();
        Lighting.setupFor3DItems();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        entity.yBodyRot = h;
        entity.setYRot(i);
        entity.setXRot(j);
        entity.yHeadRotO = k;
        entity.yHeadRot = l;
    }
}
