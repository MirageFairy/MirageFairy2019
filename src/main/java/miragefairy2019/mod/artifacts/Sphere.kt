package miragefairy2019.mod.artifacts

import miragefairy2019.api.Erg
import miragefairy2019.lib.IColoredItem
import miragefairy2019.lib.displayName
import miragefairy2019.lib.registerItemColorHandler
import miragefairy2019.libkt.EMPTY_ITEM_STACK
import miragefairy2019.libkt.ItemMulti
import miragefairy2019.libkt.ItemVariant
import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.item
import miragefairy2019.libkt.makeItemModel
import miragefairy2019.libkt.module
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.translateToLocal
import miragefairy2019.libkt.translateToLocalFormatted
import miragefairy2019.mod.Main.creativeTab
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.fairystickcraft.ApiFairyStickCraft
import miragefairy2019.mod.fairystickcraft.FairyStickCraftConditionConsumeBlock
import miragefairy2019.mod.fairystickcraft.FairyStickCraftConditionConsumeItem
import miragefairy2019.mod.fairystickcraft.FairyStickCraftConditionSpawnItem
import miragefairy2019.mod.fairystickcraft.FairyStickCraftConditionUseItem
import miragefairy2019.mod.fairystickcraft.FairyStickCraftRecipe
import mirrg.boron.util.UtilsString
import mirrg.kotlin.gson.jsonElement
import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.oredict.OreIngredient

object Sphere {
    lateinit var itemSpheres: () -> ItemSpheres
    val module = module {

        // アイテム
        itemSpheres = item({ ItemSpheres() }, "spheres") {
            setUnlocalizedName("spheres")
            setCreativeTab { creativeTab }
            onRegisterItem {
                Erg.values().forEachIndexed { i, ergType ->
                    item.registerVariant(i, VariantSphere(ergType.sphereType))
                    item.setCustomModelResourceLocation(i, model = ResourceLocation(ModMirageFairy2019.MODID, "sphere"))
                }
            }
            registerItemColorHandler()
        }
        makeItemModel(ResourceName(ModMirageFairy2019.MODID, "sphere")) {
            jsonElement(
                "parent" to "item/generated".jsonElement,
                "textures" to jsonElement(
                    "layer0" to "miragefairy2019:items/sphere_layer0".jsonElement,
                    "layer1" to "miragefairy2019:items/sphere_layer1".jsonElement,
                    "layer2" to "miragefairy2019:items/sphere_layer2".jsonElement,
                    "layer3" to "miragefairy2019:items/sphere_layer3".jsonElement
                )
            )
        }


        // 鉱石辞書
        onCreateItemStack {
            Erg.values().forEachIndexed { meta, ergType ->
                val itemStack = itemSpheres().getVariant(meta)!!.createItemStack()
                OreDictionary.registerOre(ergType.sphereType.oreName, itemStack)
                OreDictionary.registerOre("mirageFairy2019SphereAny", itemStack)
            }
        }

        // レシピ登録
        onAddRecipe {
            Erg.values().forEachIndexed { meta, ergType ->
                val sphere = ergType.sphereType
                val outputItemStack = itemSpheres().getVariant(meta)!!.createItemStack()

                fun register(suffix: String, vararg additionalIngredients: Ingredient) {
                    val ingredients = listOf(
                        OreIngredient("mirageFairy2019FairyAbility${UtilsString.toUpperCaseHead(ergType.toString())}"),
                        *additionalIngredients
                    )

                    // クラフトレシピ
                    GameRegistry.addShapelessRecipe(
                        ResourceLocation("${ModMirageFairy2019.MODID}:${ergType}_sphere_$suffix"),
                        ResourceLocation("${ModMirageFairy2019.MODID}:${ergType}_sphere"),
                        outputItemStack,
                        OreIngredient("mirageFairyStick"),
                        OreIngredient("container1000MiragiumWater"),
                        *ingredients.toTypedArray()
                    )

                    // 妖精のステッキレシピ
                    ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().apply {
                        conditions.add(FairyStickCraftConditionUseItem(OreIngredient("mirageFairyStick")))
                        conditions.add(FairyStickCraftConditionConsumeBlock { FluidMaterials.blockFluidMiragiumWater().defaultState })
                        ingredients.forEach { conditions.add(FairyStickCraftConditionConsumeItem(it)) }
                        conditions.add(FairyStickCraftConditionSpawnItem { outputItemStack })
                    })
                }

                sphere.catalystSupplier.let { register("with_fluorite", OreIngredient("gemFluorite"), it()) } // 蛍石触媒レシピ
                sphere.catalystSupplier.let { register("with_sphere_base", OreIngredient("mirageFairy2019SphereBase"), it()) } // スフィアベース触媒レシピ
                sphere.gemSupplier?.let { register("from_gem", it()) } // 宝石レシピ

            }

        }

    }
}


class SphereType(
    val ergType: Erg,
    val colorCore: Int,
    val colorHighlight: Int,
    val colorBackground: Int,
    val colorPlasma: Int,
    val catalystSupplier: () -> Ingredient,
    val gemSupplier: (() -> Ingredient)?
)

val SphereType.oreName: String get() = "mirageFairy2019Sphere${UtilsString.toUpperCaseHead(ergType.toString())}"

val Erg.sphereType
    get(): SphereType {
        fun stack(itemStack: () -> ItemStack): () -> Ingredient = { Ingredient.fromStacks(itemStack()) }
        fun item(item: () -> Item): () -> Ingredient = stack { ItemStack(item()) }
        fun block(block: () -> Block): () -> Ingredient = stack { ItemStack(block()) }
        fun ore(oreName: String): () -> Ingredient = { OreIngredient(oreName) }
        return when (this) {
            Erg.ATTACK -> SphereType(Erg.ATTACK, 0xFFA0A0, 0xFF6B6B, 0xC70000, 0xFF0000, item { Items.IRON_SWORD }, null)
            Erg.CRAFT -> SphereType(Erg.CRAFT, 0xF1B772, 0xD3FDCC, 0x92B56A, 0xFFFFFF, ore("workbench"), ore("gemNephrite"))
            Erg.HARVEST -> SphereType(Erg.HARVEST, 0x00BD00, 0xD09D74, 0x6E4219, 0x2FFF2F, item { Items.IRON_AXE }, null)
            Erg.LIGHT -> SphereType(Erg.LIGHT, 0xFF8300, 0xFFC9BC, 0xF1C483, 0xFFFF25, ore("torch"), ore("gemTopaz"))
            Erg.FLAME -> SphereType(Erg.FLAME, 0xFF9F68, 0xFF6800, 0xE2713F, 0xBF1805, item { Items.FIRE_CHARGE }, ore("gemHeliolite"))
            Erg.WATER -> SphereType(Erg.WATER, 0x67E6FF, 0xBDF0FF, 0x00ABFF, 0x83B5FF, item { Items.WATER_BUCKET }, null)
            Erg.CRYSTAL -> SphereType(Erg.CRYSTAL, 0xA2FFFF, 0xB6FFFF, 0x36CECE, 0xEBFFFF, ore("gemQuartz"), ore("gemDiamond"))
            Erg.SOUND -> SphereType(Erg.SOUND, 0x98ACE7, 0xD8DDFF, 0xBFC9D8, 0xC9D0ED, block { Blocks.NOTEBLOCK }, null)
            Erg.SPACE -> SphereType(Erg.SPACE, 0x000000, 0x4D0065, 0x67009D, 0x001E74, block { Blocks.CHEST }, null)
            Erg.WARP -> SphereType(Erg.WARP, 0x3A00D3, 0x8CF4E2, 0x349988, 0xD004FB, ore("enderpearl"), null)
            Erg.SHOOT -> SphereType(Erg.SHOOT, 0x969696, 0x896727, 0x896727, 0xD8D8D8, item { Items.BOW }, null)
            Erg.DESTROY -> SphereType(Erg.DESTROY, 0xFFFFFF, 0xFF5A35, 0xFF4800, 0x000000, block { Blocks.TNT }, null)
            Erg.CHEMICAL -> SphereType(Erg.CHEMICAL, 0x0067FF, 0xC9DFEF, 0xB0C4D7, 0x0755FF, item { Items.FERMENTED_SPIDER_EYE }, null)
            Erg.SLASH -> SphereType(Erg.SLASH, 0xAAAAAA, 0xFFC9B2, 0xD20000, 0xFFFFFF, item { Items.SHEARS }, null)
            Erg.LIFE -> SphereType(Erg.LIFE, 0xFF0033, 0xFFC9DE, 0xFF8EB2, 0xFF3F67, item { Items.BEEF }, ore("gemPyrope"))
            Erg.KNOWLEDGE -> SphereType(Erg.KNOWLEDGE, 0xFFFF00, 0x006200, 0x00A000, 0x50DD00, item { Items.BOOK }, null)
            Erg.ENERGY -> SphereType(Erg.ENERGY, 0xFFED30, 0xFFF472, 0xFFE84C, 0xBFE7FF, ore("gemCoal"), null)
            Erg.SUBMISSION -> SphereType(Erg.SUBMISSION, 0xFF0000, 0x593232, 0x1E1E1E, 0xA90000, block { Blocks.IRON_BARS }, null)
            Erg.CHRISTMAS -> SphereType(Erg.CHRISTMAS, 0xFF0000, 0xFFD723, 0x00B900, 0xFF0000, stack { ItemStack(Blocks.SAPLING, 1, 1) }, null)
            Erg.FREEZE -> SphereType(Erg.FREEZE, 0x5AFFFF, 0xFFFFFF, 0xF6FFFF, 0xACFFFF, ore("ice"), null)
            Erg.THUNDER -> SphereType(Erg.THUNDER, 0xFFFFB2, 0x359C00, 0xC370A7, 0xFFFF00, item { Items.GOLDEN_SWORD }, ore("gemTourmaline"))
            Erg.LEVITATE -> SphereType(Erg.LEVITATE, 0x00A2FF, 0xB7ECFF, 0x35366B, 0x8CD0FF, item { Items.FEATHER }, ore("gemLabradorite"))
            Erg.SENSE -> SphereType(Erg.SENSE, 0x1B3211, 0xD3E6DF, 0x7ACF45, 0x4784A0, item { Items.SPIDER_EYE }, null)
        }
    }


class VariantSphere(val sphere: SphereType) : ItemVariant()

class ItemSpheres : ItemMulti<VariantSphere>(), IColoredItem {
    override fun getItemStackDisplayName(itemStack: ItemStack): String {
        val variant = getVariant(itemStack) ?: return translateToLocal("$unlocalizedName.name")
        return translateToLocalFormatted("$unlocalizedName.format", variant.sphere.ergType.displayName.formattedText)
    }

    @SideOnly(Side.CLIENT)
    override fun colorMultiplier(itemStack: ItemStack, tintIndex: Int): Int {
        val variant = Sphere.itemSpheres().getVariant(itemStack) ?: return 0xFFFFFF
        return when (tintIndex) {
            0 -> variant.sphere.colorBackground
            1 -> variant.sphere.colorPlasma
            2 -> variant.sphere.colorCore
            3 -> variant.sphere.colorHighlight
            else -> 0xFFFFFF
        }
    }
}

operator fun (() -> ItemSpheres).get(ergType: Erg) = this().getVariant(ergType.ordinal)?.createItemStack() ?: EMPTY_ITEM_STACK
