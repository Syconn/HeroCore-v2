package mod.syconn.hero.utils.client;

import mod.syconn.hero.utils.generic.NBTUtil;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

//? if 1.20.1 {
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.core.registries.BuiltInRegistries;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
//? } else {
/*import net.minecraft.nbt.NbtOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import mod.syconn.hero.utils.generic.CodecUtil;
*///? }

public record ParticleEvent(Vec3 pos, Vec3 velocity, ParticleOptions type) {

    //? if 1.20.1 {
    public CompoundTag save() {
        var tag = new CompoundTag();
        tag.put("pos", NBTUtil.putVec3(pos));
        tag.put("velocity", NBTUtil.putVec3(velocity));
        tag.putString("type", type.writeToString());
        return tag;
    }

    public static ParticleEvent from(CompoundTag tag) {
        try {
            return new ParticleEvent(NBTUtil.getVec3(tag.getCompound("pos")), NBTUtil.getVec3(tag.getCompound("velocity")), ParticleArgument.readParticle(new StringReader(tag.getString("type")), BuiltInRegistries.PARTICLE_TYPE.asLookup()));
        } catch (CommandSyntaxException e) {
            return null;
        }
    }
    //? } else {
    /*public static final StreamCodec<RegistryFriendlyByteBuf, ParticleEvent> STREAM_CODEC =
            StreamCodec.composite(CodecUtil.VEC3, ParticleEvent::pos, CodecUtil.VEC3, ParticleEvent::velocity, ParticleTypes.STREAM_CODEC, ParticleEvent::type, ParticleEvent::new);

    public CompoundTag save(HolderLookup.Provider registries) {
        var tag = new CompoundTag();
        tag.put("pos", NBTUtil.putVec3(pos));
        tag.put("velocity", NBTUtil.putVec3(velocity));
        tag.put("type", ParticleTypes.CODEC.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), type).result().orElseThrow());
        return tag;
    }

    public static ParticleEvent from(CompoundTag tag, HolderLookup.Provider registries) {
        return new ParticleEvent(NBTUtil.getVec3(tag.getCompound("pos")), NBTUtil.getVec3(tag.getCompound("velocity")),
                ParticleTypes.CODEC.parse(registries.createSerializationContext(NbtOps.INSTANCE), tag.get("particle")).result().orElse(null));
    }
    *///? }
}
