package miragefairy2019.mod.api.main;

import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.modules.main.ModuleMain;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

public class ApiMain {

    public static SimpleNetworkWrapper simpleNetworkWrapper;

    public static void init(EventRegistryMod erMod) {
        ModuleMain.init(erMod);
    }

    public static Logger logger() {
        return ModuleMain.logger;
    }

    public static Side side() {
        return ModuleMain.side;
    }

    public static CreativeTabs creativeTab() {
        return ModuleMain.creativeTab;
    }

}
