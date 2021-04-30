package miragefairy2019.mod.modules.fairy.registry;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class FairyRelation
{

	public Set<Predicate<ItemStack>> predicatesItemStack = new HashSet<>();

	public void addFairyRelationItemStack(Predicate<ItemStack> predicate)
	{
		predicatesItemStack.add(predicate);
	}

	public Set<Predicate<IBlockState>> predicatesBlockState = new HashSet<>();

	public void addFairyRelationBlockState(Predicate<IBlockState> predicate)
	{
		predicatesBlockState.add(predicate);
	}

}
