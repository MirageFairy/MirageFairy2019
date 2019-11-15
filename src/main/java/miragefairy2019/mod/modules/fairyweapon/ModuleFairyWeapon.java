package miragefairy2019.mod.modules.fairyweapon;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiFairyWeapon;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.modules.fairyweapon.items.ItemBreakingFairyWand;
import miragefairy2019.mod.modules.fairyweapon.items.ItemBreakingFairyWand2;
import miragefairy2019.mod.modules.fairyweapon.items.ItemCollectingMagicWand;
import miragefairy2019.mod.modules.fairyweapon.items.ItemCraftingFairyWand;
import miragefairy2019.mod.modules.fairyweapon.items.ItemCraftingFairyWand2;
import miragefairy2019.mod.modules.fairyweapon.items.ItemCraftingFairyWand3;
import miragefairy2019.mod.modules.fairyweapon.items.ItemFairySword;
import miragefairy2019.mod.modules.fairyweapon.items.ItemLightMagicWand;
import miragefairy2019.mod.modules.fairyweapon.items.ItemMeltingFairyWand;
import miragefairy2019.mod.modules.fairyweapon.items.ItemMeltingFairyWand2;
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
	public static ItemCraftingFairyWand2 itemCraftingFairyWand2;
	public static ItemCraftingFairyWand3 itemCraftingFairyWand3;
	public static ItemMeltingFairyWand itemMeltingFairyWand;
	public static ItemMeltingFairyWand2 itemMeltingFairyWand2;
	public static ItemFairySword itemFairySword;
	public static ItemMiragiumAxe itemMiragiumAxe;
	public static ItemLightMagicWand itemLightMagicWand;
	public static ItemSummoningFairyWand itemSummoningFairyWand;
	public static ItemCollectingMagicWand itemCollectingMagicWand;
	public static ItemBreakingFairyWand itemBreakingFairyWand;
	public static ItemBreakingFairyWand2 itemBreakingFairyWand2;
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

			// 加工のステッキ2
			{
				ItemCraftingFairyWand2 item = new ItemCraftingFairyWand2();
				item.setRegistryName(ModMirageFairy2019.MODID, "crafting_fairy_wand_2");
				item.setUnlocalizedName("craftingFairyWand2");
				item.setCreativeTab(ApiMain.creativeTab);
				ForgeRegistries.ITEMS.register(item);
				hookBakedModelWrapper(item);
				if (ApiMain.side.isClient()) ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), null));
				ApiFairyWeapon.itemCraftingFairyWand2 = itemCraftingFairyWand2 = item;
			}

			// 加工のステッキ3
			{
				ItemCraftingFairyWand3 item = new ItemCraftingFairyWand3();
				item.setRegistryName(ModMirageFairy2019.MODID, "crafting_fairy_wand_3");
				item.setUnlocalizedName("craftingFairyWand3");
				item.setCreativeTab(ApiMain.creativeTab);
				ForgeRegistries.ITEMS.register(item);
				hookBakedModelWrapper(item);
				if (ApiMain.side.isClient()) ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), null));
				ApiFairyWeapon.itemCraftingFairyWand3 = itemCraftingFairyWand3 = item;
			}

			// 紅蓮のステッキ
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

			// 紅蓮のステッキ
			{
				ItemMeltingFairyWand2 item = new ItemMeltingFairyWand2();
				item.setRegistryName(ModMirageFairy2019.MODID, "melting_fairy_wand_2");
				item.setUnlocalizedName("meltingFairyWand2");
				item.setCreativeTab(ApiMain.creativeTab);
				ForgeRegistries.ITEMS.register(item);
				hookBakedModelWrapper(item);
				if (ApiMain.side.isClient()) ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), null));
				ApiFairyWeapon.itemMeltingFairyWand2 = itemMeltingFairyWand2 = item;
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

			// 破壊のステッキ2
			{
				ItemBreakingFairyWand2 item = new ItemBreakingFairyWand2();
				item.setRegistryName(ModMirageFairy2019.MODID, "breaking_fairy_wand_2");
				item.setUnlocalizedName("breakingFairyWand2");
				item.setCreativeTab(ApiMain.creativeTab);
				ForgeRegistries.ITEMS.register(item);
				hookBakedModelWrapper(item);
				if (ApiMain.side.isClient()) ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), null));
				ApiFairyWeapon.itemBreakingFairyWand2 = itemBreakingFairyWand2 = item;
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
			OreDictionary.registerOre("mirageFairy2019CraftingToolFairyWandCrafting", new ItemStack(itemCraftingFairyWand2, 1, OreDictionary.WILDCARD_VALUE));
			OreDictionary.registerOre("mirageFairy2019CraftingToolFairyWandCrafting", new ItemStack(itemCraftingFairyWand3, 1, OreDictionary.WILDCARD_VALUE));
			OreDictionary.registerOre("mirageFairy2019CraftingToolFairyWandMelting", new ItemStack(itemMeltingFairyWand, 1, OreDictionary.WILDCARD_VALUE));
			OreDictionary.registerOre("mirageFairy2019CraftingToolFairyWandMelting", new ItemStack(itemMeltingFairyWand2, 1, OreDictionary.WILDCARD_VALUE));
			OreDictionary.registerOre("mirageFairy2019CraftingToolFairyWandSummoning", new ItemStack(itemSummoningFairyWand, 1, OreDictionary.WILDCARD_VALUE));
			OreDictionary.registerOre("mirageFairy2019CraftingToolFairyWandBreaking", new ItemStack(itemBreakingFairyWand, 1, OreDictionary.WILDCARD_VALUE));
			OreDictionary.registerOre("mirageFairy2019CraftingToolFairyWandBreaking", new ItemStack(itemBreakingFairyWand2, 1, OreDictionary.WILDCARD_VALUE));
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
