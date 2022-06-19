package miragefairy2019.mod.artifacts

import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.item
import miragefairy2019.libkt.module
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.mod.Main
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.resourcemaker.DataOreIngredient
import miragefairy2019.resourcemaker.DataResult
import miragefairy2019.resourcemaker.DataShapelessRecipe
import miragefairy2019.resourcemaker.generated
import miragefairy2019.resourcemaker.makeItemModel
import miragefairy2019.resourcemaker.makeRecipe
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

object Fertilizer {
    lateinit var itemFertilizer: () -> ItemFertilizer
    val module = module {
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
        }
        makeItemModel("fertilizer") { generated }
        makeRecipe(
            ResourceName(ModMirageFairy2019.MODID, "fertilizer"),
            DataShapelessRecipe(
                ingredients = listOf(
                    DataOreIngredient(ore = "dustApatite"),
                    DataOreIngredient(ore = "dustMiragium")
                ),
                result = DataResult(
                    item = "miragefairy2019:fertilizer",
                    count = 4
                )
            )
        )
    }
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
