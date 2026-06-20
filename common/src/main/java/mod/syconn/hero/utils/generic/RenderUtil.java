package mod.syconn.hero.utils.generic;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mod.syconn.hero.feature.ironman.abilities.FlightAbility;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class RenderUtil {

    public static void mixinTest(AbstractClientPlayer entityLiving, PoseStack poseStack, FlightAbility flightController, float partialTicks) {
        float g = flightController.getFlyingTicks() + partialTicks;
        float h = Mth.clamp(g * g / 100.0F, 0.0F, 1.0F);

        if (!entityLiving.isAutoSpinAttack()) poseStack.mulPose(Axis.XP.rotationDegrees(h * (-90.0F - entityLiving.getXRot())));
        Vec3 look = entityLiving.getViewVector(partialTicks);
        Vec3 motion = entityLiving.getDeltaMovementLerped(partialTicks);
        double d = motion.horizontalDistanceSqr();
        double e = look.horizontalDistanceSqr();
        if (d > 0.0 && e > 0.0) {
            double i = (motion.x * look.x + motion.z * look.z) / Math.sqrt(d * e);
            double j = motion.x * look.z - motion.z * look.x;
            poseStack.mulPose(Axis.YP.rotation((float)(Math.signum(j) * Math.acos(i))));
        }

        entityLiving.setYHeadRot(90f);
    }
}
