package miragefairy2019.mod.common.fairystick;

import miragefairy2019.mod.api.fairystick.IFairyStickCraftCondition;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftEnvironment;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftExecutor;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class FairyStickCraftConditionUseItem implements IFairyStickCraftCondition
{

	private Ingredient ingredient;

	public FairyStickCraftConditionUseItem(Ingredient ingredient)
	{
		this.ingredient = ingredient;
	}

	@Override
	public boolean test(IFairyStickCraftEnvironment environment, IFairyStickCraftExecutor executor)
	{

		if (!ingredient.apply(environment.getItemStackFairyStick())) return false;

		executor.hookOnCraft(setterItemStackFairyStick -> {
			ItemStack itemStackFairyStick = environment.getItemStackFairyStick();

			// コンテナアイテム計算
			ItemStack itemStackContainer = itemStackFairyStick.getItem().hasContainerItem(itemStackFairyStick)
				? itemStackFairyStick.getItem().getContainerItem(itemStackFairyStick)
				: ItemStack.EMPTY;

			// 破損
			setterItemStackFairyStick.accept(itemStackContainer);

			// エフェクト
			EntityPlayer player = environment.getPlayer().orElse(null);
			if (player != null) {
				if (!itemStackFairyStick.isEmpty()) {
					if (itemStackContainer.isEmpty()) {
						player.renderBrokenItemStack(itemStackFairyStick);
					}
				}
			}

		});
		return true;
	}

	@Override
	public ISuppliterator<Ingredient> getIngredientsInput()
	{
		return ISuppliterator.of(ingredient);
	}

}
