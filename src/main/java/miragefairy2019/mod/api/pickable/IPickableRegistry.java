package miragefairy2019.mod.api.pickable;

import net.minecraft.block.Block;

import java.util.Optional;

public interface IPickableRegistry {

    public void register(Block block, IPickable pickable);

    public Optional<IPickable> get(Block block);

}
