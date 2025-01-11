package mod.syconn.hero.common.item;

import dev.architectury.event.EventResult;
import mod.syconn.hero.util.Energy;
import net.minecraft.ChatFormatting;
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
        tooltipComponents.add(Component.literal("Energy: " + Energy.getEnergy(stack) + "/" + Energy.getMax(stack)).withStyle(ChatFormatting.AQUA));
    }
}
