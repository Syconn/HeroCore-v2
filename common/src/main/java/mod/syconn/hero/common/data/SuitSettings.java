package mod.syconn.hero.common.data;

import mod.syconn.hero.util.AbilityUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

public class SuitSettings {

    private FlightMode flightMode;
    private boolean lifted;

    public SuitSettings(FlightMode flightMode, boolean lifted) {
        this.flightMode = flightMode;
        this.lifted = lifted;
    }

    public SuitSettings(CompoundTag tag) {
        this.flightMode = FlightMode.class.getEnumConstants()[tag.getInt("flightMode")];
        this.lifted = tag.getBoolean("lifted");
    }

    public SuitSettings(FriendlyByteBuf buf) {
        this.flightMode = buf.readEnum(SuitSettings.FlightMode.class);
        this.lifted = buf.readBoolean();
    }

    public void writeBuf(FriendlyByteBuf buf) {
        buf.writeEnum(this.flightMode);
        buf.writeBoolean(this.lifted);
    }

    public FlightMode getFlightMode() {
        return flightMode;
    }

    public boolean isLifted() {
        return lifted;
    }

    public SuitSettings flipHelmet() {
        this.lifted = !lifted;
        return this;
    }

    public void cycleMode() {
        this.flightMode = switch (this.flightMode) {
            case WALK -> FlightMode.FLY;
            case HOVER -> FlightMode.WALK;
            case FLY -> FlightMode.HOVER;
        };
    }

    public CompoundTag writeTag() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("flightMode", flightMode.ordinal());
        tag.putBoolean("lifted", lifted);
        return tag;
    }

    public static SuitSettings instance() {
        return new SuitSettings(FlightMode.WALK, false);
    }

    public static SuitSettings from(Player player) {
        SuitSettings settings = instance();
        if (AbilityUtil.canInteractWithIronManHelmet(player)) {
            if (player.getItemBySlot(EquipmentSlot.HEAD).getOrCreateTag().contains("settings"))
                return new SuitSettings((CompoundTag) player.getItemBySlot(EquipmentSlot.HEAD).getOrCreateTag().get("settings"));
            else player.getItemBySlot(EquipmentSlot.HEAD).getOrCreateTag().put("settings", settings.writeTag());
        }
        return settings;
    }

    public static void set(Player player, SuitSettings settings) {
        player.getItemBySlot(EquipmentSlot.HEAD).getOrCreateTag().put("settings", settings.writeTag());
    }

    public enum FlightMode {
        WALK,
        HOVER,
        FLY
    }
}
