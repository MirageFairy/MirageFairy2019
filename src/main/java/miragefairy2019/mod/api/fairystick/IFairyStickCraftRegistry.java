package miragefairy2019.mod.api.fairystick;

import java.util.Optional;

import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IFairyStickCraftRegistry
{

	public void addRecipe(IFairyStickCraftRecipe recipe);

	public Optional<IFairyStickCraftResult> getResult(Optional<EntityPlayer> oPlayer, World world, BlockPos pos, ItemStack itemStackFairyStick);

	public ISuppliterator<IFairyStickCraftRecipe> getRecipes();

}
