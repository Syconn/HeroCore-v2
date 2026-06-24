package mod.syconn.hero.utils.block;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum TwoPart implements StringRepresentable {

    UP("up"),
    DOWN("down");

    private final String name;

    TwoPart(String name) {
        this.name = name;
    }

    public boolean down() {
        return this == DOWN;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
}
