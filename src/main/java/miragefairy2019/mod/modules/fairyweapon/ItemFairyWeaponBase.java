package miragefairy2019.mod.modules.fairyweapon;

import java.util.List;
import java.util.Optional;

import miragefairy2019.mod.api.fairy.FairyType;
import miragefairy2019.mod.api.fairy.IItemFairy;
import mirrg.boron.util.struct.Tuple;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
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

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag)
	{

		// 妖精魔法ステータス
		Tuple<ItemStack, FairyType> fairy = Optional.ofNullable(Minecraft.getMinecraft().player).flatMap(p -> findFairy(p)).orElse(null);
		if (fairy != null) {
			addInformationFairyWeapon(itemStack, fairy.x, fairy.y, world, tooltip, flag);
		} else {
			tooltip.add(TextFormatting.BLUE + "No fairy is supplied");
		}

	}

	@SideOnly(Side.CLIENT)
	public void addInformationFairyWeapon(ItemStack itemStackFairyWeapon, ItemStack itemStackFairy, FairyType fairyType, World world, List<String> tooltip, ITooltipFlag flag)
	{
		tooltip.add(TextFormatting.BLUE + "Fairy: " + itemStackFairy.getDisplayName());
	}

	//

	protected float destroySpeed = 1;

	public float getDestroySpeed(ItemStack stack, IBlockState state)
	{
		for (String type : getToolClasses(stack)) {
			if (state.getBlock().isToolEffective(type, state)) {
				return destroySpeed;
			}
		}
		return 1;
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

	protected Optional<Tuple<ItemStack, FairyType>> findFairy(EntityPlayer player)
	{
		Optional<FairyType> oFairy;
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

	protected Optional<FairyType> getFairy(ItemStack itemStack)
	{
		Item item = itemStack.getItem();
		if (!(item instanceof IItemFairy)) return Optional.empty();
		return ((IItemFairy) item).getMirageFairy2019Fairy(itemStack);
	}

	protected Vec3d getSight(EntityPlayer player, double distance)
	{
		float rotationPitch = player.rotationPitch;
		float rotationYaw = player.rotationYaw;
		double x = player.posX;
		double y = player.posY + (double) player.getEyeHeight();
		double z = player.posZ;
		Vec3d vec1 = new Vec3d(x, y, z);
		float f2 = MathHelper.cos(-rotationYaw * 0.017453292F - (float) Math.PI);
		float f3 = MathHelper.sin(-rotationYaw * 0.017453292F - (float) Math.PI);
		float f4 = -MathHelper.cos(-rotationPitch * 0.017453292F);
		float f5 = MathHelper.sin(-rotationPitch * 0.017453292F);
		float f6 = f3 * f4;
		float f7 = f2 * f4;
		Vec3d vec2 = vec1.addVector((double) f6 * distance, (double) f5 * distance, (double) f7 * distance);
		return vec2;
	}

	protected boolean breakBlock(World world, EntityPlayer player, EnumFacing facing, ItemStack itemStack, BlockPos blockPos, int fortune)
	{
		if (!world.isBlockModifiable(player, blockPos)) return false;
		if (!player.canPlayerEdit(blockPos, facing, itemStack)) return false;

		IBlockState blockState = world.getBlockState(blockPos);
		Block block = blockState.getBlock();
		block.dropBlockAsItem(world, blockPos, blockState, fortune);
		world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 3);

		return true;
	}

	protected RayTraceResult rayTrace(World world, EntityPlayer player, boolean useLiquids, double additionalReach)
	{
		float rotationPitch = player.rotationPitch;
		float rotationYaw = player.rotationYaw;
		double x = player.posX;
		double y = player.posY + (double) player.getEyeHeight();
		double z = player.posZ;
		Vec3d vec3d = new Vec3d(x, y, z);
		float f2 = MathHelper.cos(-rotationYaw * 0.017453292F - (float) Math.PI);
		float f3 = MathHelper.sin(-rotationYaw * 0.017453292F - (float) Math.PI);
		float f4 = -MathHelper.cos(-rotationPitch * 0.017453292F);
		float f5 = MathHelper.sin(-rotationPitch * 0.017453292F);
		float f6 = f3 * f4;
		float f7 = f2 * f4;
		double d3 = player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() + additionalReach;
		Vec3d vec3d1 = vec3d.addVector((double) f6 * d3, (double) f5 * d3, (double) f7 * d3);
		return world.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);
	}

	protected void spawnParticle(World world, Vec3d sight, int color)
	{
		world.spawnParticle(
			EnumParticleTypes.SPELL_MOB,
			sight.x,
			sight.y,
			sight.z,
			((color >> 16) & 0xFF) / 255.0,
			((color >> 8) & 0xFF) / 255.0,
			((color >> 0) & 0xFF) / 255.0);
	}

}
