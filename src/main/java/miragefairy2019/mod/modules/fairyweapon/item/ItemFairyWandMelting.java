package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.Components;
import miragefairy2019.mod.modules.fairy.FairyRegistry;
import miragefairy2019.mod.modules.fairy.ModuleFairy;
import miragefairy2019.mod.modules.mirageflower.BlockMirageFlower;
import miragefairy2019.mod.modules.mirageflower.ModuleMirageFlower;
import mirrg.boron.util.struct.Tuple;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
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

				ITextComponent textComponent = new TextComponentString("");
				textComponent.appendText("===== Mirage Flower Grow Rate Table =====\n");
				FairyRegistry.getKeyItemStacks()
					.flatMap(i -> FairyRegistry.getFairies(i))
					.distinct()
					.map(f -> Tuple.of(f, BlockMirageFlower.getGrowRateInFloor(f)))
					.sortedDouble(Tuple::getY)
					.forEach(t -> {
						textComponent.appendText(String.format("%7.2f%%  ", t.y * 100));
						textComponent.appendSibling(new TextComponentTranslation(t.x.type.getUnlocalizedName()));
						textComponent.appendText("\n");
					});
				textComponent.appendText("====================");
				player.sendStatusMessage(textComponent, false);

			} else {

				BlockPos pos2 = pos.offset(facing);

				ITextComponent textComponent = new TextComponentString("");
				textComponent.appendText("===== Mirage Flower Grow Rate =====\n");
				textComponent.appendText(String.format("Pos: %d %d %d\n", pos2.getX(), pos2.getY(), pos2.getZ()));
				textComponent.appendText(String.format("Block: %s\n", world.getBlockState(pos2)));
				textComponent.appendText(String.format("Floor: %s\n", world.getBlockState(pos2.down())));
				textComponent.appendText(String.format("%.2f%%\n", ModuleMirageFlower.blockMirageFlower.getGrowRate(world, pos2) * 100));
				textComponent.appendText("====================");
				player.sendStatusMessage(textComponent, false);

			}

		}

		return EnumActionResult.SUCCESS;
	}

}
