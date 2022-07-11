package miragefairy2019.mod.fairyweapon.items

import com.google.common.collect.Multimap
import miragefairy2019.api.Mana
import miragefairy2019.libkt.WeightedItem
import miragefairy2019.libkt.drop
import miragefairy2019.libkt.getRandomItem
import miragefairy2019.libkt.red
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.fairy.createItemStack
import miragefairy2019.mod.fairyrelation.FairySelector
import miragefairy2019.mod.fairyrelation.primaries
import miragefairy2019.mod.fairyrelation.withoutPartiallyMatch
import miragefairy2019.mod.fairyweapon.findItem
import miragefairy2019.mod.fairyweapon.magic4.MagicHandler
import miragefairy2019.mod.fairyweapon.magic4.magic
import miragefairy2019.mod.fairyweapon.magic4.percent1
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
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.OreIngredient

class ItemCrystalSword(
    private val additionalDamage: Double,
    private val boost: Double
) : ItemFairyWeaponBase3(Mana.GAIA, Mastery.closeCombat) {
    val extraItemDropRate = status("extraItemDropRate", { (!mastery / 100.0 + +boost).coerceIn(0.0, 1.0) }, { percent1 })

    override fun getAttributeModifiers(equipmentSlot: EntityEquipmentSlot, itemStack: ItemStack): Multimap<String?, AttributeModifier?>? {
        val multimap = super.getAttributeModifiers(equipmentSlot, itemStack)
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.name, AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", additionalDamage, 0))
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.name, AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4, 0))
        }
        return multimap
    }

    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = lsitOf("撃破時、フェアリークリスタルを消費して妖精を召喚") // TODO translate

    override fun getMagic() = magic {
        object : MagicHandler() {
            override fun hitEntity(target: EntityLivingBase) {
                if (target.health <= 0) onKill(target)
            }

            fun onKill(target: EntityLivingBase) {

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
                    dropFairyCard.createItemStack().drop(world, target.positionVector).setPickupDelay(20) // ドロップする
                    playSound(world, player, SoundEvents.BLOCK_ANVIL_PLACE, 0.5f, 1.5f) // エフェクト

                }

            }
        }
    }
}
