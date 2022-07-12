package miragefairy2019.mod.fairyweapon.items

import com.google.common.collect.Multimap
import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.libkt.WeightedItem
import miragefairy2019.libkt.drop
import miragefairy2019.libkt.getRandomItem
import miragefairy2019.libkt.randomInt
import miragefairy2019.libkt.red
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.fairy.createItemStack
import miragefairy2019.mod.fairyrelation.FairySelector
import miragefairy2019.mod.fairyrelation.primaries
import miragefairy2019.mod.fairyrelation.withoutPartiallyMatch
import miragefairy2019.mod.fairyweapon.MagicMessage
import miragefairy2019.mod.fairyweapon.displayText
import miragefairy2019.mod.fairyweapon.findItem
import miragefairy2019.mod.fairyweapon.magic4.MagicHandler
import miragefairy2019.mod.fairyweapon.magic4.duration
import miragefairy2019.mod.fairyweapon.magic4.float2
import miragefairy2019.mod.fairyweapon.magic4.magic
import miragefairy2019.mod.fairyweapon.magic4.percent0
import miragefairy2019.mod.fairyweapon.magic4.percent1
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
import net.minecraftforge.oredict.OreIngredient
import kotlin.math.ceil

class ItemCrystalSword(
    private val weaponDamage: Double,
    private val additionalDamageBoost: Double,
    private val additionalDropRate: Double
) : ItemFairyWeaponMagic4() {
    val additionalDamage = status("additionalDamage", { (2.0 + !Mana.DARK / 20.0 + !Erg.ATTACK / 10.0) * costFactor }, { float2 })
    val damageBoost = status("damageBoost", { 1.0 + !Mastery.closeCombat / 100.0 + additionalDamageBoost }, { percent0 })
    val extraItemDropRate = status("extraItemDropRate", { (!Mastery.closeCombat / 100.0 + additionalDropRate).coerceIn(0.0, 1.0) }, { percent1 })
    val coolTime = status("coolTime", { 20.0 / (1.0 + !Mana.GAIA / 40.0 + !Erg.SLASH / 10.0) * costFactor }, { duration })
    val wear = status("wear", { 0.5 / (1.0 + (!Mana.WIND + !Erg.DESTROY) / 20.0) * costFactor }, { percent2 })
    val dps = status("dps", { (!additionalDamage * !damageBoost) / (!coolTime / 20.0) }, { float2 })

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

            fun fail(magicMessage: MagicMessage) {
                if (!world.isRemote) player.sendStatusMessage(magicMessage.displayText, true)
            }

            fun onHit(target: EntityLivingBase) {
                if (!hasPartnerFairy) return fail(MagicMessage.NO_FAIRY) // 妖精を持っていない
                if (weaponItemStack.itemDamage + ceil(wear()).toInt() > weaponItemStack.maxDamage) return fail(MagicMessage.INSUFFICIENT_DURABILITY) // 耐久不足
                if (player.cooldownTracker.hasCooldown(weaponItem)) return fail(MagicMessage.COOL_TIME) // クールタイムが終わっていない

                // 消費
                player.cooldownTracker.setCooldown(weaponItem, coolTime().toInt()) // クールタイム
                weaponItemStack.damageItem(world.rand.randomInt(wear()), player) // 耐久

                // 効果
                target.hurtResistantTime = 0 // 物理部分と競合するのを防ぐために無敵時間をリセットする
                target.attackEntityFrom(DamageSource.causePlayerDamage(player), (additionalDamage() * damageBoost()).toFloat()) // 追加ダメージ

            }

            fun onKill(target: EntityLivingBase) {
                if (weaponItemStack.itemDamage + 1 > weaponItemStack.maxDamage) return fail(MagicMessage.INSUFFICIENT_DURABILITY)// 耐久値が足りないと失敗

                // クリスタルがないと失敗
                val itemStackFuel = findItem(player) { OreIngredient("mirageFairyCrystal").test(it) } ?: run {
                    player.sendStatusMessage(textComponent { "フェアリークリスタルが足りません！"().red }, true) // TODO translate
                    return
                }

                // 魔法成立

                if (extraItemDropRate() > world.rand.nextDouble()) { // ステータスに基づいた確率で
                    // エンティティに紐づけられた妖精のリスト
                    val entries = FairySelector().entity(target).allMatch().withoutPartiallyMatch.primaries
                    if (entries.isEmpty()) return // 関連付けられた妖精が居ない場合は無視

                    // relevanceを重みとして抽選
                    val dropFairyCard = entries.map { WeightedItem(it.fairyCard, it.relevance) }.getRandomItem(world.rand) ?: return

                    // 効果成立

                    itemStackFuel.shrink(1) // クリスタル消費
                    if (itemStackFuel.isEmpty) player.sendStatusMessage(textComponent { "フェアリークリスタルを使い切りました！"().red }, true) // TODO translate
                    weaponItemStack.damageItem(1, player) // 耐久消費

                    dropFairyCard.createItemStack().drop(world, target.positionVector).setPickupDelay(20) // ドロップする

                    playSound(world, player, SoundEvents.BLOCK_ANVIL_PLACE, 0.5f, 1.5f) // エフェクト

                }

            }
        }
    }
}
