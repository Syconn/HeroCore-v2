package mod.syconn.hero.fabric.datagen;

import mod.syconn.hero.core.ModItems;
import mod.syconn.hero.core.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ItemTagGen extends FabricTagProvider.ItemTagProvider {

    public ItemTagGen(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    protected void addTags(HolderLookup.Provider arg) {
        this.tag(ModTags.TITANIUM_PLATE).add(reverseLookup(ModItems.TITANIUM_PLATE.get()));
    }
}
