package miragefairy2019.mod.modules.fairyweapon;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import miragefairy2019.mod.api.ComponentFairyAbilityType;
import miragefairy2019.mod.api.fairy.FairyType;
import miragefairy2019.mod.api.fairy.IItemFairy;
import miragefairy2019.mod.lib.component.Composite;
import miragefairy2019.mod.lib.component.ICompositeProvider;
import miragefairy2019.mod.modules.sphere.EnumSphere;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreIngredient;

public abstract class ItemFairyWeaponBase extends Item implements ISphereReplacementItem, ICompositeProvider, ICombiningItem
{

	protected Composite composite;

	public ItemFairyWeaponBase(Composite composite)
	{
		this.composite = composite;
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

	@Override
	public boolean canRepair(ItemStack itemStack)
	{
		return true;
	}

	@Override
	public ItemStack getRepairedItem(ItemStack itemStack)
	{
		itemStack = itemStack.copy();
		itemStack.setItemDamage(0);
		return itemStack;
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
		return findItem(player, itemStack -> getFairy(itemStack).isPresent())
			.map(itemStack -> Tuple.of(itemStack, getFairy(itemStack).get()));
	}

	protected Optional<ItemStack> findItem(EntityPlayer player, Predicate<ItemStack> predicate)
	{
		ItemStack itemStack;

		itemStack = player.getHeldItem(EnumHand.OFF_HAND);
		if (predicate.test(itemStack)) return Optional.of(itemStack);

		itemStack = player.getHeldItem(EnumHand.MAIN_HAND);
		if (predicate.test(itemStack)) return Optional.of(itemStack);

		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {

			itemStack = player.inventory.getStackInSlot(i);
			if (predicate.test(itemStack)) return Optional.of(itemStack);

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

	protected boolean breakBlock(World world, EntityPlayer player, EnumFacing facing, ItemStack itemStack, BlockPos blockPos, int fortune, boolean collection)
	{
		if (!world.isBlockModifiable(player, blockPos)) return false;
		if (!player.canPlayerEdit(blockPos, facing, itemStack)) return false;

		IBlockState blockState = world.getBlockState(blockPos);
		Block block = blockState.getBlock();
		block.dropBlockAsItem(world, blockPos, blockState, fortune);
		world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 3);
		if (collection) {
			for (EntityItem entityItem : world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(blockPos))) {
				entityItem.setPosition(player.posX, player.posY, player.posZ);
				entityItem.setNoPickupDelay();
			}
		}

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

	//

	@Override
	public final NonNullList<Ingredient> getRepaitmentSpheres(ItemStack itemStack)
	{
		return getComposite(itemStack).components.suppliterator()
			.filter(e -> e.x instanceof ComponentFairyAbilityType)
			.map(e -> Tuple.of((ComponentFairyAbilityType) e.x, e.y))
			.mapIfPresent(e -> EnumSphere.of(e.x.abilityType).map(s -> Tuple.of(s, e.y)))
			.flatMap(e -> {
				long amount = e.y;
				int count = (int) (amount / 1_000_000_000L) + amount % 1_000_000_000L != 0 ? 1 : 0;
				return ISuppliterator.range(count)
					.map(i -> new OreIngredient(e.x.getOreName()));
			})
			.toCollection(NonNullList::create);
	}

	//

	@Override
	public Composite getComposite(ItemStack itemStack)
	{
		return composite;
	}

	//

	@Override
	public boolean canCombine(ItemStack itemStack)
	{
		return true;
	}

	@Override
	public boolean canCombineWith(ItemStack itemStack, ItemStack itemStackPart)
	{
		return itemStackPart.getItem() instanceof IItemFairy;
	}

	@Override
	public boolean canUncombine(ItemStack itemStack)
	{
		return !getCombinedFairy(itemStack).isEmpty();
	}

	@Override
	public ItemStack getCombinedPart(ItemStack itemStack)
	{
		return getCombinedFairy(itemStack);
	}

	@Override
	public void setCombinedPart(ItemStack itemStack, ItemStack itemStackPart)
	{
		setCombinedFairy(itemStack, itemStackPart);
	}

}
