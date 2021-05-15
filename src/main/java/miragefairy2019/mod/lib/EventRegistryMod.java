package miragefairy2019.mod.lib;

import java.util.function.Consumer;

import mirrg.boron.util.event.EventProviders;
import mirrg.boron.util.event.IEventProvider;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class EventRegistryMod
{

	public final IEventProvider<Runnable> initRegistry = EventProviders.runnable();

	public final IEventProvider<Runnable> initCreativeTab = EventProviders.runnable();

	public final IEventProvider<Runnable> initKeyBinding = EventProviders.runnable();

	//

	public final IEventProvider<Consumer<FMLPreInitializationEvent>> preInit = EventProviders.consumer();

	public final IEventProvider<Consumer<InitializationContext>> registerItem = EventProviders.consumer();

	public final IEventProvider<Consumer<InitializationContext>> registerBlock = EventProviders.consumer();

	public final IEventProvider<Consumer<InitializationContext>> createItemStack = EventProviders.consumer();

	public final IEventProvider<Runnable> hookDecorator = EventProviders.runnable();

	//

	public final IEventProvider<Consumer<FMLInitializationEvent>> init = EventProviders.consumer();

	public final IEventProvider<Runnable> addRecipe = EventProviders.runnable();

	public final IEventProvider<Runnable> registerItemColorHandler = EventProviders.runnable();

	public final IEventProvider<Runnable> registerTileEntity = EventProviders.runnable();

	//

	public final IEventProvider<Consumer<FMLPostInitializationEvent>> postInit = EventProviders.consumer();

}
