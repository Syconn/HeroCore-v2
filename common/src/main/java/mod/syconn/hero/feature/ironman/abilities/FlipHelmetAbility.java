package mod.syconn.hero.feature.ironman.abilities;

import mod.syconn.hero.core.ModTags;
import mod.syconn.hero.feature.heros.interfaces.IHeroAbility;
import mod.syconn.hero.feature.heros.interfaces.IHeroType;
import mod.syconn.hero.feature.heros.util.PowerKeybind;
import mod.syconn.hero.utils.Constants;
import mod.syconn.hero.utils.generic.NBTUtil;
import mod.syconn.hero.utils.interfaces.ICustomArmor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

public class FlipHelmetAbility implements IHeroAbility { // TODO ISSUE: Both Players have suit on, One has Overlay on, Both one modifies both

    public static final ResourceLocation TYPE = Constants.withId("flip_helmet");
    private static final byte TRANSITION_TICKS = 6;

    private final IHeroType heroType;
    private final PowerKeybind flipHelmet = new PowerKeybind(Constants.CONFIG.ironmanSettings.flipHelmet.get());
    public boolean lifted;
    private byte transition;

    public FlipHelmetAbility(IHeroType heroType) {
        this.heroType = heroType;
    }

    @Override
    public void clientTick(Player player) {
        flipHelmet.tick();

        if (this.usable(player)) {
            this.requiresUpdate(player);
            var tag = new CompoundTag();

            if (this.transition != 0) {
                if (this.transition > 0) this.transition--;
                if (this.transition < 0) this.transition++;
                tag.putByte("transition", this.transition);
            }

            while (flipHelmet.consumeClick()) {
                if (this.transition != 0) return;
                this.transition = TRANSITION_TICKS;
                this.lifted = !this.lifted;
                tag = this.writeData(player);
            }

            System.out.println(player);
            if (!tag.isEmpty()) this.sendSpecificData(player, tag);
        }
    }

    private void requiresUpdate(Player player) {
        var stack = player.getItemBySlot(EquipmentSlot.HEAD);
        var tag = this.getStackTag(stack);
        this.lifted = tag.contains("lifted") && tag.getBoolean("lifted");
        this.transition = tag.contains("transition") ? tag.getByte("transition") : 0;
    }

    public byte getRenderFrame(Player player) {
        this.requiresUpdate(player);
        if (transition == 0) return this.lifted ? TRANSITION_TICKS : 0;
        return this.lifted ? (byte) (6 - transition) : transition;
    }

    @Override
    public boolean usable(Player player) {
        return player.getItemBySlot(EquipmentSlot.HEAD).is(ModTags.IRONMAN_ARMOR) && player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ICustomArmor;
    }

    @Override
    public ResourceLocation heroType() {
        return this.heroType.id();
    }

    @Override
    public ResourceLocation id() {
        return TYPE;
    }

    @Override
    public CompoundTag writeData(Player player) {
        var tag = new CompoundTag();
        tag.putBoolean("lifted", this.lifted);
        tag.putByte("transition", this.transition);
        return tag;
    }

    @Override
    public void readData(Player player, CompoundTag tag) {
        var stack = player.getItemBySlot(EquipmentSlot.HEAD);
        this.putStackTag(stack, NBTUtil.combineTags(this.getStackTag(stack), tag));
    }
}
