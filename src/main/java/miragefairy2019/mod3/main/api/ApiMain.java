package miragefairy2019.mod3.main.api;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ApiMain {

    public static Logger logger;

    public static Side side;

    public static CreativeTabs creativeTab;

    public static SimpleNetworkWrapper simpleNetworkWrapper;

    public static Map<Integer, IGuiHandler> guiHandlers = new HashMap<>();

    public static void registerGuiHandler(int id, IGuiHandler guiHandler) {
        if (guiHandlers.containsKey(id)) throw new RuntimeException("Duplicated GuiId: " + id);
        guiHandlers.put(id, guiHandler);
    }

}
