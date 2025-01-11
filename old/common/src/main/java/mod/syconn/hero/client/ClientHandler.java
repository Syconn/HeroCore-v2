package mod.syconn.hero.client;

import mod.syconn.hero.client.render.entity.MjolnirRenderer;
import mod.syconn.hero.client.screen.AbilityScreen;
import mod.syconn.hero.extra.core.Keys;
import mod.syconn.hero.extra.data.attachment.SuperPower;
import mod.syconn.hero.extra.platform.Services;
import mod.syconn.hero.registrar.AttachmentRegistrar;
import mod.syconn.hero.registrar.EntityRegistrar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import java.util.function.BiConsumer;

public class ClientHandler {
    
    private static int jumpClicks;
    private static int ticks;

    public static void registerRenderers(BiConsumer<EntityType<? extends Entity>, EntityRendererProvider> entityRenderers, BiConsumer<BlockEntityType<? extends BlockEntity>, BlockEntityRendererProvider> blockEntityRenderers) {
        entityRenderers.accept(EntityRegistrar.MJOLNIR_ENTITY_TYPE.get(), MjolnirRenderer::new);
    }

    public static void onClientPlayerTick(LocalPlayer player) {
        if (player != null) {
            handleMappings(player);

            if (jumpClicks > 0) {
                if (ticks <= 20) {
                    System.out.println("reset");
                    ticks = 0;
                    jumpClicks = 0;
                }
                ticks++;
            }
        }
    }
    
    private static void handleMappings(LocalPlayer player) {
        SuperPower powers = Services.ATTACHED_DATA.get(AttachmentRegistrar.SUPER_POWER, player);
        
        if (Keys.ABILITIES_MENU.consumeClick()) 
            Minecraft.getInstance().setScreen(new AbilityScreen(player, Services.ATTACHED_DATA.get(AttachmentRegistrar.SUPER_POWER, player)));
        
        if (Minecraft.getInstance().options.keyJump.consumeClick()) {
            jumpClicks++;
            System.out.println(jumpClicks);

            if (jumpClicks > 1) {
                Services.ATTACHED_DATA.set(AttachmentRegistrar.SUPER_POWER, powers.toggleFlight(player), player);
                jumpClicks = 0;

                System.out.println("fly");
            }
        }
    }
}
