package miragefairy2019.mod.modules.fairycrystal

class VariantFairyCrystalPure(registryName: String, unlocalizedName: String, oreName: String) : VariantFairyCrystal(registryName, unlocalizedName, oreName) {
    override val dropRank get() = 1
    override val itemRareBoost get() = 2.0
}
