package miragefairy2019.mod.modules.ore

import miragefairy2019.libkt.DataIngredient
import miragefairy2019.libkt.DataItemModel
import miragefairy2019.libkt.DataOreIngredient
import miragefairy2019.libkt.DataResult
import miragefairy2019.libkt.DataShapelessRecipe
import miragefairy2019.libkt.DataSimpleIngredient
import miragefairy2019.libkt.ItemVariantInitializer
import miragefairy2019.libkt.MakeItemVariantModelScope
import miragefairy2019.libkt.Module
import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.addOreName
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.generated
import miragefairy2019.libkt.handheld
import miragefairy2019.libkt.ingredient
import miragefairy2019.libkt.item
import miragefairy2019.libkt.itemVariant
import miragefairy2019.libkt.makeItemVariantModel
import miragefairy2019.libkt.makeRecipe
import miragefairy2019.libkt.oreIngredient
import miragefairy2019.libkt.red
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.lib.ItemMultiMaterial
import miragefairy2019.mod.lib.ItemVariantMaterial
import miragefairy2019.mod.lib.setCustomModelResourceLocations
import miragefairy2019.mod3.main.api.ApiMain
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.AnvilUpdateEvent
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object Ore {
    lateinit var itemMaterials: () -> ItemSimpleMaterials
    val module: Module = {

        // マテリアルアイテム
        itemMaterials = item({ ItemSimpleMaterials() }, "materials") {
            setUnlocalizedName("materials")
            setCreativeTab { ApiMain.creativeTab }

            fun r(
                metadata: Int,
                registryName: String,
                unlocalizedName: String,
                oreName: String,
                modelSupplier: MakeItemVariantModelScope<ItemSimpleMaterials, ItemVariantSimpleMaterials>.() -> DataItemModel
            ) = itemVariant(registryName, { ItemVariantSimpleMaterials(it, unlocalizedName) }, metadata) {
                addOreName(oreName)
                makeItemVariantModel { modelSupplier() }
            }

            fun ItemVariantInitializer<ItemSimpleMaterials, ItemVariantSimpleMaterials>.fuel(burnTime: Int) = also { itemInitializer.modInitializer.onRegisterItem { itemVariant.burnTime = burnTime } }

            r(0, "apatite_gem", "gemApatite", "gemApatite", { generated })
            r(1, "fluorite_gem", "gemFluorite", "gemFluorite", { generated })
            r(2, "sulfur_gem", "gemSulfur", "gemSulfur", { generated })
            r(3, "miragium_dust", "dustMiragium", "dustMiragium", { generated })
            r(4, "miragium_tiny_dust", "dustTinyMiragium", "dustTinyMiragium", { generated })
            r(5, "miragium_ingot", "ingotMiragium", "ingotMiragium", { generated })
            r(6, "cinnabar_gem", "gemCinnabar", "gemCinnabar", { generated })
            r(7, "moonstone_gem", "gemMoonstone", "gemMoonstone", { generated })
            r(8, "magnetite_gem", "gemMagnetite", "gemMagnetite", { generated })
            r(9, "saltpeter_gem", "gemSaltpeter", "gemSaltpeter", { generated })
            r(10, "pyrope_gem", "gemPyrope", "gemPyrope", { generated })
            r(11, "smithsonite_gem", "gemSmithsonite", "gemSmithsonite", { generated })
            r(12, "miragium_rod", "rodMiragium", "rodMiragium", { handheld })
            r(13, "miragium_nugget", "nuggetMiragium", "nuggetMiragium", { generated })
            r(14, "nephrite_gem", "gemNephrite", "gemNephrite", { generated })
            r(15, "topaz_gem", "gemTopaz", "gemTopaz", { generated })
            r(16, "tourmaline_gem", "gemTourmaline", "gemTourmaline", { generated })
            r(17, "heliolite_gem", "gemHeliolite", "gemHeliolite", { generated })
            r(18, "labradorite_gem", "gemLabradorite", "gemLabradorite", { generated })
            r(19, "lilagium_ingot", "ingotLilagium", "ingotLilagium", { generated })
            r(20, "miragium_plate", "plateMiragium", "plateMiragium", { generated })
            r(21, "coal_dust", "dustCoal", "dustCoal", { generated }).fuel(1600)
            r(22, "charcoal_dust", "dustCharcoal", "dustCharcoal", { generated }).fuel(1600)
            r(23, "apatite_dust", "dustApatite", "dustApatite", { generated })
            r(24, "fluorite_dust", "dustFluorite", "dustFluorite", { generated })
            r(25, "sulfur_dust", "dustSulfur", "dustSulfur", { generated })
            r(26, "cinnabar_dust", "dustCinnabar", "dustCinnabar", { generated })
            r(27, "moonstone_dust", "dustMoonstone", "dustMoonstone", { generated })
            r(28, "magnetite_dust", "dustMagnetite", "dustMagnetite", { generated })

            onRegisterItem {
                if (ApiMain.side.isClient) item.setCustomModelResourceLocations()
            }
        }
        onMakeLang {
            enJa("item.gemApatite.name", "Apatite", "燐灰石")
            enJa("item.gemFluorite.name", "Fluorite", "蛍石")
            enJa("item.gemSulfur.name", "Sulfur", "硫黄")
            enJa("item.dustMiragium.name", "Miragium Dust", "ミラジウムの粉")
            enJa("item.dustTinyMiragium.name", "Tiny Pile of Miragium Dust", "ミラジウムの微粉")
            enJa("item.ingotMiragium.name", "Miragium Ingot", "ミラジウムインゴット")
            enJa("item.gemCinnabar.name", "Cinnabar", "辰砂")
            enJa("item.gemMoonstone.name", "Moonstone", "月長石")
            enJa("item.gemMagnetite.name", "Magnetite", "磁鉄鉱")
            enJa("item.gemSaltpeter.name", "Saltpeter", "硝石")
            enJa("item.gemPyrope.name", "Pyrope", "パイロープ")
            enJa("item.gemSmithsonite.name", "Smithsonite", "スミソナイト")
            enJa("item.rodMiragium.name", "Miragium Rod", "ミラジウムの棒")
            enJa("item.nuggetMiragium.name", "Miragium Nugget", "ミラジウムの塊")
            enJa("item.gemNephrite.name", "Nephrite", "ネフライト")
            enJa("item.gemTopaz.name", "Topaz", "トパーズ")
            enJa("item.gemTourmaline.name", "Tourmaline", "トルマリン")
            enJa("item.gemHeliolite.name", "Heliolite", "ヘリオライト")
            enJa("item.gemLabradorite.name", "Labradorite", "ラブラドライト")
            enJa("item.ingotLilagium.name", "Lilagium Ingot", "リラジウムインゴット")
            enJa("item.plateMiragium.name", "Miragium Plate", "ミラジウムの板")
            enJa("item.dustCoal.name", "Coal Dust", "石炭の粉")
            enJa("item.dustCharcoal.name", "Charcoal Dust", "木炭の粉")
            enJa("item.dustApatite.name", "Apatite Dust", "燐灰石の粉")
            enJa("item.dustFluorite.name", "Fluorite Dust", "蛍石の粉")
            enJa("item.dustSulfur.name", "Sulfur Dust", "硫黄の粉")
            enJa("item.dustCinnabar.name", "Cinnabar Dust", "辰砂の粉")
            enJa("item.dustMoonstone.name", "Moonstone Dust", "月長石の粉")
            enJa("item.dustMagnetite.name", "Magnetite Dust", "磁鉄鉱の粉")
        }


        // レシピ

        // 破砕のワンドによる粉砕
        fun makeDustRecipe(registryName: String, ingredient: DataIngredient, metadata: Int) {
            makeRecipe(
                ResourceName(ModMirageFairy2019.MODID, registryName),
                DataShapelessRecipe(
                    ingredients = listOf(
                        ingredient,
                        DataOreIngredient(type = "miragefairy2019:ore_dict_complex", ore = "mirageFairy2019CraftingToolFairyWandBreaking")
                    ),
                    result = DataResult(
                        item = "miragefairy2019:materials",
                        data = metadata
                    )
                )
            )
        }
        makeDustRecipe("apatite_dust", DataOreIngredient(ore = "gemApatite"), 23)
        makeDustRecipe("fluorite_dust", DataOreIngredient(ore = "gemFluorite"), 24)
        makeDustRecipe("sulfur_dust", DataOreIngredient(ore = "gemSulfur"), 25)
        makeDustRecipe("cinnabar_dust", DataOreIngredient(ore = "gemCinnabar"), 26)
        makeDustRecipe("moonstone_dust", DataOreIngredient(ore = "gemMoonstone"), 27)
        makeDustRecipe("magnetite_dust", DataOreIngredient(ore = "gemMagnetite"), 28)
        makeDustRecipe("coal_dust", DataSimpleIngredient(item = "minecraft:coal", data = 0), 21)
        makeDustRecipe("charcoal_dust", DataSimpleIngredient(item = "minecraft:coal", data = 1), 22)


        // 金床による粉砕
        onInit {
            fun registerAnvilPulverization(ingredient: Ingredient, metadata: Int) {
                MinecraftForge.EVENT_BUS.register(object {
                    @SubscribeEvent
                    fun handle(event: AnvilUpdateEvent) {
                        if (!ingredient.test(event.left)) return
                        if (!ingredient.test(event.right)) return
                        val count = event.left.count + event.right.count
                        if (count > 64) return
                        event.output = itemMaterials().createItemStack(count = count, metadata = metadata)
                        event.cost = count
                    }

                    @SubscribeEvent
                    fun handle(event: ItemTooltipEvent) {
                        if (ingredient.test(event.itemStack)) {
                            event.toolTip += formattedText { (!"金床で同アイテムを組み合わせて粉砕可能").red } // TODO translate
                        }
                    }
                })
            }
            registerAnvilPulverization("gemApatite".oreIngredient, 23)
            registerAnvilPulverization("gemFluorite".oreIngredient, 24)
            registerAnvilPulverization("gemSulfur".oreIngredient, 25)
            registerAnvilPulverization("gemCinnabar".oreIngredient, 26)
            registerAnvilPulverization("gemMoonstone".oreIngredient, 27)
            registerAnvilPulverization("gemMagnetite".oreIngredient, 28)
            registerAnvilPulverization(Items.COAL.createItemStack(metadata = 0).ingredient, 21)
            registerAnvilPulverization(Items.COAL.createItemStack(metadata = 1).ingredient, 22)
        }

    }
}

class ItemSimpleMaterials : ItemMultiMaterial<ItemVariantSimpleMaterials>() {
    override fun getItemBurnTime(itemStack: ItemStack) = getVariant(itemStack)?.burnTime ?: -1
}

class ItemVariantSimpleMaterials(registryName: String, unlocalizedName: String) : ItemVariantMaterial(registryName, unlocalizedName) {
    var burnTime: Int? = null
}
