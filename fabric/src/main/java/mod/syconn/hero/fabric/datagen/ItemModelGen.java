package mod.syconn.hero.fabric.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;

public class ItemModelGen extends FabricModelProvider {

    public ItemModelGen(FabricDataOutput output) {
        super(output);
    }

    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        
    }

    public void generateItemModels(ItemModelGenerators itemModelGenerator) {

    }
}
