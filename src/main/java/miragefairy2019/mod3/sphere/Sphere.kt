package miragefairy2019.mod3.sphere

import miragefairy2019.libkt.Module
import miragefairy2019.libkt.item
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.api.ore.ApiOre
import miragefairy2019.mod.lib.OreIngredientComplex
import miragefairy2019.mod.lib.UtilsMinecraft
import miragefairy2019.mod.lib.multi.ItemMulti
import miragefairy2019.mod.lib.multi.ItemVariant
import miragefairy2019.mod3.erg.api.ErgTypes
import miragefairy2019.mod3.erg.api.IErgType
import miragefairy2019.mod3.erg.displayName
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionConsumeBlock
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionConsumeItem
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionSpawnItem
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionUseItem
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftRecipe
import miragefairy2019.mod3.fairystickcraft.api.ApiFairyStickCraft
import miragefairy2019.mod3.main.api.ApiMain
import mirrg.boron.util.UtilsString
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.color.IItemColor
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
import java.util.Optional
import java.util.function.Supplier

class ItemSpheres : ItemMulti<VariantSphere>() {
    override fun getItemStackDisplayName(itemStack: ItemStack): String {
        val variant = getVariant(itemStack).orElse(null) ?: return UtilsMinecraft.translateToLocal("$unlocalizedName.name")
        return UtilsMinecraft.translateToLocalFormatted("$unlocalizedName.format", variant.sphere.ergType.displayName.formattedText)
    }
}

class VariantSphere(val sphere: SphereType) : ItemVariant()


class SphereType(
    val ergType: IErgType,
    val colorCore: Int,
    val colorHighlight: Int,
    val colorBackground: Int,
    val colorPlasma: Int,
    val catalystSupplier: () -> Ingredient,
    val gemSupplier: (() -> Ingredient)?
)

val SphereType.oreName: String get() = "mirageFairy2019Sphere${UtilsString.toUpperCaseHead(ergType.name)}"

fun getSphereType(ergType: IErgType): SphereType {
    fun stack(itemStack: () -> ItemStack): () -> Ingredient = { Ingredient.fromStacks(itemStack()) }
    fun item(item: () -> Item): () -> Ingredient = stack { ItemStack(item()) }
    fun block(block: () -> Block): () -> Ingredient = stack { ItemStack(block()) }
    fun ore(oreName: String): () -> Ingredient = { OreIngredient(oreName) }
    return when (ergType) {
        ErgTypes.attack -> SphereType(ErgTypes.attack, 0xFFA0A0, 0xFF6B6B, 0xC70000, 0xFF0000, item { Items.IRON_SWORD }, null)
        ErgTypes.craft -> SphereType(ErgTypes.craft, 0xF1B772, 0xD3FDCC, 0x92B56A, 0xFFFFFF, ore("workbench"), ore("gemNephrite"))
        ErgTypes.harvest -> SphereType(ErgTypes.harvest, 0x00BD00, 0xD09D74, 0x6E4219, 0x2FFF2F, item { Items.IRON_AXE }, null)
        ErgTypes.light -> SphereType(ErgTypes.light, 0xFF8300, 0xFFC9BC, 0xF1C483, 0xFFFF25, ore("torch"), ore("gemTopaz"))
        ErgTypes.flame -> SphereType(ErgTypes.flame, 0xFF9F68, 0xFF6800, 0xE2713F, 0xBF1805, item { Items.FIRE_CHARGE }, ore("gemHeliolite"))
        ErgTypes.water -> SphereType(ErgTypes.water, 0x67E6FF, 0xBDF0FF, 0x00ABFF, 0x83B5FF, item { Items.WATER_BUCKET }, null)
        ErgTypes.crystal -> SphereType(ErgTypes.crystal, 0xA2FFFF, 0xB6FFFF, 0x36CECE, 0xEBFFFF, ore("gemQuartz"), ore("gemDiamond"))
        ErgTypes.sound -> SphereType(ErgTypes.sound, 0x98ACE7, 0xD8DDFF, 0xBFC9D8, 0xC9D0ED, block { Blocks.NOTEBLOCK }, null)
        ErgTypes.space -> SphereType(ErgTypes.space, 0x000000, 0x4D0065, 0x67009D, 0x001E74, block { Blocks.CHEST }, null)
        ErgTypes.warp -> SphereType(ErgTypes.warp, 0x3A00D3, 0x8CF4E2, 0x349988, 0xD004FB, ore("enderpearl"), null)
        ErgTypes.shoot -> SphereType(ErgTypes.shoot, 0x969696, 0x896727, 0x896727, 0xD8D8D8, item { Items.BOW }, null)
        ErgTypes.destroy -> SphereType(ErgTypes.destroy, 0xFFFFFF, 0xFF5A35, 0xFF4800, 0x000000, block { Blocks.TNT }, null)
        ErgTypes.chemical -> SphereType(ErgTypes.chemical, 0x0067FF, 0xC9DFEF, 0xB0C4D7, 0x0755FF, item { Items.FERMENTED_SPIDER_EYE }, null)
        ErgTypes.slash -> SphereType(ErgTypes.slash, 0xAAAAAA, 0xFFC9B2, 0xD20000, 0xFFFFFF, item { Items.SHEARS }, null)
        ErgTypes.life -> SphereType(ErgTypes.life, 0xFF0033, 0xFFC9DE, 0xFF8EB2, 0xFF3F67, item { Items.BEEF }, ore("gemPyrope"))
        ErgTypes.knowledge -> SphereType(ErgTypes.knowledge, 0xFFFF00, 0x006200, 0x00A000, 0x50DD00, item { Items.BOOK }, null)
        ErgTypes.energy -> SphereType(ErgTypes.energy, 0xFFED30, 0xFFF472, 0xFFE84C, 0xBFE7FF, ore("gemCoal"), null)
        ErgTypes.submission -> SphereType(ErgTypes.submission, 0xFF0000, 0x593232, 0x1E1E1E, 0xA90000, block { Blocks.IRON_BARS }, null)
        ErgTypes.christmas -> SphereType(ErgTypes.christmas, 0xFF0000, 0xFFD723, 0x00B900, 0xFF0000, stack { ItemStack(Blocks.SAPLING, 1, 1) }, null)
        ErgTypes.freeze -> SphereType(ErgTypes.freeze, 0x5AFFFF, 0xFFFFFF, 0xF6FFFF, 0xACFFFF, ore("ice"), null)
        ErgTypes.thunder -> SphereType(ErgTypes.thunder, 0xFFFFB2, 0x359C00, 0xC370A7, 0xFFFF00, item { Items.GOLDEN_SWORD }, ore("gemTourmaline"))
        ErgTypes.levitate -> SphereType(ErgTypes.levitate, 0x00A2FF, 0xB7ECFF, 0x35366B, 0x8CD0FF, item { Items.FEATHER }, ore("gemLabradorite"))
        ErgTypes.sense -> SphereType(ErgTypes.sense, 0x1B3211, 0xD3E6DF, 0x7ACF45, 0x4784A0, item { Items.ENDER_EYE }, null)
        else -> throw IllegalArgumentException()
    }
}


lateinit var itemSpheres: Supplier<ItemSpheres>

val moduleSphere: Module = {

    // スフィアアイテム
    itemSpheres = item({ ItemSpheres() }, "spheres") {
        setUnlocalizedName("spheres")
        setCreativeTab { ApiMain.creativeTab() }
        onRegisterItem {
            ErgTypes.values.forEachIndexed { i, ergType ->
                item.registerVariant(i, VariantSphere(getSphereType(ergType)))
                item.setCustomModelResourceLocation("sphere", i)
            }
        }
    }

    // スフィアのカスタム色
    onRegisterItemColorHandler {
        object {
            @SideOnly(Side.CLIENT)
            fun run() {
                @SideOnly(Side.CLIENT)
                class ItemColorImpl : IItemColor {
                    override fun colorMultiplier(itemStack: ItemStack, tintIndex: Int): Int {
                        val variant = itemSpheres.get().getVariant(itemStack).orElse(null) ?: return 0xFFFFFF
                        return when (tintIndex) {
                            0 -> variant.sphere.colorBackground
                            1 -> variant.sphere.colorPlasma
                            2 -> variant.sphere.colorCore
                            3 -> variant.sphere.colorHighlight
                            else -> 0xFFFFFF
                        }
                    }
                }
                Minecraft.getMinecraft().itemColors.registerItemColorHandler(ItemColorImpl(), itemSpheres.get())
            }
        }.run()
    }

    // スフィアの鉱石辞書
    onCreateItemStack {
        ErgTypes.values.forEachIndexed { meta, ergType ->
            val itemStack = itemSpheres.get().getVariant(meta).orElse(null)!!.createItemStack()
            OreDictionary.registerOre(getSphereType(ergType).oreName, itemStack)
            OreDictionary.registerOre("mirageFairy2019SphereAny", itemStack)
        }
    }

    // スフィアのレシピ登録
    onAddRecipe {
        ErgTypes.values.forEachIndexed { meta, ergType ->
            val sphere = getSphereType(ergType)
            val variant = itemSpheres.get().getVariant(meta).orElse(null)!!

            // 蛍石触媒レシピ
            run {
                val ingredient = sphere.catalystSupplier()

                // クラフトレシピ
                GameRegistry.addShapelessRecipe(
                    ResourceLocation("${ModMirageFairy2019.MODID}:${ergType.name}_sphere_with_fluorite"),
                    ResourceLocation("${ModMirageFairy2019.MODID}:${ergType.name}_sphere"),
                    variant.createItemStack(),
                    OreIngredientComplex("container1000MiragiumWater"),
                    OreIngredient("gemFluorite"),
                    OreIngredientComplex("mirageFairy2019CraftingToolFairyWandCrafting"),
                    OreIngredient("mirageFairy2019FairyAbility${UtilsString.toUpperCaseHead(ergType.name)}"),
                    ingredient
                )

                // 妖精のステッキレシピ
                ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().apply {
                    conditions.add(FairyStickCraftConditionUseItem(OreIngredient("mirageFairy2019FairyStick")))
                    conditions.add(FairyStickCraftConditionConsumeBlock { ApiOre.blockFluidMiragiumWater.defaultState })
                    conditions.add(FairyStickCraftConditionConsumeItem(OreIngredient("gemFluorite")))
                    conditions.add(FairyStickCraftConditionConsumeItem(OreIngredient("mirageFairy2019FairyAbility${UtilsString.toUpperCaseHead(ergType.name)}")))
                    conditions.add(FairyStickCraftConditionConsumeItem(ingredient))
                    conditions.add(FairyStickCraftConditionSpawnItem { variant.createItemStack() })
                })

            }

            // 宝石レシピ
            run a@{
                val ingredient = (sphere.gemSupplier ?: return@a)()

                // クラフトレシピ
                GameRegistry.addShapelessRecipe(
                    ResourceLocation("${ModMirageFairy2019.MODID}:${ergType.name}_sphere_from_gem"),
                    ResourceLocation("${ModMirageFairy2019.MODID}:${ergType.name}_sphere"),
                    variant.createItemStack(),
                    OreIngredientComplex("container1000MiragiumWater"),
                    OreIngredientComplex("mirageFairy2019CraftingToolFairyWandCrafting"),
                    OreIngredient("mirageFairy2019FairyAbility${UtilsString.toUpperCaseHead(ergType.name)}"),
                    ingredient
                )

                // 妖精のステッキレシピ
                ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().apply {
                    conditions.add(FairyStickCraftConditionUseItem(OreIngredient("mirageFairy2019FairyStick")))
                    conditions.add(FairyStickCraftConditionConsumeBlock { ApiOre.blockFluidMiragiumWater.defaultState })
                    conditions.add(FairyStickCraftConditionConsumeItem(OreIngredient("mirageFairy2019FairyAbility${UtilsString.toUpperCaseHead(ergType.name)}")))
                    conditions.add(FairyStickCraftConditionConsumeItem(ingredient))
                    conditions.add(FairyStickCraftConditionSpawnItem { variant.createItemStack() })
                })

            }

        }

    }

}


// TODO delete
@Deprecated(message = "削除予定のAPI")
object EnumSphere {
    @JvmStatic
    @Deprecated(message = "削除予定のAPI", replaceWith = ReplaceWith("Optional.of(getSphereType(ergType))"))
    fun of(ergType: IErgType) = Optional.of(getSphereType(ergType))
}
