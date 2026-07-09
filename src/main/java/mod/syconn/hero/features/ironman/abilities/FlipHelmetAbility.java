package mod.syconn.hero.features.ironman.abilities;

import mod.syconn.hero.features.heros.interfaces.IHeroAbility;
import mod.syconn.hero.features.heros.interfaces.IHeroType;
import mod.syconn.hero.features.ironman.item.IronmanArmorItem;
import mod.syconn.hero.features.ironman.server.data.SuitTag;
import mod.syconn.hero.utils.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class FlipHelmetAbility implements IHeroAbility {

    public static final ResourceLocation TYPE = Constants.withId("flip_helmet");

    private final IHeroType heroType;
//    private final PowerKeybind flipHelmet = new PowerKeybind(Constants.CONFIG.ironmanSettings.flipHelmet.get()); TODO ADD BACK

    public FlipHelmetAbility(IHeroType heroType) {
        this.heroType = heroType;
    }

    @Override
    public void clientTick(Player player) {
//        flipHelmet.tick();

        if (this.usable(player)) {
            SuitTag.update(getTagSlot(player), tag -> {
                tag.tick();

//                while (flipHelmet.consumeClick()) { TODO
//                    tag.openCloseHelmet();
//                    float pitch = 0.95f + player.getRandom().nextFloat() * 0.1f;
//                    Network.CHANNEL.sendToServer(new PlaySoundPacket(tag.lifted ? ModSounds.HELMET_RAISE.get() : ModSounds.HELMET_LOWER.get(), SoundSource.PLAYERS, 0.5f, pitch));
//                }
            });
        }
    }

    public static ItemStack getTagSlot(Player player) {
        return player.getItemBySlot(EquipmentSlot.HEAD);
    }

    @Override
    public CompoundTag writeData(Player player) {
        return new CompoundTag();
    }

    @Override
    public void readData(Player player, CompoundTag tag) {}

    @Override
    public boolean usable(Player player) {
        return getTagSlot(player).getItem() instanceof IronmanArmorItem;
    }

    @Override
    public ResourceLocation heroType() {
        return this.heroType.id();
    }

    @Override
    public ResourceLocation id() {
        return TYPE;
    }
}
