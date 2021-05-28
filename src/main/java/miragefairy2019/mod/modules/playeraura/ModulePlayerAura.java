package miragefairy2019.mod.modules.playeraura;

import miragefairy2019.mod.api.ApiPlayerAura;
import miragefairy2019.mod.api.fairy.IManaSet;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.api.playeraura.IPlayerAura;
import miragefairy2019.mod.common.playeraura.PlayerAuraManager;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.modules.fairy.EnumManaType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerConnectionFromClientEvent;
import net.minecraftforge.fml.relauncher.Side;

public class ModulePlayerAura
{

	public static void init(EventRegistryMod erMod)
	{

		// マネージャー初期化
		erMod.initRegistry.register(() -> {
			ApiPlayerAura.playerAuraManager = new PlayerAuraManager();
		});

		// ネットワークメッセージ登録
		erMod.registerNetworkMessage.register(() -> {
			ApiMain.simpleNetworkWrapper.registerMessage(PacketPlayerAura.class, MessagePlayerAura.class, 1, Side.CLIENT);
		});

		// ログインイベント
		erMod.init.register(e -> MinecraftForge.EVENT_BUS.register(new Object() {
			@SubscribeEvent
			public void hook(ServerConnectionFromClientEvent event)
			{
				INetHandlerPlayServer handler = event.getHandler();
				if (handler instanceof NetHandlerPlayServer) {
					EntityPlayerMP player = ((NetHandlerPlayServer) handler).player;
					ApiPlayerAura.playerAuraManager.getServerPlayerAura(player).send(player);
				}
			}
		}));

		// アイテムツールチップイベント
		erMod.init.register(e -> {
			if (ApiMain.side().isClient()) {
				MinecraftForge.EVENT_BUS.register(new Object() {
					@SubscribeEvent
					public void hook(ItemTooltipEvent event)
					{
						if (event.getEntityPlayer() == null) return; // なぜかクライアント起動時に呼び出される

						// 現在オーラ
						IPlayerAura playerAura = ApiPlayerAura.playerAuraManager.getClientPlayerAura();

						// TODO 摂食履歴
						// 食べた後のオーラ
						IManaSet aura = playerAura.getFoodAura(event.getItemStack()).orElse(null);

						if (aura != null) {
							event.getToolTip().add("Aura:");
							event.getToolTip().add(f1(EnumManaType.shine, playerAura, aura));
							event.getToolTip().add(f1(EnumManaType.fire, playerAura, aura));
							event.getToolTip().add(f1(EnumManaType.wind, playerAura, aura));
							event.getToolTip().add(f1(EnumManaType.gaia, playerAura, aura));
							event.getToolTip().add(f1(EnumManaType.aqua, playerAura, aura));
							event.getToolTip().add(f1(EnumManaType.dark, playerAura, aura));
						}

					}

					private int format(double value)
					{
						int value2 = (int) Math.floor(value);
						if (value2 == 0 && value > 0) value2 = 1;
						if (value2 == 0 && value < 0) value2 = -1;
						return value2;
					}

					private String f1(EnumManaType manaType, IPlayerAura playerAura, IManaSet aura)
					{
						double before = playerAura.getAura(manaType);
						double after = aura.getMana(manaType);
						double difference = after - before;
						return new TextComponentString("")
							.appendSibling(manaType.getDisplayName())
							.appendText(": ")
							.appendText(String.format("%3d", format(before)))
							.appendText(" → ")
							.appendText(String.format("%3d", format(after)))
							.appendText(" (")
							.appendSibling(new TextComponentString("")
								.appendText(String.format("%4d", format(difference)))
								.setStyle(new Style().setBold(true).setColor(difference > 0
									? TextFormatting.GREEN
									: difference < 0
										? TextFormatting.RED
										: TextFormatting.GRAY)))
							.appendText(")")
							.setStyle(new Style().setColor(manaType.getTextColor()))
							.getFormattedText();
					}
				});
			}
		});

		// アイテム使用イベント
		erMod.init.register(e -> MinecraftForge.EVENT_BUS.register(new Object() {
			@SubscribeEvent
			public void hook(LivingEntityUseItemEvent.Finish event)
			{
				if (!event.getEntity().world.isRemote) {
					ItemStack itemStack = event.getItem();
					Item item = itemStack.getItem();
					if (item instanceof ItemFood) {
						ItemFood itemFood = (ItemFood) item;

						EntityLivingBase entityLivingBase = event.getEntityLiving();
						if (entityLivingBase instanceof EntityPlayerMP) {
							EntityPlayerMP player = (EntityPlayerMP) entityLivingBase;

							ApiPlayerAura.playerAuraManager.getServerPlayerAura(player).onEat(player, itemStack, itemFood.getHealAmount(itemStack));

						}

					}
				}
			}
		}));

		// 保存イベント
		erMod.init.register(e -> MinecraftForge.EVENT_BUS.register(new Object() {
			@SubscribeEvent
			public void hook(PlayerEvent.SaveToFile event)
			{
				ApiPlayerAura.playerAuraManager.getServerPlayerAura(event.getEntityPlayer()).save(event.getEntityPlayer());
			}
		}));

	}

}
