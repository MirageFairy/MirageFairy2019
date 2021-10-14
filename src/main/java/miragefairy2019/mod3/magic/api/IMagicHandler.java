package miragefairy2019.mod3.magic.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;

public interface IMagicHandler {

    public default EnumActionResult onItemRightClick(EnumHand hand) {
        return EnumActionResult.PASS;
    }

    public default void onUpdate(int itemSlot, boolean isSelected) {

    }

    public default void hitEntity(EntityLivingBase target) {

    }

}
