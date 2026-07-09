package mod.syconn.hero.utils.config;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import eu.midnightdust.lib.config.EntryInfo;
import eu.midnightdust.lib.config.MidnightConfig;
import eu.midnightdust.lib.config.MidnightConfigListWidget;
import eu.midnightdust.lib.config.MidnightConfigScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class ExtendedConfig extends MidnightConfig {

    public static class KeybindButton extends Button {
        public static Button focusedButton;

        public static void add(KeyMapping binding, MidnightConfigListWidget list, MidnightConfigScreen screen) {
            var editButton = new KeybindButton(screen.width - 185, 0, 95, 20, binding);
            var resetButton = Button.builder(Component.translatable("controls.reset"), (arg) -> {
                binding.setToDefault();
                binding.setKey(binding.getDefaultKey());
                KeyMapping.resetMapping();
                screen.updateList();
            }).bounds(screen.width - 185 + 105, 0, 50, 20).build();
            editButton.resetButton = resetButton;
            list.addButton(Lists.newArrayList(editButton, resetButton), Component.translatable(binding.getName()), new EntryInfo(null, screen.modid));
        }

        private final KeyMapping binding;
        private @Nullable AbstractButton resetButton;
        public KeybindButton(int x, int y, int width, int height, KeyMapping binding) {
            super(x, y, width, height, binding.getTranslatedKeyMessage(), (button) -> {
                ((KeybindButton) button).updateMessage(true);
                focusedButton = button;
            }, (textSupplier) -> binding.isUnbound() ? Component.translatable("narrator.controls.unbound", binding.getName()) : Component.translatable("narrator.controls.bound", binding.getName(), textSupplier.get()));
            this.binding = binding;
            updateMessage(false);
        }

        @Override
        public boolean keyPressed(int i, int j, int k) {
            if (focusedButton == this) {
                if (i == 256) this.binding.setKey(InputConstants.UNKNOWN);
                else this.binding.setKey(InputConstants.getKey(i, j));
                updateMessage(false);
                focusedButton = null;
                return true;
            }
            return super.keyPressed(i, j, k);
        }

        public void updateMessage(boolean focused) {
            var hasConflicts = false;
            var conflictingBindings = Component.empty();
            if (focused) this.setMessage(Component.literal("> ").append(this.binding.getTranslatedKeyMessage().copy().withStyle(ChatFormatting.WHITE, ChatFormatting.UNDERLINE)).append(" <").withStyle(ChatFormatting.YELLOW));
            else {
                this.setMessage(this.binding.getTranslatedKeyMessage());

                if (!this.binding.isUnbound()) {
                    for(KeyMapping keyBinding : Minecraft.getInstance().options.keyMappings) {
                        if (keyBinding != this.binding && this.binding.equals(keyBinding)) {
                            if (hasConflicts) conflictingBindings.append(", ");

                            hasConflicts = true;
                            conflictingBindings.append(Component.translatable(keyBinding.getName()));
                        }
                    }
                }
            }

            if (this.resetButton != null) this.resetButton.active = !this.binding.isDefault();

            if (hasConflicts) {
                this.setMessage(Component.literal("[ ").append(this.getMessage().copy().withStyle(ChatFormatting.WHITE)).append(" ]").withStyle(ChatFormatting.RED));
                this.setTooltip(Tooltip.create(Component.translatable("controls.keybinds.duplicateKeybinds", conflictingBindings)));
            } else {
                this.setTooltip(null);
            }
        }
    }
}
