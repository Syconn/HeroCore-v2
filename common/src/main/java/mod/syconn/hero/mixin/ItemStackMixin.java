package mod.syconn.hero.mixin;

import com.google.common.collect.Multimap;
import mod.syconn.hero.utils.interfaces.IItemExtensions;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "getAttributeModifiers", at = @At(value = "RETURN"), cancellable = true)
    public void getAttributeModifiers(EquipmentSlot slot, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir) {
        var stack = (ItemStack) (Object) this;
        if (stack.getItem() instanceof IItemExtensions ext) {
            if (!stack.hasTag() || !stack.getTag().contains("AttributeModifiers", 9)) {
                cir.setReturnValue(ext.getAttributeModifications(stack, slot));
            }
        }
    }
}
