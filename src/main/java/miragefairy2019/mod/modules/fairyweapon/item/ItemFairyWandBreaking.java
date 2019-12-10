package miragefairy2019.mod.modules.fairyweapon.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.Components;
import miragefairy2019.mod.modules.fairy.ModuleFairy;
import miragefairy2019.mod.modules.ore.ModuleOre;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
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

		// 指定の妖精を持っていない場合、抜ける
		a:
		{
			ItemStack itemStackFairy = getCombinedFairy(player.getHeldItem(hand));
			if (getFairy(itemStackFairy).isPresent()) {
				if (getFairy(itemStackFairy).get().modid.equals(ModuleFairy.FairyTypes.mina[0].type.modid)) {
					if (getFairy(itemStackFairy).get().name.equals(ModuleFairy.FairyTypes.mina[0].type.name)) {
						break a;
					}
				}
			}
			return EnumActionResult.PASS;
		}

		if (!world.isRemote) {

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
