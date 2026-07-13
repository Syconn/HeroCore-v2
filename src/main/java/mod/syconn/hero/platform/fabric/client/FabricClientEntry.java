//? if fabric {
package mod.syconn.hero.platform.fabric.client;

import net.fabricmc.api.ClientModInitializer;

//? if >=1.21.1 {
/*import mod.syconn.hero.utils.animation.HeroAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import mod.syconn.hero.utils.Constants;
import com.zigythebird.playeranimcore.enums.PlayState;
*///? }

public class FabricClientEntry implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        //? if >= 1.21.1 {
        /*//PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(Constants.ANIM_LAYER, 1500,
                player -> new HeroAnimationController(player, (controller, state, animSetter) -> PlayState.STOP));
        *///? }
    }
}
//? }