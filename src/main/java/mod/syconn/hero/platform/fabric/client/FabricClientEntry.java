//? if fabric {
/*package mod.syconn.hero.platform.fabric.client;

import mod.syconn.hero.features.ironman.client.renderers.entity.IronmanFlightLayer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;

//? if >=1.21.1 {
/^import mod.syconn.hero.utils.animation.HeroAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import mod.syconn.hero.utils.Constants;
import com.zigythebird.playeranimcore.enums.PlayState;
^///? }

public class FabricClientEntry implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(((type, renderer, registration, context) -> {
            if(renderer instanceof PlayerRenderer r) registration.register(new IronmanFlightLayer<>(r));
        }));

        //? if >= 1.21.1 {
        /^//PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(Constants.ANIM_LAYER, 1500,
                player -> new HeroAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));
        ^///? }
    }
}
*///? }