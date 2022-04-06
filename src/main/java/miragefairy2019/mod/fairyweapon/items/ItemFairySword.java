package miragefairy2019.mod.fairyweapon.items;

import com.google.common.collect.Multimap;
import miragefairy2019.api.IFairyType;
import miragefairy2019.lib.ManaKt;
import miragefairy2019.mod.fairyweapon.FairyWeaponUtils;
import mirrg.boron.util.struct.Tuple;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemFairySword extends ItemFairyWeapon {

    public double getAdditionalAttackDamage(ItemStack itemStack) {
        return FairyWeaponUtils.getFairyAttribute("AdditionalAttackDamage", itemStack);
    }

    public double getAdditionalAttackSpeed(ItemStack itemStack) {
        return FairyWeaponUtils.getFairyAttribute("AdditionalAttackSpeed", itemStack);
    }

    public void setAdditionalAttackDamage(ItemStack itemStack, double additionalAttackDamage) {
        FairyWeaponUtils.setFairyAttribute("AdditionalAttackDamage", itemStack, additionalAttackDamage);
    }

    public void setAdditionalAttackSpeed(ItemStack itemStack, double additionalAttackSpeed) {
        FairyWeaponUtils.setFairyAttribute("AdditionalAttackSpeed", itemStack, additionalAttackSpeed);
    }

    //

    protected static class Status {

        public final double additionalAttackDamage;
        public final double additionalAttackSpeed;

        public Status(IFairyType fairyType) {

            additionalAttackDamage = 6 * ManaKt.sum(fairyType.getManaSet(), 1, 1, 1, 1, 1, 1) / 50.0; // 3~6程度

            double a = fairyType.getCost() / 100.0;
            additionalAttackSpeed = -4 + Math.min(0.25 / (a * a), 8); // -3.2~-2.4
            // コスト50のときに斧より少し早い程度（-3.0）
            // コスト0のときに+3

        }

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformationFairyWeapon(ItemStack itemStackFairyWeapon, ItemStack itemStackFairy, IFairyType fairyType, @Nullable World world, NonNullList<String> tooltip, ITooltipFlag flag) {
        Status status = new Status(fairyType);
        tooltip.add(TextFormatting.BLUE + "Additional Attack Damage: " + String.format("%.1f", status.additionalAttackDamage) + " (Shine, Fire, Wind, Gaia, Aqua, Dark)");
        tooltip.add(TextFormatting.BLUE + "Additional Attack Speed: " + String.format("%.1f", status.additionalAttackSpeed) + " (Cost)");
    }

    //

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (world.getTotalWorldTime() % 20 != 0) return;
        if (!(entity instanceof EntityLivingBase)) return;
        EntityPlayer player = (EntityPlayer) entity;

        Tuple<ItemStack, IFairyType> fairy = FairyWeaponUtils.findFairy(itemStack, player).orElse(null);
        if (fairy != null) {
            Status status = new Status(fairy.y);
            setAdditionalAttackDamage(itemStack, status.additionalAttackDamage);
            setAdditionalAttackSpeed(itemStack, status.additionalAttackSpeed);
        } else {
            setAdditionalAttackDamage(itemStack, 0);
            setAdditionalAttackSpeed(itemStack, 0);
        }
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot, ItemStack itemStack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(equipmentSlot, itemStack);
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", getAdditionalAttackDamage(itemStack), 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", getAdditionalAttackSpeed(itemStack), 0));
        }
        return multimap;
    }

}
