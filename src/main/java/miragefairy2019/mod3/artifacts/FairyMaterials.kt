package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.DataOreIngredient
import miragefairy2019.libkt.DataResult
import miragefairy2019.libkt.DataShapedRecipe
import miragefairy2019.libkt.EMPTY_ITEM_STACK
import miragefairy2019.libkt.ItemInitializer
import miragefairy2019.libkt.ItemVariantInitializer
import miragefairy2019.libkt.Module
import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.addOreName
import miragefairy2019.libkt.aqua
import miragefairy2019.libkt.canTranslate
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.item
import miragefairy2019.libkt.itemVariant
import miragefairy2019.libkt.makeRecipe
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.translateToLocal
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.lib.ItemMultiMaterial
import miragefairy2019.mod.lib.ItemVariantMaterial
import miragefairy2019.mod.lib.setCustomModelResourceLocations
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionConsumeBlock
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionConsumeItem
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionSpawnItem
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionUseItem
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftRecipe
import miragefairy2019.mod3.fairystickcraft.api.ApiFairyStickCraft
import miragefairy2019.mod3.main.api.ApiMain
import mirrg.kotlin.toUpperCamelCase
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.OreIngredient

private typealias Iifm = ItemInitializer<ItemMultiFairyMaterial>
private typealias Ivifm = ItemVariantInitializer<ItemMultiFairyMaterial, ItemVariantFairyMaterial>


object FairyMaterials {
    val module: Module = {

        // 妖精素材アイテム
        item({ ItemMultiFairyMaterial() }, "fairy_materials") {
            setUnlocalizedName("fairyMaterials")
            setCreativeTab { ApiMain.creativeTab }
            itemVariants = ItemVariants(this)
            onRegisterItem {
                if (ApiMain.side.isClient) item.setCustomModelResourceLocations()
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
                it.conditions += FairyStickCraftConditionSpawnItem { itemVariants.manaRodQuartz.createItemStack() }
            })

            // ミラ葉＋骨＋燐灰石→ミラ茎
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
                it.conditions += FairyStickCraftConditionUseItem(OreIngredient("mirageFairyStick"))
                it.conditions += FairyStickCraftConditionConsumeBlock { FluidMaterials.blockFluidMiragiumWater().defaultState }
                it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("mirageFairy2019FairyAbilityCrystal"))
                it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("leafMirageFlower"))
                it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("dustApatite"))
                it.conditions += FairyStickCraftConditionSpawnItem { itemVariants.stickMirageFlower.createItemStack() }
            })

            // 空き瓶＋ミラ種50個＋辰砂の粉4個＞珠玉→ミラオイル瓶
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
                it.conditions += FairyStickCraftConditionUseItem(OreIngredient("mirageFairy2019CraftingToolFairyWandPolishing"))
                it.conditions += FairyStickCraftConditionConsumeItem(Ingredient.fromItem(Items.GLASS_BOTTLE))
                it.conditions += FairyStickCraftConditionConsumeItem(Ingredient.fromItem(MirageFlower.itemMirageFlowerSeeds()), 50)
                it.conditions += FairyStickCraftConditionConsumeItem(OreIngredient("dustCinnabar"), 4)
                it.conditions += FairyStickCraftConditionSpawnItem { itemVariants.bottleMirageFlowerOil.createItemStack() }
            })

        }

        // 5棒＋8樹液→松明8
        makeRecipe(
            ResourceName(ModMirageFairy2019.MODID, "torch_from_fairy_wood_resin"),
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
        )

        // スフィアベース
        fun makeSphereBaseRecipe(materialName: String) = makeRecipe(
            ResourceName(ModMirageFairy2019.MODID, "sphere_base_from_$materialName"),
            DataShapedRecipe(
                pattern = listOf(
                    " Gp",
                    "GDG",
                    "fG "
                ),
                key = mapOf(
                    "p" to DataOreIngredient(type = "miragefairy2019:ore_dict_complex", ore = "mirageFairy2019CraftingToolFairyWandPolishing"),
                    "f" to DataOreIngredient(type = "miragefairy2019:ore_dict_complex", ore = "mirageFairy2019CraftingToolFairyWandFusion"),
                    "D" to DataOreIngredient(ore = "dustMiragium"),
                    "G" to DataOreIngredient(ore = "gem${materialName.toUpperCamelCase()}")
                ),
                result = DataResult(
                    item = "miragefairy2019:fairy_materials",
                    data = 16,
                    count = 4
                )
            )
        )
        makeSphereBaseRecipe("diamond")
        makeSphereBaseRecipe("emerald")
        makeSphereBaseRecipe("pyrope")
        makeSphereBaseRecipe("smithsonite")
        makeSphereBaseRecipe("nephrite")
        makeSphereBaseRecipe("topaz")
        makeSphereBaseRecipe("tourmaline")
        makeSphereBaseRecipe("heliolite")
        makeSphereBaseRecipe("labradorite")

    }


    lateinit var itemVariants: ItemVariants

    class ItemVariants(private val i: Iifm) {
        private fun iv(metadata: Int, registryName: String, unlocalizedName: String, tier: Int, oreNames: List<String>): Ivifm {
            return i.itemVariant(registryName, { ItemVariantFairyMaterial(it, unlocalizedName, tier) }, metadata) {
                oreNames.forEach { addOreName(it) }
            }
        }

        private fun Ivifm.bottle() = also { itemInitializer.modInitializer.onRegisterItem { itemVariant.containerItemSupplier = { ItemStack(Items.GLASS_BOTTLE) } } }
        private fun Ivifm.fuel(burnTime: Int) = also { itemInitializer.modInitializer.onRegisterItem { itemVariant.burnTime = burnTime } }

        val manaRodShine = iv(0, "shine_mana_rod", "manaRodShine", 3, listOf("mirageFairy2019ManaRodShine"))
        val manaRodFire = iv(1, "fire_mana_rod", "manaRodFire", 3, listOf("mirageFairy2019ManaRodFire"))
        val manaRodWind = iv(2, "wind_mana_rod", "manaRodWind", 3, listOf("mirageFairy2019ManaRodWind"))
        val manaRodGaia = iv(3, "gaia_mana_rod", "manaRodGaia", 3, listOf("mirageFairy2019ManaRodGaia"))
        val manaRodAqua = iv(4, "aqua_mana_rod", "manaRodAqua", 3, listOf("mirageFairy2019ManaRodAqua"))
        val manaRodDark = iv(5, "dark_mana_rod", "manaRodDark", 3, listOf("mirageFairy2019ManaRodDark"))
        val manaRodQuartz = iv(6, "quartz_mana_rod", "manaRodQuartz", 3, listOf("mirageFairy2019ManaRodQuartz"))
        val stickMirageFlower = iv(7, "mirage_flower_stick", "stickMirageFlower", 1, listOf("stickMirageFlower"))
        val leafMirageFlower = iv(8, "mirage_flower_leaf", "leafMirageFlower", 0, listOf("leafMirageFlower"))
        val stickMirageFairyWood = iv(9, "mirage_fairy_wood_stick", "stickMirageFairyWood", 4, listOf("stickMirageFairyWood"))
        val bottleMiragiumWater = iv(10, "miragium_water_bottle", "bottleMiragiumWater", 0, listOf("bottleMiragiumWater", "container250MiragiumWater")).bottle()
        val bottleMirageFlowerExtract = iv(11, "mirage_flower_extract_bottle", "bottleMirageFlowerExtract", 2, listOf("bottleMirageFlowerExtract", "container250MirageFlowerExtract")).bottle()
        val bottleMirageFlowerOil = iv(12, "mirage_flower_oil_bottle", "bottleMirageFlowerOil", 4, listOf("bottleMirageFlowerOil", "container250MirageFlowerOil")).bottle()
        val manaRodGlass = iv(13, "glass_mana_rod", "manaRodGlass", 2, listOf("mirageFairy2019ManaRodGlass"))
        val mirageFairyLeather = iv(14, "mirage_fairy_leather", "mirageFairyLeather", 4, listOf("mirageFairyLeather"))
        val fairyWoodResin = iv(15, "fairy_wood_resin", "fairyWoodResin", 4, listOf("mirageFairyWoodResin")).fuel(1600)
        val sphereBase = iv(16, "sphere_base", "sphereBase", 3, listOf("mirageFairy2019SphereBase"))
    }
}


class ItemVariantFairyMaterial(registryName: String, unlocalizedName: String, val tier: Int) : ItemVariantMaterial(registryName, unlocalizedName) {
    var burnTime: Int? = null
    var containerItemSupplier: (() -> ItemStack)? = null
    val containerItem get() = containerItemSupplier?.let { it() } ?: EMPTY_ITEM_STACK
}

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
        tooltip += formattedText { (!"Tier ${variant.tier}").aqua }

    }


    override fun getItemBurnTime(itemStack: ItemStack) = getVariant(itemStack)?.burnTime ?: -1


    override fun hasContainerItem(itemStack: ItemStack) = !getContainerItem(itemStack).isEmpty
    override fun getContainerItem(itemStack: ItemStack): ItemStack = getVariant(itemStack)?.containerItem ?: ItemStack.EMPTY
}
