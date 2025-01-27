package mod.syconn.hero.network.messages;

import dev.architectury.networking.NetworkManager;
import mod.syconn.hero.common.entity.ThrownMjolnir;
import mod.syconn.hero.core.ModDamageTypes;
import mod.syconn.hero.util.AbilityUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;

import java.util.function.Supplier;

public class MessageMjolnirStrikeEnemy {

    public MessageMjolnirStrikeEnemy() { }

    public MessageMjolnirStrikeEnemy(FriendlyByteBuf buf) {
        this();
    }

    public void encode(FriendlyByteBuf buf) { }

    public void apply(Supplier<NetworkManager.PacketContext> context) {
        context.get().queue(() -> {
            Player player = context.get().getPlayer();
            EntityHitResult target = AbilityUtil.playerRaycast(player, 50);
            if (target != null) {
                ThrownMjolnir.strikeLightning(player.level(), target.getEntity().blockPosition());
                target.getEntity().hurt(ModDamageTypes.mjolnir(player, target.getEntity()), 10f);
                player.getCooldowns().addCooldown(AbilityUtil.getHolding(player).getItem(), 120);
            } else player.displayClientMessage(Component.literal("No Entity in Range").withStyle(ChatFormatting.GOLD), true);
        });
    }
}
