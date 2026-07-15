package mod.syconn.hero.network.messages.clientside;

//? if 1.20.1 {
import dev.architectury.networking.NetworkManager;
import mod.syconn.hero.utils.client.ParticleEvent;
import net.minecraft.network.FriendlyByteBuf;
import java.util.Objects;
import java.util.function.Supplier;
//? } else {
/*import dev.architectury.utils.GameInstance;
import mod.syconn.hero.utils.Constants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;
*///? }

//? if 1.20.1 {
public class PlayParticleEffect {

    private final ParticleEvent event;

    public PlayParticleEffect(ParticleEvent event) {
        this.event = event;
    }

    public PlayParticleEffect(FriendlyByteBuf buf) {
        this.event = ParticleEvent.from(Objects.requireNonNull(buf.readNbt()));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(this.event.save());
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> context.get().getPlayer().level().addParticle(event.type(), event.pos().x, event.pos().y, event.pos().z, event.velocity().x, event.velocity().y, event.velocity().z));
    }
}
//? } else {
/*public record PlayParticleEffect(ParticleEvent event) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PlayParticleEffect> TYPE = new CustomPacketPayload.Type<>(Constants.withId("play_particle_effect"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayParticleEffect> STREAM_CODEC =
            StreamCodec.composite(ParticleEvent.STREAM_CODEC, PlayParticleEffect::event, PlayParticleEffect::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PlayParticleEffect packet, NetworkManager.PacketContext context) {
        context.queue(() -> {
            var event = packet.event;
            context.getPlayer().level().addParticle(event.type(), event.pos().x, event.pos().y, event.pos().z, event.velocity().x, event.velocity().y, event.velocity().z);
        });
    }
}
*///? }
