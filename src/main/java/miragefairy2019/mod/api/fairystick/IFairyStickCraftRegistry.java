package miragefairy2019.mod.api.fairystick;

import java.util.Optional;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IFairyStickCraftRegistry
{

	public void registerRecipe(IFairyStickCraftRecipe recipe);

	public Optional<IFairyStickCraftResult> getResult(Optional<EntityPlayer> oPlayer, World world, BlockPos pos, ItemStack itemStackFairyStick);

}
