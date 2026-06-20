package mod.syconn.hero.network.messages.serverside;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

import java.util.function.Supplier;

public class PlaySoundPacket {

    private final SoundEvent sound;
    private final SoundSource source;
    private final float volume;
    private final float pitch;

    public PlaySoundPacket(SoundEvent sound, SoundSource source, float volume, float pitch) {
        this.sound = sound;
        this.source = source;
        this.volume = volume;
        this.pitch = pitch;
    }

    public PlaySoundPacket(FriendlyByteBuf buf) {
        this(BuiltInRegistries.SOUND_EVENT.get(buf.readResourceLocation()), buf.readEnum(SoundSource.class), buf.readFloat(), buf.readFloat());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(BuiltInRegistries.SOUND_EVENT.getKey(this.sound));
        buf.writeEnum(this.source);
        buf.writeFloat(this.volume);
        buf.writeFloat(this.pitch);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            if (!(context.get().getPlayer() instanceof ServerPlayer player)) return;
            player.serverLevel().playSound(null, player.getX(), player.getY(), player.getZ(), this.sound, this.source, this.volume, this.pitch);
        });
    }
}
