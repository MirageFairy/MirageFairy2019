package miragefairy2019.mod.api.fairystick.contents;

import java.util.function.Predicate;
import java.util.function.Supplier;

import miragefairy2019.mod.api.fairystick.IFairyStickCraft;
import miragefairy2019.mod.api.fairystick.IFairyStickCraftCondition;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FairyStickCraftConditionReplaceBlock implements IFairyStickCraftCondition
{

	private Predicate<IBlockState> predicateInput;
	private Supplier<IBlockState> sBlockState;

	public FairyStickCraftConditionReplaceBlock(Predicate<IBlockState> predicateInput, Supplier<IBlockState> sBlockState)
	{
		this.predicateInput = predicateInput;
		this.sBlockState = sBlockState;
	}

	@Override
	public boolean test(IFairyStickCraft fairyStickCraft)
	{
		World world = fairyStickCraft.getWorld();
		BlockPos pos = fairyStickCraft.getPos();
		IBlockState blockState = sBlockState.get();

		// 設置先は指定されたブロックでなければならない
		if (!predicateInput.test(fairyStickCraft.getBlockState())) return false;

		// 設置物は召喚先に設置可能でなければならない
		if (!blockState.getBlock().canPlaceBlockAt(world, pos)) return false;

		fairyStickCraft.hookOnCraft(() -> {

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
		fairyStickCraft.hookOnUpdate(() -> {

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

}
