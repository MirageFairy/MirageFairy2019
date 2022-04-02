package miragefairy2019.api;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IFairyWeaponItem {

    @SideOnly(Side.CLIENT)
    public void addInformationFairyWeapon(
        @Nonnull ItemStack itemStackFairyWeapon,
        @Nonnull ItemStack itemStackFairy,
        @Nonnull IFairyType fairyType,
        @Nullable World world,
        @Nonnull NonNullList<String> tooltip,
        @Nonnull ITooltipFlag flag
    );

}
