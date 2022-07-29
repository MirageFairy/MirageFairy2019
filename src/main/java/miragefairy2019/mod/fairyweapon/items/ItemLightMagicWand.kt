package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.lib.BlockRayTraceWrapper
import miragefairy2019.lib.MagicSelector
import miragefairy2019.lib.doEffect
import miragefairy2019.lib.position
import miragefairy2019.lib.rayTraceBlock
import miragefairy2019.mod.fairyweapon.MagicMessage
import miragefairy2019.mod.fairyweapon.displayText
import miragefairy2019.mod.fairyweapon.findItem
import miragefairy2019.mod.fairyweapon.magic4.MagicHandler
import miragefairy2019.mod.fairyweapon.magic4.boost
import miragefairy2019.mod.fairyweapon.magic4.duration2
import miragefairy2019.mod.fairyweapon.magic4.float2
import miragefairy2019.mod.fairyweapon.magic4.magic
import miragefairy2019.mod.fairyweapon.magic4.status
import miragefairy2019.mod.fairyweapon.magic4.world
import miragefairy2019.mod.skill.Mastery
import mirrg.kotlin.hydrogen.atMost
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.ceil

class ItemLightMagicWand : ItemFairyWeaponMagic4() {
    val additionalReach = status("additionalReach", { 5.0 + !Mana.WIND / 5.0 + !Erg.LIGHT / 2.0 atMost 40.0 }, { float2 })
    val coolTime = status("coolTime", { 100.0 / (1.0 + !Mana.GAIA / 30.0 + !Erg.FLAME / 20.0) }, { duration2 })
    val speedBoost = status("speedBoost", { 1.0 + !Mastery.magicCombat / 100.0 }, { boost })

    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = listOf("右クリックで松明を設置") // TODO translate Right click to use magic

    override fun getMagic() = magic {
        val rayTraceMagicSelector = MagicSelector.rayTraceBlock(world, player, additionalReach()) // 視線判定
        val cursorMagicSelector = rayTraceMagicSelector.position // 視点判定
        val torchItemStack = findItem(player) { it.item == getItemFromBlock(Blocks.TORCH) } // 松明検索

        fun pass(color: Int, magicMessage: MagicMessage) = object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                cursorMagicSelector.item.doEffect(color)
            }

            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                if (!world.isRemote) player.sendStatusMessage(magicMessage.displayText, true)
                return EnumActionResult.PASS
            }
        }

        fun fail(color: Int, magicMessage: MagicMessage) = object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                cursorMagicSelector.item.doEffect(color)
            }

            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                if (!world.isRemote) player.sendStatusMessage(magicMessage.displayText, true)
                return EnumActionResult.FAIL
            }
        }

        if (!hasPartnerFairy) return@magic fail(0xFF00FF, MagicMessage.NO_FAIRY) // パートナー妖精判定
        if (weaponItemStack.itemDamage + ceil(1.0).toInt() > weaponItemStack.maxDamage) return@magic fail(0xFF0000, MagicMessage.INSUFFICIENT_DURABILITY) // 耐久判定
        if (torchItemStack == null) return@magic fail(0xFF00FF, MagicMessage.INSUFFICIENT_RESOURCE) // 松明がない
        if (player.cooldownTracker.hasCooldown(weaponItem)) return@magic pass(0xFFFF00, MagicMessage.COOL_TIME) // クールタイム判定

        // 魔法成立
        object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                cursorMagicSelector.item.doEffect(0xFFFFFF)
            }

            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                if (world.isRemote) return EnumActionResult.SUCCESS

                // 視線判定
                if (rayTraceMagicSelector.item.rayTraceWrapper !is BlockRayTraceWrapper) return EnumActionResult.SUCCESS
                var blockPos = rayTraceMagicSelector.item.rayTraceWrapper.blockPos
                val side = rayTraceMagicSelector.item.rayTraceWrapper.side
                val position = rayTraceMagicSelector.item.rayTraceWrapper.targetPosition

                // 置換不可能な場合はそのブロックの表面に対象を変更
                val blockState = world.getBlockState(blockPos)
                val block = blockState.block
                if (!block.isReplaceable(world, blockPos)) {
                    blockPos = blockPos.offset(side)
                }

                // 対象が変更不能なら失敗
                if (!player.canPlayerEdit(blockPos, side, weaponItemStack)) return EnumActionResult.SUCCESS

                // 対象が置換できないブロックの場合は失敗
                if (!world.mayPlace(Blocks.TORCH, blockPos, false, side, player)) return EnumActionResult.SUCCESS

                // 設置
                val meta = 0
                val placementBlockState = Blocks.TORCH.getStateForPlacement(world, blockPos, side, position.x.toFloat(), position.y.toFloat(), position.z.toFloat(), meta, player, hand)
                val result = world.setBlockState(blockPos, placementBlockState, 2)

                // 設置失敗した場合は失敗
                if (!result) return EnumActionResult.SUCCESS
                if (world.getBlockState(blockPos).block != Blocks.TORCH) return EnumActionResult.SUCCESS

                //消費
                weaponItemStack.damageItem(1, player)
                torchItemStack.shrink(1)
                player.cooldownTracker.setCooldown(this@ItemLightMagicWand, ceil(coolTime() / speedBoost()).toInt())

                // エフェクト
                val newBlockState = world.getBlockState(blockPos)
                val soundType = newBlockState.block.getSoundType(newBlockState, world, blockPos, player)
                world.playSound(player, blockPos, soundType.placeSound, SoundCategory.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F)

                return EnumActionResult.SUCCESS
            }
        }
    }
}
