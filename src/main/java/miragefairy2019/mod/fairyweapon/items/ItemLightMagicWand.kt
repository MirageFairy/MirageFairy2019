package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.api.IFairyType
import miragefairy2019.lib.getSight
import miragefairy2019.mod.Main
import miragefairy2019.mod.fairyweapon.FairyWeaponUtils
import miragefairy2019.mod.fairyweapon.findFairy
import miragefairy2019.mod.fairyweapon.spawnParticle
import mirrg.kotlin.hydrogen.atMost
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.NonNullList
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.pow

// TODO magic4
class ItemLightMagicWand : ItemFairyWeapon() {

    private class Status(fairyType: IFairyType) {
        val additionalReach = fairyType.manaSet.aqua / 50.0 * 20.0 atMost 40.0
        val coolTime = fairyType.cost * 2.0 * 0.5.pow(fairyType.manaSet.gaia / 30.0)
    }

    override fun getMagicDescription(itemStack: ItemStack) = "右クリックで松明を設置" // TODO translate Right click to use magic

    @SideOnly(Side.CLIENT)
    override fun addInformationFairyWeapon(itemStackFairyWeapon: ItemStack, itemStackFairy: ItemStack, fairyType: IFairyType, world: World?, tooltip: NonNullList<String>, flag: ITooltipFlag) {
        val status = Status(fairyType)
        tooltip += "" + TextFormatting.BLUE + "Additional Reach: " + String.format("%.1f", status.additionalReach) + " (Aqua)"
        tooltip += "" + TextFormatting.BLUE + "Cool Time: " + status.coolTime.toInt() + "t (Gaia, Cost)"
    }


    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        val itemStack = player.getHeldItem(hand)
        if (world.isRemote) return ActionResult(EnumActionResult.SUCCESS, itemStack)

        // 妖精を取得
        val fairy = findFairy(itemStack, player) ?: return ActionResult(EnumActionResult.SUCCESS, itemStack)

        // ステータスを評価
        val status = Status(fairy.second)

        // 松明検索
        val itemStackTorch = findTorch(player) ?: return ActionResult(EnumActionResult.SUCCESS, itemStack)

        // 視線判定
        val rayTraceResult = FairyWeaponUtils.rayTrace(world, player, false, status.additionalReach, Entity::class.java) { true } ?: return ActionResult(EnumActionResult.SUCCESS, itemStack)
        if (rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) return ActionResult(EnumActionResult.SUCCESS, itemStack)
        var blockPos = rayTraceResult.blockPos

        // 置換不可能な場合はそのブロックの表面に対象を変更
        val blockState = world.getBlockState(blockPos)
        val block = blockState.block
        if (!block.isReplaceable(world, blockPos)) {
            blockPos = blockPos.offset(rayTraceResult.sideHit)
        }

        // 対象が変更不能なら失敗
        if (!player.canPlayerEdit(blockPos, rayTraceResult.sideHit, itemStack)) return ActionResult(EnumActionResult.SUCCESS, itemStack)

        // 対象が置換できないブロックの場合は失敗
        if (!world.mayPlace(Blocks.TORCH, blockPos, false, rayTraceResult.sideHit, player)) return ActionResult(EnumActionResult.SUCCESS, itemStack)

        val meta = 0
        val result = world.setBlockState(
            blockPos, Blocks.TORCH.getStateForPlacement(
                world,
                blockPos,
                rayTraceResult.sideHit,
                rayTraceResult.hitVec.x.toFloat(),
                rayTraceResult.hitVec.y.toFloat(),
                rayTraceResult.hitVec.z.toFloat(),
                meta,
                player,
                hand
            ), 2
        )

        // 設置失敗した場合は失敗
        if (!result) return ActionResult(EnumActionResult.SUCCESS, itemStack)
        if (world.getBlockState(blockPos).block != Blocks.TORCH) return ActionResult(EnumActionResult.SUCCESS, itemStack)

        // エフェクト
        val blockState2 = world.getBlockState(blockPos)
        val soundType = blockState2.block.getSoundType(blockState2, world, blockPos, player)
        world.playSound(player, blockPos, soundType.placeSound, SoundCategory.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F)

        //消費
        itemStack.damageItem(1, player)
        itemStackTorch.shrink(1)

        // クールタイム
        player.cooldownTracker.setCooldown(this, status.coolTime.toInt())

        return ActionResult(EnumActionResult.SUCCESS, itemStack)
    }

    private fun findTorch(player: EntityPlayer): ItemStack? {
        var itemStack: ItemStack

        itemStack = player.getHeldItem(EnumHand.OFF_HAND)
        if (itemStack.item == getItemFromBlock(Blocks.TORCH)) return itemStack

        itemStack = player.getHeldItem(EnumHand.MAIN_HAND)
        if (itemStack.item == getItemFromBlock(Blocks.TORCH)) return itemStack

        repeat(player.inventory.sizeInventory) { i ->
            itemStack = player.inventory.getStackInSlot(i)
            if (itemStack.item == getItemFromBlock(Blocks.TORCH)) return itemStack
        }

        return null
    }

    override fun onUpdate(itemStack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean) {
        if (entity !is EntityPlayer) return
        if (!isSelected && entity.heldItemOffhand != itemStack) return
        if (!Main.side.isClient) return

        // 妖精がない場合はマゼンタ
        val fairy = findFairy(itemStack, entity)
        if (fairy == null) {
            spawnParticle(world, getSight(entity, 0.0), 0xFF00FF)
            return
        }

        // 松明検索
        val itemStackTorch = findTorch(entity)
        if (itemStackTorch == null) {
            spawnParticle(world, getSight(entity, 0.0), 0xFF00FF)
            return
        }

        // ステータスを評価
        val status = Status(fairy.second)

        // 耐久がない場合は赤
        // 対象が発動対象でない場合は緑
        // クールタイムの場合は黄色
        val rayTraceResult = FairyWeaponUtils.rayTrace(world, entity, false, status.additionalReach, Entity::class.java) { true }
        if (rayTraceResult == null) {
            val color = if (itemStack.itemDamage >= itemStack.maxDamage) 0xFF0000 else if (entity.cooldownTracker.hasCooldown(this)) 0x00FF00 else 0x00FFFF
            spawnParticle(world, getSight(entity, status.additionalReach), color)
            return
        }
        if (!canExecute(world, rayTraceResult)) {
            val color = if (itemStack.itemDamage >= itemStack.maxDamage) 0xFF0000 else if (entity.cooldownTracker.hasCooldown(this)) 0x00FF00 else 0x00FFFF
            spawnParticle(world, rayTraceResult.hitVec, color)
            return
        }

        val color = if (itemStack.itemDamage >= itemStack.maxDamage) 0xFF0000 else if (entity.cooldownTracker.hasCooldown(this)) 0xFFFF00 else 0xFFFFFF
        spawnParticle(world, rayTraceResult.hitVec, color)
    }

    private fun canExecute(world: World, rayTraceResult: RayTraceResult) = rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK
}
