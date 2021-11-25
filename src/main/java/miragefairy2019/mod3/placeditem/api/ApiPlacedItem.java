package miragefairy2019.mod3.placeditem.api;

import net.minecraft.block.Block;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ApiPlacedItem {

    @SideOnly(Side.CLIENT)
    public static KeyBinding keyBindingPlaceItem;

    public static Block blockPlacedItem;

}
