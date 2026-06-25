package mod.syconn.hero.features.ironman.abilities;

import mod.syconn.hero.features.heros.interfaces.IHeroAbility;
import mod.syconn.hero.features.heros.interfaces.IHeroType;
import mod.syconn.hero.features.heros.util.PowerKeybind;
import mod.syconn.hero.features.ironman.server.data.SuitTag;
import mod.syconn.hero.item.IronmanArmorItem;
import mod.syconn.hero.utils.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class VisorAbility implements IHeroAbility {

    public static final ResourceLocation TYPE = Constants.withId("ironman_visor");

    private final PowerKeybind toggleVisor = new PowerKeybind(Constants.CONFIG.ironmanSettings.toggleVisor.get());
    private final IHeroType heroType;
    private boolean enabled;

    public VisorAbility(IHeroType heroType) {
        this.heroType = heroType;
        this.enabled = true;
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
        toggleVisor.tick();

        while (toggleVisor.consumeClick()) {
            enabled = !enabled;
        }
    }

    @Override
    public boolean usable(Player player) {
        return enabled && IronmanArmorItem.wearingFullSameSuit(player) && !SuitTag.getOrCreate(FlipHelmetAbility.getTagSlot(player)).lifted;
    }

    @Override
    public CompoundTag writeData(Player player) {
        return new CompoundTag();
    }

    @Override
    public void readData(Player player, CompoundTag tag) {}
}
