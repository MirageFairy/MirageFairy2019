package miragefairy2019.mod

import miragefairy2019.libkt.ModInitializer
import miragefairy2019.mod.lib.EventRegistryMod
import miragefairy2019.mod.lib.InitializationContext
import miragefairy2019.mod.modules.fairycrystal.ModuleFairyCrystal
import miragefairy2019.mod.modules.ore.ModuleOre
import miragefairy2019.mod3.main.api.ApiMain.creativeTab
import miragefairy2019.mod3.placeditem.ModulePlacedItem
import miragefairy2019.mod3.playeraura.ModulePlayerAura
import miragefairy2019.mod3.worldgen.ModuleMirageFlower
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

class InitializerMirageFairy2019 {

    var modInitializer = ModInitializer()
    var erMod = EventRegistryMod()

    init {

        modules.forEach { it(modInitializer) }

        modInitializer.onInstantiation()


        ModuleFairyCrystal.init(erMod)
        miragefairy2019.mod.modules.fairyweapon.recipe.Loader.init(erMod)
        ModuleMirageFlower.init(erMod)
        ModuleOre.init(erMod)
        ModulePlacedItem.init(erMod)
        ModulePlayerAura.init(erMod)

        erMod.initRegistry.trigger().run()
        modInitializer.onInitCreativeTab()
        erMod.initCreativeTab.trigger().run()
    }

    fun preInit(event: FMLPreInitializationEvent) {
        modInitializer.onPreInit(event)
        erMod.preInit.trigger().accept(event)
        val initializationContext = InitializationContext(ModMirageFairy2019.MODID, event.side, creativeTab)
        modInitializer.onRegisterBlock()
        erMod.registerBlock.trigger().accept(initializationContext)
        modInitializer.onRegisterItem()
        erMod.registerItem.trigger().accept(initializationContext)
        modInitializer.onCreateItemStack()
        erMod.createItemStack.trigger().accept(initializationContext)
        modInitializer.onHookDecorator()
        erMod.hookDecorator.trigger().run()
        modInitializer.onInitKeyBinding()
        erMod.initKeyBinding.trigger().run()
    }

    fun init(event: FMLInitializationEvent) {
        modInitializer.onInit(event)
        erMod.init.trigger().accept(event)
        modInitializer.onAddRecipe()
        erMod.addRecipe.trigger().run()
        if (event.side.isClient) modInitializer.onRegisterItemColorHandler()
        if (event.side.isClient) erMod.registerItemColorHandler.trigger().run()
        modInitializer.onRegisterTileEntity()
        erMod.registerTileEntity.trigger().run()
        modInitializer.onRegisterTileEntityRenderer()
        modInitializer.onInitNetworkChannel()
        erMod.initNetworkChannel.trigger().run()
        modInitializer.onRegisterNetworkMessage()
        erMod.registerNetworkMessage.trigger().run()
    }

    fun postInit(event: FMLPostInitializationEvent) {
        modInitializer.onPostInit(event)
        erMod.postInit.trigger().accept(event)
    }
}
