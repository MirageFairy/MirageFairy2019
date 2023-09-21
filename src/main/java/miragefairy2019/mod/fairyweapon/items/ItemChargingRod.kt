package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.api.Erg.THUNDER
import miragefairy2019.api.Mana.WIND
import miragefairy2019.mod.fairyweapon.magic4.MagicHandler
import miragefairy2019.mod.fairyweapon.magic4.boost
import miragefairy2019.mod.fairyweapon.magic4.float2
import miragefairy2019.mod.fairyweapon.magic4.magic
import miragefairy2019.mod.fairyweapon.magic4.status
import miragefairy2019.mod.fairyweapon.magic4.suffix
import miragefairy2019.mod.skill.Mastery
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.floor

class ItemChargingRod : ItemFairyWeaponMagic4() {
    val chargeSpeed = status("chargeSpeed", { 20.0 + !WIND / 5.0 + !THUNDER / 2.0 }, { float2.suffix(" AE/t") })
    val speedBoost = status("speedBoost", { 1.0 + !Mastery.processing / 100.0 }, { boost })

    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = listOf("AE2エネルギーセルの上に乗るとチャージ") // TRANSLATE

    override fun getMagic() = magic {
        object : MagicHandler() {
            override fun onServerUpdate(itemSlot: Int, isSelected: Boolean) {
                if (!hasAe2) return

                // 二刀流ガード
                if (player.heldItemOffhand == weaponItemStack) { // オフハンドで持っているとき
                    if (player.heldItemMainhand.item is ItemChargingRod) { // メインハンドもチャージングロッドだった場合
                        return // 不活性化
                    }
                }

                // 対象ブロック
                val blockPos = BlockPos(floor(player.posX).toInt(), floor(player.posY).toInt() - 1, floor(player.posZ).toInt())

                // 注入
                //val tileEntity = world.getTileEntity(blockPos) as? IAEPowerStorage ?: return
                //tileEntity.injectAEPower(chargeSpeed() * speedBoost(), Actionable.MODULATE)

            }
        }
    }

}

private val hasAe2 by lazy {
    false
    //val result = try {
    //    IAEPowerStorage::class.java.toString()
    //    true
    //} catch (_: NoClassDefFoundError) {
    //    false
    //}
    //Main.logger.info("Appeng2 Power Storage Charger state: $result")
    //result
}
