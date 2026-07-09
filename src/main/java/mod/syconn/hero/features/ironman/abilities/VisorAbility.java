package mod.syconn.hero.features.ironman.abilities;

import mod.syconn.hero.core.ModKeys;
import mod.syconn.hero.features.heros.interfaces.IHeroAbility;
import mod.syconn.hero.features.heros.interfaces.IHeroType;
import mod.syconn.hero.features.ironman.item.IronmanArmorItem;
import mod.syconn.hero.features.ironman.server.data.SuitTag;
import mod.syconn.hero.utils.Constants;
import mod.syconn.hero.utils.HeroConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class VisorAbility implements IHeroAbility { // TODO MAKE HELMET OVERLAY LIKE PUMPKIN?

    public static final ResourceLocation TYPE = Constants.withId("ironman_visor");
    private final IHeroType heroType;

    public VisorAbility(IHeroType heroType) {
        this.heroType = heroType;
    }

    @Override
    public ResourceLocation heroType() {
        return heroType.id();
    }

    @Override
    public ResourceLocation id() {
        return TYPE;
    }

    @Override
    public void clientTick(Player player) {
        while (ModKeys.MODIFY_OVERLAY.consumeClick()) {
            HeroConfig.ironmanOverlay = !HeroConfig.ironmanOverlay;
            HeroConfig.write(Constants.MOD);
        }
    }

    @Override
    public boolean usable(Player player) {
        return IronmanArmorItem.wearingFullSameSuit(player) && !SuitTag.getOrCreate(FlipHelmetAbility.getTagSlot(player)).lifted;
    }

    @Override
    public CompoundTag writeData(Player player) {
        return new CompoundTag();
    }

    @Override
    public void readData(Player player, CompoundTag tag) {}
}
