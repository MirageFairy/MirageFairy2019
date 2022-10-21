package miragefairy2019.mod.artifacts

import miragefairy2019.api.IFairyStickCraftItem
import miragefairy2019.lib.addFairyStickCraftCoolTime
import miragefairy2019.lib.modinitializer.addOreName
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.proxy
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.handheld
import miragefairy2019.lib.resourcemaker.lang
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.blue
import miragefairy2019.libkt.canTranslate
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.gold
import miragefairy2019.libkt.plus
import miragefairy2019.libkt.red
import miragefairy2019.libkt.translateToLocal
import miragefairy2019.mod.Main
import miragefairy2019.mod.fairystickcraft.ApiFairyStickCraft
import miragefairy2019.mod.skill.ApiSkill
import miragefairy2019.mod.skill.Mastery
import miragefairy2019.mod.skill.displayName
import miragefairy2019.mod.skill.getSkillLevel
import miragefairy2019.mod.skill.skillContainer
import mirrg.kotlin.hydrogen.formatAs
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
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
import kotlin.math.ceil

lateinit var itemFairyStick: () -> ItemFairyStick

val fairyStickModule = module {

    // アイテム
    itemFairyStick = item({ ItemFairyStick() }, "fairy_stick") {
        setUnlocalizedName("fairyStick")
        setCreativeTab { Main.creativeTab }
        setCustomModelResourceLocation()
        addOreName("mirageFairyStick")
        makeItemModel { handheld }
        makeRecipe {
            DataShapedRecipe(
                pattern = listOf(
                    "  c",
                    " s ",
                    "s  "
                ),
                key = mapOf(
                    "s" to DataOreIngredient(ore = "stickWood"),
                    "c" to DataOreIngredient(ore = "mirageFairyCrystal")
                ),
                result = DataResult(item = "miragefairy2019:fairy_stick")
            )
        }
    }

    // 翻訳生成
    lang("item.fairyStick.name", "Fairy Stick", "妖精のステッキ")
    lang("item.fairyStick.poem", "", "頼みごとをしてみよう")

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
        tooltip += formattedText { "右クリックでフェアリーステッキクラフト"().red } // TRANSLATE

        tooltip += formattedText { ("スキル: "() + Mastery.processing.displayName() + " (${ApiSkill.skillManager.getClientSkillContainer().getSkillLevel(Mastery.processing)})"()).gold } // TRANSLATE
        tooltip += formattedText { "クールタイム: ${getCoolTime(player) / 20.0 formatAs "%.2f"} 秒"().blue } // TRANSLATE

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

    override fun isFairyStickCraftItem() = true
}
