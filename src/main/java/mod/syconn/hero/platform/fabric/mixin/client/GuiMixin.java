//? if fabric {
package mod.syconn.hero.platform.fabric.mixin.client;

import mod.syconn.hero.features.ironman.client.screen.overlays.IronmanOverlay;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    @Inject(method = "render", at = @At("HEAD"))
    public void renderGui(GuiGraphics guiGraphics, float f, CallbackInfo ci) {
        IronmanOverlay.renderOverlay(guiGraphics, f);
    }
}
//? }