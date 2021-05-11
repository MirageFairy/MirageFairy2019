package miragefairy2019.mod.api.pickable;

import java.util.Optional;

import net.minecraft.block.Block;

public interface IPickableRegistry
{

	public void register(Block block, IPickable pickable);

	public Optional<IPickable> get(Block block);

}
