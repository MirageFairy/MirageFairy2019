package miragefairy2019.mod.fairy

import miragefairy2019.lib.entries
import miragefairy2019.lib.registerItemColorHandler
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.item
import miragefairy2019.libkt.module
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.mod.Main.side
import miragefairy2019.mod.ModMirageFairy2019
import mirrg.boron.util.UtilsString
import mirrg.kotlin.toUpperCamelCase
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.OreDictionary

object Fairy {
    lateinit var creativeTabFairyMotif: CreativeTabs
    lateinit var creativeTabFairyRank: CreativeTabs
    lateinit var listItemFairy: List<() -> ItemFairy>
    val module = module {

        loaderFairyCrystalDrop(this)
        loaderFairyLogDrop(this)

        // クリエイティブタブ
        onInitCreativeTab {
            creativeTabFairyMotif = object : CreativeTabs("mirageFairy2019.fairy.motif") {
                @SideOnly(Side.CLIENT)
                override fun getTabIconItem() = FairyTypes.instance.imperialTopaz.main.createItemStack()

                @SideOnly(Side.CLIENT)
                override fun displayAllRelevantItems(itemStacks: NonNullList<ItemStack>) {
                    listItemFairy.forEachIndexed { i, _ ->
                        FairyTypes.instance.variants.forEach { variant ->
                            if (variant.bundle.main.canSeeOnCreativeTab) {
                                itemStacks.add(variant.bundle[i].createItemStack())
                            }
                        }
                    }
                }

                override fun hasSearchBar() = true
            }.setBackgroundImageName("item_search.png")
            creativeTabFairyRank = object : CreativeTabs("mirageFairy2019.fairy.rank") {
                @SideOnly(Side.CLIENT)
                override fun getTabIconItem() = FairyTypes.instance.moonstone.main.createItemStack()

                @SideOnly(Side.CLIENT)
                override fun displayAllRelevantItems(itemStacks: NonNullList<ItemStack>) {
                    FairyTypes.instance.variants.forEach { variant ->
                        if (variant.bundle.main.canSeeOnCreativeTab) {
                            listItemFairy.forEachIndexed { i, _ ->
                                itemStacks.add(variant.bundle[i].createItemStack())
                            }
                        }
                    }
                }

                override fun hasSearchBar() = true
            }.setBackgroundImageName("item_search.png")
        }
        onMakeLang {
            enJa("itemGroup.mirageFairy2019.fairy.motif", "Fairy: Motif", "妖精：モチーフ")
            enJa("itemGroup.mirageFairy2019.fairy.rank", "Fairy: Rank", "妖精：ランク")
        }

        val rankMax = 7

        // モチーフ
        onRegisterItem {
            FairyTypes.instance = FairyTypes(rankMax)
        }

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
                    FairyTypes.instance.variants.forEach { entry -> item.registerVariant(entry.id, entry.bundle[rank - 1]) }
                    if (side.isClient) {
                        item.variants.forEach { variant ->
                            ModelLoader.setCustomModelResourceLocation(item, variant.metadata, ModelResourceLocation(ResourceLocation(ModMirageFairy2019.MODID, "fairy"), "normal"))
                        }
                    }
                }
                registerItemColorHandler()
            }
        }
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

        // 鉱石辞書
        onCreateItemStack {
            FairyTypes.instance.variants.forEach { variant ->
                listItemFairy.forEachIndexed { i, _ ->

                    // 品種別
                    OreDictionary.registerOre("mirageFairy2019Fairy${variant.bundle[i].type.motif!!.resourcePath.toUpperCamelCase()}Rank${i + 1}", variant.bundle[i].createItemStack())

                    // エルグ別
                    variant.bundle[i].type.ergSet.entries.forEach {
                        if (it.second >= 10) {
                            OreDictionary.registerOre("mirageFairy2019FairyAbility${UtilsString.toUpperCaseHead(it.first.toString())}", variant.bundle[i].createItemStack())
                        }
                    }

                }
            }
        }

        // 凝縮・分散レシピ
        onAddRecipe {
            FairyTypes.instance.variants.forEach { variant ->
                (0 until listItemFairy.size - 1).forEach { i ->

                    // 凝縮
                    GameRegistry.addShapelessRecipe(
                        ResourceLocation("${ModMirageFairy2019.MODID}:condense_r${i}_fairy_${variant.bundle[i].type.motif!!.resourcePath}"),
                        ResourceLocation("${ModMirageFairy2019.MODID}:condense_r${i}_fairy_${variant.bundle[i].type.motif!!.resourcePath}"),
                        variant.bundle[i + 1].createItemStack(),
                        Ingredient.fromStacks(variant.bundle[i].createItemStack()),
                        Ingredient.fromStacks(variant.bundle[i].createItemStack()),
                        Ingredient.fromStacks(variant.bundle[i].createItemStack()),
                        Ingredient.fromStacks(variant.bundle[i].createItemStack()),
                        Ingredient.fromStacks(variant.bundle[i].createItemStack()),
                        Ingredient.fromStacks(variant.bundle[i].createItemStack()),
                        Ingredient.fromStacks(variant.bundle[i].createItemStack()),
                        Ingredient.fromStacks(variant.bundle[i].createItemStack())
                    )

                    // 分解
                    GameRegistry.addShapelessRecipe(
                        ResourceLocation("${ModMirageFairy2019.MODID}:decondense_r${i}_fairy_${variant.bundle[i].type.motif!!.resourcePath}"),
                        ResourceLocation("${ModMirageFairy2019.MODID}:decondense_r${i}_fairy_${variant.bundle[i].type.motif!!.resourcePath}"),
                        variant.bundle[i].createItemStack(8),
                        Ingredient.fromStacks(variant.bundle[i + 1].createItemStack())
                    )

                }
            }
        }

    }
}
