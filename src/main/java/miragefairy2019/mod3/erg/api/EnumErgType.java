package miragefairy2019.mod3.erg.api;

public enum EnumErgType {
    attack,
    craft,
    harvest,
    light,
    flame,
    water,
    crystal,
    sound,
    space,
    warp,
    shoot,
    destroy,
    chemical,
    slash,
    life,
    knowledge,
    energy,
    submission,
    christmas,
    freeze,
    thunder,
    levitate,
    sense,
    ;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
