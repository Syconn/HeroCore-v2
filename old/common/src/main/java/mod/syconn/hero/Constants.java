package mod.syconn.hero;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

	public static final String MOD_ID = "hero";
	public static final String MOD_NAME = "HeroCore";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	public static ResourceLocation withId(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}