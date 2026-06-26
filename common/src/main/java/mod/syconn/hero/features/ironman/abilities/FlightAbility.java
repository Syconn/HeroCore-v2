package mod.syconn.hero.features.ironman.abilities;

import dev.kosmx.playerAnim.core.util.Ease;
import mod.syconn.hero.client.particle.TrailParticleOptions;
import mod.syconn.hero.core.ModSounds;
import mod.syconn.hero.features.heros.interfaces.IHeroAbility;
import mod.syconn.hero.features.heros.interfaces.IHeroType;
import mod.syconn.hero.features.heros.interfaces.IServerSynced;
import mod.syconn.hero.features.heros.interfaces.IVFXRenderer;
import mod.syconn.hero.features.heros.util.PowerKeybind;
import mod.syconn.hero.features.ironman.item.IronmanArmorItem;
import mod.syconn.hero.features.ironman.server.data.SuitTag;
import mod.syconn.hero.network.Network;
import mod.syconn.hero.network.messages.serverside.FlightTravelPacket;
import mod.syconn.hero.network.messages.serverside.HoverPacket;
import mod.syconn.hero.network.messages.serverside.PlaySoundPacket;
import mod.syconn.hero.utils.Constants;
import mod.syconn.hero.utils.client.ParticleEvent;
import mod.syconn.hero.utils.generic.AnimationUtil;
import mod.syconn.hero.utils.generic.NBTUtil;
import mod.syconn.hero.utils.interfaces.ICustomArmor;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class FlightAbility implements IHeroAbility, IServerSynced, IVFXRenderer {

    public static final ResourceLocation TYPE = Constants.withId("flight");

    private final IHeroType hero;
    private final PowerKeybind toggleFlightMode = new PowerKeybind(Constants.CONFIG.ironmanSettings.toggleFlightMode.get());
    private FlightMode mode = FlightMode.NORMAL;
    private boolean engagedHover = false;
    private boolean initialJump = false;
    private boolean flying = false;
    private boolean renderFlying = false;
    private boolean landed = true;
    private boolean wasSprinting = false;
    private int flyingTicks = 0;
    private float slowFallingTicks = 0;

    public FlightAbility(IHeroType hero) {
        this.hero = hero;
    }

    @Override
    public void clientTick(Player player) {
        toggleFlightMode.tick();

        if (!usable(player)) {
            mode = FlightMode.NORMAL;
            flying = false;
            flyingTicks = 0;
            initialJump = false;
            this.sendAllData(player);
            return;
        }

        if (Minecraft.getInstance().options.keyJump.consumeClick() && initialJump && !player.onGround() && mode == FlightMode.NORMAL && !flying) {
            flying = true;
            float pitch = 0.95f + player.getRandom().nextFloat() * 0.1f;
            Network.CHANNEL.sendToServer(new PlaySoundPacket(ModSounds.TAKE_OFF.get(), SoundSource.PLAYERS, 0.5f, pitch));
        }

        if (Minecraft.getInstance().options.keyJump.consumeClick() && !initialJump && mode == FlightMode.NORMAL) initialJump = true;

        if (usable(player)) this.requiresUpdate(player);

        while (this.toggleFlightMode.consumeClick()) {
            this.cycleMode();
            player.displayClientMessage(Component.literal("Engaging ").withStyle(ChatFormatting.GOLD).append(mode == FlightMode.HOVER ? "Hover" : "Normal").withStyle(ChatFormatting.AQUA)
                    .append(" Mode").withStyle(ChatFormatting.GOLD), true);
            this.sendSpecificData(player, this.writeData(player));
            this.sendClientSyncData(player);
        }

        if (this.mode == FlightMode.HOVER) {
            var options = Minecraft.getInstance().options;
            Network.CHANNEL.sendToServer(new HoverPacket(vector(options.keyJump.isDown(), options.keyShift.isDown()), vector(options.keyUp.isDown(), options.keyDown.isDown()), vector(options.keyLeft.isDown(), options.keyRight.isDown())));
            if (flyingTicks < 15 && !player.onGround()) flyingTicks++;
            else if (player.onGround()) flyingTicks--;
            flying = false;
            renderFlying = false;
            initialJump = false;

            var worldY = player.level().getHeight(Heightmap.Types.MOTION_BLOCKING, player.getBlockX(), player.getBlockZ());
            if ((player.onGround() || player.getY() - worldY < 0.65f)) {
                flying = false;
                if (flyingTicks > 0) flyingTicks--;
            }

            sendClientSyncData(player);
        }

        if (this.mode == FlightMode.NORMAL) {
            boolean dirty = false;
            boolean stillFlying = false;
            if (Minecraft.getInstance().options.keyJump.isDown() && flying) {
                var options = Minecraft.getInstance().options;
                Network.CHANNEL.sendToServer(new FlightTravelPacket(vector(options.keyJump.isDown(), options.keyShift.isDown()), vector(options.keyUp.isDown(), options.keyDown.isDown()), vector(options.keyLeft.isDown(), options.keyRight.isDown())));
                if (flyingTicks < 15) flyingTicks++;
                stillFlying = true;
                dirty = true;

                if (!this.renderFlying) {
                    float pitch = 0.95f + player.getRandom().nextFloat() * 0.1f;
                    Network.CHANNEL.sendToServer(new PlaySoundPacket(ModSounds.TAKE_OFF.get(), SoundSource.PLAYERS, 0.5f, pitch));
                }

                if (player.isSprinting() && !wasSprinting && flying) {
                    float pitch = 0.95f + player.getRandom().nextFloat() * 0.1f;
                    Network.CHANNEL.sendToServer(new PlaySoundPacket(ModSounds.TAKE_OFF.get(), SoundSource.PLAYERS, 0.5f, pitch));
                }
                this.wasSprinting = player.isSprinting();
            }

            if (flyingTicks > 0) {
                if (!stillFlying) flyingTicks--;
                if (flying) renderFlying = true;
                dirty = true;
            }

            if ((this.flyingTicks <= 0 || !this.flying) && renderFlying) {
                this.renderFlying = false;
                dirty = true;
            }

            if (player.onGround() && flying && !stillFlying) {
                flying = false;
                initialJump = false;
                if (flyingTicks > 0) flyingTicks--;
                dirty = true;
            }

            if (dirty) sendClientSyncData(player);
        }
    }

    @Override
    public void serverTick(Player player) {
        if (!usable(player)) {
            if (this.engagedHover) player.setNoGravity(false);
            return;
        }

        if (this.mode == FlightMode.NORMAL) {
            double slowHeight = 20;
            BlockPos pos = player.blockPosition();
            int groundY = player.level().getHeight(Heightmap.Types.MOTION_BLOCKING, pos.getX(), pos.getZ());
            Vec3 motion = player.getDeltaMovement();
            double targetSpeed = -0.08;
            double distance = player.getY() - groundY;

            if (distance <= slowHeight && !player.onGround() && this.flyingTicks < 10) {
                if (slowFallingTicks < 8) {
                    float amount = Mth.clamp((float)(distance / slowHeight), 0.1F, 1.0F);
                    slowFallingTicks = Math.max(0, Math.min(8, slowFallingTicks + amount));
                    if (slowFallingTicks >= 6 && landed) landed = false;
                    sendTrackingData(player);
                }
                if (motion.y < targetSpeed) {
                    double strength = 1.0 - distance / slowHeight;
                    double newY = Mth.lerp(strength * 0.25, motion.y, targetSpeed);
                    player.setDeltaMovement(motion.x, newY, motion.z);
                    player.fallDistance = 0;
                    player.hurtMarked = true;
                }
            } else if (slowFallingTicks > 0) {
                slowFallingTicks = Math.max(0, slowFallingTicks - 1);
                if (!landed && player instanceof ServerPlayer sp && player.onGround()) {
                    landed = true;
                    AnimationUtil.notifyAndPlay(sp, "ironman.landing", 1.5f, 1, Ease.OUTCUBIC);
                    float pitch = 0.95f + player.getRandom().nextFloat() * 0.1f;
                    Network.CHANNEL.sendToServer(new PlaySoundPacket(ModSounds.LANDING.get(), SoundSource.PLAYERS, 0.5f, pitch));
                    if (player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ICustomArmor) groundParticles(player);
                }
                sendTrackingData(player);
            }
        }

        if (this.mode == FlightMode.HOVER) {
            player.setNoGravity(true);
            this.engagedHover = true;
        } else if (this.engagedHover) player.setNoGravity(false);
    }

    private void groundParticles(Player player) {
        var level = player.level();
        var center = player.position();
        int count = 24;
        for (int i = 0; i < count; i++) {
            var groundPos = player.blockPosition().below();
            double angle = (Math.PI * 2.0 * i) / count;
            double speed = 1 * 0.35d;
            double dx = Math.cos(angle), dz = Math.sin(angle);
            double radius = 0.5;
            double x = center.x + dx * radius, z = center.z + dz * radius;
            var state = level.getBlockState(groundPos);
            var color = SuitTag.getOrCreate(player.getItemBySlot(EquipmentSlot.FEET)).color;
            var start = new Vector3f(FastColor.ARGB32.red(color) / 255f, FastColor.ARGB32.green(color) / 255f, FastColor.ARGB32.blue(color) / 255f);
            var end = new Vector3f(start.x * 0.35f, start.y * 0.35f, start.z * 0.35f);
            if (player instanceof ServerPlayer sp) addParticleEvent(sp, new ParticleEvent(new Vec3(x, player.getY() + 0.05, z), new Vec3(dx * speed, 0.02, dz * speed), new BlockParticleOption(ParticleTypes.BLOCK, state)));
            if (player instanceof ServerPlayer sp) addParticleEvent(sp, new ParticleEvent(new Vec3(x, player.getY() + 0.05, z), new Vec3(dx * speed, 0.02, dz * speed), new TrailParticleOptions(start, end)));
        }
    }

    private static int vector(boolean input, boolean otherInput) {
        if (input == otherInput) return 0;
        else return input ? 1 : -1;
    }

    private void requiresUpdate(Player player) {
        var stack = player.getItemBySlot(EquipmentSlot.FEET);
        var tag = this.getStackTag(stack);
        this.mode = tag.contains("mode") ? NBTUtil.getEnum(FlightMode.class, tag.getCompound("mode")) : FlightMode.NORMAL;
    }

    @Override
    public ResourceLocation heroType() {
        return this.hero.id();
    }

    @Override
    public ResourceLocation id() {
        return TYPE;
    }

    @Override
    public CompoundTag writeData(Player player) {
        var tag = new CompoundTag();
        tag.put("mode", NBTUtil.putEnum(this.mode));
        return tag;
    }

    @Override
    public void readData(Player player, CompoundTag tag) {
        var stack = player.getItemBySlot(EquipmentSlot.FEET);
        this.putStackTag(stack, NBTUtil.combineTags(this.getStackTag(stack), tag));
    }

    @Override
    public boolean usable(Player player) {
        return IronmanArmorItem.wearingFullSameSuit(player);
    }

    @Override
    public void syncPlayer(Player player) {
        this.requiresUpdate(player);
    }

    @Override
    public CompoundTag writeAdditionalSync() {
        var tag = new CompoundTag();
        tag.putBoolean("flying", this.flying);
        tag.putInt("flyingTicks", this.flyingTicks);
        tag.putBoolean("renderFlying", this.renderFlying);
        tag.putFloat("slowFallingTicks", this.slowFallingTicks);
        tag.put("mode", NBTUtil.putEnum(this.mode));
        return tag;
    }

    @Override
    public void additionalSync(Player player, CompoundTag tag) {
        this.flying = tag.getBoolean("flying");
        this.flyingTicks = tag.getInt("flyingTicks");
        this.renderFlying = tag.getBoolean("renderFlying");
        this.slowFallingTicks = tag.getFloat("slowFallingTicks");
        this.mode = NBTUtil.getEnum(FlightMode.class, tag.getCompound("mode"));
        IServerSynced.super.additionalSync(player, tag);
    }

    public FlightMode getMode() {
        return mode;
    }

    public boolean isFlying() {
        return renderFlying;
    }

    public int getFlyingTicks() {
        return flyingTicks;
    }

    public int getSlowFallingTicks() {
        return (int) slowFallingTicks;
    }

    private void cycleMode() {
        this.mode = switch (this.mode) {
            case HOVER -> FlightMode.NORMAL;
            case NORMAL -> FlightMode.HOVER;
        };
    }

    public enum FlightMode {
        NORMAL,
        HOVER
    }
}
