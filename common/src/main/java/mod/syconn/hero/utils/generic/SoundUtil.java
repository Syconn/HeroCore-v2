package mod.syconn.hero.utils.generic;

import mod.syconn.hero.utils.Constants;

public class SoundUtil {

    @SafeVarargs
    public static <R> R randomSound(R... choices) {
        return choices[Constants.RANDOM.nextIntBetweenInclusive(0, choices.length - 1)];
    }
}
