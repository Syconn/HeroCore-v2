package mod.syconn.hero.features.heros.util;

import me.fzzyhmstrs.fzzy_config.screen.context.FzzyKeybind;
import net.minecraft.client.Minecraft;

public class PowerKeybind {

    private final FzzyKeybind parent;
    private final int waitTime;
    private int wait = 0;
    private int clickCount = 0;
    private boolean pressed = false;

    public PowerKeybind(FzzyKeybind parent, int waitTime) {
        this.parent = parent;
        this.waitTime = waitTime;
    }

    public PowerKeybind(FzzyKeybind parent) {
        this(parent, 10);
    }

    public void tick() {
        if (Minecraft.getInstance().screen == null && parent.isPressed() && !pressed) {
            pressed = true;
            clickCount++;
        } else if (!parent.isPressed()) {
            pressed = false;
            if (wait <= waitTime) wait++;
            else {
                wait = 0;
                clickCount = 0;
            }
        }
    }

    public boolean consumeClick() {
        if (this.clickCount == 0) return false;
        else {
            --this.clickCount;
            return true;
        }
    }
}
