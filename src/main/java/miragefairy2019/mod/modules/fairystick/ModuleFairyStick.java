package miragefairy2019.mod.modules.fairystick;

import static miragefairy2019.mod.lib.Configurator.*;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.fairystick.ApiFairyStick;
import miragefairy2019.mod.api.fairystick.IFairyStickCraft;
import miragefairy2019.mod.api.fairystick.contents.FairyStickCraftConditionConsumeItem;
import miragefairy2019.mod.api.fairystick.contents.FairyStickCraftConditionSpawnBlock;
import miragefairy2019.mod.api.fairystick.contents.FairyStickCraftRecipe;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.lib.EventRegistryMod;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

public class ModuleFairyStick
{

	public static void init(EventRegistryMod erMod)
	{

		// 妖精のステッキ
		item(erMod, ItemFairyStick::new, new ResourceLocation(ModMirageFairy2019.MODID, "fairy_stick"), "fairyStick")
			.bind(setCreativeTab(() -> ApiMain.creativeTab()))
			.bind(onRegisterItem(i -> {
				if (ApiMain.side().isClient()) ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), null));
			}))
			.bind(onCreateItemStack(i -> OreDictionary.registerOre("mirageFairy2019FairyStick", new ItemStack(i))));

		// レシピ登録
		{

			// 水精→水源
			ApiFairyStick.fairyStickCraftRegistry.registerRecipe(new FairyStickCraftRecipe(
				new FairyStickCraftConditionSpawnBlock(() -> Blocks.WATER.getDefaultState()) {
					@Override
					public boolean test(IFairyStickCraft fairyStickCraft)
					{
						if (BiomeDictionary.hasType(fairyStickCraft.getWorld().getBiome(fairyStickCraft.getPos()), BiomeDictionary.Type.NETHER)) return false;
						return super.test(fairyStickCraft);
					}
				},
				new FairyStickCraftConditionConsumeItem(new OreIngredient("mirageFairyCrystal")),
				new FairyStickCraftConditionConsumeItem(new OreIngredient("mirageFairy2019FairyWaterRank1"))));

			// 溶岩精→溶岩流
			ApiFairyStick.fairyStickCraftRegistry.registerRecipe(new FairyStickCraftRecipe(
				new FairyStickCraftConditionSpawnBlock(() -> Blocks.FLOWING_LAVA.getDefaultState().withProperty(BlockDynamicLiquid.LEVEL, 3)),
				new FairyStickCraftConditionConsumeItem(new OreIngredient("mirageFairyCrystal")),
				new FairyStickCraftConditionConsumeItem(new OreIngredient("mirageFairy2019FairyLavaRank1"))));

		}

	}

}
