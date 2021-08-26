package miragefairy2019.mod;

import kotlin.Unit;
import miragefairy2019.libkt.ModInitializer;
import miragefairy2019.mod.api.fairy.ApiFairy;
import miragefairy2019.mod.api.fertilizer.ApiFertilizer;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.api.materialsfairy.ApiMaterialsFairy;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.InitializationContext;
import miragefairy2019.mod.modules.fairycrystal.ModuleFairyCrystal;
import miragefairy2019.mod.modules.fairystick.ModuleFairyStick;
import miragefairy2019.mod.modules.mirageflower.ModuleMirageFlower;
import miragefairy2019.mod.modules.ore.ModuleOre;
import miragefairy2019.mod.modules.oreseed.ModuleOreSeed;
import miragefairy2019.mod.modules.sphere.ModuleSphere;
import miragefairy2019.modkt.modules.artifacts.ModuleArtifacts;
import miragefairy2019.modkt.modules.placeditem.ModulePlacedItem;
import miragefairy2019.modkt.modules.playeraura.ModulePlayerAura;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModMirageFairy2019.MODID, name = ModMirageFairy2019.NAME, version = ModMirageFairy2019.VERSION, acceptableRemoteVersions = ModMirageFairy2019.ACCEPTABLE_REMOTE_VERSIONS)
public class ModMirageFairy2019 {

    public static final String MODID = "miragefairy2019";
    public static final String NAME = "MirageFairy2019";
    public static final String VERSION = "{version}";
    public static final String ACCEPTABLE_REMOTE_VERSIONS = "{acceptableRemoteVersions}";

    public EventRegistryMod erMod = new EventRegistryMod();
    public ModInitializer modInitializer = new ModInitializer();

    public ModMirageFairy2019() {
        ModuleFairyStick.init(erMod);

        ApiMain.init(erMod);
        ApiFairy.init(erMod);
        ModuleFairyCrystal.init(erMod);
        miragefairy2019.mod.modules.fairyweapon.item.Loader.init(erMod);
        miragefairy2019.mod.modules.fairyweapon.damagesource.Loader.init(erMod);
        miragefairy2019.mod.modules.fairyweapon.recipe.Loader.init(erMod);
        ApiFertilizer.init(erMod);
        ApiMaterialsFairy.init(erMod);
        ModuleMirageFlower.init(erMod);
        ModuleOreSeed.init(erMod);
        ModuleOre.init(erMod);
        ModuleSphere.init(erMod);
        ModulePlacedItem.init(erMod);
        ModulePlayerAura.init(erMod);
        ModuleArtifacts.init(erMod);

        modInitializer.getOnInstantiation().fire(Unit.INSTANCE);

        erMod.initRegistry.trigger().run();

        erMod.initCreativeTab.trigger().run();

    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        modInitializer.getOnPreInit().fire(event);

        erMod.preInit.trigger().accept(event);

        InitializationContext initializationContext = new InitializationContext(MODID, event.getSide(), ApiMain.creativeTab());

        erMod.registerBlock.trigger().accept(initializationContext);

        erMod.registerItem.trigger().accept(initializationContext);

        erMod.createItemStack.trigger().accept(initializationContext);

        erMod.hookDecorator.trigger().run();

        erMod.initKeyBinding.trigger().run();

    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

        modInitializer.getOnInit().fire(event);

        erMod.init.trigger().accept(event);

        erMod.addRecipe.trigger().run();

        if (event.getSide().isClient()) erMod.registerItemColorHandler.trigger().run();

        erMod.registerTileEntity.trigger().run();

        erMod.initNetworkChannel.trigger().run();

        erMod.registerNetworkMessage.trigger().run();

    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        modInitializer.getOnPostInit().fire(event);

        erMod.postInit.trigger().accept(event);

    }

}
