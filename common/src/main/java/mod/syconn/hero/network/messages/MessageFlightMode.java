package mod.syconn.hero.network.messages;

import dev.architectury.networking.NetworkManager;
import mod.syconn.hero.util.SuitSettings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

import java.util.function.Supplier;

public class MessageFlightMode {

    private final SuitSettings.FlightMode mode;

    public MessageFlightMode(SuitSettings.FlightMode mode) {
        this.mode = mode;
    }

    public MessageFlightMode(FriendlyByteBuf buf) {
        this(buf.readEnum(SuitSettings.FlightMode.class));
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(mode);
    }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            Player player = context.get().getPlayer();
            SuitSettings settings = SuitSettings.from(player);
            settings.setFlightMode(mode);
            player.getItemBySlot(EquipmentSlot.CHEST).getOrCreateTag().put("settings", settings.writeTag());
        });
    }
}
