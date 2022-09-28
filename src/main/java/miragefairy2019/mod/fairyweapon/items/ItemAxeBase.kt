package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.mod.fairyweapon.magic4.FormulaArguments
import miragefairy2019.mod.fairyweapon.magic4.MagicArguments
import miragefairy2019.mod.fairyweapon.magic4.float2
import miragefairy2019.mod.fairyweapon.magic4.percent2
import miragefairy2019.mod.fairyweapon.magic4.status
import net.minecraft.util.math.BlockPos

class ItemAxeBase : ItemMiragiumToolBase() {
    init {
        setHarvestLevel("axe", 2) // 鉄相当
        destroySpeed = 6.0f // 鉄相当
    }

    override val maxHardness = status("maxHardness", { 2.0 }, { float2 })
    override fun focusSurface() = false
    override fun iterateTargets(magicArguments: MagicArguments, blockPosBase: BlockPos) = iterator {
        if (canBreak(magicArguments, blockPosBase)) yield(blockPosBase)
    }

    override fun getActualCoolTimePerBlock(magicArguments: MagicArguments) = 20.0
    override val actualFortune: FormulaArguments.() -> Double get() = { 0.0 }
    override val wear = status("wear", { 0.1 }, { percent2 })
}
