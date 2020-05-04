package miragefairy2019.mod.modules.fairy.registry;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;

import miragefairy2019.mod.api.fairy.IFairySelector;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class FairySelector implements IFairySelector
{

	private FairyRelationRegistry fairyRelationRegistry;

	public FairySelector(FairyRelationRegistry fairyRelationRegistry)
	{
		this.fairyRelationRegistry = fairyRelationRegistry;
	}

	//

	private Set<ItemStack> itemStacks = new HashSet<>();

	@Override
	public IFairySelector add(ItemStack... itemStacks)
	{
		ISuppliterator.ofObjArray(itemStacks)
			.forEach(this.itemStacks::add);
		return this;
	}

	private Set<IBlockState> blockStates = new HashSet<>();

	@Override
	public IFairySelector add(IBlockState... blockStates)
	{
		ISuppliterator.ofObjArray(blockStates)
			.forEach(this.blockStates::add);
		return this;
	}

	//

	@Override
	public ISuppliterator<ResourceLocation> select()
	{
		return ISuppliterator.ofIterable(fairyRelationRegistry.map.entrySet())
			.filter(e -> {
				FairyRelation fairyRelation = e.getValue();

				for (Predicate<ItemStack> predicate : fairyRelation.predicatesItemStack) {
					for (ItemStack itemStack : itemStacks) {
						if (predicate.test(itemStack)) return true;
					}
				}

				for (Predicate<IBlockState> predicate : fairyRelation.predicatesBlockState) {
					for (IBlockState blockState : blockStates) {
						if (predicate.test(blockState)) return true;
					}
				}

				return false;
			})
			.map(Entry::getKey);
	}

}
