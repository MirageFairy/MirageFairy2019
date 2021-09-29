package miragefairy2019.mod3.skill

import miragefairy2019.mod.ModMirageFairy2019
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World
import java.util.function.Supplier

lateinit var itemSkillBook: Supplier<ItemSkillBook>
lateinit var itemAstronomicalObservationBook: Supplier<ItemAstronomicalObservationBook>

class ItemSkillBook : Item() {
    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        if (!world.isRemote) player.openGui(ModMirageFairy2019.instance, guiIdSkill, player.world, player.position.x, player.position.y, player.position.z)
        return ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand))
    }
}

class ItemAstronomicalObservationBook : Item() {
    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        if (!world.isRemote) {
            // TODO
        }
        return ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand))
    }
}
