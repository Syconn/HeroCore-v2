package mod.syconn.hero.utils;

import mod.syconn.hero.utils.config.ExtendedConfig;

public class HeroConfig extends ExtendedConfig {
    public static final String SERVER = "server";
    public static final String IRONMAN = "ironman";

    @Server
    @Entry(category = SERVER, name = "Max Hover Height")
    public static int maxHoverHeight = 40;

    @Entry(category = SERVER) public static boolean ironmanOverlay;
}
