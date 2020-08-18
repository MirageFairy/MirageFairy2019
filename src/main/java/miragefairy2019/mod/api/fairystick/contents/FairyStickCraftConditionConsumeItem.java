package miragefairy2019.mod.api.fairystick.contents;

import java.util.function.Predicate;

import miragefairy2019.mod.api.fairystick.IFairyStickCraft;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftCondition;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;

public class FairyStickCraftConditionConsumeItem implements IFairyStickCraftCondition
{

	private Predicate<ItemStack> ingredient;

	public FairyStickCraftConditionConsumeItem(Predicate<ItemStack> ingredient)
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
		});
		fairyStickCraft.hookOnUpdate(() -> {
			fairyStickCraft.getWorld().spawnParticle(EnumParticleTypes.SPELL_MOB, entity.posX, entity.posY, entity.posZ, 0, 1, 0);
		});
		return true;
	}

}
