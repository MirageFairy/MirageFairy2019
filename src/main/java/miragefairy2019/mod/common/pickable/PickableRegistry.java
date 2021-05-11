package miragefairy2019.mod.common.pickable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import miragefairy2019.mod.api.pickable.IPickable;
import miragefairy2019.mod.api.pickable.IPickableRegistry;
import net.minecraft.block.Block;

public class PickableRegistry implements IPickableRegistry
{

	private Map<Block, IPickable> map = new HashMap<>();

	@Override
	public void register(Block block, IPickable pickable)
	{
		map.put(block, pickable);
	}

	@Override
	public Optional<IPickable> get(Block block)
	{
		return Optional.ofNullable(map.get(block));
	}

}
