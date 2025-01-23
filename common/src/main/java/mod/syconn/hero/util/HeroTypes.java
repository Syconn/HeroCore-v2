package mod.syconn.hero.util;

import mod.syconn.hero.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;

import java.util.function.Function;

public enum HeroTypes implements StringRepresentable { // TODO Need Hold Required Gear Hear to Grab for Menu Screen

    NONE("No Powers", 64, 0, player -> true),
    IRON_MAN("Fight with Iron Man's Suit", 0, 0, AbilityUtil::canUseIronManPowers),
    THOR("Wield Thor's Lightning", 32, 0, AbilityUtil::canUseThorPowers);

    private final String name;
    private final int renderX;
    private final int renderY;
    private final Function<Player, Boolean> usable;

    public static final HeroTypes values[] = values();

    HeroTypes(String name, int renderX, int renderY, Function<Player, Boolean> usable) {
        this.name = name;
        this.renderX = renderX;
        this.renderY = renderY;
        this.usable = usable;
    }

    public String getOverlayName() {
        return this.name;
    }

    public int getRenderX() {
        return this.renderX;
    }

    public int getRenderY() {
        return this.renderY;
    }

    public boolean canUse(Player player) {
        return this.usable.apply(player);
    }

    public String getSerializedName() {
        return name().toLowerCase();
    }

    public CompoundTag write(CompoundTag tag) {
        tag.putInt(Constants.MOD_ID + ":power", this.ordinal());
        return tag;
    }

    public static HeroTypes read(CompoundTag tag) {
        for (HeroTypes heroType : values) if (heroType.ordinal() == tag.getInt(Constants.MOD_ID + ":power")) return heroType;
        return NONE;
    }
}
