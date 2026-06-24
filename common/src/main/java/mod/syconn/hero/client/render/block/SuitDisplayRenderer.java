package mod.syconn.hero.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mod.syconn.hero.blockentities.SuitDisplayBlockEntity;
import mod.syconn.hero.client.HeroClient;
import mod.syconn.hero.client.model.DisplayDoorModel;
import mod.syconn.hero.utils.Constants;
import mod.syconn.hero.utils.generic.ModelUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class SuitDisplayRenderer implements BlockEntityRenderer<SuitDisplayBlockEntity> {

    private final DisplayDoorModel door;

    public SuitDisplayRenderer(BlockEntityRendererProvider.Context context) {
        this.door = new DisplayDoorModel(context.bakeLayer(DisplayDoorModel.LAYER_LOCATION));
    }

    @Override
    public void render(SuitDisplayBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        float open = -90f * Mth.clamp(blockEntity.getOpenProgress(), 0f, 1f);
        final var facing = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        ModelUtil.translateRotation(poseStack, Direction.NORTH, facing, 0.125f, 0.440625f, 0.71875f);
        poseStack.mulPose(Axis.YP.rotationDegrees((float) (getCustomYRot(facing) + open)));
        this.door.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityTranslucent(Constants.withId("textures/block/door.png"))), packedLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);

        poseStack.popPose();
    }

    public static float getCustomYRot(Direction dir) {
        return switch (dir) {
            case NORTH -> 180f;
            case WEST  -> 270f;
            case EAST  -> 90F;
            default -> 0f;
        };
    }
}
