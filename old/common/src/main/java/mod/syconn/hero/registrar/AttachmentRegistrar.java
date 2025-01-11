package mod.syconn.hero.registrar;

import mod.syconn.hero.extra.data.attachment.IAttachmentType;
import mod.syconn.hero.extra.data.attachment.SuperPower;
import mod.syconn.hero.extra.platform.Services;

import java.util.function.Supplier;

public class AttachmentRegistrar {
    
    public static String SUPER_POWER = register(SuperPower::new);

    public static void init() {}

    private static <T extends IAttachmentType<T>> String register(Supplier<T> type) {
        return Services.ATTACHED_DATA.registerType(type.get().id(), type);
    }
}
