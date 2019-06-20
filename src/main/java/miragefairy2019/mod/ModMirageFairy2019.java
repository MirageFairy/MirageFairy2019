package miragefairy2019.mod;

import net.minecraftforge.fml.common.Mod;

@Mod(modid = ModMirageFairy2019.MODID, name = ModMirageFairy2019.NAME, version = ModMirageFairy2019.VERSION)
public class ModMirageFairy2019
{

	public static final String MODID = "miragefairy2019";
	public static final String NAME = "MirageFairy2019";
	public static final String VERSION = "0.0.1";
	/*
		public EventRegistryMod erMod = new EventRegistryMod();

		public ModMirageFairy2018()
		{
			ModuleMain.init(erMod);
			ModuleMaterial.init(erMod);
			ModuleOre.init(erMod);
			ModuleTool.init(erMod);
			ModuleDreamyFlower.init(erMod);
			ModuleFairy.init(erMod);
			ModuleCity.init(erMod);
		}

		@EventHandler
		public void preInit(FMLPreInitializationEvent event)
		{

			erMod.preInit.accept(event);

			Builder builder = new Builder(MODID, event.getSide(), ModuleMain.creativeTab);

			erMod.registerBlock.accept(builder);

			erMod.registerItem.accept(builder);

			erMod.createItemStack.accept(builder);

			erMod.decorate.run();

		}

		@EventHandler
		public void init(FMLInitializationEvent event)
		{

			erMod.init.accept(event);

			erMod.recipe.run();

			if (event.getSide().isClient()) erMod.registerItemColorHandler.run();

			erMod.registerTileEntity.run();

		}

		@EventHandler
		public void postInit(FMLPostInitializationEvent event)
		{

			erMod.postInit.accept(event);

		}
	*/
}
