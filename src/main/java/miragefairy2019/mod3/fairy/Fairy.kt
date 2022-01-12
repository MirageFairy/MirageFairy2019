package miragefairy2019.modkt.modules.fairy

import miragefairy2019.libkt.Module
import miragefairy2019.libkt.ingredient
import miragefairy2019.libkt.item
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.api.fairy.IItemFairy
import miragefairy2019.mod.lib.OreIngredientComplex
import miragefairy2019.mod3.fairy.FairyTypes
import miragefairy2019.mod3.fairy.ItemBakedFairy
import miragefairy2019.mod3.fairy.ItemDebugFairyList
import miragefairy2019.mod3.fairy.ItemFairy
import miragefairy2019.mod3.fairy.RecipeFairyBaking
import miragefairy2019.mod3.fairy.itemBakedFairy
import miragefairy2019.mod3.fairy.loaderFairyCrystalDrop
import miragefairy2019.mod3.fairy.loaderFairyLogDrop
import miragefairy2019.mod3.fairy.relation.FairyRelationRegistries
import miragefairy2019.mod3.fairy.relation.withoutPartiallyMatch
import miragefairy2019.mod3.main.api.ApiMain
import miragefairy2019.mod3.main.api.ApiMain.side
import mirrg.boron.util.UtilsString
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.oredict.OreIngredient
import net.minecraftforge.oredict.ShapelessOreRecipe

object Fairy {
    lateinit var creativeTab: CreativeTabs
    lateinit var listItemFairy: List<ItemFairy>
    val module: Module = {

        loaderFairyCrystalDrop(this)
        loaderFairyLogDrop(this)

        // 妖精クリエイティブタブ
        onInitCreativeTab {
            creativeTab = object : CreativeTabs("mirageFairy2019.fairy") {
                @SideOnly(Side.CLIENT)
                override fun getTabIconItem() = FairyTypes.instance.magentaGlazedTerracotta.main.createItemStack()

                @SideOnly(Side.CLIENT)
                override fun displayAllRelevantItems(itemStacks: NonNullList<ItemStack>) {
                    FairyTypes.instance.variants.forEach { variant ->
                        listItemFairy.forEachIndexed { i, _ ->
                            itemStacks.add(variant.y[i].createItemStack())
                        }
                    }
                }
            }
        }

        // 妖精アイテム
        onRegisterItem {
            val rankMax = 7

            // 妖精タイプ登録
            FairyTypes.instance = FairyTypes(rankMax)

            // 妖精
            listItemFairy = (1..rankMax).map { rank ->
                val item = ItemFairy()
                item.setRegistryName(ModMirageFairy2019.MODID, if (rank == 1) "mirage_fairy" else "mirage_fairy_r$rank")
                item.unlocalizedName = "mirageFairyR$rank"
                item.creativeTab = creativeTab // TODO 冗長説
                FairyTypes.instance.variants.forEach { item.registerVariant(it.x, it.y[rank - 1]) }
                ForgeRegistries.ITEMS.register(item)
                if (side.isClient) {
                    item.variants.forEach { ModelLoader.setCustomModelResourceLocation(item, it.x, ModelResourceLocation(ResourceLocation(ModMirageFairy2019.MODID, "fairy"), "normal")) }
                }
                item
            }

        }
        onRegisterItemColorHandler {
            object {
                @SideOnly(Side.CLIENT)
                fun run() {

                    // 妖精アイテムのカスタム色
                    listItemFairy.forEachIndexed { i, item ->
                        @SideOnly(Side.CLIENT)
                        class ItemColorImpl : IItemColor {
                            override fun colorMultiplier(itemStack: ItemStack, tintIndex: Int): Int {
                                val variant = item.getVariant(itemStack).orElse(null) ?: return 0xFFFFFF
                                return when (tintIndex) {
                                    0 -> variant.colorSet.skin
                                    1 -> when (i) {
                                        0 -> 0xFF8888
                                        1 -> 0x8888FF
                                        2 -> 0x88FF88
                                        3 -> 0xFFFF88
                                        4 -> 0x111111
                                        5 -> 0xFFFFFF
                                        6 -> 0x88FFFF
                                        else -> 0xFFFFFF
                                    }
                                    2 -> variant.colorSet.dark
                                    3 -> variant.colorSet.bright
                                    4 -> variant.colorSet.hair
                                    else -> 0xFFFFFF
                                }
                            }
                        }
                        Minecraft.getMinecraft().itemColors.registerItemColorHandler(ItemColorImpl(), item)
                    }

                }
            }.run()
        }

        // 妖精の鉱石辞書
        onCreateItemStack {
            FairyTypes.instance.variants.forEach { variant ->
                listItemFairy.forEachIndexed { i, _ ->

                    // 品種別
                    OreDictionary.registerOre("mirageFairy2019Fairy${UtilsString.toUpperCaseHead(variant.y[i].type.motif!!.resourcePath)}Rank${i + 1}", variant.y[i].createItemStack())

                    // エルグ別
                    variant.y[i].type.ergSet.entries.forEach {
                        if (it.power >= 10) {
                            OreDictionary.registerOre("mirageFairy2019FairyAbility${UtilsString.toUpperCaseHead(it.type.toString())}", variant.y[i].createItemStack())
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
                        ResourceLocation("${ModMirageFairy2019.MODID}:condense_r${i}_fairy_${variant.y[i].type.motif!!.resourcePath}"),
                        ResourceLocation("${ModMirageFairy2019.MODID}:condense_r${i}_fairy_${variant.y[i].type.motif!!.resourcePath}"),
                        variant.y[i + 1].createItemStack(),
                        Ingredient.fromStacks(variant.y[i].createItemStack()),
                        Ingredient.fromStacks(variant.y[i].createItemStack()),
                        Ingredient.fromStacks(variant.y[i].createItemStack()),
                        Ingredient.fromStacks(variant.y[i].createItemStack()),
                        Ingredient.fromStacks(variant.y[i].createItemStack()),
                        Ingredient.fromStacks(variant.y[i].createItemStack()),
                        Ingredient.fromStacks(variant.y[i].createItemStack()),
                        Ingredient.fromStacks(variant.y[i].createItemStack())
                    )

                    // 分解
                    GameRegistry.addShapelessRecipe(
                        ResourceLocation("${ModMirageFairy2019.MODID}:decondense_r${i}_fairy_${variant.y[i].type.motif!!.resourcePath}"),
                        ResourceLocation("${ModMirageFairy2019.MODID}:decondense_r${i}_fairy_${variant.y[i].type.motif!!.resourcePath}"),
                        variant.y[i].createItemStack(8),
                        Ingredient.fromStacks(variant.y[i + 1].createItemStack())
                    )

                }
            }
        }

        // 確定レシピ
        onAddRecipe {
            var counter = 0

            // レシピに変換可能な妖精関係の関係性順のリスト
            // この順序のまま登録すれば競合したレシピが関係性の強いものが優先して使われる
            // ingredient, entry
            val pairs = listOf(
                FairyRelationRegistries.ingredient.entries.withoutPartiallyMatch.map { Pair(it.key, it) },
                FairyRelationRegistries.item.entries.withoutPartiallyMatch.map { Pair(it.key.ingredient, it) },
                FairyRelationRegistries.block.entries.withoutPartiallyMatch.mapNotNull { it.key.item?.ingredient?.let { ingredient -> Pair(ingredient, it) } }
            ).flatten().sortedByDescending { it.second.relevance }

            // 登録
            pairs.forEach { (ingredient, entry) ->

                // 召喚のワンド使用
                GameRegistry.findRegistry(IRecipe::class.java).register(
                    ShapelessOreRecipe(
                        ResourceLocation(ModMirageFairy2019.MODID, "mirage_fairy_from_item_$counter"),
                        entry.fairy.main.createItemStack(),
                        OreIngredientComplex("mirageFairy2019CraftingToolFairyWandSummoning"),
                        OreIngredient("mirageFairyCrystal"),
                        ingredient
                    ).setRegistryName(ModMirageFairy2019.MODID, "mirage_fairy_from_item_$counter")
                )
                counter++

            }

        }

        // 妖精一覧デバッグアイテム
        item({ ItemDebugFairyList() }, "debug_fairy_list") {
            setUnlocalizedName("debugFairyList")
            setCreativeTab { ApiMain.creativeTab }
            modInitializer.onRegisterItem {
                if (side.isClient) {
                    ModelLoader.setCustomModelResourceLocation(item, 0, ModelResourceLocation("minecraft:book", "normal"))
                }
            }
        }

        // 焼き妖精
        itemBakedFairy = item({ ItemBakedFairy() }, "baked_fairy") {
            setUnlocalizedName("bakedFairy")
            setCreativeTab { ApiMain.creativeTab }
            modInitializer.onRegisterItem {
                if (side.isClient) {
                    ModelLoader.setCustomModelResourceLocation(item, 0, ModelResourceLocation(item.registryName!!, "normal"))
                }
            }
        }

        // 焼き妖精のカスタム色
        onRegisterItemColorHandler {
            object {
                @SideOnly(Side.CLIENT)
                fun run() {
                    @SideOnly(Side.CLIENT)
                    class ItemColorImpl : IItemColor {
                        override fun colorMultiplier(itemStack: ItemStack, tintIndex: Int): Int {
                            val fairyItemStack = (ItemBakedFairy.getFairy(itemStack) ?: return 0xFFFFFF)
                            val variant = listItemFairy[0].getVariant(fairyItemStack).orElse(null) ?: return 0xFFFFFF
                            return when (tintIndex) {
                                0 -> 0xFFFFFF
                                1 -> variant.colorSet.skin
                                2 -> 0xFFFFFF
                                3 -> variant.colorSet.dark
                                4 -> variant.colorSet.bright
                                5 -> variant.colorSet.hair
                                6 -> 0xFFFFFF
                                else -> 0xFFFFFF
                            }
                        }
                    }
                    Minecraft.getMinecraft().itemColors.registerItemColorHandler(ItemColorImpl(), itemBakedFairy())
                }
            }.run()
        }

        // 焼き妖精レシピ
        onAddRecipe {
            ForgeRegistries.RECIPES.register(RecipeFairyBaking())
        }

    }
}

val ItemStack.fairyType get() = (item as? IItemFairy)?.getMirageFairy2019Fairy(this)?.orElse(null) // TODO 移動
