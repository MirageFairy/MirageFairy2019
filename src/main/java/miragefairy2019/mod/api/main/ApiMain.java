package miragefairy2019.mod.api.main;

import miragefairy2019.mod3.main.ModuleMainKt;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

public class ApiMain {

    public static SimpleNetworkWrapper simpleNetworkWrapper;

    public static Logger logger() {
        return ModuleMainKt.logger;
    }

    public static Side side() {
        return ModuleMainKt.side;
    }

    public static CreativeTabs creativeTab() {
        return ModuleMainKt.creativeTab;
    }

}
