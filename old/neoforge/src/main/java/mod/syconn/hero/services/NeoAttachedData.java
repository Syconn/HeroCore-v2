package mod.syconn.hero.services;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import mod.syconn.hero.extra.data.attachment.IAttachmentType;
import mod.syconn.hero.extra.platform.services.IAttachedData;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static mod.syconn.hero.HeroNeo.ATTACHMENT_TYPES;

public class NeoAttachedData implements IAttachedData {

    private static final Map<String, Supplier<AttachmentType<?>>> registrar = new HashMap<>();

    public <T extends IAttachmentType<T>> String registerType(String id, Supplier<T> typeSupplier) {
        DeferredHolder<AttachmentType<?>, AttachmentType<?>> type = ATTACHMENT_TYPES.register(id, () ->
                typeSupplier.get().copyOnDeath() ? AttachmentType.builder(typeSupplier).serialize(typeSupplier.get().codec()).copyOnDeath().build() : AttachmentType.builder(typeSupplier).serialize(typeSupplier.get().codec()).build());
        registrar.put(id, type);
        return id;
    }

    public <T extends IAttachmentType<T>> T get(String id, Player entity) {
        return entity.getData(getType(id));
    }

    public <T extends IAttachmentType<T>> void set(String id, T data, Player entity) {
        entity.setData(getType(id), data);
    }

    public <T extends IAttachmentType<T>> void update(String id, Function<T, T> action, Player entity) {
        entity.setData(getType(id), action.apply(get(id, entity)));
    }

    public <T extends IAttachmentType<T>> boolean has(String id, Player player) {
        return player.hasData(getType(id));
    }

    @SuppressWarnings("unchecked")
    private <T> AttachmentType<T> getType(String id) {
        AttachmentType<T> typeSupplier = (AttachmentType<T>) registrar.get(id).get();
        if(typeSupplier == null) throw new IllegalArgumentException("Unregistered attachment: " + id);
        return typeSupplier;
    }
}
