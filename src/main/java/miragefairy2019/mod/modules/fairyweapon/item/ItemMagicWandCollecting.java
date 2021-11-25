package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod3.erg.ErgKt;
import miragefairy2019.mod3.erg.api.ErgTypes;
import miragefairy2019.mod3.fairy.api.IFairyType;
import miragefairy2019.mod3.main.api.ApiMain;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ItemMagicWandCollecting extends ItemFairyWeaponBase {

    protected static class Status {

        public final double additionalReach;
        public final double radius;
        public final int maxTargets;
        public final double wear;
        public final double coolTime;

        public Status(IFairyType fairyType) {
            additionalReach = Math.min(fairyType.getManaSet().getWind() / 5.0, 8);
            radius = Math.min(2 + fairyType.getManaSet().getFire() / 10.0 + fairyType.getErgSet().getPower(ErgTypes.warp) / 10.0, 7);
            maxTargets = (int) (Math.min(1 + fairyType.getManaSet().getGaia() / 2.0 + fairyType.getErgSet().getPower(ErgTypes.space) / 2.0, 20));
            wear = 0.25 * Math.pow(0.5, fairyType.getManaSet().getAqua() / 30);
            coolTime = fairyType.getCost() * 3 * Math.pow(0.5, fairyType.getManaSet().getDark() / 40);
        }

    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void addInformationFunctions(ItemStack itemStack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {

        tooltip.add(TextFormatting.RED + "Right click to use magic");

        super.addInformationFunctions(itemStack, world, tooltip, flag);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformationFairyWeapon(ItemStack itemStackFairyWeapon, ItemStack itemStackFairy, IFairyType fairyType, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        Status status = new Status(fairyType);
        tooltip.add(TextFormatting.BLUE + "Additional Reach: " + String.format("%.1f", status.additionalReach) + " (Wind)");
        tooltip.add(TextFormatting.BLUE + "Radius: " + String.format("%.1f", status.radius) + " (Fire, " + ErgKt.getDisplayName(ErgTypes.warp).getUnformattedText() + ")");
        tooltip.add(TextFormatting.BLUE + "Max Targets: " + status.maxTargets + " (Gaia, " + ErgKt.getDisplayName(ErgTypes.space).getUnformattedText() + ")");
        tooltip.add(TextFormatting.BLUE + "Wear: " + String.format("%.1f", status.wear * 100) + "% (Aqua)");
        tooltip.add(TextFormatting.BLUE + "Cool Time: " + ((int) status.coolTime) + "t (Dark, Cost)");
    }

    //

    private static enum EnumExecutability {
        OK(4, 0xFFFFFF),
        COOLTIME(3, 0xFFFF00),
        NO_TARGET(2, 0x00FFFF),
        NO_FAIRY(1, 0xFF00FF),
        NO_DURABILITY(0, 0xFF0000),
        ;

        public final int health;
        public final int color;

        private EnumExecutability(int health, int color) {
            this.health = health;
            this.color = color;
        }

    }

    private static class Result {

        public final EnumExecutability executability;
        public final Vec3d positionTarget;

        public Result(
                EnumExecutability executability,
                Vec3d positionTarget) {
            this.executability = executability;
            this.positionTarget = positionTarget;
        }

    }

    private static class ResultWithFairy extends Result {

        public final Status status;
        @SuppressWarnings("unused")
        public final RayTraceResult rayTraceResult;
        public final List<EntityItem> targets;

        public ResultWithFairy(
                EnumExecutability executability,
                Vec3d positionTarget,
                Status status,
                RayTraceResult rayTraceResult,
                List<EntityItem> targets) {
            super(executability, positionTarget);
            this.status = status;
            this.rayTraceResult = rayTraceResult;
            this.targets = targets;
        }

    }

    private Result getExecutability(World world, ItemStack itemStack, EntityPlayer player) {

        // 妖精取得
        Tuple<ItemStack, IFairyType> fairy = findFairy(itemStack, player).orElse(null);
        if (fairy == null) {
            return new Result(EnumExecutability.NO_FAIRY, getSight(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue()));
        }

        // ステータスを評価
        Status status = new Status(fairy.y);

        // 発動座標
        RayTraceResult rayTraceResult = rayTrace(world, player, false, status.additionalReach);
        Vec3d positionTarget = rayTraceResult != null
                ? rayTraceResult.hitVec
                : getSight(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() + status.additionalReach);

        // 対象を取得
        List<EntityItem> entityTargets = getEntities(EntityItem.class, world, positionTarget, status.radius);

        // 実行可能性を計算
        EnumExecutability executability = itemStack.getItemDamage() >= itemStack.getMaxDamage()
                ? EnumExecutability.NO_DURABILITY
                : entityTargets.isEmpty()
                ? EnumExecutability.NO_TARGET
                : player.getCooldownTracker().hasCooldown(this)
                ? EnumExecutability.COOLTIME
                : EnumExecutability.OK;

        return new ResultWithFairy(executability, positionTarget, status, rayTraceResult, entityTargets);
    }

    //

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

        // アイテム取得
        ItemStack itemStack = player.getHeldItem(hand);

        // サーバーのみ
        if (world.isRemote) return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);

        //

        // 判定
        Result result = getExecutability(world, itemStack, player);

        // 判定がだめだったらスルー
        if (result.executability.health < EnumExecutability.OK.health) return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);
        ResultWithFairy resultWithFairy = (ResultWithFairy) result;

        // 魔法成立

        int successed = 0;
        double maxStacks2 = resultWithFairy.status.maxTargets;
        for (EntityItem entityItem : resultWithFairy.targets) {

            // 耐久が0のときは破壊をやめる
            if (itemStack.getItemDamage() >= itemStack.getMaxDamage()) break;

            // パワーが足りないので破壊をやめる
            if (maxStacks2 < 1) break;

            //消費
            if (world.rand.nextDouble() < resultWithFairy.status.wear) itemStack.damageItem(1, player);
            maxStacks2--;
            successed++;

            // 魔法を行使
            entityItem.setPosition(player.posX, player.posY, player.posZ);
            entityItem.setNoPickupDelay();

        }

        if (successed > 0) {

            // エフェクト
            world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);

            // クールタイム
            player.getCooldownTracker().setCooldown(this, (int) (resultWithFairy.status.coolTime * (1 - maxStacks2 / (double) resultWithFairy.status.maxTargets)));

        }

        return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected) {

        // プレイヤー取得
        if (!(entity instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) entity;

        // アイテム取得
        if (!isSelected && player.getHeldItemOffhand() != itemStack) return;

        // クライアントのみ
        if (!ApiMain.side().isClient()) return;

        //

        // 判定
        Result result = getExecutability(world, itemStack, player);

        // 発動中心点にパーティクルを表示
        spawnParticle(
                world,
                result.positionTarget,
                result.executability.color);

        if (result.executability.health >= EnumExecutability.NO_TARGET.health) {
            ResultWithFairy resultWithFairy = (ResultWithFairy) result;

            // 発動範囲にパーティクルを表示
            spawnParticleSphericalRange(world, result.positionTarget, resultWithFairy.status.radius);

            // 対象にパーティクルを表示
            spawnParticleTargets(
                    world,
                    resultWithFairy.targets,
                    entityTarget -> entityTarget.getDistanceSq(player) > 0.2 * 0.2,
                    entityTarget -> entityTarget.getPositionVector(),
                    resultWithFairy.status.maxTargets);

        }

    }

    //

    public static <E extends Entity> List<E> getEntities(Class<? extends E> classEntity, World world, Vec3d positionCenter, double radius) {
        return world.getEntitiesWithinAABB(classEntity, new AxisAlignedBB(
                        positionCenter.x - radius,
                        positionCenter.y - radius,
                        positionCenter.z - radius,
                        positionCenter.x + radius,
                        positionCenter.y + radius,
                        positionCenter.z + radius),
                e -> {
                    if (e.getDistanceSq(positionCenter.x, positionCenter.y, positionCenter.z) > radius * radius) return false;
                    return true;
                });
    }

    private static double rotateY = 0;

    public static void spawnParticleSphericalRange(World world, Vec3d positionCenter, double radius) {

        // 角度アニメーション更新
        rotateY += 7.4 / 180.0 * Math.PI;
        if (rotateY > 2 * Math.PI) rotateY -= 2 * Math.PI;

        for (int i = 0; i < 8; i++) {

            // 横角度
            double yaw = rotateY + i * 0.25 * Math.PI;

            // 円形パーティクル生成
            a:
            for (int j = 0; j < 100; j++) {

                // パーティクル仮出現点
                double pitch = (-0.5 + Math.random()) * Math.PI;
                Vec3d offset = new Vec3d(
                        Math.cos(pitch) * Math.cos(yaw),
                        Math.sin(pitch),
                        Math.cos(pitch) * Math.sin(yaw)).scale(radius);
                Vec3d positionParticle = positionCenter.add(offset);

                // 仮出現点が、真下がブロックな空洞だった場合のみ受理
                if (!world.getBlockState(new BlockPos(positionParticle)).isFullBlock()) {
                    if (world.getBlockState(new BlockPos(positionParticle).down()).isFullBlock()) {

                        // パーティクル出現点2
                        // 高さを地面にくっつけるために、高さを地面の高さに固定した状態で横位置を調整する
                        double y = Math.floor(positionParticle.y) + 0.15;
                        double offsetY = y - positionCenter.y;
                        double r1 = Math.sqrt(offset.x * offset.x + offset.z * offset.z);
                        if (Double.isNaN(r1)) break a;
                        double r2 = Math.sqrt(radius * radius - offsetY * offsetY);
                        if (Double.isNaN(r2)) break a;
                        double offsetX = offset.x / r1 * r2;
                        double offsetZ = offset.z / r1 * r2;

                        // パーティクル生成
                        world.spawnParticle(
                                EnumParticleTypes.END_ROD,
                                positionCenter.x + offsetX,
                                Math.floor(positionParticle.y) + 0.15,
                                positionCenter.z + offsetZ,
                                0,
                                -0.08,
                                0);

                        break a;
                    }
                }

            }

        }

    }

    public static <T> void spawnParticleTargets(World world, Iterable<? extends T> targets, Predicate<? super T> filter, Function<? super T, ? extends Vec3d> fPosition, int maxCount) {
        List<? extends T> listTargets = ISuppliterator.ofIterable(targets)
                .toList();
        double rate = 5 / (double) Math.max(listTargets.size(), 5);
        for (T target : listTargets) {

            int color;
            if (filter.test(target)) {
                if (maxCount > 0) {
                    maxCount--;
                    color = 0xFFFFFF;
                } else {
                    color = 0x00FFFF;
                }
            } else {
                color = 0xFF0000;
            }

            if (Math.random() < 0.2 * rate) {
                Vec3d position = fPosition.apply(target);
                world.spawnParticle(
                        EnumParticleTypes.SPELL_MOB,
                        position.x,
                        position.y,
                        position.z,
                        ((color >> 16) & 0xFF) / 255.0,
                        ((color >> 8) & 0xFF) / 255.0,
                        ((color >> 0) & 0xFF) / 255.0);
            }

        }
    }

}
