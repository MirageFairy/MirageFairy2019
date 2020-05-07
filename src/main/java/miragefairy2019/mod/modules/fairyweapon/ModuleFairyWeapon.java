package miragefairy2019.mod.modules.fairyweapon;

import static miragefairy2019.mod.api.composite.ApiComposite.*;
import static miragefairy2019.mod.api.composite.Components.*;
import static miragefairy2019.mod.api.fairy.AbilityTypes.*;
import static miragefairy2019.mod.api.fairy.ApiFairy.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.composite.Components;
import miragefairy2019.mod.api.composite.IComponentInstance;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.lib.BakedModelBuiltinWrapper;
import miragefairy2019.mod.lib.Configurator;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.Monad;
import miragefairy2019.mod.modules.fairyweapon.item.ItemBellBase;
import miragefairy2019.mod.modules.fairyweapon.item.ItemBellChristmas;
import miragefairy2019.mod.modules.fairyweapon.item.ItemBellFlowerPicking;
import miragefairy2019.mod.modules.fairyweapon.item.ItemFairySword;
import miragefairy2019.mod.modules.fairyweapon.item.ItemFairyWandBreaking;
import miragefairy2019.mod.modules.fairyweapon.item.ItemFairyWandCrafting;
import miragefairy2019.mod.modules.fairyweapon.item.ItemFairyWandHydrating;
import miragefairy2019.mod.modules.fairyweapon.item.ItemFairyWandMelting;
import miragefairy2019.mod.modules.fairyweapon.item.ItemFairyWandPolishing;
import miragefairy2019.mod.modules.fairyweapon.item.ItemFairyWandSummoning;
import miragefairy2019.mod.modules.fairyweapon.item.ItemFairyWeaponBase;
import miragefairy2019.mod.modules.fairyweapon.item.ItemMagicWandBase;
import miragefairy2019.mod.modules.fairyweapon.item.ItemMagicWandCollecting;
import miragefairy2019.mod.modules.fairyweapon.item.ItemMagicWandLight;
import miragefairy2019.mod.modules.fairyweapon.item.ItemMiragiumAxe;
import miragefairy2019.mod.modules.fairyweapon.item.ItemMiragiumSword;
import miragefairy2019.mod.modules.fairyweapon.item.ItemOcarinaBase;
import miragefairy2019.mod.modules.fairyweapon.item.ItemOcarinaTemptation;
import miragefairy2019.mod.modules.fairyweapon.recipe.RecipesCombining;
import miragefairy2019.mod.modules.fairyweapon.recipe.RecipesSphereReplacement;
import miragefairy2019.mod.modules.fairyweapon.recipe.RecipesUncombining;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class ModuleFairyWeapon
{

	private static class Items
	{

		@SuppressWarnings("unused")
		private static List<Configurator<?>> getConfigurators()
		{
			List<Configurator<?>> configurators = new ArrayList<>();

			// 技巧のステッキ
			fairyWeapon(ItemFairyWandCrafting::new, "crafting_fairy_wand", "fairyWandCrafting")
				.bind(addOreName("mirageFairy2019CraftingToolFairyWandCrafting"))
				.bind(addComponent(createComponentInstance(wood.get(), 1)))
				.bind(addComponent(createComponentInstance(getComponentAbilityType(craft.get()))))
				.bind(setFairyWandStatusOfTier(1))
				.bind(register(configurators));

			// 技巧のステッキ2
			fairyWeapon(ItemFairyWandCrafting::new, "crafting_fairy_wand_2", "fairyWandCrafting2")
				.bind(addOreName("mirageFairy2019CraftingToolFairyWandCrafting"))
				.bind(addComponent(createComponentInstance(miragium.get(), 1)))
				.bind(addComponent(createComponentInstance(getComponentAbilityType(craft.get()))))
				.bind(setFairyWandStatusOfTier(2))
				.bind(register(configurators));

			// 技巧のステッキ3
			fairyWeapon(ItemFairyWandCrafting::new, "crafting_fairy_wand_3", "fairyWandCrafting3")
				.bind(addOreName("mirageFairy2019CraftingToolFairyWandCrafting"))
				.bind(addComponent(createComponentInstance(miragium.get(), 1)))
				.bind(addComponent(createComponentInstance(magnetite.get(), 1)))
				.bind(addComponent(createComponentInstance(getComponentAbilityType(craft.get()))))
				.bind(setFairyWandStatusOfTier(3))
				.bind(register(configurators));

			// 潤いのステッキ
			fairyWeapon(ItemFairyWandHydrating::new, "hydrating_fairy_wand", "fairyWandHydrating")
				.bind(addOreName("mirageFairy2019CraftingToolFairyWandHydrating"))
				.bind(addOreName("container1000Water"))
				.bind(addComponent(createComponentInstance(wood.get(), 1)))
				.bind(addComponent(createComponentInstance(getComponentAbilityType(water.get()))))
				.bind(setFairyWandStatusOfTier(1))
				.bind(register(configurators));

			// 潤いのステッキ2
			fairyWeapon(ItemFairyWandHydrating::new, "hydrating_fairy_wand_2", "fairyWandHydrating2")
				.bind(addOreName("mirageFairy2019CraftingToolFairyWandHydrating"))
				.bind(addOreName("container1000Water"))
				.bind(addComponent(createComponentInstance(miragium.get(), 1)))
				.bind(addComponent(createComponentInstance(getComponentAbilityType(water.get()))))
				.bind(setFairyWandStatusOfTier(2))
				.bind(register(configurators));

			// 潤いのステッキ3
			fairyWeapon(ItemFairyWandHydrating::new, "hydrating_fairy_wand_3", "fairyWandHydrating3")
				.bind(addOreName("mirageFairy2019CraftingToolFairyWandHydrating"))
				.bind(addOreName("container1000Water"))
				.bind(addComponent(createComponentInstance(miragium.get(), 1)))
				.bind(addComponent(createComponentInstance(moonstone.get(), 1)))
				.bind(addComponent(createComponentInstance(getComponentAbilityType(water.get()))))
				.bind(setFairyWandStatusOfTier(3))
				.bind(register(configurators));

			// 紅蓮のステッキ
			fairyWeapon(ItemFairyWandMelting::new, "melting_fairy_wand", "fairyWandMelting")
				.bind(addOreName("mirageFairy2019CraftingToolFairyWandMelting"))
				.bind(addComponent(createComponentInstance(miragium.get(), 1)))
				.bind(addComponent(createComponentInstance(getComponentAbilityType(flame.get()))))
				.bind(setFairyWandStatusOfTier(2))
				.bind(register(configurators));

			// 紅蓮のステッキ
			fairyWeapon(ItemFairyWandMelting::new, "melting_fairy_wand_2", "fairyWandMelting2")
				.bind(addOreName("mirageFairy2019CraftingToolFairyWandMelting"))
				.bind(addComponent(createComponentInstance(miragium.get(), 1)))
				.bind(addComponent(createComponentInstance(fluorite.get(), 1)))
				.bind(addComponent(createComponentInstance(getComponentAbilityType(flame.get()))))
				.bind(setFairyWandStatusOfTier(3))
				.bind(register(configurators));

			// 破壊のステッキ
			fairyWeapon(ItemFairyWandBreaking::new, "breaking_fairy_wand", "fairyWandBreaking")
				.bind(addOreName("mirageFairy2019CraftingToolFairyWandBreaking"))
				.bind(addComponent(createComponentInstance(miragium.get(), 1)))
				.bind(addComponent(createComponentInstance(getComponentAbilityType(breaking.get()))))
				.bind(setFairyWandStatusOfTier(2))
				.bind(register(configurators));

			// 破壊のステッキ2
			fairyWeapon(ItemFairyWandBreaking::new, "breaking_fairy_wand_2", "fairyWandBreaking2")
				.bind(addOreName("mirageFairy2019CraftingToolFairyWandBreaking"))
				.bind(addComponent(createComponentInstance(miragium.get(), 1)))
				.bind(addComponent(createComponentInstance(sulfur.get(), 1)))
				.bind(addComponent(createComponentInstance(getComponentAbilityType(breaking.get()))))
				.bind(setFairyWandStatusOfTier(3))
				.bind(register(configurators));

			// 珠玉のステッキ
			fairyWeapon(ItemFairyWandPolishing::new, "polishing_fairy_wand", "fairyWandPolishing")
				.bind(addOreName("mirageFairy2019CraftingToolFairyWandPolishing"))
				.bind(addComponent(createComponentInstance(miragium.get(), 1)))
				.bind(addComponent(createComponentInstance(moonstone.get(), 1)))
				.bind(addComponent(createComponentInstance(getComponentAbilityType(crystal.get()))))
				.bind(setFairyWandStatusOfTier(3))
				.bind(register(configurators));

			// 召喚のステッキ
			fairyWeapon(ItemFairyWandSummoning::new, "summoning_fairy_wand", "fairyWandSummoning")
				.bind(addOreName("mirageFairy2019CraftingToolFairyWandSummoning"))
				.bind(addComponent(createComponentInstance(miragium.get(), 1)))
				.bind(addComponent(createComponentInstance(moonstone.get(), 1)))
				.bind(addComponent(createComponentInstance(getComponentAbilityType(crystal.get()))))
				.bind(setFairyWandStatusOfTier(3))
				.bind(register(configurators));

			//

			// ミラジウムの剣
			Configurator<ItemMiragiumSword> miragiumSword = fairyWeapon(ItemMiragiumSword::new, "miragium_sword", "miragiumSword")
				.bind(addComponent(createComponentInstance(Components.miragium.get(), 2)))
				.bind(addComponent(createComponentInstance(Components.wood.get(), 0.5)))
				.bind(addComponent(createComponentInstance(getComponentAbilityType(attack.get()))))
				.bind(addComponent(createComponentInstance(getComponentAbilityType(slash.get()))))
				.bind(setWeaponStatusOfTier(2))
				.bind(register(configurators));

			// 妖精剣
			Configurator<ItemFairySword> fairySword = fairyWeapon(ItemFairySword::new, "fairy_sword", "fairySword")
				.bind(addComponent(miragiumSword))
				.bind(addComponent(createComponentInstance(Components.apatite.get(), 1)))
				.bind(addComponent(createComponentInstance(Components.fluorite.get(), 1)))
				.bind(addComponent(createComponentInstance(Components.sulfur.get(), 1)))
				.bind(addComponent(createComponentInstance(Components.cinnabar.get(), 1)))
				.bind(addComponent(createComponentInstance(Components.magnetite.get(), 1)))
				.bind(addComponent(createComponentInstance(Components.moonstone.get(), 1)))
				.bind(addComponent(createComponentInstance(getComponentAbilityType(attack.get()))))
				.bind(setWeaponStatusOfTier(3))
				.bind(register(configurators));

			// ミラジウムの斧
			Configurator<ItemMiragiumAxe> miragiumAxe = fairyWeapon(ItemMiragiumAxe::new, "miragium_axe", "miragiumAxe")
				.bind(addComponent(createComponentInstance(Components.miragium.get(), 3)))
				.bind(addComponent(createComponentInstance(Components.wood.get(), 1)))
				.bind(addComponent(createComponentInstance(getComponentAbilityType(slash.get()))))
				.bind(addComponent(createComponentInstance(getComponentAbilityType(fell.get()))))
				.bind(setWeaponStatusOfTier(2))
				.bind(register(configurators));

			// ロッドベース
			Configurator<ItemMagicWandBase> magicWandBase = fairyWeapon(ItemMagicWandBase::new, "magic_wand_base", "magicWandBase")
				.bind(addComponent(createComponentInstance(Components.miragium.get(), 1)))
				.bind(addComponent(createComponentInstance(Components.fluorite.get(), 1)))
				.bind(addComponent(createComponentInstance(Components.miragium.get(), 4)))
				.bind(addComponent(createComponentInstance(getComponentAbilityType(knowledge.get()))))
				.bind(setWeaponStatusOfTier(3))
				.bind(register(configurators));

			// 光のロッド
			Configurator<ItemMagicWandLight> magicWandLight = fairyWeapon(ItemMagicWandLight::new, "light_magic_wand", "magicWandLight")
				.bind(addComponent(magicWandBase))
				.bind(addComponent(createComponentInstance(Components.apatite.get(), 3)))
				.bind(addComponent(createComponentInstance(getComponentAbilityType(light.get()))))
				.bind(setWeaponStatusOfTier(3))
				.bind(register(configurators));

			// 収集のロッド
			Configurator<ItemMagicWandCollecting> magicWandCollecting = fairyWeapon(ItemMagicWandCollecting::new, "collecting_magic_wand", "magicWandCollecting")
				.bind(addComponent(magicWandBase))
				.bind(addComponent(createComponentInstance(Components.obsidian.get(), 2)))
				.bind(addComponent(createComponentInstance(Components.fluorite.get(), 1)))
				.bind(addComponent(createComponentInstance(getComponentAbilityType(warp.get()))))
				.bind(setWeaponStatusOfTier(3))
				.bind(register(configurators));

			// オカリナベース
			Configurator<ItemOcarinaBase> ocarinaBase = fairyWeapon(ItemOcarinaBase::new, "ocarina_base", "ocarinaBase")
				.bind(addComponent(createComponentInstance(Components.miragium.get(), 1)))
				.bind(addComponent(createComponentInstance(Components.apatite.get(), 4)))
				.bind(addComponent(createComponentInstance(getComponentAbilityType(art.get()))))
				.bind(setWeaponStatusOfTier(3))
				.bind(register(configurators));

			// 魅惑のオカリナ
			Configurator<ItemOcarinaTemptation> ocarinaTemptation = fairyWeapon(ItemOcarinaTemptation::new, "temptation_ocarina", "ocarinaTemptation")
				.bind(addComponent(ocarinaBase))
				.bind(addComponent(createComponentInstance(Components.pyrope.get(), 4)))
				.bind(addComponent(createComponentInstance(getComponentAbilityType(food.get()))))
				.bind(setWeaponStatusOfTier(3))
				.bind(register(configurators));

			// 鐘ベース
			Configurator<ItemBellBase> bellBase = fairyWeapon(ItemBellBase::new, "bell_base", "bellBase")
				.bind(addComponent(createComponentInstance(Components.miragium.get(), 0.5)))
				.bind(addComponent(createComponentInstance(Components.miragium.get(), 3)))
				.bind(addComponent(createComponentInstance(getComponentAbilityType(submission.get()))))
				.bind(setWeaponStatusOfTier(2))
				.bind(register(configurators));

			// 花摘みの鐘
			Configurator<ItemBellFlowerPicking> bellFlowerPicking = fairyWeapon(ItemBellFlowerPicking::new, "flower_picking_bell", "bellFlowerPicking")
				.bind(addComponent(bellBase))
				.bind(addComponent(createComponentInstance(Components.miragium.get(), 0.5)))
				.bind(addComponent(createComponentInstance(Components.magnetite.get(), 0.5)))
				.bind(addComponent(createComponentInstance(Components.pyrope.get(), 2)))
				.bind(addComponent(createComponentInstance(Components.gold.get(), 1)))
				.bind(addComponent(createComponentInstance(getComponentAbilityType(slash.get()))))
				.bind(setWeaponStatusOfTier(3))
				.bind(register(configurators));

			// クリスマスの鐘
			Configurator<ItemBellChristmas> bellChristmas = fairyWeapon(ItemBellChristmas::new, "christmas_bell", "bellChristmas")
				.bind(addComponent(bellBase))
				.bind(addComponent(createComponentInstance(Components.miragium.get(), 0.5)))
				.bind(addComponent(createComponentInstance(Components.magnetite.get(), 0.5)))
				.bind(addComponent(createComponentInstance(Components.gold.get(), 10)))
				.bind(addComponent(createComponentInstance(getComponentAbilityType(christmas.get()))))
				.bind(addComponent(createComponentInstance(getComponentAbilityType(attack.get()))))
				.bind(setWeaponStatusOfTier(3))
				.bind(register(configurators));

			return configurators;
		}

		//

		private static <I extends Item> Monad<Configurator<I>> fairyWeapon(Supplier<I> sItem, String registryName, String unlocalizedName)
		{
			Configurator<I> c = new Configurator<>();

			c.hook(erMod -> {
				erMod.registerItem.register(b -> {
					I item = sItem.get();
					item.setRegistryName(ModMirageFairy2019.MODID, registryName);
					item.setUnlocalizedName(unlocalizedName);
					item.setCreativeTab(ApiMain.creativeTab());
					ForgeRegistries.ITEMS.register(item);
					if (ApiMain.side().isClient()) {

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
					c.set(item);
				});
			});

			return Monad.of(c);
		}

		private static <I extends Item> Function<Configurator<I>, Configurator<I>> register(List<Configurator<?>> configurators)
		{
			return c -> {
				configurators.add(c);
				return c;
			};
		}

		private static <I> Function<Configurator<I>, Monad<Configurator<I>>> onRegisterItem(Consumer<I> consumer)
		{
			return c -> {
				c.hook(erMod -> {
					erMod.registerItem.register(b -> {
						consumer.accept(c.get());
					});
				});
				return Monad.of(c);
			};
		}

		private static <I extends Item> Function<Configurator<I>, Monad<Configurator<I>>> addOreName(String oreName)
		{
			return c -> {
				c.hook(erMod -> {
					erMod.createItemStack.register(ic -> {
						OreDictionary.registerOre(oreName, new ItemStack(c.get(), 1, OreDictionary.WILDCARD_VALUE));
					});
				});
				return Monad.of(c);
			};
		}

		private static <I extends ItemFairyWeaponBase, I2 extends ItemFairyWeaponBase> Function<Configurator<I>, Monad<Configurator<I>>> addComponent(Configurator<I2> mci)
		{
			return c -> Monad.of(c)
				.bind(onRegisterItem(i -> i.addComponent(mci.get().getComposite())));
		}

		private static <I extends ItemFairyWeaponBase> Function<Configurator<I>, Monad<Configurator<I>>> addComponent(IComponentInstance componentInstance)
		{
			return c -> Monad.of(c)
				.bind(onRegisterItem(i -> i.addComponent(componentInstance)));
		}

		private static <I extends ItemFairyWeaponBase> Function<Configurator<I>, Monad<Configurator<I>>> setWeaponStatusOfTier(int tier)
		{
			return c -> Monad.of(c)
				.bind(onRegisterItem(i -> i.setMaxDamage(getDurability(tier) - 1)));
		}

		private static <I extends ItemFairyWeaponBase> Function<Configurator<I>, Monad<Configurator<I>>> setFairyWandStatusOfTier(int tier)
		{
			return c -> Monad.of(c)
				.bind(onRegisterItem(i -> i.setMaxDamage(getDurability(tier) / 2 - 1)));
		}

		private static int getDurability(int tier)
		{
			if (tier == 1) return 32;
			if (tier == 2) return 64;
			if (tier == 3) return 128;
			throw new IllegalArgumentException("Illegal tier: " + tier);
		}

	}

	public static void init(EventRegistryMod erMod)
	{
		Items.getConfigurators().forEach(p -> p.init(erMod));
		erMod.init.register(e -> {

			// LootingLevel付きのダメージソースが来たら適用するリスナ登録
			MinecraftForge.EVENT_BUS.register(new Object() {
				@SubscribeEvent
				public void accept(LootingLevelEvent event)
				{
					if (event.getDamageSource() instanceof IDamageSourceLooting) {
						event.setLootingLevel(Math.max(event.getLootingLevel(), ((IDamageSourceLooting) event.getDamageSource()).getLootingLevel()));
					}
				}
			});

		});
		erMod.addRecipe.register(() -> {

			// スフィア交換レシピ
			GameRegistry.findRegistry(IRecipe.class).register(new RecipesSphereReplacement());

			// 妖精搭乗レシピ
			GameRegistry.findRegistry(IRecipe.class).register(new RecipesCombining());
			GameRegistry.findRegistry(IRecipe.class).register(new RecipesUncombining());

		});
	}

}
