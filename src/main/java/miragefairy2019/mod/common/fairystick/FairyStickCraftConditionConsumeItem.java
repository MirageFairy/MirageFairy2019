package miragefairy2019.mod.common.fairystick;

import miragefairy2019.mod.api.fairystick.IFairyStickCraftCondition;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftEnvironment;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftExecutor;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EnumParticleTypes;

public class FairyStickCraftConditionConsumeItem implements IFairyStickCraftCondition
{

	private Ingredient ingredient;

	public FairyStickCraftConditionConsumeItem(Ingredient ingredient)
	{
		this.ingredient = ingredient;
	}

	@Override
	public boolean test(IFairyStickCraftEnvironment environment, IFairyStickCraftExecutor executor)
	{
		// TODO 同種のアイテムを登録したとき、スタックされていると反応しない

		// アイテムを抽出する
		EntityItem entity = environment.pullItem(ingredient).orElse(null);
		if (entity == null) return false;

		//

		executor.hookOnCraft(() -> {

			entity.getItem().shrink(1);
			if (entity.getItem().isEmpty()) environment.getWorld().removeEntity(entity);

			environment.getWorld().spawnParticle(EnumParticleTypes.SPELL_MOB, entity.posX, entity.posY, entity.posZ, 1, 0, 0);

		});
		executor.hookOnUpdate(() -> {

			for (int i = 0; i < 2; i++) {
				environment.getWorld().spawnParticle(EnumParticleTypes.SPELL_MOB, entity.posX, entity.posY, entity.posZ, 0, 1, 0);
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
