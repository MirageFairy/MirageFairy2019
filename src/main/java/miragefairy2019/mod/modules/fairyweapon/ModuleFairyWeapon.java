package miragefairy2019.mod.modules.fairyweapon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.Provider;
import miragefairy2019.mod.modules.fairyweapon.items.ItemFairySword;
import miragefairy2019.mod.modules.fairyweapon.items.ItemFairyWandBreaking;
import miragefairy2019.mod.modules.fairyweapon.items.ItemFairyWandBreaking2;
import miragefairy2019.mod.modules.fairyweapon.items.ItemFairyWandCrafting;
import miragefairy2019.mod.modules.fairyweapon.items.ItemFairyWandCrafting2;
import miragefairy2019.mod.modules.fairyweapon.items.ItemFairyWandCrafting3;
import miragefairy2019.mod.modules.fairyweapon.items.ItemFairyWandMelting;
import miragefairy2019.mod.modules.fairyweapon.items.ItemFairyWandMelting2;
import miragefairy2019.mod.modules.fairyweapon.items.ItemFairyWandSummoning;
import miragefairy2019.mod.modules.fairyweapon.items.ItemMagicWandCollecting;
import miragefairy2019.mod.modules.fairyweapon.items.ItemMagicWandLight;
import miragefairy2019.mod.modules.fairyweapon.items.ItemMiragiumAxe;
import miragefairy2019.mod.modules.fairyweapon.items.ItemMiragiumSword;
import miragefairy2019.mod.modules.fairyweapon.items.ItemOcarinaBase;
import miragefairy2019.mod.modules.fairyweapon.items.ItemOcarinaTemptation;
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

	public static Items items = new Items();

	public static class Items
	{

		public Provider<ItemFairyWandCrafting> fairyWandCrafting = item(ItemFairyWandCrafting::new, "crafting_fairy_wand", "fairyWandCrafting").ore("mirageFairy2019CraftingToolFairyWandCrafting"); // 技巧のステッキ
		public Provider<ItemFairyWandCrafting2> fairyWandCrafting2 = item(ItemFairyWandCrafting2::new, "crafting_fairy_wand_2", "fairyWandCrafting2").ore("mirageFairy2019CraftingToolFairyWandCrafting"); // 技巧のステッキ2
		public Provider<ItemFairyWandCrafting3> fairyWandCrafting3 = item(ItemFairyWandCrafting3::new, "crafting_fairy_wand_3", "fairyWandCrafting3").ore("mirageFairy2019CraftingToolFairyWandCrafting"); // 技巧のステッキ3
		public Provider<ItemFairyWandMelting> fairyWandMelting = item(ItemFairyWandMelting::new, "melting_fairy_wand", "fairyWandMelting").ore("mirageFairy2019CraftingToolFairyWandMelting"); // 紅蓮のステッキ
		public Provider<ItemFairyWandMelting2> fairyWandMelting2 = item(ItemFairyWandMelting2::new, "melting_fairy_wand_2", "fairyWandMelting2").ore("mirageFairy2019CraftingToolFairyWandMelting"); // 紅蓮のステッキ
		public Provider<ItemFairyWandBreaking> fairyWandBreaking = item(ItemFairyWandBreaking::new, "breaking_fairy_wand", "fairyWandBreaking").ore("mirageFairy2019CraftingToolFairyWandBreaking"); // 破壊のステッキ
		public Provider<ItemFairyWandBreaking2> fairyWandBreaking2 = item(ItemFairyWandBreaking2::new, "breaking_fairy_wand_2", "fairyWandBreaking2").ore("mirageFairy2019CraftingToolFairyWandBreaking"); // 破壊のステッキ2
		public Provider<ItemFairyWandSummoning> fairyWandSummoning = item(ItemFairyWandSummoning::new, "summoning_fairy_wand", "fairyWandSummoning").ore("mirageFairy2019CraftingToolFairyWandSummoning"); // 召喚のステッキ

		public Provider<ItemMiragiumSword> miragiumSword = item(ItemMiragiumSword::new, "miragium_sword", "miragiumSword"); // ミラジウムの剣
		public Provider<ItemFairySword> fairySword = item(ItemFairySword::new, "fairy_sword", "fairySword"); // 妖精剣
		public Provider<ItemMiragiumAxe> miragiumAxe = item(ItemMiragiumAxe::new, "miragium_axe", "miragiumAxe"); // ミラジウムの斧
		public Provider<ItemMagicWandLight> magicWandLight = item(ItemMagicWandLight::new, "light_magic_wand", "magicWandLight"); // 光のロッド
		public Provider<ItemMagicWandCollecting> magicWandCollecting = item(ItemMagicWandCollecting::new, "collecting_magic_wand", "magicWandCollecting"); // 収集のロッド
		public Provider<ItemOcarinaBase> ocarinaBase = item(ItemOcarinaBase::new, "ocarina_base", "ocarinaBase"); // オカリナベース
		public Provider<ItemOcarinaTemptation> ocarinaTemptation = item(ItemOcarinaTemptation::new, "temptation_ocarina", "ocarinaTemptation"); // 魅惑のオカリナ

		//

		private List<Provider<?>> providers;

		private <T extends Item> ProviderExtension<T> item(Supplier<T> sItem, String registryName, String unlocalizedName)
		{
			ProviderExtension<T> provider = new ProviderExtension<T>(sItem, unlocalizedName, registryName);
			if (providers == null) providers = new ArrayList<>();
			providers.add(provider);
			return provider;
		}

		private static class ProviderExtension<T extends Item> extends Provider<T>
		{

			public ProviderExtension(Supplier<T> sItem, String unlocalizedName, String registryName)
			{
				hook(erMod -> {
					erMod.registerItem.register(b -> {
						T item = sItem.get();
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
						set(item);
					});
				});
			}

			public ProviderExtension<T> ore(String oreName)
			{
				hook(erMod -> {
					erMod.createItemStack.register(ic -> {
						OreDictionary.registerOre(oreName, new ItemStack(get(), 1, OreDictionary.WILDCARD_VALUE));
					});
				});
				return this;
			}

		}

	}

	public static void init(EventRegistryMod erMod)
	{
		items.providers.forEach(p -> p.init(erMod));
		erMod.addRecipe.register(() -> {

			// スフィア交換レシピ
			GameRegistry.findRegistry(IRecipe.class).register(new RecipesSphereReplacement());

			// 妖精搭乗レシピ
			GameRegistry.findRegistry(IRecipe.class).register(new RecipesCombining());
			GameRegistry.findRegistry(IRecipe.class).register(new RecipesUncombining());

		});
	}

}
