package miragefairy2019.lib;

import miragefairy2019.mod3.fairy.FairyTypeEmpty;
import miragefairy2019.api.IFairyType;

public class ApiFairy {

    public static IFairyType empty() {
        return new FairyTypeEmpty();
    }

}
