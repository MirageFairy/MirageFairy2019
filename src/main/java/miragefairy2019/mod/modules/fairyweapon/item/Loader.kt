package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.libkt.ModInitializer
import miragefairy2019.libkt.Module
import miragefairy2019.libkt.item
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.mod.api.composite.ApiComposite
import miragefairy2019.mod.api.fairy.ApiFairy
import miragefairy2019.mod.lib.BakedModelBuiltinWrapper
import miragefairy2019.mod3.erg.api.EnumErgType
import miragefairy2019.mod3.erg.api.EnumErgType.ATTACK
import miragefairy2019.mod3.erg.api.EnumErgType.CHRISTMAS
import miragefairy2019.mod3.erg.api.EnumErgType.CRAFT
import miragefairy2019.mod3.erg.api.EnumErgType.CRYSTAL
import miragefairy2019.mod3.erg.api.EnumErgType.DESTROY
import miragefairy2019.mod3.erg.api.EnumErgType.ENERGY
import miragefairy2019.mod3.erg.api.EnumErgType.FLAME
import miragefairy2019.mod3.erg.api.EnumErgType.FREEZE
import miragefairy2019.mod3.erg.api.EnumErgType.HARVEST
import miragefairy2019.mod3.erg.api.EnumErgType.KNOWLEDGE
import miragefairy2019.mod3.erg.api.EnumErgType.LIFE
import miragefairy2019.mod3.erg.api.EnumErgType.LIGHT
import miragefairy2019.mod3.erg.api.EnumErgType.SLASH
import miragefairy2019.mod3.erg.api.EnumErgType.SOUND
import miragefairy2019.mod3.erg.api.EnumErgType.SPACE
import miragefairy2019.mod3.erg.api.EnumErgType.SUBMISSION
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
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.oredict.OreIngredient
import java.util.function.Supplier

private fun getDurability(tier: Int) = when (tier) {
    1 -> 32
    2 -> 64
    3 -> 128
    4 -> 256
    else -> throw IllegalArgumentException("Illegal tier: $tier")
}

private fun <T : ItemFairyWeaponBase> ModInitializer.fw(
    tier: Int,
    creator: () -> T,
    registryName: String,
    unlocalizedName: String,
    oreNameList: List<String>,
    parent: (() -> Supplier<out ItemFairyWeaponBase>)?,
    vararg ergTypeSuppliers: () -> EnumErgType
) = item(creator, registryName) {
    setUnlocalizedName(unlocalizedName)
    setCreativeTab { creativeTab }
    onRegisterItem {
        if (side.isClient) {
            val modelResourceLocation = ModelResourceLocation(item.registryName!!, "normal")
            MinecraftForge.EVENT_BUS.register(object : Any() {
                @SubscribeEvent
                fun accept(event: ModelBakeEvent) {
                    event.modelRegistry.putObject(modelResourceLocation, BakedModelBuiltinWrapper(event.modelRegistry.getObject(modelResourceLocation)))
                }
            })
            ModelLoader.setCustomModelResourceLocation(item, 0, modelResourceLocation)
        }
    }
    onInit {
        if (parent != null) item.addComponent(parent().get().composite)
        ergTypeSuppliers.forEach { item.addComponent(ApiComposite.instance(ApiFairy.getComponentAbilityType(it()))) }
        item.maxDamage = getDurability(tier) - 1
    }
    onCreateItemStack {
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

    val fairyWandCrafting = m.fw(1, ::ItemFairyWandCrafting, "crafting_fairy_wand", "${fw}Crafting", listOf("${fw2}Crafting", "mirageFairy2019FairyStick"), null, { CRAFT })
    val fairyWandCrafting2 = m.fw(2, ::ItemFairyWandCrafting, "crafting_fairy_wand_2", "${fw}Crafting2", listOf("${fw2}Crafting", "mirageFairy2019FairyStick"), null, { CRAFT })
    val fairyWandCrafting3 = m.fw(3, ::ItemFairyWandCrafting, "crafting_fairy_wand_3", "${fw}Crafting3", listOf("${fw2}Crafting", "mirageFairy2019FairyStick"), null, { CRAFT })
    val fairyWandCrafting4 = m.fw(4, ::ItemFairyWandCrafting, "crafting_fairy_wand_4", "${fw}Crafting4", listOf("${fw2}Crafting", "mirageFairy2019FairyStick"), null, { CRAFT })
    val fairyWandHydrating = m.fw(1, ::ItemFairyWeaponCraftingTool, "hydrating_fairy_wand", "${fw}Hydrating", listOf("${fw2}Hydrating", "container1000Water"), null, { WATER })
    val fairyWandHydrating2 = m.fw(2, ::ItemFairyWeaponCraftingTool, "hydrating_fairy_wand_2", "${fw}Hydrating2", listOf("${fw2}Hydrating", "container1000Water"), null, { WATER })
    val fairyWandHydrating3 = m.fw(3, ::ItemFairyWeaponCraftingTool, "hydrating_fairy_wand_3", "${fw}Hydrating3", listOf("${fw2}Hydrating", "container1000Water"), null, { WATER })
    val fairyWandHydrating4 = m.fw(4, ::ItemFairyWeaponCraftingTool, "hydrating_fairy_wand_4", "${fw}Hydrating4", listOf("${fw2}Hydrating", "container1000Water"), null, { WATER })
    val fairyWandMelting = m.fw(2, ::ItemFairyWandMelting, "melting_fairy_wand", "${fw}Melting", listOf("${fw2}Melting"), null, { FLAME })
    val fairyWandMelting2 = m.fw(3, ::ItemFairyWandMelting, "melting_fairy_wand_2", "${fw}Melting2", listOf("${fw2}Melting"), null, { FLAME })
    val fairyWandMelting3 = m.fw(4, ::ItemFairyWandMelting, "melting_fairy_wand_3", "${fw}Melting3", listOf("${fw2}Melting"), null, { FLAME })
    val fairyWandBreaking = m.fw(2, ::ItemFairyWandBreaking, "breaking_fairy_wand", "${fw}Breaking", listOf("${fw2}Breaking"), null, { DESTROY })
    val fairyWandBreaking2 = m.fw(3, ::ItemFairyWandBreaking, "breaking_fairy_wand_2", "${fw}Breaking2", listOf("${fw2}Breaking"), null, { DESTROY })
    val fairyWandBreaking3 = m.fw(4, ::ItemFairyWandBreaking, "breaking_fairy_wand_3", "${fw}Breaking3", listOf("${fw2}Breaking"), null, { DESTROY })
    val fairyWandFreezing = m.fw(2, ::ItemFairyWeaponCraftingTool, "freezing_fairy_wand", "${fw}Freezing", listOf("${fw2}Freezing"), null, { FREEZE })
    val fairyWandFreezing2 = m.fw(3, ::ItemFairyWeaponCraftingTool, "freezing_fairy_wand_2", "${fw}Freezing2", listOf("${fw2}Freezing"), null, { FREEZE })
    val fairyWandFreezing3 = m.fw(4, ::ItemFairyWeaponCraftingTool, "freezing_fairy_wand_3", "${fw}Freezing3", listOf("${fw2}Freezing"), null, { FREEZE })
    val fairyWandPolishing = m.fw(3, ::ItemFairyWeaponCraftingTool, "polishing_fairy_wand", "${fw}Polishing", listOf("${fw2}Polishing"), null, { CRYSTAL })
    val fairyWandPolishing2 = m.fw(4, ::ItemFairyWeaponCraftingTool, "polishing_fairy_wand_2", "${fw}Polishing2", listOf("${fw2}Polishing"), null, { CRYSTAL })
    val fairyWandSummoning = m.fw(3, { ItemFairyWandSummoning(2) }, "summoning_fairy_wand", "${fw}Summoning", listOf("${fw2}Summoning"), null, { SUBMISSION })
    val fairyWandSummoning2 = m.fw(4, { ItemFairyWandSummoning(5) }, "summoning_fairy_wand_2", "${fw}Summoning2", listOf("${fw2}Summoning"), null, { SUBMISSION })
    val fairyWandDistortion = m.fw(4, ::ItemFairyWeaponCraftingTool, "distortion_fairy_wand", "${fw}Distortion", listOf("${fw2}Distortion"), null, { SPACE })
    val fairyWandFusion = m.fw(4, ::ItemFairyWeaponCraftingTool, "fusion_fairy_wand", "${fw}Fusion", listOf("${fw2}Fusion"), null, { WARP })

    val miragiumSword = m.fw(2, ::ItemFairyWeaponBase, "miragium_sword", "miragiumSword", listOf(), null, { ATTACK }, { SLASH })
    val crystalSword = m.fw(3, ::ItemCrystalSword, "crystal_sword", "crystalSword", listOf(), { miragiumSword }, { CRYSTAL })
    val fairySword = m.fw(3, ::ItemFairySword, "fairy_sword", "fairySword", listOf(), { miragiumSword }, { ATTACK })

    val miragiumAxe = m.fw(2, ::ItemMiragiumAxe, "miragium_axe", "miragiumAxe", listOf(), null, { SLASH }, { HARVEST })

    val magicWandBase = m.fw(3, ::ItemFairyWeaponBase, "magic_wand_base", "magicWandBase", listOf(), null, { KNOWLEDGE })
    val magicWandLight = m.fw(3, ::ItemMagicWandLight, "light_magic_wand", "magicWandLight", listOf(), { magicWandBase }, { LIGHT })
    val magicWandCollecting = m.fw(3, ::ItemMagicWandCollecting, "collecting_magic_wand", "magicWandCollecting", listOf(), { magicWandBase }, { WARP })
    val magicWandLightning = m.fw(3, ::ItemMagicWandLightning, "lightning_magic_wand", "magicWandLightning", listOf(), { magicWandBase }, { THUNDER }, { ENERGY })

    val ocarinaBase = m.fw(3, ::ItemFairyWeaponBase, "ocarina_base", "ocarinaBase", listOf(), null, { SOUND })
    val ocarinaTemptation = m.fw(3, ::ItemOcarinaTemptation, "temptation_ocarina", "ocarinaTemptation", listOf(), { ocarinaBase }, { LIFE })
    val bellBase = m.fw(2, ::ItemBellBase, "bell_base", "bellBase", listOf(), null, { SOUND })
    val bellFlowerPicking = m.fw(2, { ItemBellFlowerPicking(0.0, 0.0, 0.0, 0.0, 0.2) }, "flower_picking_bell", "bellFlowerPicking", listOf(), { bellBase }, { HARVEST })
    val bellFlowerPicking2 = m.fw(4, { ItemBellFlowerPicking(10.0, 10.0, 10.0, 10.0, 1.0) }, "flower_picking_bell_2", "bellFlowerPicking2", listOf(), { bellFlowerPicking }, { HARVEST })
    val bellChristmas = m.fw(3, ::ItemBellChristmas, "christmas_bell", "bellChristmas", listOf(), { bellBase }, { CHRISTMAS }, { ATTACK })
    val miragiumScythe = m.fw(2, ::ItemMiragiumScythe, "miragium_scythe", "miragiumScythe", listOf(), null, { SLASH }, { HARVEST })
}


val moduleFairyWeapon: Module = {

    fairyWeaponLoader = FairyWeaponLoader(this)

    // ワンドステッキクラフトレシピ登録
    onAddRecipe {

        // 丸石＞紅蓮→焼き石
        ApiFairyStickCraft.fairyStickCraftRegistry.addRecipe(FairyStickCraftRecipe().also {
            it.conditions += FairyStickCraftConditionUseItem(OreIngredient("mirageFairy2019CraftingToolFairyWandMelting"))
            it.conditions += FairyStickCraftConditionReplaceBlock({ Blocks.COBBLESTONE.defaultState }, { Blocks.STONE.defaultState })
        })

    }

}
