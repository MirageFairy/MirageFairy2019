package miragefairy2019.mod.modules.placeditem;

import org.lwjgl.input.Keyboard;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiPlacedItem;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.lib.EventRegistryMod;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModulePlacedItem
{

	public static void init(EventRegistryMod erMod)
	{

		// ブロック
		erMod.registerBlock.register(b -> {
			BlockPlacedItem blockPlacedItem = new BlockPlacedItem();
			blockPlacedItem.setRegistryName(ModMirageFairy2019.MODID, "placed_item");
			blockPlacedItem.setUnlocalizedName("placedItem");
			ForgeRegistries.BLOCKS.register(blockPlacedItem);
			ApiPlacedItem.blockPlacedItem = blockPlacedItem;
		});

		// タイルエンティティ
		erMod.init.register(e -> {
			GameRegistry.registerTileEntity(TileEntityPlacedItem.class, new ResourceLocation(ModMirageFairy2019.MODID, "placed_item"));
		});

		// タイルエンティティレンダラー
		erMod.init.register(e -> {
			if (ApiMain.side().isClient()) {
				new Object() {
					@SideOnly(Side.CLIENT)
					public void run()
					{
						ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPlacedItem.class, new TileEntityRendererPlacedItem());
					}
				}.run();
			}
		});

		// キーバインディング
		erMod.initKeyBinding.register(() -> {
			if (ApiMain.side().isClient()) {
				new Object() {
					@SideOnly(Side.CLIENT)
					public void run()
					{
						ApiPlacedItem.keyBindingPlaceItem = new KeyBinding("miragefairy2019.placeItem", KeyConflictContext.IN_GAME, Keyboard.KEY_P, "miragefairy2019 (MirageFairy2019)");
					}
				}.run();
			}
		});

		// ネットワークラッパー
		erMod.init.register(e -> {
			ApiPlacedItem.simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(ModMirageFairy2019.MODID);
			ApiPlacedItem.simpleNetworkWrapper.registerMessage(PacketPlaceItem.class, PacketPlaceItem.Message.class, 0, Side.SERVER);
		});

		// キーリスナー
		erMod.init.register(e -> {
			if (ApiMain.side().isClient()) {
				new Object() {
					@SideOnly(Side.CLIENT)
					public void run()
					{
						ClientRegistry.registerKeyBinding(ApiPlacedItem.keyBindingPlaceItem);
						MinecraftForge.EVENT_BUS.register(new Object() {
							@SubscribeEvent
							public void accept(InputUpdateEvent event)
							{
								while (ApiPlacedItem.keyBindingPlaceItem.isPressed()) {
									EntityPlayer player = event.getEntityPlayer();
									if (!player.isSpectator()) {
										if (player instanceof EntityPlayerSP) {
											RayTraceResult result = player.rayTrace(player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue(), 0);
											if (result.typeOfHit == Type.BLOCK) {
												ApiPlacedItem.simpleNetworkWrapper.sendToServer(new PacketPlaceItem.Message(
													player.world.getBlockState(result.getBlockPos()).getBlock().isReplaceable(player.world, result.getBlockPos())
														? result.getBlockPos()
														: result.getBlockPos().offset(result.sideHit)));
											}
										}
									}
								}
							}
						});
					}
				}.run();
			}
		});

	}

}
