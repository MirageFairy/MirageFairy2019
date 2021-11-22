package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.libkt.ModInitializer
import miragefairy2019.libkt.Module
import miragefairy2019.libkt.item
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.mod.api.composite.ApiComposite
import miragefairy2019.mod.api.fairy.ApiFairy
import miragefairy2019.mod.api.main.ApiMain
import miragefairy2019.mod.lib.BakedModelBuiltinWrapper
import miragefairy2019.mod.modules.main.ModuleMain
import miragefairy2019.modkt.api.erg.ErgTypes.attack
import miragefairy2019.modkt.api.erg.ErgTypes.breaking
import miragefairy2019.modkt.api.erg.ErgTypes.craft
import miragefairy2019.modkt.api.erg.ErgTypes.crystal
import miragefairy2019.modkt.api.erg.ErgTypes.flame
import miragefairy2019.modkt.api.erg.ErgTypes.freeze
import miragefairy2019.modkt.api.erg.ErgTypes.slash
import miragefairy2019.modkt.api.erg.ErgTypes.submission
import miragefairy2019.modkt.api.erg.ErgTypes.water
import miragefairy2019.modkt.api.erg.IErgType
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.function.Supplier

private fun <T : ItemFairyWeaponBase> ModInitializer.fw(
    tier: Int,
    creator: () -> T,
    registryName: String,
    unlocalizedNameList: List<String>,
    parent: (() -> Supplier<ItemFairyWeaponBase>)?,
    vararg ergTypeSuppliers: () -> IErgType
) = item(creator, registryName) {
    unlocalizedNameList.forEach { setUnlocalizedName(it) }
    setCreativeTab { ModuleMain.creativeTab }
    onRegisterItem {
        if (ApiMain.side().isClient) {
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
        item.maxDamage = Loader.getDurability(tier) - 1
    }
}


lateinit var fairyWeaponLoader: FairyWeaponLoader

@Suppress("MemberVisibilityCanBePrivate", "unused")
class FairyWeaponLoader(m: ModInitializer) {
    companion object {
        private val fw = "fairyWand"
        private val fw2 = "mirageFairy2019CraftingToolFairyWand"
    }

    val fairyWandCrafting = m.fw(1, ::ItemFairyWandCrafting, "crafting_fairy_wand", listOf("${fw}Crafting", "${fw2}Crafting", "mirageFairy2019FairyStick"), null, { craft })
    val fairyWandCrafting2 = m.fw(2, ::ItemFairyWandCrafting, "crafting_fairy_wand_2", listOf("${fw}Crafting2", "${fw2}Crafting", "mirageFairy2019FairyStick"), null, { craft })
    val fairyWandCrafting3 = m.fw(3, ::ItemFairyWandCrafting, "crafting_fairy_wand_3", listOf("${fw}Crafting3", "${fw2}Crafting", "mirageFairy2019FairyStick"), null, { craft })
    val fairyWandCrafting4 = m.fw(4, ::ItemFairyWandCrafting, "crafting_fairy_wand_4", listOf("${fw}Crafting4", "${fw2}Crafting", "mirageFairy2019FairyStick"), null, { craft })
    val fairyWandHydrating = m.fw(1, ::ItemFairyWeaponCraftingTool, "hydrating_fairy_wand", listOf("${fw}Hydrating", "${fw2}Hydrating", "container1000Water"), null, { water })
    val fairyWandHydrating2 = m.fw(2, ::ItemFairyWeaponCraftingTool, "hydrating_fairy_wand_2", listOf("${fw}Hydrating2", "${fw2}Hydrating", "container1000Water"), null, { water })
    val fairyWandHydrating3 = m.fw(3, ::ItemFairyWeaponCraftingTool, "hydrating_fairy_wand_3", listOf("${fw}Hydrating3", "${fw2}Hydrating", "container1000Water"), null, { water })
    val fairyWandHydrating4 = m.fw(4, ::ItemFairyWeaponCraftingTool, "hydrating_fairy_wand_4", listOf("${fw}Hydrating4", "${fw2}Hydrating", "container1000Water"), null, { water })
    val fairyWandMelting = m.fw(2, ::ItemFairyWandMelting, "melting_fairy_wand", listOf("${fw}Melting", "${fw2}Melting"), null, { flame })
    val fairyWandMelting2 = m.fw(3, ::ItemFairyWandMelting, "melting_fairy_wand_2", listOf("${fw}Melting2", "${fw2}Melting"), null, { flame })
    val fairyWandMelting3 = m.fw(4, ::ItemFairyWandMelting, "melting_fairy_wand_3", listOf("${fw}Melting3", "${fw2}Melting"), null, { flame })
    val fairyWandBreaking = m.fw(2, ::ItemFairyWandBreaking, "breaking_fairy_wand", listOf("${fw}Breaking", "${fw2}Breaking"), null, { breaking })
    val fairyWandBreaking2 = m.fw(3, ::ItemFairyWandBreaking, "breaking_fairy_wand_2", listOf("${fw}Breaking2", "${fw2}Breaking"), null, { breaking })
    val fairyWandBreaking3 = m.fw(4, ::ItemFairyWandBreaking, "breaking_fairy_wand_3", listOf("${fw}Breaking3", "${fw2}Breaking"), null, { breaking })
    val fairyWandFreezing = m.fw(2, ::ItemFairyWeaponCraftingTool, "freezing_fairy_wand", listOf("${fw}Freezing", "${fw2}Freezing"), null, { freeze })
    val fairyWandFreezing2 = m.fw(3, ::ItemFairyWeaponCraftingTool, "freezing_fairy_wand_2", listOf("${fw}Freezing2", "${fw2}Freezing"), null, { freeze })
    val fairyWandFreezing3 = m.fw(4, ::ItemFairyWeaponCraftingTool, "freezing_fairy_wand_3", listOf("${fw}Freezing3", "${fw2}Freezing"), null, { freeze })
    val fairyWandPolishing = m.fw(3, ::ItemFairyWeaponCraftingTool, "polishing_fairy_wand", listOf("${fw}Polishing", "${fw2}Polishing"), null, { crystal })
    val fairyWandPolishing2 = m.fw(4, ::ItemFairyWeaponCraftingTool, "polishing_fairy_wand_2", listOf("${fw}Polishing2", "${fw2}Polishing"), null, { crystal })
    val fairyWandSummoning = m.fw(3, ::ItemFairyWandSummoning, "summoning_fairy_wand", listOf("${fw}Summoning", "${fw2}Summoning"), null, { submission })
    val fairyWandSummoning2 = m.fw(4, ::ItemFairyWandSummoning, "summoning_fairy_wand_2", listOf("${fw}Summoning2", "${fw2}Summoning"), null, { submission })

    val miragiumSword = m.fw(2, ::ItemFairyWeaponBase, "miragium_sword", listOf("miragiumSword"), null, { attack }, { slash })
    val crystalSword = m.fw(3, ::ItemCrystalSword, "crystal_sword", listOf("crystalSword"), { miragiumSword }, { crystal })
    val fairySword = m.fw(3, ::ItemFairySword, "fairy_sword", listOf("fairySword"), { miragiumSword }, { attack })
}


val moduleFairyWeapon: Module = { fairyWeaponLoader = FairyWeaponLoader(this) }
