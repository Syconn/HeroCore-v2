package mod.syconn.hero.core;

import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.syconn.hero.features.ironman.client.screen.SuitDisplayScreen;
import mod.syconn.hero.features.ironman.server.menu.SuitDisplayMenu;
import mod.syconn.hero.utils.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

public class ModMenus {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Constants.MOD, Registries.MENU);

    public static final RegistrySupplier<MenuType<SuitDisplayMenu>> SUIT_DISPLAY = MENUS.register("suit_display", () -> MenuRegistry.ofExtended(SuitDisplayMenu::new));

    public static void registerScreens() {
        MenuRegistry.registerScreenFactory(SUIT_DISPLAY.get(), SuitDisplayScreen::new);
    }
}
