package mod.syconn.hero.core;

import dev.architectury.registry.CreativeTabOutput;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.syconn.hero.features.addons.IronmanContent;
import mod.syconn.hero.item.IronmanArmorItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.*;

import java.util.function.Function;

import static mod.syconn.hero.utils.Constants.MOD;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD, Registries.ITEM);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(MOD, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<Item> IRONMAN_HELMET = registerItem("helmet", properties -> new IronmanArmorItem(ArmorItem.Type.HELMET, properties));
    public static final RegistrySupplier<Item> IRONMAN_CHESTPLATE = registerItem("chestplate", properties -> new IronmanArmorItem(ArmorItem.Type.CHESTPLATE, properties));
    public static final RegistrySupplier<Item> IRONMAN_LEGGINGS = registerItem("leggings", properties -> new IronmanArmorItem(ArmorItem.Type.LEGGINGS, properties));
    public static final RegistrySupplier<Item> IRONMAN_BOOTS = registerItem("boots", properties -> new IronmanArmorItem(ArmorItem.Type.BOOTS, properties));

    public static final RegistrySupplier<CreativeModeTab> TAB = TABS.register("hero_tab", () -> CreativeTabRegistry.create(Component.translatable("itemGroup." + MOD + ".hero_items"), () -> new ItemStack(IRONMAN_HELMET.get())));

    @SuppressWarnings("UnstableApiUsage")
    public static void addCreative(FeatureFlagSet flags, CreativeTabOutput output, boolean canUseGameMasterBlocks) {
        output.acceptAll(IronmanContent.createArmorTypes(), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
    }

    private static <T extends Item> RegistrySupplier<T> registerItem(String id, Function<Item.Properties, T> factory) {
        return registerItem(id, factory, new Item.Properties(), true);
    }

    @SuppressWarnings("UnstableApiUsage")
    private static <T extends Item> RegistrySupplier<T> registerItem(String id, Function<Item.Properties, T> factory, Item.Properties properties, boolean noTab) {
        return ITEMS.register(id, () -> factory.apply(noTab ? properties : properties.arch$tab(TAB)));
    }
}
