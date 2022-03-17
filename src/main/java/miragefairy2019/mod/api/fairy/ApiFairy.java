package miragefairy2019.mod.api.fairy;

import miragefairy2019.mod3.fairy.FairyTypeEmpty;
import miragefairy2019.mod3.fairy.api.IFairyType;

public class ApiFairy {

    public static IFairyType empty() {
        return new FairyTypeEmpty();
    }

}
