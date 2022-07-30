package miragefairy2019.mod.systems

import miragefairy2019.lib.modinitializer.addOreName
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.handheld
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.mod.artifacts.ingredientData
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

    fun register(name: String, output: DataResult, inputRank: Int, pattern: PatternBuilder.() -> Unit) = makeRecipe("minia/${name}_from_minia") {
        DataShapedRecipe(
            pattern = PatternBuilder().also { pattern(it) }.list,
            key = mapOf(
                "s" to miragefairy2019.mod.artifacts.WandType.SUMMONING.ingredientData,
                "#" to DataOreIngredient(ore = "mirageFairy2019FairyMinaRank$inputRank")
            ),
            result = output
        )
    }

    register("cobblestone", DataResult(item = "minecraft:cobblestone"), 2) { // 丸石
        !"#  "
        !" s "
        !"   "
    }
    register("charcoal", DataResult(item = "minecraft:coal", data = 1), 3) { // 木炭
        !"   "
        !" s "
        !"  #"
    }
    register("iron_ingot", DataResult(item = "minecraft:iron_ingot"), 3) { // 鉄
        !"#  "
        !" s "
        !"   "
    }
    register("gold_ingot", DataResult(item = "minecraft:gold_ingot"), 3) { // 金
        !"## "
        !" s "
        !"   "
    }
    register("magnetite", DataResult(item = "miragefairy2019:materials", data = 8), 5) { // 磁鉄鉱
        !"   "
        !" s "
        !" # "
    }
    register("sulfur", DataResult(item = "miragefairy2019:materials", data = 2), 5) { // 硫黄
        !"   "
        !" s "
        !"## "
    }
    register("apatite", DataResult(item = "miragefairy2019:materials", data = 0), 5) { // 燐灰石
        !"   "
        !" s "
        !" ##"
    }
    register("cinnabar", DataResult(item = "miragefairy2019:materials", data = 6), 5) { // 辰砂
        !"#  "
        !"#s "
        !"## "
    }
    register("fluorite", DataResult(item = "miragefairy2019:materials", data = 1), 5) { // 蛍石
        !"  #"
        !" s#"
        !" ##"
    }
    register("moonstone", DataResult(item = "miragefairy2019:materials", data = 7), 5) { // 月長石
        !"###"
        !"#s#"
        !"###"
    }

}

class ItemApostleStick : Item() {
    @SideOnly(Side.CLIENT)
    override fun isFull3D() = true
    override fun hasContainerItem(stack: ItemStack) = true
    override fun getContainerItem(itemStack: ItemStack): ItemStack = itemStack.copy()
}
