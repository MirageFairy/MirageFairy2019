package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.item
import miragefairy2019.libkt.module
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod3.main.Main
import miragefairy2019.mod3.skill.SkillGui
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

object SkillBook {
    lateinit var itemSkillBook: () -> ItemSkillBook
    val module = module {
        itemSkillBook = item({ ItemSkillBook() }, "skill_book") {
            setUnlocalizedName("skillBook")
            setCreativeTab { Main.creativeTab }
            setCustomModelResourceLocation()
        }
    }
}

class ItemSkillBook : Item() {
    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        if (!world.isRemote) player.openGui(ModMirageFairy2019.instance, SkillGui.guiIdSkillGui, player.world, player.position.x, player.position.y, player.position.z)
        return ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand))
    }

    @SideOnly(Side.CLIENT)
    override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        // TODO 現在スキルレベルとか表示
    }
}
