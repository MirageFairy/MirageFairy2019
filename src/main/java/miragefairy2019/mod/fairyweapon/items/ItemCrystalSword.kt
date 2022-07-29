package miragefairy2019.mod.fairyweapon.items

import com.google.common.collect.Multimap
import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.libkt.WeightedItem
import miragefairy2019.libkt.drop
import miragefairy2019.libkt.getRandomItem
import miragefairy2019.libkt.randomInt
import miragefairy2019.mod.fairy.createItemStack
import miragefairy2019.mod.fairyrelation.FairySelector
import miragefairy2019.mod.fairyrelation.withoutPartiallyMatch
import miragefairy2019.mod.fairyweapon.MagicMessage
import miragefairy2019.mod.fairyweapon.displayText
import miragefairy2019.mod.fairyweapon.magic4.MagicHandler
import miragefairy2019.mod.fairyweapon.magic4.boost
import miragefairy2019.mod.fairyweapon.magic4.duration2
import miragefairy2019.mod.fairyweapon.magic4.float2
import miragefairy2019.mod.fairyweapon.magic4.magic
import miragefairy2019.mod.fairyweapon.magic4.percent0
import miragefairy2019.mod.fairyweapon.magic4.percent2
import miragefairy2019.mod.fairyweapon.magic4.status
import miragefairy2019.mod.fairyweapon.magic4.world
import miragefairy2019.mod.fairyweapon.playSound
import miragefairy2019.mod.skill.Mastery
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.init.SoundEvents
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.ceil

class ItemCrystalSword(
    private val weaponDamage: Double,
    private val baseDamage: Double,
    private val baseDropRate: Double
) : ItemFairyWeaponMagic4() {
    val damage = status("damage", { (baseDamage + !Mana.DARK / 20.0 + !Erg.ATTACK / 10.0) * costFactor }, { float2 })
    val damageBoost = status("damageBoost", { 1.0 + !Mastery.closeCombat / 100.0 }, { boost })
    val extraItemDropRate = status("extraItemDropRate", { baseDropRate + !Mastery.fairySummoning / 100.0 }, { percent0 })
    val coolTime = status("coolTime", { 20.0 * 2.0 / (1.0 + !Mana.GAIA / 40.0 + !Erg.SLASH / 10.0) * costFactor }, { duration2 })
    val wear = status("wear", { 0.5 / (1.0 + (!Mana.WIND + !Erg.DESTROY) / 20.0) * costFactor }, { percent2 })
    val dps = status("dps", { (!damage * !damageBoost) / (!coolTime / 20.0) }, { float2 })

    override fun getAttributeModifiers(equipmentSlot: EntityEquipmentSlot, itemStack: ItemStack): Multimap<String?, AttributeModifier?>? {
        val multimap = super.getAttributeModifiers(equipmentSlot, itemStack)
        if (itemStack.itemDamage + 1 <= itemStack.maxDamage) {
            if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
                multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.name, AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", weaponDamage, 0))
                multimap.put(SharedMonsterAttributes.ATTACK_SPEED.name, AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4, 0))
            }
        }
        return multimap
    }

    override fun wearWhenHitEntity(itemStack: ItemStack) = itemStack.itemDamage + 1 <= itemStack.maxDamage

    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = listOf(
        "攻撃時、追加ダメージが発生", // TODO translate
        "撃破時、フェアリークリスタルを消費して妖精を召喚" // TODO translate
    )

    override fun getMagic() = magic {
        object : MagicHandler() {
            override fun hitEntity(target: EntityLivingBase) {
                onHit(target)
                if (target.health <= 0) onKill(target)
            }

            fun fail(magicMessage: MagicMessage, actionBar: Boolean) {
                if (!world.isRemote) player.sendStatusMessage(magicMessage.displayText, actionBar)
            }

            fun onHit(target: EntityLivingBase) {
                if (!hasPartnerFairy) return fail(MagicMessage.NO_FAIRY, true) // 妖精を持っていない
                if (weaponItemStack.itemDamage + ceil(wear()).toInt() > weaponItemStack.maxDamage) return fail(MagicMessage.INSUFFICIENT_DURABILITY, true) // 耐久不足
                if (player.cooldownTracker.hasCooldown(weaponItem)) return fail(MagicMessage.COOL_TIME, true) // クールタイムが終わっていない

                // 消費
                player.cooldownTracker.setCooldown(weaponItem, ceil(coolTime()).toInt()) // クールタイム
                weaponItemStack.damageItem(world.rand.randomInt(wear()), player) // 耐久

                // 効果
                target.hurtResistantTime = 0 // 物理部分と競合するのを防ぐために無敵時間をリセットする
                target.attackEntityFrom(DamageSource.causePlayerDamage(player), (damage() * damageBoost()).toFloat()) // 追加ダメージ

            }

            fun onKill(target: EntityLivingBase) {
                val times = world.rand.randomInt(extraItemDropRate()) // ステータスに基づいた回数
                val table = FairySelector().entity(target).allMatch() // エンティティに紐づけられた妖精のリスト
                    .withoutPartiallyMatch // 抽象的な妖精を除外
                    .map { WeightedItem(it.fairyCard, it.weight) } // relevanceを重みとする

                repeat(times) {
                    if (weaponItemStack.itemDamage + 1 > weaponItemStack.maxDamage) return fail(MagicMessage.INSUFFICIENT_DURABILITY, false) // 耐久値が足りないと失敗
                    val dropFairyCard = table.getRandomItem(world.rand) ?: return fail(MagicMessage.INVALID_TARGET, false) // 抽選

                    // 成立

                    weaponItemStack.damageItem(1, player) // 耐久消費
                    dropFairyCard.createItemStack().drop(world, target.positionVector).setPickupDelay(20) // ドロップする
                    playSound(world, player, SoundEvents.BLOCK_ANVIL_PLACE, 0.5f, 1.5f) // エフェクト

                }
            }
        }
    }
}
