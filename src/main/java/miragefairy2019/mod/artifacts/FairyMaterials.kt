package miragefairy2019.mod.artifacts

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.lib.fairyCentrifugeCraftHandler
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
    val metadata: Int,
    val tier: Int,
    val registryName: String,
    val isHandheld: Boolean,
    val unlocalizedName: String,
    val enName: String,
    val jaName: String,
    val jaPoem: String,
    initializer: FairyMaterialCard.() -> Unit
) {
    SHINE_MANA_ROD(0, 3, "shine_mana_rod", true, "manaRodShine", "Shine Mana Rod", "月長石の魔導芯棒", "真実の心を伝える", { ore("mirageFairy2019ManaRodShine") }),
    FIRE_MANA_ROD(1, 3, "fire_mana_rod", true, "manaRodFire", "Fire Mana Rod", "辰砂の魔導芯棒", "閃きの心を伝える", { ore("mirageFairy2019ManaRodFire") }),
    WIND_MANA_ROD(2, 3, "wind_mana_rod", true, "manaRodWind", "Wind Mana Rod", "蛍石の魔導芯棒", "直感の心を伝える", { ore("mirageFairy2019ManaRodWind") }),
    GAIA_MANA_ROD(3, 3, "gaia_mana_rod", true, "manaRodGaia", "Gaia Mana Rod", "硫黄の魔導芯棒", "工夫の心を伝える", { ore("mirageFairy2019ManaRodGaia") }),
    AQUA_MANA_ROD(4, 3, "aqua_mana_rod", true, "manaRodAqua", "Aqua Mana Rod", "燐灰石の魔導芯棒", "優しさの心を伝える", { ore("mirageFairy2019ManaRodAqua") }),
    DARK_MANA_ROD(5, 3, "dark_mana_rod", true, "manaRodDark", "Dark Mana Rod", "磁鉄鉱の魔導芯棒", "カリスマの心を伝える", { ore("mirageFairy2019ManaRodDark") }),
    QUARTZ_MANA_ROD(6, 3, "quartz_mana_rod", true, "manaRodQuartz", "Nether Quartz Mana Rod", "ネザークォーツの魔導芯棒", "魔力を導通させる芯の入った棒材", { ore("mirageFairy2019ManaRodQuartz") }),
    MIRAGE_FLOWER_STICK(7, 1, "mirage_flower_stick", true, "stickMirageFlower", "Mirage Flower Stem", "ミラージュフラワーの茎", "叩くとコツコツと鳴る", { ore("stickMirageFlower") }),
    MIRAGE_FLOWER_LEAF(8, 0, "mirage_flower_leaf", false, "leafMirageFlower", "Mirage Flower Leaf", "ミラージュフラワーの葉", "指を切らないように！", { ore("leafMirageFlower") }),
    MIRAGE_FAIRY_WOOD_STICK(9, 4, "mirage_fairy_wood_stick", true, "stickMirageFairyWood", "Fairy Wood Stick", "妖精の木の棒", "吸い込まれるようだ", { ore("stickMirageFairyWood") }),
    MIRAGIUM_WATER_BOTTLE(10, 0, "miragium_water_bottle", false, "bottleMiragiumWater", "Miragium Water Bottle", "ミラジウムウォーター入り瓶", "ほんのり甘い香り", { ore("bottleMiragiumWater").ore("container250MiragiumWater").bottle() }),
    MIRAGE_FLOWER_EXTRACT_BOTTLE(11, 2, "mirage_flower_extract_bottle", false, "bottleMirageFlowerExtract", "Mirage Extract Bottle", "ミラージュエキス入り瓶", "飲めそうにはない", { ore("bottleMirageFlowerExtract").ore("container250MirageFlowerExtract").bottle() }),
    MIRAGE_FLOWER_OIL_BOTTLE(12, 4, "mirage_flower_oil_bottle", false, "bottleMirageFlowerOil", "Mirage Oil Bottle", "ミラージュオイル入り瓶", "皮膚に付くとなかなか落ちない", { ore("bottleMirageFlowerOil").ore("container250MirageFlowerOil").bottle() }),
    GLASS_MANA_ROD(13, 2, "glass_mana_rod", true, "manaRodGlass", "Glass Mana Rod", "ガラスの魔導芯棒", "絶縁性は石と同じ", { ore("mirageFairy2019ManaRodGlass") }),
    MIRAGE_FAIRY_LEATHER(14, 4, "mirage_fairy_leather", false, "mirageFairyLeather", "Fairy Leather", "妖精の革", "エーテルグラウンド", { ore("mirageFairyLeather") }),
    FAIRY_WOOD_RESIN(15, 5, "fairy_wood_resin", false, "fairyWoodResin", "Fairy Wood Resin", "妖精の木の樹液", "くちどけまろやか", { ore("mirageFairyWoodResin").fuel(1600) }),
    SPHERE_BASE(16, 3, "sphere_base", false, "sphereBase", "Sphere Base", "スフィアベース", "前世が見える。              （らしい）", { ore("mirageFairy2019SphereBase") }),
    FAIRY_SYRUP(17, 5, "fairy_syrup", false, "fairySyrup", "Fairy Syrup", "妖精のシロップ", "爽やかで心が洗われるような「水色」の香り", { ore("mirageFairySyrup").bottle() }),
    FAIRY_PLASTIC(18, 5, "fairy_plastic", false, "fairyPlastic", "Fairy Plastic", "妖精のプラスチック", "熱可塑性有機高分子", { ore("gemMirageFairyPlastic") }),
    FAIRY_PLASTIC_WITH_FAIRY(19, 5, "fairy_plastic_with_fairy", false, "fairyPlasticWithFairy", "Fairy Plastic with Fairy", "妖精入り妖精のプラスチック", "何を見てヨシ！って言ったんですか！？", { ore("gemMirageFairyPlasticWithFairy") }),
    FAIRY_PLASTIC_ROD(20, 5, "fairy_plastic_rod", true, "fairyPlasticRod", "Fairy Plastic Rod", "妖精のプラスチックの棒", "魔導性抜群、耐久性抜群、耐水性最悪", { ore("rodMirageFairyPlastic") }),
    INDIA_INK(21, 0, "india_ink", false, "indiaInk", "India Ink", "墨汁", "司書精はこれをコーヒーの代わりに飲んだらしい", { ore("dyeBlack").bottle() }),
    ANCIENT_POTTERY(22, 5, "ancient_pottery", false, "ancientPottery", "Ancient Pottery", "古代の壺", "「煮る」という発明", { ore("mirageFairyAncientPottery") }),
    ANNIHILATION_POTTERY(23, 5, "annihilation_pottery", false, "annihilationPottery", "Annihilation Pottery", "渇きの壺", "無限の水を吸い込む壺", { ore("mirageFairyAnnihilationPottery") }),
    ;

    val oreNames = mutableListOf<String>()
    var burnTime: Int? = null
    var containerItemSupplier: (() -> ItemStack)? = null

    init {
        initializer()
    }
}

private fun FairyMaterialCard.ore(oreName: String) = apply { this.oreNames += oreName }
private fun FairyMaterialCard.fuel(burnTime: Int) = apply { this.burnTime = burnTime }
private fun FairyMaterialCard.bottle() = apply { this.containerItemSupplier = { ItemStack(Items.GLASS_BOTTLE) } }


object FairyMaterials {
    lateinit var itemFairyMaterials: () -> ItemMultiFairyMaterial
    val fairyMaterialsModule = module {

        // アイテム登録
        itemFairyMaterials = item({ ItemMultiFairyMaterial() }, "fairy_materials") {
            setUnlocalizedName("fairyMaterials")
            setCreativeTab { Main.creativeTab }
            FairyMaterialCard.values().forEach { fairyMaterialCard ->
                itemVariant(fairyMaterialCard.registryName, { ItemVariantFairyMaterial(registryName = it, fairyMaterialCard = fairyMaterialCard) }, fairyMaterialCard.metadata) {
                    fairyMaterialCard.oreNames.forEach {
                        addOreName(it)
                    }
                }
            }
            onRegisterItem {
                if (Main.side.isClient) item.setCustomModelResourceLocations()
            }
        }

        // 種類ごと
        FairyMaterialCard.values().forEach { fairyMaterialCard ->

            // アイテムモデル生成
            makeItemModel(fairyMaterialCard.registryName) { if (fairyMaterialCard.isHandheld) handheld else generated }

            // 翻訳生成
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
                it.conditions += FairyStickCraftConditionSpawnItem { itemFairyMaterials[FairyMaterialCard.QUARTZ_MANA_ROD].createItemStack() }
            })

            // ミラ葉＋骨＋燐灰石→ミラ茎
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
                it.conditions += FairyStickCraftConditionUseItem(OreIngredient("mirageFairyStick"))
                it.conditions += FairyStickCraftConditionConsumeBlock { FluidMaterials.blockFluidMiragiumWater().defaultState }
                it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("mirageFairy2019FairyAbilityCrystal"))
                it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("leafMirageFlower"))
                it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("dustApatite"))
                it.conditions += FairyStickCraftConditionSpawnItem { itemFairyMaterials[FairyMaterialCard.MIRAGE_FLOWER_STICK].createItemStack() }
            })

            // 空き瓶＋ミラ葉64個＞破砕→ミラエキス瓶
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
                it.conditions += FairyStickCraftConditionUseItem(WandType.BREAKING.ingredient)
                it.conditions += FairyStickCraftConditionConsumeItem(Ingredient.fromItem(Items.GLASS_BOTTLE))
                it.conditions += FairyStickCraftConditionConsumeItem(itemFairyMaterials[FairyMaterialCard.MIRAGE_FLOWER_LEAF].createItemStack().ingredient, 64)
                it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("dustApatite"), 5)
                it.conditions += FairyStickCraftConditionSpawnItem { itemFairyMaterials[FairyMaterialCard.MIRAGE_FLOWER_EXTRACT_BOTTLE].createItemStack() }
            })

            // 空き瓶＋ミラ葉50個＞珠玉→ミラエキス瓶
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
                it.conditions += FairyStickCraftConditionUseItem(WandType.POLISHING.ingredient)
                it.conditions += FairyStickCraftConditionConsumeItem(Ingredient.fromItem(Items.GLASS_BOTTLE))
                it.conditions += FairyStickCraftConditionConsumeItem(itemFairyMaterials[FairyMaterialCard.MIRAGE_FLOWER_LEAF].createItemStack().ingredient, 50)
                it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("dustApatite"), 4)
                it.conditions += FairyStickCraftConditionSpawnItem { itemFairyMaterials[FairyMaterialCard.MIRAGE_FLOWER_EXTRACT_BOTTLE].createItemStack() }
            })

            // 空き瓶＋ミラ葉40個＞歪曲→ミラエキス瓶
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
                it.conditions += FairyStickCraftConditionUseItem(WandType.DISTORTION.ingredient)
                it.conditions += FairyStickCraftConditionConsumeItem(Ingredient.fromItem(Items.GLASS_BOTTLE))
                it.conditions += FairyStickCraftConditionConsumeItem(itemFairyMaterials[FairyMaterialCard.MIRAGE_FLOWER_LEAF].createItemStack().ingredient, 40)
                it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("dustApatite"), 3)
                it.conditions += FairyStickCraftConditionSpawnItem { itemFairyMaterials[FairyMaterialCard.MIRAGE_FLOWER_EXTRACT_BOTTLE].createItemStack() }
            })

            // ミラの葉30＋燐灰石の粉2＋空き瓶＞錬金の家→ミラエキス瓶
            fairyCentrifugeCraftHandler(30.0) {
                process { !Mana.GAIA + !Erg.DESTROY * 2.0 }
                process { !Mana.GAIA + !Erg.FLAME * 2.0 }
                process { !Mana.WIND + !Erg.CHEMICAL * 2.0 }
                input(itemFairyMaterials[FairyMaterialCard.MIRAGE_FLOWER_LEAF].createItemStack().ingredient, 30)
                input("dustApatite".oreIngredient, 2)
                input(Items.GLASS_BOTTLE.createItemStack().ingredient, 1)
                output(itemFairyMaterials[FairyMaterialCard.MIRAGE_FLOWER_EXTRACT_BOTTLE].createItemStack(), 1.0)
            }

            // 空き瓶＋ミラ種50個＋辰砂の粉4個＞珠玉→ミラオイル瓶
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
                it.conditions += FairyStickCraftConditionUseItem(WandType.POLISHING.ingredient)
                it.conditions += FairyStickCraftConditionConsumeItem(Ingredient.fromItem(Items.GLASS_BOTTLE))
                it.conditions += FairyStickCraftConditionConsumeItem(Ingredient.fromItem(MirageFlower.itemMirageFlowerSeeds()), 50)
                it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("dustCinnabar"), 4)
                it.conditions += FairyStickCraftConditionSpawnItem { itemFairyMaterials[FairyMaterialCard.MIRAGE_FLOWER_OIL_BOTTLE].createItemStack() }
            })

            // 空き瓶＋ミラ種50個＋辰砂の粉4個＞歪曲→ミラオイル瓶
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
                it.conditions += FairyStickCraftConditionUseItem(WandType.DISTORTION.ingredient)
                it.conditions += FairyStickCraftConditionConsumeItem(Ingredient.fromItem(Items.GLASS_BOTTLE))
                it.conditions += FairyStickCraftConditionConsumeItem(Ingredient.fromItem(MirageFlower.itemMirageFlowerSeeds()), 40)
                it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("dustCinnabar"), 3)
                it.conditions += FairyStickCraftConditionSpawnItem { itemFairyMaterials[FairyMaterialCard.MIRAGE_FLOWER_OIL_BOTTLE].createItemStack() }
            })

            // ミラの種30＋辰砂の粉2＋空き瓶＞錬金の家→ミラオイル瓶
            fairyCentrifugeCraftHandler(30.0) {
                process { !Mana.GAIA + !Erg.DESTROY * 2.0 }
                process { !Mana.GAIA + !Erg.FLAME * 2.0 }
                process { !Mana.WIND + !Erg.CHEMICAL * 2.0 }
                input(MirageFlower.itemMirageFlowerSeeds().createItemStack().ingredient, 30)
                input("dustCinnabar".oreIngredient, 2)
                input(Items.GLASS_BOTTLE.createItemStack().ingredient, 1)
                output(itemFairyMaterials[FairyMaterialCard.MIRAGE_FLOWER_OIL_BOTTLE].createItemStack(), 1.0)
            }

        }

    }
}


// ItemMultiMaterial

class ItemVariantFairyMaterial(registryName: String, val fairyMaterialCard: FairyMaterialCard) : ItemVariantMaterial(registryName, fairyMaterialCard.unlocalizedName)

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
        tooltip += formattedText { "Tier ${variant.fairyMaterialCard.tier}"().aqua }

        // Container Item
        if (flag.isAdvanced) {
            getContainerItemStack(itemStack)?.let { containerItemStack ->
                tooltip += formattedText { ("Has "() + containerItemStack.displayName()).green } // TODO translation
            }
        }

    }


    override fun getItemBurnTime(itemStack: ItemStack) = getVariant(itemStack)?.fairyMaterialCard?.burnTime ?: -1


    private fun getContainerItemStack(itemStack: ItemStack) = getVariant(itemStack)?.fairyMaterialCard?.containerItemSupplier?.invoke()?.notEmptyOrNull
    override fun hasContainerItem(itemStack: ItemStack) = getContainerItemStack(itemStack) != null
    override fun getContainerItem(itemStack: ItemStack) = getContainerItemStack(itemStack) ?: EMPTY_ITEM_STACK
}

operator fun (() -> ItemMultiFairyMaterial).get(fairyMaterialCard: FairyMaterialCard) = this().getVariant(fairyMaterialCard.metadata)!!
