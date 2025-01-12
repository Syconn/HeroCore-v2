package mod.syconn.hero.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

public class SuitSettings {

    private FlightMode flightMode;

    public SuitSettings(FlightMode flightMode) {
        this.flightMode = flightMode;
    }

    public SuitSettings(CompoundTag tag) {
        this.flightMode = FlightMode.class.getEnumConstants()[tag.getInt("flightMode")];
    }

    public FlightMode getFlightMode() {
        return flightMode;
    }

    public void setFlightMode(FlightMode flightMode) {
        this.flightMode = flightMode;
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
        return tag;
    }

    public static SuitSettings instance() {
        return new SuitSettings(FlightMode.WALK);
    }

    public static SuitSettings from(Player player) {
        SuitSettings settings = instance();
        if (Helpers.isWearingIronManSuit(player)) {
            if (player.getItemBySlot(EquipmentSlot.CHEST).getOrCreateTag().contains("settings"))
                return new SuitSettings((CompoundTag) player.getItemBySlot(EquipmentSlot.CHEST).getOrCreateTag().get("settings"));
            else player.getItemBySlot(EquipmentSlot.CHEST).getOrCreateTag().put("settings", settings.writeTag());
        }
        return settings;
    }

    public enum FlightMode {
        WALK,
        HOVER,
        FLY
    }
}
