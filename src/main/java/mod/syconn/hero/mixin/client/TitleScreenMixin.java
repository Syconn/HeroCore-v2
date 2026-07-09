package mod.syconn.hero.mixin.client;

import mod.syconn.hero.utils.Constants;
import mod.syconn.hero.utils.UpdateTracker;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    @Unique
    private static boolean SHOW_UPDATE = true;

    protected TitleScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At(value = "HEAD"))
    public void createScreen(CallbackInfo ci) {
        if (Constants.TRACKER.shouldUpdate() && this.minecraft != null && SHOW_UPDATE) {
            this.minecraft.setScreen(new UpdateTracker.UpdateScreen(this));
            SHOW_UPDATE = false;
        }
    }
}
