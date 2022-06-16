package miragefairy2019.mod.fairyweapon.items;

import kotlin.Pair;
import miragefairy2019.api.Erg;
import miragefairy2019.api.IFairyType;
import miragefairy2019.lib.ErgKt;
import miragefairy2019.mod.Main;
import miragefairy2019.mod.fairyweapon.FairyWeaponUtils;
import miragefairy2019.mod.fairyweapon.ParticleUtilKt;
import miragefairy2019.mod.fairyweapon.UtilKt;
import mirrg.boron.util.UtilsMath;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static miragefairy2019.mod.fairyweapon.ParticleUtilKt.spawnParticleSphericalRange;

public class ItemOcarinaTemptation extends ItemFairyWeapon {

    protected static class Status {

        public final double radius;
        public final int maxTargetCount;
        public final double wear;
        public final double experienceCost;
        public final double coolTime;

        public Status(IFairyType fairyType) {
            radius = UtilsMath.trim(5 + fairyType.getManaSet().wind / 5.0, 5, 10);
            maxTargetCount = UtilsMath.trim(1 + (int) (fairyType.getManaSet().aqua / 7.0), 1, 8);
            wear = UtilsMath.trim(4 * Math.pow(0.5, fairyType.getManaSet().fire / 50.0), 0.4, 4);
            experienceCost = UtilsMath.trim(1 * Math.pow(0.5, fairyType.getManaSet().gaia / 50.0 + fairyType.getErgSet().getValue(Erg.LIFE) / 10.0), 0.1, 1);
            coolTime = fairyType.getCost() * UtilsMath.trim(1 * Math.pow(0.5, fairyType.getManaSet().dark / 50.0), 0.1, 1);
        }

    }

    @Nullable
    @Override
    public String getMagicDescription(@NotNull ItemStack itemStack) {
        return "Lv30以上のとき、右クリックでLv消費で村人を満腹化"; // TODO translate
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformationFairyWeapon(ItemStack itemStackFairyWeapon, ItemStack itemStackFairy, IFairyType fairyType, @Nullable World world, NonNullList<String> tooltip, ITooltipFlag flag) {
        Status status = new Status(fairyType);
        tooltip.add(TextFormatting.BLUE + "Radius: " + String.format("%.1f", status.radius) + " (Wind)");
        tooltip.add(TextFormatting.BLUE + "Max Targets: " + String.format("%d", status.maxTargetCount) + " (Aqua)");
        tooltip.add(TextFormatting.BLUE + "Wear: " + String.format("%.1f", status.wear * 100) + "% (Fire)");
        tooltip.add(TextFormatting.BLUE + "Experience Cost: " + String.format("%.1f", status.experienceCost * 100) + "% (Gaia, " + ErgKt.getDisplayName(Erg.LIFE).getUnformattedText() + ")");
        tooltip.add(TextFormatting.BLUE + "Cool Time: " + String.format("%.0f", status.coolTime) + "t (Dark, Cost)");
    }

    //

    private static enum EnumExecutability {
        OK(4, 0xFFFFFF),
        COOLTIME(3, 0xFFFF00),
        NO_TARGET(2, 0x00FFFF),
        NO_RESOURCE(1, 0xFF0000),
        NO_FAIRY(0, 0xFF00FF),
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
        public final boolean isEntity;
        public final List<Tuple<EntityVillager, Boolean>> targets;

        public ResultWithFairy(
            EnumExecutability executability,
            Vec3d positionTarget,
            Status status,
            boolean isEntity,
            List<Tuple<EntityVillager, Boolean>> targets) {
            super(executability, positionTarget);
            this.status = status;
            this.isEntity = isEntity;
            this.targets = targets;
        }

    }

    private Result getExecutability(World world, ItemStack itemStack, EntityPlayer player) {

        // 妖精取得
        Pair<ItemStack, IFairyType> fairy = UtilKt.findFairy(itemStack, player);
        if (fairy == null) {
            RayTraceResult rayTraceResult = FairyWeaponUtils.rayTrace(world, player, false, 0);
            Vec3d positionTarget = rayTraceResult != null
                ? rayTraceResult.hitVec
                : UtilKt.getSight(player, 0);
            return new Result(
                EnumExecutability.NO_FAIRY,
                positionTarget);
        }

        // ステータスを評価
        Status status = new Status(fairy.getSecond());

        // 発動対象
        RayTraceResult rayTraceResult = FairyWeaponUtils.rayTrace(world, player, false, 0, EntityVillager.class, e -> true);
        Vec3d positionTarget = rayTraceResult != null
            ? rayTraceResult.hitVec
            : UtilKt.getSight(player, 0);
        boolean isEntity;
        List<EntityVillager> targetEntities;
        if (rayTraceResult != null && rayTraceResult.typeOfHit == Type.ENTITY && rayTraceResult.entityHit instanceof EntityVillager) {
            // エンティティ

            isEntity = true;
            targetEntities = ISuppliterator.of((EntityVillager) rayTraceResult.entityHit).toList();

        } else {
            // 範囲

            isEntity = false;
            targetEntities = UtilKt.getEntities(EntityVillager.class, world, player.getPositionVector(), status.radius);

        }
        List<Tuple<EntityVillager, Boolean>> targets = ISuppliterator.ofIterable(targetEntities)
            .map(trgetEntity -> Tuple.of(trgetEntity, validate(trgetEntity)))
            .toList();

        // 実行可能性を計算
        EnumExecutability executability = itemStack.getItemDamage() + (int) Math.ceil(status.wear) > itemStack.getMaxDamage() || (!player.isCreative() && player.experienceLevel < 30)
            ? EnumExecutability.NO_RESOURCE
            : !targets.stream().anyMatch(t -> t.y)
            ? EnumExecutability.NO_TARGET
            : player.getCooldownTracker().hasCooldown(this)
            ? EnumExecutability.COOLTIME
            : EnumExecutability.OK;

        return new ResultWithFairy(executability, positionTarget, status, isEntity, targets);
    }

    private boolean validate(EntityVillager entity) {
        if (entity.getGrowingAge() < 0) return false;
        if (entity.getIsWillingToMate(false)) return false;
        return true;
    }

    //

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

        // アイテム取得
        ItemStack itemStack = player.getHeldItem(hand);

        //

        // 判定
        Result result = getExecutability(world, itemStack, player);

        // 判定がだめだったらスルー
        if (result.executability.health < EnumExecutability.OK.health) return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);
        ResultWithFairy resultWithFairy = (ResultWithFairy) result;

        // 魔法成立

        int targetCount = 0;
        boolean experienceCosted = false;
        for (Tuple<EntityVillager, Boolean> tuple : resultWithFairy.targets) {
            if (tuple.y) {

                // 耐久が足りないので中止
                if (itemStack.getItemDamage() + (int) Math.ceil(resultWithFairy.status.wear) > itemStack.getMaxDamage()) break;

                // パワーが足りないので中止
                if (targetCount >= resultWithFairy.status.maxTargetCount) break;

                // レベルが足りないので中止
                if (!player.isCreative()) if (player.experienceLevel < 30) break;

                // 行使
                {
                    int damage = UtilsMath.randomInt(world.rand, resultWithFairy.status.wear);
                    System.out.println(damage);
                    for (int i = 0; i < damage; i++) {
                        itemStack.damageItem(1, player);
                    }
                }
                targetCount++;
                if (!player.isCreative()) if (world.rand.nextDouble() < resultWithFairy.status.experienceCost) {
                    player.addExperienceLevel(-1);
                    experienceCosted = true;
                }
                world.setEntityState(tuple.x, (byte) 18);
                tuple.x.setIsWillingToMate(true);

                // エフェクト
                Random random = new Random();
                for (int i = 0; i < 5; i++) {
                    world.spawnParticle(
                        EnumParticleTypes.VILLAGER_HAPPY,
                        tuple.x.posX + (random.nextDouble() * 2 - 1) * tuple.x.width,
                        tuple.x.posY + 1 + random.nextDouble() * tuple.x.height,
                        tuple.x.posZ + (random.nextDouble() * 2 - 1) * tuple.x.width,
                        random.nextGaussian() * 0.02,
                        random.nextGaussian() * 0.02,
                        random.nextGaussian() * 0.02);
                }

            }
        }

        if (targetCount >= 1) {

            // エフェクト
            if (experienceCosted) world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.2F, 0.5F);
            world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1.0F, 1.0F);

            // クールタイム
            player.getCooldownTracker().setCooldown(this, (int) (resultWithFairy.status.coolTime * (targetCount / (double) resultWithFairy.status.maxTargetCount)));

        }

        return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected) {

        // クライアントのみ
        if (!Main.side.isClient()) return;

        // プレイヤー取得
        if (!(entity instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) entity;

        // アイテム取得
        if (!isSelected && player.getHeldItemOffhand() != itemStack) return;

        //

        // 判定
        Result result = getExecutability(world, itemStack, player);

        //

        // 発動中心点にパーティクルを表示
        ParticleUtilKt.spawnParticle(world, result.positionTarget, result.executability.color);

        if (result instanceof ResultWithFairy && result.executability.health >= EnumExecutability.NO_TARGET.health) {
            ResultWithFairy resultWithFairy = (ResultWithFairy) result;

            // 発動範囲にパーティクルを表示
            if (!resultWithFairy.isEntity) {
                spawnParticleSphericalRange(
                    world,
                    player.getPositionVector(),
                    resultWithFairy.status.radius);
            }

            // 対象にパーティクルを表示
            ParticleUtilKt.spawnParticleTargets(
                world,
                resultWithFairy.targets.stream().filter(target -> target.y).collect(Collectors.toList()),
                target -> target.x.getPositionVector(),
                e -> 0xFFFFFF);

        }

    }

}
