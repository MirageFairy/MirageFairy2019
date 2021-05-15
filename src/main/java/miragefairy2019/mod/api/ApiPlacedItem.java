package miragefairy2019.mod.api;

import net.minecraft.block.Block;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ApiPlacedItem
{

	public static SimpleNetworkWrapper simpleNetworkWrapper;

	@SideOnly(Side.CLIENT)
	public static KeyBinding keyBindingPlaceItem;

	public static Block blockPlacedItem;

}
