package miragefairy2019.mod.fairy

import miragefairy2019.api.Erg
import miragefairy2019.api.ErgSet
import miragefairy2019.api.Mana
import miragefairy2019.lib.div
import miragefairy2019.lib.entries
import miragefairy2019.lib.fairyCentrifugeCraftHandler
import miragefairy2019.lib.max
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.obtain
import miragefairy2019.lib.registerItemColorHandler
import miragefairy2019.lib.resourcemaker.DataModel
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.sum
import miragefairy2019.lib.times
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.oreIngredient
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.Main.side
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.artifacts.ingredient
import miragefairy2019.mod.fairystickcraft.ApiFairyStickCraft
import miragefairy2019.mod.fairystickcraft.FairyStickCraftConditionConsumeItem
import miragefairy2019.mod.fairystickcraft.FairyStickCraftConditionNotNether
import miragefairy2019.mod.fairystickcraft.FairyStickCraftConditionSpawnBlock
import miragefairy2019.mod.fairystickcraft.FairyStickCraftConditionUseItem
import miragefairy2019.mod.fairystickcraft.FairyStickCraftRecipe
import mirrg.kotlin.hydrogen.toUpperCamelCase
import mirrg.kotlin.hydrogen.toUpperCaseHead
import net.minecraft.block.BlockDynamicLiquid
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.oredict.OreIngredient
import kotlin.math.pow

fun FairyCard.getVariant(rank: Int = 1) = listItemFairy[rank - 1]().getVariant(id)!!
fun FairyCard.createItemStack(rank: Int = 1, count: Int = 1) = getVariant(rank).createItemStack(count)

fun EntityPlayer.obtainFairy(fairyCard: FairyCard, count: Int) {
    var remainingCount = count // どんどんスケールが下がっていく残り妖精数
    var rank = 1

    // まだ上がある間
    while (rank < rankMax) {
        this.obtain(fairyCard.createItemStack(rank = rank, count = remainingCount % 8)) // そのランクの妖精を余りの分だけ排出
        remainingCount /= 8 // スケールダウン
        rank++
    }

    // もう上がない
    this.obtain(fairyCard.createItemStack(rank = rank, count = remainingCount)) // 最高ランクの妖精を残り個数全部排出

}

private val rankMax get() = listItemFairy.size

lateinit var creativeTabFairyMotif: CreativeTabs
lateinit var creativeTabFairyRank: CreativeTabs
lateinit var listItemFairy: List<() -> ItemFairy>

val fairyModule = module {

    // クリエイティブタブ
    onInitCreativeTab {
        creativeTabFairyMotif = object : CreativeTabs("mirageFairy2019.fairy.motif") {
            @SideOnly(Side.CLIENT)
            override fun getTabIconItem() = FairyCard.IMPERIAL_TOPAZ.createItemStack()

            @SideOnly(Side.CLIENT)
            override fun displayAllRelevantItems(itemStacks: NonNullList<ItemStack>) {
                listItemFairy.forEachIndexed { i, _ ->
                    FairyCard.values().forEach { fairyCard ->
                        if (fairyCard.getVariant().canSeeOnCreativeTab) {
                            itemStacks.add(fairyCard.createItemStack(i + 1))
                        }
                    }
                }
            }

            override fun hasSearchBar() = true
        }.setBackgroundImageName("item_search.png")
        creativeTabFairyRank = object : CreativeTabs("mirageFairy2019.fairy.rank") {
            @SideOnly(Side.CLIENT)
            override fun getTabIconItem() = FairyCard.MOONSTONE.createItemStack()

            @SideOnly(Side.CLIENT)
            override fun displayAllRelevantItems(itemStacks: NonNullList<ItemStack>) {
                FairyCard.values().forEach { fairyCard ->
                    if (fairyCard.getVariant().canSeeOnCreativeTab) {
                        listItemFairy.forEachIndexed { i, _ ->
                            itemStacks.add(fairyCard.createItemStack(i + 1))
                        }
                    }
                }
            }

            override fun hasSearchBar() = true
        }.setBackgroundImageName("item_search.png")
    }

    // 翻訳生成
    onMakeLang {
        enJa("itemGroup.mirageFairy2019.fairy.motif", "Fairy: Motif", "妖精：モチーフ")
        enJa("itemGroup.mirageFairy2019.fairy.rank", "Fairy: Rank", "妖精：ランク")
    }


    val rankMax = 7

    // アイテム
    listItemFairy = (1..rankMax).map { rank ->
        val dressColor = when (rank) {
            1 -> 0xFF8888
            2 -> 0x8888FF
            3 -> 0x88FF88
            4 -> 0xFFFF88
            5 -> 0x111111
            6 -> 0xFFFFFF
            7 -> 0x88FFFF
            else -> 0xFFFFFF
        }
        item({ ItemFairy(dressColor) }, if (rank == 1) "mirage_fairy" else "mirage_fairy_r$rank") {
            setUnlocalizedName("mirageFairyR$rank")
            onRegisterItem {
                FairyCard.values().forEach { fairyCard ->
                    val rateRare = 2.0.pow((fairyCard.rare + rank - 2) / 4.0)
                    val rateVariance = 0.5.pow(((fairyCard.manaSet / fairyCard.manaSet.max).sum - 1) / 5.0)
                    val manaSetBase = fairyCard.manaSet / fairyCard.manaSet.sum * (50 * rateRare * rateVariance * fairyCard.rateSpecial)
                    val ergSetReal = ErgSet(fairyCard.ergSet.entries.associate { (erg, value) -> erg to value * rateRare })
                    val variant = VariantFairy(
                        fairyCard,
                        ResourceLocation(ModMirageFairy2019.MODID, fairyCard.registryName),
                        textComponent { translate("mirageFairy2019.fairy.${fairyCard.unlocalizedName}.name") },
                        manaSetBase,
                        ergSetReal,
                        rank
                    )

                    item.registerVariant(fairyCard.id, variant)
                }
                if (side.isClient) {
                    item.variants.forEach { variant ->
                        ModelLoader.setCustomModelResourceLocation(item, variant.metadata, ModelResourceLocation(ResourceLocation(ModMirageFairy2019.MODID, "fairy"), "normal"))
                    }
                }
            }
            registerItemColorHandler()
        }
    }

    // アイテムモデル生成
    makeItemModel("fairy") {
        DataModel(
            parent = "item/generated",
            textures = mapOf(
                "layer0" to "miragefairy2019:items/fairy_layer0",
                "layer1" to "miragefairy2019:items/fairy_layer1",
                "layer2" to "miragefairy2019:items/fairy_layer2",
                "layer3" to "miragefairy2019:items/fairy_layer3",
                "layer4" to "miragefairy2019:items/fairy_layer4"
            )
        )
    }

    // 翻訳生成
    onMakeLang {
        enJa("item.mirageFairyR1.name", "Mirage Fairy", "妖精")
        enJa("item.mirageFairyR1.format", "%s", "%s")
        enJa("item.mirageFairyR2.name", "Mirage Fairy II", "妖精 II")
        enJa("item.mirageFairyR2.format", "%s II", "%s II")
        enJa("item.mirageFairyR3.name", "Mirage Fairy III", "妖精 III")
        enJa("item.mirageFairyR3.format", "%s III", "%s III")
        enJa("item.mirageFairyR4.name", "Mirage Fairy IV", "妖精 IV")
        enJa("item.mirageFairyR4.format", "%s IV", "%s IV")
        enJa("item.mirageFairyR5.name", "Mirage Fairy V", "妖精 V")
        enJa("item.mirageFairyR5.format", "%s V", "%s V")
        enJa("item.mirageFairyR6.name", "Mirage Fairy VI", "妖精 VI")
        enJa("item.mirageFairyR6.format", "%s VI", "%s VI")
        enJa("item.mirageFairyR7.name", "Mirage Fairy VII", "妖精 VII")
        enJa("item.mirageFairyR7.format", "%s VII", "%s VII")
    }

    // 妖精ごと
    FairyCard.values().forEach { fairyCard ->

        // 鉱石辞書
        onCreateItemStack {
            listItemFairy.indices.forEach { i ->
                val rank = i + 1

                // 品種別
                OreDictionary.registerOre("mirageFairy2019Fairy${fairyCard.getVariant(rank).motif!!.resourcePath.toUpperCamelCase()}Rank$rank", fairyCard.createItemStack(rank))

                // エルグ別
                fairyCard.getVariant(rank).ergSet.entries.forEach {
                    if (it.second >= 10) {
                        OreDictionary.registerOre("mirageFairy2019FairyAbility${it.first.toString().toUpperCaseHead()}", fairyCard.createItemStack(rank))
                    }
                }

            }
        }

        // 凝縮・分散レシピ
        onAddRecipe {
            (0 until listItemFairy.size - 1).forEach { i ->
                val rank = i + 1

                // 凝縮
                GameRegistry.addShapelessRecipe(
                    ResourceLocation("${ModMirageFairy2019.MODID}:condense_r${i}_fairy_${fairyCard.getVariant(rank).motif!!.resourcePath}"),
                    ResourceLocation("${ModMirageFairy2019.MODID}:condense_r${i}_fairy_${fairyCard.getVariant(rank).motif!!.resourcePath}"),
                    fairyCard.createItemStack(rank + 1),
                    Ingredient.fromStacks(fairyCard.createItemStack(rank)),
                    Ingredient.fromStacks(fairyCard.createItemStack(rank)),
                    Ingredient.fromStacks(fairyCard.createItemStack(rank)),
                    Ingredient.fromStacks(fairyCard.createItemStack(rank)),
                    Ingredient.fromStacks(fairyCard.createItemStack(rank)),
                    Ingredient.fromStacks(fairyCard.createItemStack(rank)),
                    Ingredient.fromStacks(fairyCard.createItemStack(rank)),
                    Ingredient.fromStacks(fairyCard.createItemStack(rank))
                )

                // 分解
                GameRegistry.addShapelessRecipe(
                    ResourceLocation("${ModMirageFairy2019.MODID}:decondense_r${i}_fairy_${fairyCard.getVariant(rank).motif!!.resourcePath}"),
                    ResourceLocation("${ModMirageFairy2019.MODID}:decondense_r${i}_fairy_${fairyCard.getVariant(rank).motif!!.resourcePath}"),
                    fairyCard.createItemStack(rank, 8),
                    Ingredient.fromStacks(fairyCard.createItemStack(rank + 1))
                )

            }
        }

        // 翻訳生成
        onMakeLang {
            enJa("mirageFairy2019.fairy.${fairyCard.unlocalizedName}.name", fairyCard.englishName, fairyCard.japaneseName)
        }

    }

    // 妖精使役レシピ
    onAddRecipe {

        // 水精→水源
        ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
            it.conditions += FairyStickCraftConditionUseItem(miragefairy2019.mod.artifacts.WandType.CRAFTING.ingredient)
            it.conditions += FairyStickCraftConditionNotNether()
            it.conditions += FairyStickCraftConditionSpawnBlock { Blocks.WATER.defaultState }
            it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("mirageFairy2019FairyWaterRank1"))
        })

        // 溶岩精→溶岩流
        ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
            it.conditions += FairyStickCraftConditionUseItem(miragefairy2019.mod.artifacts.WandType.CRAFTING.ingredient)
            it.conditions += FairyStickCraftConditionSpawnBlock { Blocks.FLOWING_LAVA.defaultState.withProperty(BlockDynamicLiquid.LEVEL, 15) }
            it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("mirageFairy2019FairyLavaRank1"))
        })

        // 蜘蛛精→糸ブロック
        ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
            it.conditions += FairyStickCraftConditionUseItem(miragefairy2019.mod.artifacts.WandType.CRAFTING.ingredient)
            it.conditions += FairyStickCraftConditionSpawnBlock { Blocks.WEB.defaultState }
            it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("mirageFairy2019FairySpiderRank1"))
        })

    }

    // 妖精合成レシピ
    onAddRecipe {

        // マンドレイク
        fairyCentrifugeCraftHandler(90.0) {
            process { !Mana.FIRE + !Erg.DESTROY * 2.0 }
            process { !Mana.WIND + !Erg.WARP * 2.0 }
            process { !Mana.FIRE + !Erg.LIFE * 2.0 }
            input("mirageFairy2019FairyMirageFlowerRank1".oreIngredient, 1)
            input("container1000MirageFairyBlood".oreIngredient, 1)
            input("mirageFairy2019FairyVillagerRank1".oreIngredient, 1)
            input("mirageFairy2019FairyGoldenAppleRank1".oreIngredient, 1)
            output(FairyCard.MANDRAKE.createItemStack(), 1.0, 1.0)
        }

        // ヴェロペーダ
        fairyCentrifugeCraftHandler(90.0) {
            process { !Mana.FIRE + !Erg.DESTROY * 2.0 }
            process { !Mana.WIND + !Erg.WARP * 2.0 }
            process { !Mana.FIRE + !Erg.ATTACK * 2.0 }
            input("mirageFairy2019FairyMirageFlowerRank1".oreIngredient, 1)
            input("container1000MirageFairyBlood".oreIngredient, 1)
            input("mirageFairy2019FairyCurseOfVanishingRank1".oreIngredient, 1)
            input("mirageFairy2019FairyCinnabarRank1".oreIngredient, 1)
            output(FairyCard.VELOPEDA.createItemStack(), 1.0, 1.0)
        }

    }

}
