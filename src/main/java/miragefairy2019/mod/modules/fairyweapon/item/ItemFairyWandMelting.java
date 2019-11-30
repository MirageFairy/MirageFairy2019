package miragefairy2019.mod.modules.fairyweapon.item;

import java.util.ArrayList;
import java.util.List;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.Components;
import miragefairy2019.mod.modules.fairy.FairyRegistry;
import miragefairy2019.mod.modules.fairy.ModuleFairy;
import miragefairy2019.mod.modules.mirageflower.ModuleMirageFlower;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemFairyWandMelting extends ItemFairyWeaponCraftingToolBase
{

	public ItemFairyWandMelting()
	{
		addComponent(Components.MIRAGIUM, 1);
		addComponent(Components.fairyAbilityType(EnumAbilityType.flame));
		setMaxDamage(32 - 1);
		setDescription("金属を溶かすほどの情熱");
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

			if (player.isSneaking()) {

				List<String> lines = new ArrayList<>();
				lines.add("===== Mirage Flower Grow Rate Table =====");
				FairyRegistry.getKeyItemStacks()
					.flatMap(i -> FairyRegistry.getFairies(i))
					.distinct()
					.map(f -> Tuple.of(f, (f.type.manaSet.shine * f.type.abilitySet.get(EnumAbilityType.crystal) / 100.0 * 3) * 100))
					.sortedDouble(Tuple::getY)
					.map(t -> String.format("%s: %.2f%%", t.x.type.getLocalizedName(), t.y))
					.forEach(t -> lines.add(t));
				lines.add("====================");
				player.sendStatusMessage(new TextComponentString(ISuppliterator.ofIterable(lines)
					.join("\n")), false);

			} else {

				BlockPos pos2 = pos.offset(facing);

				List<String> lines = new ArrayList<>();
				lines.add("===== Mirage Flower Grow Rate =====");
				lines.add(String.format("Pos: %d %d %d", pos2.getX(), pos2.getY(), pos2.getZ()));
				lines.add(String.format("Block: %s", world.getBlockState(pos2)));
				lines.add(String.format("Floor: %s", world.getBlockState(pos2.down())));
				lines.add(String.format("%.2f%%", ModuleMirageFlower.blockMirageFlower.getGrowRate(world, pos2) * 100));
				lines.add("====================");
				player.sendStatusMessage(new TextComponentString(ISuppliterator.ofIterable(lines)
					.join("\n")), false);

			}

		}

		return EnumActionResult.SUCCESS;
	}

}
