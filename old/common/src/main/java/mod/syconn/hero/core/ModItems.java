package mod.syconn.hero.core;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.syconn.hero.common.item.IronmanArmorItem;
import mod.syconn.hero.common.item.MjolnirItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;

import java.util.function.Function;

import static mod.syconn.hero.Constants.MOD_ID;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registries.ITEM);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<MjolnirItem> MJOLNIR = registerItem("mjolnir", MjolnirItem::new, new Item.Properties().stacksTo(1).rarity(Rarity.RARE));
//    public static final RegistrySupplier<Item> SHURIKEN = registerItem("shuriken", new Item.Properties().stacksTo(16));
//    public static final RegistrySupplier<Item> GAUNTLET = registerItem("repulsor", new Item.Properties().stacksTo(1));
//    public static final RegistrySupplier<Item> ARC_REACTOR = registerItem("arc_reactor", new Item.Properties().stacksTo(1));
    
    public static final RegistrySupplier<Item> MARK_42_HELMET = registerItem("mark_42_helmet", properties -> new IronmanArmorItem(ModArmors.MARK_42, ArmorItem.Type.HELMET, properties));
    public static final RegistrySupplier<Item> MARK_42_CHESTPLATE = registerItem("mark_42_chestplate", properties -> new IronmanArmorItem(ModArmors.MARK_42, ArmorItem.Type.CHESTPLATE, properties));
    public static final RegistrySupplier<Item> MARK_42_LEGGINGS = registerItem("mark_42_leggings", properties -> new IronmanArmorItem(ModArmors.MARK_42, ArmorItem.Type.LEGGINGS, properties));
    public static final RegistrySupplier<Item> MARK_42_BOOTS = registerItem("mark_42_boots", properties -> new IronmanArmorItem(ModArmors.MARK_42, ArmorItem.Type.BOOTS, properties));

    public static final RegistrySupplier<Item> INGOT_STEEL = registerItem("ingot_steel", new Item.Properties().stacksTo(64));
    public static final RegistrySupplier<Item> INGOT_TIN = registerItem("ingot_tin", new Item.Properties().stacksTo(64));
    public static final RegistrySupplier<Item> TITANIUM_PLATE = registerItem("titanium_plate", new Item.Properties().stacksTo(16));

    public static final RegistrySupplier<CreativeModeTab> TAB = TABS.register("hero_tab", () -> CreativeTabRegistry.create(Component.translatable("itemGroup." + MOD_ID + ".hero_items"), 
            () -> new ItemStack(ModItems.MARK_42_HELMET.get())));
    
    @SuppressWarnings("unchecked")
    private static <T extends Item> RegistrySupplier<T> registerItem(String id, Item.Properties properties) {
        return (RegistrySupplier<T>) registerItem(id, Item::new, properties);
    }

    private static <T extends Item> RegistrySupplier<T> registerItem(String id, Function<Item.Properties, T> factory) {
        return registerItem(id, factory, new Item.Properties());
    }

    private static <T extends Item> RegistrySupplier<T> registerItem(String id, Function<Item.Properties, T> factory, Item.Properties properties) {
        return ITEMS.register(id, () -> factory.apply(properties.arch$tab(TAB)));
    }
}
