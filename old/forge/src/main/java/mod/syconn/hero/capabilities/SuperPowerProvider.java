package mod.syconn.hero.capabilities;

import mod.syconn.hero.extra.data.attachment.SuperPower;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SuperPowerProvider implements ICapabilitySerializable<CompoundTag> {

    public static Capability<SuperPower> SUPER_POWER = CapabilityManager.get(new CapabilityToken<>() {});
    private SuperPower SuperPower = null;
    private final LazyOptional<SuperPower> holder = LazyOptional.of(this::createSuperPower);

    private SuperPower createSuperPower() {
        if (this.SuperPower == null) this.SuperPower = new SuperPower();
        return this.SuperPower;
    }

    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == SUPER_POWER) return this.holder.cast();
        return LazyOptional.empty();
    }

    public CompoundTag serializeNBT(HolderLookup.Provider registryAccess) {
        return createSuperPower().writeSyncedData();
    }

    public void deserializeNBT(HolderLookup.Provider registryAccess, CompoundTag nbt) {
        createSuperPower().readSyncedData(nbt);
    }
}