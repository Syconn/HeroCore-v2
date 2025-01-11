package mod.syconn.hero.extra.data.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.syconn.hero.extra.data.powers.HeroType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

// Coded just for Iron man Powers
public class SuperPower implements IAttachmentType<SuperPower> {
    
    private HeroType type = HeroType.NONE;
    private boolean flying = false;
    
    public SuperPower() {}

    public SuperPower(int type) {
        this.type = HeroType.getType(type);
    }

    public SuperPower setType(HeroType type, Player player) {
        this.type = type;
        sync(player);
        return this;
    }

    private int getType() {
        return this.type.getId();
    }

    public HeroType type() {
        return this.type;
    }

    public boolean flying() {
        return flying;
    }

    public SuperPower toggleFlight(Player player) {
        this.flying = !flying;
        sync(player);
        return this;
    }

    public Codec<SuperPower> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("type").forGetter(SuperPower::getType)
        ).apply(instance, SuperPower::new));
    }

    public CompoundTag writeSyncedData() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("type", getType());
        return tag;
    }

    public SuperPower readSyncedData(CompoundTag nbt) {
        this.type = HeroType.getType(nbt.getInt("type"));
        return this;
    }
}
