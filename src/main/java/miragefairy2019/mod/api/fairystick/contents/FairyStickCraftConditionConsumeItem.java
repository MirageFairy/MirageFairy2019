package miragefairy2019.mod.api.fairystick.contents;

import miragefairy2019.mod.api.fairystick.IFairyStickCraft;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftCondition;
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
	public boolean test(IFairyStickCraft fairyStickCraft)
	{
		// TODO 同種のアイテムを登録したとき、スタックされていると反応しない

		// アイテムを抽出する
		EntityItem entity = fairyStickCraft.pullItem(ingredient).orElse(null);
		if (entity == null) return false;

		//

		fairyStickCraft.hookOnCraft(() -> {

			entity.getItem().shrink(1);
			if (entity.getItem().isEmpty()) fairyStickCraft.getWorld().removeEntity(entity);

			fairyStickCraft.getWorld().spawnParticle(EnumParticleTypes.SPELL_MOB, entity.posX, entity.posY, entity.posZ, 1, 0, 0);

		});
		fairyStickCraft.hookOnUpdate(() -> {

			for (int i = 0; i < 2; i++) {
				fairyStickCraft.getWorld().spawnParticle(EnumParticleTypes.SPELL_MOB, entity.posX, entity.posY, entity.posZ, 0, 1, 0);
			}

		});
		return true;
	}

}
