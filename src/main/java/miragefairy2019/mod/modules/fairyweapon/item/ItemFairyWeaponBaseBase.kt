package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.api.IFairyCombiningHandler
import miragefairy2019.api.IFairyCombiningItem
import miragefairy2019.libkt.drop
import miragefairy2019.mod.api.fairy.IItemFairy
import net.minecraft.block.state.IBlockState
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

open class ItemFairyWeaponBaseBase : IFairyCombiningItem, Item() {
    var tier = 0

    init {
        setMaxStackSize(1)
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


    // ユーティリティの利用

    override fun isEnchantable(stack: ItemStack) = false // エンチャント不可
    override fun canApplyAtEnchantingTable(stack: ItemStack, enchantment: Enchantment) = false // すべてのエンチャントが不適正
    override fun isBookEnchantable(stack: ItemStack, book: ItemStack) = false // 本を使用したエンチャント不可
    override fun isRepairable() = false // 金床での修理不可


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
        override fun canCombine(itemStack: ItemStack) = ItemFairyWeaponBase.getCombinedFairy(itemStack).isEmpty
        override fun canCombineWith(itemStack: ItemStack, itemStackPart: ItemStack) = itemStackPart.item is IItemFairy
        override fun canUncombine(itemStack: ItemStack) = !ItemFairyWeaponBase.getCombinedFairy(itemStack).isEmpty
        override fun getCombinedPart(itemStack: ItemStack) = ItemFairyWeaponBase.getCombinedFairy(itemStack)
        override fun setCombinedPart(itemStack: ItemStack, itemStackPart: ItemStack) = ItemFairyWeaponBase.setCombinedFairy(itemStack, itemStackPart)
    }

    override fun hasContainerItem(itemStack: ItemStack) = !getContainerItem(itemStack).isEmpty
    override fun getContainerItem(itemStack: ItemStack) = ItemFairyWeaponBase.getCombinedFairy(itemStack)


    companion object {
        fun damageItem(itemStack: ItemStack, entityLivingBase: EntityLivingBase) {
            itemStack.damageItem(1, entityLivingBase) // アイテムスタックにダメージ
            // 壊れた場合、搭乗している妖精をドロップ
            if (itemStack.isEmpty) ItemFairyWeaponBase.getCombinedFairy(itemStack).drop(entityLivingBase.world, entityLivingBase.position).setNoPickupDelay()
        }
    }
}
