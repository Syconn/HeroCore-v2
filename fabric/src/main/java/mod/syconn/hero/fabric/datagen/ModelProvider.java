package mod.syconn.hero.fabric.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mod.syconn.hero.core.ModBlocks;
import mod.syconn.hero.utils.Constants;
import mod.syconn.hero.utils.block.ModBlockStateProperties;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ModelProvider extends FabricModelProvider {

    public ModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generator) {
        var gen = generator.blockStateOutput;

        generator.skipAutoItemBlock(ModBlocks.SUIT_DISPLAY.get());

        gen.accept(MultiVariantGenerator.multiVariant(ModBlocks.SUIT_DISPLAY.get()).with(
                PropertyDispatch.property(ModBlockStateProperties.TWO_PART).generate(f -> Variant.variant()
                        .with(VariantProperties.MODEL, Constants.withId(getId(ModBlocks.SUIT_DISPLAY.get()) + (f.down() ? "_down" : "_top"))))).with(PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING)
                        .generate(f -> Variant.variant().with(VariantProperties.Y_ROT, f == Direction.NORTH ? VariantProperties.Rotation.R0 : f == Direction.WEST ? VariantProperties.Rotation.R270 : f == Direction.EAST ? VariantProperties.Rotation.R90 : VariantProperties.Rotation.R180))
                )
        );
    }

    @Override
    public void generateItemModels(ItemModelGenerators generator) {
//        var gen = generator.output;
//        generator.generateFlatItem(ModItems.SCREEN.get(), ModelTemplates.FLAT_ITEM);
//        gen.accept(ModelLocationUtils.getModelLocation(ModItems.LIGHTSABER.get()), () -> parent("lightsaber/yoda"));
    }

    private static JsonObject parent(String parent) {
        var json = new JsonObject();
        json.addProperty("parent", Constants.MOD + ":item/" + parent);
        return json;
    }

    private static void override(JsonObject parent, ResourceLocation model, ResourceLocation id, double index) {
        var array = parent.has("overrides") ? parent.get("overrides").getAsJsonArray() : new JsonArray();

        var override = new JsonObject();
        var predicate = new JsonObject();
        predicate.addProperty(id.toString(), index);
        override.add("predicate", predicate);
        override.addProperty("model", model.toString());
        array.add(override);

        parent.add("overrides", array);
    }

    private String getId(Block block) {
        return "block/" + BuiltInRegistries.BLOCK.getKey(block).getPath();
    }
}
