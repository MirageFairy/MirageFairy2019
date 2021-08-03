package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.ApiMirageFlower;
import miragefairy2019.mod.api.fairy.IFairyType;
import miragefairy2019.mod.api.magic.IMagicExecutor;
import miragefairy2019.mod.api.magic.IMagicHandler;
import miragefairy2019.mod.api.magic.IMagicStatus;
import miragefairy2019.mod.api.pickable.IPickable;
import miragefairy2019.mod.common.magic.MagicSelectorCircle;
import miragefairy2019.mod.common.magic.MagicSelectorPosition;
import miragefairy2019.mod.common.magic.MagicSelectorRayTrace;
import miragefairy2019.mod.common.magic.MagicStatusHelper;
import miragefairy2019.modkt.api.playeraura.IPlayerAuraHandler;
import mirrg.boron.util.UtilsMath;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static miragefairy2019.mod.modules.fairy.EnumAbilityType.*;
import static miragefairy2019.mod.modules.fairy.EnumManaType.*;

public class ItemBellFlowerPicking extends ItemFairyWeaponBase3 {

    private double maxTargetCountFactor;
    private double fortuneFactor;
    private double radiusFactor;

    public ItemBellFlowerPicking(double maxTargetCountFactor, double fortuneFactor, double radiusFactor) {
        this.maxTargetCountFactor = maxTargetCountFactor;
        this.fortuneFactor = fortuneFactor;
        this.radiusFactor = radiusFactor;
    }

    @Override
    public IMagicHandler getMagicHandler(IPlayerAuraHandler playerAura, IFairyType fairyType) {
        IMagicStatus<Double> pitch = MagicStatusHelper.getMagicStatusPitch(
                () -> -(fairyType.getCost() / 50.0 - 1) * 12,
                f -> new TextComponentString("").appendSibling(f.cost()),
                -12, 0, 12);
        IMagicStatus<Integer> maxTargetCount = MagicStatusHelper.getMagicStatusMaxTargetCount(
                () -> (int) Math.floor(2 + (fairyType.getManas().getDark() + playerAura.getPlayerAura().getMana(dark)) * maxTargetCountFactor + fairyType.getAbilities().getAbilityPower(fell) * 0.1),
                f -> new TextComponentString("")
                        .appendText("2")
                        .appendText("+")
                        .appendSibling(f.mana(dark)).appendText("*" + String.format("%.2f", maxTargetCountFactor))
                        .appendText("+")
                        .appendSibling(f.ability(fell)).appendText("*0.1"),
                2, 10000);
        IMagicStatus<Double> fortune = MagicStatusHelper.getMagicStatusFortune(
                () -> 3 + (fairyType.getManas().getShine() + playerAura.getPlayerAura().getMana(shine)) * fortuneFactor + fairyType.getAbilities().getAbilityPower(knowledge) * 0.1,
                f -> new TextComponentString("")
                        .appendText("3")
                        .appendText("+")
                        .appendSibling(f.mana(shine)).appendText("*" + String.format("%.2f", fortuneFactor))
                        .appendText("+")
                        .appendSibling(f.ability(knowledge)).appendText("*0.1"),
                3, 10000);
        IMagicStatus<Double> additionalReach = MagicStatusHelper.getMagicStatusAdditionalReach(
                () -> 0 + (fairyType.getManas().getWind() + playerAura.getPlayerAura().getMana(wind)) * 0.1,
                f -> new TextComponentString("")
                        .appendSibling(f.mana(wind)).appendText("*0.1"),
                0, 10);
        IMagicStatus<Double> radius = MagicStatusHelper.getMagicStatusRadius(
                () -> 4 + (fairyType.getManas().getGaia() + playerAura.getPlayerAura().getMana(gaia)) * radiusFactor,
                f -> new TextComponentString("")
                        .appendText("4")
                        .appendText("+")
                        .appendSibling(f.mana(gaia)).appendText("*" + String.format("%.2f", radiusFactor)),
                4, 10);
        IMagicStatus<Double> wear = MagicStatusHelper.getMagicStatusWear(
                () -> 0.2 / (1 + (fairyType.getManas().getFire() + playerAura.getPlayerAura().getMana(fire)) * 0.03),
                f -> new TextComponentString("")
                        .appendText("0.2/(1+")
                        .appendSibling(f.mana(fire)).appendText("*0.03")
                        .appendText(")"),
                0.0001, 0.2);
        IMagicStatus<Double> coolTime = MagicStatusHelper.getMagicStatusCoolTime(
                () -> fairyType.getCost() * 0.5 / (1 + (fairyType.getManas().getAqua() + playerAura.getPlayerAura().getMana(aqua)) * 0.03),
                f -> new TextComponentString("")
                        .appendSibling(f.cost()).appendText("*0.5")
                        .appendText("/(1+")
                        .appendSibling(f.mana(aqua)).appendText("*0.03")
                        .appendText(")"),
                0.0001, 100);
        IMagicStatus<Boolean> collection = MagicStatusHelper.getMagicStatusCollection(
                () -> fairyType.getAbilities().getAbilityPower(warp) >= 10,
                f -> new TextComponentString("")
                        .appendSibling(f.ability(warp)).appendText(">=10"));

        return new IMagicHandler() {
            @Override
            public ISuppliterator<IMagicStatus<?>> getMagicStatusList() {
                return ISuppliterator.of(
                        pitch,
                        maxTargetCount,
                        fortune,
                        additionalReach,
                        radius,
                        wear,
                        coolTime,
                        collection);
            }

            @Override
            public IMagicExecutor getMagicExecutor(World world, EntityPlayer player, ItemStack itemStack) {

                // 視線判定
                MagicSelectorRayTrace magicSelectorRayTrace = new MagicSelectorRayTrace(world, player, additionalReach.get());

                // 視点判定
                MagicSelectorPosition magicSelectorPosition = magicSelectorRayTrace.getMagicSelectorPosition();

                // 妖精を持っていない場合、中止
                if (fairyType.isEmpty()) return new IMagicExecutor() {
                    @Override
                    public void onUpdate(int itemSlot, boolean isSelected) {
                        magicSelectorPosition.doEffect(0xFF00FF);
                    }
                };

                // 範囲判定
                MagicSelectorCircle magicSelectorCircle = magicSelectorPosition.getMagicSelectorCircle(radius.get());

                // 対象計算
                List<Tuple<BlockPos, IPickable>> listTarget = new ArrayList<>();
                magicSelectorCircle.getBlockPosList()
                        .mapIfPresent(e -> {
                            IBlockState blockState = world.getBlockState(e.blockPos);
                            IPickable pickable = ApiMirageFlower.pickableRegistry.get(blockState.getBlock()).orElse(null);
                            if (pickable != null) {
                                if (pickable.isPickableAge(blockState)) {
                                    return Optional.of(Tuple.of(e, pickable));
                                }
                            }
                            return Optional.empty();
                        })
                        .sortedDouble(t -> t.x.distanceSquared)
                        .limit(maxTargetCount.get())
                        .forEach(t -> {
                            listTarget.add(Tuple.of(t.x.blockPos, t.y));
                        });

                // 資源がない場合、中止
                if (itemStack.getItemDamage() + (int) Math.ceil(wear.get()) > itemStack.getMaxDamage()) return new IMagicExecutor() {
                    @Override
                    public void onUpdate(int itemSlot, boolean isSelected) {

                        // 視点
                        magicSelectorPosition.doEffect(0xFF0000);

                        // 範囲
                        magicSelectorCircle.doEffect();

                    }
                };

                // 発動対象がない場合、中止
                if (listTarget.isEmpty()) return new IMagicExecutor() {
                    @Override
                    public void onUpdate(int itemSlot, boolean isSelected) {

                        // 視点
                        magicSelectorPosition.doEffect(0x00FFFF);

                        // 範囲
                        magicSelectorCircle.doEffect();

                    }
                };

                // クールタイムが残っている場合、中止
                if (player.getCooldownTracker().hasCooldown(ItemBellFlowerPicking.this)) return new IMagicExecutor() {
                    @Override
                    public void onUpdate(int itemSlot, boolean isSelected) {

                        // 視点
                        magicSelectorPosition.doEffect(0xFFFF00);

                        // 範囲
                        magicSelectorCircle.doEffect();

                        // 対象
                        spawnParticleTargets(
                                world,
                                listTarget,
                                target -> new Vec3d(target.x).addVector(0.5, 0.5, 0.5),
                                target -> 0xFFFF00);

                    }
                };

                // 魔法成立
                return new IMagicExecutor() {
                    @Override
                    public void onUpdate(int itemSlot, boolean isSelected) {

                        // 視点
                        magicSelectorPosition.doEffect(0x00FF00);

                        // 範囲
                        magicSelectorCircle.doEffect();

                        // 対象
                        spawnParticleTargets(
                                world,
                                listTarget,
                                target -> new Vec3d(target.x).addVector(0.5, 0.5, 0.5),
                                target -> 0x00FF00);

                    }

                    @Override
                    public EnumActionResult onItemRightClick(EnumHand hand) {

                        SoundEvent breakSound = null;
                        boolean collected = false;
                        int targetCount = 0;
                        for (Tuple<BlockPos, IPickable> tuple : listTarget) {

                            // 耐久が足りないので中止
                            if (itemStack.getItemDamage() + (int) Math.ceil(wear.get()) > itemStack.getMaxDamage()) break;

                            // パワーが足りないので中止
                            if (targetCount + 1 > maxTargetCount.get()) break;

                            // 成立

                            // 資源消費
                            itemStack.damageItem(UtilsMath.randomInt(world.rand, wear.get()), player);

                            targetCount++;

                            // 音取得
                            {
                                IBlockState blockState = world.getBlockState(tuple.x);
                                breakSound = blockState.getBlock().getSoundType(blockState, world, tuple.x, player).getBreakSound();
                            }

                            // 収穫
                            {

                                // 収穫試行
                                boolean result = tuple.y.tryPick(world, tuple.x, Optional.of(player), UtilsMath.randomInt(world.rand, fortune.get()));
                                if (!result) continue;

                                // 収集
                                if (collection.get()) {

                                    // 破壊したばかりのブロックの周辺のアイテムを集める
                                    for (EntityItem entityItem : world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(tuple.x))) {
                                        collected = true;
                                        entityItem.setPosition(player.posX, player.posY, player.posZ);
                                        entityItem.setNoPickupDelay();
                                    }

                                }

                            }

                            // エフェクト
                            int color = fairyType.getColor();
                            world.spawnParticle(
                                    EnumParticleTypes.SPELL_MOB,
                                    tuple.x.getX() + 0.5,
                                    tuple.x.getY() + 0.5,
                                    tuple.x.getZ() + 0.5,
                                    ((color >> 16) & 0xFF) / 255.0,
                                    ((color >> 8) & 0xFF) / 255.0,
                                    ((color >> 0) & 0xFF) / 255.0);

                        }

                        if (targetCount >= 1) {

                            // エフェクト
                            playSound(world, player, Math.pow(2, pitch.get() / 12.0));
                            world.playSound(null, player.posX, player.posY, player.posZ, breakSound, SoundCategory.PLAYERS, 1.0F, 1.0F);

                            // クールタイム
                            double ratio = targetCount / (double) maxTargetCount.get();
                            player.getCooldownTracker().setCooldown(ItemBellFlowerPicking.this, (int) (coolTime.get() * Math.pow(ratio, 0.5)));

                        }
                        if (collected) {

                            // エフェクト
                            world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);

                        }

                        return EnumActionResult.SUCCESS;
                    }
                };
            }
        };
    }

    public static void playSound(World world, EntityPlayer player, double pitch) {
        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0F, (float) pitch);
    }

    public static <T> void spawnParticleTargets(
            World world,
            Iterable<? extends T> targets,
            Function<? super T, ? extends Vec3d> fPosition,
            Function<? super T, ? extends Integer> fColor) {
        List<? extends T> listTargets = ISuppliterator.ofIterable(targets).toList();
        double rate = 5 / (double) Math.max(listTargets.size(), 5);
        for (T target : listTargets) {

            int color = fColor.apply(target);

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
