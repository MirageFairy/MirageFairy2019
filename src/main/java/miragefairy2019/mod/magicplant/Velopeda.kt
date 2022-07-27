package miragefairy2019.mod.magicplant

import miragefairy2019.api.Erg
import miragefairy2019.api.IFairySpec
import miragefairy2019.api.Mana
import miragefairy2019.lib.erg
import miragefairy2019.lib.mana
import miragefairy2019.lib.modinitializer.block
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.obtain
import miragefairy2019.lib.resourcemaker.DataBlockState
import miragefairy2019.lib.resourcemaker.DataBlockStates
import miragefairy2019.lib.resourcemaker.DataModel
import miragefairy2019.lib.resourcemaker.generated
import miragefairy2019.lib.resourcemaker.makeBlockModel
import miragefairy2019.lib.resourcemaker.makeBlockStates
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.libkt.containerItem
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.oreIngredient
import miragefairy2019.libkt.randomInt
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.Main
import miragefairy2019.mod.artifacts.FairyMaterialCard
import miragefairy2019.mod.artifacts.createItemStack
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.Random

lateinit var blockVelopeda: () -> BlockVelopeda
lateinit var itemVelopedaSeeds: () -> ItemMagicPlantSeed

val velopedaModule = module {

    // ブロック登録
    blockVelopeda = block({ BlockVelopeda() }, "velopeda") {
        setUnlocalizedName("velopeda")
        makeBlockStates {
            DataBlockStates(variants = (0..5).associate { age -> "age=$age" to DataBlockState("miragefairy2019:velopeda_age$age") })
        }
    }

    // ブロックモデル生成
    run {
        fun makeBlockModel(name: String) = makeBlockModel(name) {
            DataModel(
                parent = "block/cross",
                textures = mapOf(
                    "particle" to "miragefairy2019:blocks/$name",
                    "cross" to "miragefairy2019:blocks/$name"
                )
            )
        }
        makeBlockModel("velopeda_age0")
        makeBlockModel("velopeda_age1")
        makeBlockModel("velopeda_age2")
        makeBlockModel("velopeda_age3")
        makeBlockModel("velopeda_age4")
        makeBlockModel("velopeda_age5")
    }

    // 種アイテム登録
    itemVelopedaSeeds = item({ ItemMagicPlantSeed(blockVelopeda()) }, "velopeda_seeds") {
        setUnlocalizedName("velopedaSeeds")
        setCreativeTab { Main.creativeTab }
        setCustomModelResourceLocation()
        makeItemModel { generated }
    }

    // 翻訳生成
    onMakeLang { enJa("item.velopedaSeeds.name", "Velopeda Seed", "ヴェロペーダの種") }

}
    }


class BlockVelopeda : BlockMagicPlant(5) {

    // TODO
    override val boundingBoxList = listOf(
        AxisAlignedBB(1 / 16.0, 0 / 16.0, 1 / 16.0, 15 / 16.0, 1 / 16.0, 15 / 16.0),
        AxisAlignedBB(5 / 16.0, 0 / 16.0, 5 / 16.0, 11 / 16.0, 5 / 16.0, 11 / 16.0),
        AxisAlignedBB(3 / 16.0, 0 / 16.0, 3 / 16.0, 13 / 16.0, 6 / 16.0, 13 / 16.0),
        AxisAlignedBB(2 / 16.0, 0 / 16.0, 2 / 16.0, 14 / 16.0, 7 / 16.0, 14 / 16.0),
        AxisAlignedBB(0 / 16.0, 0 / 16.0, 0 / 16.0, 16 / 16.0, 12 / 16.0, 16 / 16.0),
        AxisAlignedBB(0 / 16.0, 0 / 16.0, 0 / 16.0, 16 / 16.0, 12 / 16.0, 16 / 16.0)
    )

    override fun getGrowthFactorInFloor(fairySpec: IFairySpec) = fairySpec.mana(Mana.FIRE) * fairySpec.erg(Erg.ATTACK) / 100.0 * 3

    override fun getAgeAfterPick(age: Int) = if (age == maxAge) 0 else null

    override fun canGrow(age: Int) = age != maxAge && age != 0

    override val growthHandlers = listOf(

        // 何もしなくても25回に1回の割合で成長する
        IGrowthHandler { world, blockPos ->
            listOf(GrowthRateModifier(textComponent { "Base Rate"() }, 0.04))
        }

    )

    override fun grow(world: World, blockPos: BlockPos, blockState: IBlockState, random: Random) {
        repeat(random.randomInt(growthHandlers.getGrowthRateModifiers(world, blockPos).growthRate)) {
            val blockState2 = world.getBlockState(blockPos)
            if (!canGrow(getAge(blockState2))) return
            world.setBlockState(blockPos, defaultState.withProperty(AGE, getAge(blockState2) + 1), 2)
        }
    }

    override fun onBlockActivated(world: World, blockPos: BlockPos, blockState: IBlockState, player: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (getAge(blockState) == 0) {
            val itemStack = player.getHeldItem(hand)
            if ("mirageFairyBlood".oreIngredient.test(itemStack)) { // TODO

                val containerItem = itemStack.containerItem
                if (containerItem != null) player.obtain(containerItem)

                itemStack.shrink(1)

                world.setBlockState(blockPos, defaultState.withProperty(AGE, getAge(blockState) + 1), 2)

                return true
            }
        }
        return super.onBlockActivated(world, blockPos, blockState, player, hand, facing, hitX, hitY, hitZ)
    }

    override fun getSeed() = itemVelopedaSeeds().createItemStack()

    override fun getDrops(age: Int, random: Random, fortune: Int, isBreaking: Boolean) = mutableListOf<ItemStack>().also { drops ->
        if (isBreaking) drops.drop(random, 1.0) { itemVelopedaSeeds().createItemStack(it) } // 破壊時、確定で種1個ドロップ
        if (age == maxAge) drops.drop(random, 1.0 + fortune * 0.5) { FairyMaterialCard.VELOPEDA_LEAF.createItemStack(it) } // 完全成長時、収穫物
    }

    override fun getExpDrop(age: Int, random: Random, fortune: Int, isBreaking: Boolean) = if (age == maxAge) 1 else 0

}
