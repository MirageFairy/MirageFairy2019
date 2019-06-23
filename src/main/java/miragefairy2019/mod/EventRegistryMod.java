package miragefairy2019.mod;

import mirrg.boron.util.event.lib.EventProviderConsumer;
import mirrg.boron.util.event.lib.EventProviderRunnable;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class EventRegistryMod
{

	public final EventProviderRunnable initCreativeTab = new EventProviderRunnable();

	//

	public final EventProviderConsumer<FMLPreInitializationEvent> preInit = new EventProviderConsumer<>();

	public final EventProviderConsumer<InitializationContext> registerItem = new EventProviderConsumer<>();

	public final EventProviderConsumer<InitializationContext> registerBlock = new EventProviderConsumer<>();

	public final EventProviderConsumer<InitializationContext> createItemStack = new EventProviderConsumer<>();

	public final EventProviderRunnable hookDecorator = new EventProviderRunnable();

	//

	public final EventProviderConsumer<FMLInitializationEvent> init = new EventProviderConsumer<>();

	public final EventProviderRunnable addRecipe = new EventProviderRunnable();

	public final EventProviderRunnable registerItemColorHandler = new EventProviderRunnable();

	public final EventProviderRunnable registerTileEntity = new EventProviderRunnable();

	//

	public final EventProviderConsumer<FMLPostInitializationEvent> postInit = new EventProviderConsumer<>();

}
