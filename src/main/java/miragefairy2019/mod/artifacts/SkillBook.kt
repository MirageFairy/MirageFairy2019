package miragefairy2019.mod.artifacts

import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.resourcemaker.generated
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.libkt.enJa
import miragefairy2019.mod.GuiId
import miragefairy2019.mod.Main
import miragefairy2019.mod.ModMirageFairy2019
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
    val skillBookModule = module {
        itemSkillBook = item({ ItemSkillBook() }, "skill_book") {
            setUnlocalizedName("skillBook")
            setCreativeTab { Main.creativeTab }
            setCustomModelResourceLocation()
        }
        onMakeLang {
            enJa("item.skillBook.name", "Skill Book", "スキルブック")
        }
        makeItemModel("skill_book") { generated }
    }
}

class ItemSkillBook : Item() {
    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        if (!world.isRemote) player.openGui(ModMirageFairy2019.instance, GuiId.skillGui, player.world, player.position.x, player.position.y, player.position.z)
        return ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand))
    }

    @SideOnly(Side.CLIENT)
    override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        // TODO 現在スキルレベルとか表示
    }
}
