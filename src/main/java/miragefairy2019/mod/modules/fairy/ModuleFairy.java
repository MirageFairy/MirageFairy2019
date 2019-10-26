package miragefairy2019.mod.modules.fairy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.ApiFairy;
import miragefairy2019.mod.api.ApiFairy.EnumAbilityType;
import miragefairy2019.mod.api.ApiMain;
import miragefairy2019.mod.api.fairy.AbilitySet;
import miragefairy2019.mod.api.fairy.ColorSet;
import miragefairy2019.mod.api.fairy.FairyType;
import miragefairy2019.mod.api.fairy.IAbilityType;
import miragefairy2019.mod.api.fairy.ManaSet;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.Utils;
import mirrg.boron.util.struct.ImmutableArray;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ModuleFairy
{

	public static ItemFairy[] itemFairyList;

	public static class FairyTypes
	{

		private final int count;

		public FairyTypes(int count)
		{
			this.count = count;
		}

		//

		public static ImmutableArray<Tuple<Integer, VariantFairy[]>> variants;

		public static VariantFairy[] air;
		public static VariantFairy[] water;
		public static VariantFairy[] fire;
		public static VariantFairy[] sun;
		public static VariantFairy[] stone;
		public static VariantFairy[] dirt;
		public static VariantFairy[] iron;
		public static VariantFairy[] diamond;
		public static VariantFairy[] redstone;
		public static VariantFairy[] enderman;
		public static VariantFairy[] moon;
		public static VariantFairy[] sand;
		public static VariantFairy[] gold;
		public static VariantFairy[] spider;
		public static VariantFairy[] skeleton;
		public static VariantFairy[] zombie;
		public static VariantFairy[] creeper;
		public static VariantFairy[] wheat;
		public static VariantFairy[] lilac;
		public static VariantFairy[] torch;
		public static VariantFairy[] lava;
		public static VariantFairy[] star;
		public static VariantFairy[] gravel;
		public static VariantFairy[] emerald;
		public static VariantFairy[] lapislazuli;
		public static VariantFairy[] enderdragon;
		public static VariantFairy[] witherskeleton;
		public static VariantFairy[] wither;
		public static VariantFairy[] thunder;
		public static VariantFairy[] chicken;
		public static VariantFairy[] furnace;
		public static VariantFairy[] magentaglazedterracotta;
		public static VariantFairy[] bread;
		public static VariantFairy[] daytime;
		public static VariantFairy[] night;
		public static VariantFairy[] morning;
		public static VariantFairy[] fine;
		public static VariantFairy[] rain;
		public static VariantFairy[] plains;
		public static VariantFairy[] forest;
		public static VariantFairy[] apple;
		public static VariantFairy[] carrot;
		public static VariantFairy[] cactus;
		public static VariantFairy[] axe;
		public static VariantFairy[] chest;
		public static VariantFairy[] craftingtable;
		public static VariantFairy[] potion;
		public static VariantFairy[] sword;
		public static VariantFairy[] dispenser;
		public static VariantFairy[] ocean;
		public static VariantFairy[] fish;
		public static VariantFairy[] cod;
		public static VariantFairy[] salmon;
		public static VariantFairy[] pufferfish;
		public static VariantFairy[] clownfish;
		public static VariantFairy[] spruce;
		public static VariantFairy[] anvil;
		public static VariantFairy[] obsidian;
		public static VariantFairy[] seed;
		public static VariantFairy[] enchant;

		//

		private List<Tuple<Integer, VariantFairy[]>> variants2 = new ArrayList<>();

		protected void init()
		{
			{
				r(0, air = v(t(0, "air", 1, 15, m(0, 0, 2, 0, 1, 10), a(0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 3, 0, 0, 0, 0, 0, 1), c(0xFFBE80, 0xDEFFFF, 0xDEFFFF, 0xB0FFFF))));
				r(1, water = v(t(1, "water", 1, 50, m(0, 0, 1, 4, 8, 10), a(1, 0, 0, 0, 0, 10, 0, 5, 0, 0, 0, 1, 7, 0, 1, 0, 1), c(0x5469F2, 0x5985FF, 0x172AD3, 0x2D40F4))));
				r(2, fire = v(t(2, "fire", 2, 20, m(1, 17, 1, 0, 0, 10), a(7, 2, 1, 10, 15, 0, 0, 6, 0, 0, 0, 4, 8, 0, 0, 0, 10), c(0xFF6C01, 0xF9DFA4, 0xFF7324, 0xFF4000))));
				r(3, sun = v(t(3, "sun", 5, 99, m(10, 40, 40, 20, 20, 60), a(2, 0, 0, 21, 6, 0, 0, 6, 0, 0, 0, 2, 0, 0, 0, 1, 5), c(0xff2f00, 0xff972b, 0xff7500, 0xffe7b2))));
				r(4, stone = v(t(4, "stone", 1, 83, m(0, 0, 0, 8, 0, 10), a(2, 0, 2, 0, 0, 0, 3, 2, 0, 0, 0, 4, 0, 0, 0, 0, 0), c(0x333333, 0x8F8F8F, 0x686868, 0x747474))));
				r(5, dirt = v(t(5, "dirt", 1, 70, m(0, 0, 0, 0, 5, 10), a(1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1), c(0xB87440, 0xB9855C, 0x593D29, 0x914A18))));
				r(6, iron = v(t(6, "iron", 2, 86, m(0, 1, 0, 10, 1, 4), a(2, 0, 2, 1, 0, 0, 4, 2, 0, 0, 0, 3, 0, 5, 0, 0, 0), c(0xA0A0A0, 0xD8D8D8, 0x727272, 0xD8AF93))));
				r(7, diamond = v(t(7, "diamond", 4, 76, m(10, 13, 19, 49, 23, 0), a(1, 0, 2, 1, 0, 0, 17, 8, 0, 0, 0, 10, 0, 7, 0, 1, 0), c(0x97FFE3, 0xD1FAF3, 0x70FFD9, 0x30DBBD))));
				r(8, redstone = v(t(8, "redstone", 3, 54, m(1, 35, 11, 10, 0, 6), a(0, 0, 0, 7, 0, 0, 6, 4, 0, 1, 0, 0, 4, 0, 0, 4, 6), c(0xFF5959, 0xFF0000, 0xCD0000, 0xBA0000))));
				r(9, enderman = v(t(9, "enderman", 4, 48, m(1, 12, 12, 16, 10, 1), a(11, 3, 5, 2, 0, 1, 2, 6, 1, 12, 0, 4, 2, 0, 2, 8, 4), c(0x000000, 0x161616, 0x161616, 0xEF84FA))));
				r(10, moon = v(t(10, "moon", 5, 95, m(10, 25, 25, 20, 20, 90), a(0, 0, 0, 6, 0, 0, 2, 6, 0, 0, 0, 0, 0, 0, 0, 1, 2), c(0xD9E4FF, 0x747D93, 0x0C121F, 0x2D4272))));
				r(11, sand = v(t(11, "sand", 1, 64, m(0.1, 0, 0, 4, 1, 10), a(1, 0, 0, 0, 0, 0, 7, 3, 0, 0, 0, 0, 0, 1, 0, 0, 0), c(0xB87440, 0xEEE4B6, 0xC2BC84, 0xD8D09B))));
				r(12, gold = v(t(12, "gold", 3, 93, m(1, 0, 12, 10, 3, 15), a(1, 0, 1, 1, 0, 0, 3, 12, 0, 0, 0, 1, 0, 2, 0, 0, 0), c(0xA0A0A0, 0xFFFF0B, 0xDC7613, 0xDEDE00))));
				r(13, spider = v(t(13, "spider", 2, 43, m(0, 0, 0, 0, 10, 4), a(10, 0, 0, 2, 0, 2, 0, 2, 0, 0, 0, 2, 2, 1, 3, 4, 2), c(0x494422, 0x61554A, 0x52483F, 0xA80E0E))));
				r(14, skeleton = v(t(14, "skeleton", 1, 49, m(0, 8, 10, 5, 0, 8), a(12, 1, 3, 0, 0, 0, 0, 2, 0, 1, 12, 5, 2, 0, 0, 4, 3), c(0xCACACA, 0xCFCFCF, 0xCFCFCF, 0x494949))));
				r(15, zombie = v(t(15, "zombie", 1, 55, m(0, 9, 10, 0, 2, 9), a(13, 0, 1, 0, 0, 4, 0, 1, 2, 0, 0, 2, 3, 2, 4, 3, 3), c(0x2B4219, 0x00AAAA, 0x322976, 0x2B4219))));
				r(16, creeper = v(t(16, "creeper", 2, 35, m(0, 0, 10, 0, 12, 4), a(10, 0, 3, 0, 3, 2, 0, 4, 0, 0, 3, 17, 6, 0, 2, 3, 7), c(0x5BAA53, 0xD6FFCF, 0x5EE74C, 0x000000))));
				r(17, wheat = v(t(17, "wheat", 2, 31, m(0, 0, 0, 0, 10, 5), a(0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 6, 0, 2), c(0x168700, 0xD5DA45, 0x716125, 0x9E8714))));
				r(18, lilac = v(t(18, "lilac", 3, 28, m(0, 0, 1, 1, 10, 0), a(0, 0, 1, 0, 0, 2, 0, 11, 0, 0, 0, 0, 1, 0, 2, 0, 1), c(0x63D700, 0xF0C9FF, 0xDC8CE6, 0xA22CFF))));
				r(19, torch = v(t(19, "torch", 1, 19, m(0.1, 1, 1, 10, 4, 0), a(1, 1, 2, 12, 8, 0, 0, 2, 0, 0, 1, 0, 0, 0, 0, 1, 4), c(0xFFFFFF, 0xFFC52C, 0xFF5800, 0xFFE6A5))));
				r(20, lava = v(t(20, "lava", 2, 58, m(0, 18, 0, 4, 0, 10), a(8, 1, 1, 13, 18, 0, 0, 1, 0, 0, 0, 3, 3, 0, 0, 0, 7), c(0xCD4208, 0xEDB54A, 0xCC4108, 0x4C1500))));
				r(21, star = v(t(21, "star", 4, 98, m(10, 30, 50, 10, 30, 90), a(0, 0, 0, 4, 2, 0, 1, 7, 0, 0, 0, 0, 0, 0, 0, 1, 1), c(0xffffff, 0x2C2C2E, 0x0E0E10, 0x191919))));
				r(22, gravel = v(t(22, "gravel", 2, 77, m(0, 0, 0, 12, 0, 10), a(1, 0, 0, 0, 0, 0, 3, 1, 0, 0, 0, 5, 0, 3, 0, 0, 0), c(0x333333, 0xC0B5B6, 0x968B8E, 0x63565C))));
				r(23, emerald = v(t(23, "emerald", 4, 73, m(10, 0, 6, 0, 42, 71), a(0, 0, 0, 1, 0, 0, 15, 9, 0, 0, 0, 8, 0, 4, 0, 1, 0), c(0x9FF9B5, 0x81F99E, 0x17DD62, 0x008A25))));
				r(24, lapislazuli = v(t(24, "lapislazuli", 4, 62, m(1, 0, 8, 10, 18, 0), a(0, 0, 0, 0, 0, 0, 9, 10, 0, 0, 0, 3, 0, 1, 0, 2, 0), c(0xA2B7E8, 0x4064EC, 0x224BD5, 0x0A33C2))));
				r(25, enderdragon = v(t(25, "enderdragon", 5, 61, m(3, 0, 51, 0, 10, 0), a(20, 0, 0, 2, 0, 2, 0, 5, 0, 2, 7, 9, 1, 0, 2, 5, 6), c(0x000000, 0x181818, 0x181818, 0xA500E2))));
				r(26, witherskeleton = v(t(26, "witherskeleton", 4, 69, m(0, 11, 10, 4, 1, 1), a(17, 1, 3, 0, 0, 0, 0, 3, 0, 0, 0, 7, 2, 4, 1, 5, 4), c(0x505252, 0x1C1C1C, 0x1C1C1C, 0x060606))));
				r(27, wither = v(t(27, "wither", 5, 52, m(0, 8, 10, 3, 1, 0), a(25, 0, 3, 2, 1, 1, 0, 2, 0, 0, 14, 15, 2, 0, 1, 4, 7), c(0x181818, 0x3C3C3C, 0x141414, 0x557272))));
				r(28, thunder = v(t(28, "thunder", 3, 18, m(2, 9, 3, 2, 0, 10), a(9, 0, 1, 8, 11, 0, 0, 6, 0, 4, 5, 11, 11, 0, 0, 0, 12), c(0xB4FFFF, 0x4D5670, 0x4D5670, 0xFFEB00))));
				r(29, chicken = v(t(29, "chicken", 1, 39, m(0, 0, 1, 0, 10, 7), a(1, 0, 0, 0, 0, 3, 0, 1, 0, 0, 2, 0, 1, 0, 14, 3, 2), c(0xFFDFA3, 0xFFFFFF, 0xFFFFFF, 0xD93117))));
				r(30, furnace = v(t(30, "furnace", 2, 72, m(0, 2, 0, 10, 2, 0), a(1, 10, 0, 6, 10, 0, 0, 1, 8, 0, 0, 1, 4, 0, 0, 3, 3), c(0xFFFFFF, 0xFF7F19, 0x8E8E8E, 0x383838))));
				r(31, magentaglazedterracotta = v(t(31, "magentaglazedterracotta", 3, 60, m(0, 1, 0, 10, 11, 0), a(0, 2, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 1, 0), c(0xFFFFFF, 0xF4B5CB, 0xCB58C2,
					0x9D2D95))));
				r(32, bread = v(t(32, "bread", 2, 35, m(0, 0, 0, 5, 10, 0), a(0, 1, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 14, 0, 5), c(0xCC850C, 0x9E7325, 0x654B17, 0x3F2E0E))));
				r(33, daytime = v(t(33, "daytime", 1, 88, m(1, 0, 10, 3, 7, 24), a(0, 0, 0, 11, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 3), c(0xFFE260, 0xAACAEF, 0x84B5EF, 0xFFE7B2))));
				r(34, night = v(t(34, "night", 1, 83, m(0, 7, 10, 0, 7, 24), a(0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 1, 1), c(0xFFE260, 0x2C2C2E, 0x0E0E10, 0x2D4272))));
				r(35, morning = v(t(35, "morning", 2, 85, m(1, 5, 10, 1, 7, 18), a(0, 0, 0, 9, 1, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 2), c(0xFFE260, 0x91C4D9, 0x4570A6, 0xFF7017))));
				r(36, fine = v(t(36, "fine", 1, 22, m(1, 0, 10, 4, 12, 28), a(0, 0, 0, 17, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 3), c(0xB4FFFF, 0xAACAEF, 0x84B5EF, 0xffe7b2))));
				r(37, rain = v(t(37, "rain", 2, 25, m(0, 2, 10, 0, 19, 17), a(1, 0, 0, 0, 0, 13, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 1), c(0xB4FFFF, 0x4D5670, 0x4D5670, 0x2D40F4))));
				r(38, plains = v(t(38, "plains", 1, 79, m(0, 0, 0, 3, 18, 10), a(0, 0, 1, 0, 0, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 2), c(0x80FF00, 0xD4FF82, 0x86C91C, 0xBB5400))));
				r(39, forest = v(t(39, "forest", 2, 83, m(0, 0, 2, 12, 32, 10), a(1, 0, 9, 0, 0, 3, 0, 3, 0, 0, 0, 0, 0, 0, 0, 2, 3), c(0x80FF00, 0x7B9C62, 0x89591D, 0x2E6E14))));
				r(40, apple = v(t(40, "apple", 2, 43, m(0.1, 0, 3, 0, 10, 2), a(0, 0, 4, 0, 0, 2, 0, 6, 0, 0, 0, 0, 2, 0, 12, 0, 2), c(0xFF755D, 0xFF564E, 0xFF0000, 0x01A900))));
				r(41, carrot = v(t(41, "carrot", 3, 38, m(0, 0, 1, 0, 10, 0), a(0, 0, 1, 0, 0, 2, 0, 3, 0, 0, 0, 0, 1, 0, 10, 0, 2), c(0xFF8F00, 0xFFAD66, 0xFF9600, 0x01A900))));
				r(42, cactus = v(t(42, "cactus", 2, 41, m(0, 7, 0, 0, 10, 2), a(9, 0, 1, 0, 0, 4, 0, 2, 0, 0, 1, 2, 3, 7, 4, 0, 1), c(0x008200, 0xB0FFAC, 0x00E100, 0x010000))));
				r(43, axe = v(t(43, "axe", 2, 83, m(0, 7, 0, 10, 2, 5), a(12, 2, 12, 0, 0, 0, 0, 1, 0, 0, 0, 6, 0, 10, 0, 2, 0), c(0xFFFFFF, 0xCD9A6A, 0x529B3A, 0xC9D0C6))));
				r(44, chest = v(t(44, "chest", 1, 31, m(0, 0, 0, 10, 0, 7), a(0, 1, 3, 0, 0, 0, 0, 1, 15, 0, 0, 0, 0, 0, 0, 2, 0), c(0xFFFFFF, 0xFFA431, 0xFFA900, 0xFFC2A5))));
				r(45, craftingtable = v(t(45, "craftingtable", 2, 40, m(0, 6, 0, 10, 0, 0), a(0, 12, 2, 0, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 6, 0), c(0xFFFFFF, 0xFFBB9A, 0xFFC980, 0x000000))));
				r(46, potion = v(t(46, "potion", 3, 29, m(1, 2, 18, 10, 3, 0), a(3, 2, 0, 1, 1, 6, 0, 4, 2, 0, 2, 0, 10, 0, 1, 8, 1), c(0xFFFFFF, 0x52CAFF, 0x00AEFF, 0xFFFFFF))));
				r(47, sword = v(t(47, "sword", 2, 62, m(0.1, 8, 1, 10, 0, 3), a(13, 1, 1, 0, 0, 0, 0, 3, 0, 0, 0, 4, 0, 12, 0, 2, 0), c(0xFFFFFF, 0xFFC48E, 0xFF0300, 0xFFFFFF))));
				r(48, dispenser = v(t(48, "dispenser", 2, 86, m(0, 10, 0, 3, 0, 0), a(0, 1, 0, 0, 0, 0, 0, 1, 10, 3, 10, 0, 0, 0, 0, 2, 3), c(0xFFFFFF, 0xD7D7D7, 0x727272, 0x95623C))));
				r(49, ocean = v(t(49, "ocean", 1, 73, m(0, 0, 0, 0, 22, 10), a(0, 0, 0, 0, 0, 22, 0, 3, 0, 0, 0, 0, 3, 0, 0, 0, 2), c(0x80FF00, 0x86B5FF, 0x1D7EFF, 0x004DA5))));
				r(50, fish = v(t(50, "fish", 2, 29, m(0, 0, 0, 0, 10, 3), a(0, 0, 0, 0, 0, 4, 0, 1, 0, 0, 0, 0, 1, 0, 10, 1, 1), c(0x6B9F93, 0x5A867C, 0x43655D, 0xADBEDB))));
				r(51, cod = v(t(51, "cod", 2, 27, m(0, 0, 0, 0, 10, 3), a(0, 0, 0, 0, 0, 4, 0, 1, 0, 0, 0, 0, 1, 0, 11, 1, 1), c(0xC6A271, 0xD6C5AD, 0x986D4E, 0xBEA989))));
				r(52, salmon = v(t(52, "salmon", 3, 31, m(0, 0, 0, 4, 10, 2), a(0, 0, 0, 0, 0, 4, 0, 2, 0, 0, 0, 0, 1, 0, 12, 1, 1), c(0xAB3533, 0xFF6763, 0x6B8073, 0xBD928B))));
				r(53, pufferfish = v(t(53, "pufferfish", 4, 36, m(0, 12, 0, 0, 10, 0), a(11, 0, 0, 0, 0, 8, 0, 3, 1, 0, 0, 0, 7, 3, 4, 1, 1), c(0xEBDE39, 0xEBC500, 0xBF9B00, 0x429BBA))));
				r(54, clownfish = v(t(54, "clownfish", 4, 26, m(1, 0, 12, 0, 10, 0), a(0, 0, 0, 0, 0, 5, 0, 9, 0, 0, 0, 0, 1, 0, 7, 2, 1), c(0xE46A22, 0xF46F20, 0xA94B1D, 0xFFDBC5))));
				r(55, spruce = v(t(55, "spruce", 2, 95, m(0, 0, 0, 8, 10, 6), a(0, 0, 8, 0, 0, 1, 0, 2, 0, 0, 0, 0, 1, 0, 1, 0, 1), c(0x795C36, 0x583E1F, 0x23160A, 0x4C784C))));
				r(56, anvil = v(t(56, "anvil", 3, 82, m(0, 2, 0, 10, 0, 2), a(2, 8, 0, 0, 0, 0, 0, 1, 0, 0, 0, 9, 0, 0, 0, 4, 0), c(0xFFFFFF, 0xA9A9A9, 0x909090, 0xA86F18))));
				r(57, obsidian = v(t(57, "obsidian", 3, 78, m(1, 2, 3, 10, 0, 0), a(3, 0, 0, 0, 0, 0, 10, 7, 0, 0, 0, 10, 0, 4, 0, 0, 0), c(0x775599, 0x6029B3, 0x2E095E, 0x0F0033))));
				r(58, seed = v(t(58, "seed", 1, 17, m(0, 0, 0, 0, 10, 3), a(0, 0, 1, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 5, 0, 2), c(0x03B50A, 0x03FF14, 0x037B0A, 0xAAAE36))));
				r(59, enchant = v(t(59, "enchant", 3, 13, m(2, 0, 10, 0, 2, 0), a(0, 6, 0, 5, 0, 0, 0, 7, 0, 0, 1, 0, 0, 0, 0, 12, 2), c(0xD0C2FF, 0xF055FF, 0xC381E3, 0xBE00FF))));
			}

			variants = ImmutableArray.ofList(variants2);
		}

		private void r(int id, VariantFairy[] variants)
		{
			variants2.add(Tuple.of(id, variants));
		}

		private VariantFairy[] v(FairyType[] types)
		{
			return ISuppliterator.ofObjArray(types)
				.map(type -> new VariantFairy(type))
				.toArray(VariantFairy[]::new);
		}

		private FairyType[] t(int id, String name, int rare, int cost, ManaSet manaSet, AbilitySet abilitySet, ColorSet colorSet)
		{
			IntFunction<FairyType> f = rank -> {

				double rateRare = Math.pow(2, (rare + rank - 2) / 4.0);
				double rateVariance = Math.pow(0.5, ((manaSet.shine / manaSet.max + manaSet.fire / manaSet.max + manaSet.wind / manaSet.max
					+ manaSet.gaia / manaSet.max + manaSet.aqua / manaSet.max + manaSet.dark / manaSet.max) - 1) / 5.0);
				double sum = cost * rateRare * rateVariance;
				ManaSet manaSetReal = new ManaSet(
					manaSet.shine * sum / manaSet.sum,
					manaSet.fire * sum / manaSet.sum,
					manaSet.wind * sum / manaSet.sum,
					manaSet.gaia * sum / manaSet.sum,
					manaSet.aqua * sum / manaSet.sum,
					manaSet.dark * sum / manaSet.sum);

				AbilitySet abilitySetReal = new AbilitySet(abilitySet.tuples.suppliterator()
					.map(tuple -> Tuple.of(tuple.x, tuple.y * rateRare))
					.toImmutableArray());

				return new FairyType(ModMirageFairy2019.MODID, id, name, rare, rank, cost, manaSetReal, abilitySetReal, colorSet);
			};
			return ISuppliterator.rangeClosed(1, count)
				.map(i -> f.apply(i))
				.toArray(FairyType[]::new);
		}

		private ManaSet m(double shine, double fire, double wind, double gaia, double aqua, double dark)
		{
			return new ManaSet(shine, fire, wind, gaia, aqua, dark);
		}

		private final AbilitySet a(double... abilities)
		{
			EnumAbilityType[] types = EnumAbilityType.values();
			if (abilities.length != types.length) throw null;
			return new AbilitySet(ISuppliterator.ofObjArray(types)
				.map((t, i) -> Tuple.of((IAbilityType) t, abilities[i]))
				.toImmutableArray());
		}

		private ColorSet c(int skin, int bright, int dark, int hair)
		{
			return new ColorSet(skin, bright, dark, hair);
		}

	}

	public static void init(EventRegistryMod erMod)
	{
		erMod.initCreativeTab.register(() -> {
			ApiFairy.creativeTab = new CreativeTabs("mirageFairy2019.fairy") {
				@Override
				@SideOnly(Side.CLIENT)
				public ItemStack getTabIconItem()
				{
					return ApiFairy.itemStackFairyMain;
				}

				@SideOnly(value = Side.CLIENT)
				public void displayAllRelevantItems(NonNullList<ItemStack> itemStacks)
				{
					for (Tuple<Integer, VariantFairy[]> variant : FairyTypes.variants) {
						for (int i = 0; i < itemFairyList.length; i++) {
							itemStacks.add(variant.y[i].createItemStack());
						}
					}
				}
			};
		});
		erMod.registerItem.register(b -> {

			// 妖精タイプ登録
			new FairyTypes(7).init();

			itemFairyList = new ItemFairy[7];
			ApiFairy.itemFairyList = new Item[7];

			for (int i = 0; i < itemFairyList.length; i++) {

				// 妖精
				ApiFairy.itemFairyList[i] = itemFairyList[i] = new ItemFairy();
				itemFairyList[i].setRegistryName(ModMirageFairy2019.MODID, i == 0 ? "mirage_fairy" : "mirage_fairy_r" + (i + 1));
				itemFairyList[i].setUnlocalizedName("mirageFairyR" + (i + 1));
				itemFairyList[i].setCreativeTab(ApiFairy.creativeTab);
				for (Tuple<Integer, VariantFairy[]> tuple : FairyTypes.variants) {
					itemFairyList[i].registerVariant(tuple.x, tuple.y[i]);
				}
				ForgeRegistries.ITEMS.register(itemFairyList[i]);
				if (ApiMain.side.isClient()) {
					for (Tuple<Integer, VariantFairy> tuple : itemFairyList[i].getVariants()) {
						ModelLoader.setCustomModelResourceLocation(itemFairyList[i], tuple.x, new ModelResourceLocation(new ResourceLocation(ModMirageFairy2019.MODID, "fairy"), null));
					}
				}

			}

		});
		erMod.registerItemColorHandler.register(new Runnable() {
			@SideOnly(Side.CLIENT)
			@Override
			public void run()
			{

				// 妖精のカスタム色
				{
					@SideOnly(Side.CLIENT)
					class ItemColorImpl implements IItemColor
					{

						private final ItemFairy itemMirageFairy;
						private final int colorCloth;

						public ItemColorImpl(ItemFairy itemMirageFairy, int colorCloth)
						{
							this.itemMirageFairy = itemMirageFairy;
							this.colorCloth = colorCloth;
						}

						@Override
						public int colorMultiplier(ItemStack stack, int tintIndex)
						{
							VariantFairy variant = itemMirageFairy.getVariant(stack).orElse(null);
							if (variant == null) return 0xFFFFFF;
							if (tintIndex == 0) return variant.type.colorSet.skin;
							if (tintIndex == 1) return colorCloth;
							if (tintIndex == 2) return variant.type.colorSet.dark;
							if (tintIndex == 3) return variant.type.colorSet.bright;
							if (tintIndex == 4) return variant.type.colorSet.hair;
							return 0xFFFFFF;
						}

					}
					for (int i = 0; i < itemFairyList.length; i++) {
						Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new ItemColorImpl(itemFairyList[i], new int[] {
							0xFF8888,
							0x8888FF,
							0x88FF88,
							0xFFFF88,
							0x111111,
							0xFFFFFF,
							0x88FFFF,
						}[i]), itemFairyList[i]);
					}
				}

			}
		});
		erMod.createItemStack.register(ic -> {

			ApiFairy.itemStackFairyMain = FairyTypes.magentaglazedterracotta[0].createItemStack();

			// 妖精の鉱石辞書
			for (Tuple<Integer, VariantFairy[]> variant : FairyTypes.variants) {
				for (int i = 0; i <= itemFairyList.length - 1; i++) {
					for (Tuple<IAbilityType, Double> tuple : variant.y[i].type.abilitySet.tuples) {
						if (tuple.y >= 10) {
							OreDictionary.registerOre(
								"mirageFairy2019FairyAbility" + Utils.toUpperCaseHead(tuple.x.getName()),
								variant.y[i].createItemStack());
						}
					}
				}
			}

		});
		erMod.addRecipe.register(() -> {

			// 凝縮・分散レシピ
			for (Tuple<Integer, VariantFairy[]> tuple : FairyTypes.variants) {

				for (int i = 0; i < itemFairyList.length - 1; i++) {

					// 凝縮
					GameRegistry.addShapelessRecipe(
						new ResourceLocation(ModMirageFairy2019.MODID + ":" + "condense_r" + i + "_fairy_" + tuple.y[i].type.name),
						new ResourceLocation(ModMirageFairy2019.MODID + ":" + "condense_r" + i + "_fairy_" + tuple.y[i].type.name),
						tuple.y[i + 1].createItemStack(),
						Ingredient.fromStacks(tuple.y[i].createItemStack()),
						Ingredient.fromStacks(tuple.y[i].createItemStack()),
						Ingredient.fromStacks(tuple.y[i].createItemStack()),
						Ingredient.fromStacks(tuple.y[i].createItemStack()),
						Ingredient.fromStacks(tuple.y[i].createItemStack()),
						Ingredient.fromStacks(tuple.y[i].createItemStack()),
						Ingredient.fromStacks(tuple.y[i].createItemStack()),
						Ingredient.fromStacks(tuple.y[i].createItemStack()));

					// 分散
					GameRegistry.addShapelessRecipe(
						new ResourceLocation(ModMirageFairy2019.MODID + ":" + "decondense_r" + i + "_fairy_" + tuple.y[i].type.name),
						new ResourceLocation(ModMirageFairy2019.MODID + ":" + "decondense_r" + i + "_fairy_" + tuple.y[i].type.name),
						tuple.y[i].createItemStack(8),
						Ingredient.fromStacks(tuple.y[i + 1].createItemStack()));

				}

			}

		});
	}

}
