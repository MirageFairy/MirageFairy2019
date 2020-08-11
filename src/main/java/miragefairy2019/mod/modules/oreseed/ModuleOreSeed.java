package miragefairy2019.mod.modules.oreseed;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.lib.EventRegistryMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModuleOreSeed
{

	public static BlockOreSeed blockOreSeed;

	public static void init(EventRegistryMod erMod)
	{
		erMod.registerBlock.register(b -> {

			// 鉱石の種
			blockOreSeed = new BlockOreSeed();
			blockOreSeed.setRegistryName(ModMirageFairy2019.MODID, "ore_seed");
			blockOreSeed.setCreativeTab(ApiMain.creativeTab());
			ForgeRegistries.BLOCKS.register(blockOreSeed);

		});
		erMod.hookDecorator.register(() -> {

			// 地形生成
			MinecraftForge.ORE_GEN_BUS.register(new Object() {
				private WorldGenCompoundOreSeed worldGenCompound = new WorldGenCompoundOreSeed();

				@SubscribeEvent
				public void accept(OreGenEvent.Post event)
				{
					worldGenCompound.accept(event);
				}
			});

		});
	}

}
