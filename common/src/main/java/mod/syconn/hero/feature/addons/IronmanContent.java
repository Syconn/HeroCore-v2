package mod.syconn.hero.feature.addons;

import mod.syconn.hero.feature.ironman.server.data.SuitJson;
import mod.syconn.hero.utils.Constants;
import mod.syconn.hero.utils.server.JsonResourceReloader;

public class IronmanContent {

    public static final JsonResourceReloader<SuitJson> SUITS = new JsonResourceReloader<>(Constants.withId("suits"), "suits", SuitJson::fromJson, SuitJson::readTag);
}
