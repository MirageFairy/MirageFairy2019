package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.libkt.ModInitializer
import miragefairy2019.libkt.Module
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.item
import miragefairy2019.libkt.oreIngredient
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.lib.BakedModelBuiltinWrapper
import miragefairy2019.mod3.artifacts.getSphereType
import miragefairy2019.mod3.artifacts.oreName
import miragefairy2019.mod3.erg.api.EnumErgType
import miragefairy2019.mod3.erg.api.EnumErgType.ATTACK
import miragefairy2019.mod3.erg.api.EnumErgType.CHRISTMAS
import miragefairy2019.mod3.erg.api.EnumErgType.CRYSTAL
import miragefairy2019.mod3.erg.api.EnumErgType.DESTROY
import miragefairy2019.mod3.erg.api.EnumErgType.ENERGY
import miragefairy2019.mod3.erg.api.EnumErgType.HARVEST
import miragefairy2019.mod3.erg.api.EnumErgType.KNOWLEDGE
import miragefairy2019.mod3.erg.api.EnumErgType.LIFE
import miragefairy2019.mod3.erg.api.EnumErgType.LIGHT
import miragefairy2019.mod3.erg.api.EnumErgType.SLASH
import miragefairy2019.mod3.erg.api.EnumErgType.SOUND
import miragefairy2019.mod3.erg.api.EnumErgType.THUNDER
import miragefairy2019.mod3.erg.api.EnumErgType.WARP
import miragefairy2019.mod3.erg.api.EnumErgType.WATER
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionReplaceBlock
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftConditionUseItem
import miragefairy2019.mod3.fairystickcraft.FairyStickCraftRecipe
import miragefairy2019.mod3.fairystickcraft.api.ApiFairyStickCraft
import miragefairy2019.mod3.main.api.ApiMain.creativeTab
import miragefairy2019.mod3.main.api.ApiMain.side
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.oredict.OreIngredient

private fun getDurability(tier: Int) = when (tier) {
    1 -> 32
    2 -> 64
    3 -> 128
    4 -> 256
    else -> throw IllegalArgumentException("Illegal tier: $tier")
}

private fun <T : ItemFairyWeapon> ModInitializer.fw(
    tier: Int,
    creator: () -> T,
    registryName: String,
    unlocalizedName: String,
    oreNameList: List<String>,
    parent: (() -> () -> ItemFairyWeapon)?,
    vararg manualRepairIngredientSuppliers: () -> Ingredient
) = item(creator, registryName) {
    setUnlocalizedName(unlocalizedName)
    setCreativeTab { creativeTab }
    onRegisterItem {
        if (side.isClient) {
            val modelResourceLocation = ModelResourceLocation(item.registryName!!, "normal")
            MinecraftForge.EVENT_BUS.register(object : Any() {
                @SubscribeEvent
                fun accept(event: ModelBakeEvent) {
                    event.modelRegistry.putObject(modelResourceLocation, BakedModelBuiltinWrapper(event.modelRegistry.getObject(modelResourceLocation)!!))
                }
            })
            ModelLoader.setCustomModelResourceLocation(item, 0, modelResourceLocation)
        }
    }
    onInit {
        item.maxDamage = getDurability(tier) - 1
        item.tier = tier
    }
    onCreateItemStack {
        if (parent != null) item.manualRepairIngredients += parent()().manualRepairIngredients
        manualRepairIngredientSuppliers.forEach { item.manualRepairIngredients += it() }
        oreNameList.forEach { OreDictionary.registerOre(it, ItemStack(item, 1, OreDictionary.WILDCARD_VALUE)) }
    }
}


lateinit var fairyWeaponLoader: FairyWeaponLoader

@Suppress("MemberVisibilityCanBePrivate", "unused")
class FairyWeaponLoader(m: ModInitializer) {
    companion object {
        private val fw = "fairyWand"
        private val fw2 = "mirageFairy2019CraftingToolFairyWand"
    }

    operator fun EnumErgType.not(): () -> Ingredient = { getSphereType(this).oreName.oreIngredient }
    operator fun String.not(): () -> Ingredient = { oreIngredient }

    val fairyWandCrafting = m.fw(1, ::ItemFairyWand, "crafting_fairy_wand", "${fw}Crafting", listOf("${fw2}Crafting"), null)
    val fairyWandCrafting2 = m.fw(2, ::ItemFairyWand, "crafting_fairy_wand_2", "${fw}Crafting2", listOf("${fw2}Crafting"), null)
    val fairyWandCrafting3 = m.fw(3, ::ItemFairyWand, "crafting_fairy_wand_3", "${fw}Crafting3", listOf("${fw2}Crafting"), null)
    val fairyWandCrafting4 = m.fw(4, ::ItemFairyWand, "crafting_fairy_wand_4", "${fw}Crafting4", listOf("${fw2}Crafting"), null)
    val fairyWandHydrating = m.fw(1, ::ItemFairyWand, "hydrating_fairy_wand", "${fw}Hydrating", listOf("${fw2}Hydrating", "container1000Water"), null)
    val fairyWandHydrating2 = m.fw(2, ::ItemFairyWand, "hydrating_fairy_wand_2", "${fw}Hydrating2", listOf("${fw2}Hydrating", "container1000Water"), null)
    val fairyWandHydrating3 = m.fw(3, ::ItemFairyWand, "hydrating_fairy_wand_3", "${fw}Hydrating3", listOf("${fw2}Hydrating", "container1000Water"), null)
    val fairyWandHydrating4 = m.fw(4, ::ItemFairyWand, "hydrating_fairy_wand_4", "${fw}Hydrating4", listOf("${fw2}Hydrating", "container1000Water"), null)
    val fairyWandMelting = m.fw(2, ::ItemFairyWand, "melting_fairy_wand", "${fw}Melting", listOf("${fw2}Melting"), null)
    val fairyWandMelting2 = m.fw(3, ::ItemFairyWand, "melting_fairy_wand_2", "${fw}Melting2", listOf("${fw2}Melting"), null)
    val fairyWandMelting3 = m.fw(4, ::ItemFairyWand, "melting_fairy_wand_3", "${fw}Melting3", listOf("${fw2}Melting"), null)
    val fairyWandBreaking = m.fw(2, ::ItemFairyWand, "breaking_fairy_wand", "${fw}Breaking", listOf("${fw2}Breaking"), null)
    val fairyWandBreaking2 = m.fw(3, ::ItemFairyWand, "breaking_fairy_wand_2", "${fw}Breaking2", listOf("${fw2}Breaking"), null)
    val fairyWandBreaking3 = m.fw(4, ::ItemFairyWand, "breaking_fairy_wand_3", "${fw}Breaking3", listOf("${fw2}Breaking"), null)
    val fairyWandFreezing = m.fw(2, ::ItemFairyWand, "freezing_fairy_wand", "${fw}Freezing", listOf("${fw2}Freezing"), null)
    val fairyWandFreezing2 = m.fw(3, ::ItemFairyWand, "freezing_fairy_wand_2", "${fw}Freezing2", listOf("${fw2}Freezing"), null)
    val fairyWandFreezing3 = m.fw(4, ::ItemFairyWand, "freezing_fairy_wand_3", "${fw}Freezing3", listOf("${fw2}Freezing"), null)
    val fairyWandPolishing = m.fw(3, ::ItemFairyWand, "polishing_fairy_wand", "${fw}Polishing", listOf("${fw2}Polishing"), null)
    val fairyWandPolishing2 = m.fw(4, ::ItemFairyWand, "polishing_fairy_wand_2", "${fw}Polishing2", listOf("${fw2}Polishing"), null)
    val fairyWandSummoning = m.fw(3, { ItemFairyWandSummoning(2) }, "summoning_fairy_wand", "${fw}Summoning", listOf("${fw2}Summoning"), null)
    val fairyWandSummoning2 = m.fw(4, { ItemFairyWandSummoning(5) }, "summoning_fairy_wand_2", "${fw}Summoning2", listOf("${fw2}Summoning"), null)
    val fairyWandDistortion = m.fw(4, ::ItemFairyWand, "distortion_fairy_wand", "${fw}Distortion", listOf("${fw2}Distortion"), null)
    val fairyWandFusion = m.fw(4, ::ItemFairyWand, "fusion_fairy_wand", "${fw}Fusion", listOf("${fw2}Fusion"), null)

    val miragiumSword = m.fw(2, ::ItemFairyWeapon, "miragium_sword", "miragiumSword", listOf(), null, !ATTACK, !SLASH)
    val crystalSword = m.fw(3, ::ItemCrystalSword, "crystal_sword", "crystalSword", listOf(), { miragiumSword }, !CRYSTAL)
    val fairySword = m.fw(3, ::ItemFairySword, "fairy_sword", "fairySword", listOf(), { miragiumSword }, !ATTACK)

    val miragiumAxe = m.fw(2, ::ItemMiragiumAxe, "miragium_axe", "miragiumAxe", listOf(), null, !SLASH, !HARVEST, !"plateMiragium")

    val magicWandBase = m.fw(3, ::ItemFairyWeapon, "magic_wand_base", "magicWandBase", listOf(), null, !KNOWLEDGE)
    val magicWandLight = m.fw(3, ::ItemMagicWandLight, "light_magic_wand", "magicWandLight", listOf(), { magicWandBase }, !LIGHT)
    val magicWandCollecting = m.fw(3, ::ItemMagicWandCollecting, "collecting_magic_wand", "magicWandCollecting", listOf(), { magicWandBase }, !WARP)
    val magicWandLightning = m.fw(3, ::ItemMagicWandLightning, "lightning_magic_wand", "magicWandLightning", listOf(), { magicWandBase }, !THUNDER, !ENERGY)

    val ocarinaBase = m.fw(3, ::ItemFairyWeapon, "ocarina_base", "ocarinaBase", listOf(), null, !SOUND)
    val ocarinaTemptation = m.fw(3, ::ItemOcarinaTemptation, "temptation_ocarina", "ocarinaTemptation", listOf(), { ocarinaBase }, !LIFE)
    val bellBase = m.fw(2, ::ItemBellBase, "bell_base", "bellBase", listOf(), null, !SOUND)
    val bellFlowerPicking = m.fw(2, { ItemBellFlowerPicking(0.0, 0.001, 0.2) }, "flower_picking_bell", "bellFlowerPicking", listOf(), { bellBase }, !HARVEST)
    val bellFlowerPicking2 = m.fw(4, { ItemBellFlowerPicking(10.0, 0.01, 10000.0) }, "flower_picking_bell_2", "bellFlowerPicking2", listOf(), { bellFlowerPicking }, !HARVEST)
    val bellChristmas = m.fw(3, ::ItemBellChristmas, "christmas_bell", "bellChristmas", listOf(), { bellBase }, !CHRISTMAS, !ATTACK)
    val miragiumScythe = m.fw(2, { ItemMiragiumScythe(0.0, 2.0f) }, "miragium_scythe", "miragiumScythe", listOf(), null, !SLASH, !HARVEST)
    val lilagiumScythe = m.fw(3, { ItemMiragiumScythe(10.0, 4.0f) }, "lilagium_scythe", "lilagiumScythe", listOf(), { miragiumScythe }, !HARVEST)
    val ryugyoDrill = m.fw(4, { ItemRyugyoDrill(0.0) }, "ryugyo_drill", "ryugyoDrill", listOf(), null, !DESTROY, !THUNDER, !WATER)
}

object FairyWeapon {
    val module: Module = {

        fairyWeaponLoader = FairyWeaponLoader(this)

        // ワンドステッキクラフトレシピ登録
        onAddRecipe {

            // 丸石＞紅蓮→焼き石
            ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
                it.conditions += FairyStickCraftConditionUseItem(OreIngredient("mirageFairy2019CraftingToolFairyWandMelting"))
                it.conditions += FairyStickCraftConditionReplaceBlock({ Blocks.COBBLESTONE.defaultState }, { Blocks.STONE.defaultState })
            })

        }

        onMakeLang {
            enJa("miragefairy2019.magic.${MagicMessage.NO_FAIRY.unlocalizedName}.text", "You don't have a fairy", "妖精を所持していません")
            enJa("miragefairy2019.magic.${MagicMessage.INSUFFICIENT_DURABILITY.unlocalizedName}.text", "Insufficient durability", "耐久値が不足しています")
            enJa("miragefairy2019.magic.${MagicMessage.NO_TARGET.unlocalizedName}.text", "There is no target", "発動対象がありません")
            enJa("miragefairy2019.magic.${MagicMessage.COOL_TIME.unlocalizedName}.text", "Cool time remains", "クールタイムが残っています")
        }

    }
}

enum class MagicMessage(val unlocalizedName: String) {
    NO_FAIRY("noFairy"),
    INSUFFICIENT_DURABILITY("insufficientDurability"),
    NO_TARGET("noTarget"),
    COOL_TIME("coolTime"),
    ;

    val displayText get() = textComponent { translate("miragefairy2019.magic.$unlocalizedName.text") }
}
