package miragefairy2019.modkt.api.magic;

import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;

public interface IMagicHandler {

    public default EnumActionResult onItemRightClick(EnumHand hand) {
        return EnumActionResult.PASS;
    }

    public default void onUpdate(int itemSlot, boolean isSelected) {

    }

}
