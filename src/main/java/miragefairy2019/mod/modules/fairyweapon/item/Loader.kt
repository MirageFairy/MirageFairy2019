package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.libkt.ItemInitializer
import miragefairy2019.libkt.Module
import miragefairy2019.libkt.item
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.mod.api.composite.ApiComposite
import miragefairy2019.mod.api.fairy.ApiFairy
import miragefairy2019.mod.api.main.ApiMain
import miragefairy2019.mod.lib.BakedModelBuiltinWrapper
import miragefairy2019.mod.modules.main.ModuleMain
import miragefairy2019.modkt.api.erg.ErgTypes
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

fun <T : Item> ItemInitializer<T>.registerFairyWeaponModel() = modInitializer.onRegisterItem {
    modInitializer.onRegisterItem {
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
}

val moduleFairyWeapon: Module = {

    item({ ItemCrystalSword() }, "crystal_sword") {
        setUnlocalizedName("crystalSword")
        setCreativeTab { ModuleMain.creativeTab }
        registerFairyWeaponModel()
        modInitializer.onInit {
            item.addComponent(Loader.miragiumSword.get().composite)
            item.addComponent(ApiComposite.instance(ApiFairy.getComponentAbilityType(ErgTypes.crystal)))
            item.maxDamage = Loader.getDurability(3) - 1
        }
    }

}
