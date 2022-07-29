package miragefairy2019.mod.fairyweapon

import miragefairy2019.api.Erg
import miragefairy2019.api.Erg.ATTACK
import miragefairy2019.api.Erg.CHRISTMAS
import miragefairy2019.api.Erg.CRYSTAL
import miragefairy2019.api.Erg.DESTROY
import miragefairy2019.api.Erg.ENERGY
import miragefairy2019.api.Erg.HARVEST
import miragefairy2019.api.Erg.KNOWLEDGE
import miragefairy2019.api.Erg.LIFE
import miragefairy2019.api.Erg.LIGHT
import miragefairy2019.api.Erg.SLASH
import miragefairy2019.api.Erg.SOUND
import miragefairy2019.api.Erg.SPACE
import miragefairy2019.api.Erg.SUBMISSION
import miragefairy2019.api.Erg.THUNDER
import miragefairy2019.api.Erg.WARP
import miragefairy2019.api.Erg.WATER
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.handheld
import miragefairy2019.lib.resourcemaker.makeAdvancement
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.BakedModelBuiltinWrapper
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.ingredient
import miragefairy2019.libkt.oreIngredient
import miragefairy2019.mod.Main.creativeTab
import miragefairy2019.mod.Main.side
import miragefairy2019.mod.artifacts.WandType
import miragefairy2019.mod.artifacts.ingredientData
import miragefairy2019.mod.artifacts.oreName
import miragefairy2019.mod.artifacts.sphereType
import miragefairy2019.mod.fairyweapon.items.ItemBellBase
import miragefairy2019.mod.fairyweapon.items.ItemChargingRod
import miragefairy2019.mod.fairyweapon.items.ItemChristmasBell
import miragefairy2019.mod.fairyweapon.items.ItemCollectingMagicWand
import miragefairy2019.mod.fairyweapon.items.ItemCrystalSword
import miragefairy2019.mod.fairyweapon.items.ItemFairySword
import miragefairy2019.mod.fairyweapon.items.ItemFairyWeapon
import miragefairy2019.mod.fairyweapon.items.ItemFlowerPickingBell
import miragefairy2019.mod.fairyweapon.items.ItemGravityRod
import miragefairy2019.mod.fairyweapon.items.ItemLightMagicWand
import miragefairy2019.mod.fairyweapon.items.ItemLightningMagicWand
import miragefairy2019.mod.fairyweapon.items.ItemMagicWandBase
import miragefairy2019.mod.fairyweapon.items.ItemMiragiumAxe
import miragefairy2019.mod.fairyweapon.items.ItemMiragiumScythe
import miragefairy2019.mod.fairyweapon.items.ItemPrayerWheel
import miragefairy2019.mod.fairyweapon.items.ItemRyugyoDrill
import miragefairy2019.mod.fairyweapon.items.ItemTemptationOcarina
import mirrg.kotlin.gson.hydrogen.jsonArray
import mirrg.kotlin.gson.hydrogen.jsonElement
import mirrg.kotlin.gson.hydrogen.jsonObject
import mirrg.kotlin.gson.hydrogen.jsonObjectNotNull
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.init.Items
import net.minecraft.item.crafting.Ingredient
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class LangPair(val english: String, val japanese: String)

private operator fun Erg.not(): () -> Ingredient = { this.sphereType.oreName.oreIngredient }
private operator fun String.not(): () -> Ingredient = { this.oreIngredient }

@Suppress("EnumEntryName")
enum class FairyWeaponKind(
    val parent: FairyWeaponKind?,
    val registryName: String,
    val unlocalizedName: String,
    val tier: Int,
    val itemCreator: () -> ItemFairyWeapon,
    val displayName: LangPair,
    val poem: LangPair,
    val author: LangPair?,
    val advancementText: LangPair,
    val frame: String?,
    val ownManualRepairIngredientSuppliers: List<() -> Ingredient>,
    val recipeDataSupplier: () -> DataShapedRecipe
) {
    miragiumSword(
        null, "miragium_sword", "miragiumSword", 2, { ItemFairyWeapon() },
        LangPair("Miragium Sword", "ミラジウムの剣"),
        LangPair("", "その刃で何を切る？"),
        null,
        LangPair("Get a specific item", "ミラジウムは軟らかいので刃物には向かない"),
        null,
        listOf(!ATTACK, !SLASH, !"plateMiragium"),
        {
            DataShapedRecipe(
                pattern = listOf(
                    "sP ",
                    "aPm",
                    "cR "
                ),
                key = mapOf(
                    "R" to DataOreIngredient(ore = "stickWood"),
                    "P" to DataOreIngredient(ore = "plateMiragium"),
                    "s" to DataOreIngredient(ore = "mirageFairy2019SphereSlash"),
                    "a" to DataOreIngredient(ore = "mirageFairy2019SphereAttack"),
                    "c" to WandType.CRAFTING.ingredientData,
                    "m" to WandType.MELTING.ingredientData
                ),
                result = DataResult(item = "miragefairy2019:miragium_sword")
            )
        }
    ),
    crystalSword(
        miragiumSword, "crystal_sword", "crystalSword", 3, { ItemCrystalSword(0.0, 5.0, 1.0) },
        LangPair("Crystal Sword", "クリスタルソード"),
        LangPair("", "妖精はこれをおやつにするという"),
        null,
        LangPair("Get a specific item", "金属質よりも非晶質の方が鋭利だ、って"),
        null,
        listOf(!CRYSTAL, !"mirageFairyCrystal"),
        {
            DataShapedRecipe(
                pattern = listOf(
                    "cCC",
                    "S#C",
                    "Rmp"
                ),
                key = mapOf(
                    "#" to DataSimpleIngredient(item = "miragefairy2019:miragium_sword"),
                    "R" to DataOreIngredient(ore = "rodMiragium"),
                    "S" to DataOreIngredient(ore = "mirageFairy2019SphereCrystal"),
                    "C" to DataOreIngredient(ore = "mirageFairyCrystal"),
                    "c" to WandType.CRAFTING.ingredientData,
                    "p" to WandType.POLISHING.ingredientData,
                    "m" to WandType.MELTING.ingredientData
                ),
                result = DataResult(item = "miragefairy2019:crystal_sword")
            )
        }
    ),
    plasticSword(
        crystalSword, "plastic_sword", "plasticSword", 5, { ItemCrystalSword(1.0, 6.0, 1.2) },
        LangPair("Plastic Sword", "プラスチックソード"),
        LangPair("", "これは妖精をおやつにするという"),
        null,
        LangPair("Get a specific item", "17億年のチカラ"),
        "challenge",
        listOf(!SUBMISSION, !SUBMISSION, !"gemMirageFairyPlastic"),
        {
            DataShapedRecipe(
                pattern = listOf(
                    "sGF",
                    "c#G",
                    "Rcs"
                ),
                key = mapOf(
                    "#" to DataSimpleIngredient(item = "miragefairy2019:crystal_sword"),
                    "s" to DataOreIngredient(ore = "mirageFairy2019SphereSubmission"),
                    "c" to DataOreIngredient(ore = "dustCinnabar"),
                    "R" to DataOreIngredient(ore = "rodMirageFairyPlastic"),
                    "G" to DataOreIngredient(ore = "gemMirageFairyPlastic"),
                    "F" to DataOreIngredient(ore = "gemMirageFairyPlasticWithFairy")
                ),
                result = DataResult(item = "miragefairy2019:plastic_sword")
            )
        }
    ),
    fairySword(
        miragiumSword, "fairy_sword", "fairySword", 3, { ItemFairySword() },
        LangPair("Fairy Sword", "妖精剣"),
        LangPair("Design Contest Work", "デザインコンテスト武器"),
        LangPair("tanun3sei", "たぬん三世"),
        LangPair("Get a specific item", "デザインコンテスト武器"),
        "goal",
        listOf(!ATTACK),
        {
            DataShapedRecipe(
                pattern = listOf(
                    "CaF",
                    "S#A",
                    "MpO"
                ),
                key = mapOf(
                    "#" to DataSimpleIngredient(item = "miragefairy2019:miragium_sword"),
                    "C" to DataOreIngredient(ore = "gemCinnabar"),
                    "S" to DataOreIngredient(ore = "gemSulfur"),
                    "M" to DataOreIngredient(ore = "gemMagnetite"),
                    "F" to DataOreIngredient(ore = "gemFluorite"),
                    "A" to DataOreIngredient(ore = "gemApatite"),
                    "O" to DataOreIngredient(ore = "gemMoonstone"),
                    "a" to DataOreIngredient(ore = "mirageFairy2019SphereAttack"),
                    "p" to WandType.POLISHING.ingredientData
                ),
                result = DataResult(item = "miragefairy2019:fairy_sword")
            )
        }
    ),

    miragiumAxe(
        null, "miragium_axe", "miragiumAxe", 2, { ItemMiragiumAxe() },
        LangPair("Miragium Axe", "ミラジウムの斧"),
        LangPair("", "飛べるって素敵"),
        null,
        LangPair("Get a specific item", "切断のエルグを真空波にする構造"),
        null,
        listOf(!SLASH, !HARVEST, !"plateMiragium"),
        {
            DataShapedRecipe(
                pattern = listOf(
                    "sPI",
                    "fPR",
                    "cmR"
                ),
                key = mapOf(
                    "R" to DataOreIngredient(ore = "stickWood"),
                    "I" to DataOreIngredient(ore = "ingotMiragium"),
                    "P" to DataOreIngredient(ore = "plateMiragium"),
                    "s" to DataOreIngredient(ore = "mirageFairy2019SphereSlash"),
                    "f" to DataOreIngredient(ore = "mirageFairy2019SphereHarvest"),
                    "c" to WandType.CRAFTING.ingredientData,
                    "m" to WandType.MELTING.ingredientData
                ),
                result = DataResult(item = "miragefairy2019:miragium_axe")
            )
        }
    ),

    magicWandBase(
        null, "magic_wand_base", "magicWandBase", 3, { ItemMagicWandBase() },
        LangPair("Magic Wand Base", "ロッドベース"),
        LangPair("", "風の心、探求"),
        null,
        LangPair("Get a specific item", "妖精→持ち手→魔導芯棒→スフィア→対象物"),
        null,
        listOf(!KNOWLEDGE, !"ingotMiragium", !"gemFluorite"),
        {
            DataShapedRecipe(
                pattern = listOf(
                    "cIk",
                    "IRI",
                    "RIm"
                ),
                key = mapOf(
                    "R" to DataOreIngredient(ore = "mirageFairy2019ManaRodWind"),
                    "I" to DataOreIngredient(ore = "ingotMiragium"),
                    "k" to DataOreIngredient(ore = "mirageFairy2019SphereKnowledge"),
                    "c" to WandType.CRAFTING.ingredientData,
                    "m" to WandType.MELTING.ingredientData
                ),
                result = DataResult(item = "miragefairy2019:magic_wand_base")
            )
        }
    ),
    lightMagicWand(
        magicWandBase, "light_magic_wand", "lightMagicWand", 3, { ItemLightMagicWand() },
        LangPair("Light Magic Wand", "光のロッド"),
        LangPair("", "古代の魔法「ニクトフォビア」、優しい光が洞窟を照らす"),
        null,
        LangPair("Get a specific item", "パラボラの焦点に発光のスフィア"),
        "goal",
        listOf(!LIGHT, !"gemApatite"),
        {
            DataShapedRecipe(
                pattern = listOf(
                    "cal",
                    " #a",
                    "a p"
                ),
                key = mapOf(
                    "#" to DataSimpleIngredient(item = "miragefairy2019:magic_wand_base"),
                    "a" to DataOreIngredient(ore = "gemApatite"),
                    "l" to DataOreIngredient(ore = "mirageFairy2019SphereLight"),
                    "c" to WandType.CRAFTING.ingredientData,
                    "p" to WandType.POLISHING.ingredientData
                ),
                result = DataResult(item = "miragefairy2019:light_magic_wand")
            )
        }
    ),
    collectingMagicWand(
        magicWandBase, "collecting_magic_wand", "collectingMagicWand", 3, { ItemCollectingMagicWand() },
        LangPair("Collecting Magic Wand", "収集のロッド"),
        LangPair("", "新開発の魔法「ソルメローシェ・トリーパ」、魔法のマジックハンド"),
        null,
        LangPair("Get a specific item", "縮地のエルグが渦を巻いて収束するように"),
        "goal",
        listOf(!WARP, !"obsidian"),
        {
            DataShapedRecipe(
                pattern = listOf(
                    "cow",
                    " #o",
                    "f p"
                ),
                key = mapOf(
                    "#" to DataSimpleIngredient(item = "miragefairy2019:magic_wand_base"),
                    "o" to DataOreIngredient(ore = "obsidian"),
                    "f" to DataOreIngredient(ore = "gemFluorite"),
                    "w" to DataOreIngredient(ore = "mirageFairy2019SphereWarp"),
                    "c" to WandType.CRAFTING.ingredientData,
                    "p" to WandType.POLISHING.ingredientData
                ),
                result = DataResult(item = "miragefairy2019:collecting_magic_wand")
            )
        }
    ),
    chargingRod(
        magicWandBase, "charging_rod", "chargingRod", 3, { ItemChargingRod() },
        LangPair("Charging Rod", "チャージングロッド"),
        LangPair("", "電気の力で栄えた文明があったという"),
        null,
        LangPair("Get a specific item", "カミナリのエルグなんか集めて何に使うんだろう？"),
        null,
        listOf(!THUNDER, !WARP, !"ingotGold"),
        {
            DataShapedRecipe(
                pattern = listOf(
                    "cgw",
                    "g#g",
                    "tgm"
                ),
                key = mapOf(
                    "#" to DataSimpleIngredient(item = "miragefairy2019:magic_wand_base"),
                    "g" to DataOreIngredient(ore = "ingotGold"),
                    "w" to DataOreIngredient(ore = "mirageFairy2019SphereWarp"),
                    "t" to DataOreIngredient(ore = "mirageFairy2019SphereThunder"),
                    "c" to WandType.CRAFTING.ingredientData,
                    "m" to WandType.MELTING.ingredientData
                ),
                result = DataResult(item = "miragefairy2019:charging_rod")
            )
        }
    ),
    lightningMagicWand(
        chargingRod, "lightning_magic_wand", "lightningMagicWand", 3, { ItemLightningMagicWand() },
        LangPair("Lightning Magic Wand", "ライトニングロッド"),
        LangPair("", "古代魔法「ライトニングボルト」"),
        null,
        LangPair("Get a specific item", "雷電のエルグは金属の中を伝うことが知られている"),
        "goal",
        listOf(!ENERGY, !"blockMirageFairyCrystalPure"),
        {
            DataShapedRecipe(
                pattern = listOf(
                    "gge",
                    "c#g",
                    "Gmg"
                ),
                key = mapOf(
                    "#" to DataSimpleIngredient(item = "miragefairy2019:charging_rod"),
                    "g" to DataOreIngredient(ore = "ingotGold"),
                    "e" to DataOreIngredient(ore = "mirageFairy2019SphereEnergy"),
                    "c" to WandType.CRAFTING.ingredientData,
                    "m" to WandType.MELTING.ingredientData,
                    "G" to DataOreIngredient(ore = "blockMirageFairyCrystalPure")
                ),
                result = DataResult(item = "miragefairy2019:lightning_magic_wand")
            )
        }
    ),

    gravityRod(
        null, "gravity_rod", "gravityRod", 4, { ItemGravityRod() },
        LangPair("Gravity Rod", "グラビティロッド"),
        LangPair("", "局所エーテル超球面歪曲技術"),
        null,
        LangPair("Get a specific item", "ミラージュオイルで物体と空間を接着するのだ！"),
        "goal",
        listOf(!KNOWLEDGE, !SPACE, !"gemCinnabar", !"obsidian"),
        {
            DataShapedRecipe(
                pattern = listOf(
                    "cOs",
                    "oRO",
                    "kof"
                ),
                key = mapOf(
                    "R" to DataOreIngredient(ore = "mirageFairy2019ManaRodFire"),
                    "k" to DataOreIngredient(ore = "mirageFairy2019SphereKnowledge"),
                    "s" to DataOreIngredient(ore = "mirageFairy2019SphereSpace"),
                    "o" to DataOreIngredient(ore = "obsidian"),
                    "O" to DataOreIngredient(ore = "container1000MirageFlowerOil"),
                    "c" to WandType.CRAFTING.ingredientData,
                    "f" to WandType.FUSION.ingredientData
                ),
                result = DataResult(item = "miragefairy2019:gravity_rod")
            )
        }
    ),

    ocarinaBase(
        null, "ocarina_base", "ocarinaBase", 3, { ItemFairyWeapon() },
        LangPair("Ocarina Base", "オカリナベース"),
        LangPair("", "適当に吹いても音楽になる笛"),
        null,
        LangPair("Get a specific item", "カラカラする？音の妖精の魂が入っている"),
        null,
        listOf(!SOUND, !"gemApatite"),
        {
            DataShapedRecipe(
                pattern = listOf(
                    "pGm",
                    "GSG",
                    "cGI"
                ),
                key = mapOf(
                    "G" to DataOreIngredient(ore = "gemApatite"),
                    "S" to DataOreIngredient(ore = "mirageFairy2019SphereSound"),
                    "I" to DataOreIngredient(ore = "ingotMiragium"),
                    "c" to WandType.CRAFTING.ingredientData,
                    "m" to WandType.MELTING.ingredientData,
                    "p" to WandType.POLISHING.ingredientData
                ),
                result = DataResult(item = "miragefairy2019:ocarina_base")
            )
        }
    ),
    temptationOcarina(
        ocarinaBase, "temptation_ocarina", "temptationOcarina", 3, { ItemTemptationOcarina() },
        LangPair("Ocarina of Temptation", "魅惑のオカリナ"),
        LangPair("", "その音は人の腹を満たし、淫靡な気分にさせる"),
        null,
        LangPair("Get a specific item", "生物は生命のエルグさえあれば増える"),
        "goal",
        listOf(!LIFE, !"gemPyrope"),
        {
            DataShapedRecipe(
                pattern = listOf(
                    "pGS",
                    "G#G",
                    "cG "
                ),
                key = mapOf(
                    "#" to DataSimpleIngredient(item = "miragefairy2019:ocarina_base"),
                    "G" to DataOreIngredient(ore = "gemPyrope"),
                    "S" to DataOreIngredient(ore = "mirageFairy2019SphereLife"),
                    "c" to WandType.CRAFTING.ingredientData,
                    "p" to WandType.POLISHING.ingredientData
                ),
                result = DataResult(item = "miragefairy2019:temptation_ocarina")
            )
        }
    ),

    bellBase(
        null, "bell_base", "bellBase", 2, { ItemBellBase() },
        LangPair("Bell Base", "鐘ベース"),
        LangPair("", "妖精の力を解放せよ"),
        null,
        LangPair("Get a specific item", "妖精よ、わずかの間、我に力を与えたまえ"),
        null,
        listOf(!SOUND, !"plateMiragium"),
        {
            DataShapedRecipe(
                pattern = listOf(
                    "cPs",
                    " RP",
                    "I m"
                ),
                key = mapOf(
                    "R" to DataOreIngredient(ore = "rodMiragium"),
                    "I" to DataOreIngredient(ore = "ingotMiragium"),
                    "P" to DataOreIngredient(ore = "plateMiragium"),
                    "s" to DataOreIngredient(ore = "mirageFairy2019SphereSound"),
                    "c" to WandType.CRAFTING.ingredientData,
                    "m" to WandType.MELTING.ingredientData
                ),
                result = DataResult(item = "miragefairy2019:bell_base")
            )
        }
    ),
    flowerPickingBell(
        bellBase, "flower_picking_bell", "flowerPickingBell", 2, { ItemFlowerPickingBell(3.0, 0.1) },
        LangPair("Flower Picking Bell", "花摘みの鐘"),
        LangPair("", "ちょっとお花を摘みに"),
        null,
        LangPair("Get a specific item", "リラジウムの音は草花の心に響くという"),
        null,
        listOf(!HARVEST, !"ingotLilagium", !"obsidian"),
        {
            DataShapedRecipe(
                pattern = listOf(
                    "cIh",
                    "o#I",
                    "gom"
                ),
                key = mapOf(
                    "#" to DataSimpleIngredient(item = "miragefairy2019:bell_base"),
                    "g" to DataOreIngredient(ore = "ingotGold"),
                    "o" to DataOreIngredient(ore = "obsidian"),
                    "I" to DataOreIngredient(ore = "ingotLilagium"),
                    "h" to DataOreIngredient(ore = "mirageFairy2019SphereHarvest"),
                    "c" to WandType.CRAFTING.ingredientData,
                    "m" to WandType.MELTING.ingredientData
                ),
                result = DataResult(item = "miragefairy2019:flower_picking_bell")
            )
        }
    ),
    flowerPickingBell2(
        flowerPickingBell, "flower_picking_bell_2", "flowerPickingBell2", 4, { ItemFlowerPickingBell(4.0, 1.0) },
        LangPair("Flower Picking Bell II", "花摘みの鐘 II"),
        LangPair("", "光輝のフェロモン"),
        null,
        LangPair("Get a specific item", "妖精の正体はミラージュの花粉、つまり花に魅かれる"),
        "goal",
        listOf(!HARVEST, !"gemPyrope", !"container250MirageFlowerOil"),
        {
            DataShapedRecipe(
                pattern = listOf(
                    "cGh",
                    "G#G",
                    "RGp"
                ),
                key = mapOf(
                    "#" to DataSimpleIngredient(item = "miragefairy2019:flower_picking_bell"),
                    "R" to DataOreIngredient(ore = "stickMirageFairyWood"),
                    "G" to DataOreIngredient(ore = "gemPyrope"),
                    "h" to DataOreIngredient(ore = "mirageFairy2019SphereHarvest"),
                    "c" to WandType.CRAFTING.ingredientData,
                    "p" to WandType.POLISHING.ingredientData
                ),
                result = DataResult(item = "miragefairy2019:flower_picking_bell_2")
            )
        }
    ),
    christmasBell(
        bellBase, "christmas_bell", "christmasBell", 3, { ItemChristmasBell() },
        LangPair("Christmas Bell", "クリスマスの鐘"),
        LangPair("", "いけない子には"),
        null,
        LangPair("Get a specific item", "金メッキする、輪っかを付ける、木の枝を付ける"),
        "challenge",
        listOf(!CHRISTMAS, !ATTACK, !"ingotGold", !"gemMagnetite"),
        {
            DataShapedRecipe(
                pattern = listOf(
                    "c1S",
                    "3#4",
                    "R2m"
                ),
                key = mapOf(
                    "#" to DataSimpleIngredient(item = "miragefairy2019:bell_base"),
                    "R" to DataOreIngredient(ore = "mirageFairy2019ManaRodDark"),
                    "1" to DataOreIngredient(ore = "blockGold"),
                    "2" to DataOreIngredient(ore = "ingotGold"),
                    "3" to DataSimpleIngredient(item = "minecraft:sapling", data = 1),
                    "S" to DataOreIngredient(ore = "mirageFairy2019SphereChristmas"),
                    "4" to DataOreIngredient(ore = "mirageFairy2019SphereAttack"),
                    "c" to WandType.CRAFTING.ingredientData,
                    "m" to WandType.MELTING.ingredientData
                ),
                result = DataResult(item = "miragefairy2019:christmas_bell")
            )
        }
    ),

    miragiumScythe(
        null, "miragium_scythe", "miragiumScythe", 2, { ItemMiragiumScythe(0.0, 2.0f) },
        LangPair("Miragium Scythe", "ミラジウムの大鎌"),
        LangPair("", "自分を切らないように！"),
        null,
        LangPair("Get a specific item", "作物を刈り奪る形をしてるだろ？"),
        null,
        listOf(!SLASH, !HARVEST, !"plateMiragium"),
        {
            DataShapedRecipe(
                pattern = listOf(
                    "fPI",
                    "PsR",
                    "mcR"
                ),
                key = mapOf(
                    "R" to DataOreIngredient(ore = "stickWood"),
                    "I" to DataOreIngredient(ore = "ingotMiragium"),
                    "P" to DataOreIngredient(ore = "plateMiragium"),
                    "s" to DataOreIngredient(ore = "mirageFairy2019SphereSlash"),
                    "f" to DataOreIngredient(ore = "mirageFairy2019SphereHarvest"),
                    "c" to WandType.CRAFTING.ingredientData,
                    "m" to WandType.MELTING.ingredientData
                ),
                result = DataResult(item = "miragefairy2019:miragium_scythe")
            )
        }
    ),
    lilagiumScythe(
        miragiumScythe, "lilagium_scythe", "lilagiumScythe", 3, { ItemMiragiumScythe(1.0, 4.0f) },
        LangPair("Lilagium Scythe", "リラジウムの大鎌"),
        LangPair("", "葉っぱが吸い込まれてくる"),
        null,
        LangPair("Get a specific item", "植物だって話せばわかる"),
        null,
        listOf(!HARVEST, !"ingotLilagium", !"gemSulfur"),
        {
            DataShapedRecipe(
                pattern = listOf(
                    "hII",
                    "I#R",
                    "mcR"
                ),
                key = mapOf(
                    "#" to DataSimpleIngredient(item = "miragefairy2019:miragium_scythe"),
                    "R" to DataOreIngredient(ore = "mirageFairy2019ManaRodGaia"),
                    "I" to DataOreIngredient(ore = "ingotLilagium"),
                    "h" to DataOreIngredient(ore = "mirageFairy2019SphereHarvest"),
                    "c" to WandType.CRAFTING.ingredientData,
                    "m" to WandType.MELTING.ingredientData
                ),
                result = DataResult(item = "miragefairy2019:lilagium_scythe")
            )
        }
    ),

    ryugyoDrill(
        null, "ryugyo_drill", "ryugyoDrill", 4, { ItemRyugyoDrill(2.0) },
        LangPair("Ryugyo Drill", "龍魚ドリル"),
        LangPair("Design Contest Work", "デザインコンテスト武器"),
        LangPair("Yoshinon", "よしのん"),
        LangPair("Get a specific item", "デザインコンテスト武器"),
        null,
        listOf(!DESTROY, !THUNDER, !WATER, !"mirageFairyLeather", { Items.BREAD.ingredient }),
        {
            DataShapedRecipe(
                pattern = listOf(
                    "cLL",
                    "b#L",
                    "twf"
                ),
                key = mapOf(
                    "#" to DataSimpleIngredient(item = "minecraft:fish", data = 2),
                    "L" to DataOreIngredient(ore = "mirageFairyLeather"),
                    "c" to WandType.CRAFTING.ingredientData,
                    "f" to WandType.FUSION.ingredientData,
                    "b" to DataOreIngredient(ore = "mirageFairy2019SphereDestroy"),
                    "t" to DataOreIngredient(ore = "mirageFairy2019SphereThunder"),
                    "w" to DataOreIngredient(ore = "mirageFairy2019SphereWater")
                ),
                result = DataResult(item = "miragefairy2019:ryugyo_drill")
            )
        }
    ),
    ryugyoDrill2(
        ryugyoDrill, "ryugyo_drill_2", "ryugyoDrill2", 5, { ItemRyugyoDrill(4.0) },
        LangPair("Ryugyo Drill II", "龍魚の一撃"),
        LangPair("Design Contest Work", "デザインコンテスト武器（派生作品）"),
        LangPair("Yoshinon", "よしのん（原作者）"),
        LangPair("Get a specific item", "デザインコンテスト武器（派生作品）"),
        "goal",
        listOf(!THUNDER, !"mirageFairyLeafVelopeda", !"blockPrismarineBrick"),
        {
            DataShapedRecipe(
                pattern = listOf(
                    " vv",
                    "p#v",
                    "tp "
                ),
                key = mapOf(
                    "#" to DataSimpleIngredient(item = "miragefairy2019:ryugyo_drill"),
                    "v" to DataOreIngredient(ore = "mirageFairyLeafVelopeda"),
                    "t" to DataOreIngredient(ore = "mirageFairy2019SphereThunder"),
                    "p" to DataOreIngredient(ore = "blockPrismarineBrick")
                ),
                result = DataResult(item = "miragefairy2019:ryugyo_drill_2")
            )
        }
    ),

    prayerWheel(
        null, "prayer_wheel", "prayerWheel", 1, { ItemPrayerWheel(0.0, 1) },
        LangPair("Prayer Wheel", "収束の地"),
        LangPair("", "重合するアストラル光。魂の叫び。"),
        null,
        LangPair("Get a specific item", "ホイールの外側に妖精語の呪文なんか書いて読めるの？"),
        null,
        listOf(!SUBMISSION, !SUBMISSION, !"ingotIron", !"gemDiamond"),
        {
            DataShapedRecipe(
                pattern = listOf(
                    "JCJ",
                    "SRS",
                    "sRs"
                ),
                key = mapOf(
                    "R" to DataOreIngredient(ore = "stickWood"),
                    "C" to DataSimpleIngredient(item = "minecraft:compass"),
                    "J" to DataSimpleIngredient(item = "minecraft:jukebox"),
                    "S" to DataOreIngredient(ore = "string"),
                    "s" to DataOreIngredient(ore = "mirageFairy2019SphereSubmission")
                ),
                result = DataResult(item = "miragefairy2019:prayer_wheel")
            )
        }
    ),
    prayerWheel2(
        prayerWheel, "prayer_wheel_2", "prayerWheel2", 3, { ItemPrayerWheel(0.2, 5) },
        LangPair("Prayer Wheel II", "約束の地"),
        LangPair("", "吹き荒れる妖精の声、宇宙のジェット"),
        null,
        LangPair("Get a specific item", "人は死ぬと妖精になるんだって"),
        null,
        listOf(!"ingotGold", !"dustCinnabar"),
        {
            DataShapedRecipe(
                pattern = listOf(
                    "crC",
                    " #r",
                    "R s"
                ),
                key = mapOf(
                    "#" to DataSimpleIngredient(item = "miragefairy2019:prayer_wheel"),
                    "R" to DataOreIngredient(ore = "mirageFairy2019ManaRodShine"),
                    "C" to DataSimpleIngredient(item = "minecraft:clock"),
                    "r" to DataOreIngredient(ore = "dustCinnabar"),
                    "c" to WandType.CRAFTING.ingredientData,
                    "s" to WandType.SUMMONING.ingredientData
                ),
                result = DataResult(item = "miragefairy2019:prayer_wheel_2")
            )
        }
    ),
    prayerWheel3(
        prayerWheel2, "prayer_wheel_3", "prayerWheel3", 5, { ItemPrayerWheel(0.4, 10) },
        LangPair("Prayer Wheel III", "束縛の地"),
        LangPair("", "覚えてる？前世、自分が何の妖精だったか"),
        null,
        LangPair("Get a specific item", "人、牛、妖精、ゾンビ、植物、土"),
        "challenge",
        listOf(!"gemMirageFairyPlastic", !"gemMirageFairyPlastic"),
        {
            DataShapedRecipe(
                pattern = listOf(
                    " pf",
                    "c#p",
                    "Rc "
                ),
                key = mapOf(
                    "#" to DataSimpleIngredient(item = "miragefairy2019:prayer_wheel_2"),
                    "R" to DataOreIngredient(ore = "rodMirageFairyPlastic"),
                    "p" to DataOreIngredient(ore = "gemMirageFairyPlastic"),
                    "f" to DataOreIngredient(ore = "gemMirageFairyPlasticWithFairy"),
                    "c" to DataOreIngredient(ore = "dustCoal")
                ),
                result = DataResult(item = "miragefairy2019:prayer_wheel_3")
            )
        }
    ),
}


val FairyWeaponKind.manualRepairIngredients: List<Ingredient> get() = if (parent != null) parent.manualRepairIngredients + ownManualRepairIngredientSuppliers.map { it() } else ownManualRepairIngredientSuppliers.map { it() }

val fairyWeaponModule = module {

    // 翻訳生成
    onMakeLang {
        enJa("advancements.miragefairy2019.fairy_weapon.root.title", "Fairy Weapon", "妖精武器")
        enJa("advancements.miragefairy2019.fairy_weapon.root.description", "Fairy Weapon", "妖精の力を何かに使えないだろうか")
    }

    // 実績生成
    makeAdvancement("fairy_weapon/root") {
        jsonObject(
            "display" to jsonObjectNotNull(
                "icon" to jsonObject(
                    "item" to "miragefairy2019:miragium_sword".jsonElement
                ),
                "title" to jsonObject(
                    "translate" to "advancements.miragefairy2019.fairy_weapon.root.title".jsonElement
                ),
                "description" to jsonObject(
                    "translate" to "advancements.miragefairy2019.fairy_weapon.root.description".jsonElement
                ),
                "background" to "miragefairy2019:textures/blocks/magnetite_block.png".jsonElement
            ),
            "criteria" to jsonObject(
                "main" to jsonObject(
                    "trigger" to "minecraft:inventory_changed".jsonElement,
                    "conditions" to jsonObject(
                        "items" to jsonArray(
                            jsonObject(
                                "type" to "forge:ore_dict".jsonElement,
                                "ore" to "mirageFairy2019SphereAny".jsonElement
                            )
                        )
                    )
                )
            )
        )
    }

    // 個別
    FairyWeaponKind.values().forEach { fairyWeaponKind ->

        // アイテム登録
        item(fairyWeaponKind.itemCreator, fairyWeaponKind.registryName) {
            setUnlocalizedName(fairyWeaponKind.unlocalizedName)
            setCreativeTab { creativeTab }
            onRegisterItem {
                if (side.isClient) {
                    MinecraftForge.EVENT_BUS.register(object : Any() {
                        @SubscribeEvent
                        fun accept(event: ModelBakeEvent) {
                            val modelResourceLocation = ModelResourceLocation(item.registryName!!, "normal")
                            event.modelRegistry.putObject(modelResourceLocation, BakedModelBuiltinWrapper(event.modelRegistry.getObject(modelResourceLocation)!!))
                        }
                    })
                }
            }
            setCustomModelResourceLocation(variant = "normal")
            onInit {
                val durability = (1..fairyWeaponKind.tier).fold(16) { a, _ -> a * 2 }
                item.maxDamage = durability - 1
                item.tier = fairyWeaponKind.tier
            }
            onCreateItemStack {
                item.manualRepairRequirements += fairyWeaponKind.manualRepairIngredients
            }
        }

        // アイテムモデル生成
        makeItemModel(fairyWeaponKind.registryName) { handheld }

        // 翻訳生成
        onMakeLang {
            enJa("item.${fairyWeaponKind.unlocalizedName}.name", fairyWeaponKind.displayName.english, fairyWeaponKind.displayName.japanese)
            enJa("item.${fairyWeaponKind.unlocalizedName}.poem", fairyWeaponKind.poem.english, fairyWeaponKind.poem.japanese)
            if (fairyWeaponKind.author != null) enJa("item.${fairyWeaponKind.unlocalizedName}.author", fairyWeaponKind.author.english, fairyWeaponKind.author.japanese)
            enJa("item.${fairyWeaponKind.unlocalizedName}.recipe", fairyWeaponKind.advancementText.english, fairyWeaponKind.advancementText.japanese)
        }

        // レシピ生成
        makeRecipe("fairy_weapon/${fairyWeaponKind.registryName}") {
            fairyWeaponKind.recipeDataSupplier()
        }

        // 実績生成
        makeAdvancement("fairy_weapon/${fairyWeaponKind.registryName}") {
            jsonObject(
                "display" to jsonObjectNotNull(
                    "icon" to jsonObject(
                        "item" to "miragefairy2019:${fairyWeaponKind.registryName}".jsonElement
                    ),
                    "title" to jsonObject(
                        "translate" to "item.${fairyWeaponKind.unlocalizedName}.name".jsonElement
                    ),
                    "description" to jsonObject(
                        "translate" to "item.${fairyWeaponKind.unlocalizedName}.recipe".jsonElement
                    ),
                    fairyWeaponKind.frame?.let { "frame" to it.jsonElement }
                ),
                "parent" to "miragefairy2019:fairy_weapon/${fairyWeaponKind.parent?.registryName ?: "root"}".jsonElement,
                "criteria" to jsonObject(
                    "main" to jsonObject(
                        "trigger" to "minecraft:inventory_changed".jsonElement,
                        "conditions" to jsonObject(
                            "items" to jsonArray(
                                jsonObject(
                                    "item" to "miragefairy2019:${fairyWeaponKind.registryName}".jsonElement
                                )
                            )
                        )
                    )
                )
            )
        }

    }

    magicMessageModule()
}
