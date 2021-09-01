package miragefairy2019.modkt.modules.fairy

import miragefairy2019.libkt.Module
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.api.fairy.ApiFairy
import miragefairy2019.mod.api.main.ApiMain
import miragefairy2019.mod.common.fairy.relation.FairyRelationRegistry
import miragefairy2019.mod.lib.OreIngredientComplex
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

object ModuleFairy {
    lateinit var creativeTab: CreativeTabs
    lateinit var listItemFairy: List<ItemFairy>

    val init: Module = {

        // 妖精関係性レジストリ
        onInstantiation {
            ApiFairy.fairyRelationRegistry = FairyRelationRegistry()
        }

        // 妖精クリエイティブタブ
        onInitCreativeTab {
            creativeTab = object : CreativeTabs("mirageFairy2019.fairy") {
                @SideOnly(Side.CLIENT)
                override fun getTabIconItem() = FairyTypes.instance.magentaglazedterracotta.main.createItemStack()

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
                if (ApiMain.side().isClient) {
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
                    OreDictionary.registerOre("mirageFairy2019Fairy${UtilsString.toUpperCaseHead(variant.y[i].type.breed!!.resourcePath)}Rank${i + 1}", variant.y[i].createItemStack())

                    // エルグ別
                    variant.y[i].type.ergSet.entries.forEach {
                        if (it.power >= 10) {
                            OreDictionary.registerOre("mirageFairy2019FairyAbility${UtilsString.toUpperCaseHead(it.type.name)}", variant.y[i].createItemStack())
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
                            ResourceLocation("${ModMirageFairy2019.MODID}:condense_r${i}_fairy_${variant.y[i].type.breed!!.resourcePath}"),
                            ResourceLocation("${ModMirageFairy2019.MODID}:condense_r${i}_fairy_${variant.y[i].type.breed!!.resourcePath}"),
                            variant.y[i + 1].createItemStack(),
                            Ingredient.fromStacks(variant.y[i].createItemStack()),
                            Ingredient.fromStacks(variant.y[i].createItemStack()),
                            Ingredient.fromStacks(variant.y[i].createItemStack()),
                            Ingredient.fromStacks(variant.y[i].createItemStack()),
                            Ingredient.fromStacks(variant.y[i].createItemStack()),
                            Ingredient.fromStacks(variant.y[i].createItemStack()),
                            Ingredient.fromStacks(variant.y[i].createItemStack()),
                            Ingredient.fromStacks(variant.y[i].createItemStack()))

                    // 分解
                    GameRegistry.addShapelessRecipe(
                            ResourceLocation("${ModMirageFairy2019.MODID}:decondense_r${i}_fairy_${variant.y[i].type.breed!!.resourcePath}"),
                            ResourceLocation("${ModMirageFairy2019.MODID}:decondense_r${i}_fairy_${variant.y[i].type.breed!!.resourcePath}"),
                            variant.y[i].createItemStack(8),
                            Ingredient.fromStacks(variant.y[i + 1].createItemStack()))

                }
            }
        }

        // 確定レシピ
        onAddRecipe {
            var counter = 0
            ApiFairy.fairyRelationRegistry.ingredientFairyRelations.forEach { relation ->
                if (relation.relevance >= 1) {

                    // ダイヤモンド使用
                    GameRegistry.findRegistry(IRecipe::class.java).register(ShapelessOreRecipe(
                            ResourceLocation(ModMirageFairy2019.MODID, "mirage_fairy_from_item_$counter"),
                            relation.itemStackFairy,
                            OreIngredientComplex("mirageFairy2019CraftingToolFairyWandSummoning"),
                            OreIngredient("mirageFairyCrystal"),
                            relation.ingredient).setRegistryName(ModMirageFairy2019.MODID, "mirage_fairy_from_item_$counter"))
                    counter++

                    // 召喚のワンド使用
                    GameRegistry.findRegistry(IRecipe::class.java).register(ShapelessOreRecipe(
                            ResourceLocation(ModMirageFairy2019.MODID, "mirage_fairy_from_summoning_fairy_wand_$counter"),
                            relation.itemStackFairy,
                            OreIngredient("gemDiamond"),
                            OreIngredient("mirageFairyCrystal"),
                            relation.ingredient).setRegistryName(ModMirageFairy2019.MODID, "mirage_fairy_from_summoning_fairy_wand_$counter"))
                    counter++

                }
            }
        }

    }
}
