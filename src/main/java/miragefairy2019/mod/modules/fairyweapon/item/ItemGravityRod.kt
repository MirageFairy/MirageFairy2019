package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.libkt.randomInt
import miragefairy2019.mod.common.magic.MagicSelectorRayTrace
import miragefairy2019.mod.formula4.status
import miragefairy2019.mod.magic4.MagicHandler
import miragefairy2019.mod.magic4.duration
import miragefairy2019.mod.magic4.integer
import miragefairy2019.mod.magic4.percent0
import miragefairy2019.mod.magic4.percent2
import miragefairy2019.mod.magic4.world
import miragefairy2019.mod.modules.fairyweapon.MagicMessage
import miragefairy2019.mod3.erg.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.mod3.skill.EnumMastery
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
    val duration = status("duration", { 20.0 * (5.0 + (!Mana.FIRE + !Erg.SPACE) / 10.0) * costFactor }, { duration })
    val strength = status("strength", { floor(1.0 + (!Mana.SHINE + !Erg.LEVITATE) / 10.0).toInt() }, { integer })
    val speedUp = status("speedUp", { floor((!Mana.GAIA + !Erg.THUNDER) / 10.0).toInt() }, { integer })
    val wear = status("wear", { 1.0 / (1.0 + (!Mana.AQUA + !Erg.KNOWLEDGE) / 20.0) * costFactor }, { percent2 })
    val coolTime = status("coolTime", { 20.0 * 20.0 / (1.0 + (!Mana.DARK + !Erg.ENERGY) / 50.0) * costFactor }, { duration })
    val speedBoost = status("speedBoost", { 1.0 + 0.01 * !EnumMastery.magicCombat }, { percent0 })

    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = "右クリックで浮遊" // TODO translate

    override fun getMagic() = miragefairy2019.mod.magic4.magic {
        val magicSelectorRayTrace = MagicSelectorRayTrace.createIgnoreEntity(world, player, 0.0) // 視線判定
        val magicSelectorPosition = magicSelectorRayTrace.magicSelectorPosition // 視点判定

        fun error(color: Int, magicMessage: MagicMessage) = object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                magicSelectorPosition.doEffect(color)
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
                magicSelectorPosition.doEffect(0xFFFFFF)
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
