package miragefairy2019.mod.modules.oreseed;

import miragefairy2019.mod3.oreseeddrop.api.EnumOreSeedShape;
import net.minecraft.util.IStringSerializable;

public enum EnumVariantOreSeed implements IStringSerializable {
    TINY(0, "tiny", "tiny", EnumOreSeedShape.TINY),
    LAPIS(1, "lapis", "lapis", EnumOreSeedShape.LAPIS),
    DIAMOND(2, "diamond", "diamond", EnumOreSeedShape.DIAMOND),
    IRON(3, "iron", "iron", EnumOreSeedShape.IRON),
    MEDIUM(4, "medium", "medium", EnumOreSeedShape.MEDIUM),
    LARGE(5, "large", "large", EnumOreSeedShape.LARGE),
    COAL(6, "coal", "coal", EnumOreSeedShape.COAL),
    HUGE(7, "huge", "huge", EnumOreSeedShape.HUGE),

    STRING(8, "string", "string", EnumOreSeedShape.STRING),
    HORIZONTAL(9, "horizontal", "horizontal", EnumOreSeedShape.HORIZONTAL),
    VERTICAL(10, "vertical", "vertical", EnumOreSeedShape.VERTICAL),
    POINT(11, "point", "point", EnumOreSeedShape.POINT),
    STAR(12, "star", "star", EnumOreSeedShape.STAR),
    RING(13, "ring", "ring", EnumOreSeedShape.RING),
    PYRAMID(14, "pyramid", "pyramid", EnumOreSeedShape.PYRAMID),
    CUBE(15, "cube", "cube", EnumOreSeedShape.CUBE),

    ;

    //

    private static final EnumVariantOreSeed[] META_LOOKUP;

    static {
        META_LOOKUP = new EnumVariantOreSeed[EnumVariantOreSeed.values().length];
        EnumVariantOreSeed[] types = EnumVariantOreSeed.values();
        for (int i = 0; i < types.length; i++) {
            EnumVariantOreSeed.META_LOOKUP[types[i].metadata] = types[i];
        }
    }

    public static EnumVariantOreSeed byMetadata(int metadata) {
        if (metadata < 0 || metadata >= META_LOOKUP.length) metadata = 0;
        return META_LOOKUP[metadata];
    }

    //

    public final int metadata;
    public final String resourceName;
    public final String unlocalizedName;
    public final EnumOreSeedShape shape;

    private EnumVariantOreSeed(int metadata, String resourceName, String unlocalizedName, EnumOreSeedShape shape) {
        this.metadata = metadata;
        this.resourceName = resourceName;
        this.unlocalizedName = unlocalizedName;
        this.shape = shape;
    }

    @Override
    public String toString() {
        return this.resourceName;
    }

    @Override
    public String getName() {
        return this.resourceName;
    }

}
