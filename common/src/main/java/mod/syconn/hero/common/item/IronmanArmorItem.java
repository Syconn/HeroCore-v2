package mod.syconn.hero.common.item;

import com.mojang.blaze3d.platform.InputConstants;
import mod.syconn.hero.client.KeyBindings;
import mod.syconn.hero.util.EnergyUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IronmanArmorItem extends ArmorItem {

    public IronmanArmorItem(ArmorMaterial material, ArmorItem.Type armorType, Properties properties) {
        super(material, armorType, properties);
    }

    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
        tooltipComponents.add(Component.literal("Energy: " + EnergyUtil.getEnergy(stack) + "/" + EnergyUtil.getMax(stack)).withStyle(ChatFormatting.AQUA));

        if (level != null && level.isClientSide && InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LCONTROL)) {
            tooltipComponents.add(Component.literal(""));
            tooltipComponents.add(Component.literal("To Change Flight Mode press " + KeyBindings.key(KeyBindings.ABILITY1)).withStyle(ChatFormatting.GOLD));
            tooltipComponents.add(Component.literal("To Lower/Raise Helmet press " + KeyBindings.key(KeyBindings.ABILITY2)).withStyle(ChatFormatting.GOLD));
        } else {
            tooltipComponents.add(Component.literal(""));
            tooltipComponents.add(Component.literal("Press LCTRL for more info").withStyle(ChatFormatting.GOLD));
        }
    }
}
