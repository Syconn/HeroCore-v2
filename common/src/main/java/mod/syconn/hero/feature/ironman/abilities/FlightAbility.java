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
import net.minecraft.world.entity.player.Player;

public class FlightAbility implements IHeroAbility, IServerSynced {

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
    public void clientTick(Player player) { // TODO PARTICLES && Sound Effects && maybe shaders STUFF
        toggleFlightMode.tick();

        if (!usable(player)) {
            mode = FlightMode.NORMAL;
            flying = false;
            flyingTicks = 0;
            this.sendAllData(player);
            return;
        }

        if (Minecraft.getInstance().options.keyJump.consumeClick() && initialJump && !player.onGround() && mode == FlightMode.NORMAL) flying = true;
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
        return tag;
    }

    @Override
    public void additionalSync(Player player, CompoundTag tag) {
        this.flying = tag.getBoolean("flying");
        this.flyingTicks = tag.getInt("flyingTicks");
        this.renderFlying = tag.getBoolean("renderFlying");

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
