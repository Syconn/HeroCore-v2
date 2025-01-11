package mod.syconn.hero.services;

import mod.syconn.hero.capabilities.SuperPowerProvider;
import mod.syconn.hero.extra.data.attachment.IAttachmentType;
import mod.syconn.hero.extra.data.attachment.SuperPower;
import mod.syconn.hero.extra.platform.services.IAttachedData;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class ForgeAttachedData implements IAttachedData { // TODO HARD CODED CURRENTLY

    private static final Map<Class<?>, Capability<?>> registrar = new HashMap<>();

    public <T extends IAttachmentType<T>> String registerType(String id, Supplier<T> typeSupplier) {
        registrar.put(SuperPower.class, SuperPowerProvider.SUPER_POWER);
        return id;
    }

    @SuppressWarnings("all")
    public <T extends IAttachmentType<T>> T get(String id, Player player) {
        return (T) player.getCapability(getCapability(id)).orElse(null);
    }

    public <T extends IAttachmentType<T>> void set(String id, T data, Player player) {
        player.getCapability(getCapability(id)).map(cap -> data);
    }

    @SuppressWarnings("unchecked")
    public <T extends IAttachmentType<T>> void update(String id, Function<T, T> action, Player player) {
        player.getCapability(getCapability(id)).map(cap -> action.apply((T) cap));
    }

    public <T extends IAttachmentType<T>> boolean has(String id, Player player) {
        return player.getCapability(getCapability(id)).isPresent();
    }

    @SuppressWarnings("unchecked")
    private <T> Capability<T> getCapability(String id) {
        Capability<T> typeSupplier = (Capability<T>) registrar.get(id);
        if (typeSupplier == null) throw new IllegalArgumentException("Unregistered forge capability: " + id);
        return typeSupplier;
    }
}