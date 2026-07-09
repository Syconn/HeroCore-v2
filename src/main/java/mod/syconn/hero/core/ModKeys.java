package mod.syconn.hero.core;

import mod.syconn.hero.utils.Constants;
import mod.syconn.hero.utils.HeroConfig;
import mod.syconn.hero.utils.config.ConfigKey;
import mod.syconn.hero.utils.config.ConfigEntry;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ModKeys {

    public static List<KeyMapping> KEYS = new ArrayList<>();

    public static KeyMapping EDIT_SETTINGS = registerKeybind("hero_settings", GLFW.GLFW_KEY_Y);

    @ConfigKey({
            @ConfigEntry(hero = HeroConfig.IRONMAN, desc = "Toggle Helmet Overlay")
    })
    public static KeyMapping MODIFY_OVERLAY = lazyRegister("Modify Overlay", GLFW.GLFW_KEY_M);

    @ConfigKey({
            @ConfigEntry(hero = HeroConfig.IRONMAN, desc = "Flip Helmet")
    })
    public static KeyMapping MODIFY_EQUIPMENT = lazyRegister("Modify Equipment", GLFW.GLFW_KEY_O);

    @ConfigKey({
            @ConfigEntry(hero = HeroConfig.IRONMAN, desc = "Toggle Flight Mode")
    })
    public static KeyMapping TOGGLE_FLIGHT = lazyRegister("Toggle Flight", GLFW.GLFW_KEY_N);

    public static String modCategory() {
        return "key.categories." + Constants.MOD;
    }

    private static KeyMapping registerKeybind(String name, int keyCode) {
        var key = new KeyMapping("key." + Constants.MOD + "." + name, keyCode, modCategory());
        KEYS.add(key);
        return key;
    }

    private static KeyMapping lazyRegister(String name, int keyCode) {
        var key = new KeyMapping(name, keyCode, modCategory());
        KEYS.add(key);
        return key;
    }
}
