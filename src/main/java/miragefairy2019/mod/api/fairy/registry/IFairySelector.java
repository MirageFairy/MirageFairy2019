package miragefairy2019.mod.api.fairy.registry;

import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface IFairySelector
{

	public IFairySelector add(ItemStack... itemStacks);

	public IFairySelector add(IBlockState... blockStates);

	public ISuppliterator<ResourceLocation> select();

}
