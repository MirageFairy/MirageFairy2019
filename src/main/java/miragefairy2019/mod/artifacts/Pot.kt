package miragefairy2019.mod.artifacts

import miragefairy2019.lib.modinitializer.addOreName
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.itemVariant
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.resourcemaker.DataIngredient
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.DataShapelessRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.generated
import miragefairy2019.lib.resourcemaker.lang
import miragefairy2019.lib.resourcemaker.makeItemModel
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.libkt.CapabilityProviderAdapter
import miragefairy2019.libkt.ItemMultiMaterial
import miragefairy2019.libkt.ItemVariantMaterial
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.setCustomModelResourceLocations
import miragefairy2019.mod.Main
import miragefairy2019.mod.ModMirageFairy2019
import mirrg.kotlin.gson.hydrogen.jsonElement
import mirrg.kotlin.gson.hydrogen.jsonObject
import mirrg.kotlin.hydrogen.castOrNull
import mirrg.kotlin.hydrogen.toUpperCamelCase
import mirrg.kotlin.hydrogen.toUpperCaseHead
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fluids.capability.FluidTankProperties
import net.minecraftforge.fluids.capability.IFluidHandlerItem
import net.minecraftforge.oredict.OreIngredient

enum class PotCard(
    val fluidRegistryName: String,
    val fluidUnlocalizedName: String,
    val fluidEnglishName: String,
    val fluidJapaneseName: String
) {
    MIRAGIUM_WATER_POT("miragium_water", "miragiumWater", "Miragium Water", "ミラジウムウォーター"),
    MIRAGE_FLOWER_EXTRACT_POT("mirage_flower_extract", "mirageFlowerExtract", "Mirage Extract", "ミラージュエキス"),
    MIRAGE_FLOWER_OIL_POT("mirage_flower_oil", "mirageFlowerOil", "Mirage Oil", "ミラージュオイル"),
    WATER_POT("water", "water", "Water", "水"),
    LAVA_POT("lava", "lava", "Lava", "溶岩"),
    MIRAGE_FAIRY_BLOOD_POT("mirage_fairy_blood", "mirageFairyBlood", "Mirage Fairy Blood", "妖精の血"),
}


lateinit var itemPot: () -> ItemPot
lateinit var itemFilledPot: () -> ItemFilledPot

val potModule = module {

    // 中身なしポット
    itemPot = item({ ItemPot() }, "pot") {
        setUnlocalizedName("mirageFairyPot")
        addOreName("mirageFairyPot")
        setCreativeTab { Main.creativeTab }
        setCustomModelResourceLocation()
    }
    makeItemModel("pot") { generated }
    makeRecipe("pot") {
        DataShapedRecipe(
            pattern = listOf(
                "# #",
                " # "
            ),
            key = mapOf(
                "#" to DataOreIngredient(ore = "blockMirageFairyCrystal")
            ),
            result = DataResult(item = "${ModMirageFairy2019.MODID}:pot")
        )
    }
    lang("item.mirageFairyPot.name", "Fairy Pot", "妖精のポット")


    // 中身入りポット
    itemFilledPot = item({ ItemFilledPot() }, "filled_bucket") {
        setUnlocalizedName("filledBucket")
        setCreativeTab { Main.creativeTab }

        itemVariant("miragium_water_pot", {
            ItemVariantFilledPot(it, "mirageFairyPotMiragiumWater", "miragium_water")
        }, 0) {
            addOreName("mirageFairyPotMiragiumWater")
            addOreName("container1000MiragiumWater")
        }
        itemVariant("mirage_flower_extract_pot", {
            ItemVariantFilledPot(it, "mirageFairyPotMirageFlowerExtract", "mirage_flower_extract")
        }, 1) {
            addOreName("mirageFairyPotMirageFlowerExtract")
            addOreName("container1000MirageFlowerExtract")
        }
        itemVariant("mirage_flower_oil_pot", {
            ItemVariantFilledPot(it, "mirageFairyPotMirageFlowerOil", "mirage_flower_oil")
        }, 2) {
            addOreName("mirageFairyPotMirageFlowerOil")
            addOreName("container1000MirageFlowerOil")
        }
        itemVariant("water_pot", {
            ItemVariantFilledPot(it, "mirageFairyPotWater", "water")
        }, 3) {
            addOreName("mirageFairyPotWater")
            addOreName("container1000Water")
        }
        itemVariant("lava_pot", {
            ItemVariantFilledPot(it, "mirageFairyPotLava", "lava")
        }, 4) {
            addOreName("mirageFairyPotLava")
            addOreName("container1000Lava")
        }
        itemVariant("mirage_fairy_blood_pot", {
            ItemVariantFilledPot(it, "mirageFairyPotMirageFairyBlood", "mirage_fairy_blood")
        }, 5) {
            addOreName("mirageFairyPotMirageFairyBlood")
            addOreName("container1000MirageFairyBlood")
        }

        onRegisterItem {
            if (Main.side.isClient) item.setCustomModelResourceLocations()
        }
    }
    PotCard.values().forEach { potCard ->
        makeItemModel("${potCard.fluidRegistryName}_pot") { generated }
        lang("item.mirageFairyPot${potCard.fluidUnlocalizedName.toUpperCaseHead()}.name", "${potCard.fluidEnglishName} Pot", "${potCard.fluidJapaneseName}入りポット")
    }


    // 詰め替えレシピ

    fun containerToBucket(fluidName: String, fluidOreSuffix: String, result: DataResult) = makeRecipe("pot/${fluidName}_container_to_${fluidName}_bucket") {
        DataShapelessRecipe(
            ingredients = listOf(
                DataSimpleIngredient(item = "minecraft:bucket"),
                DataOreIngredient(ore = "container1000$fluidOreSuffix")
            ),
            result = result
        )
    }

    fun containerToUniversalBucket(fluidName: String, fluidOreSuffix: String) = containerToBucket(
        fluidName,
        fluidOreSuffix,
        DataResult(
            item = "forge:bucketfilled",
            nbt = jsonObject(
                "FluidName" to fluidName.jsonElement,
                "Amount" to 1000.jsonElement
            )
        )
    )

    fun itemToPot(fluidName: String, potMetadata: Int, input: DataIngredient) = makeRecipe("pot/${fluidName}_pot_from_${fluidName}_bucket") {
        DataShapelessRecipe(
            ingredients = listOf(
                DataOreIngredient(ore = "mirageFairyPot"),
                input
            ),
            result = DataResult(item = "miragefairy2019:filled_bucket", data = potMetadata)
        )
    }

    fun universalBucketToPot(fluidName: String, potMetadata: Int) = itemToPot(
        fluidName,
        potMetadata,
        DataSimpleIngredient(
            type = "minecraft:item_nbt",
            item = "forge:bucketfilled",
            nbt = jsonObject(
                "FluidName" to fluidName.jsonElement,
                "Amount" to 1000.jsonElement
            )
        )
    )

    fun bottleToPot(registryName: String, fluidOreSuffix: String, potMetadata: Int) = makeRecipe("pot/${registryName}_bottle_to_${registryName}_pot") {
        DataShapelessRecipe(
            ingredients = listOf(
                DataOreIngredient(ore = "mirageFairyPot"),
                DataOreIngredient(ore = "container250$fluidOreSuffix"),
                DataOreIngredient(ore = "container250$fluidOreSuffix"),
                DataOreIngredient(ore = "container250$fluidOreSuffix"),
                DataOreIngredient(ore = "container250$fluidOreSuffix")
            ),
            result = DataResult(item = "miragefairy2019:filled_bucket", data = potMetadata)
        )
    }

    fun potToBottle(registryName: String, fluidOreSuffix: String, bottleMetadata: Int) = makeRecipe("pot/${registryName}_pot_to_${registryName}_bottle") {
        DataShapelessRecipe(
            ingredients = listOf(
                DataSimpleIngredient(item = "minecraft:glass_bottle"),
                DataSimpleIngredient(item = "minecraft:glass_bottle"),
                DataSimpleIngredient(item = "minecraft:glass_bottle"),
                DataSimpleIngredient(item = "minecraft:glass_bottle"),
                DataOreIngredient(ore = "container1000$fluidOreSuffix")
            ),
            result = DataResult(item = "miragefairy2019:fairy_materials", data = bottleMetadata, count = 4)
        )
    }

    containerToUniversalBucket("miragium_water", "MiragiumWater")
    universalBucketToPot("miragium_water", 0)
    bottleToPot("miragium_water", "MiragiumWater", 0)
    potToBottle("miragium_water", "MiragiumWater", 10)

    containerToUniversalBucket("mirage_flower_extract", "MirageFlowerExtract")
    universalBucketToPot("mirage_flower_extract", 1)
    bottleToPot("mirage_flower_extract", "MirageFlowerExtract", 1)
    potToBottle("mirage_flower_extract", "MirageFlowerExtract", 11)

    containerToUniversalBucket("mirage_flower_oil", "MirageFlowerOil")
    universalBucketToPot("mirage_flower_oil", 2)
    bottleToPot("mirage_flower_oil", "MirageFlowerOil", 2)
    potToBottle("mirage_flower_oil", "MirageFlowerOil", 12)

    containerToBucket("water", "Water", DataResult(item = "minecraft:water_bucket"))
    itemToPot("water", 3, DataSimpleIngredient(item = "minecraft:water_bucket"))

    containerToBucket("lava", "Lava", DataResult(item = "minecraft:lava_bucket"))
    itemToPot("lava", 4, DataSimpleIngredient(item = "minecraft:lava_bucket"))

    containerToUniversalBucket("mirage_fairy_blood", "MirageFairyBlood")
    universalBucketToPot("mirage_fairy_blood", 5)
    bottleToPot("mirage_fairy_blood", "MirageFairyBlood", 5)
    potToBottle("mirage_fairy_blood", "MirageFairyBlood", 24)

}

interface IPot {
    fun getFluid(itemStack: ItemStack): Fluid?
}

class ItemPot : Item(), IPot {
    override fun initCapabilities(itemStack: ItemStack, nbt: NBTTagCompound?) = object : CapabilityProviderAdapter() {
        override fun <T> getCapabilityImpl(capability: Capability<T>, facing: EnumFacing?): (() -> T)? = when (capability) {
            CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY -> ({ CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.cast(FluidHandlerPot(itemStack)) })
            else -> null
        }
    }

    override fun getFluid(itemStack: ItemStack): Fluid? = null
}

class ItemVariantFilledPot(
    registryName: String,
    unlocalizedName: String,
    val fluidName: String
) : ItemVariantMaterial(registryName, unlocalizedName)

class ItemFilledPot : ItemMultiMaterial<ItemVariantFilledPot>(), IPot {
    override fun hasContainerItem(itemStack: ItemStack) = true
    override fun getContainerItem(itemStack: ItemStack) = ItemStack(itemPot.invoke())
    override fun initCapabilities(itemStack: ItemStack, nbt: NBTTagCompound?) = object : CapabilityProviderAdapter() {
        override fun <T> getCapabilityImpl(capability: Capability<T>, facing: EnumFacing?): (() -> T)? = when (capability) {
            CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY -> ({ CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.cast(FluidHandlerPot(itemStack)) })
            else -> null
        }
    }

    override fun getFluid(itemStack: ItemStack): Fluid? = FluidRegistry.getFluid(getVariant(itemStack)?.fluidName)
}

class FluidHandlerPot(@JvmField var container: ItemStack) : IFluidHandlerItem {
    private fun getFluid() = container.item.castOrNull<IPot>()?.getFluid(container)
    override fun getContainer() = container
    override fun getTankProperties() = getFluid()?.let { fluid -> arrayOf(FluidTankProperties(FluidStack(fluid, 1000), 1000)) }
    override fun fill(resource: FluidStack, doFill: Boolean): Int {
        if (container.count != 1) return 0 // 複数スタックされている場合は不可
        if (getFluid() != null) return 0 // 中身が入っている場合は不可
        if (resource.amount < 1000) return 0 // 入力が1000に満たない場合は不可
        val result = OreIngredient("mirageFairyPot${resource.fluid.name.toUpperCamelCase()}").matchingStacks.getOrNull(0)?.copy() ?: return 0 // 対応していない場合は不可

        if (doFill) container = result
        return 1000
    }

    override fun drain(resource: FluidStack, doDrain: Boolean): FluidStack? {
        if (container.count != 1) return null // 複数スタックされている場合は不可
        if (resource.amount < 1000) return null // 一括で取り出せない場合は不可
        val fluid = getFluid() ?: return null // 中身が空の場合は不可
        if (resource.fluid != fluid || resource.tag != null) return null // 液体の種類が異なる場合は不可

        if (doDrain) container = itemPot().createItemStack()
        return FluidStack(fluid, 1000)
    }

    override fun drain(maxDrain: Int, doDrain: Boolean): FluidStack? {
        if (container.count != 1) return null // 複数スタックされている場合は不可
        if (maxDrain < 1000) return null // 一括で取り出せない場合は不可
        val fluid = getFluid() ?: return null // 中身が空の場合は不可

        if (doDrain) container = itemPot().createItemStack()
        return FluidStack(fluid, 1000)
    }
}
