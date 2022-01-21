package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.Module
import miragefairy2019.libkt.addOreName
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.item
import miragefairy2019.libkt.red
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.mod.api.ore.ApiOre
import miragefairy2019.mod.lib.UtilsMinecraft
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionConsumeItem
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionNotNether
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionReplaceBlock
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionSpawnBlock
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionUseItem
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftRecipe
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftRegistry
import miragefairy2019.mod3.fairystickcraft.api.ApiFairyStickCraft
import miragefairy2019.mod3.main.api.ApiMain
import net.minecraft.block.BlockDynamicLiquid
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.OreIngredient

lateinit var itemFairyStick: () -> ItemFairyStick

object FairyStick {
    val module: Module = {

        onInstantiation {
            ApiFairyStickCraft.fairyStickCraftRegistry = FairyStickCraftRegistry()
        }

        // 妖精のステッキ
        itemFairyStick = item({ ItemFairyStick() }, "fairy_stick") {
            setUnlocalizedName("fairyStick")
            setCreativeTab { ApiMain.creativeTab }
            setCustomModelResourceLocation()
            addOreName("mirageFairyStick")
        }

        // レシピ登録
        onAddRecipe {

            // 水精→水源
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
                it.conditions += FairyStickCraftConditionUseItem(OreIngredient("mirageFairyStick"))
                it.conditions += FairyStickCraftConditionNotNether()
                it.conditions += FairyStickCraftConditionSpawnBlock { Blocks.WATER.defaultState }
                it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("mirageFairy2019FairyWaterRank1"))
            })

            // 溶岩精→溶岩流
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
                it.conditions += FairyStickCraftConditionUseItem(OreIngredient("mirageFairyStick"))
                it.conditions += FairyStickCraftConditionSpawnBlock { Blocks.FLOWING_LAVA.defaultState.withProperty(BlockDynamicLiquid.LEVEL, 15) }
                it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("mirageFairy2019FairyLavaRank1"))
            })

            // 蜘蛛精→糸ブロック
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
                it.conditions += FairyStickCraftConditionUseItem(OreIngredient("mirageFairyStick"))
                it.conditions += FairyStickCraftConditionSpawnBlock { Blocks.WEB.defaultState }
                it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("mirageFairy2019FairySpiderRank1"))
            })

            // 水＋ミラジウムの粉→妖水
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
                it.conditions += FairyStickCraftConditionUseItem(OreIngredient("mirageFairyStick"))
                it.conditions += FairyStickCraftConditionReplaceBlock({ Blocks.WATER.defaultState }, { ApiOre.blockFluidMiragiumWater.defaultState })
                it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("dustMiragium"))
            })

        }

    }
}

class ItemFairyStick : Item() {
    @SideOnly(Side.CLIENT)
    override fun isFull3D() = true


    @SideOnly(Side.CLIENT)
    override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        // ポエム
        if (UtilsMinecraft.canTranslate("$unlocalizedName.poem")) {
            val string = UtilsMinecraft.translateToLocal("$unlocalizedName.poem")
            if (string.isNotEmpty()) tooltip += string
        }

        // 機能
        tooltip += formattedText { (!"Right Click: World Craft").red } // TODO translate
    }


    override fun onItemUse(player: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        // レシピ判定
        val executor = ApiFairyStickCraft.fairyStickCraftRegistry.getExecutor(player, worldIn, pos) { player.getHeldItem(hand) }
            ?: ApiFairyStickCraft.fairyStickCraftRegistry.getExecutor(player, worldIn, pos.offset(facing)) { player.getHeldItem(hand) }
            ?: return EnumActionResult.PASS

        // 成立
        executor.onCraft { itemStackFairyStick ->
            player.setHeldItem(hand, itemStackFairyStick)
        }
        return EnumActionResult.SUCCESS
    }

    override fun onUpdate(itemStack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean) {
        if (!world.isRemote) return // クライアントワールドのみ
        if (world.rand.nextDouble() >= 0.1) return // 使用tick判定
        if (entity !is EntityPlayer) return // プレイヤー取得
        if (!isSelected && entity.heldItemOffhand != itemStack) return // アイテム取得

        // プレイヤー視線判定
        val rayTraceResult = rayTrace(world, entity, false) ?: return // ブロックに当たらなかった場合は無視
        if (rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) return  // ブロックに当たらなかった場合は無視

        // レシピ判定
        val executor = ApiFairyStickCraft.fairyStickCraftRegistry.getExecutor(entity, world, rayTraceResult.blockPos) { itemStack }
            ?: ApiFairyStickCraft.fairyStickCraftRegistry.getExecutor(entity, world, rayTraceResult.blockPos.offset(rayTraceResult.sideHit)) { itemStack }
            ?: return

        // 成立
        executor.onUpdate()
    }
}