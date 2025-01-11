package mod.syconn.hero.extra.platform;

import mod.syconn.hero.Constants;
import mod.syconn.hero.extra.platform.services.IAttachedData;
import mod.syconn.hero.extra.platform.services.INetwork;
import mod.syconn.hero.extra.platform.services.IRegistrar;

import java.util.ServiceLoader;

public class Services {

    public static final IRegistrar REGISTRAR = load(IRegistrar.class);
    public static final INetwork NETWORK = load(INetwork.class);
    public static final IAttachedData ATTACHED_DATA = load(IAttachedData.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz).findFirst().orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
