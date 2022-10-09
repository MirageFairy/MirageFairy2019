package miragefairy2019.mod.recipes

import miragefairy2019.lib.modinitializer.addOreName
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.resourcemaker.DataIngredient
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.DataShapelessRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.handheld
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.copy
import miragefairy2019.libkt.enJa
import miragefairy2019.mod.Main
import miragefairy2019.mod.artifacts.PotionCard
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

lateinit var itemApostleStick: () -> ItemApostleStick

val miniaRecipeModule = module {

    // 使徒のステッキ
    itemApostleStick = item({ ItemApostleStick() }, "apostle_stick") {
        setUnlocalizedName("apostleStick")
        setCreativeTab { Main.creativeTab }
        setCustomModelResourceLocation()
        addOreName("mirageFairyApostleStick")
        makeItemModel { handheld }
        makeRecipe {
            DataShapedRecipe(
                pattern = listOf(
                    " g ",
                    "g#g",
                    " g "
                ),
                key = mapOf(
                    "#" to DataOreIngredient(ore = "mirageFairyStick"),
                    "g" to DataOreIngredient(ore = "nuggetGold")
                ),
                result = DataResult(item = "miragefairy2019:apostle_stick")
            )
        }
    }

    // 翻訳生成
    onMakeLang { enJa("item.apostleStick.name", "Apostle Stick", "使徒のステッキ") }


    fun r(inputRank: Int, count: Int, name: String, input: DataIngredient, output: DataResult) = makeRecipe("minia/$name") {
        DataShapelessRecipe(
            ingredients = listOf(
                DataOreIngredient(ore = "mirageFairyApostleStick"),
                input,
                *(0 until count).map { DataOreIngredient(ore = "mirageFairy2019FairyMinaRank$inputRank") }.toTypedArray()
            ),
            result = output
        )
    }

    fun b(inputRank: Int, count: Int, name: String, input: DataIngredient, output: DataResult) = makeRecipe("minia/$name") {
        DataShapelessRecipe(
            ingredients = listOf(
                DataOreIngredient(ore = "mirageFairyApostleStick"),
                input,
                *(0 until count).map { DataOreIngredient(ore = "mirageFairy2019FairyMinaRank$inputRank") }.toTypedArray(),
                DataSimpleIngredient(item = "minecraft:glass_bottle")
            ),
            result = output
        )
    }

    r(1, 1, "cobblestone", DataOreIngredient(ore = "cobblestone"), DataResult(item = "minecraft:cobblestone", data = null, count = 2)) // 丸石
    r(3, 1, "coal_ore", DataOreIngredient(ore = "oreCoal"), DataResult(item = "minecraft:coal_ore", data = null, count = 2)) // 石炭
    r(3, 1, "iron_ore", DataOreIngredient(ore = "oreIron"), DataResult(item = "minecraft:iron_ore", data = null, count = 2)) // 鉄
    r(3, 4, "gold_ore", DataOreIngredient(ore = "oreGold"), DataResult(item = "minecraft:gold_ore", data = null, count = 2)) // 金
    r(3, 2, "redstone_ore", DataOreIngredient(ore = "oreRedstone"), DataResult(item = "minecraft:redstone_ore", data = null, count = 2)) // 赤石
    r(4, 2, "lapis_ore", DataOreIngredient(ore = "oreLapis"), DataResult(item = "minecraft:lapis_ore", data = null, count = 2)) // ラピスラズリ
    r(4, 1, "emerald_ore", DataOreIngredient(ore = "oreEmerald"), DataResult(item = "minecraft:emerald_ore", data = null, count = 2)) // エメラルド
    r(4, 4, "diamond_ore", DataOreIngredient(ore = "oreDiamond"), DataResult(item = "minecraft:diamond_ore", data = null, count = 2)) // ダイヤモンド
    r(4, 1, "quartz_ore", DataOreIngredient(ore = "oreQuartz"), DataResult(item = "minecraft:quartz_ore", data = null, count = 2)) // ネザークォーツ
    r(4, 1, "magnetite_ore", DataOreIngredient(ore = "oreMagnetite"), DataResult(item = "miragefairy2019:ore1", data = 5, count = 2)) // 磁鉄鉱
    r(4, 1, "sulfur_ore", DataOreIngredient(ore = "oreSulfur"), DataResult(item = "miragefairy2019:ore1", data = 2, count = 2)) // 硫黄
    r(4, 1, "apatite_ore", DataOreIngredient(ore = "oreApatite"), DataResult(item = "miragefairy2019:ore1", data = 0, count = 2)) // 燐灰石
    r(4, 1, "cinnabar_ore", DataOreIngredient(ore = "oreCinnabar"), DataResult(item = "miragefairy2019:ore1", data = 3, count = 2)) // 辰砂
    r(4, 1, "fluorite_ore", DataOreIngredient(ore = "oreFluorite"), DataResult(item = "miragefairy2019:ore1", data = 1, count = 2)) // 蛍石
    r(4, 2, "moonstone_ore", DataOreIngredient(ore = "oreMoonstone"), DataResult(item = "miragefairy2019:ore1", data = 4, count = 2)) // 月長石
    r(3, 2, "log", DataOreIngredient(ore = "logWood"), DataResult(item = "minecraft:log", data = 0, count = 2)) // 原木
    r(4, 1, "glowstone", DataOreIngredient(ore = "glowstone"), DataResult(item = "minecraft:glowstone", count = 2)) // グロウストーン
    b(3, 1, "poison_juice", DataSimpleIngredient(item = "minecraft:rotten_flesh"), DataResult(item = "miragefairy2019:potion", data = PotionCard.POISON_JUICE.metadata)) // 毒薬
    b(7, 1, "skill_point_reset_potion", DataOreIngredient(ore = "nuggetGold"), DataResult(item = "miragefairy2019:potion", data = PotionCard.SKILL_POINT_RESET_POTION.metadata)) // SP還元ポーション

}

class ItemApostleStick : Item() {
    init {
        maxStackSize = 1
    }

    @SideOnly(Side.CLIENT)
    override fun isFull3D() = true
    override fun hasContainerItem(stack: ItemStack) = true
    override fun getContainerItem(itemStack: ItemStack): ItemStack = itemStack.copy(1)
}
