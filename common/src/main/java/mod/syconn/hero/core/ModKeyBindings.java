package mod.syconn.hero.core;

import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import mod.syconn.hero.Constants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {
    
    public static final KeyMapping ABILITY1 = new KeyMapping(keyId("use.ability1"), GLFW.GLFW_KEY_N, modCategory());
    public static final KeyMapping ABILITY2 = new KeyMapping(keyId("use.ability2"), GLFW.GLFW_KEY_M, modCategory());
    public static final KeyMapping ABILITY3 = new KeyMapping(keyId("use.ability3"), GLFW.GLFW_KEY_V, modCategory());
    public static final KeyMapping ABILITIES_MENU = new KeyMapping(keyId("menu.abilities"), GLFW.GLFW_KEY_B, modCategory());
    
    public static void registerMappings() {
        KeyMappingRegistry.register(ABILITIES_MENU);
        KeyMappingRegistry.register(ABILITY1);
        KeyMappingRegistry.register(ABILITY2);
        KeyMappingRegistry.register(ABILITY3);
    }

    public static String key(KeyMapping mapping) {
        return mapping.saveString().split("\\.")[2].toUpperCase();
    }
    
    private static String keyId(String id) {
        return "key." + Constants.MOD_ID + "." + id;
    }

    private static String modCategory() {
        return "key.categories." + Constants.MOD_ID;
    }
}
