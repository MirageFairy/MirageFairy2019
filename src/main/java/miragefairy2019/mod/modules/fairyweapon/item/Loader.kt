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
import miragefairy2019.modkt.api.erg.ErgTypes.store
import miragefairy2019.modkt.api.erg.ErgTypes.submission
import miragefairy2019.modkt.api.erg.ErgTypes.water
import miragefairy2019.modkt.api.erg.IErgType
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.ItemStack
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.oredict.OreDictionary
import java.util.function.Supplier

private fun <T : ItemFairyWeaponBase> ModInitializer.fw(
    tier: Int,
    creator: () -> T,
    registryName: String,
    unlocalizedName: String,
    oreNameList: List<String>,
    parent: (() -> Supplier<ItemFairyWeaponBase>)?,
    vararg ergTypeSuppliers: () -> IErgType
) = item(creator, registryName) {
    setUnlocalizedName(unlocalizedName)
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

    val fairyWandCrafting = m.fw(1, ::ItemFairyWandCrafting, "crafting_fairy_wand", "${fw}Crafting", listOf("${fw2}Crafting", "mirageFairy2019FairyStick"), null, { craft })
    val fairyWandCrafting2 = m.fw(2, ::ItemFairyWandCrafting, "crafting_fairy_wand_2", "${fw}Crafting2", listOf("${fw2}Crafting", "mirageFairy2019FairyStick"), null, { craft })
    val fairyWandCrafting3 = m.fw(3, ::ItemFairyWandCrafting, "crafting_fairy_wand_3", "${fw}Crafting3", listOf("${fw2}Crafting", "mirageFairy2019FairyStick"), null, { craft })
    val fairyWandCrafting4 = m.fw(4, ::ItemFairyWandCrafting, "crafting_fairy_wand_4", "${fw}Crafting4", listOf("${fw2}Crafting", "mirageFairy2019FairyStick"), null, { craft })
    val fairyWandHydrating = m.fw(1, ::ItemFairyWeaponCraftingTool, "hydrating_fairy_wand", "${fw}Hydrating", listOf("${fw2}Hydrating", "container1000Water"), null, { water })
    val fairyWandHydrating2 = m.fw(2, ::ItemFairyWeaponCraftingTool, "hydrating_fairy_wand_2", "${fw}Hydrating2", listOf("${fw2}Hydrating", "container1000Water"), null, { water })
    val fairyWandHydrating3 = m.fw(3, ::ItemFairyWeaponCraftingTool, "hydrating_fairy_wand_3", "${fw}Hydrating3", listOf("${fw2}Hydrating", "container1000Water"), null, { water })
    val fairyWandHydrating4 = m.fw(4, ::ItemFairyWeaponCraftingTool, "hydrating_fairy_wand_4", "${fw}Hydrating4", listOf("${fw2}Hydrating", "container1000Water"), null, { water })
    val fairyWandMelting = m.fw(2, ::ItemFairyWandMelting, "melting_fairy_wand", "${fw}Melting", listOf("${fw2}Melting"), null, { flame })
    val fairyWandMelting2 = m.fw(3, ::ItemFairyWandMelting, "melting_fairy_wand_2", "${fw}Melting2", listOf("${fw2}Melting"), null, { flame })
    val fairyWandMelting3 = m.fw(4, ::ItemFairyWandMelting, "melting_fairy_wand_3", "${fw}Melting3", listOf("${fw2}Melting"), null, { flame })
    val fairyWandBreaking = m.fw(2, ::ItemFairyWandBreaking, "breaking_fairy_wand", "${fw}Breaking", listOf("${fw2}Breaking"), null, { breaking })
    val fairyWandBreaking2 = m.fw(3, ::ItemFairyWandBreaking, "breaking_fairy_wand_2", "${fw}Breaking2", listOf("${fw2}Breaking"), null, { breaking })
    val fairyWandBreaking3 = m.fw(4, ::ItemFairyWandBreaking, "breaking_fairy_wand_3", "${fw}Breaking3", listOf("${fw2}Breaking"), null, { breaking })
    val fairyWandFreezing = m.fw(2, ::ItemFairyWeaponCraftingTool, "freezing_fairy_wand", "${fw}Freezing", listOf("${fw2}Freezing"), null, { freeze })
    val fairyWandFreezing2 = m.fw(3, ::ItemFairyWeaponCraftingTool, "freezing_fairy_wand_2", "${fw}Freezing2", listOf("${fw2}Freezing"), null, { freeze })
    val fairyWandFreezing3 = m.fw(4, ::ItemFairyWeaponCraftingTool, "freezing_fairy_wand_3", "${fw}Freezing3", listOf("${fw2}Freezing"), null, { freeze })
    val fairyWandPolishing = m.fw(3, ::ItemFairyWeaponCraftingTool, "polishing_fairy_wand", "${fw}Polishing", listOf("${fw2}Polishing"), null, { crystal })
    val fairyWandPolishing2 = m.fw(4, ::ItemFairyWeaponCraftingTool, "polishing_fairy_wand_2", "${fw}Polishing2", listOf("${fw2}Polishing"), null, { crystal })
    val fairyWandSummoning = m.fw(3, ::ItemFairyWandSummoning, "summoning_fairy_wand", "${fw}Summoning", listOf("${fw2}Summoning"), null, { submission })
    val fairyWandSummoning2 = m.fw(4, ::ItemFairyWandSummoning, "summoning_fairy_wand_2", "${fw}Summoning2", listOf("${fw2}Summoning"), null, { submission })
    val fairyWandDistortion = m.fw(3, ::ItemFairyWeaponCraftingTool, "distortion_fairy_wand", "${fw}Distortion", listOf("${fw2}Distortion"), null, { store })

    val miragiumSword = m.fw(2, ::ItemFairyWeaponBase, "miragium_sword", "miragiumSword", listOf(), null, { attack }, { slash })
    val crystalSword = m.fw(3, ::ItemCrystalSword, "crystal_sword", "crystalSword", listOf(), { miragiumSword }, { crystal })
    val fairySword = m.fw(3, ::ItemFairySword, "fairy_sword", "fairySword", listOf(), { miragiumSword }, { attack })
}


val moduleFairyWeapon: Module = { fairyWeaponLoader = FairyWeaponLoader(this) }
