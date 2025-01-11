package mod.syconn.hero.datagen;

import mod.syconn.hero.Constants;
import mod.syconn.hero.registrar.ItemRegistrar;
import mod.syconn.hero.registrar.TagRegistrar;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ItemTagGen extends ItemTagsProvider {

    public ItemTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, Constants.MOD_ID, existingFileHelper);
    }

    protected void addTags(HolderLookup.Provider provider) {
        this.tag(TagRegistrar.TITANIUM_PLATE).add(ItemRegistrar.TITANIUM_PLATE.get());
    }
}
