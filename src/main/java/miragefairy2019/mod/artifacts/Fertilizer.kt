package miragefairy2019.mod.artifacts

import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapelessRecipe
import miragefairy2019.lib.resourcemaker.generated
import miragefairy2019.lib.resourcemaker.lang
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.mod.Main
import net.minecraft.block.BlockDispenser
import net.minecraft.dispenser.IBlockSource
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Bootstrap.BehaviorDispenseOptional
import net.minecraft.item.Item
import net.minecraft.item.ItemDye
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

lateinit var itemFertilizer: () -> ItemFertilizer

val fertilizerModule = module {
    itemFertilizer = item({ ItemFertilizer() }, "fertilizer") {
        setUnlocalizedName("fertilizer")
        setCreativeTab { Main.creativeTab }
        setCustomModelResourceLocation()
        onInit {
            BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(item, object : BehaviorDispenseOptional() {
                override fun dispenseStack(blockSource: IBlockSource, itemStack: ItemStack): ItemStack {
                    val world = blockSource.world
                    val blockPos = blockSource.blockPos.offset(blockSource.blockState.getValue(BlockDispenser.FACING))

                    // 実行
                    val result = ItemDye.applyBonemeal(itemStack, world, blockPos)
                    successful = result
                    if (!result) return itemStack

                    // エフェクト
                    if (!world.isRemote) world.playEvent(2005, blockPos, 0)

                    return itemStack
                }
            })
        }
        makeItemModel { generated }
        makeRecipe {
            DataShapelessRecipe(
                ingredients = listOf(
                    DataOreIngredient(ore = "dustApatite"),
                    DataOreIngredient(ore = "dustMiragium")
                ),
                result = DataResult(item = "miragefairy2019:fertilizer", count = 4)
            )
        }
    }
    lang("item.fertilizer.name", "Fertilizer", "肥料")
}

class ItemFertilizer : Item() {
    override fun onItemUse(player: EntityPlayer, world: World, blockPos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        val itemStack = player.getHeldItem(hand)

        if (!player.canPlayerEdit(blockPos.offset(facing), facing, itemStack)) return EnumActionResult.FAIL

        if (ItemDye.applyBonemeal(itemStack, world, blockPos, player, hand)) {
            if (!world.isRemote) world.playEvent(2005, blockPos, 0)
            return EnumActionResult.SUCCESS
        }

        return EnumActionResult.PASS
    }
}
