package miragefairy2019.mod

import miragefairy2019.libkt.ModInitializer
import miragefairy2019.mod.api.fairy.ApiFairy
import miragefairy2019.mod.api.fertilizer.ApiFertilizer
import miragefairy2019.mod.api.main.ApiMain
import miragefairy2019.mod.api.materialsfairy.ApiMaterialsFairy
import miragefairy2019.mod.lib.EventRegistryMod
import miragefairy2019.mod.lib.InitializationContext
import miragefairy2019.modkt.modules.fairy.loaderFairyRelation
import miragefairy2019.mod.modules.fairycrystal.ModuleFairyCrystal
import miragefairy2019.mod.modules.fairystick.ModuleFairyStick
import miragefairy2019.mod.modules.fairyweapon.item.Loader
import miragefairy2019.mod.modules.mirageflower.ModuleMirageFlower
import miragefairy2019.mod.modules.ore.ModuleOre
import miragefairy2019.mod.modules.oreseed.ModuleOreSeed
import miragefairy2019.mod.modules.sphere.ModuleSphere
import miragefairy2019.modkt.impl.fairy.moduleErg
import miragefairy2019.modkt.impl.mana.moduleMana
import miragefairy2019.modkt.modules.artifacts.ModuleArtifacts
import miragefairy2019.modkt.modules.placeditem.ModulePlacedItem
import miragefairy2019.modkt.modules.playeraura.ModulePlayerAura
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

class InitializerMirageFairy2019 {

    var modInitializer = ModInitializer()
    var erMod = EventRegistryMod()

    init {

        modInitializer.run {
            moduleMana()
            moduleErg()
            loaderFairyRelation()
        }

        modInitializer.onInstantiation()


        ModuleFairyStick.init(erMod)
        ApiMain.init(erMod)
        ApiFairy.init(erMod)
        ModuleFairyCrystal.init(erMod)
        Loader.init(erMod)
        miragefairy2019.mod.modules.fairyweapon.damagesource.Loader.init(erMod)
        miragefairy2019.mod.modules.fairyweapon.recipe.Loader.init(erMod)
        ApiFertilizer.init(erMod)
        ApiMaterialsFairy.init(erMod)
        ModuleMirageFlower.init(erMod)
        ModuleOreSeed.init(erMod)
        ModuleOre.init(erMod)
        ModuleSphere.init(erMod)
        ModulePlacedItem.init(erMod)
        ModulePlayerAura.init(erMod)
        ModuleArtifacts.init(erMod)

        erMod.initRegistry.trigger().run()
        erMod.initCreativeTab.trigger().run()
    }

    fun preInit(event: FMLPreInitializationEvent) {
        modInitializer.onPreInit(event)
        erMod.preInit.trigger().accept(event)
        val initializationContext = InitializationContext(ModMirageFairy2019.MODID, event.side, ApiMain.creativeTab())
        erMod.registerBlock.trigger().accept(initializationContext)
        erMod.registerItem.trigger().accept(initializationContext)
        modInitializer.onCreateItemStack()
        erMod.createItemStack.trigger().accept(initializationContext)
        erMod.hookDecorator.trigger().run()
        erMod.initKeyBinding.trigger().run()
    }

    fun init(event: FMLInitializationEvent) {
        modInitializer.onInit(event)
        erMod.init.trigger().accept(event)
        erMod.addRecipe.trigger().run()
        if (event.side.isClient) erMod.registerItemColorHandler.trigger().run()
        erMod.registerTileEntity.trigger().run()
        erMod.initNetworkChannel.trigger().run()
        erMod.registerNetworkMessage.trigger().run()
    }

    fun postInit(event: FMLPostInitializationEvent) {
        modInitializer.onPostInit(event)
        erMod.postInit.trigger().accept(event)
    }
}
