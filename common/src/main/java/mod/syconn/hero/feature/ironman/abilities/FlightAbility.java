package mod.syconn.hero.feature.ironman.abilities;

import mod.syconn.hero.feature.heros.interfaces.IHeroAbility;
import mod.syconn.hero.feature.heros.interfaces.IHeroType;
import mod.syconn.hero.feature.heros.interfaces.IServerSynced;
import mod.syconn.hero.feature.heros.util.PowerKeybind;
import mod.syconn.hero.item.IronmanArmorItem;
import mod.syconn.hero.network.Network;
import mod.syconn.hero.network.messages.serverside.FlightTravelPacket;
import mod.syconn.hero.network.messages.serverside.HoverPacket;
import mod.syconn.hero.utils.Constants;
import mod.syconn.hero.utils.generic.NBTUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;

public class FlightAbility implements IHeroAbility, IServerSynced { // TODO CURRENTLY ANIMATION DOESN't DOESNT SYNC ALSO SERVER TICK SEEMS QUESTIONABLE, APPEARS EVERY CLIENT IS FORCING ALL UPDATES

    public static final ResourceLocation TYPE = Constants.withId("flight");

    private final IHeroType hero;
    private final PowerKeybind toggleFlightMode = new PowerKeybind(Constants.CONFIG.ironmanSettings.toggleFlightMode.get());
    private FlightMode mode = FlightMode.NORMAL;
    private boolean engagedHover = false;
    private boolean initialJump = false;
    private boolean flying = false;
    private boolean renderFlying = false;
    private int flyingTicks = 0;

    public FlightAbility(IHeroType hero) {
        this.hero = hero;
    }

    @Override
    public void clientTick(Player player) { // TODO PARTICLE STUFF AND ANIMATIONS
        toggleFlightMode.tick();

        if (!usable(player)) {
//            mode = FlightMode.NORMAL;
//            flying = false;
//            flyingTicks = 0;
//            this.sendAllData(player);
            return;
        }

//        if (Minecraft.getInstance().options.keyJump.consumeClick() && initialJump && !player.onGround()) flying = true;
//        if (Minecraft.getInstance().options.keyJump.consumeClick() && !initialJump) initialJump = true;

        if (flying) System.out.println("flight");

        if (flying) {
            flyingTicks++;
            this.sendSyncData(player);
        }

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
        }

        if (this.mode == FlightMode.NORMAL && Minecraft.getInstance().options.keyJump.isDown()) {
            var options = Minecraft.getInstance().options;
            Network.CHANNEL.sendToServer(new FlightTravelPacket(vector(options.keyJump.isDown(), options.keyShift.isDown()), vector(options.keyUp.isDown(), options.keyDown.isDown()), vector(options.keyLeft.isDown(), options.keyRight.isDown())));

            if (!this.flying) {
                this.flying = true;
                sendSyncData(player);
            }
        }

        if (this.flying && this.flyingTicks > 0) {
            renderFlying = true;
            sendSyncData(player);
        }
        if ((this.flyingTicks <= 0 || !this.flying) && renderFlying) {
            this.renderFlying = false;
            sendSyncData(player);
        }

        if (player.onGround() && flying) {
            flying = false;
            flyingTicks = 0;
            this.sendSyncData(player);
        }

        if (flying) System.out.println("client for " + this.flyingTicks);
    }

    @Override
    public void serverTick(Player player) {
        if (!usable(player)) {
            if (this.engagedHover) player.setNoGravity(false);
            return;
        }

        if (flying) System.out.println("server for " + this.flyingTicks);

        if (this.mode == FlightMode.NORMAL && this.flying) player.fallDistance = 0;
        if (this.mode == FlightMode.HOVER) {
            player.setNoGravity(true);
            this.engagedHover = true;
        } else if (this.engagedHover) player.setNoGravity(false);
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

//        System.out.println("write " + this.flying);
        return tag;
    }

    @Override
    public void additionalSync(CompoundTag tag) {
        this.flying = tag.getBoolean("flying");
        this.flyingTicks = tag.getInt("flyingTicks");
        this.renderFlying = tag.getBoolean("renderFlying");

//        System.out.println("read " + this.flying);
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
