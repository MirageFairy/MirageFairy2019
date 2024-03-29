package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.lib.MagicSelector
import miragefairy2019.lib.doEffect
import miragefairy2019.lib.position
import miragefairy2019.lib.rayTraceBlock
import miragefairy2019.libkt.randomInt
import miragefairy2019.mod.fairyweapon.MagicMessage
import miragefairy2019.mod.fairyweapon.displayText
import miragefairy2019.mod.fairyweapon.magic4.MagicHandler
import miragefairy2019.mod.fairyweapon.magic4.boost
import miragefairy2019.mod.fairyweapon.magic4.duration2
import miragefairy2019.mod.fairyweapon.magic4.integer
import miragefairy2019.mod.fairyweapon.magic4.magic
import miragefairy2019.mod.fairyweapon.magic4.percent0
import miragefairy2019.mod.fairyweapon.magic4.percent2
import miragefairy2019.mod.fairyweapon.magic4.status
import miragefairy2019.mod.fairyweapon.magic4.world
import miragefairy2019.mod.skill.Mastery
import net.minecraft.init.MobEffects
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.ceil
import kotlin.math.floor

class ItemGravityRod : ItemFairyWeaponMagic4() {
    val duration = status("duration", { 20.0 * (5.0 + (!Mana.FIRE + !Erg.SPACE) / 10.0) * costFactor }, { duration2 })
    val strength = status("strength", { floor(1.0 + (!Mana.SHINE + !Erg.LEVITATE) / 10.0).toInt() }, { integer })
    val speedUp = status("speedUp", { floor((!Mana.GAIA + !Erg.THUNDER) / 10.0).toInt() }, { integer })
    val wear = status("wear", { 1.0 / (1.0 + (!Mana.AQUA + !Erg.KNOWLEDGE) / 20.0) * costFactor }, { percent2 })
    val coolTime = status("coolTime", { 20.0 * 20.0 / (1.0 + (!Mana.DARK + !Erg.ENERGY) / 50.0) * costFactor }, { duration2 })
    val speedBoost = status("speedBoost", { 1.0 + !Mastery.magicCombat / 100.0 }, { boost })
    val coverRate = status("coverRate", { !duration / (!coolTime / !speedBoost) }, { percent0 })

    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = listOf("右クリックで浮遊") // TRANSLATE

    override fun getMagic() = magic {
        val rayTraceMagicSelector = MagicSelector.rayTraceBlock(world, player, 0.0) // 視線判定
        val cursorMagicSelector = rayTraceMagicSelector.position // 視点判定

        fun error(color: Int, magicMessage: MagicMessage) = object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                cursorMagicSelector.item.doEffect(color)
            }

            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                if (!world.isRemote) player.sendStatusMessage(magicMessage.displayText, true)
                return EnumActionResult.FAIL
            }
        }

        if (!hasPartnerFairy) return@magic error(0xFF00FF, MagicMessage.NO_FAIRY) // パートナー妖精判定
        if (weaponItemStack.itemDamage + ceil(wear()).toInt() > weaponItemStack.maxDamage) return@magic error(0xFF0000, MagicMessage.INSUFFICIENT_DURABILITY) // 耐久判定
        if (player.cooldownTracker.hasCooldown(weaponItem)) return@magic error(0xFFFF00, MagicMessage.COOL_TIME) // クールタイム判定

        // 魔法成立
        object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                cursorMagicSelector.item.doEffect(0xFFFFFF)
            }

            override fun onItemRightClick(hand: EnumHand): EnumActionResult {

                // 効果
                val actualDuration = duration()
                if (!world.isRemote) {
                    val value = strength()
                    if (value > 0) player.addPotionEffect(PotionEffect(MobEffects.LEVITATION, ceil(actualDuration).toInt(), value - 1)) // 浮遊の効果
                }
                if (!world.isRemote) {
                    val value = speedUp()
                    if (value > 0) player.addPotionEffect(PotionEffect(MobEffects.SPEED, ceil(actualDuration).toInt(), value - 1)) // 加速の効果
                }

                // 消費
                if (!world.isRemote) {
                    weaponItemStack.damageItem(world.rand.randomInt(wear()), player) // 耐久
                    player.cooldownTracker.setCooldown(this@ItemGravityRod, ceil(coolTime() / speedBoost()).toInt()) // クールタイム
                }

                // エフェクト
                if (!world.isRemote) world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 0.5f, 1.0f) // 魔法のSE
                if (world.isRemote) player.swingArm(hand) // 腕を振る

                return EnumActionResult.SUCCESS
            }
        }
    }
}
