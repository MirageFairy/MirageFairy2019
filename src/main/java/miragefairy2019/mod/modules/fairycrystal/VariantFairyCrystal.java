package miragefairy2019.mod.modules.fairycrystal;

import java.util.List;
import java.util.Optional;

import miragefairy2019.mod.lib.WeightedRandom;
import miragefairy2019.mod.lib.multi.ItemVariant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VariantFairyCrystal extends ItemVariant
{

	public final String registryName;
	public final String unlocalizedName;
	public final String oreName;

	public VariantFairyCrystal(String registryName, String unlocalizedName, String oreName)
	{
		this.registryName = registryName;
		this.unlocalizedName = unlocalizedName;
		this.oreName = oreName;
	}

	public Optional<ItemStack> drop(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return ItemFairyCrystal.drop(player, world, pos, hand, facing, hitX, hitY, hitZ);
	}

	public List<WeightedRandom.Item<ItemStack>> getDropTable(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return ItemFairyCrystal.getDropTable(player, world, pos, hand, facing, hitX, hitY, hitZ);
	}

}
