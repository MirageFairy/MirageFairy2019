package miragefairy2019.mod.modules.fairyweapon;

import miragefairy2019.mod.ModMirageFairy2019;
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
import miragefairy2019.mod.modules.fairyweapon.items.ItemMiragiumSword;
import miragefairy2019.mod.modules.fairyweapon.items.ItemOcarinaBase;
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
	public static ItemOcarinaBase itemOcarinaBase;
	public static ItemMiragiumSword itemMiragiumSword;

	public static void init(EventRegistryMod erMod)
	{
		erMod.registerItem.register(b -> {
			itemCraftingFairyWand = item(new ItemCraftingFairyWand(), "crafting_fairy_wand", "craftingFairyWand"); // 技巧のステッキ
			itemCraftingFairyWand2 = item(new ItemCraftingFairyWand2(), "crafting_fairy_wand_2", "craftingFairyWand2"); // 技巧のステッキ2
			itemCraftingFairyWand3 = item(new ItemCraftingFairyWand3(), "crafting_fairy_wand_3", "craftingFairyWand3"); // 技巧のステッキ3
			itemMeltingFairyWand = item(new ItemMeltingFairyWand(), "melting_fairy_wand", "meltingFairyWand"); // 紅蓮のステッキ
			itemMeltingFairyWand2 = item(new ItemMeltingFairyWand2(), "melting_fairy_wand_2", "meltingFairyWand2"); // 紅蓮のステッキ
			itemFairySword = item(new ItemFairySword(), "fairy_sword", "fairySword"); // 妖精剣
			itemMiragiumAxe = item(new ItemMiragiumAxe(), "miragium_axe", "miragiumAxe"); // ミラジウムの斧
			itemLightMagicWand = item(new ItemLightMagicWand(), "light_magic_wand", "lightMagicWand"); // 光のロッド
			itemSummoningFairyWand = item(new ItemSummoningFairyWand(), "summoning_fairy_wand", "summoningFairyWand"); // 召喚のステッキ
			itemCollectingMagicWand = item(new ItemCollectingMagicWand(), "collecting_magic_wand", "collectingMagicWand"); // 収集のロッド
			itemBreakingFairyWand = item(new ItemBreakingFairyWand(), "breaking_fairy_wand", "breakingFairyWand"); // 破壊のステッキ
			itemBreakingFairyWand2 = item(new ItemBreakingFairyWand2(), "breaking_fairy_wand_2", "breakingFairyWand2"); // 破壊のステッキ2
			itemTemptationOcarina = item(new ItemTemptationOcarina(), "temptation_ocarina", "temptationOcarina"); // 魅惑のオカリナ
			itemOcarinaBase = item(new ItemOcarinaBase(), "ocarina_base", "ocarinaBase"); // オカリナベース
			itemMiragiumSword = item(new ItemMiragiumSword(), "miragium_sword", "miragiumSword"); // ミラジウムの剣
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

	private static <I extends Item> I item(I item, String registryName, String unlocalizedName)
	{
		item.setRegistryName(ModMirageFairy2019.MODID, registryName);
		item.setUnlocalizedName(unlocalizedName);
		item.setCreativeTab(ApiMain.creativeTab);
		ForgeRegistries.ITEMS.register(item);
		if (ApiMain.side.isClient()) {

			// 搭乗妖精の描画
			MinecraftForge.EVENT_BUS.register(new Object() {
				@SubscribeEvent
				public void accept(ModelBakeEvent event)
				{
					IBakedModel bakedModel = event.getModelRegistry().getObject(new ModelResourceLocation(item.getRegistryName(), null));
					event.getModelRegistry().putObject(new ModelResourceLocation(item.getRegistryName(), null), new BakedModelBuiltinWrapper(bakedModel));
				}
			});

			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), null));

		}
		return item;
	}

}
