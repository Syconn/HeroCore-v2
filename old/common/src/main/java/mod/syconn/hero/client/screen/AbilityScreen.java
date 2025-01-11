package mod.syconn.hero.client.screen;

import mod.syconn.hero.Constants;
import mod.syconn.hero.client.screen.widgets.HeroButton;
import mod.syconn.hero.extra.data.attachment.SuperPower;
import mod.syconn.hero.extra.data.powers.HeroType;
import mod.syconn.hero.extra.platform.Services;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class AbilityScreen extends Screen {
    
    private final ResourceLocation BACKGROUND = Constants.withId("textures/gui/ability_selector.png");
    private final int imageWidth = 176;
    private final int imageHeight = 85;
    private final int iconSize = 32;
    private final HeroButton[] buttons = new HeroButton[HeroType.values().length];
    private final Player player;
    private HeroType heroType;
    private int leftPos, topPos;

    public AbilityScreen(Player player, SuperPower power) {
        super(Component.translatable("hero.ability.screen"));
        this.player = player;
        this.heroType = power.type();
    }

    protected void init() {
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
        for (int i = 0; i < HeroType.values().length; i++)
            addRenderableWidget(buttons[i] = new HeroButton(leftPos + i * 32 + 6 + i * 7, topPos + (75 - iconSize) / 2, iconSize, iconSize, HeroType.getType(i), HeroType.getType(i) == heroType, this::changeHeroType));
    }

    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        guiGraphics.blit(RenderType::guiTextured, BACKGROUND, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    private void changeHeroType(Button button) {
        if (button instanceof HeroButton heroButton) {
            buttons[heroType.getId()].setSelected(false);
            heroType = heroButton.getType();
            Services.ATTACHED_DATA.update(SuperPower.class, data -> data.setType(heroType, player), player);
            heroButton.setSelected(true);
        }
    }
}
