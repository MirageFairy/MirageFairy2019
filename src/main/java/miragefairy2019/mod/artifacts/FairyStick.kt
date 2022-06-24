package miragefairy2019.mod.artifacts

import miragefairy2019.lib.modinitializer.addOreName
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.proxy
import miragefairy2019.lib.resourcemaker.handheld
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.skillContainer
import miragefairy2019.libkt.blue
import miragefairy2019.libkt.canTranslate
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.gold
import miragefairy2019.libkt.plus
import miragefairy2019.libkt.red
import miragefairy2019.libkt.translateToLocal
import miragefairy2019.mod.Main
import miragefairy2019.mod.fairystickcraft.ApiFairyStickCraft
import miragefairy2019.mod.fairystickcraft.FairyStickCraftConditionConsumeItem
import miragefairy2019.mod.fairystickcraft.FairyStickCraftConditionNotNether
import miragefairy2019.mod.fairystickcraft.FairyStickCraftConditionReplaceBlock
import miragefairy2019.mod.fairystickcraft.FairyStickCraftConditionSpawnBlock
import miragefairy2019.mod.fairystickcraft.FairyStickCraftConditionUseItem
import miragefairy2019.mod.fairystickcraft.FairyStickCraftRecipe
import miragefairy2019.mod.fairystickcraft.FairyStickCraftRegistry
import miragefairy2019.mod.skill.ApiSkill
import miragefairy2019.mod.skill.Mastery
import miragefairy2019.mod.skill.displayName
import miragefairy2019.mod.skill.getSkillLevel
import miragefairy2019.mod.systems.IFairyStickCraftItem
import miragefairy2019.mod.systems.addFairyStickCraftCoolTime
import mirrg.kotlin.formatAs
import net.minecraft.block.BlockDynamicLiquid
import net.minecraft.client.Minecraft
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
import kotlin.math.ceil

lateinit var itemFairyStick: () -> ItemFairyStick

val fairyStickModule = module {

    onInstantiation {
        ApiFairyStickCraft.fairyStickCraftRegistry = FairyStickCraftRegistry()
    }

    // 妖精のステッキ
    itemFairyStick = item({ ItemFairyStick() }, "fairy_stick") {
        setUnlocalizedName("fairyStick")
        setCreativeTab { Main.creativeTab }
        setCustomModelResourceLocation()
        addOreName("mirageFairyStick")
    }
    makeItemModel("fairy_stick") { handheld }

    // レシピ登録
    onAddRecipe {

        // 水精→水源
        ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
            it.conditions += FairyStickCraftConditionUseItem(WandType.CRAFTING.ingredient)
            it.conditions += FairyStickCraftConditionNotNether()
            it.conditions += FairyStickCraftConditionSpawnBlock { Blocks.WATER.defaultState }
            it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("mirageFairy2019FairyWaterRank1"))
        })

        // 溶岩精→溶岩流
        ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
            it.conditions += FairyStickCraftConditionUseItem(WandType.CRAFTING.ingredient)
            it.conditions += FairyStickCraftConditionSpawnBlock { Blocks.FLOWING_LAVA.defaultState.withProperty(BlockDynamicLiquid.LEVEL, 15) }
            it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("mirageFairy2019FairyLavaRank1"))
        })

        // 蜘蛛精→糸ブロック
        ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
            it.conditions += FairyStickCraftConditionUseItem(WandType.CRAFTING.ingredient)
            it.conditions += FairyStickCraftConditionSpawnBlock { Blocks.WEB.defaultState }
            it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("mirageFairy2019FairySpiderRank1"))
        })

        // 水＋ミラジウムの粉→妖水
        ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
            it.conditions += FairyStickCraftConditionUseItem(OreIngredient("mirageFairyStick"))
            it.conditions += FairyStickCraftConditionReplaceBlock({ Blocks.WATER.defaultState }, { FluidMaterials.blockFluidMiragiumWater().defaultState })
            it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("dustMiragium"))
        })

    }

}

class ItemFairyStick : Item(), IFairyStickCraftItem {
    @SideOnly(Side.CLIENT)
    override fun isFull3D() = true


    @SideOnly(Side.CLIENT)
    override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        val player = Minecraft.getMinecraft().player ?: return

        // ポエム
        if (canTranslate("$unlocalizedName.poem")) {
            val string = translateToLocal("$unlocalizedName.poem")
            if (string.isNotEmpty()) tooltip += string
        }

        // 機能
        tooltip += formattedText { "右クリックでフェアリーステッキクラフト"().red } // TODO translate

        tooltip += formattedText { ("スキル: "() + Mastery.processing.displayName() + " (${ApiSkill.skillManager.getClientSkillContainer().getSkillLevel(Mastery.processing)})"()).gold } // TODO translate
        tooltip += formattedText { "クールタイム: ${getCoolTime(player) / 20.0 formatAs "%.2f"} 秒"().blue } // TODO translate

    }


    fun getCoolTime(player: EntityPlayer) = ceil(40.0 / (1.0 + 0.01 * player.proxy.skillContainer.getSkillLevel(Mastery.processing))).toInt()

    override fun onItemUse(player: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {
        // レシピ判定
        val executor = ApiFairyStickCraft.fairyStickCraftRegistry.getExecutor(player, worldIn, pos) { player.getHeldItem(hand) }
            ?: ApiFairyStickCraft.fairyStickCraftRegistry.getExecutor(player, worldIn, pos.offset(facing)) { player.getHeldItem(hand) }
            ?: return EnumActionResult.PASS

        // 成立
        executor.onCraft { player.setHeldItem(hand, it) }
        addFairyStickCraftCoolTime(player, getCoolTime(player))

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

    override val isFairyStickCraftItem get() = true
}
