package miragefairy2019.mod.modules.fairyweapon;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;

import javax.annotation.Nonnull;

public interface IMagicHandler {

    @Nonnull
    public default EnumActionResult onItemRightClick(@Nonnull EnumHand hand) {
        return EnumActionResult.PASS;
    }

    public default void onUpdate(int itemSlot, boolean isSelected) {

    }

    public default void hitEntity(@Nonnull EntityLivingBase target) {

    }

}
