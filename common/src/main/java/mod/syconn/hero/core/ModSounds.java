package mod.syconn.hero.core;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import mod.syconn.hero.utils.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Constants.MOD, Registries.SOUND_EVENT);

    public static final RegistrySupplier<SoundEvent> HELMET_LOWER = register("power.ironman.helmet_lower");
    public static final RegistrySupplier<SoundEvent> HELMET_RAISE = register("power.ironman.helmet_raise");
    public static final RegistrySupplier<SoundEvent> TAKE_OFF = register("power.ironman.take_off");
    public static final RegistrySupplier<SoundEvent> FLYING = register("power.ironman.flying");
    public static final RegistrySupplier<SoundEvent> HOVER = register("power.ironman.hover");
    public static final RegistrySupplier<SoundEvent> LANDING = register("power.ironman.landing");

    private static RegistrySupplier<SoundEvent> register(String key) {
        return SOUNDS.register(key, () -> SoundEvent.createVariableRangeEvent(Constants.withId(key)));
    }
}
