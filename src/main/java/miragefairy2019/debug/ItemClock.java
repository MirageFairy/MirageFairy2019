package miragefairy2019.debug;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import miragefairy2019.mod.api.ApiOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemClock extends Item
{

	public ItemClock()
	{
		this.addPropertyOverride(new ResourceLocation("time"), new IItemPropertyGetter() {
			@SideOnly(Side.CLIENT)
			double rotation;
			@SideOnly(Side.CLIENT)
			double rota;
			@SideOnly(Side.CLIENT)
			long lastUpdateTick;

			@SideOnly(Side.CLIENT)
			public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
			{
				boolean flag = entityIn != null;
				Entity entity = (Entity) (flag ? entityIn : stack.getItemFrame());

				if (worldIn == null && entity != null) {
					worldIn = entity.world;
				}

				if (worldIn == null) {
					return 0.0F;
				} else {
					double d0;

					if (worldIn.provider.isSurfaceWorld()) {
						d0 = (double) worldIn.getCelestialAngle(1.0F);
					} else {
						d0 = Math.random();
					}

					d0 = this.wobble(worldIn, d0);
					return (float) d0;
				}
			}

			@SideOnly(Side.CLIENT)
			private double wobble(World p_185087_1_, double p_185087_2_)
			{
				if (p_185087_1_.getTotalWorldTime() != this.lastUpdateTick) {
					this.lastUpdateTick = p_185087_1_.getTotalWorldTime();
					double d0 = p_185087_2_ - this.rotation;
					d0 = MathHelper.positiveModulo(d0 + 0.5D, 1.0D) - 0.5D;
					this.rota += d0 * 0.1D;
					this.rota *= 0.9D;
					this.rotation = MathHelper.positiveModulo(this.rotation + this.rota, 1.0D);
				}

				return this.rotation;
			}
		});
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		Map<IBlockState, Integer> map = new HashMap<>();
		int x = Math.floorDiv(pos.getX(), 16);
		int z = Math.floorDiv(pos.getZ(), 16);

		for (int xi = 0; xi < 16; xi++) {
			for (int zi = 0; zi < 16; zi++) {
				for (int yi = 0; yi < 256; yi++) {
					IBlockState blockState = worldIn.getBlockState(new BlockPos(x + xi, yi, z + zi));
					if (blockState.getBlock().equals(ApiOre.blockOreSeed)) {
						map.put(blockState, map.getOrDefault(blockState, 0) + 1);
					}
				}
			}
		}

		map.entrySet().stream().forEach(e -> {
			System.out.println(e.getKey() + "\t" + e.getValue());
		});

		return EnumActionResult.SUCCESS;
	}

}
