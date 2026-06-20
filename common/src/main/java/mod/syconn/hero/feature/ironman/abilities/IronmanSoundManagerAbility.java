package mod.syconn.hero.feature.ironman.abilities;

import mod.syconn.hero.feature.heros.interfaces.IHeroAbility;
import mod.syconn.hero.feature.heros.interfaces.IHeroType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class IronmanSoundManagerAbility implements IHeroAbility {

    private final IHeroType hero;

    public IronmanSoundManagerAbility() {
    }

    @Override
    public ResourceLocation heroType() {
        return null;
    }

    @Override
    public ResourceLocation id() {
        return null;
    }

    @Override
    public void clientTick(Player player) {

    }

    @Override
    public CompoundTag writeData(Player player) {
        return null;
    }

    @Override
    public void readData(Player player, CompoundTag tag) {

    }
}
