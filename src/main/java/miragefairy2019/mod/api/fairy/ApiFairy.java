package miragefairy2019.mod.api.fairy;

import miragefairy2019.mod3.fairy.api.IFairyType;
import miragefairy2019.mod3.fairy.FairyTypeEmpty;

public class ApiFairy {

    public static IFairyType empty() {
        return new FairyTypeEmpty();
    }

}
