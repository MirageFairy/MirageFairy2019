package miragefairy2019.mod.modules.fairyweapon;

import java.util.Optional;

import miragefairy2019.mod.api.fairy.IItemMirageFairy;
import miragefairy2019.mod.api.fairy.MirageFairyType;
import mirrg.boron.util.struct.Tuple;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemFairyWeaponBase extends Item
{

	public ItemFairyWeaponBase()
	{
		setMaxStackSize(1);
	}

	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}

	@Override
	public boolean isEnchantable(ItemStack stack)
	{
		return false;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
	{
		return false;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book)
	{
		return false;
	}

	@Override
	public boolean isRepairable()
	{
		return false;
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		stack.damageItem(1, attacker);
		return true;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
	{
		if (!worldIn.isRemote && state.getBlockHardness(worldIn, pos) != 0.0) {
			stack.damageItem(1, entityLiving);
		}
		return true;
	}

	//

	protected double getFairyAttribute(String attributeName, ItemStack itemStack)
	{
		if (!itemStack.hasTagCompound()) return 0;
		NBTTagCompound nbt = itemStack.getTagCompound();
		if (!nbt.hasKey("Fairy", NBT.TAG_COMPOUND)) return 0;
		NBTTagCompound fairy = nbt.getCompoundTag("Fairy");
		if (!fairy.hasKey(attributeName, NBT.TAG_DOUBLE)) return 0;
		return fairy.getDouble(attributeName);
	}

	protected void setFairyAttribute(String attributeName, ItemStack itemStack, double value)
	{
		if (!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
		NBTTagCompound nbt = itemStack.getTagCompound();
		if (!nbt.hasKey("Fairy", NBT.TAG_COMPOUND)) nbt.setTag("Fairy", new NBTTagCompound());
		NBTTagCompound fairy = nbt.getCompoundTag("Fairy");
		fairy.setDouble(attributeName, value);
		itemStack.setTagCompound(nbt);
	}

	protected Optional<Tuple<ItemStack, MirageFairyType>> findFairy(EntityPlayer player)
	{
		Optional<MirageFairyType> oFairy;
		ItemStack itemStack;

		itemStack = player.getHeldItem(EnumHand.OFF_HAND);
		oFairy = getFairy(itemStack);
		if (oFairy.isPresent()) return Optional.of(Tuple.of(itemStack, oFairy.get()));

		itemStack = player.getHeldItem(EnumHand.MAIN_HAND);
		oFairy = getFairy(itemStack);
		if (oFairy.isPresent()) return Optional.of(Tuple.of(itemStack, oFairy.get()));

		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {

			itemStack = player.inventory.getStackInSlot(i);
			oFairy = getFairy(itemStack);
			if (oFairy.isPresent()) return Optional.of(Tuple.of(itemStack, oFairy.get()));

		}

		return Optional.empty();
	}

	protected Optional<MirageFairyType> getFairy(ItemStack itemStack)
	{
		Item item = itemStack.getItem();
		if (!(item instanceof IItemMirageFairy)) return Optional.empty();
		return ((IItemMirageFairy) item).getMirageFairy(itemStack);
	}

}
