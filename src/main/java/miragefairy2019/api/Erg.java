package miragefairy2019.api;

import javax.annotation.Nonnull;

public enum Erg {
    ATTACK,
    CRAFT,
    HARVEST,
    LIGHT,
    FLAME,
    WATER,
    CRYSTAL,
    SOUND,
    SPACE,
    WARP,
    KINESIS,
    DESTROY,
    CHEMICAL,
    SLASH,
    LIFE,
    KNOWLEDGE,
    ENERGY,
    SUBMISSION,
    CHRISTMAS,
    FREEZE,
    THUNDER,
    LEVITATE,
    SENSE,
    ;

    @Nonnull
    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
