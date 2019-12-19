package miragefairy2019.mod.modules.fairycrystal;

import static miragefairy2019.mod.modules.fairy.ModuleFairy.FairyTypes.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.fairycrystal.DropFixed;
import miragefairy2019.mod.api.fairycrystal.IRightClickDrop;
import miragefairy2019.mod.api.fairycrystal.RightClickDrops;
import miragefairy2019.mod.lib.WeightedRandom;
import mirrg.boron.util.UtilsFile;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class VariantFairyCrystalChristmas extends VariantFairyCrystal
{

	private static final Logger LOGGER = LogManager.getLogger(VariantFairyCrystalChristmas.class);

	public VariantFairyCrystalChristmas(String registryName, String unlocalizedName, String oreName)
	{
		super(registryName, unlocalizedName, oreName);
	}

	//

	private static final Object lock = new Object();

	private static final String GLOBAL = "GLOBAL";

	private static File getCapacityTableFile(World world, String point)
	{
		File directoryWorld = world.getSaveHandler().getWorldDirectory();
		File directoryModData = new File(directoryWorld, ModMirageFairy2019.MODID);
		File directoryDrop = new File(directoryModData, "drop");
		File fileDrop = new File(directoryDrop, "fairyCrystalChristmas." + point + ".json");
		return fileDrop;
	}

	// TODO キャッシュ
	@SuppressWarnings("unchecked")
	private static Map<String, Integer> loadCapacityTable(World world, String point) throws IOException
	{
		File fileDrop = getCapacityTableFile(world, point);

		try {
			Map<String, Number> input;
			if (fileDrop.exists()) {
				try (InputStreamReader in = new InputStreamReader(new FileInputStream(fileDrop))) {
					input = new Gson().fromJson(in, Map.class);
				}
			} else {
				input = new HashMap<>();
			}
			Map<String, Integer> result = new HashMap<>();
			{
				for (Entry<String, Number> entry : input.entrySet()) {
					result.put(entry.getKey(), entry.getValue().intValue());
				}
			}
			return result;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	private static void saveCapacityTable(World world, String point, Map<String, Integer> capacityTable) throws IOException
	{
		File fileDrop = getCapacityTableFile(world, point);

		try {
			try (OutputStreamWriter out = new OutputStreamWriter(UtilsFile.getOutputStreamWithMkdirs(fileDrop))) {
				new Gson().toJson(capacityTable, out);
			}
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	//

	private static final int capacityGlobal = 500;
	private static final int capacityPlayer = 10;

	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack itemStackCrystal = player.getHeldItem(hand);
		if (itemStackCrystal.isEmpty()) return EnumActionResult.PASS;

		super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);

		if (player.isSneaking()) {

			{

				// 在庫読み込み
				// 在庫が読み込めなかった場合は出ない
				Map<String, Integer> capacityTable;
				synchronized (lock) {
					try {
						capacityTable = loadCapacityTable(world, "santaclaus");
					} catch (IOException e) {
						LOGGER.error("Can not load capacity table!", e);
						return EnumActionResult.SUCCESS;
					}
				}

				// 在庫がない場合は出ない
				int droppedGlobal;
				int droppedPlayer;
				int stockGlobal;
				int stockPlayer;
				{
					droppedGlobal = capacityTable.getOrDefault(GLOBAL, 0);
					droppedPlayer = capacityTable.getOrDefault(player.getCachedUniqueIdString(), 0);
					stockGlobal = Math.max(capacityGlobal - droppedGlobal, 0);
					stockPlayer = Math.max(capacityPlayer - droppedPlayer, 0);
				}

				// 表示
				player.sendStatusMessage(new TextComponentString("Global Stock: " + stockGlobal + " / " + capacityGlobal + " " + santaclaus[0].createItemStack().getDisplayName()), false);
				player.sendStatusMessage(new TextComponentString("Player Stock: " + stockPlayer + " / " + capacityPlayer + " " + santaclaus[0].createItemStack().getDisplayName()), false);

			}

			{

				// 在庫読み込み
				// 在庫が読み込めなかった場合は出ない
				Map<String, Integer> capacityTable;
				synchronized (lock) {
					try {
						capacityTable = loadCapacityTable(world, "christmas");
					} catch (IOException e) {
						LOGGER.error("Can not load capacity table!", e);
						return EnumActionResult.SUCCESS;
					}
				}

				// 在庫がない場合は出ない
				int droppedGlobal;
				int droppedPlayer;
				int stockGlobal;
				int stockPlayer;
				{
					droppedGlobal = capacityTable.getOrDefault(GLOBAL, 0);
					droppedPlayer = capacityTable.getOrDefault(player.getCachedUniqueIdString(), 0);
					stockGlobal = Math.max(capacityGlobal - droppedGlobal, 0);
					stockPlayer = Math.max(capacityPlayer - droppedPlayer, 0);
				}

				// 表示
				player.sendStatusMessage(new TextComponentString("Global Stock: " + stockGlobal + " / " + capacityGlobal + " " + christmas[0].createItemStack().getDisplayName()), false);
				player.sendStatusMessage(new TextComponentString("Player Stock: " + stockPlayer + " / " + capacityPlayer + " " + christmas[0].createItemStack().getDisplayName()), false);

			}

		}

		return EnumActionResult.SUCCESS;
	}

	@Override
	public FairyCrystalDropper getDropper()
	{
		FairyCrystalDropper self = super.getDropper();
		return new FairyCrystalDropper() {
			@Override
			public Optional<ItemStack> drop(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
			{
				synchronized (lock) {

					// ガチャリスト取得
					List<WeightedRandom.Item<ItemStack>> dropTable = getDropTable(player, world, pos, hand, facing, hitX, hitY, hitZ);

					// ガチャを引く
					Optional<ItemStack> oItemStack = WeightedRandom.getRandomItem(world.rand, dropTable);

					// ドロップしたものが限定ならカウントする
					if (oItemStack.isPresent()) {
						onDrop(world, pos, player, oItemStack.get(), santaclaus[0].createItemStack(), "santaclaus");
						onDrop(world, pos, player, oItemStack.get(), christmas[0].createItemStack(), "christmas");
					}

					return oItemStack;
				}
			}

			private void onDrop(World world, BlockPos pos, EntityPlayer player, ItemStack itemStack, ItemStack dropItem, String stringFairyType)
			{
				if (ItemStack.areItemStacksEqualUsingNBTShareTag(itemStack, dropItem)) {

					// 在庫読み込み
					// 在庫が読み込めなかった場合は何もできない
					Map<String, Integer> capacityTable;
					synchronized (lock) {
						try {
							capacityTable = loadCapacityTable(world, stringFairyType);
						} catch (IOException e) {
							LOGGER.error("Can not load capacity table!", e);
							return;
						}
					}

					// 在庫を減らす
					int droppedGlobal;
					int droppedPlayer;
					int stockGlobal;
					int stockPlayer;
					boolean isGlobal;
					{
						droppedGlobal = capacityTable.getOrDefault(GLOBAL, 0);
						droppedPlayer = capacityTable.getOrDefault(player.getCachedUniqueIdString(), 0);
						stockGlobal = Math.max(capacityGlobal - droppedGlobal, 0);
						stockPlayer = Math.max(capacityPlayer - droppedPlayer, 0);

						// 在庫の比に従って排出
						isGlobal = world.rand.nextDouble() < stockGlobal / (double) (stockGlobal + stockPlayer);
						if (isGlobal) {
							capacityTable.put(GLOBAL, droppedGlobal + 1);
						} else {
							capacityTable.put(player.getCachedUniqueIdString(), droppedPlayer + 1);
						}

					}

					// 在庫を書き込み
					// 在庫が書き込めなかった場合は何もしない
					try {
						saveCapacityTable(world, stringFairyType, capacityTable);
					} catch (IOException e) {
						LOGGER.error("Can not save capacity table!", e);
						return;
					}

					// ログ出力
					LOGGER.info("Dropped: " + dropItem.getDisplayName() + " by " + player.getDisplayNameString() + " at " + pos + "@" + world.provider.getDimension());

					// ドロップ本人のメッセージ欄に出力
					if (isGlobal) {
						player.sendStatusMessage(new TextComponentString("Global Stock: " + (stockGlobal - 1) + " / " + capacityGlobal + " " + dropItem.getDisplayName()), true);
					} else {
						player.sendStatusMessage(new TextComponentString("Player Stock: " + (stockPlayer - 1) + " / " + capacityPlayer + " " + dropItem.getDisplayName()), true);
					}

					// ワールド全体チャット
					if (isGlobal && (stockGlobal - 1) % (capacityGlobal / 20) == 0) {
						TextComponentString message = new TextComponentString("Global Stock: " + (stockGlobal - 1) + " / " + capacityGlobal + " " + dropItem.getDisplayName());
						message.getStyle().setColor(TextFormatting.GRAY);
						message.getStyle().setItalic(true);
						MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
						for (EntityPlayer player2 : server.getPlayerList().getPlayers()) {
							player2.sendMessage(message);
						}
						server.sendMessage(message);
					}

				}
			}

			@Override
			public ISuppliterator<IRightClickDrop> getDropList()
			{
				return self.getDropList()
					.after(RightClickDrops.eventDrop(new DropFixed(santaclaus[0].createItemStack(), 0.01), t -> {

						// 在庫読み込み
						// 在庫が読み込めなかった場合は出ない
						Map<String, Integer> capacityTable;
						synchronized (lock) {
							try {
								capacityTable = loadCapacityTable(t.x, "santaclaus");
							} catch (IOException e) {
								LOGGER.error("Can not load capacity table!", e);
								return false;
							}
						}

						// 在庫がない場合は出ない
						{
							int droppedGlobal = capacityTable.getOrDefault(GLOBAL, 0);
							int droppedPlayer = capacityTable.getOrDefault(t.z.getCachedUniqueIdString(), 0);
							int stockGlobal = Math.max(capacityGlobal - droppedGlobal, 0);
							int stockPlayer = Math.max(capacityPlayer - droppedPlayer, 0);
							if (stockGlobal + stockPlayer <= 0) return false;
						}

						// 指定座標の付近でないならば出ない
						if (t.x.provider.getDimension() != 0) return false;
						if (t.y.getDistance(207, 64, -244) > 32) return false;

						return true;
					}))
					.after(RightClickDrops.eventDrop(new DropFixed(christmas[0].createItemStack(), 0.1), t -> {

						// 在庫読み込み
						// 在庫が読み込めなかった場合は出ない
						Map<String, Integer> capacityTable;
						synchronized (lock) {
							try {
								capacityTable = loadCapacityTable(t.x, "christmas");
							} catch (IOException e) {
								LOGGER.error("Can not load capacity table!", e);
								return false;
							}
						}

						// 在庫がない場合は出ない
						{
							int droppedGlobal = capacityTable.getOrDefault(GLOBAL, 0);
							int droppedPlayer = capacityTable.getOrDefault(t.z.getCachedUniqueIdString(), 0);
							int stockGlobal = Math.max(capacityGlobal - droppedGlobal, 0);
							int stockPlayer = Math.max(capacityPlayer - droppedPlayer, 0);
							if (stockGlobal + stockPlayer <= 0) return false;
						}

						// 指定座標の付近でないならば出ない
						if (t.x.provider.getDimension() != 0) return false;
						if (t.y.getDistance(207, 64, -244) > 32) return false;

						return true;
					}));
			}
		};
	}

}
