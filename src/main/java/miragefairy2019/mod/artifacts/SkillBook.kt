package miragefairy2019.mod.artifacts

import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.proxy
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapelessRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.generated
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.TextComponentScope
import miragefairy2019.libkt.TextComponentWrapper
import miragefairy2019.libkt.blue
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.green
import miragefairy2019.libkt.plus
import miragefairy2019.libkt.red
import miragefairy2019.libkt.white
import miragefairy2019.mod.GuiId
import miragefairy2019.mod.Main
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.skill.canResetMastery
import miragefairy2019.mod.skill.getRequiredFairyMasterExpForNextLevel
import miragefairy2019.mod.skill.remainingSkillPoints
import miragefairy2019.mod.skill.skillContainer
import net.minecraft.client.Minecraft
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
import java.time.Instant

lateinit var itemSkillBook: () -> ItemSkillBook

val skillBookModule = module {
    itemSkillBook = item({ ItemSkillBook() }, "skill_book") {
        setUnlocalizedName("skillBook")
        setCreativeTab { Main.creativeTab }
        setCustomModelResourceLocation()
        makeItemModel { generated }
        makeRecipe {
            DataShapelessRecipe(
                ingredients = listOf(
                    DataSimpleIngredient(item = "minecraft:writable_book"),
                    DataOreIngredient(ore = "mirageFairyCrystal"),
                    DataOreIngredient(ore = "dustRedstone"),
                    DataOreIngredient(ore = "ingotGold")
                ),
                result = DataResult(item = "miragefairy2019:skill_book")
            )
        }
    }
    onMakeLang {
        enJa("item.skillBook.name", "Skill Book", "スキルブック")
        enJa("skill_book.fairyMasterLevel", "Fairy Master Level", "フェアリーマスターレベル")
        enJa("skill_book.requiredFairyMasterXp", "Required Fairy Master XP", "必要経験値")
        enJa("skill_book.sp", "SP", "SP")
        enJa("skill_book.masteryLevelReset", "Mastery Level Reset", "マスタリレベル初期化")
        enJa("skill_book.possible", "Possible", "可能")
        enJa("skill_book.impossible", "Impossible", "不可能")
    }
}

class ItemSkillBook : Item() {
    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {
        if (!world.isRemote) player.openGui(ModMirageFairy2019.instance, GuiId.skill, player.world, player.position.x, player.position.y, player.position.z)
        return ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand))
    }

    @SideOnly(Side.CLIENT)
    override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        val player = Minecraft.getMinecraft().player ?: return
        val skillContainer = player.proxy.skillContainer

        fun parameter(key: String, valueSupplier: TextComponentScope.() -> TextComponentWrapper) {
            tooltip += formattedText { (translate(key) + ": "() + valueSupplier().white).blue }
        }
        parameter("skill_book.fairyMasterLevel") { "${skillContainer.skillManager.getFairyMasterLevel(skillContainer.variables.exp)}"() }
        parameter("skill_book.requiredFairyMasterXp") { "${skillContainer.skillManager.getRequiredFairyMasterExpForNextLevel(skillContainer.variables.exp)}"() }
        parameter("skill_book.sp") { "${skillContainer.remainingSkillPoints}"() }
        parameter("skill_book.masteryLevelReset") {
            when (skillContainer.canResetMastery(Instant.now())) {
                true -> translate("skill_book.possible").green
                false -> translate("skill_book.impossible").red
            }
        }
    }
}
