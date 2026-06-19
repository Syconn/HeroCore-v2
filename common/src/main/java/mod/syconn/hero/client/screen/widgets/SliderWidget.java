package mod.syconn.hero.client.screen.widgets;

import mod.syconn.hero.utils.generic.GraphicsUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.text.DecimalFormat;

public class SliderWidget extends AbstractSliderButton {
    private static final ResourceLocation SLIDER_LOCATION = new ResourceLocation("textures/gui/slider.png");
    protected final OnChange onChange;
    protected Component prefix;
    protected Component suffix;

    protected double minValue;
    protected double maxValue;

    protected double stepSize;

    protected boolean drawString;

    private final DecimalFormat format;

    public SliderWidget(int x, int y, int width, int height, Component prefix, Component suffix, double minValue, double maxValue, double currentValue, double stepSize, int precision, boolean drawString, @Nullable OnChange onChange) {
        super(x, y, width, height, Component.empty(), 0D);
        this.onChange = onChange;
        this.prefix = prefix;
        this.suffix = suffix;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.stepSize = Math.abs(stepSize);
        this.value = this.snapToNearest((currentValue - minValue) / (maxValue - minValue));
        this.drawString = drawString;

        if (stepSize == 0D) {
            precision = Math.min(precision, 4);

            StringBuilder builder = new StringBuilder("0");

            if (precision > 0) builder.append('.');

            while (precision-- > 0) builder.append('0');

            this.format = new DecimalFormat(builder.toString());
        }
        else if (Mth.equal(this.stepSize, Math.floor(this.stepSize))) this.format = new DecimalFormat("0");

        else this.format = new DecimalFormat(Double.toString(this.stepSize).replaceAll("\\d", "0"));

        this.updateMessage();
    }

    public SliderWidget(int x, int y, int width, int height, Component prefix, Component suffix, double minValue, double maxValue, double currentValue, boolean drawString, OnChange onChange) {
        this(x, y, width, height, prefix, suffix, minValue, maxValue, currentValue, 1D, 0, drawString, onChange);
    }

    public double getValue() {
        return this.value * (maxValue - minValue) + minValue;
    }

    public long getValueLong() {
        return Math.round(this.getValue());
    }

    public int getValueInt() {
        return (int) this.getValueLong();
    }

    public void setValue(double value) {
        this.value = this.snapToNearest((value - this.minValue) / (this.maxValue - this.minValue));
        this.updateMessage();
    }

    public String getValueString() {
        return this.format.format(this.getValue());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.setValueFromMouse(mouseX);
    }

    public void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        super.onDrag(mouseX, mouseY, dragX, dragY);
        this.setValueFromMouse(mouseX);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean flag = keyCode == GLFW.GLFW_KEY_LEFT;
        if (flag || keyCode == GLFW.GLFW_KEY_RIGHT) {
            if (this.minValue > this.maxValue) flag = !flag;
            float f = flag ? -1F : 1F;
            if (stepSize <= 0D) this.setSliderValue(this.value + (f / (this.width - 8)));
            else this.setValue(this.getValue() + f * this.stepSize);
        }

        return false;
    }

    private void setValueFromMouse(double mouseX) {
        this.setSliderValue((mouseX - (this.getX() + 4)) / (this.width - 8));
    }

    private void setSliderValue(double value) {
        double oldValue = this.value;
        this.value = this.snapToNearest(value);
        if (!Mth.equal(oldValue, this.value)) this.applyValue();
        this.updateMessage();
    }

    private double snapToNearest(double value) {
        if(stepSize <= 0D) return Mth.clamp(value, 0D, 1D);

        value = Mth.lerp(Mth.clamp(value, 0D, 1D), this.minValue, this.maxValue);
        value = (stepSize * Math.round(value / stepSize));

        if (this.minValue > this.maxValue) value = Mth.clamp(value, this.maxValue, this.minValue);
        else value = Mth.clamp(value, this.minValue, this.maxValue);

        return Mth.map(value, this.minValue, this.maxValue, 0D, 1D);
    }

    @Override
    protected void updateMessage() {
        if (this.drawString) this.setMessage(Component.literal("").append(prefix).append(this.getValueString()).append(suffix));
        else this.setMessage(Component.empty());
        if (onChange != null) onChange.onChange(this);
    }

    @Override
    protected void applyValue() {}

    @Environment(EnvType.CLIENT)
    public interface OnChange {
        void onChange(SliderWidget widget);
    }

    private int getTextureY() {
        int i = this.isHovered ? 1 : 0;
        return i * 20;
    }

    private int getHandleTextureY() {
        int i = !this.isHovered ? 2 : 3;
        return i * 20;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        final Minecraft mc = Minecraft.getInstance();

        renderBackground(guiGraphics);
        GraphicsUtil.blitWithBorder(guiGraphics, SLIDER_LOCATION, this.getX() + (int)(this.value * (double)(this.width - 8)), this.getY(), 0, getHandleTextureY(), 8, this.height, 200, 20 , 2, 3, 2, 2);

        renderScrollingString(guiGraphics, mc.font, 2, this.active ? 16777215 : 10526880 | Mth.ceil(this.alpha * 255.0F) << 24);
    }

    protected void renderBackground(GuiGraphics graphics) {
        GraphicsUtil.blitWithBorder(graphics, SLIDER_LOCATION, this.getX(), this.getY(), 0, getTextureY(), this.width, this.height, 200, 20, 2, 3, 2, 2);
    }
}
