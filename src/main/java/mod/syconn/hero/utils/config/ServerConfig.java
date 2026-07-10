package mod.syconn.hero.utils.config;

import eu.midnightdust.lib.config.MidnightConfig;

import static mod.syconn.hero.utils.HeroConfig.SERVER;

public class ServerConfig extends MidnightConfig {

    @Server
    @Entry(category = SERVER, name = "Max Hover Height", min = 0)
    public static int maxHoverHeight = 40;
}
