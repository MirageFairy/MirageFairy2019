package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.mod.formula4.status
import miragefairy2019.mod.magic4.MagicHandler
import miragefairy2019.mod.magic4.pitch
import miragefairy2019.mod.magic4.world
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.pow

class ItemBellBase : ItemFairyWeaponMagic4() {
    val pitch = status("pitch", { 0.5.pow(costFactor - 1.0) }, { pitch })

    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = "右クリックで鳴らす" // TODO translate

    override fun getMagic() = miragefairy2019.mod.magic4.magic {
        object : MagicHandler() {
            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0f, pitch().toFloat())
                return EnumActionResult.SUCCESS
            }
        }
    }
}
