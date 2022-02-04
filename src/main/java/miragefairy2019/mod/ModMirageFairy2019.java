package miragefairy2019.mod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

@Mod(modid = ModMirageFairy2019.MODID, name = ModMirageFairy2019.NAME, version = ModMirageFairy2019.VERSION, acceptableRemoteVersions = ModMirageFairy2019.ACCEPTABLE_REMOTE_VERSIONS)
public class ModMirageFairy2019 {

    public static final String MODID = "miragefairy2019";
    public static final String NAME = "MirageFairy2019";
    public static final String VERSION = "{version}";
    public static final String ACCEPTABLE_REMOTE_VERSIONS = "{acceptableRemoteVersions}";

    @Mod.Instance
    public static ModMirageFairy2019 instance;

    private final InitializerMirageFairy2019 initializer = new InitializerMirageFairy2019();

    @EventHandler
    public void handle(FMLConstructionEvent event) {
        initializer.construction(event);
    }

    @EventHandler
    public void handle(FMLPreInitializationEvent event) {
        initializer.preInit(event);
    }

    @EventHandler
    public void handle(FMLInitializationEvent event) {
        initializer.init(event);
    }

    @EventHandler
    public void handle(FMLPostInitializationEvent event) {
        initializer.postInit(event);
    }

    @EventHandler
    public void handle(FMLLoadCompleteEvent event) {
        initializer.loadComplete(event);
    }

    @EventHandler
    public void handle(FMLServerStartingEvent event) {
        initializer.serverStarting(event);
    }

    @EventHandler
    public void handle(FMLServerStartedEvent event) {
        initializer.serverStarted(event);
    }

    @EventHandler
    public void handle(FMLServerStoppingEvent event) {
        initializer.serverStopping(event);
    }

    @EventHandler
    public void handle(FMLServerStoppedEvent event) {
        initializer.serverStopped(event);
    }

}
