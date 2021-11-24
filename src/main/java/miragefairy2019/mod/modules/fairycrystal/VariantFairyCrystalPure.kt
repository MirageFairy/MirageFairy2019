package miragefairy2019.mod.modules.fairycrystal

class VariantFairyCrystalPure(registryName: String, unlocalizedName: String, oreName: String) : VariantFairyCrystal(registryName, unlocalizedName, oreName) {
    override fun getDropRank() = 1
    override fun getRareBoost() = 2.0
}
