package mod.syconn.hero.client.screen.widgets;

import mod.syconn.hero.Constants;
import mod.syconn.hero.util.HeroTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class HeroButton extends ExtendedButton {

    private final ResourceLocation BACKGROUND = Constants.withId("textures/gui/ability_selector.png");
    private final ResourceLocation ICONS = Constants.withId("textures/gui/hero_icons.png");
    private final HeroTypes type;
    private final boolean canUse;
    private boolean selected;

    public HeroButton(int x, int y, int width, int height, HeroTypes type, boolean selected, boolean canUse, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, DEFAULT_NARRATION);
        this.type = type;
        this.selected = selected;
        this.canUse = canUse;
    }

    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        int iconWidth = 128, iconSize = 32;
        guiGraphics.blit(BACKGROUND, this.getX(), this.getY(), selected ? 208 : 176, 8, iconSize, iconSize, 256, 256);
        guiGraphics.blit(ICONS, this.getX(), this.getY(), type.getRenderX(), type.getRenderY(), iconSize, iconSize, iconWidth, iconSize);
        if (isHovered()) guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.literal(type.getOverlayName()).withStyle(canUse ? ChatFormatting.WHITE : ChatFormatting.RED), mouseX, mouseY);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public HeroTypes getType() {
        return type;
    }
}
