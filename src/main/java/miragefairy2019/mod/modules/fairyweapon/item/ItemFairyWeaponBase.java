package miragefairy2019.mod.modules.fairyweapon.item;

import com.google.common.base.Predicate;
import miragefairy2019.mod.api.fairy.IItemFairy;
import miragefairy2019.mod.api.fairyweapon.formula.ApiFormula;
import miragefairy2019.mod.api.fairyweapon.formula.IFormula;
import miragefairy2019.mod.api.fairyweapon.formula.IMagicStatus;
import miragefairy2019.mod.api.fairyweapon.item.IItemFairyWeapon;
import miragefairy2019.mod3.fairy.api.IFairyType;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static net.minecraft.util.text.TextFormatting.BLUE;

public class ItemFairyWeaponBase extends ItemFairyWeaponBaseBase implements IItemFairyWeapon {

    public static double getFairyAttribute(String attributeName, ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) return 0;
        NBTTagCompound nbt = itemStack.getTagCompound();
        if (!nbt.hasKey("Fairy", NBT.TAG_COMPOUND)) return 0;
        NBTTagCompound fairy = nbt.getCompoundTag("Fairy");
        if (!fairy.hasKey(attributeName, NBT.TAG_DOUBLE)) return 0;
        return fairy.getDouble(attributeName);
    }

    public static void setFairyAttribute(String attributeName, ItemStack itemStack, double value) {
        if (!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
        NBTTagCompound nbt = itemStack.getTagCompound();
        if (!nbt.hasKey("Fairy", NBT.TAG_COMPOUND)) nbt.setTag("Fairy", new NBTTagCompound());
        NBTTagCompound fairy = nbt.getCompoundTag("Fairy");
        fairy.setDouble(attributeName, value);
        itemStack.setTagCompound(nbt);
    }

    @Nonnull
    public static ItemStack getCombinedFairy(@Nonnull ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) return ItemStack.EMPTY;
        NBTTagCompound nbt = itemStack.getTagCompound();
        if (!nbt.hasKey("Fairy", NBT.TAG_COMPOUND)) return ItemStack.EMPTY;
        NBTTagCompound fairy = nbt.getCompoundTag("Fairy");
        if (!fairy.hasKey("CombinedFairy", NBT.TAG_COMPOUND)) return ItemStack.EMPTY;
        return new ItemStack(fairy.getCompoundTag("CombinedFairy"));
    }

    public static void setCombinedFairy(ItemStack itemStack, ItemStack itemStackFairy) {
        if (!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
        NBTTagCompound nbt = itemStack.getTagCompound();
        if (!nbt.hasKey("Fairy", NBT.TAG_COMPOUND)) nbt.setTag("Fairy", new NBTTagCompound());
        NBTTagCompound fairy = nbt.getCompoundTag("Fairy");
        fairy.setTag("CombinedFairy", itemStackFairy.copy().splitStack(1).writeToNBT(new NBTTagCompound()));
        itemStack.setTagCompound(nbt);
    }

    @Nonnull
    public static Optional<Tuple<ItemStack, IFairyType>> findFairy(ItemStack itemStack, EntityPlayer player) {

        // 搭乗中の妖精を優先
        {
            ItemStack itemStackFairy = getCombinedFairy(itemStack);
            if (getFairy(itemStackFairy).isPresent()) {
                return Optional.of(Tuple.of(itemStackFairy, getFairy(itemStackFairy).get()));
            }
        }

        return findItemOptional(player, itemStackFairy -> getFairy(itemStackFairy).isPresent())
            .map(itemStackFairy -> Tuple.of(itemStackFairy, getFairy(itemStackFairy).get()));
    }

    @Nonnull
    public static Optional<ItemStack> findItemOptional(EntityPlayer player, Predicate<ItemStack> predicate) {
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

    @Nonnull
    public static Optional<IFairyType> getFairy(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (!(item instanceof IItemFairy)) return Optional.empty();
        return ((IItemFairy) item).getMirageFairy2019Fairy(itemStack);
    }

    @Nonnull
    public static Vec3d getSight(EntityPlayer player, double distance) {
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

    public static boolean breakBlock(World world, EntityPlayer player, EnumFacing facing, ItemStack itemStack, BlockPos blockPos, int fortune, boolean collection) {
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

    @Nullable
    public static RayTraceResult rayTrace(World world, EntityPlayer player, boolean useLiquids, double additionalReach) {
        return rayTrace(world, player, useLiquids, additionalReach, Entity.class, e -> true);
    }

    @Nullable
    public static <E extends Entity> RayTraceResult rayTrace(
        World world,
        EntityPlayer player,
        boolean useLiquids,
        double additionalReach,
        Class<? extends E> classEntity,
        Predicate<? super E> filterEntity) {
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

        // ブロックのレイトレース
        RayTraceResult rayTraceResultBlock = world.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);
        double squareDistanceBlock = rayTraceResultBlock != null
            ? vec3d.squareDistanceTo(rayTraceResultBlock.hitVec)
            : 0;

        // エンティティのレイトレース
        RayTraceResult rayTraceResultEntity = null;
        double squareDistanceEntity = 0;
        {
            List<E> entities = world.getEntitiesWithinAABB(classEntity, new AxisAlignedBB(
                vec3d.x,
                vec3d.y,
                vec3d.z,
                vec3d1.x,
                vec3d1.y,
                vec3d1.z), filterEntity);

            Tuple<Double, RayTraceResult> nTuple = ISuppliterator.ofIterable(entities)
                .mapIfPresent(entity -> {
                    if (entity == player) return Optional.empty();
                    AxisAlignedBB aabb = entity.getEntityBoundingBox();
                    RayTraceResult rayTraceResult = aabb.calculateIntercept(vec3d, vec3d1);
                    if (rayTraceResult == null) return Optional.empty();
                    return Optional.of(Tuple.of(vec3d.squareDistanceTo(rayTraceResult.hitVec), new RayTraceResult(entity, rayTraceResult.hitVec)));
                })
                .min((a, b) -> a.x.compareTo(b.x)).orElse(null);
            if (nTuple != null) {
                rayTraceResultEntity = nTuple.y;
                squareDistanceEntity = nTuple.x;
            }
        }

        if (rayTraceResultBlock != null && rayTraceResultEntity != null) {
            if (squareDistanceBlock < squareDistanceEntity) {
                return rayTraceResultBlock;
            } else {
                return rayTraceResultEntity;
            }
        } else if (rayTraceResultBlock != null) {
            return rayTraceResultBlock;
        } else if (rayTraceResultEntity != null) {
            return rayTraceResultEntity;
        } else {
            return null;
        }
    }

    public static void spawnParticle(World world, Vec3d sight, int color) {
        world.spawnParticle(
            EnumParticleTypes.SPELL_MOB,
            sight.x,
            sight.y,
            sight.z,
            ((color >> 16) & 0xFF) / 255.0,
            ((color >> 8) & 0xFF) / 255.0,
            ((color >> 0) & 0xFF) / 255.0);
    }

    //////////////////// 妖精魔法ステータス関連

    private List<IMagicStatus<?>> magicStatuses = new ArrayList<>();

    protected <T> IMagicStatus<T> registerMagicStatus(String name, Function<T, ITextComponent> formatter, IFormula<T> formula) {
        IMagicStatus<T> magicStatus = ApiFormula.createMagicStatus(name, formatter, formula);
        magicStatuses.add(magicStatus);
        return magicStatus;
    }

    protected ISuppliterator<IMagicStatus<?>> getMagicStatuses() {
        return ISuppliterator.ofIterable(magicStatuses);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformationMagicStatuses(ItemStack itemStackFairyWeapon, ItemStack itemStackFairy, IFairyType fairyType, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        for (IMagicStatus<?> magicStatus : getMagicStatuses()) {
            tooltip.add(magicStatus.getDisplayString(fairyType)
                .setStyle(new Style().setColor(BLUE))
                .getFormattedText());
        }
    }

}
