package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.api.ICombineAcceptorItem
import miragefairy2019.api.ICombineHandler
import miragefairy2019.api.ICombineResult
import miragefairy2019.api.IFairyItem
import miragefairy2019.api.IFairySpec
import miragefairy2019.api.IFairyWeaponItem
import miragefairy2019.api.IManualRepairAcceptorItem
import miragefairy2019.lib.EMPTY_FAIRY
import miragefairy2019.lib.toNonNullList
import miragefairy2019.libkt.BakedModelBuiltinWrapper
import miragefairy2019.libkt.EMPTY_ITEM_STACK
import miragefairy2019.libkt.aqua
import miragefairy2019.libkt.blue
import miragefairy2019.libkt.canTranslate
import miragefairy2019.libkt.drop
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.green
import miragefairy2019.libkt.lightPurple
import miragefairy2019.libkt.notEmptyOrNull
import miragefairy2019.libkt.plus
import miragefairy2019.libkt.red
import miragefairy2019.libkt.translateToLocal
import miragefairy2019.libkt.white
import miragefairy2019.mod.Main
import miragefairy2019.mod.fairyweapon.findFairy
import miragefairy2019.mod.fairyweapon.getCombinedFairy
import miragefairy2019.mod.fairyweapon.setCombinedFairy
import mirrg.kotlin.hydrogen.atLeast
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

open class ItemFairyWeapon : ICombineAcceptorItem, Item(), IManualRepairAcceptorItem, IFairyWeaponItem {
    var tier = 0

    init {
        setMaxStackSize(1)
        if (Main.side.isClient) tileEntityItemStackRenderer = TileEntityItemStackRendererFairyWeapon()
    }


    // グラフィック

    @SideOnly(Side.CLIENT)
    override fun isFull3D() = true


    // 採掘道具

    open var destroySpeed = 1.0f

    open fun isEffective(itemStack: ItemStack, blockState: IBlockState) = getToolClasses(itemStack).any {
        when {
            !blockState.block.isToolEffective(it, blockState) -> false
            getHarvestLevel(itemStack, it, null, blockState) < blockState.block.getHarvestLevel(blockState) -> false
            else -> true
        }
    }

    override fun getDestroySpeed(itemStack: ItemStack, blockState: IBlockState) = if (isEffective(itemStack, blockState)) destroySpeed else 1.0f
    override fun canHarvestBlock(blockState: IBlockState, itemStack: ItemStack) = isEffective(itemStack, blockState)


    // ツールチップ

    @SideOnly(Side.CLIENT)
    final override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {

        if (canTranslate("$unlocalizedName.poem")) { // ポエム
            val string = translateToLocal("$unlocalizedName.poem")
            if (string.isNotBlank()) tooltip += formattedText { string() }
        }

        if (canTranslate("$unlocalizedName.author")) { // 著者
            val string = translateToLocal("$unlocalizedName.author")
            if (string.isNotBlank()) tooltip += formattedText { "作者: $string"().lightPurple } // TRANSLATE Author
        }

        tooltip += formattedText { "Tier $tier"().aqua } // tier // TRANSLATE

        // 機能
        tooltip += formattedText { "クラフトで妖精を搭乗・分離可能"().red } // TRANSLATE Can be combined with fairy by crafting
        if (canManualRepair(itemStack)) tooltip += formattedText { "手入れ可能"().red } // TRANSLATE Can be repaired by crafting with contained sphere
        getMagicDescription(itemStack).forEach {
            tooltip += formattedText { it().red } // 魔法
        }

        tooltip += formattedText { "耐久値: ${(getMaxDamage(itemStack) - getDamage(itemStack)) atLeast 0} / ${getMaxDamage(itemStack)}"().green } // 耐久値 TODO translate

        getCombinedFairy(itemStack).notEmptyOrNull?.let { tooltip += formattedText { "搭乗中: ${it.displayName}"().aqua } } // 搭乗中の妖精 // TRANSLATE

        // 妖精魔法ステータス
        val fairy = Minecraft.getMinecraft().player?.let { findFairy(itemStack, it) } ?: Pair(EMPTY_ITEM_STACK, EMPTY_FAIRY)
        tooltip += formattedText { ("パートナー: "() + (if (fairy.first.isEmpty) "-"() else fairy.first.displayName()).white).blue } // TRANSLATE
        tooltip += NonNullList.create<String>().also { addInformationFairyWeapon(itemStack, fairy.first, fairy.second, world, it, flag) }

    }

    @SideOnly(Side.CLIENT)
    open fun getMagicDescription(itemStack: ItemStack) = listOf<String>()

    @SideOnly(Side.CLIENT)
    override fun addInformationFairyWeapon(itemStackFairyWeapon: ItemStack, itemStackFairy: ItemStack, fairySpec: IFairySpec, world: World?, tooltip: NonNullList<String>, flag: ITooltipFlag) {

    }


    // ユーティリティの利用
    override fun isEnchantable(stack: ItemStack) = false // エンチャント不可
    override fun canApplyAtEnchantingTable(stack: ItemStack, enchantment: Enchantment) = false // すべてのエンチャントが不適正
    override fun isBookEnchantable(stack: ItemStack, book: ItemStack) = false // 本を使用したエンチャント不可
    override fun isRepairable() = false // 金床での修理不可


    // 挙動

    // エンティティを殴ると1/8の確率で削れる
    override fun hitEntity(itemStack: ItemStack, target: EntityLivingBase, attacker: EntityLivingBase): Boolean {
        if (wearWhenHitEntity(itemStack)) {
            if (itemRand.nextDouble() < 1.0 / 8.0) {
                damageItem(itemStack, attacker)
            }
        }
        return true
    }

    // 固さのあるブロックを壊すと1/8の確率で削れる
    override fun onBlockDestroyed(stack: ItemStack, worldIn: World, state: IBlockState, pos: BlockPos, entityLiving: EntityLivingBase): Boolean {
        if (worldIn.isRemote || state.getBlockHardness(worldIn, pos).toDouble() == 0.0) return true
        if (wearWhenDestroyBlock(stack)) {
            if (itemRand.nextDouble() < 1.0 / 8.0) {
                damageItem(stack, entityLiving)
            }
        }
        return true
    }

    open fun wearWhenHitEntity(itemStack: ItemStack) = true
    open fun wearWhenDestroyBlock(itemStack: ItemStack) = true

    // 搭乗

    override fun getCombineHandler(itemStack: ItemStack) = ICombineHandler { otherItemStack ->
        val fairyItemStack = getCombinedFairy(itemStack)
        if (otherItemStack.isEmpty && fairyItemStack.isEmpty) return@ICombineHandler null // 分解モードで搭乗中の妖精が無い場合は失敗
        if (!otherItemStack.isEmpty && otherItemStack.item !is IFairyItem) return@ICombineHandler null // 合成モードで合成対象が妖精でない場合は失敗
        object : ICombineResult {
            override fun getCombinedItem() = itemStack.copy().also { setCombinedFairy(it, otherItemStack) } // 妖精もしくは無を搭乗させて返す
            override fun getRemainingItem() = fairyItemStack // 搭乗していた妖精
        }
    }

    override fun hasContainerItem(itemStack: ItemStack) = !getContainerItem(itemStack).isEmpty
    override fun getContainerItem(itemStack: ItemStack) = getCombinedFairy(itemStack)


    // 手入れ
    val manualRepairRequirements = mutableListOf<Ingredient>()
    override fun canManualRepair(itemStack: ItemStack) = true
    override fun getManualRepairRequirements(itemStack: ItemStack) = manualRepairRequirements.toNonNullList()
    override fun getManualRepairedItem(itemStack: ItemStack): ItemStack = itemStack.copy().also { it.itemDamage = 0 }


    companion object {
        fun damageItem(itemStack: ItemStack, entityLivingBase: EntityLivingBase) {
            itemStack.damageItem(1, entityLivingBase) // アイテムスタックにダメージ
            // 壊れた場合、搭乗している妖精をドロップ
            if (itemStack.isEmpty) getCombinedFairy(itemStack).drop(entityLivingBase.world, entityLivingBase.position).setNoPickupDelay()
        }
    }
}

@SideOnly(Side.CLIENT)
class TileEntityItemStackRendererFairyWeapon : TileEntityItemStackRenderer() {
    override fun renderByItem(itemStack: ItemStack, partialTicks: Float) {
        GlStateManager.disableRescaleNormal()

        // 本体描画
        run {
            val bakedModel = Minecraft.getMinecraft().renderItem.getItemModelWithOverrides(itemStack, null, null)
            if (bakedModel is BakedModelBuiltinWrapper) {
                GlStateManager.pushMatrix()
                try {
                    GlStateManager.translate(0.5f, 0.5f, 0.5f)
                    Minecraft.getMinecraft().renderItem.renderItem(itemStack, bakedModel.bakedModel)
                } finally {
                    GlStateManager.popMatrix()
                }
            }
        }

        GlStateManager.disableRescaleNormal()

        // 搭乗妖精描画
        val itemStackFairy = getCombinedFairy(itemStack).notEmptyOrNull
        if (itemStackFairy != null) {
            val bakedModel = Minecraft.getMinecraft().renderItem.getItemModelWithOverrides(itemStackFairy, null, null)
            GlStateManager.pushMatrix()
            try {
                GlStateManager.translate(0.75f, 0.25f, 0.51f)
                GlStateManager.scale(0.5f, 0.5f, 1.0f)
                Minecraft.getMinecraft().renderItem.renderItem(itemStackFairy, bakedModel)
            } finally {
                GlStateManager.popMatrix()
            }
        }
    }
}
