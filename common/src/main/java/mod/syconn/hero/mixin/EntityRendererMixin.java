package mod.syconn.hero.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.syconn.hero.core.ModItems;
import mod.syconn.hero.common.data.SuitSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {

    @Inject(at = @At("HEAD"), method = "render")
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        Player player = Minecraft.getInstance().player;
        if (player.getItemBySlot(EquipmentSlot.HEAD).is(ModItems.MARK_42_HELMET.get()) && !SuitSettings.from(player).isLifted() && entity instanceof LivingEntity living && entity != player) {
            double d = Minecraft.getInstance().getEntityRenderDispatcher().distanceToSqr(entity);
            String displayName = "HP " + (int) (living.getHealth() / living.getMaxHealth() * 100) + "%";
            if (!(d > 4096.0)) {
                float f = entity.getNameTagOffsetY();
                poseStack.pushPose();
                poseStack.translate(0.0F, f, 0.0F);
                poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
                poseStack.scale(-0.025F, -0.025F, 0.025F);
                Matrix4f matrix4f = poseStack.last().pose();
                float g = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
                int j = (int)(g * 255.0F) << 24;
                Font font = Minecraft.getInstance().font;
                float h = (float)(-font.width(displayName) / 2);
                font.drawInBatch(displayName, h, 0, DyeColor.LIGHT_BLUE.getTextColor(), false, matrix4f, buffer, Font.DisplayMode.NORMAL, j, packedLight);
                poseStack.popPose();
            }
        }
    }
}
