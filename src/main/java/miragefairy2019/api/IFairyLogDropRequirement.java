package miragefairy2019.api;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public interface IFairyLogDropRequirement {

    public boolean test(@Nonnull World world, @Nonnull BlockPos blockPos);

    @Nonnull
    public ITextComponent getDescription();

}
