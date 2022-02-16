package miragefairy2019.mod.modules.fairyweapon.item

import com.google.common.collect.Multimap
import miragefairy2019.libkt.WeightedItem
import miragefairy2019.libkt.drop
import miragefairy2019.libkt.getRandomItem
import miragefairy2019.libkt.red
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod3.fairy.relation.FairySelector
import miragefairy2019.mod3.fairy.relation.primaries
import miragefairy2019.mod3.fairy.relation.withoutPartiallyMatch
import miragefairy2019.mod3.magic.api.IMagicHandler
import miragefairy2019.mod3.magic.positive
import miragefairy2019.mod3.mana.api.EnumManaType
import miragefairy2019.mod3.skill.EnumMastery
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.init.SoundEvents
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.OreIngredient

class ItemCrystalSword : ItemFairyWeaponBase3(EnumManaType.GAIA, EnumMastery.closeCombat) {
    val extraItemDropRate = "extraItemDropRate"({ percent1.positive }) { (getSkillLevel(mastery) / 100.0).coerceIn(0.0, 1.0) }.setVisibility(Companion.EnumVisibility.ALWAYS)

    // 攻撃力は7（ダイヤ剣+1）固定
    override fun getAttributeModifiers(equipmentSlot: EntityEquipmentSlot, itemStack: ItemStack): Multimap<String?, AttributeModifier?>? {
        val multimap = super.getAttributeModifiers(equipmentSlot, itemStack)
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.name, AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 7.0, 0))
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.name, AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4, 0))
        }
        return multimap
    }

    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = "撃破時、フェアリークリスタルを消費して妖精を召喚" // TODO translate

    override val magic = magic {
        object : IMagicHandler {
            override fun hitEntity(target: EntityLivingBase) {
                if (target.health > 0) return // 撃破時のみ有効

                // クリスタルがないと失敗
                val itemStackFuel = findItem(player, OreIngredient("mirageFairyCrystal")) ?: run {
                    player.sendStatusMessage(textComponent { (!"フェアリークリスタルが足りません！").red }, true) // TODO translate
                    return
                }

                // 魔法成立

                if (!extraItemDropRate > world.rand.nextDouble()) { // ステータスに基づいた確率で
                    // エンティティに紐づけられた妖精のリスト
                    val entries = FairySelector().entity(target).allMatch().withoutPartiallyMatch.primaries
                    if (entries.isEmpty()) return // 関連付けられた妖精が居ない場合は無視

                    // relevanceを重みとして抽選
                    val dropFairy = entries.map { WeightedItem(it.fairy, it.relevance) }.getRandomItem(world.rand) ?: return

                    // 効果成立

                    itemStackFuel.shrink(1) // クリスタル消費
                    if (itemStackFuel.isEmpty) player.sendStatusMessage(textComponent { (!"フェアリークリスタルを使い切りました！").red }, true) // TODO translate
                    dropFairy.main.createItemStack().drop(world, target.positionVector).setPickupDelay(20) // ドロップする
                    playSound(world, player, SoundEvents.BLOCK_ANVIL_PLACE, 0.5f, 1.5f) // エフェクト

                }

            }
        }
    }
}
