package mod.syconn.hero.extra.platform.services;

import mod.syconn.hero.extra.data.attachment.IAttachmentType;
import net.minecraft.world.entity.player.Player;

import java.util.function.Function;
import java.util.function.Supplier;

public interface IAttachedData {

    <T extends IAttachmentType<T>> String registerType(String id, Supplier<T> typeSupplier);
    <T extends IAttachmentType<T>> T get(String id, Player player);
    <T extends IAttachmentType<T>> void set(String id, T data, Player player);
    <T extends IAttachmentType<T>> boolean has(String id, Player player);
    <T extends IAttachmentType<T>> void update(String id, Function<T, T> action, Player player);
    default <T extends IAttachmentType<T>> void update(Class<T> type, Function<T, T> action, Player player) {
        update(type.getSimpleName().toLowerCase(), action, player);
    }
}