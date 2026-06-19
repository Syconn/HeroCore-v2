package mod.syconn.hero.core;

import me.fzzyhmstrs.fzzy_config.screen.context.ContextInput;
import me.fzzyhmstrs.fzzy_config.validation.ValidatedField;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedKeybind;
import mod.syconn.hero.utils.Constants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ModKeys {

    public static List<KeyMapping> KEYS = new ArrayList<>();

    public static KeyMapping EDIT_SETTINGS = registerKeybind("hero_settings", GLFW.GLFW_KEY_Y);

    public static String modCategory() {
        return "key.categories." + Constants.MOD;
    }

    private static KeyMapping registerKeybind(String name, int keyCode) {
        var key = new KeyMapping("key." + Constants.MOD + "." + name, keyCode, modCategory());
        KEYS.add(key);
        return key;
    }
}
