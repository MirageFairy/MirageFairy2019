package miragefairy2019.mod.magicplant

import miragefairy2019.api.Erg
import miragefairy2019.api.IFairySpec
import miragefairy2019.api.Mana
import miragefairy2019.common.toOreName
import miragefairy2019.lib.erg
import miragefairy2019.lib.mana
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.libkt.copyItemStack
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.randomInt
import miragefairy2019.mod.artifacts.FairyCrystal
import miragefairy2019.mod.artifacts.FairyMaterialCard
import miragefairy2019.mod.artifacts.createItemStack
import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.Random

val mandrakeModule = module {

}

class BlockMandrake : BlockMagicPlant(4) {

    // TODO
    override val boundingBoxList = listOf(
        AxisAlignedBB(5 / 16.0, 0 / 16.0, 5 / 16.0, 11 / 16.0, 5 / 16.0, 11 / 16.0),
        AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 12 / 16.0, 14 / 16.0),
        AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 16 / 16.0, 14 / 16.0),
        AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 16 / 16.0, 14 / 16.0),
        AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 16 / 16.0, 14 / 16.0)
    )

    // TODO
    override fun getGrowthFactorInFloor(fairySpec: IFairySpec) = fairySpec.mana(Mana.SHINE) * fairySpec.erg(Erg.CRYSTAL) / 100.0 * 3

    override fun canPick(age: Int) = age == maxAge

    override fun canGrow(age: Int) = age != maxAge && age != 0

    override fun grow(world: World, blockPos: BlockPos, blockState: IBlockState, random: Random) {
        repeat(random.randomInt(mirageFlowerGrowthHandlers.getGrowthRateModifiers(world, blockPos).growthRate)) {
            val blockState2 = world.getBlockState(blockPos)
            if (!canGrow(getAge(blockState2))) return
            world.setBlockState(blockPos, defaultState.withProperty(AGE, getAge(blockState2) + 1), 2)
        }
    }

    // TODO
    override fun getSeed() = itemMirageFlowerSeeds().createItemStack()

    // TODO
    override fun getDrops(age: Int, random: Random, fortune: Int, isBreaking: Boolean) = mutableListOf<ItemStack>().also { drops ->
        if (isBreaking) drops.drop(random, 1.0) { itemMirageFlowerSeeds().createItemStack(it) } // 破壊時、確定で種1個ドロップ
        if (isBreaking && age >= 2) drops.drop(random, 1 + fortune * 0.2) { FairyMaterialCard.MIRAGE_FLOWER_LEAF.createItemStack(it) } // 破壊時、サイズ2以上で茎
        if (age >= 3) drops.drop(random, fortune * 0.01) { itemMirageFlowerSeeds().createItemStack(it) } // 完全成長時、低確率で追加の種
        if (age >= 3) drops.drop(random, 1 + fortune * 0.5) { FairyCrystal.variantFairyCrystal().createItemStack(it) } // 完全成長時、フェアリークリスタル
        if (age >= 3) drops.drop(random, 1 + fortune * 0.5) { "dustTinyMiragium".toOreName().copyItemStack(it) } // 完全成長時、ミラジウムの微粉
    }

    // TODO
    override fun getExpDrop(age: Int, random: Random, fortune: Int, isBreaking: Boolean) = when (age) {
        3 -> if (isBreaking) 2 else 1
        2 -> if (isBreaking) 1 else 0
        else -> 0
    }

}
