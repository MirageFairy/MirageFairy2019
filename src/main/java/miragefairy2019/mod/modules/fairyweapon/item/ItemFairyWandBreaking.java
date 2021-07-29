package miragefairy2019.mod.modules.fairyweapon.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.modules.oreseed.ModuleOreSeed;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemFairyWandBreaking extends ItemFairyWeaponCraftingTool
{

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{

		// 指定の妖精を持っていない場合、抜ける
		a:
		{
			ItemStack itemStackFairy = getCombinedFairy(player.getHeldItem(hand));
			if (getFairy(itemStackFairy).isPresent()) {
				if (getFairy(itemStackFairy).get().getName().equals(new ResourceLocation(ModMirageFairy2019.MODID, "mina"))) {
					break a;
				}
			}
			return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
		}

		if (!world.isRemote) {

			Map<IBlockState, Integer> map = new HashMap<>();
			int x = Math.floorDiv(pos.getX(), 16);
			int z = Math.floorDiv(pos.getZ(), 16);

			for (int xi = 0; xi < 16; xi++) {
				for (int zi = 0; zi < 16; zi++) {
					for (int yi = 0; yi < 256; yi++) {
						IBlockState blockState = world.getBlockState(new BlockPos(x + xi, yi, z + zi));
						if (blockState.getBlock().equals(ModuleOreSeed.blockOreSeed)) {
							map.put(blockState, map.getOrDefault(blockState, 0) + 1);
						}
					}
				}
			}

			// 鉱石生成確率表示
			List<String> lines = new ArrayList<>();
			lines.add("===== Ore Seed =====");
			lines.add("------");
			map.entrySet().stream().forEach(e -> {
				lines.add(e.getKey() + ": " + e.getValue());
			});
			lines.add("====================");
			player.sendStatusMessage(new TextComponentString(ISuppliterator.ofIterable(lines)
				.join("\n")), false);

		}

		return EnumActionResult.SUCCESS;
	}

}
