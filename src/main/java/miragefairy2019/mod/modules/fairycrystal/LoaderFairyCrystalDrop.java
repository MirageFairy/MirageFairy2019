package miragefairy2019.mod.modules.fairycrystal;

import static miragefairy2019.mod.modules.fairy.ModuleFairy.FairyTypes.*;

import java.util.List;

import miragefairy2019.mod.api.ApiFairyCrystal;
import miragefairy2019.mod.api.fairycrystal.DropFixed;
import miragefairy2019.mod.api.fairycrystal.IDrop;
import miragefairy2019.mod.api.fairycrystal.IRightClickDrop;
import miragefairy2019.mod.api.fairycrystal.RightClickDrops;
import miragefairy2019.mod.modules.fairy.VariantFairy;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockDoublePlant.EnumPlantType;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;

public class LoaderFairyCrystalDrop
{

	public static void loadFairyCrystalDrop()
	{
		List<IRightClickDrop> d = ApiFairyCrystal.dropsFairyCrystal;

		// コモン
		{

		}

		// 限定高確率ドロップ
		{

		}

	}

	private static IDrop r(VariantFairy variantFairy)
	{
		return new DropFixed(variantFairy.createItemStack(), 0.1 * Math.pow(0.1, variantFairy.type.rare - 1));
	}

	private static IDrop d(VariantFairy variantFairy, double weight)
	{
		return new DropFixed(variantFairy.createItemStack(), weight);
	}

	private static boolean time(World world, int min, int max)
	{
		return world.provider.isSurfaceWorld() && min <= (world.getWorldTime() + 6000) % 24000 && (world.getWorldTime() + 6000) % 24000 <= max;
	}

}
