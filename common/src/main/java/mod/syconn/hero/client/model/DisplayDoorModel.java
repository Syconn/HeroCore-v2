package mod.syconn.hero.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.syconn.hero.utils.Constants;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;

public class DisplayDoorModel extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Constants.withId("door"), "main");
    private final ModelPart bot;
    private final ModelPart top;

    public DisplayDoorModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.bot = root.getChild("bot");
        this.top = root.getChild("top");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("bot", CubeListBuilder.create().texOffs(0, 15).addBox(-13.0F, -14.0F, -1.0F, 14.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0F, 24.0F, 0.0F));
        partdefinition.addOrReplaceChild("top", CubeListBuilder.create().texOffs(0, 0).addBox(-13.0F, -28.0F, -1.0F, 14.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0F, 24.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.top.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        this.bot.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
