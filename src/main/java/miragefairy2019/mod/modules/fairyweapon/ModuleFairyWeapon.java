package miragefairy2019.mod.modules.fairyweapon;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiFairyWeapon;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.modules.fairyweapon.items.ItemBreakingFairyWand;
import miragefairy2019.mod.modules.fairyweapon.items.ItemCollectingMagicWand;
import miragefairy2019.mod.modules.fairyweapon.items.ItemCraftingFairyWand;
import miragefairy2019.mod.modules.fairyweapon.items.ItemFairySword;
import miragefairy2019.mod.modules.fairyweapon.items.ItemLightMagicWand;
import miragefairy2019.mod.modules.fairyweapon.items.ItemMeltingFairyWand;
import miragefairy2019.mod.modules.fairyweapon.items.ItemMiragiumAxe;
import miragefairy2019.mod.modules.fairyweapon.items.ItemSummoningFairyWand;
import miragefairy2019.mod.modules.fairyweapon.items.ItemTemptationOcarina;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class ModuleFairyWeapon
{

	public static ItemCraftingFairyWand itemCraftingFairyWand;
	public static ItemMeltingFairyWand itemMeltingFairyWand;
	public static ItemFairySword itemFairySword;
	public static ItemMiragiumAxe itemMiragiumAxe;
	public static ItemLightMagicWand itemLightMagicWand;
	public static ItemSummoningFairyWand itemSummoningFairyWand;
	public static ItemCollectingMagicWand itemCollectingMagicWand;
	public static ItemBreakingFairyWand itemBreakingFairyWand;
	public static ItemTemptationOcarina itemTemptationOcarina;

	public static void init(EventRegistryMod erMod)
	{
		erMod.registerItem.register(b -> {

			// 加工のステッキ
			{
				ItemCraftingFairyWand item = new ItemCraftingFairyWand();
				item.setRegistryName(ModMirageFairy2019.MODID, "crafting_fairy_wand");
				item.setUnlocalizedName("craftingFairyWand");
				item.setCreativeTab(ApiMain.creativeTab);
				ForgeRegistries.ITEMS.register(item);
				hookBakedModelWrapper(item);
				if (ApiMain.side.isClient()) ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), null));
				ApiFairyWeapon.itemCraftingFairyWand = itemCraftingFairyWand = item;
			}

			// 融解のステッキ
			{
				ItemMeltingFairyWand item = new ItemMeltingFairyWand();
				item.setRegistryName(ModMirageFairy2019.MODID, "melting_fairy_wand");
				item.setUnlocalizedName("meltingFairyWand");
				item.setCreativeTab(ApiMain.creativeTab);
				ForgeRegistries.ITEMS.register(item);
				hookBakedModelWrapper(item);
				if (ApiMain.side.isClient()) ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), null));
				ApiFairyWeapon.itemMeltingFairyWand = itemMeltingFairyWand = item;
			}

			// 妖精剣
			{
				ItemFairySword item = new ItemFairySword();
				item.setRegistryName(ModMirageFairy2019.MODID, "fairy_sword");
				item.setUnlocalizedName("fairySword");
				item.setCreativeTab(ApiMain.creativeTab);
				ForgeRegistries.ITEMS.register(item);
				hookBakedModelWrapper(item);
				if (ApiMain.side.isClient()) ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), null));
				ApiFairyWeapon.itemFairySword = itemFairySword = item;
			}

			// ミラジウムの斧
			{
				ItemMiragiumAxe item = new ItemMiragiumAxe();
				item.setRegistryName(ModMirageFairy2019.MODID, "miragium_axe");
				item.setUnlocalizedName("miragiumAxe");
				item.setCreativeTab(ApiMain.creativeTab);
				ForgeRegistries.ITEMS.register(item);
				hookBakedModelWrapper(item);
				if (ApiMain.side.isClient()) ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), null));
				ApiFairyWeapon.itemMiragiumAxe = itemMiragiumAxe = item;
			}

			// 光のロッド
			{
				ItemLightMagicWand item = new ItemLightMagicWand();
				item.setRegistryName(ModMirageFairy2019.MODID, "light_magic_wand");
				item.setUnlocalizedName("lightMagicWand");
				item.setCreativeTab(ApiMain.creativeTab);
				ForgeRegistries.ITEMS.register(item);
				hookBakedModelWrapper(item);
				if (ApiMain.side.isClient()) ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), null));
				ApiFairyWeapon.itemLightMagicWand = itemLightMagicWand = item;
			}

			// 召喚のステッキ
			{
				ItemSummoningFairyWand item = new ItemSummoningFairyWand();
				item.setRegistryName(ModMirageFairy2019.MODID, "summoning_fairy_wand");
				item.setUnlocalizedName("summoningFairyWand");
				item.setCreativeTab(ApiMain.creativeTab);
				ForgeRegistries.ITEMS.register(item);
				hookBakedModelWrapper(item);
				if (ApiMain.side.isClient()) ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), null));
				ApiFairyWeapon.itemSummoningFairyWand = itemSummoningFairyWand = item;
			}

			// 収集のロッド
			{
				ItemCollectingMagicWand item = new ItemCollectingMagicWand();
				item.setRegistryName(ModMirageFairy2019.MODID, "collecting_magic_wand");
				item.setUnlocalizedName("collectingMagicWand");
				item.setCreativeTab(ApiMain.creativeTab);
				ForgeRegistries.ITEMS.register(item);
				hookBakedModelWrapper(item);
				if (ApiMain.side.isClient()) ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), null));
				ApiFairyWeapon.itemCollectingMagicWand = itemCollectingMagicWand = item;
			}

			// 破壊のステッキ
			{
				ItemBreakingFairyWand item = new ItemBreakingFairyWand();
				item.setRegistryName(ModMirageFairy2019.MODID, "breaking_fairy_wand");
				item.setUnlocalizedName("breakingFairyWand");
				item.setCreativeTab(ApiMain.creativeTab);
				ForgeRegistries.ITEMS.register(item);
				hookBakedModelWrapper(item);
				if (ApiMain.side.isClient()) ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), null));
				ApiFairyWeapon.itemBreakingFairyWand = itemBreakingFairyWand = item;
			}

			// 魅惑のオカリナ
			{
				ItemTemptationOcarina item = new ItemTemptationOcarina();
				item.setRegistryName(ModMirageFairy2019.MODID, "temptation_ocarina");
				item.setUnlocalizedName("temptationOcarina");
				item.setCreativeTab(ApiMain.creativeTab);
				ForgeRegistries.ITEMS.register(item);
				hookBakedModelWrapper(item);
				if (ApiMain.side.isClient()) ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), null));
				ApiFairyWeapon.itemTemptationOcarina = itemTemptationOcarina = item;
			}

		});
		erMod.createItemStack.register(ic -> {
			OreDictionary.registerOre("mirageFairy2019CraftingToolFairyWandCrafting", new ItemStack(itemCraftingFairyWand, 1, OreDictionary.WILDCARD_VALUE));
			OreDictionary.registerOre("mirageFairy2019CraftingToolFairyWandMelting", new ItemStack(itemMeltingFairyWand, 1, OreDictionary.WILDCARD_VALUE));
			OreDictionary.registerOre("mirageFairy2019CraftingToolFairyWandSummoning", new ItemStack(itemSummoningFairyWand, 1, OreDictionary.WILDCARD_VALUE));
			OreDictionary.registerOre("mirageFairy2019CraftingToolFairyWandBreaking", new ItemStack(itemBreakingFairyWand, 1, OreDictionary.WILDCARD_VALUE));
		});
		erMod.addRecipe.register(() -> {

			// スフィア交換レシピ
			GameRegistry.findRegistry(IRecipe.class).register(new RecipesSphereReplacement());

			// 妖精搭乗レシピ
			GameRegistry.findRegistry(IRecipe.class).register(new RecipesCombining());
			GameRegistry.findRegistry(IRecipe.class).register(new RecipesUncombining());

		});
	}

	private static void hookBakedModelWrapper(Item item)
	{
		if (ApiMain.side.isClient()) {
			MinecraftForge.EVENT_BUS.register(new Object() {
				@SubscribeEvent
				public void accept(ModelBakeEvent event)
				{
					IBakedModel bakedModel = event.getModelRegistry().getObject(new ModelResourceLocation(item.getRegistryName(), null));
					event.getModelRegistry().putObject(new ModelResourceLocation(item.getRegistryName(), null), new BakedModelBuiltinWrapper(bakedModel));
				}
			});
		}
	}

}
