package miragefairy2019.mod.common.fairystick;

import java.util.function.Supplier;

import miragefairy2019.mod.api.fairystick.IFairyStickCraftCondition;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftEnvironment;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftExecutor;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FairyStickCraftConditionSpawnBlock implements IFairyStickCraftCondition
{

	private Supplier<IBlockState> sBlockStateInput;

	public FairyStickCraftConditionSpawnBlock(Supplier<IBlockState> sBlockState)
	{
		this.sBlockStateInput = sBlockState;
	}

	@Override
	public boolean test(IFairyStickCraftEnvironment environment, IFairyStickCraftExecutor executor)
	{
		World world = environment.getWorld();
		BlockPos pos = environment.getBlockPos();
		IBlockState blockState = sBlockStateInput.get();

		// 設置先は空気でなければならない
		if (!environment.getWorld().getBlockState(environment.getBlockPos()).getBlock().isReplaceable(world, pos)) return false;

		// 設置物は召喚先に設置可能でなければならない
		if (!blockState.getBlock().canPlaceBlockAt(world, pos)) return false;

		executor.hookOnCraft(setterItemStackFairyStick -> {

			world.setBlockState(pos, blockState, 3);
			world.neighborChanged(pos, blockState.getBlock(), pos.up());

			world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BELL, SoundCategory.PLAYERS, 0.2F, 1.0F);
			for (int i = 0; i < 20; i++) {
				world.spawnParticle(
					EnumParticleTypes.VILLAGER_HAPPY,
					pos.getX() + world.rand.nextDouble(),
					pos.getY() + world.rand.nextDouble(),
					pos.getZ() + world.rand.nextDouble(),
					0,
					0,
					0);
			}

		});
		executor.hookOnUpdate(() -> {

			for (int i = 0; i < 3; i++) {
				world.spawnParticle(
					EnumParticleTypes.END_ROD,
					pos.getX() + world.rand.nextDouble(),
					pos.getY() + world.rand.nextDouble(),
					pos.getZ() + world.rand.nextDouble(),
					0,
					0,
					0);
			}

		});
		return true;
	}

	@Override
	public ISuppliterator<String> getStringsOutput()
	{
		return ISuppliterator.of(sBlockStateInput.get().getBlock().getLocalizedName());
	}

}
