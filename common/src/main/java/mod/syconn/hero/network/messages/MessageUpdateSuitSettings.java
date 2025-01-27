package mod.syconn.hero.network.messages;

import dev.architectury.networking.NetworkManager;
import mod.syconn.hero.common.data.SuitSettings;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public class MessageUpdateSuitSettings {

    private final SuitSettings settings;

    public MessageUpdateSuitSettings(SuitSettings settings) {
        this.settings = settings;
    }

    public MessageUpdateSuitSettings(FriendlyByteBuf buf) {
        this(new SuitSettings(buf));
    }

    public void encode(FriendlyByteBuf buf) {
        settings.writeBuf(buf);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> SuitSettings.set(context.get().getPlayer(), settings));
    }
}
