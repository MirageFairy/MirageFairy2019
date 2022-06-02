package miragefairy2019.mod.fairyweapon.items;

import kotlin.Pair;
import miragefairy2019.api.Erg;
import miragefairy2019.api.IFairyType;
import miragefairy2019.lib.ErgKt;
import miragefairy2019.mod.fairyweapon.FairyWeaponUtils;
import miragefairy2019.mod.Main;
import miragefairy2019.mod.fairyweapon.UtilKt;
import mirrg.boron.util.struct.Tuple;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

import static miragefairy2019.mod.fairyweapon.FairyWeaponUtils.getEntities;
import static miragefairy2019.mod.fairyweapon.FairyWeaponUtils.spawnParticleSphericalRange;
import static miragefairy2019.mod.fairyweapon.FairyWeaponUtils.spawnParticleTargets;

public class ItemMagicWandCollecting extends ItemFairyWeapon {

    protected static class Status {

        public final double additionalReach;
        public final double radius;
        public final int maxTargets;
        public final double wear;
        public final double coolTime;

        public Status(IFairyType fairyType) {
            additionalReach = Math.min(fairyType.getManaSet().wind / 5.0, 8);
            radius = Math.min(2 + fairyType.getManaSet().fire / 10.0 + fairyType.getErgSet().getValue(Erg.WARP) / 10.0, 7);
            maxTargets = (int) (Math.min(1 + fairyType.getManaSet().gaia / 2.0 + fairyType.getErgSet().getValue(Erg.SPACE) / 2.0, 20));
            wear = 0.25 * Math.pow(0.5, fairyType.getManaSet().aqua / 30);
            coolTime = fairyType.getCost() * 3 * Math.pow(0.5, fairyType.getManaSet().dark / 40);
        }

    }

    @Nullable
    @Override
    public String getMagicDescription(@NotNull ItemStack itemStack) {
        return "右クリックでアイテムを回収"; // TODO translate Right click to cut whole tree
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformationFairyWeapon(ItemStack itemStackFairyWeapon, ItemStack itemStackFairy, IFairyType fairyType, @Nullable World world, NonNullList<String> tooltip, ITooltipFlag flag) {
        Status status = new Status(fairyType);
        tooltip.add(TextFormatting.BLUE + "Additional Reach: " + String.format("%.1f", status.additionalReach) + " (Wind)");
        tooltip.add(TextFormatting.BLUE + "Radius: " + String.format("%.1f", status.radius) + " (Fire, " + ErgKt.getDisplayName(Erg.WARP).getUnformattedText() + ")");
        tooltip.add(TextFormatting.BLUE + "Max Targets: " + status.maxTargets + " (Gaia, " + ErgKt.getDisplayName(Erg.SPACE).getUnformattedText() + ")");
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
        Pair<ItemStack, IFairyType> fairy = UtilKt.findFairy(itemStack, player);
        if (fairy == null) {
            return new Result(EnumExecutability.NO_FAIRY, FairyWeaponUtils.getSight(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue()));
        }

        // ステータスを評価
        Status status = new Status(fairy.getSecond());

        // 発動座標
        RayTraceResult rayTraceResult = FairyWeaponUtils.rayTrace(world, player, false, status.additionalReach);
        Vec3d positionTarget = rayTraceResult != null
            ? rayTraceResult.hitVec
            : FairyWeaponUtils.getSight(player, player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue() + status.additionalReach);

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
        if (!Main.side.isClient()) return;

        //

        // 判定
        Result result = getExecutability(world, itemStack, player);

        // 発動中心点にパーティクルを表示
        FairyWeaponUtils.spawnParticle(
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

}
