package mod.syconn.hero.network.messages.clientside;

import dev.architectury.networking.NetworkManager;
import mod.syconn.hero.utils.client.ParticleEvent;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Objects;
import java.util.function.Supplier;

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
