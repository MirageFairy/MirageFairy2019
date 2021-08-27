package miragefairy2019.mod;

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

    private final InitializerMirageFairy2019 initializer = new InitializerMirageFairy2019();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        initializer.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        initializer.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        initializer.postInit(event);
    }

}
