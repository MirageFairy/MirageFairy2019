package miragefairy2019.mod.api.magic;

import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IMagicHandler
{

	public ISuppliterator<IMagicStatus<?>> getMagicStatusList();

	public IMagicExecutor getMagicExecutor(World world, EntityPlayer player, ItemStack itemStack);

}
