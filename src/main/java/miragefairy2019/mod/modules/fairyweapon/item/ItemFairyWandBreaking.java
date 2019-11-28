package miragefairy2019.mod.modules.fairyweapon.item;

import java.util.HashMap;
import java.util.Map;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.Components;
import miragefairy2019.mod.modules.ore.ModuleOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemFairyWandBreaking extends ItemFairyWeaponCraftingToolBase
{

	public ItemFairyWandBreaking()
	{
		addComponent(Components.MIRAGIUM, 1);
		addComponent(Components.fairyAbilityType(EnumAbilityType.breaking));
		setMaxDamage(32 - 1);
		setDescription("振ると衝撃波が迸る");
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{

		if (!world.isRemote) {
			if (player.isSneaking()) {

				Map<IBlockState, Integer> map = new HashMap<>();
				int x = Math.floorDiv(pos.getX(), 16);
				int z = Math.floorDiv(pos.getZ(), 16);

				for (int xi = 0; xi < 16; xi++) {
					for (int zi = 0; zi < 16; zi++) {
						for (int yi = 0; yi < 256; yi++) {
							IBlockState blockState = world.getBlockState(new BlockPos(x + xi, yi, z + zi));
							if (blockState.getBlock().equals(ModuleOre.blockOreSeed)) {
								map.put(blockState, map.getOrDefault(blockState, 0) + 1);
							}
						}
					}
				}

				System.out.println("------");
				map.entrySet().stream().forEach(e -> {
					System.out.println(e.getKey() + "\t" + e.getValue());
				});

			}
		}

		return EnumActionResult.SUCCESS;
	}

}
