package mod.syconn.hero.feature.ironman.abilities;

import dev.kosmx.playerAnim.core.util.Ease;
import mod.syconn.hero.core.ModSounds;
import mod.syconn.hero.feature.heros.interfaces.IHeroAbility;
import mod.syconn.hero.feature.heros.interfaces.IHeroType;
import mod.syconn.hero.feature.heros.interfaces.IServerSynced;
import mod.syconn.hero.feature.heros.interfaces.IVFXRenderer;
import mod.syconn.hero.feature.heros.util.PowerKeybind;
import mod.syconn.hero.item.IronmanArmorItem;
import mod.syconn.hero.network.Network;
import mod.syconn.hero.network.messages.serverside.FlightTravelPacket;
import mod.syconn.hero.network.messages.serverside.HoverPacket;
import mod.syconn.hero.network.messages.serverside.PlaySoundPacket;
import mod.syconn.hero.utils.Constants;
import mod.syconn.hero.utils.generic.AnimationUtil;
import mod.syconn.hero.utils.generic.NBTUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

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
    public void clientTick(Player player) { // TODO PARTICLES && Add Chats Render Ribbon through Layers && Hover Ticks Broken && CLIENT TICKS WHEN GAME PAUSED
        toggleFlightMode.tick();

        renderVFX(player); // TODO MOVE TO SERVER

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
                }
                sendTrackingData(player);
            }
        }

        if (this.mode == FlightMode.HOVER) {
            player.setNoGravity(true);
            this.engagedHover = true;
        } else if (this.engagedHover) player.setNoGravity(false);
    }

    private static void renderVFX(Player player) {
//        Vec3 center = player.getEyePosition();
//
//        float pitch = player.getXRot();
//
//        Vec3 look = player.getLookAngle();
//
//        Vec3 right = look.cross(new Vec3(0,1,0));
//
//        if (right.lengthSqr() < 0.001) {
//            right = new Vec3(1,0,0);
//        }
//
//        right = right.normalize();
//        Vec3 leftHand = center
//                .add(0,-0.45,0)
//                .add(right.scale(-0.55));
//
//        Vec3 rightHand = center
//                .add(0,-0.45,0)
//                .add(right.scale(0.55));
//
//        Vec3 leftFoot = player.position()
//                .add(0,0.12,0)
//                .add(right.scale(-0.18));
//
//        Vec3 rightFoot = player.position()
//                .add(0,0.12,0)
//                .add(right.scale(0.18));
//
//        Vec3 velocity = player.getDeltaMovement();
//
//        Vec3 exhaust;
//
//        if (velocity.lengthSqr() > 0.01) {
//            exhaust = velocity.normalize().scale(-0.16);
//        } else {
//            exhaust = new Vec3(0,-0.16,0);
//        }
//
//        spawn(player.level(), leftHand, exhaust);
//        spawn(player.level(), rightHand, exhaust);
//
//        spawn(player.level(), leftFoot, exhaust);
//        spawn(player.level(), rightFoot, exhaust);
    }

    private static void spawn(Level level, Vec3 pos, Vec3 dir) {

        level.addParticle(
                ParticleTypes.END_ROD,

                pos.x,
                pos.y,
                pos.z,

                dir.x,
                dir.y,
                dir.z
        );

        level.addParticle(
                ParticleTypes.ELECTRIC_SPARK,

                pos.x,
                pos.y,
                pos.z,

                dir.x * .4,
                dir.y * .4,
                dir.z * .4
        );
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
        return tag;
    }

    @Override
    public void additionalSync(Player player, CompoundTag tag) {
        this.flying = tag.getBoolean("flying");
        this.flyingTicks = tag.getInt("flyingTicks");
        this.renderFlying = tag.getBoolean("renderFlying");
        this.slowFallingTicks = tag.getFloat("slowFallingTicks");
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
