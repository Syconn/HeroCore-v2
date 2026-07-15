package mod.syconn.hero.features.ironman.abilities;

import mod.syconn.hero.features.heros.interfaces.IHeroAbility;
import mod.syconn.hero.features.heros.interfaces.IHeroHolder;
import mod.syconn.hero.features.heros.interfaces.IHeroType;
import mod.syconn.hero.features.ironman.Ironman;
import mod.syconn.hero.features.ironman.client.sounds.IronFlightSoundInstance;
import mod.syconn.hero.features.ironman.item.IronmanArmorItem;
import mod.syconn.hero.utils.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

//? if forge {
/*import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
*///? }

//? if fabric {
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
//? }

public class IronmanSoundManagerAbility implements IHeroAbility {

    public static final ResourceLocation TYPE = Constants.withId("sound_manager");

    //? if forge
    //@OnlyIn(Dist.CLIENT)
    //? if fabric
    @Environment(EnvType.CLIENT)
    private static Map<UUID, IronFlightSoundInstance> FLIGHT_SOUNDS;

    private final IHeroType hero;

    public IronmanSoundManagerAbility(IHeroType hero) {
        this.hero = hero;
    }

    //? if forge
    //@OnlyIn(Dist.CLIENT)
    //? if fabric
    @Environment(EnvType.CLIENT)
    @Override
    public void clientTick(Player player) {
        if (FLIGHT_SOUNDS == null) FLIGHT_SOUNDS = new HashMap<>();

        if (usable(player)) {
            var level = player.level();

            var activePlayers = new HashSet<UUID>();
            for (var target : level.players()) {
                var soundInstance = activeSound(target);
                if (soundInstance == 0) continue;
                if (FLIGHT_SOUNDS.containsKey(target.getUUID()) && FLIGHT_SOUNDS.get(target.getUUID()).soundNumber() != soundInstance) continue;

                activePlayers.add(target.getUUID());
                FLIGHT_SOUNDS.computeIfAbsent(target.getUUID(), uuid -> {
                    var sound = new IronFlightSoundInstance(target, soundInstance == 2);
                    Minecraft.getInstance().getSoundManager().play(sound);
                    return sound;
                });
            }

            var it = FLIGHT_SOUNDS.entrySet().iterator();
            while (it.hasNext()) {
                var entry = it.next();
                if (!activePlayers.contains(entry.getKey())) {
                    entry.getValue().forceStop();
                    it.remove();
                }
            }
        }
    }

    private static int activeSound(Player player) {
        if (player instanceof IHeroHolder holder) {
            var controller = holder.hero$getManager().getType(Ironman.class).getAbility(FlightAbility.class);
            var grounded = (player.onGround() || player.getY() - player.level().getHeight(Heightmap.Types.MOTION_BLOCKING, player.getBlockX(), player.getBlockZ()) < 0.65f);
            if (controller.isFlying()) return 1;
            else if (controller.getSlowFallingTicks() > 3 || (controller.getMode() == FlightAbility.FlightMode.HOVER && !grounded)) return 2;
        }
        return 0;
    }

    @Override
    public boolean usable(Player player) {
        return IronmanArmorItem.wearingFullSameSuit(player);
    }

    @Override
    public ResourceLocation heroType() {
        return hero.id();
    }

    @Override
    public ResourceLocation id() {
        return TYPE;
    }

    @Override
    public CompoundTag writeData(Player player) {
        return new CompoundTag();
    }

    @Override
    public void readData(Player player, CompoundTag tag) { }
}
