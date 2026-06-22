package mod.syconn.hero.mixin;

import mod.syconn.hero.utils.interfaces.ICustomArmor;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin {

    @Shadow
    public abstract boolean canReplaceEqualItem(ItemStack candidate, ItemStack existing);

    @Inject(method = "canReplaceCurrentItem", at = @At("HEAD"), cancellable = true)
    private void hero$override(ItemStack candidate, ItemStack existing, CallbackInfoReturnable<Boolean> cir) {
        if (candidate.getItem() instanceof ArmorItem && (candidate.getItem() instanceof ICustomArmor || existing.getItem() instanceof ICustomArmor)) {
            if (EnchantmentHelper.hasBindingCurse(existing)) cir.setReturnValue(false);
            else if (!(existing.getItem() instanceof ArmorItem)) cir.setReturnValue(true);
            else {
                var defense1 = candidate.getItem() instanceof ICustomArmor armor ? armor.getDefense(candidate) : ((ArmorItem)candidate.getItem()).getDefense();
                var defense2 = existing.getItem() instanceof ICustomArmor armor ? armor.getDefense(existing) : ((ArmorItem)existing.getItem()).getDefense();
                var toughness1 = candidate.getItem() instanceof ICustomArmor armor ? armor.getToughness(candidate) : ((ArmorItem)candidate.getItem()).getToughness();
                var toughness2 = existing.getItem() instanceof ICustomArmor armor ? armor.getToughness(existing) : ((ArmorItem)existing.getItem()).getToughness();
                if (defense1 != defense2) cir.setReturnValue(defense1 > defense2);
                else if (toughness1 != toughness2) cir.setReturnValue(toughness1 > toughness2);
                else cir.setReturnValue(this.canReplaceEqualItem(candidate, existing));
            }
        }
    }
}
