package mod.syconn.hero.core;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.syconn.hero.item.IronmanArmorItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

import static mod.syconn.hero.utils.Constants.MOD;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD, Registries.ITEM);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(MOD, Registries.CREATIVE_MODE_TAB);
    
    public static final RegistrySupplier<Item> MARK_2_HELMET = registerItem("mark_2_helmet", properties -> new IronmanArmorItem(ModArmors.MARK_2, ArmorItem.Type.HELMET, properties));
    public static final RegistrySupplier<Item> MARK_2_CHESTPLATE = registerItem("mark_2_chestplate", properties -> new IronmanArmorItem(ModArmors.MARK_2, ArmorItem.Type.CHESTPLATE, properties));
    public static final RegistrySupplier<Item> MARK_2_LEGGINGS = registerItem("mark_2_leggings", properties -> new IronmanArmorItem(ModArmors.MARK_2, ArmorItem.Type.LEGGINGS, properties));
    public static final RegistrySupplier<Item> MARK_2_BOOTS = registerItem("mark_2_boots", properties -> new IronmanArmorItem(ModArmors.MARK_2, ArmorItem.Type.BOOTS, properties));

    public static final RegistrySupplier<Item> MARK_5_HELMET = registerItem("mark_5_helmet", properties -> new IronmanArmorItem(ModArmors.MARK_5, ArmorItem.Type.HELMET, properties));
    public static final RegistrySupplier<Item> MARK_5_CHESTPLATE = registerItem("mark_5_chestplate", properties -> new IronmanArmorItem(ModArmors.MARK_5, ArmorItem.Type.CHESTPLATE, properties));
    public static final RegistrySupplier<Item> MARK_5_LEGGINGS = registerItem("mark_5_leggings", properties -> new IronmanArmorItem(ModArmors.MARK_5, ArmorItem.Type.LEGGINGS, properties));
    public static final RegistrySupplier<Item> MARK_5_BOOTS = registerItem("mark_5_boots", properties -> new IronmanArmorItem(ModArmors.MARK_5, ArmorItem.Type.BOOTS, properties));

    public static final RegistrySupplier<Item> MARK_42_HELMET = registerItem("mark_42_helmet", properties -> new IronmanArmorItem(ModArmors.MARK_42, ArmorItem.Type.HELMET, properties));
    public static final RegistrySupplier<Item> MARK_42_CHESTPLATE = registerItem("mark_42_chestplate", properties -> new IronmanArmorItem(ModArmors.MARK_42, ArmorItem.Type.CHESTPLATE, properties));
    public static final RegistrySupplier<Item> MARK_42_LEGGINGS = registerItem("mark_42_leggings", properties -> new IronmanArmorItem(ModArmors.MARK_42, ArmorItem.Type.LEGGINGS, properties));
    public static final RegistrySupplier<Item> MARK_42_BOOTS = registerItem("mark_42_boots", properties -> new IronmanArmorItem(ModArmors.MARK_42, ArmorItem.Type.BOOTS, properties));

    public static final RegistrySupplier<Item> MARK_43_HELMET = registerItem("mark_43_helmet", properties -> new IronmanArmorItem(ModArmors.MARK_43, ArmorItem.Type.HELMET, properties));
    public static final RegistrySupplier<Item> MARK_43_CHESTPLATE = registerItem("mark_43_chestplate", properties -> new IronmanArmorItem(ModArmors.MARK_43, ArmorItem.Type.CHESTPLATE, properties));
    public static final RegistrySupplier<Item> MARK_43_LEGGINGS = registerItem("mark_43_leggings", properties -> new IronmanArmorItem(ModArmors.MARK_43, ArmorItem.Type.LEGGINGS, properties));
    public static final RegistrySupplier<Item> MARK_43_BOOTS = registerItem("mark_43_boots", properties -> new IronmanArmorItem(ModArmors.MARK_43, ArmorItem.Type.BOOTS, properties));

    public static final RegistrySupplier<Item> WAR_MACHINE_HELMET = registerItem("war_machine_helmet", properties -> new IronmanArmorItem(ModArmors.WAR_MACHINE, ArmorItem.Type.HELMET, properties));
    public static final RegistrySupplier<Item> WAR_MACHINE_CHESTPLATE = registerItem("war_machine_chestplate", properties -> new IronmanArmorItem(ModArmors.WAR_MACHINE, ArmorItem.Type.CHESTPLATE, properties));
    public static final RegistrySupplier<Item> WAR_MACHINE_LEGGINGS = registerItem("war_machine_leggings", properties -> new IronmanArmorItem(ModArmors.WAR_MACHINE, ArmorItem.Type.LEGGINGS, properties));
    public static final RegistrySupplier<Item> WAR_MACHINE_BOOTS = registerItem("war_machine_boots", properties -> new IronmanArmorItem(ModArmors.WAR_MACHINE, ArmorItem.Type.BOOTS, properties));

    public static final RegistrySupplier<CreativeModeTab> TAB = TABS.register("hero_tab", () -> CreativeTabRegistry.create(Component.translatable("itemGroup." + MOD + ".hero_items"),
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
