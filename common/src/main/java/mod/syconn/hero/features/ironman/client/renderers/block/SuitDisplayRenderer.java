package mod.syconn.hero.features.ironman.client.renderers.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mod.syconn.hero.client.model.DisplayDoorModel;
import mod.syconn.hero.features.ironman.blockentity.SuitDisplayBlockEntity;
import mod.syconn.hero.features.ironman.client.renderers.entity.IronmanArmorRenderer;
import mod.syconn.hero.utils.Constants;
import mod.syconn.hero.utils.generic.ModelUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class SuitDisplayRenderer implements BlockEntityRenderer<SuitDisplayBlockEntity> {

    private static final ResourceLocation DOOR_TEXTURE = Constants.withId("textures/block/door.png");
    private final DisplayDoorModel door;
    private final IronmanArmorRenderer renderer;

    public SuitDisplayRenderer(BlockEntityRendererProvider.Context context) {
        this.door = new DisplayDoorModel(context::bakeLayer);
        this.renderer = new IronmanArmorRenderer(context::bakeLayer);
    }

    @Override
    public void render(SuitDisplayBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        final var facing = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);

        poseStack.pushPose();
        float open = -90f * blockEntity.getOpenProgress(partialTick);
        ModelUtil.translateRotation(poseStack, Direction.NORTH, facing, 0.125f, 0.440625f, 0.71875f);
        poseStack.mulPose(Axis.YP.rotationDegrees(getCustomYRot(facing) + open));
        this.door.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityTranslucent(DOOR_TEXTURE)), packedLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
        poseStack.popPose();

        var spin = blockEntity.getSuitSpin(partialTick);
//        System.out.println(spin);

        poseStack.pushPose();
        ModelUtil.translateRotation(poseStack, Direction.NORTH, facing, 0.5f, 0.2f, 0.4f);
        poseStack.mulPose(Axis.YP.rotationDegrees(getCustomYRot(facing) + 180f * Mth.clamp(spin, 0, 1)));
        poseStack.scale(0.8f, 0.8f, 0.8f);
        renderer.setGear(blockEntity.getGear());
        renderer.render(poseStack, buffer, packedLight);
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
