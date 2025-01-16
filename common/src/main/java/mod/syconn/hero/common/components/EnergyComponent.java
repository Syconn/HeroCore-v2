package mod.syconn.hero.common.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.syconn.hero.core.ModComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record EnergyComponent(int energy, int max) {

    private static final EnergyComponent DEFAULT = new EnergyComponent(100, 100);

    public static final StreamCodec<RegistryFriendlyByteBuf, EnergyComponent> STREAM_CODEC =
            StreamCodec.composite(ByteBufCodecs.INT, EnergyComponent::energy, ByteBufCodecs.INT, EnergyComponent::max, EnergyComponent::new);

    public static final Codec<EnergyComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.INT.fieldOf("energy").forGetter(EnergyComponent::energy),
            Codec.INT.fieldOf("max").forGetter(EnergyComponent::max)
    ).apply(builder, EnergyComponent::new));

    public EnergyComponent modifyEnergy(int amount) {
        return new EnergyComponent(energy + amount, max);
    }

    public static EnergyComponent from(ItemStack stack) {
        return stack.getOrDefault(ModComponents.ENERGY.get(), DEFAULT);
    }

    public static void lowerSuitEnergy(Player player) {
        player.getItemBySlot(EquipmentSlot.HEAD).update(ModComponents.ENERGY.get(), DEFAULT, e -> e.modifyEnergy(-1));
        player.getItemBySlot(EquipmentSlot.CHEST).update(ModComponents.ENERGY.get(), DEFAULT, e -> e.modifyEnergy(-1));
        player.getItemBySlot(EquipmentSlot.LEGS).update(ModComponents.ENERGY.get(), DEFAULT, e -> e.modifyEnergy(-1));
        player.getItemBySlot(EquipmentSlot.FEET).update(ModComponents.ENERGY.get(), DEFAULT, e -> e.modifyEnergy(-1));
    }

    public static int getSuitEnergy(Player player) {
        return player.getItemBySlot(EquipmentSlot.HEAD).getOrDefault(ModComponents.ENERGY.get(), DEFAULT).energy + player.getItemBySlot(EquipmentSlot.CHEST).getOrDefault(ModComponents.ENERGY.get(), DEFAULT).energy +
                player.getItemBySlot(EquipmentSlot.LEGS).getOrDefault(ModComponents.ENERGY.get(), DEFAULT).energy + player.getItemBySlot(EquipmentSlot.FEET).getOrDefault(ModComponents.ENERGY.get(), DEFAULT).energy;
    }

    public static int getSuitEnergyMax(Player player) {
        return player.getItemBySlot(EquipmentSlot.HEAD).getOrDefault(ModComponents.ENERGY.get(), DEFAULT).max + player.getItemBySlot(EquipmentSlot.CHEST).getOrDefault(ModComponents.ENERGY.get(), DEFAULT).max +
                player.getItemBySlot(EquipmentSlot.LEGS).getOrDefault(ModComponents.ENERGY.get(), DEFAULT).max + player.getItemBySlot(EquipmentSlot.FEET).getOrDefault(ModComponents.ENERGY.get(), DEFAULT).max;
    }

    public static int getSuitPercentage(Player player) {
        return (int) ((float) getSuitEnergy(player) / getSuitEnergyMax(player) * 100);
    }
}
