package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.lib.MagicSelector
import miragefairy2019.lib.doEffect
import miragefairy2019.lib.entities
import miragefairy2019.lib.position
import miragefairy2019.lib.rayTraceBlock
import miragefairy2019.lib.sphere
import miragefairy2019.libkt.randomInt
import miragefairy2019.mod.fairyweapon.MagicMessage
import miragefairy2019.mod.fairyweapon.displayText
import miragefairy2019.mod.fairyweapon.magic4.MagicHandler
import miragefairy2019.mod.fairyweapon.magic4.duration
import miragefairy2019.mod.fairyweapon.magic4.float1
import miragefairy2019.mod.fairyweapon.magic4.integer
import miragefairy2019.mod.fairyweapon.magic4.magic
import miragefairy2019.mod.fairyweapon.magic4.status
import miragefairy2019.mod.fairyweapon.magic4.world
import mirrg.kotlin.hydrogen.atMost
import net.minecraft.entity.item.EntityItem
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.ceil
import kotlin.math.pow

class ItemMagicWandCollecting : ItemFairyWeaponBase2() {
    val additionalReach = status("additionalReach", { !Mana.WIND / 5.0 atMost 8.0 }, { float1 })
    val radius = status("radius", { 2.0 + !Mana.FIRE / 10.0 + !Erg.WARP / 10.0 atMost 7.0 }, { float1 })
    val maxTargetCount = status("maxTargetCount", { (1.0 + !Mana.GAIA / 2.0 + !Erg.SPACE / 2.0 atMost 20.0).toInt() }, { integer })
    val wear = status("wear", { 0.25 * 0.5.pow(!Mana.AQUA / 30.0) }, { float1 })
    val coolTime = status("coolTime", { cost * 3.0 * 0.5.pow(!Mana.DARK / 40.0) }, { duration })

    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = "右クリックでアイテムを回収" // TODO translate

    override fun getMagic() = magic {
        val rayTraceMagicSelector = MagicSelector.rayTraceBlock(world, player, additionalReach()) // 視線判定
        val cursorMagicSelector = rayTraceMagicSelector.position // 視点判定
        val rangeMagicSelector = cursorMagicSelector.sphere(radius())
        val targetsMagicSelector = rangeMagicSelector.entities(EntityItem::class.java, { true }, maxTargetCount())
        val targets = targetsMagicSelector.item.entities

        fun error(color: Int, magicMessage: MagicMessage) = object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                cursorMagicSelector.item.doEffect(color)
                rangeMagicSelector.item.doEffect()
                targetsMagicSelector.item.doEffect()
            }

            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                if (!world.isRemote) player.sendStatusMessage(magicMessage.displayText, true)
                return EnumActionResult.FAIL
            }
        }

        if (!hasPartnerFairy) return@magic error(0xFF00FF, MagicMessage.NO_FAIRY) // パートナー妖精判定
        if (weaponItemStack.itemDamage + ceil(wear()).toInt() > weaponItemStack.maxDamage) return@magic error(0xFF0000, MagicMessage.INSUFFICIENT_DURABILITY) // 耐久判定
        if (targets.isEmpty()) return@magic error(0xFF8800, MagicMessage.NO_TARGET) // 対象判定
        if (player.cooldownTracker.hasCooldown(weaponItem)) return@magic error(0xFFFF00, MagicMessage.COOL_TIME) // クールタイム判定

        // 魔法成立
        object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                cursorMagicSelector.item.doEffect(0xFFFFFF)
                rangeMagicSelector.item.doEffect()
                targetsMagicSelector.item.doEffect()
            }

            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                if (!world.isRemote) {

                    var successed = 0
                    run a@{
                        targets.forEach { target ->

                            // ターゲットごとの消費確認
                            if (weaponItemStack.itemDamage + ceil(wear()).toInt() > weaponItemStack.maxDamage) return@a // 耐久がないなら中断

                            // ターゲットごとの効果
                            successed++
                            target.setPosition(player.posX, player.posY, player.posZ)
                            target.setNoPickupDelay()

                            // ターゲットごとの消費
                            weaponItemStack.damageItem(world.rand.randomInt(wear()), player) // 耐久値の消費

                        }
                    }

                    if (successed > 0) {
                        val rate = successed.toDouble() / maxTargetCount().toDouble()

                        // 行使ごとの消費
                        player.cooldownTracker.setCooldown(this@ItemMagicWandCollecting, ceil(coolTime() * rate).toInt()) // クールタイム

                        // 行使ごとのエフェクト
                        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F) // 音

                    }

                } else {

                    // エフェクト
                    player.swingArm(hand) // 腕を振る

                }
                return EnumActionResult.SUCCESS
            }
        }
    }
}
