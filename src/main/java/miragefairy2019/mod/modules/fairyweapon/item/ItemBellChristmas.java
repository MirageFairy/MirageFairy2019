package miragefairy2019.mod.modules.fairyweapon.item;

import miragefairy2019.mod.api.fairy.ApiFairy;
import miragefairy2019.mod.api.fairy.IFairyType;
import miragefairy2019.mod.api.fairyweapon.formula.IFormulaDouble;
import miragefairy2019.mod.api.fairyweapon.formula.IFormulaSelectEntry;
import miragefairy2019.mod.api.fairyweapon.formula.IMagicStatus;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.modules.fairyweapon.magic.MagicExecutor;
import miragefairy2019.mod.modules.fairyweapon.magic.SelectorEntityRanged;
import miragefairy2019.mod.modules.fairyweapon.magic.SelectorRayTrace;
import miragefairy2019.modkt.impl.fairy.AbilityType;
import mirrg.boron.util.UtilsMath;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static miragefairy2019.mod.api.fairyweapon.formula.ApiFormula.*;

public class ItemBellChristmas extends ItemBellBase {

    public IMagicStatus<Double> damage = registerMagicStatus("damage", formatterDouble1(),
            add(new IFormulaDouble[]{
                    val(1),
                    scale(dark(), 90.0, 5.0, 3),
                    scale(ability(AbilityType.Companion.getChristmas()), 20.0, 10.0),
            }));

    public IMagicStatus<Double> additionalReach = registerMagicStatus("additionalReach", formatterDouble1(),
            add(new IFormulaDouble[]{
                    val(8 - 4),
                    scale(wind(), 30.0, 6.0),
            }));

    public IMagicStatus<Double> radius = registerMagicStatus("radius", formatterDouble1(),
            add(new IFormulaDouble[]{
                    val(3),
                    scale(gaia(), 30.0, 3.0),
            }));

    public IMagicStatus<Integer> maxTargetCount = registerMagicStatus("maxTargetCount", formatterInteger(),
            round(add(new IFormulaDouble[]{
                    val(2),
                    scale(dark(), 90.0, 3.0, 3),
                    scale(ability(AbilityType.Companion.getAttack()), 10.0, 10.0 / 3.0),
            })));

    public IMagicStatus<Integer> looting = registerMagicStatus("looting", formatterInteger(),
            round(select(shine(), 0, new IFormulaSelectEntry[]{
                    entry(1, 1),
                    entry(2, 2),
                    entry(5, 3),
                    entry(10, 4),
            })));

    public IMagicStatus<Double> wear = registerMagicStatus("wear", formatterPercent0(),
            mul(new IFormulaDouble[]{
                    div(cost(), 50),
                    pow(0.5, norm(fire(), 30.0)),
                    pow(0.5, norm(aqua(), 30.0)),
            }));

    public IMagicStatus<Double> coolTime = registerMagicStatus("coolTime", formatterTick(),
            mul(new IFormulaDouble[]{
                    mul(cost(), 0.5),
                    pow(0.5, norm(dark(), 90.0, 3)),
                    pow(0.5, norm(ability(AbilityType.Companion.getSubmission()), 10.0)),
            }));

    //

    public MagicExecutor getExecutor(ItemFairyWeaponBase item, World world, ItemStack itemStack, EntityPlayer player) {

        // 妖精取得
        IFairyType fairyType = findFairy(itemStack, player).map(t -> t.y).orElseGet(ApiFairy::empty);

        // 視線判定
        SelectorRayTrace selectorRayTrace = new SelectorRayTrace(world, player, additionalReach.get(fairyType));

        // 対象判定
        SelectorEntityRanged<EntityLivingBase> selectorEntityRanged = new SelectorEntityRanged<>(
                world,
                selectorRayTrace.getPosition(),
                EntityLivingBase.class,
                e -> e != player,
                radius.get(fairyType),
                maxTargetCount.get(fairyType));

        // 実行可能性を計算
        boolean ok;
        int color;
        if (fairyType.isEmpty()) {
            ok = false;
            color = 0xFF00FF;
        } else if (itemStack.getItemDamage() + (int) Math.ceil(wear.get(fairyType)) > itemStack.getMaxDamage()) {
            ok = false;
            color = 0xFF0000;
        } else if (selectorEntityRanged.getEffectiveEntities().count() == 0) {
            ok = false;
            color = 0x00FFFF;
        } else if (player.getCooldownTracker().hasCooldown(item)) {
            ok = false;
            color = 0xFFFF00;
        } else {
            ok = true;
            color = 0xFFFFFF;
        }

        return new MagicExecutor() {
            @Override
            public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
                if (!ok) return new ActionResult<>(EnumActionResult.PASS, itemStack);

                int targetCount = 0;
                for (EntityLivingBase target : selectorEntityRanged.getEffectiveEntities()) {

                    // 耐久が足りないので中止
                    if (itemStack.getItemDamage() + (int) Math.ceil(wear.get(fairyType)) > itemStack.getMaxDamage()) break;

                    // パワーが足りないので中止
                    if (targetCount >= maxTargetCount.get(fairyType)) break;

                    // 行使
                    itemStack.damageItem(UtilsMath.randomInt(world.rand, wear.get(fairyType)), player);
                    targetCount++;
                    {
                        double damage2 = damage.get(fairyType);

                        if (target.isEntityUndead()) damage2 *= 1.5;

                        target.attackEntityFrom(new DamageSourceFairyMagic(player, looting.get(fairyType)), (float) damage2);
                    }

                }

                if (targetCount >= 1) {

                    // エフェクト
                    ItemBellBase.playSound(world, player, pitch.get(fairyType));

                    // クールタイム
                    player.getCooldownTracker().setCooldown(item, (int) (double) coolTime.get(fairyType));

                }

                return new ActionResult<>(targetCount >= 1 ? EnumActionResult.SUCCESS : EnumActionResult.PASS, itemStack);
            }

            @Override
            public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected) {
                selectorRayTrace.doEffect(color);
                selectorEntityRanged.effect();
            }
        };
    }

    //

    @Override
    @SideOnly(Side.CLIENT)
    protected void addInformationFunctions(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag) {

        super.addInformationFunctions(itemStack, world, tooltip, flag);

        tooltip.add(TextFormatting.RED + "Right click to use magic");

    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

        // アイテム取得
        ItemStack itemStack = player.getHeldItem(hand);

        return getExecutor(this, world, itemStack, player).onItemRightClick(world, player, hand);
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected) {

        // クライアントのみ
        if (!ApiMain.side().isClient()) return;

        // プレイヤー取得
        if (!(entity instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) entity;

        // アイテム取得
        if (!isSelected && player.getHeldItemOffhand() != itemStack) return;

        getExecutor(this, world, itemStack, player).onUpdate(itemStack, world, entity, itemSlot, isSelected);

    }

}
