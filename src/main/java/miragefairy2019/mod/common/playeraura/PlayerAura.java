package miragefairy2019.mod.common.playeraura;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.fairy.IManaSet;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.api.playeraura.IPlayerAura;
import miragefairy2019.mod.lib.Monad;
import miragefairy2019.mod.lib.json.JsonExtractor;
import miragefairy2019.mod.modules.fairy.EnumManaType;
import miragefairy2019.mod.modules.fairy.ManaSet;
import miragefairy2019.mod.modules.playeraura.MessagePlayerAura;
import mirrg.boron.util.UtilsFile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class PlayerAura implements IPlayerAura
{

	private static final Logger LOGGER = LogManager.getLogger(PlayerAura.class);

	private PlayerAuraManager playerAuraManager;

	public double shine = 0;
	public double fire = 0;
	public double wind = 0;
	public double gaia = 0;
	public double aqua = 0;
	public double dark = 0;

	//

	public PlayerAura(PlayerAuraManager playerAuraManager)
	{
		this.playerAuraManager = playerAuraManager;
	}

	@Override
	public double getAura(EnumManaType manaType)
	{
		switch (manaType) {
			case shine:
				return shine;
			case fire:
				return fire;
			case wind:
				return wind;
			case gaia:
				return gaia;
			case aqua:
				return aqua;
			case dark:
				return dark;
			default:
				throw new IllegalArgumentException("" + manaType);
		}
	}

	@Override
	public Optional<IManaSet> getFoodAura(ItemStack itemStack)
	{
		// TODO 摂食履歴による効果低減
		return playerAuraManager.getFoodAura(itemStack).map(aura -> {
			return new ManaSet(
				aura.getShine(),
				aura.getFire(),
				aura.getWind(),
				aura.getGaia(),
				aura.getAqua(),
				aura.getDark());
		});
	}

	@Override
	public void setAura(double shine, double fire, double wind, double gaia, double aqua, double dark)
	{
		this.shine = shine;
		this.fire = fire;
		this.wind = wind;
		this.gaia = gaia;
		this.aqua = aqua;
		this.dark = dark;
	}

	//

	public File getFile(EntityPlayer player)
	{
		return new File(player.world.getSaveHandler().getWorldDirectory(), ModMirageFairy2019.MODID + "/playeraura/" + player.getCachedUniqueIdString() + ".json");
	}

	public void fromJson(Object object)
	{
		new JsonExtractor(object).asMap()
			.get("aura", mapAura -> mapAura.asMap()
				.get("shine", value -> shine = value.toInt(0))
				.get("fire", value -> fire = value.toInt(0))
				.get("wind", value -> wind = value.toInt(0))
				.get("gaia", value -> gaia = value.toInt(0))
				.get("aqua", value -> aqua = value.toInt(0))
				.get("dark", value -> dark = value.toInt(0)));
	}

	public Object toJson()
	{
		return Monad.of(new HashMap<>())
			.peek(map -> map.put("aura", Monad.of(new HashMap<>())
				.peek(mapAura -> mapAura.put("shine", shine))
				.peek(mapAura -> mapAura.put("fire", fire))
				.peek(mapAura -> mapAura.put("wind", wind))
				.peek(mapAura -> mapAura.put("gaia", gaia))
				.peek(mapAura -> mapAura.put("aqua", aqua))
				.peek(mapAura -> mapAura.put("dark", dark))
				.get()))
			.get();
	}

	@Override
	public void load(EntityPlayer player)
	{
		File file = getFile(player);
		if (file.exists()) {
			try (InputStreamReader in = new InputStreamReader(new FileInputStream(file))) {
				fromJson(new Gson().fromJson(in, Object.class));
			} catch (IOException e) {
				LOGGER.error("Can not load player aura: " + player.getName(), e);
			}
		} else {

		}
	}

	@Override
	public void save(EntityPlayer player)
	{
		LOGGER.debug("Saving: " + player.getCachedUniqueIdString() + " > " + getFile(player));
		try (OutputStreamWriter out = new OutputStreamWriter(UtilsFile.getOutputStreamWithMkdirs(getFile(player)))) {
			new Gson().toJson(toJson(), out);
		} catch (IOException e) {
			LOGGER.error("Can not save player aura: " + player.getName(), e);
			LOGGER.error(toJson());
		}
	}

	//

	@Override
	public void onEat(EntityPlayerMP player, ItemStack itemStack, int healAmount)
	{
		IManaSet aura = playerAuraManager.getFoodAura(itemStack).orElse(null);
		if (aura == null) return;

		// TODO 摂食履歴システム　連続で同じアイテムを食べると効果が下がっていく
		shine = aura.getShine();
		fire = aura.getFire();
		wind = aura.getWind();
		gaia = aura.getGaia();
		aqua = aura.getAqua();
		dark = aura.getDark();

		send(player);

	}

	@Override
	public void send(EntityPlayerMP player)
	{
		ApiMain.simpleNetworkWrapper.sendTo(new MessagePlayerAura(
			shine,
			fire,
			wind,
			gaia,
			aqua,
			dark), player);
	}

}
