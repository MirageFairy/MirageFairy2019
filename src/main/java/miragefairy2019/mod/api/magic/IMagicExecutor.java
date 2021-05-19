package miragefairy2019.mod.api.magic;

import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;

public interface IMagicExecutor
{

	public default EnumActionResult onItemRightClick(EnumHand hand)
	{
		return EnumActionResult.PASS;
	}

	public default void onUpdate(int itemSlot, boolean isSelected)
	{

	}

}
