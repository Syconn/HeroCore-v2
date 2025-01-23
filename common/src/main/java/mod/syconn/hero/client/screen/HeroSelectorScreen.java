package mod.syconn.hero.client.screen;

import mod.syconn.hero.Constants;
import mod.syconn.hero.client.screen.widgets.HeroButton;
import mod.syconn.hero.util.AbilityUtil;
import mod.syconn.hero.util.HeroTypes;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class HeroSelectorScreen extends Screen {

    private final ResourceLocation BACKGROUND = Constants.withId("textures/gui/ability_selector.png");
    private final int imageWidth = 176;
    private final int imageHeight = 85;
    private final HeroButton[] buttons = new HeroButton[HeroTypes.values().length];
    private final Player player;
    private HeroTypes selectedType;
    private int leftPos, topPos;

    public HeroSelectorScreen(Player player) {
        super(Component.translatable("hero.ability.screen"));
        this.player = player;
        this.selectedType = AbilityUtil.getHeroType(player);
    }

    protected void init() {
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
        for (HeroTypes type : HeroTypes.values) {
            int i = type.ordinal(), iconSize = 32;
            addRenderableWidget(buttons[i] =
                    new HeroButton(leftPos + i * 32 + 6 + i * 7, topPos + (75 - iconSize) / 2, iconSize, iconSize, type, type == this.selectedType, type.canUse(player), this::changeHeroType));
        }
    }

    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        guiGraphics.blit(BACKGROUND, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    private void changeHeroType(Button button) {
        if (button instanceof HeroButton heroButton && heroButton.getType().canUse(player)) {
            buttons[this.selectedType.ordinal()].setSelected(false);
            this.selectedType = AbilityUtil.setHeroType(player, heroButton.getType());
            heroButton.setSelected(true);
        }
    }
}