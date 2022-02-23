package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.aqua
import miragefairy2019.libkt.canTranslate
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.green
import miragefairy2019.libkt.item
import miragefairy2019.libkt.module
import miragefairy2019.libkt.red
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.translateToLocal
import miragefairy2019.mod.modules.fairyweapon.item.FairyWeaponUtils
import miragefairy2019.mod3.artifacts.fairycrystal.ItemFairyCrystal
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionReplaceBlock
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionUseItem
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftRecipe
import miragefairy2019.mod3.fairystickcraft.api.ApiFairyStickCraft
import miragefairy2019.mod3.main.api.ApiMain
import miragefairy2019.mod3.skill.api.ApiSkill
import mirrg.kotlin.toUpperCamelCase
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.EnumAction
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.stats.StatList
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.oredict.OreIngredient

object Wand {
    val module = module {

        fun <T : ItemFairyWand> fairyWand(tier: Int, type: String, number: Int, creator: () -> T, vararg additionalOreNames: String) {
            item(creator, "${type}_fairy_wand${if (number == 1) "" else "_$number"}") {
                setUnlocalizedName("fairyWand${type.toUpperCamelCase()}${if (number == 1) "" else "$number"}")
                setCreativeTab { ApiMain.creativeTab }
                setCustomModelResourceLocation()
                onInit {
                    val durability = (1..tier).fold(16) { a, b -> a * 2 }
                    item.maxDamage = durability - 1
                    item.tier = tier
                }
                onCreateItemStack {
                    OreDictionary.registerOre("mirageFairy2019CraftingToolFairyWand${type.toUpperCamelCase()}", item.createItemStack(metadata = OreDictionary.WILDCARD_VALUE))
                    additionalOreNames.forEach { OreDictionary.registerOre(it, item.createItemStack(metadata = OreDictionary.WILDCARD_VALUE)) }
                }
            }
        }
        fairyWand(1, "crafting", 1, { ItemFairyWand() })
        fairyWand(2, "crafting", 2, { ItemFairyWand() })
        fairyWand(3, "crafting", 3, { ItemFairyWand() })
        fairyWand(4, "crafting", 4, { ItemFairyWand() })
        fairyWand(1, "hydrating", 1, { ItemFairyWand() }, "container1000Water")
        fairyWand(2, "hydrating", 2, { ItemFairyWand() }, "container1000Water")
        fairyWand(3, "hydrating", 3, { ItemFairyWand() }, "container1000Water")
        fairyWand(4, "hydrating", 4, { ItemFairyWand() }, "container1000Water")
        fairyWand(2, "melting", 1, { ItemFairyWand() })
        fairyWand(3, "melting", 2, { ItemFairyWand() })
        fairyWand(4, "melting", 3, { ItemFairyWand() })
        fairyWand(2, "breaking", 1, { ItemFairyWand() })
        fairyWand(3, "breaking", 2, { ItemFairyWand() })
        fairyWand(4, "breaking", 3, { ItemFairyWand() })
        fairyWand(2, "freezing", 1, { ItemFairyWand() })
        fairyWand(3, "freezing", 2, { ItemFairyWand() })
        fairyWand(4, "freezing", 3, { ItemFairyWand() })
        fairyWand(3, "polishing", 1, { ItemFairyWand() })
        fairyWand(4, "polishing", 2, { ItemFairyWand() })
        fairyWand(3, "summoning", 1, { ItemFairyWandSummoning(2) })
        fairyWand(4, "summoning", 2, { ItemFairyWandSummoning(5) })
        fairyWand(4, "distortion", 1, { ItemFairyWand() })
        fairyWand(4, "fusion", 1, { ItemFairyWand() })

        onAddRecipe {

            // 丸石＞紅蓮→焼き石
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
                it.conditions += FairyStickCraftConditionUseItem(OreIngredient("mirageFairy2019CraftingToolFairyWandMelting"))
                it.conditions += FairyStickCraftConditionReplaceBlock({ Blocks.COBBLESTONE.defaultState }, { Blocks.STONE.defaultState })
            })

        }

    }
}

open class ItemFairyWand : Item() {
    var tier = 0

    init {
        setMaxStackSize(1)
    }


    // グラフィック
    @SideOnly(Side.CLIENT)
    override fun isFull3D() = true


    // ツールチップ
    @SideOnly(Side.CLIENT)
    override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {

        if (canTranslate("$unlocalizedName.poem")) { // ポエム
            val string = translateToLocal("$unlocalizedName.poem")
            if (string.isNotBlank()) tooltip += formattedText { !string }
        }

        tooltip += formattedText { (!"Tier $tier").aqua } // tier // TODO translate

        // 機能
        getMagicDescription(itemStack)?.let { tooltip += formattedText { (!it).red } } // 魔法

        tooltip += formattedText { (!"耐久値: ${(getMaxDamage(itemStack) - getDamage(itemStack)).coerceAtLeast(0)} / ${getMaxDamage(itemStack)}").green } // 耐久値 TODO translate

    }


    // ユーティリティの利用
    override fun isEnchantable(stack: ItemStack) = false // エンチャント不可
    override fun canApplyAtEnchantingTable(stack: ItemStack, enchantment: Enchantment) = false // すべてのエンチャントが不適正
    override fun isBookEnchantable(stack: ItemStack, book: ItemStack) = false // 本を使用したエンチャント不可
    override fun isRepairable() = false // 金床での修理不可


    // フェアリーステッキクラフト関係

    @SideOnly(Side.CLIENT)
    open fun getMagicDescription(itemStack: ItemStack): String? = "右クリックでフェアリーステッキクラフト" // TODO translate

    override fun onItemUse(player: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): EnumActionResult {

        // フェアリーステッキクラフトのレシピを判定
        val executor = null
            ?: ApiFairyStickCraft.fairyStickCraftRegistry.getExecutor(player, worldIn, pos) { player.getHeldItem(hand) }
            ?: ApiFairyStickCraft.fairyStickCraftRegistry.getExecutor(player, worldIn, pos.offset(facing)) { player.getHeldItem(hand) }
            ?: return EnumActionResult.PASS // マッチするレシピが無かった場合は抜ける

        // クラフトを実行
        executor.onCraft { player.setHeldItem(hand, it) }

        return EnumActionResult.SUCCESS
    }

    override fun onUpdate(itemStack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean) {
        if (!world.isRemote) return // クライアント側のみ描画処理を行う
        if (!(world.rand.nextDouble() < 0.1)) return // 10Tickに1回の確率で描画を行う
        if (entity !is EntityPlayer) return // 主体はプレイヤーでなければならない
        if (!isSelected && entity.heldItemOffhand != itemStack) return // アイテムが異常な状態なら中止

        // プレイヤー視線判定
        val rayTraceResult: RayTraceResult? = rayTrace(world, entity, false)
        if (rayTraceResult == null || rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) return  // 視線がブロックに当たらなかった場合は無視

        // レシピ判定
        val executor = null
            ?: ApiFairyStickCraft.fairyStickCraftRegistry.getExecutor(entity, world, rayTraceResult.blockPos) { itemStack }
            ?: ApiFairyStickCraft.fairyStickCraftRegistry.getExecutor(entity, world, rayTraceResult.blockPos.offset(rayTraceResult.sideHit)) { itemStack }
            ?: return // マッチするレシピが無かった場合は抜ける

        // 描画を実行
        executor.onUpdate()

    }


    // クラフティングツール関係
    override fun getContainerItem(itemStack: ItemStack): ItemStack {
        if (itemStack.itemDamage >= itemStack.maxDamage) return super.getContainerItem(itemStack) // 耐久を使い果たした場合はデフォルトの挙動
        return itemStack.copy().also { it.itemDamage = it.itemDamage + 1 }
    }

}

class ItemFairyWandSummoning(val maxTryCountPerTick: Int) : ItemFairyWand() {

    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = "右クリック長押しでフェアリークリスタルを高速消費" // TODO translate Hold right mouse button to use fairy crystals quickly

    //

    // TODO フェアリーステッキクラフト無効
    //override fun onItemUse(player: EntityPlayer, worldIn: World, pos: BlockPos, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float) = EnumActionResult.PASS
    //override fun onUpdate(itemStack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean) = Unit

    override fun getItemUseAction(stack: ItemStack) = EnumAction.BOW
    override fun getMaxItemUseDuration(stack: ItemStack) = 72000 // 永続

    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        player.activeHand = hand
        return ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand))
    }

    override fun onUsingTick(stack: ItemStack, entityLivingBase: EntityLivingBase, count: Int) {
        if (entityLivingBase.world.isRemote) return

        if (entityLivingBase is EntityPlayer) {
            repeat(getTryCount(count)) a@{
                if (!tryUseCrystal(entityLivingBase)) return@a
            }
        }
    }

    private fun tryUseCrystal(player: EntityPlayer): Boolean {

        // 妖晶を得る
        val itemStackFairyCrystal = FairyWeaponUtils.findItemOptional(player) { itemStack -> itemStack!!.item is ItemFairyCrystal }.orElse(null) ?: return false // クリスタルを持ってない場合は無視
        val variantFairyCrystal = (itemStackFairyCrystal.item as ItemFairyCrystal).getVariant(itemStackFairyCrystal) ?: return false // 異常なクリスタルを持っている場合は無視

        // プレイヤー視点判定
        val rayTraceResult = rayTrace(player.world, player, false) ?: return false // ブロックに当たらなかった場合は無視
        if (rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) return false // ブロックに当たらなかった場合は無視

        // ガチャを引く
        val itemStackDrop = variantFairyCrystal.dropper.drop(
            player,
            player.world,
            rayTraceResult.blockPos,
            if (player.getHeldItem(EnumHand.MAIN_HAND).item === this) EnumHand.MAIN_HAND else EnumHand.OFF_HAND,
            rayTraceResult.sideHit,
            rayTraceResult.hitVec.x.toFloat(),
            rayTraceResult.hitVec.y.toFloat(),
            rayTraceResult.hitVec.z.toFloat(),
            variantFairyCrystal.dropRank,
            variantFairyCrystal.getRareBoost(ApiSkill.skillManager.getServerSkillContainer(player))
        ) ?: return false // ガチャが引けなかった場合は無視
        if (itemStackDrop.isEmpty) return false // ガチャが引けなかった場合は無視

        // 成立

        // ガチャアイテムを消費
        if (!player.isCreative) itemStackFairyCrystal.shrink(1)
        player.addStat(StatList.getObjectUseStats(itemStackFairyCrystal.item))

        // 妖精をドロップ
        val blockPos = rayTraceResult.blockPos.offset(rayTraceResult.sideHit)
        val entityItem = EntityItem(player.world, blockPos.x + 0.5, blockPos.y + 0.5, blockPos.z + 0.5, itemStackDrop.copy())
        entityItem.setNoPickupDelay()
        player.world.spawnEntity(entityItem)

        return true
    }

    private fun getTryCount(count: Int): Int {
        val t = 72000 - count
        return when {
            t >= 200 -> 5
            t >= 100 -> 2
            t >= 60 -> 1
            t >= 20 -> if (t % 2 == 0) 1 else 0
            t >= 5 -> if (t % 5 == 0) 1 else 0
            t == 1 -> 1
            else -> 0
        }.coerceAtMost(maxTryCountPerTick)
    }
}
