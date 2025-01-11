package mod.syconn.hero.extra.data.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mod.syconn.hero.registrar.ComponentRegistrar;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record EnergyComponent(float energy, float maxEnergy) {

    public static final StreamCodec<ByteBuf, EnergyComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, EnergyComponent::energy, ByteBufCodecs.FLOAT, EnergyComponent::maxEnergy, EnergyComponent::new
    );

    public static final Codec<EnergyComponent> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.FLOAT.fieldOf("energy").forGetter(EnergyComponent::energy),
            Codec.FLOAT.fieldOf("maxEnergy").forGetter(EnergyComponent::maxEnergy)
    ).apply(builder, EnergyComponent::new));
    
    public EnergyComponent increment(float pEnergy) {
        return new EnergyComponent(energy + pEnergy, maxEnergy);
    }

    public static boolean updateStacksEnergy(ItemStack stack, float energy) {
        EnergyComponent component = stack.getOrDefault(ComponentRegistrar.ENERGY_COMPONENT.get(), new EnergyComponent(100, 100));
        if (component.energy <= 0) return false;
        stack.set(ComponentRegistrar.ENERGY_COMPONENT.get(), component.increment(-energy));
        return true;
    }
}
