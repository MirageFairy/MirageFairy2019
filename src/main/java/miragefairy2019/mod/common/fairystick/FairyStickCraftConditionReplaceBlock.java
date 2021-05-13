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

public class FairyStickCraftConditionReplaceBlock implements IFairyStickCraftCondition
{

	private Supplier<IBlockState> sBlockStateInput;
	private Supplier<IBlockState> sBlockStateOutput;

	public FairyStickCraftConditionReplaceBlock(Supplier<IBlockState> sBlockStateInput, Supplier<IBlockState> sBlockState)
	{
		this.sBlockStateInput = sBlockStateInput;
		this.sBlockStateOutput = sBlockState;
	}

	@Override
	public boolean test(IFairyStickCraftEnvironment environment, IFairyStickCraftExecutor executor)
	{
		World world = environment.getWorld();
		BlockPos pos = environment.getBlockPos();
		IBlockState blockState = sBlockStateOutput.get();

		// 設置先は指定されたブロックでなければならない
		if (!environment.getWorld().getBlockState(environment.getBlockPos()).equals(sBlockStateInput.get())) return false;

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
	public ISuppliterator<String> getStringsInput()
	{
		return ISuppliterator.of(sBlockStateInput.get().getBlock().getLocalizedName());
	}

	@Override
	public ISuppliterator<String> getStringsOutput()
	{
		return ISuppliterator.of(sBlockStateOutput.get().getBlock().getLocalizedName());
	}

}
