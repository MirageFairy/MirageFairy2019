package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.lib.MagicSelector
import miragefairy2019.lib.doEffect
import miragefairy2019.lib.entities
import miragefairy2019.lib.position
import miragefairy2019.lib.rayTrace
import miragefairy2019.lib.sphere
import miragefairy2019.libkt.randomInt
import miragefairy2019.mod.fairyweapon.MagicMessage
import miragefairy2019.mod.fairyweapon.displayText
import miragefairy2019.mod.fairyweapon.magic4.MagicHandler
import miragefairy2019.mod.fairyweapon.magic4.boost
import miragefairy2019.mod.fairyweapon.magic4.duration2
import miragefairy2019.mod.fairyweapon.magic4.float2
import miragefairy2019.mod.fairyweapon.magic4.integer
import miragefairy2019.mod.fairyweapon.magic4.magic
import miragefairy2019.mod.fairyweapon.magic4.percent0
import miragefairy2019.mod.fairyweapon.magic4.status
import miragefairy2019.mod.fairyweapon.magic4.world
import miragefairy2019.mod.fairyweapon.spawnParticleTargets
import miragefairy2019.mod.skill.Mastery
import mirrg.kotlin.hydrogen.atMost
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.passive.EntityVillager
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.SoundCategory
import net.minecraft.world.WorldServer
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.ceil

class ItemTemptationOcarina : ItemFairyWeaponMagic4() {
    val radius = status("radius", { 2.0 + !Mana.WIND / 10.0 + !Erg.SOUND / 5.0 atMost 20.0 }, { float2 })
    val maxTargetCount = status("maxTargetCount", { 1 + (!Mana.AQUA / 20.0 + !Erg.LIFE / 10.0).toInt() }, { integer })
    val wear = status("wear", { 4.0 / (1.0 + !Mana.FIRE / 20.0 + !Erg.SPACE / 10.0) }, { percent0 })
    val levelCost = status("levelCost", { 1.0 / (1.0 + !Mana.GAIA / 20.0 + !Erg.KNOWLEDGE / 10.0) }, { float2 })
    val coolTime = status("coolTime", { 100.0 / (1.0 + !Mana.DARK / 20.0 + !Erg.LEVITATE / 10.0) }, { duration2 })
    val speedBoost = status("speedBoost", { 1.0 + !Mastery.rangedCombat / 100.0 }, { boost })

    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = listOf("Lv30以上のとき、右クリックでLv消費で村人を満腹化") // TODO translate

    override fun getMagic() = magic {
        val rayTraceMagicSelector = MagicSelector.rayTrace(world, player, 0.0, EntityLivingBase::class.java) { it != player } // 視線判定
        val cursorMagicSelector = rayTraceMagicSelector.position // 視点判定
        val rangeMagicSelector = cursorMagicSelector.sphere(radius())
        val targetsMagicSelector = rangeMagicSelector.entities(EntityVillager::class.java, { it.growingAge >= 0 && !it.getIsWillingToMate(false) }, maxTargetCount()) // 対象判定
        val targets = targetsMagicSelector.item.entities

        fun pass(color: Int, magicMessage: MagicMessage) = object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                cursorMagicSelector.item.doEffect(color)
                rangeMagicSelector.item.doEffect()
                targetsMagicSelector.item.doEffect()
            }

            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                if (!world.isRemote) player.sendStatusMessage(magicMessage.displayText, true)
                return EnumActionResult.PASS
            }
        }

        fun fail(color: Int, magicMessage: MagicMessage) = object : MagicHandler() {
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

        if (!hasPartnerFairy) return@magic fail(0xFF00FF, MagicMessage.NO_FAIRY) // パートナー妖精判定
        if (weaponItemStack.itemDamage + ceil(wear()).toInt() > weaponItemStack.maxDamage) return@magic fail(0xFF0000, MagicMessage.INSUFFICIENT_DURABILITY) // 耐久判定
        if (targets.isEmpty()) return@magic pass(0xFF8800, MagicMessage.NO_TARGET) // 対象判定
        if (player.cooldownTracker.hasCooldown(weaponItem)) return@magic pass(0xFFFF00, MagicMessage.COOL_TIME) // クールタイム判定

        // 魔法成立
        object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                cursorMagicSelector.item.doEffect(0xFFFFFF)
                rangeMagicSelector.item.doEffect()
                targetsMagicSelector.item.doEffect()
                spawnParticleTargets(world, targets, { it.positionVector.addVector(0.0, it.height.toDouble(), 0.0) }, { 0x00FF00 })
            }

            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                if (!world.isRemote) {
                    val worldServer = world as? WorldServer ?: return EnumActionResult.SUCCESS

                    var success = 0
                    var experienceCosted = false
                    run a@{
                        targets.forEach { target ->

                            // ターゲットごとの消費確認
                            if (weaponItemStack.itemDamage + ceil(wear()).toInt() > weaponItemStack.maxDamage) return@a // 耐久がないなら中断
                            if (!player.isCreative) {
                                if (player.experienceLevel < 30) return@a // レベルが足りないなら中断
                            }

                            // 効果
                            success++
                            if (!player.isCreative) {
                                val cost = worldServer.rand.randomInt(levelCost())
                                if (cost >= 1) {
                                    player.addExperienceLevel(-cost)
                                    experienceCosted = true
                                }
                            }
                            worldServer.setEntityState(target, 18.toByte())
                            target.setIsWillingToMate(true)

                            // ターゲットごとの消費
                            weaponItemStack.damageItem(worldServer.rand.randomInt(wear()), player) // 耐久値の消費

                            // ターゲットごとのエフェクト
                            worldServer.spawnParticle(
                                EnumParticleTypes.VILLAGER_HAPPY,
                                target.posX, target.posY + target.height * 0.5, target.posZ,
                                5,
                                target.width * 0.5,
                                target.height * 0.5,
                                target.width * 0.5,
                                0.02
                            )

                        }
                    }

                    if (success >= 1) {

                        // 行使ごとの消費
                        player.cooldownTracker.setCooldown(this@ItemTemptationOcarina, ceil(coolTime() / speedBoost()).toInt()) // クールタイム

                        // 行使ごとのエフェクト
                        if (experienceCosted) worldServer.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.2F, 0.5F)
                        worldServer.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1.0F, 1.0F)

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
