package mod.syconn.hero.utils.config;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import eu.midnightdust.lib.config.EntryInfo;
import eu.midnightdust.lib.config.MidnightConfig;
import eu.midnightdust.lib.config.MidnightConfigListWidget;
import eu.midnightdust.lib.config.MidnightConfigScreen;
import mod.syconn.hero.core.ModKeys;
import mod.syconn.hero.utils.HeroConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtendedConfig extends MidnightConfig {

    private record AutoKey(Field field, String description) {}
    private final Map<String, List<AutoKey>> AUTO_KEYS = new HashMap<>();

    private void generateKeybindings() {
        for (var field : ModKeys.class.getDeclaredFields()) {
            var annotation = field.getAnnotation(ConfigKey.class);
            if (annotation == null) continue;
            for (var entry : annotation.value()) AUTO_KEYS.computeIfAbsent(entry.hero(), h -> new ArrayList<>()).add(new AutoKey(field, entry.desc()));
        }
    }

    @Override
    public void onTabInit(String tabName, MidnightConfigListWidget list, MidnightConfigScreen screen) {
        if (AUTO_KEYS.isEmpty()) generateKeybindings();

        for (var autoKey : AUTO_KEYS.getOrDefault(tabName, List.of())) {
            try {
                KeyMapping key = (KeyMapping) autoKey.field().get(null);
                addKeybind(key, autoKey.description(), list, screen);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected static void addKeybind(KeyMapping binding, MidnightConfigListWidget list, MidnightConfigScreen screen) {
        var editButton = new ConfigKeybind(screen.width - 185, 0, 95, 20, binding);
        var resetButton = Button.builder(Component.translatable("controls.reset"), (arg) -> {
            //? if forge
            binding.setToDefault();

            binding.setKey(binding.getDefaultKey());
            KeyMapping.resetMapping();
            screen.updateList();
        }).bounds(screen.width - 185 + 105, 0, 50, 20).build();
        editButton.resetButton = resetButton;
        list.addButton(Lists.newArrayList(editButton, resetButton), Component.translatable(binding.getName()), new EntryInfo(null, screen.modid));
    }

    protected static void addKeybind(KeyMapping binding, String name, MidnightConfigListWidget list, MidnightConfigScreen screen) {
        var editButton = new ConfigKeybind(screen.width - 185, 0, 95, 20, binding);
        var resetButton = Button.builder(Component.translatable("controls.reset"), (arg) -> {
            //? if forge
            binding.setToDefault();

            binding.setKey(binding.getDefaultKey());
            KeyMapping.resetMapping();
            screen.updateList();
        }).bounds(screen.width - 185 + 105, 0, 50, 20).build();
        editButton.resetButton = resetButton;
        list.addButton(Lists.newArrayList(editButton, resetButton), Component.literal(name), new EntryInfo(null, screen.modid));
    }

    private static class ConfigKeybind extends Button {
        public static Button focusedButton;

        private final KeyMapping binding;
        private @Nullable AbstractButton resetButton;

        public ConfigKeybind(int x, int y, int width, int height, KeyMapping binding) {
            super(x, y, width, height, binding.getTranslatedKeyMessage(), (button) -> {
                ((ConfigKeybind) button).updateMessage(true);
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
                KeyMapping.resetMapping();
                updateMessage(false);
                focusedButton = null;
                return true;
            }
            return super.keyPressed(i, j, k);
        }

        public void updateMessage(boolean focused) {
            var hasConflicts = false;
            var conflictingBindings = Component.empty();
            if (focused)
                this.setMessage(Component.literal("> ").append(this.binding.getTranslatedKeyMessage().copy().withStyle(ChatFormatting.WHITE, ChatFormatting.UNDERLINE)).append(" <").withStyle(ChatFormatting.YELLOW));
            else {
                this.setMessage(this.binding.getTranslatedKeyMessage());

                if (!this.binding.isUnbound()) {
                    for (KeyMapping keyBinding : Minecraft.getInstance().options.keyMappings) {
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
