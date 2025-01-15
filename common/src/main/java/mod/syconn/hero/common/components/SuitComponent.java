package mod.syconn.hero.common.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mod.syconn.hero.core.ModComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

import java.util.function.UnaryOperator;

public record SuitComponent(FlightMode flightMode) {

    public static SuitComponent DEFAULT = new SuitComponent(FlightMode.WALK);
    public static final Codec<SuitComponent> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(FlightMode.CODEC.fieldOf("flightMode").forGetter(SuitComponent::flightMode)).apply(builder, SuitComponent::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, SuitComponent> STREAM_CODEC = StreamCodec.composite(FlightMode.STREAM_CODEC, SuitComponent::flightMode, SuitComponent::new);

    public SuitComponent cycleMode() {
        return new SuitComponent(switch (this.flightMode) {
            case WALK -> FlightMode.FLY;
            case HOVER -> FlightMode.WALK;
            case FLY -> FlightMode.HOVER;
        });
    }

    public SuitComponent setFlightMode(FlightMode mode) {
        return new SuitComponent(mode);
    }

    public static SuitComponent from(Player player) {
        return player.getItemBySlot(EquipmentSlot.CHEST).getOrDefault(ModComponents.SUIT_SETTINGS.get(), DEFAULT);
    }

    public static void update(Player player, UnaryOperator<SuitComponent> updater) {
        player.getItemBySlot(EquipmentSlot.CHEST).update(ModComponents.SUIT_SETTINGS.get(), DEFAULT, updater);
    }

    public enum FlightMode implements StringRepresentable {
        WALK(0),
        HOVER(1),
        FLY(2);

        public static final StringRepresentable.EnumCodec<FlightMode> CODEC = StringRepresentable.fromEnum(FlightMode::values);
        public static final StreamCodec<ByteBuf, FlightMode> STREAM_CODEC = ByteBufCodecs.idMapper(ByIdMap.continuous(FlightMode::getId, values(), ByIdMap.OutOfBoundsStrategy.WRAP), FlightMode::getId);
        final int id;

        FlightMode(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}
