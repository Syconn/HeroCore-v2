package mod.syconn.hero.feature.ironman.client.sounds;

import mod.syconn.hero.core.ModSounds;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;

public class IronFlightSoundInstance extends AbstractTickableSoundInstance {

    private final Player player;
    private final boolean hover;

    public IronFlightSoundInstance(Player player, boolean hover) {
        super(hover ? ModSounds.HOVER.get() : ModSounds.FLYING.get(), SoundSource.PLAYERS, RandomSource.create());
        this.player = player;
        this.hover = hover;
        this.looping = true;
    }

    @Override
    public void tick() {
        if (player.isRemoved()) {
            this.stop();
            return;
        }

        this.volume = hover ? 0.3f : player.isSprinting() ? 0.6f : 0.4f;
        this.pitch = hover ? 0.2f : 0.5F;
        this.pitch += player.getRandom().nextFloat() * 0.1f;
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
    }

    public void forceStop() {
        this.stop();
    }

    public int soundNumber() {
        return this.hover ? 2 : 1;
    }
}
