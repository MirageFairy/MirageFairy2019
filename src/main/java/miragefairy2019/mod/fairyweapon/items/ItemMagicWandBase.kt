package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.lib.MagicSelector
import miragefairy2019.lib.doEffect
import miragefairy2019.lib.position
import miragefairy2019.lib.rayTrace
import miragefairy2019.mod.fairyweapon.magic4.MagicHandler
import miragefairy2019.mod.fairyweapon.magic4.float2
import miragefairy2019.mod.fairyweapon.magic4.magic
import miragefairy2019.mod.fairyweapon.magic4.status
import miragefairy2019.mod.fairyweapon.magic4.world
import miragefairy2019.mod.fairyweapon.spawnMagicParticle
import miragefairy2019.mod.fairyweapon.spawnMagicSplashParticle
import mirrg.kotlin.hydrogen.atMost
import mirrg.kotlin.hydrogen.castOrNull
import net.minecraft.entity.EntityLivingBase
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.world.WorldServer
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class ItemMagicWandBase : ItemFairyWeaponMagic4() {
    val additionalReach = status("additionalReach", { 10.0 + (!Mana.WIND + !Erg.LEVITATE) / 5.0 atMost 50.0 }, { float2 })

    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = listOf("右クリックで魔法エフェクト") // TODO translate

    override fun getMagic() = magic {
        val rayTraceMagicSelector = MagicSelector.rayTrace(world, player, additionalReach(), EntityLivingBase::class.java) { it != player } // 視線判定
        val cursorMagicSelector = rayTraceMagicSelector.position // 視点判定
        object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                cursorMagicSelector.item.doEffect(0xFFFFFF)
            }

            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                if (!world.isRemote) {
                    world.castOrNull<WorldServer>()?.let { spawnMagicParticle(it, player, cursorMagicSelector.item.position) } // 線状パーティクル
                    world.castOrNull<WorldServer>()?.let { spawnMagicSplashParticle(it, cursorMagicSelector.item.position) } // スプラッシュパーティクル
                    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1.0f, 1.0f) // 魔法のSE
                }
                if (world.isRemote) player.swingArm(hand) // 腕を振る
                return EnumActionResult.SUCCESS
            }
        }
    }
}
