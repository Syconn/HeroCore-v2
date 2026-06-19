package mod.syconn.hero.fabric.datagen;

import dev.architectury.platform.Mod;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.syconn.hero.core.ModItems;
import mod.syconn.hero.core.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class ItemTagGen extends FabricTagProvider.ItemTagProvider {

    public ItemTagGen(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    protected void addTags(HolderLookup.Provider arg) {
        this.addAll(ModTags.MARK_2, ModItems.MARK_2_BOOTS, ModItems.MARK_2_LEGGINGS, ModItems.MARK_2_CHESTPLATE, ModItems.MARK_2_HELMET);
        this.addAll(ModTags.MARK_5, ModItems.MARK_5_BOOTS, ModItems.MARK_5_LEGGINGS, ModItems.MARK_5_CHESTPLATE, ModItems.MARK_5_HELMET);
        this.addAll(ModTags.MARK_42, ModItems.MARK_42_BOOTS, ModItems.MARK_42_LEGGINGS, ModItems.MARK_42_CHESTPLATE, ModItems.MARK_42_HELMET);
        this.addAll(ModTags.MARK_43, ModItems.MARK_43_BOOTS, ModItems.MARK_43_LEGGINGS, ModItems.MARK_43_CHESTPLATE, ModItems.MARK_43_HELMET);
        this.addAll(ModTags.WAR_MACHINE, ModItems.WAR_MACHINE_BOOTS, ModItems.WAR_MACHINE_LEGGINGS, ModItems.WAR_MACHINE_CHESTPLATE, ModItems.WAR_MACHINE_HELMET);
        this.addAllTags(ModTags.IRONMAN_ARMOR, ModTags.MARK_2, ModTags.MARK_5, ModTags.MARK_42, ModTags.MARK_43, ModTags.WAR_MACHINE);
    }

    @SafeVarargs
    private void addAll(TagKey<Item> tagKey, RegistrySupplier<Item>... items) {
        var tag = this.tag(tagKey);
        for (RegistrySupplier<Item> registryObject : items) tag.add(reverseLookup(registryObject.get()));
    }
    @SafeVarargs
    private void addAllTags(TagKey<Item> tagKey, TagKey<Item>... items) {
        var tag = this.tag(tagKey);
        for (TagKey<Item> key : items) tag.addTag(key);
    }

}
