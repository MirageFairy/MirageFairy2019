package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.libkt.DataOreIngredient
import miragefairy2019.libkt.DataResult
import miragefairy2019.libkt.DataShapedRecipe
import miragefairy2019.libkt.DataSimpleIngredient
import miragefairy2019.libkt.ModInitializer
import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.item
import miragefairy2019.libkt.makeRecipe
import miragefairy2019.libkt.module
import miragefairy2019.libkt.oreIngredient
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.lib.BakedModelBuiltinWrapper
import miragefairy2019.mod.modules.fairyweapon.recipe.RecipesCombining
import miragefairy2019.mod.modules.fairyweapon.recipe.RecipesUncombining
import miragefairy2019.mod3.artifacts.oreName
import miragefairy2019.mod3.artifacts.sphereType
import miragefairy2019.mod3.erg.api.EnumErgType
import miragefairy2019.mod3.erg.api.EnumErgType.ATTACK
import miragefairy2019.mod3.erg.api.EnumErgType.CHRISTMAS
import miragefairy2019.mod3.erg.api.EnumErgType.CRYSTAL
import miragefairy2019.mod3.erg.api.EnumErgType.DESTROY
import miragefairy2019.mod3.erg.api.EnumErgType.ENERGY
import miragefairy2019.mod3.erg.api.EnumErgType.HARVEST
import miragefairy2019.mod3.erg.api.EnumErgType.KNOWLEDGE
import miragefairy2019.mod3.erg.api.EnumErgType.LIFE
import miragefairy2019.mod3.erg.api.EnumErgType.LIGHT
import miragefairy2019.mod3.erg.api.EnumErgType.SLASH
import miragefairy2019.mod3.erg.api.EnumErgType.SOUND
import miragefairy2019.mod3.erg.api.EnumErgType.THUNDER
import miragefairy2019.mod3.erg.api.EnumErgType.WARP
import miragefairy2019.mod3.erg.api.EnumErgType.WATER
import miragefairy2019.mod3.main.api.ApiMain.creativeTab
import miragefairy2019.mod3.main.api.ApiMain.side
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.Ingredient
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.OreDictionary

object FairyWeapon {
    @Suppress("UNUSED_VARIABLE")
    val module = module {

        // 妖精武器

        fun <T : ItemFairyWeapon> ModInitializer.fw(
            tier: Int,
            creator: () -> T,
            registryName: String,
            unlocalizedName: String,
            oreNameList: List<String>,
            parent: (() -> ItemFairyWeapon)?,
            vararg manualRepairIngredientSuppliers: () -> Ingredient
        ) = item(creator, registryName) {
            setUnlocalizedName(unlocalizedName)
            setCreativeTab { creativeTab }
            onRegisterItem {
                if (side.isClient) {
                    val modelResourceLocation = ModelResourceLocation(item.registryName!!, "normal")
                    MinecraftForge.EVENT_BUS.register(object : Any() {
                        @SubscribeEvent
                        fun accept(event: ModelBakeEvent) {
                            event.modelRegistry.putObject(modelResourceLocation, BakedModelBuiltinWrapper(event.modelRegistry.getObject(modelResourceLocation)!!))
                        }
                    })
                    ModelLoader.setCustomModelResourceLocation(item, 0, modelResourceLocation)
                }
            }
            onInit {
                val durability = (1..tier).fold(16) { a, _ -> a * 2 }
                item.maxDamage = durability - 1
                item.tier = tier
            }
            onCreateItemStack {
                if (parent != null) item.manualRepairIngredients += parent().manualRepairIngredients
                manualRepairIngredientSuppliers.forEach { item.manualRepairIngredients += it() }
                oreNameList.forEach { OreDictionary.registerOre(it, item.createItemStack(metadata = OreDictionary.WILDCARD_VALUE)) }
            }
        }

        operator fun EnumErgType.not(): () -> Ingredient = { sphereType.oreName.oreIngredient }
        operator fun String.not(): () -> Ingredient = { oreIngredient }

        val miragiumSword = fw(2, { ItemFairyWeapon() }, "miragium_sword", "miragiumSword", listOf(), null, !ATTACK, !SLASH)
        val crystalSword = fw(3, { ItemCrystalSword() }, "crystal_sword", "crystalSword", listOf(), miragiumSword, !CRYSTAL)
        val fairySword = fw(3, { ItemFairySword() }, "fairy_sword", "fairySword", listOf(), miragiumSword, !ATTACK)

        val miragiumAxe = fw(2, { ItemMiragiumAxe() }, "miragium_axe", "miragiumAxe", listOf(), null, !SLASH, !HARVEST, !"plateMiragium")

        val magicWandBase = fw(3, { ItemFairyWeapon() }, "magic_wand_base", "magicWandBase", listOf(), null, !KNOWLEDGE)
        val magicWandLight = fw(3, { ItemMagicWandLight() }, "light_magic_wand", "magicWandLight", listOf(), magicWandBase, !LIGHT)
        val magicWandCollecting = fw(3, { ItemMagicWandCollecting() }, "collecting_magic_wand", "magicWandCollecting", listOf(), magicWandBase, !WARP)
        val chargingRod = fw(3, { ItemChargingRod() }, "charging_rod", "chargingRod", listOf(), magicWandBase, !THUNDER, !WARP, !"ingotGold")
        val magicWandLightning = fw(3, { ItemMagicWandLightning() }, "lightning_magic_wand", "magicWandLightning", listOf(), chargingRod, !ENERGY, !"blockMirageFairyCrystalPure")

        val ocarinaBase = fw(3, { ItemFairyWeapon() }, "ocarina_base", "ocarinaBase", listOf(), null, !SOUND)
        val ocarinaTemptation = fw(3, { ItemOcarinaTemptation() }, "temptation_ocarina", "ocarinaTemptation", listOf(), ocarinaBase, !LIFE)
        val bellBase = fw(2, { ItemBellBase() }, "bell_base", "bellBase", listOf(), null, !SOUND)
        val bellFlowerPicking = fw(2, { ItemBellFlowerPicking(0.0, 0.001, 0.2) }, "flower_picking_bell", "bellFlowerPicking", listOf(), bellBase, !HARVEST)
        val bellFlowerPicking2 = fw(4, { ItemBellFlowerPicking(10.0, 0.01, 10000.0) }, "flower_picking_bell_2", "bellFlowerPicking2", listOf(), bellFlowerPicking, !HARVEST)
        val bellChristmas = fw(3, { ItemBellChristmas() }, "christmas_bell", "bellChristmas", listOf(), bellBase, !CHRISTMAS, !ATTACK)
        val miragiumScythe = fw(2, { ItemMiragiumScythe(0.0, 2.0f) }, "miragium_scythe", "miragiumScythe", listOf(), null, !SLASH, !HARVEST)
        val lilagiumScythe = fw(3, { ItemMiragiumScythe(10.0, 4.0f) }, "lilagium_scythe", "lilagiumScythe", listOf(), miragiumScythe, !HARVEST)
        val ryugyoDrill = fw(4, { ItemRyugyoDrill(0.0) }, "ryugyo_drill", "ryugyoDrill", listOf(), null, !DESTROY, !THUNDER, !WATER)

        onMakeLang {
            enJa("item.miragiumSword.name", "Miragium Sword", "ミラジウムの剣")
            enJa("item.miragiumSword.poem", "", "その刃で何を切る？")
            enJa("item.fairySword.name", "Fairy Sword", "妖精剣")
            enJa("item.fairySword.poem", "Design Contest Work", "デザインコンテスト武器")
            enJa("item.fairySword.author", "tanun3sei", "たぬん三世")
            enJa("item.miragiumAxe.name", "Miragium Axe", "ミラジウムの斧")
            enJa("item.miragiumAxe.poem", "", "飛べるって素敵")
            enJa("item.miragiumScythe.name", "Miragium Scythe", "ミラジウムの大鎌")
            enJa("item.miragiumScythe.poem", "", "自分を切らないように！")
            enJa("item.lilagiumScythe.name", "Lilagium Scythe", "リラジウムの大鎌")
            enJa("item.lilagiumScythe.poem", "", "葉っぱが吸い込まれてくる")
            enJa("item.magicWandBase.name", "Magic Wand Base", "ロッドベース")
            enJa("item.magicWandBase.poem", "", "風の心、探求")
            enJa("item.magicWandLight.name", "Magic Wand of Light", "光のロッド")
            enJa("item.magicWandLight.poem", "", "古代の魔法「ニクトフォビア」、優しい光が洞窟を照らす")
            enJa("item.magicWandCollecting.name", "Magic Wand of Collecting", "収集のロッド")
            enJa("item.magicWandCollecting.poem", "", "新開発の魔法「ソルメローシェ・トリーパ」、魔法のマジックハンド")
            enJa("item.chargingRod.name", "Charging Rod", "チャージングロッド")
            enJa("item.chargingRod.poem", "", "電気の力で栄えた文明があったという")
            enJa("item.magicWandLightning.name", "Magic Wand of Lightning", "ライトニングロッド")
            enJa("item.magicWandLightning.poem", "", "古代魔法「ライトニングボルト」")
            enJa("item.ocarinaBase.name", "Ocarina Base", "オカリナベース")
            enJa("item.ocarinaBase.poem", "", "適当に吹いても音楽になる笛")
            enJa("item.ocarinaTemptation.name", "Ocarina of Temptation", "魅惑のオカリナ")
            enJa("item.ocarinaTemptation.poem", "", "その音は人の腹を満たし、淫靡な気分にさせる")
            enJa("item.bellBase.name", "Bell Base", "鐘ベース")
            enJa("item.bellBase.poem", "", "妖精の力を解放せよ")
            enJa("item.bellFlowerPicking.name", "Bell of Flower Picking", "花摘みの鐘")
            enJa("item.bellFlowerPicking.poem", "", "ちょっとお花を摘みに")
            enJa("item.bellFlowerPicking2.name", "Bell of Flower Picking II", "花摘みの鐘 II")
            enJa("item.bellFlowerPicking2.poem", "", "光輝のフェロモン")
            enJa("item.bellChristmas.name", "Christmas Bell", "クリスマスの鐘")
            enJa("item.bellChristmas.poem", "", "いけない子には")
            enJa("item.crystalSword.name", "Crystal Sword", "クリスタルソード")
            enJa("item.crystalSword.poem", "", "妖精はこれをおやつにするという")
            enJa("item.ryugyoDrill.name", "Ryugyo Drill", "龍魚ドリル")
            enJa("item.ryugyoDrill.poem", "Design Contest Work", "デザインコンテスト武器")
            enJa("item.ryugyoDrill.author", "Yoshinon", "よしのん")

            enJa("miragefairy2019.magic.${MagicMessage.NO_FAIRY.unlocalizedName}.text", "You don't have a fairy", "妖精を所持していません")
            enJa("miragefairy2019.magic.${MagicMessage.INSUFFICIENT_DURABILITY.unlocalizedName}.text", "Insufficient durability", "耐久値が不足しています")
            enJa("miragefairy2019.magic.${MagicMessage.NO_TARGET.unlocalizedName}.text", "There is no target", "発動対象がありません")
            enJa("miragefairy2019.magic.${MagicMessage.COOL_TIME.unlocalizedName}.text", "Cool time remains", "クールタイムが残っています")
        }

        // チャージングロッド
        makeRecipe(
            ResourceName(ModMirageFairy2019.MODID, "charging_rod"),
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
                    "c" to DataOreIngredient(type = "miragefairy2019:ore_dict_complex", ore = "mirageFairy2019CraftingToolFairyWandCrafting"),
                    "m" to DataOreIngredient(type = "miragefairy2019:ore_dict_complex", ore = "mirageFairy2019CraftingToolFairyWandMelting")
                ),
                result = DataResult(
                    item = "miragefairy2019:charging_rod"
                )
            )
        )

        // ライトニングロッド
        makeRecipe(
            ResourceName(ModMirageFairy2019.MODID, "lightning_magic_wand"),
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
                    "c" to DataOreIngredient(type = "miragefairy2019:ore_dict_complex", ore = "mirageFairy2019CraftingToolFairyWandCrafting"),
                    "m" to DataOreIngredient(type = "miragefairy2019:ore_dict_complex", ore = "mirageFairy2019CraftingToolFairyWandMelting"),
                    "G" to DataOreIngredient(ore = "blockMirageFairyCrystalPure")
                ),
                result = DataResult(
                    item = "miragefairy2019:lightning_magic_wand"
                )
            )
        )


        // 妖精搭乗レシピ
        onAddRecipe {
            GameRegistry.findRegistry(IRecipe::class.java).register(RecipesCombining())
            GameRegistry.findRegistry(IRecipe::class.java).register(RecipesUncombining())
        }

    }
}

enum class MagicMessage(val unlocalizedName: String) {
    NO_FAIRY("noFairy"),
    INSUFFICIENT_DURABILITY("insufficientDurability"),
    NO_TARGET("noTarget"),
    COOL_TIME("coolTime"),
    ;

    val displayText get() = textComponent { translate("miragefairy2019.magic.$unlocalizedName.text") }
}
