package miragefairy2019.mod.lib.multi;

public class ItemVariantMaterial extends ItemVariant {

    public final String registryName;
    public final String unlocalizedName;

    public ItemVariantMaterial(String registryName, String unlocalizedName) {
        this.registryName = registryName;
        this.unlocalizedName = unlocalizedName;
    }

    public String getUnlocalizedName() {
        return "item." + unlocalizedName;
    }

}
