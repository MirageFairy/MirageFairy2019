package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.api.IFairyCombiningHandler
import miragefairy2019.api.IFairyCombiningItem
import miragefairy2019.libkt.EMPTY_ITEM_STACK
import miragefairy2019.libkt.aqua
import miragefairy2019.libkt.blue
import miragefairy2019.libkt.canTranslate
import miragefairy2019.libkt.drop
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.green
import miragefairy2019.libkt.lightPurple
import miragefairy2019.libkt.orNull
import miragefairy2019.libkt.oreIngredient
import miragefairy2019.libkt.red
import miragefairy2019.libkt.translateToLocal
import miragefairy2019.libkt.unit
import miragefairy2019.libkt.white
import miragefairy2019.libkt.yellow
import miragefairy2019.mod.api.fairy.ApiFairy
import miragefairy2019.mod.api.fairy.IItemFairy
import miragefairy2019.mod.api.fairyweapon.formula.ApiFormula
import miragefairy2019.mod.api.fairyweapon.formula.IFormula
import miragefairy2019.mod.api.fairyweapon.formula.IMagicStatus
import miragefairy2019.mod.api.fairyweapon.item.IItemFairyWeapon
import miragefairy2019.mod.lib.BakedModelBuiltinWrapper
import miragefairy2019.mod3.artifacts.getSphereType
import miragefairy2019.mod3.artifacts.oreName
import miragefairy2019.mod3.erg.api.EnumErgType
import miragefairy2019.mod3.erg.displayName
import miragefairy2019.mod3.fairy.api.IFairyType
import miragefairy2019.mod3.main.api.ApiMain
import miragefairy2019.mod3.manualrepair.api.IManualRepairableItem
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
import net.minecraft.util.text.ITextComponent
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.function.Function

open class ItemFairyWeapon : IFairyCombiningItem, Item(), IManualRepairableItem, IItemFairyWeapon {
    var tier = 0

    init {
        setMaxStackSize(1)
        if (ApiMain.side.isClient) tileEntityItemStackRenderer = TileEntityItemStackRendererFairyWeapon()
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
            if (string.isNotBlank()) tooltip += formattedText { !string }
        }

        if (canTranslate("$unlocalizedName.author")) { // 著者
            val string = translateToLocal("$unlocalizedName.author")
            if (string.isNotBlank()) tooltip += formattedText { (!"作者: $string").lightPurple } // TODO translate Author
        }

        tooltip += formattedText { (!"Tier $tier").aqua } // tier // TODO translate

        // 機能
        if (mirageFairyCombiningHandler.canCombine(itemStack)) tooltip += formattedText { (!"クラフトで妖精を搭乗可能").red } // TODO translate Can be combined with fairy by crafting
        if (mirageFairyCombiningHandler.canUncombine(itemStack)) tooltip += formattedText { (!"クラフトで妖精を分離可能").red } // TODO translate
        if (canManualRepair(itemStack)) tooltip += formattedText { (!"手入れ可能").red } // TODO translate Can be repaired by crafting with contained sphere
        getMagicDescription(itemStack)?.let { tooltip += formattedText { (!it).red } } // 魔法

        tooltip += formattedText { (!"耐久値: ${(getMaxDamage(itemStack) - getDamage(itemStack)).coerceAtLeast(0)} / ${getMaxDamage(itemStack)}").green } // 耐久値 TODO translate

        FairyWeaponUtils.getCombinedFairy(itemStack).orNull?.let { tooltip += formattedText { (!"搭乗中: ${it.displayName}").aqua } } // 搭乗中の妖精 // TODO translate

        // 手入れ
        if (canManualRepair(itemStack)) {
            tooltip += formattedText {
                val value = getManualRepairErgs().entries.map { !it.key.displayName + (if (it.value == 1) !"" else !"x${it.value}") }.concat(!", ")
                (!"手入れ用スフィア: " + value).yellow // TODO translate Manual Repair
            }
        }

        // 妖精魔法ステータス
        val fairy = Minecraft.getMinecraft().player?.let { FairyWeaponUtils.findFairy(itemStack, it).orNull?.let { t -> Pair(t.x!!, t.y!!) } } ?: Pair(EMPTY_ITEM_STACK, ApiFairy.empty()!!)
        tooltip += formattedText { (!"パートナー: " + (if (fairy.first.isEmpty) !"-" else !fairy.first.displayName).white).blue } // TODO translate
        addInformationFairyWeapon(itemStack, fairy.first, fairy.second, world, tooltip, flag)

    }

    @SideOnly(Side.CLIENT)
    open fun getMagicDescription(itemStack: ItemStack): String? = null

    @SideOnly(Side.CLIENT)
    override fun addInformationFairyWeapon(itemStackFairyWeapon: ItemStack, itemStackFairy: ItemStack, fairyType: IFairyType, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        addInformationMagicStatuses(itemStackFairyWeapon, itemStackFairy, fairyType, world, tooltip, flag)
    }


    // ユーティリティの利用
    override fun isEnchantable(stack: ItemStack) = false // エンチャント不可
    override fun canApplyAtEnchantingTable(stack: ItemStack, enchantment: Enchantment) = false // すべてのエンチャントが不適正
    override fun isBookEnchantable(stack: ItemStack, book: ItemStack) = false // 本を使用したエンチャント不可
    override fun isRepairable() = false // 金床での修理不可


    // 旧魔法ステータス

    private val magicStatuses = mutableListOf<IMagicStatus<*>>()

    fun <T> registerMagicStatus(name: String, formatter: Function<T, ITextComponent>, formula: IFormula<T>): IMagicStatus<T> {
        return ApiFormula.createMagicStatus(name, formatter, formula).also { magicStatuses += it }
    }

    @SideOnly(Side.CLIENT)
    fun addInformationMagicStatuses(itemStackFairyWeapon: ItemStack, itemStackFairy: ItemStack, fairyType: IFairyType, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        magicStatuses.forEach { tooltip += formattedText { !(it.getDisplayString(fairyType)).blue } }
    }


    // 挙動

    // エンティティを殴ると1/8の確率で削れる
    override fun hitEntity(stack: ItemStack, target: EntityLivingBase, attacker: EntityLivingBase): Boolean {
        if (itemRand.nextDouble() < 1.0 / 8.0) damageItem(stack, attacker)
        return true
    }

    // 固さのあるブロックを壊すと1/8の確率で削れる
    override fun onBlockDestroyed(stack: ItemStack, worldIn: World, state: IBlockState, pos: BlockPos, entityLiving: EntityLivingBase): Boolean {
        if (worldIn.isRemote || state.getBlockHardness(worldIn, pos).toDouble() == 0.0) return true
        if (itemRand.nextDouble() < 1.0 / 8.0) damageItem(stack, entityLiving)
        return true
    }


    // 搭乗

    override fun getMirageFairyCombiningHandler() = FairyCombiningHandler()
    open class FairyCombiningHandler : IFairyCombiningHandler {
        override fun canCombine(itemStack: ItemStack): Boolean = FairyWeaponUtils.getCombinedFairy(itemStack).isEmpty
        override fun canCombineWith(itemStack: ItemStack, itemStackPart: ItemStack) = itemStackPart.item is IItemFairy
        override fun canUncombine(itemStack: ItemStack): Boolean = !FairyWeaponUtils.getCombinedFairy(itemStack).isEmpty
        override fun getCombinedPart(itemStack: ItemStack): ItemStack = FairyWeaponUtils.getCombinedFairy(itemStack)
        override fun setCombinedPart(itemStack: ItemStack, itemStackPart: ItemStack): Unit = FairyWeaponUtils.setCombinedFairy(itemStack, itemStackPart)
    }

    override fun hasContainerItem(itemStack: ItemStack) = !getContainerItem(itemStack).isEmpty
    override fun getContainerItem(itemStack: ItemStack): ItemStack = FairyWeaponUtils.getCombinedFairy(itemStack)


    // 手入れ

    private val manualRepairErgs = mutableMapOf<EnumErgType, Int>()
    fun getManualRepairErgs() = manualRepairErgs
    fun addManualRepairErg(ergType: EnumErgType) = addManualRepairErg(ergType, 1)
    fun addManualRepairErg(ergType: EnumErgType, amount: Int) = unit { manualRepairErgs.compute(ergType) { _, amountNow -> (amountNow ?: 0) + amount } }
    override fun canManualRepair(itemStack: ItemStack) = true
    override fun getManualRepairSubstitute(itemStack: ItemStack): NonNullList<Ingredient> = manualRepairErgs.entries
        .filter { it.value > 0 }
        .sortedBy { it.key }
        .flatMap { (key, value) -> (0 until value).map { getSphereType(key).oreName.oreIngredient } }
        .toCollection(NonNullList.create())

    override fun getManualRepairedItem(itemStack: ItemStack): ItemStack = itemStack.copy().also { it.itemDamage = 0 }


    companion object {
        fun damageItem(itemStack: ItemStack, entityLivingBase: EntityLivingBase) {
            itemStack.damageItem(1, entityLivingBase) // アイテムスタックにダメージ
            // 壊れた場合、搭乗している妖精をドロップ
            if (itemStack.isEmpty) FairyWeaponUtils.getCombinedFairy(itemStack).drop(entityLivingBase.world, entityLivingBase.position).setNoPickupDelay()
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
        val itemStackFairy = FairyWeaponUtils.getCombinedFairy(itemStack).orNull
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
