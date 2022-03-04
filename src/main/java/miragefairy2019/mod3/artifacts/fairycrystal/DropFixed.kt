package miragefairy2019.mod3.artifacts.fairycrystal

import miragefairy2019.mod3.fairy.RankedFairyTypeBundle

class DropFixed(
    val bundle: RankedFairyTypeBundle,
    override val dropCategory: DropCategory,
    override val weight: Double
) : IDrop {
    override fun getItemStack(rank: Int) = bundle[rank].createItemStack()
}
