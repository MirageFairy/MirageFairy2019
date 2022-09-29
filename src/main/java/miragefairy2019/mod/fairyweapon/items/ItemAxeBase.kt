package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.mod.fairyweapon.magic4.FormulaArguments
import miragefairy2019.mod.fairyweapon.magic4.MagicArguments
import miragefairy2019.mod.fairyweapon.magic4.float2
import miragefairy2019.mod.fairyweapon.magic4.status
import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class ItemAxeBase : ItemMiragiumToolBase() {
    init {
        setHarvestLevel("axe", 2) // 鉄相当
        destroySpeed = 6.0f // 鉄相当
    }

    override fun focusSurface() = false

    override val maxHardness = status("maxHardness", { 2.0 }, { float2 })

    override fun iterateTargets(a: MagicArguments, blockPosBase: BlockPos) = iterator {
        if (canBreak(a, blockPosBase)) yield(blockPosBase)
    }

    override fun getDurabilityCost(a: FormulaArguments, world: World, blockPos: BlockPos, blockState: IBlockState) = 0.1
}
