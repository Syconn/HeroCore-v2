package mod.syconn.hero.mixin;

import mod.syconn.hero.utils.interfaces.ICustomArmor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    protected abstract boolean doesEmitEquipEvent(EquipmentSlot slot);

    @Inject(method = "onEquipItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Equipable;get(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/Equipable;", shift = At.Shift.AFTER), cancellable = true)
    private void hero$overrideEquipSound(EquipmentSlot slot, ItemStack oldItem, ItemStack newItem, CallbackInfo ci) {
        var bl = newItem.isEmpty() && oldItem.isEmpty();
        if (!bl && !ItemStack.isSameItemSameTags(oldItem, newItem) && !this.firstTick) {
            var equipable = Equipable.get(newItem);
            if (equipable != null && !this.isSpectator() && equipable.getEquipmentSlot() == slot && newItem.getItem() instanceof ICustomArmor ar) {
                if (!this.level().isClientSide() && !this.isSilent()) this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ar.getEquipSound(newItem), this.getSoundSource(), 1.0F, 1.0F);
                if (this.doesEmitEquipEvent(slot)) this.gameEvent(GameEvent.EQUIP);
                ci.cancel();
            }
        }
    }
}
