package mod.syconn.hero.registrar;

import mod.syconn.hero.Constants;
import mod.syconn.hero.extra.core.ArmorMaterials;
import mod.syconn.hero.extra.platform.Services;
import mod.syconn.hero.item.IronmanArmorItem;
import mod.syconn.hero.item.MjolnirItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.function.Function;
import java.util.function.Supplier;

public class ItemRegistrar {

    public static final Supplier<MjolnirItem> MJOLNIR = registerItem("mjolnir", MjolnirItem::new, new Item.Properties().stacksTo(1).rarity(Rarity.RARE));
    public static final Supplier<Item> SHURIKEN = registerItem("shuriken", new Item.Properties().stacksTo(16));
    public static final Supplier<Item> GAUNTLET = registerItem("repulsor", new Item.Properties().stacksTo(1));

    public static final Supplier<Item> MARK_42_HELMET = registerItem("mark_42_helmet", properties -> new IronmanArmorItem(ArmorMaterials.MARK_42, ArmorType.HELMET, properties));
    public static final Supplier<Item> MARK_42_CHESTPLATE = registerItem("mark_42_chestplate", properties -> new IronmanArmorItem(ArmorMaterials.MARK_42, ArmorType.CHESTPLATE, properties));
    public static final Supplier<Item> MARK_42_LEGGINGS = registerItem("mark_42_leggings", properties -> new IronmanArmorItem(ArmorMaterials.MARK_42, ArmorType.LEGGINGS, properties));
    public static final Supplier<Item> MARK_42_BOOTS = registerItem("mark_42_boots", properties -> new IronmanArmorItem(ArmorMaterials.MARK_42, ArmorType.BOOTS, properties));

    public static final Supplier<Item> INGOT_STEEL = registerItem("ingot_steel", new Item.Properties().stacksTo(64));
    public static final Supplier<Item> INGOT_TIN = registerItem("ingot_tin", new Item.Properties().stacksTo(64));
    public static final Supplier<Item> TITANIUM_PLATE = registerItem("titanium_plate", new Item.Properties().stacksTo(16));
    public static final Supplier<Item> ARC_REACTOR = registerItem("arc_reactor", new Item.Properties().stacksTo(1));

    public static final Supplier<CreativeModeTab> TAB = Services.REGISTRAR.registerCreativeModeTab("hero_items", () -> Services.REGISTRAR.newCreativeTabBuilder()
            .title(Component.translatable("itemGroup." + Constants.MOD_ID + ".hero_items"))
            .icon(() -> new ItemStack(MJOLNIR.get()))
            .displayItems((enabledFeatures, entries) -> {
                entries.accept(MJOLNIR.get());
                entries.accept(MARK_42_HELMET.get());
                entries.accept(MARK_42_CHESTPLATE.get());
                entries.accept(MARK_42_LEGGINGS.get());
                entries.accept(MARK_42_BOOTS.get());
                entries.accept(INGOT_STEEL.get());
                entries.accept(INGOT_TIN.get());
                entries.accept(TITANIUM_PLATE.get());
            }).build());

    public static void init() {}
    
    @SuppressWarnings("unchecked")
    private static <T extends Item> Supplier<T> registerItem(String id, Item.Properties properties) {
        return (Supplier<T>) registerItem(id, Item::new, properties);
    }

    private static <T extends Item> Supplier<T> registerItem(String id, Function<Item.Properties, T> factory) {
        return registerItem(id, factory, new Item.Properties());
    }

    private static <T extends Item> Supplier<T> registerItem(String id, Function<Item.Properties, T> factory, Item.Properties properties) {
        return Services.REGISTRAR.registerItem(id, () -> factory.apply(properties.setId(ItemId(id))));
    }

    private static ResourceKey<Item> ItemId(String id) {
        return ResourceKey.create(Registries.ITEM, Constants.withId(id));
    }
}
