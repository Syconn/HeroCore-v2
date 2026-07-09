package mod.syconn.hero.utils;

import mod.syconn.hero.utils.config.ExtendedConfig;

public class HeroConfig extends ExtendedConfig {
    public static final String SERVER = "server";
    public static final String IRONMAN = "ironman";

    @Server
    @Entry(category = SERVER, name = "Max Hover Height", min = 0)
    public static int maxHoverHeight = 40;

    @Client
    @Entry(category = IRONMAN, name = "Show Active Helmet Overlay")
    public static boolean ironmanOverlay = true;

    @Client
    @Entry(category = IRONMAN, name = "Obfuscated Helmet Overlay Mode")
    public static OverlayMode overlayMode = OverlayMode.Enabled;
    public enum OverlayMode { Enabled, Disabled, Configurable }

    @Client
    @Condition(requiredOption = "overlayMode", requiredValue = "Configurable")
    @Entry(category = IRONMAN, isSlider = true, min = 0f, max = 1f, precision = 1000, name = "Helmet Alpha Obfuscation")
    public static float helmetObfuscationAlpha = 1.0f;
}
