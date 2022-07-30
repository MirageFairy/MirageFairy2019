package miragefairy2019.mod.systems

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
import miragefairy2019.lib.resourcemaker.handheld
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.enJa
import miragefairy2019.mod.Main
import mirrg.kotlin.hydrogen.unit
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


    class PatternBuilder {
        val list = mutableListOf<String>()
        operator fun String.not() = unit { list += this }
    }

    fun register(name: String, input: DataIngredient, output: DataResult, inputRank: Int, count: Int) = makeRecipe("minia/${name}_from_minia") {
        DataShapelessRecipe(
            ingredients = listOf(
                input,
                DataOreIngredient(ore = "mirageFairyApostleStick"),
                *(0 until count).map { DataOreIngredient(ore = "mirageFairy2019FairyMinaRank$inputRank") }.toTypedArray()
            ),
            result = output
        )
    }

    register("cobblestone", DataOreIngredient(ore = "cobblestone"), DataResult(item = "minecraft:cobblestone", data = null, count = 2), 2, 1) // 丸石
    register("charcoal", DataOreIngredient(ore = "oreCoal"), DataResult(item = "minecraft:coal_ore", data = null, count = 2), 3, 1) // 石炭
    register("iron_ingot", DataOreIngredient(ore = "oreIron"), DataResult(item = "minecraft:iron_ore", data = null, count = 2), 3, 1) // 鉄
    register("gold_ingot", DataOreIngredient(ore = "oreGold"), DataResult(item = "minecraft:gold_ore", data = null, count = 2), 3, 4) // 金
    register("redstone", DataOreIngredient(ore = "oreRedstone"), DataResult(item = "minecraft:redstone_ore", data = null, count = 2), 3, 2) // 赤石
    register("lapis", DataOreIngredient(ore = "oreLapis"), DataResult(item = "minecraft:lapis_ore", data = null, count = 2), 4, 2) // ラピスラズリ
    register("emerald", DataOreIngredient(ore = "oreEmerald"), DataResult(item = "minecraft:emerald_ore", data = null, count = 2), 4, 1) // エメラルド
    register("diamond", DataOreIngredient(ore = "oreDiamond"), DataResult(item = "minecraft:diamond_ore", data = null, count = 2), 4, 4) // ダイヤモンド
    register("magnetite", DataOreIngredient(ore = "oreMagnetite"), DataResult(item = "miragefairy2019:ore1", data = 5, count = 2), 4, 1) // 磁鉄鉱
    register("sulfur", DataOreIngredient(ore = "oreSulfur"), DataResult(item = "miragefairy2019:ore1", data = 2, count = 2), 4, 1) // 硫黄
    register("apatite", DataOreIngredient(ore = "oreApatite"), DataResult(item = "miragefairy2019:ore1", data = 0, count = 2), 4, 1) // 燐灰石
    register("cinnabar", DataOreIngredient(ore = "oreCinnabar"), DataResult(item = "miragefairy2019:ore1", data = 3, count = 2), 4, 1) // 辰砂
    register("fluorite", DataOreIngredient(ore = "oreFluorite"), DataResult(item = "miragefairy2019:ore1", data = 1, count = 2), 4, 1) // 蛍石
    register("moonstone", DataOreIngredient(ore = "oreMoonstone"), DataResult(item = "miragefairy2019:ore1", data = 4, count = 2), 4, 2) // 月長石

}

class ItemApostleStick : Item() {
    @SideOnly(Side.CLIENT)
    override fun isFull3D() = true
    override fun hasContainerItem(stack: ItemStack) = true
    override fun getContainerItem(itemStack: ItemStack): ItemStack = itemStack.copy()
}
