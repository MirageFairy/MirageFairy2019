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
import miragefairy2019.lib.resourcemaker.lang
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.copy
import miragefairy2019.mod.Main
import miragefairy2019.mod.artifacts.PotionCard
import miragefairy2019.mod.fairy.FairyCard
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
    lang("item.apostleStick.name", "Apostle Stick", "使徒のステッキ")


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

    fun f(inputRank: Int, count: Int, name: String, input: DataIngredient, output: DataResult) = makeRecipe("minia/$name") {
        DataShapelessRecipe(
            ingredients = listOf(
                DataOreIngredient(ore = "mirageFairyApostleStick"),
                input,
                *(0 until count).map { DataOreIngredient(ore = "mirageFairy2019FairyMinaRank$inputRank") }.toTypedArray(),
                DataOreIngredient(ore = "mirageFairyCrystal")
            ),
            result = output
        )
    }


    // 増殖

    // 岩石系
    r(2, 2, "dirt", DataOreIngredient(ore = "dirt"), DataResult(item = "minecraft:dirt", data = 0, count = 2)) // 土
    r(3, 1, "sand", DataOreIngredient(ore = "sand"), DataResult(item = "minecraft:sand", data = 0, count = 2)) // 砂
    r(3, 1, "gravel", DataOreIngredient(ore = "gravel"), DataResult(item = "minecraft:gravel", data = 0, count = 2)) // 砂利
    r(2, 2, "grass", DataOreIngredient(ore = "grass"), DataResult(item = "minecraft:grass", data = null, count = 2)) // 草ブロック
    r(2, 2, "mycelium", DataSimpleIngredient(item = "minecraft:mycelium"), DataResult(item = "minecraft:mycelium", data = null, count = 2)) // 菌糸
    r(1, 1, "cobblestone", DataOreIngredient(ore = "cobblestone"), DataResult(item = "minecraft:cobblestone", data = null, count = 2)) // 丸石

    // 鉱石系
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
    r(4, 2, "certus_quartz_ore", DataSimpleIngredient(item = "appliedenergistics2:quartz_ore"), DataResult(item = "appliedenergistics2:quartz_ore", data = null, count = 2)) // Certus Quarts

    // 無機物系
    r(4, 1, "glowstone", DataOreIngredient(ore = "glowstone"), DataResult(item = "minecraft:glowstone", count = 2)) // グロウストーン
    r(3, 2, "clay", DataSimpleIngredient(item = "minecraft:clay"), DataResult(item = "minecraft:clay", data = null, count = 2)) // 粘土

    // 有機物系
    r(3, 1, "log", DataOreIngredient(ore = "logWood"), DataResult(item = "minecraft:log", data = 0, count = 2)) // 原木
    r(3, 1, "wool", DataOreIngredient(ore = "wool"), DataResult(item = "minecraft:wool", data = 0, count = 2)) // 羊毛
    r(3, 2, "leather", DataOreIngredient(ore = "leather"), DataResult(item = "minecraft:leather", data = null, count = 2)) // 革
    r(3, 2, "vine", DataOreIngredient(ore = "vine"), DataResult(item = "minecraft:vine", data = null, count = 2)) // ツタ
    r(3, 1, "leaves", DataOreIngredient(ore = "treeLeaves"), DataResult(item = "minecraft:leaves", data = 0, count = 2)) // 葉ブロック
    r(4, 1, "waterlily", DataSimpleIngredient(item = "minecraft:waterlily"), DataResult(item = "minecraft:waterlily", data = null, count = 2)) // スイレン
    r(4, 4, "sponge", DataSimpleIngredient(item = "minecraft:sponge", data = 0), DataResult(item = "minecraft:sponge", data = 0, count = 2)) // スポンジ
    r(4, 4, "spider_eye", DataSimpleIngredient(item = "minecraft:spider_eye"), DataResult(item = "minecraft:spider_eye", data = null, count = 2)) // 蜘蛛の目
    r(4, 2, "slime_ball", DataOreIngredient(ore = "slimeball"), DataResult(item = "minecraft:slime_ball", data = null, count = 2)) // スライムボール
    r(5, 2, "ghast_tear", DataSimpleIngredient(item = "minecraft:ghast_tear"), DataResult(item = "minecraft:ghast_tear", data = null, count = 2)) // ガストの涙
    r(4, 1, "mirage_flower_seeds", DataSimpleIngredient(item = "miragefairy2019:mirage_flower_seeds"), DataResult(item = "miragefairy2019:mirage_flower_seeds", data = null, count = 2)) // ミラージュフラワーの種
    r(6, 1, "skeleton_skull", DataSimpleIngredient(item = "minecraft:skull", data = 0), DataResult(item = "minecraft:skull", data = 0, count = 2)) // スケルトンの頭
    r(6, 2, "wither_skeleton_skull", DataSimpleIngredient(item = "minecraft:skull", data = 1), DataResult(item = "minecraft:skull", data = 1, count = 2)) // ウィザースケルトンの頭

    // ダブルプラント
    r(3, 1, "sunflower", DataSimpleIngredient(item = "minecraft:double_plant", data = 0), DataResult(item = "minecraft:double_plant", data = 0, count = 2)) // ヒマワリ
    r(3, 1, "syringa", DataSimpleIngredient(item = "minecraft:double_plant", data = 1), DataResult(item = "minecraft:double_plant", data = 1, count = 2)) // ライラック
    r(3, 1, "double_grass", DataSimpleIngredient(item = "minecraft:double_plant", data = 2), DataResult(item = "minecraft:double_plant", data = 2, count = 2)) // 高い草
    r(3, 1, "double_fern", DataSimpleIngredient(item = "minecraft:double_plant", data = 3), DataResult(item = "minecraft:double_plant", data = 3, count = 2)) // 大きなシダ
    r(3, 1, "double_rose", DataSimpleIngredient(item = "minecraft:double_plant", data = 4), DataResult(item = "minecraft:double_plant", data = 4, count = 2)) // バラの低木
    r(3, 1, "paeonia", DataSimpleIngredient(item = "minecraft:double_plant", data = 5), DataResult(item = "minecraft:double_plant", data = 5, count = 2)) // 牡丹


    // 一発入手

    // 加工品系
    b(3, 1, "poison_juice", DataSimpleIngredient(item = "minecraft:rotten_flesh"), DataResult(item = "miragefairy2019:potion", data = PotionCard.POISON_JUICE.metadata)) // 毒薬
    r(7, 1, "totem_of_undying", DataSimpleIngredient(item = "minecraft:rotten_flesh"), DataResult(item = "minecraft:totem_of_undying")) // 不死のトーテム

    // 課金アイテム系
    b(7, 1, "skill_point_reset_potion", DataOreIngredient(ore = "nuggetGold"), DataResult(item = "miragefairy2019:potion", data = PotionCard.SKILL_POINT_RESET_POTION.metadata)) // SP還元ポーション

    // 妖精
    f(7, 1, "hatsuyume_fairy", DataSimpleIngredient(item = "minecraft:bed", data = 0), DataResult(item = "miragefairy2019:mirage_fairy", data = FairyCard.HATSUYUME.id))
    f(7, 1, "dark_chocolate_fairy", DataSimpleIngredient(item = "minecraft:cookie"), DataResult(item = "miragefairy2019:mirage_fairy", data = FairyCard.DARK_CHOCOLATE.id))
    f(6, 2, "diamond_dust_fairy", DataOreIngredient(ore = "gemDiamond"), DataResult(item = "miragefairy2019:mirage_fairy", data = FairyCard.DIAMOND_DUST.id))
    f(7, 1, "red_spinel_fairy", DataOreIngredient(ore = "dustRedstone"), DataResult(item = "miragefairy2019:mirage_fairy", data = FairyCard.RED_SPINEL.id))
    f(7, 2, "cupid_fairy", DataSimpleIngredient(item = "minecraft:bow"), DataResult(item = "miragefairy2019:mirage_fairy", data = FairyCard.CUPID.id))
    f(7, 1, "imperial_topaz_fairy", DataOreIngredient(ore = "gemTopaz"), DataResult(item = "miragefairy2019:mirage_fairy", data = FairyCard.IMPERIAL_TOPAZ.id))
    f(6, 4, "christmas_fairy", DataSimpleIngredient(item = "minecraft:sapling", data = 1), DataResult(item = "miragefairy2019:mirage_fairy", data = FairyCard.CHRISTMAS.id))
    f(7, 2, "santa_claus_fairy", DataSimpleIngredient(item = "minecraft:sapling", data = 1), DataResult(item = "miragefairy2019:mirage_fairy", data = FairyCard.SANTA_CLAUS.id))
    // avalon

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
