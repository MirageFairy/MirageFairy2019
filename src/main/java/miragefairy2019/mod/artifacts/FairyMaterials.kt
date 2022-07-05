package miragefairy2019.mod.artifacts

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.lib.fairyCentrifugeCraftHandler
import miragefairy2019.lib.modinitializer.ItemInitializer
import miragefairy2019.lib.modinitializer.addOreName
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.itemVariant
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.DataShapelessRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.generated
import miragefairy2019.lib.resourcemaker.handheld
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.EMPTY_ITEM_STACK
import miragefairy2019.libkt.ItemMultiMaterial
import miragefairy2019.libkt.ItemVariantMaterial
import miragefairy2019.libkt.aqua
import miragefairy2019.libkt.canTranslate
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.green
import miragefairy2019.libkt.ingredient
import miragefairy2019.libkt.notEmptyOrNull
import miragefairy2019.libkt.oreIngredient
import miragefairy2019.libkt.plus
import miragefairy2019.libkt.setCustomModelResourceLocations
import miragefairy2019.libkt.translateToLocal
import miragefairy2019.mod.Main
import miragefairy2019.mod.fairystickcraft.ApiFairyStickCraft
import miragefairy2019.mod.fairystickcraft.FairyStickCraftConditionConsumeBlock
import miragefairy2019.mod.fairystickcraft.FairyStickCraftConditionConsumeItem
import miragefairy2019.mod.fairystickcraft.FairyStickCraftConditionSpawnItem
import miragefairy2019.mod.fairystickcraft.FairyStickCraftConditionUseItem
import miragefairy2019.mod.fairystickcraft.FairyStickCraftRecipe
import mirrg.kotlin.hydrogen.toUpperCamelCase
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.OreIngredient

enum class FairyMaterialCard(
    val registryName: String,
    val isHandheld: Boolean,
    val unlocalizedName: String,
    val enName: String,
    val jaName: String,
    val jaPoem: String
) {
    AQUA_MANA_ROD("aqua_mana_rod", true, "manaRodAqua", "Aqua Mana Rod", "燐灰石の魔導芯棒", "優しさの心を伝える"),
    WIND_MANA_ROD("wind_mana_rod", true, "manaRodWind", "Wind Mana Rod", "蛍石の魔導芯棒", "直感の心を伝える"),
    GAIA_MANA_ROD("gaia_mana_rod", true, "manaRodGaia", "Gaia Mana Rod", "硫黄の魔導芯棒", "工夫の心を伝える"),
    FIRE_MANA_ROD("fire_mana_rod", true, "manaRodFire", "Fire Mana Rod", "辰砂の魔導芯棒", "閃きの心を伝える"),
    SHINE_MANA_ROD("shine_mana_rod", true, "manaRodShine", "Shine Mana Rod", "月長石の魔導芯棒", "真実の心を伝える"),
    DARK_MANA_ROD("dark_mana_rod", true, "manaRodDark", "Dark Mana Rod", "磁鉄鉱の魔導芯棒", "カリスマの心を伝える"),
    QUARTZ_MANA_ROD("quartz_mana_rod", true, "manaRodQuartz", "Nether Quartz Mana Rod", "ネザークォーツの魔導芯棒", "魔力を導通させる芯の入った棒材"),
    MIRAGE_FLOWER_STICK("mirage_flower_stick", true, "stickMirageFlower", "Mirage Flower Stem", "ミラージュフラワーの茎", "叩くとコツコツと鳴る"),
    MIRAGE_FLOWER_LEAF("mirage_flower_leaf", false, "leafMirageFlower", "Mirage Flower Leaf", "ミラージュフラワーの葉", "指を切らないように！"),
    MIRAGE_FAIRY_WOOD_STICK("mirage_fairy_wood_stick", true, "stickMirageFairyWood", "Fairy Wood Stick", "妖精の木の棒", "吸い込まれるようだ"),
    MIRAGIUM_WATER_BOTTLE("miragium_water_bottle", false, "bottleMiragiumWater", "Miragium Water Bottle", "ミラジウムウォーター入り瓶", "ほんのり甘い香り"),
    MIRAGE_FLOWER_EXTRACT_BOTTLE("mirage_flower_extract_bottle", false, "bottleMirageFlowerExtract", "Mirage Extract Bottle", "ミラージュエキス入り瓶", "飲めそうにはない"),
    MIRAGE_FLOWER_OIL_BOTTLE("mirage_flower_oil_bottle", false, "bottleMirageFlowerOil", "Mirage Oil Bottle", "ミラージュオイル入り瓶", "皮膚に付くとなかなか落ちない"),
    GLASS_MANA_ROD("glass_mana_rod", true, "manaRodGlass", "Glass Mana Rod", "ガラスの魔導芯棒", "絶縁性は石と同じ"),
    MIRAGE_FAIRY_LEATHER("mirage_fairy_leather", false, "mirageFairyLeather", "Fairy Leather", "妖精の革", "エーテルグラウンド"),
    FAIRY_WOOD_RESIN("fairy_wood_resin", false, "fairyWoodResin", "Fairy Wood Resin", "妖精の木の樹液", "くちどけまろやか"),
    SPHERE_BASE("sphere_base", false, "sphereBase", "Sphere Base", "スフィアベース", "前世が見える。              （らしい）"),
    FAIRY_SYRUP("fairy_syrup", false, "fairySyrup", "Fairy Syrup", "妖精のシロップ", "爽やかで心が洗われるような「水色」の香り"),
    FAIRY_PLASTIC("fairy_plastic", false, "fairyPlastic", "Fairy Plastic", "妖精のプラスチック", "熱可塑性有機高分子"),
    FAIRY_PLASTIC_WITH_FAIRY("fairy_plastic_with_fairy", false, "fairyPlasticWithFairy", "Fairy Plastic with Fairy", "妖精入り妖精のプラスチック", "何を見てヨシ！って言ったんですか！？"),
    FAIRY_PLASTIC_ROD("fairy_plastic_rod", true, "fairyPlasticRod", "Fairy Plastic Rod", "妖精のプラスチックの棒", "魔導性抜群、耐久性抜群、耐水性最悪"),
    INDIA_INK("india_ink", false, "indiaInk", "India Ink", "墨汁", "司書精はこれをコーヒーの代わりに飲んだらしい"),
    ANCIENT_POTTERY("ancient_pottery", false, "ancientPottery", "Ancient Pottery", "古代の壺", "「煮る」という発明"),
    ANNIHILATION_POTTERY("annihilation_pottery", false, "annihilationPottery", "Annihilation Pottery", "渇きの壺", "無限の水を吸い込む壺"),
}

object FairyMaterials {
    lateinit var itemFairyMaterials: () -> ItemMultiFairyMaterial
    val fairyMaterialsModule = module {

        // 妖精素材アイテム
        itemFairyMaterials = item({ ItemMultiFairyMaterial() }, "fairy_materials") {
            setUnlocalizedName("fairyMaterials")
            setCreativeTab { Main.creativeTab }
            EnumFairyMaterial.values().forEach {
                it.fairyMaterial.registerItemVariant(this)
            }
            onRegisterItem {
                if (Main.side.isClient) item.setCustomModelResourceLocations()
            }
        }

        // アイテムモデルの生成
        FairyMaterialCard.values().forEach { fairyMaterialCard ->
            makeItemModel(fairyMaterialCard.registryName) { if (fairyMaterialCard.isHandheld) handheld else generated }
        }

        // 翻訳の生成
        FairyMaterialCard.values().forEach { fairyMaterialCard ->
            onMakeLang {
                enJa("item.${fairyMaterialCard.unlocalizedName}.name", fairyMaterialCard.enName, fairyMaterialCard.jaName)
                enJa("item.${fairyMaterialCard.unlocalizedName}.poem", "", fairyMaterialCard.jaPoem)
            }
        }

        // レシピ生成
        run {

            // 月長石の魔導芯棒
            makeRecipe("shine_mana_rod") {
                DataShapedRecipe(
                    pattern = listOf(
                        " Gp",
                        "GRG",
                        "cG "
                    ),
                    key = mapOf(
                        "R" to DataOreIngredient(ore = "rodMiragium"),
                        "G" to DataOreIngredient(ore = "gemMoonstone"),
                        "c" to WandType.CRAFTING.ingredientData,
                        "p" to WandType.POLISHING.ingredientData
                    ),
                    result = DataResult(item = "miragefairy2019:fairy_materials", data = 0)
                )
            }

            // 辰砂の魔導芯棒
            makeRecipe("fire_mana_rod") {
                DataShapedRecipe(
                    pattern = listOf(
                        " Gp",
                        "GRG",
                        "cG "
                    ),
                    key = mapOf(
                        "R" to DataOreIngredient(ore = "rodMiragium"),
                        "G" to DataOreIngredient(ore = "gemCinnabar"),
                        "c" to WandType.CRAFTING.ingredientData,
                        "p" to WandType.POLISHING.ingredientData
                    ),
                    result = DataResult(item = "miragefairy2019:fairy_materials", data = 1)
                )
            }

            // 蛍石の魔導芯棒
            makeRecipe("wind_mana_rod") {
                DataShapedRecipe(
                    pattern = listOf(
                        " Gp",
                        "GRG",
                        "cG "
                    ),
                    key = mapOf(
                        "R" to DataOreIngredient(ore = "rodMiragium"),
                        "G" to DataOreIngredient(ore = "gemFluorite"),
                        "c" to WandType.CRAFTING.ingredientData,
                        "p" to WandType.POLISHING.ingredientData
                    ),
                    result = DataResult(item = "miragefairy2019:fairy_materials", data = 2)
                )
            }

            // 硫黄の魔導芯棒
            makeRecipe("gaia_mana_rod") {
                DataShapedRecipe(
                    pattern = listOf(
                        " Gp",
                        "GRG",
                        "cG "
                    ),
                    key = mapOf(
                        "R" to DataOreIngredient(ore = "rodMiragium"),
                        "G" to DataOreIngredient(ore = "gemSulfur"),
                        "c" to WandType.CRAFTING.ingredientData,
                        "p" to WandType.POLISHING.ingredientData
                    ),
                    result = DataResult(item = "miragefairy2019:fairy_materials", data = 3)
                )
            }

            // 燐灰石の魔導芯棒
            makeRecipe("aqua_mana_rod") {
                DataShapedRecipe(
                    pattern = listOf(
                        " Gp",
                        "GRG",
                        "cG "
                    ),
                    key = mapOf(
                        "R" to DataOreIngredient(ore = "rodMiragium"),
                        "G" to DataOreIngredient(ore = "gemApatite"),
                        "c" to WandType.CRAFTING.ingredientData,
                        "p" to WandType.POLISHING.ingredientData
                    ),
                    result = DataResult(item = "miragefairy2019:fairy_materials", data = 4)
                )
            }

            // 磁鉄鉱の魔導芯棒
            makeRecipe("dark_mana_rod") {
                DataShapedRecipe(
                    pattern = listOf(
                        " Gp",
                        "GRG",
                        "cG "
                    ),
                    key = mapOf(
                        "R" to DataOreIngredient(ore = "rodMiragium"),
                        "G" to DataOreIngredient(ore = "gemMagnetite"),
                        "c" to WandType.CRAFTING.ingredientData,
                        "p" to WandType.POLISHING.ingredientData
                    ),
                    result = DataResult(item = "miragefairy2019:fairy_materials", data = 5)
                )
            }

            // ネザークォーツの魔導芯棒
            makeRecipe("quartz_mana_rod") {
                DataShapedRecipe(
                    pattern = listOf(
                        " Gp",
                        "GRG",
                        "cG "
                    ),
                    key = mapOf(
                        "R" to DataOreIngredient(ore = "rodMiragium"),
                        "G" to DataOreIngredient(ore = "gemQuartz"),
                        "c" to WandType.CRAFTING.ingredientData,
                        "p" to WandType.POLISHING.ingredientData
                    ),
                    result = DataResult(item = "miragefairy2019:fairy_materials", data = 6)
                )
            }

            // ミラージュフラワーの茎
            makeRecipe("mirage_flower_stick") {
                DataShapelessRecipe(
                    ingredients = listOf(
                        DataOreIngredient(ore = "mirageFairyStick"),
                        DataOreIngredient(ore = "container1000MiragiumWater"),
                        DataOreIngredient(ore = "mirageFairy2019FairyAbilityCrystal"),
                        DataOreIngredient(ore = "leafMirageFlower"),
                        DataOreIngredient(ore = "dustApatite")
                    ),
                    result = DataResult(item = "miragefairy2019:fairy_materials", data = 7)
                )
            }

            // 妖精の木の棒
            makeRecipe("mirage_fairy_wood_stick") {
                DataShapelessRecipe(
                    ingredients = listOf(
                        DataOreIngredient(ore = "stickWood"),
                        DataOreIngredient(ore = "container250MirageFlowerOil")
                    ),
                    result = DataResult(item = "miragefairy2019:fairy_materials", data = 9)
                )
            }

            // エンチャントの瓶
            makeRecipe("experience_bottle") {
                DataShapelessRecipe(
                    ingredients = listOf(
                        DataOreIngredient(ore = "container250MirageFlowerExtract"),
                        WandType.BREAKING.ingredientData,
                        DataSimpleIngredient(item = "minecraft:ender_eye")
                    ),
                    result = DataResult(item = "minecraft:experience_bottle")
                )
            }

            // ガラスの魔導芯棒
            makeRecipe("glass_mana_rod") {
                DataShapedRecipe(
                    pattern = listOf(
                        " G ",
                        "GRG",
                        "cG "
                    ),
                    key = mapOf(
                        "R" to DataOreIngredient(ore = "rodMiragium"),
                        "G" to DataOreIngredient(ore = "blockGlass"),
                        "c" to WandType.CRAFTING.ingredientData
                    ),
                    result = DataResult(item = "miragefairy2019:fairy_materials", data = 13)
                )
            }

            // 妖精の革
            makeRecipe("mirage_fairy_leather") {
                DataShapelessRecipe(
                    ingredients = listOf(
                        DataOreIngredient(ore = "leather"),
                        DataOreIngredient(ore = "container1000MirageFlowerOil")
                    ),
                    result = DataResult(item = "miragefairy2019:fairy_materials", data = 14)
                )
            }

            // エリトラ
            makeRecipe("elytra_from_mirage_fairy_leather") {
                DataShapedRecipe(
                    pattern = listOf(
                        "SLS",
                        "LLL",
                        "LcL"
                    ),
                    key = mapOf(
                        "c" to WandType.CRAFTING.ingredientData,
                        "S" to DataOreIngredient(ore = "mirageFairy2019SphereLevitate"),
                        "L" to DataOreIngredient(ore = "mirageFairyLeather")
                    ),
                    result = DataResult(
                        item = "minecraft:elytra"
                    )
                )
            }

            // 5棒＋8樹液→松明8
            makeRecipe("torch_from_fairy_wood_resin") {
                DataShapedRecipe(
                    pattern = listOf(
                        "r",
                        "S"
                    ),
                    key = mapOf(
                        "r" to DataOreIngredient(ore = "mirageFairyWoodResin"),
                        "S" to DataOreIngredient(ore = "stickWood")
                    ),
                    result = DataResult(
                        item = "minecraft:torch",
                        count = 8
                    )
                )
            }

            // 蛍石→スフィアベース
            makeRecipe("sphere_base") {
                DataShapelessRecipe(
                    ingredients = listOf(
                        WandType.POLISHING.ingredientData,
                        DataOreIngredient(ore = "gemFluorite")
                    ),
                    result = DataResult(item = "miragefairy2019:fairy_materials", data = 16, count = 2)
                )
            }

            // 宝石→スフィアベース
            fun makeSphereBaseRecipe(materialName: String) = makeRecipe("sphere_base_from_$materialName") {
                DataShapedRecipe(
                    pattern = listOf(
                        " Gp",
                        "GDG",
                        "fG "
                    ),
                    key = mapOf(
                        "p" to WandType.POLISHING.ingredientData,
                        "f" to WandType.FUSION.ingredientData,
                        "D" to DataOreIngredient(ore = "dustMiragium"),
                        "G" to DataOreIngredient(ore = "gem${materialName.toUpperCamelCase()}")
                    ),
                    result = DataResult(
                        item = "miragefairy2019:fairy_materials",
                        data = 16,
                        count = 4
                    )
                )
            }
            makeSphereBaseRecipe("diamond")
            makeSphereBaseRecipe("emerald")
            makeSphereBaseRecipe("pyrope")
            makeSphereBaseRecipe("smithsonite")
            makeSphereBaseRecipe("nephrite")
            makeSphereBaseRecipe("topaz")
            makeSphereBaseRecipe("tourmaline")
            makeSphereBaseRecipe("heliolite")
            makeSphereBaseRecipe("labradorite")

            // 妖精のプラスチック2→妖精のプラスチックの棒4
            makeRecipe("fairy_plastic_rod") {
                DataShapedRecipe(
                    pattern = listOf(
                        "X",
                        "X"
                    ),
                    key = mapOf(
                        "X" to DataOreIngredient(ore = "gemMirageFairyPlastic")
                    ),
                    result = DataResult(
                        item = "miragefairy2019:fairy_materials",
                        data = 20,
                        count = 4
                    )
                )
            }

            // 木炭の粉＋樹液＋空き瓶→墨汁
            makeRecipe("india_ink") {
                DataShapelessRecipe(
                    ingredients = listOf(
                        DataOreIngredient(ore = "dustCharcoal"),
                        DataOreIngredient(ore = "mirageFairyWoodResin"),
                        DataSimpleIngredient(item = "minecraft:glass_bottle")
                    ),
                    result = DataResult(
                        item = "miragefairy2019:fairy_materials",
                        data = 21,
                        count = 1
                    )
                )
            }

        }

        // レシピ
        onAddRecipe {

            // ガラス棒＋クォーツ→クォーツ棒
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
                it.conditions += FairyStickCraftConditionUseItem(OreIngredient("mirageFairyStick"))
                it.conditions += FairyStickCraftConditionConsumeBlock { FluidMaterials.blockFluidMirageFlowerExtract().defaultState }
                it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("mirageFairy2019ManaRodGlass"))
                it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("gemQuartz"), 16)
                it.conditions += FairyStickCraftConditionSpawnItem { itemFairyMaterials[EnumFairyMaterial.manaRodQuartz].createItemStack() }
            })

            // ミラ葉＋骨＋燐灰石→ミラ茎
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
                it.conditions += FairyStickCraftConditionUseItem(OreIngredient("mirageFairyStick"))
                it.conditions += FairyStickCraftConditionConsumeBlock { FluidMaterials.blockFluidMiragiumWater().defaultState }
                it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("mirageFairy2019FairyAbilityCrystal"))
                it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("leafMirageFlower"))
                it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("dustApatite"))
                it.conditions += FairyStickCraftConditionSpawnItem { itemFairyMaterials[EnumFairyMaterial.stickMirageFlower].createItemStack() }
            })

            // 空き瓶＋ミラ葉64個＞破砕→ミラエキス瓶
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
                it.conditions += FairyStickCraftConditionUseItem(WandType.BREAKING.ingredient)
                it.conditions += FairyStickCraftConditionConsumeItem(Ingredient.fromItem(Items.GLASS_BOTTLE))
                it.conditions += FairyStickCraftConditionConsumeItem(itemFairyMaterials[EnumFairyMaterial.leafMirageFlower].createItemStack().ingredient, 64)
                it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("dustApatite"), 5)
                it.conditions += FairyStickCraftConditionSpawnItem { itemFairyMaterials[EnumFairyMaterial.bottleMirageFlowerExtract].createItemStack() }
            })

            // 空き瓶＋ミラ葉50個＞珠玉→ミラエキス瓶
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
                it.conditions += FairyStickCraftConditionUseItem(WandType.POLISHING.ingredient)
                it.conditions += FairyStickCraftConditionConsumeItem(Ingredient.fromItem(Items.GLASS_BOTTLE))
                it.conditions += FairyStickCraftConditionConsumeItem(itemFairyMaterials[EnumFairyMaterial.leafMirageFlower].createItemStack().ingredient, 50)
                it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("dustApatite"), 4)
                it.conditions += FairyStickCraftConditionSpawnItem { itemFairyMaterials[EnumFairyMaterial.bottleMirageFlowerExtract].createItemStack() }
            })

            // 空き瓶＋ミラ葉40個＞歪曲→ミラエキス瓶
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
                it.conditions += FairyStickCraftConditionUseItem(WandType.DISTORTION.ingredient)
                it.conditions += FairyStickCraftConditionConsumeItem(Ingredient.fromItem(Items.GLASS_BOTTLE))
                it.conditions += FairyStickCraftConditionConsumeItem(itemFairyMaterials[EnumFairyMaterial.leafMirageFlower].createItemStack().ingredient, 40)
                it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("dustApatite"), 3)
                it.conditions += FairyStickCraftConditionSpawnItem { itemFairyMaterials[EnumFairyMaterial.bottleMirageFlowerExtract].createItemStack() }
            })

            // ミラの葉30＋燐灰石の粉2＋空き瓶＞錬金の家→ミラエキス瓶
            fairyCentrifugeCraftHandler(30.0) {
                process { !Mana.GAIA + !Erg.DESTROY * 2.0 }
                process { !Mana.GAIA + !Erg.FLAME * 2.0 }
                process { !Mana.WIND + !Erg.CHEMICAL * 2.0 }
                input(itemFairyMaterials[EnumFairyMaterial.leafMirageFlower].createItemStack().ingredient, 30)
                input("dustApatite".oreIngredient, 2)
                input(Items.GLASS_BOTTLE.createItemStack().ingredient, 1)
                output(itemFairyMaterials[EnumFairyMaterial.bottleMirageFlowerExtract].createItemStack(), 1.0)
            }

            // 空き瓶＋ミラ種50個＋辰砂の粉4個＞珠玉→ミラオイル瓶
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
                it.conditions += FairyStickCraftConditionUseItem(WandType.POLISHING.ingredient)
                it.conditions += FairyStickCraftConditionConsumeItem(Ingredient.fromItem(Items.GLASS_BOTTLE))
                it.conditions += FairyStickCraftConditionConsumeItem(Ingredient.fromItem(MirageFlower.itemMirageFlowerSeeds()), 50)
                it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("dustCinnabar"), 4)
                it.conditions += FairyStickCraftConditionSpawnItem { itemFairyMaterials[EnumFairyMaterial.bottleMirageFlowerOil].createItemStack() }
            })

            // 空き瓶＋ミラ種50個＋辰砂の粉4個＞歪曲→ミラオイル瓶
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
                it.conditions += FairyStickCraftConditionUseItem(WandType.DISTORTION.ingredient)
                it.conditions += FairyStickCraftConditionConsumeItem(Ingredient.fromItem(Items.GLASS_BOTTLE))
                it.conditions += FairyStickCraftConditionConsumeItem(Ingredient.fromItem(MirageFlower.itemMirageFlowerSeeds()), 40)
                it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("dustCinnabar"), 3)
                it.conditions += FairyStickCraftConditionSpawnItem { itemFairyMaterials[EnumFairyMaterial.bottleMirageFlowerOil].createItemStack() }
            })

            // ミラの種30＋辰砂の粉2＋空き瓶＞錬金の家→ミラオイル瓶
            fairyCentrifugeCraftHandler(30.0) {
                process { !Mana.GAIA + !Erg.DESTROY * 2.0 }
                process { !Mana.GAIA + !Erg.FLAME * 2.0 }
                process { !Mana.WIND + !Erg.CHEMICAL * 2.0 }
                input(MirageFlower.itemMirageFlowerSeeds().createItemStack().ingredient, 30)
                input("dustCinnabar".oreIngredient, 2)
                input(Items.GLASS_BOTTLE.createItemStack().ingredient, 1)
                output(itemFairyMaterials[EnumFairyMaterial.bottleMirageFlowerOil].createItemStack(), 1.0)
            }

        }

    }
}

@Suppress("EnumEntryName", "unused")
enum class EnumFairyMaterial(override val fairyMaterial: FairyMaterial) : IFairyMaterialProvider {
    manaRodShine(FairyMaterial(0, "shine_mana_rod", "manaRodShine", 3) ore "mirageFairy2019ManaRodShine"),
    manaRodFire(FairyMaterial(1, "fire_mana_rod", "manaRodFire", 3) ore "mirageFairy2019ManaRodFire"),
    manaRodWind(FairyMaterial(2, "wind_mana_rod", "manaRodWind", 3) ore "mirageFairy2019ManaRodWind"),
    manaRodGaia(FairyMaterial(3, "gaia_mana_rod", "manaRodGaia", 3) ore "mirageFairy2019ManaRodGaia"),
    manaRodAqua(FairyMaterial(4, "aqua_mana_rod", "manaRodAqua", 3) ore "mirageFairy2019ManaRodAqua"),
    manaRodDark(FairyMaterial(5, "dark_mana_rod", "manaRodDark", 3) ore "mirageFairy2019ManaRodDark"),
    manaRodQuartz(FairyMaterial(6, "quartz_mana_rod", "manaRodQuartz", 3) ore "mirageFairy2019ManaRodQuartz"),
    stickMirageFlower(FairyMaterial(7, "mirage_flower_stick", "stickMirageFlower", 1) ore "stickMirageFlower"),
    leafMirageFlower(FairyMaterial(8, "mirage_flower_leaf", "leafMirageFlower", 0) ore "leafMirageFlower"),
    stickMirageFairyWood(FairyMaterial(9, "mirage_fairy_wood_stick", "stickMirageFairyWood", 4) ore "stickMirageFairyWood"),
    bottleMiragiumWater(FairyMaterial(10, "miragium_water_bottle", "bottleMiragiumWater", 0) ore "bottleMiragiumWater" ore "container250MiragiumWater" has bottle),
    bottleMirageFlowerExtract(FairyMaterial(11, "mirage_flower_extract_bottle", "bottleMirageFlowerExtract", 2) ore "bottleMirageFlowerExtract" ore "container250MirageFlowerExtract" has bottle),
    bottleMirageFlowerOil(FairyMaterial(12, "mirage_flower_oil_bottle", "bottleMirageFlowerOil", 4) ore "bottleMirageFlowerOil" ore "container250MirageFlowerOil" has bottle),
    manaRodGlass(FairyMaterial(13, "glass_mana_rod", "manaRodGlass", 2) ore "mirageFairy2019ManaRodGlass"),
    mirageFairyLeather(FairyMaterial(14, "mirage_fairy_leather", "mirageFairyLeather", 4) ore "mirageFairyLeather"),
    fairyWoodResin(FairyMaterial(15, "fairy_wood_resin", "fairyWoodResin", 5) ore "mirageFairyWoodResin" fuel 1600),
    sphereBase(FairyMaterial(16, "sphere_base", "sphereBase", 3) ore "mirageFairy2019SphereBase"),
    fairySyrup(FairyMaterial(17, "fairy_syrup", "fairySyrup", 5) ore "mirageFairySyrup" has bottle),
    fairyPlastic(FairyMaterial(18, "fairy_plastic", "fairyPlastic", 5) ore "gemMirageFairyPlastic"),
    fairyPlasticWithFairy(FairyMaterial(19, "fairy_plastic_with_fairy", "fairyPlasticWithFairy", 5) ore "gemMirageFairyPlasticWithFairy"),
    fairyPlasticRod(FairyMaterial(20, "fairy_plastic_rod", "fairyPlasticRod", 5) ore "rodMirageFairyPlastic"),
    indiaInk(FairyMaterial(21, "india_ink", "indiaInk", 0) ore "dyeBlack" has bottle),
    ancientPottery(FairyMaterial(22, "ancient_pottery", "ancientPottery", 5) ore "mirageFairyAncientPottery"),
    annihilationPottery(FairyMaterial(23, "annihilation_pottery", "annihilationPottery", 5) ore "mirageFairyAnnihilationPottery"),
}


// ItemMultiMaterial

interface IFairyMaterialProvider {
    val fairyMaterial: FairyMaterial
}

class FairyMaterial(
    val metadata: Int,
    val registryName: String,
    val unlocalizedName: String,
    val tier: Int
) {
    val oreNames = mutableListOf<String>()
    var burnTime: Int? = null
    var containerItemSupplier: (() -> ItemStack)? = null
    fun registerItemVariant(itemInitializer: ItemInitializer<ItemMultiFairyMaterial>) = itemInitializer.itemVariant(registryName, {
        ItemVariantFairyMaterial(
            registryName = it,
            unlocalizedName = unlocalizedName,
            tier = tier,
            burnTime = burnTime,
            containerItemSupplier = containerItemSupplier
        )
    }, metadata) {
        oreNames.forEach { addOreName(it) }
    }
}

private infix fun FairyMaterial.has(block: (FairyMaterial) -> Unit) = this.also { block(this) }
private infix fun FairyMaterial.ore(oreName: String) = this has { it.oreNames += oreName }
private infix fun FairyMaterial.fuel(burnTime: Int) = this has { it.burnTime = burnTime }
private val bottle: (FairyMaterial) -> Unit get() = { it.containerItemSupplier = { ItemStack(Items.GLASS_BOTTLE) } }

class ItemVariantFairyMaterial(
    registryName: String,
    unlocalizedName: String,
    val tier: Int,
    val burnTime: Int?,
    val containerItemSupplier: (() -> ItemStack)?
) : ItemVariantMaterial(registryName, unlocalizedName)

class ItemMultiFairyMaterial : ItemMultiMaterial<ItemVariantFairyMaterial>() {
    @SideOnly(Side.CLIENT)
    override fun addInformation(itemStack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag) {
        val variant = getVariant(itemStack) ?: return

        // ポエム
        if (canTranslate("${getUnlocalizedName(itemStack)}.poem")) {
            val string = translateToLocal("${getUnlocalizedName(itemStack)}.poem")
            if (string.isNotEmpty()) tooltip += string
        }

        // Tier
        tooltip += formattedText { "Tier ${variant.tier}"().aqua }

        // Container Item
        if (flag.isAdvanced) {
            getContainerItemStack(itemStack)?.let { containerItemStack ->
                tooltip += formattedText { ("Has "() + containerItemStack.displayName()).green } // TODO translation
            }
        }

    }


    override fun getItemBurnTime(itemStack: ItemStack) = getVariant(itemStack)?.burnTime ?: -1


    private fun getContainerItemStack(itemStack: ItemStack) = getVariant(itemStack)?.containerItemSupplier?.invoke()?.notEmptyOrNull
    override fun hasContainerItem(itemStack: ItemStack) = getContainerItemStack(itemStack) != null
    override fun getContainerItem(itemStack: ItemStack) = getContainerItemStack(itemStack) ?: EMPTY_ITEM_STACK
}

operator fun (() -> ItemMultiFairyMaterial).get(fairyMaterialProvider: IFairyMaterialProvider) = this().getVariant(fairyMaterialProvider.fairyMaterial.metadata)!!
