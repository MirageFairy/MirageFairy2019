package miragefairy2019.mod.modules.fairyweapon;

import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.modules.fairyweapon.recipe.RecipesCombining;
import miragefairy2019.mod.modules.fairyweapon.recipe.RecipesSphereReplacement;
import miragefairy2019.mod.modules.fairyweapon.recipe.RecipesUncombining;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModuleFairyWeapon
{

	public static void init(EventRegistryMod erMod)
	{
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
