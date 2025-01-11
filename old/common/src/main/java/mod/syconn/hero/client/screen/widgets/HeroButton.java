package mod.syconn.hero.client.screen.widgets;

import mod.syconn.hero.Constants;
import mod.syconn.hero.extra.data.powers.HeroType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class HeroButton extends Button {
    
    private final ResourceLocation BACKGROUND = Constants.withId("textures/gui/ability_selector.png");
    private final ResourceLocation ICONS = Constants.withId("textures/gui/hero_icons.png");
    private final HeroType type;
    private boolean selected;

    public HeroButton(int x, int y, int width, int height, HeroType type, boolean selected, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, DEFAULT_NARRATION);
        this.type = type;
        this.selected = selected;
    }

    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        int iconWidth = 128, iconHeight = 32;
        guiGraphics.blit(RenderType::guiTextured, BACKGROUND, this.getX(), this.getY(), selected ? 208 : 176, 8, iconHeight, iconHeight, 256, 256);
        guiGraphics.blit(RenderType::guiTextured, ICONS, this.getX(), this.getY(), type.getXPos(), type.getYPos(), iconHeight, iconHeight, iconWidth, iconHeight);
        if (isHovered()) guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.literal(type.getName()), mouseX, mouseY);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public HeroType getType() {
        return type;
    }
}