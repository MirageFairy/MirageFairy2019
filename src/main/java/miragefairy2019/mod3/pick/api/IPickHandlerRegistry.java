package miragefairy2019.mod3.pick.api;

import net.minecraft.block.Block;

import javax.annotation.Nullable;

public interface IPickHandlerRegistry {

    public void register(Block block, IPickHandler pickHandler);

    @Nullable
    public IPickHandler get(Block block);

}
