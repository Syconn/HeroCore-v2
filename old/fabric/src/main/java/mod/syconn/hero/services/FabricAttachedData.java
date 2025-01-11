package mod.syconn.hero.services;

import mod.syconn.hero.Constants;
import mod.syconn.hero.extra.data.attachment.IAttachmentType;
import mod.syconn.hero.extra.platform.services.IAttachedData;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("all")
public class FabricAttachedData implements IAttachedData {

    private static final Map<String, AttachmentType<?>> registrar = new HashMap<>();

    public <T extends IAttachmentType<T>> String registerType(String id, Supplier<T> typeSupplier) {
        AttachmentType<T> type = !typeSupplier.get().copyOnDeath() ? AttachmentRegistry.<T>builder().persistent(typeSupplier.get().codec()).initializer(typeSupplier).buildAndRegister(Constants.withId(id)) :
                AttachmentRegistry.<T>builder().persistent(typeSupplier.get().codec()).initializer(typeSupplier).copyOnDeath().buildAndRegister(Constants.withId(id));
        registrar.put(id, type);
        return id;
    }

    public <T extends IAttachmentType<T>> T get(String id, Player player) {
        return player.getAttachedOrCreate(getType(id));
    }

    public <T extends IAttachmentType<T>> void set(String id, T data, Player player) {
        player.setAttached(getType(id), data);
    }

    public <T extends IAttachmentType<T>> void update(String id, Function<T, T> action, Player player) {
        player.setAttached(getType(id), action.apply(get(id, player)));
    }

    public <T extends IAttachmentType<T>> boolean has(String id, Player player) {
        return player.hasAttached(getType(id));
    }

    private <T> AttachmentType<T> getType(String id) {
        AttachmentType<T> typeSupplier = (AttachmentType<T>) registrar.get(id);
        if(typeSupplier == null) throw new IllegalArgumentException("Unregistered attachment: " + id);
        return typeSupplier;
    }
}
