package miragefairy2019.mod3.fairy

import miragefairy2019.lib.entries
import miragefairy2019.libkt.module
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod3.main.ApiMain.side
import mirrg.boron.util.UtilsString
import mirrg.kotlin.toUpperCamelCase
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.OreDictionary

object Fairy {
    lateinit var creativeTab: CreativeTabs
    lateinit var listItemFairy: List<ItemFairy>
    val module = module {

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
                        if (variant.bundle.main.canSeeOnCreativeTab) {
                            listItemFairy.forEachIndexed { i, _ ->
                                itemStacks.add(variant.bundle[i].createItemStack())
                            }
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
                FairyTypes.instance.variants.forEach { item.registerVariant(it.id, it.bundle[rank - 1]) }
                ForgeRegistries.ITEMS.register(item)
                if (side.isClient) {
                    item.variants.forEach { ModelLoader.setCustomModelResourceLocation(item, it.metadata, ModelResourceLocation(ResourceLocation(ModMirageFairy2019.MODID, "fairy"), "normal")) }
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
                                val variant = item.getVariant(itemStack) ?: return 0xFFFFFF
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
