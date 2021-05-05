package miragefairy2019.mod.modules.ore;

import static miragefairy2019.mod.lib.Configurator.*;

import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.api.ore.ApiOre;
import miragefairy2019.mod.lib.EventRegistryMod;
import miragefairy2019.mod.lib.Monad;
import miragefairy2019.mod.lib.multi.IBlockVariant;
import miragefairy2019.mod.lib.multi.ItemMultiMaterial;
import miragefairy2019.mod.lib.multi.ItemVariantMaterial;
import miragefairy2019.mod.modules.ore.material.BlockMaterials;
import miragefairy2019.mod.modules.ore.material.EnumVariantMaterials1;
import miragefairy2019.mod.modules.ore.material.ItemBlockMaterials;
import miragefairy2019.mod.modules.ore.ore.BlockOre;
import miragefairy2019.mod.modules.ore.ore.EnumVariantOre1;
import miragefairy2019.mod.modules.ore.ore.EnumVariantOre2;
import miragefairy2019.mod.modules.ore.ore.IBlockVariantOre;
import miragefairy2019.mod.modules.ore.ore.ItemBlockOre;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ModuleOre
{

	public static Fluid fluidMiragiumWater;

	public static BlockOre<EnumVariantOre1> blockOre1;
	public static BlockOre<EnumVariantOre2> blockOre2;
	public static BlockMaterials<EnumVariantMaterials1> blockMaterials1;
	public static BlockFluidMiragiumWater blockFluidMiragiumWater;

	public static ItemMultiMaterial<ItemVariantMaterial> itemMaterials;
	public static ItemBlockOre<EnumVariantOre1> itemBlockOre1;
	public static ItemBlockOre<EnumVariantOre2> itemBlockOre2;
	public static ItemBlockMaterials<EnumVariantMaterials1> itemBlockMaterials1;
	public static ItemBlock itemFluidMiragiumWater;

	public static void init(EventRegistryMod erMod)
	{

		LoaderOreSeedDrop.loadOreSeedDrop();

		// マテリアル
		item(erMod, ItemMultiMaterial<ItemVariantMaterial>::new, new ResourceLocation(ModMirageFairy2019.MODID, "materials"), "materials")
			.bind(onRegisterItem(item -> ApiOre.itemMaterials1 = itemMaterials = item))
			.bind(setCreativeTab(() -> ApiMain.creativeTab()))
			.bind(c -> {

				itemVariant(c.erMod, c, 0, () -> new ItemVariantMaterial("apatite_gem", "gemApatite")).bind(addOreName("gemApatite"));
				itemVariant(c.erMod, c, 1, () -> new ItemVariantMaterial("fluorite_gem", "gemFluorite")).bind(addOreName("gemFluorite"));
				itemVariant(c.erMod, c, 2, () -> new ItemVariantMaterial("sulfur_gem", "gemSulfur")).bind(addOreName("gemSulfur"));
				itemVariant(c.erMod, c, 3, () -> new ItemVariantMaterial("miragium_dust", "dustMiragium")).bind(addOreName("dustMiragium"));
				itemVariant(c.erMod, c, 4, () -> new ItemVariantMaterial("miragium_tiny_dust", "dustTinyMiragium")).bind(addOreName("dustTinyMiragium"));
				itemVariant(c.erMod, c, 5, () -> new ItemVariantMaterial("miragium_ingot", "ingotMiragium")).bind(addOreName("ingotMiragium"));
				itemVariant(c.erMod, c, 6, () -> new ItemVariantMaterial("cinnabar_gem", "gemCinnabar")).bind(addOreName("gemCinnabar"));
				itemVariant(c.erMod, c, 7, () -> new ItemVariantMaterial("moonstone_gem", "gemMoonstone")).bind(addOreName("gemMoonstone"));
				itemVariant(c.erMod, c, 8, () -> new ItemVariantMaterial("magnetite_gem", "gemMagnetite")).bind(addOreName("gemMagnetite"));
				itemVariant(c.erMod, c, 9, () -> new ItemVariantMaterial("saltpeter_gem", "gemSaltpeter")).bind(addOreName("gemSaltpeter"));
				itemVariant(c.erMod, c, 10, () -> new ItemVariantMaterial("pyrope_gem", "gemPyrope")).bind(addOreName("gemPyrope"));
				itemVariant(c.erMod, c, 11, () -> new ItemVariantMaterial("smithsonite_gem", "gemSmithsonite")).bind(addOreName("gemSmithsonite"));
				itemVariant(c.erMod, c, 12, () -> new ItemVariantMaterial("miragium_rod", "rodMiragium")).bind(addOreName("rodMiragium"));
				itemVariant(c.erMod, c, 13, () -> new ItemVariantMaterial("miragium_nugget", "nuggetMiragium")).bind(addOreName("nuggetMiragium"));
				itemVariant(c.erMod, c, 14, () -> new ItemVariantMaterial("nephrite_gem", "gemNephrite")).bind(addOreName("gemNephrite"));
				itemVariant(c.erMod, c, 15, () -> new ItemVariantMaterial("topaz_gem", "gemTopaz")).bind(addOreName("gemTopaz"));
				itemVariant(c.erMod, c, 16, () -> new ItemVariantMaterial("tourmaline_gem", "gemTourmaline")).bind(addOreName("gemTourmaline"));

				erMod.registerItem.register(b -> {
					if (ApiMain.side().isClient()) c.get().setCustomModelResourceLocations();
				});

				return Monad.of(c);
			});

		class ItemVariantFilledBucket extends ItemVariantMaterial
		{

			public final boolean vaporizable;
			public final Supplier<Optional<IBlockState>> soBlockStateFluid;

			public ItemVariantFilledBucket(String registryName, String unlocalizedName, boolean vaporizable, Supplier<Optional<IBlockState>> soBlockStateFluid)
			{
				super(registryName, unlocalizedName);
				this.vaporizable = vaporizable;
				this.soBlockStateFluid = soBlockStateFluid;
			}

		}

		class ItemFilledBucket extends ItemMultiMaterial<ItemVariantFilledBucket>
		{

			public ItemFilledBucket()
			{
				this.maxStackSize = 1;
			}

			//

			@Override
			public boolean hasContainerItem(ItemStack itemStack)
			{
				return true;
			}

			@Override
			public ItemStack getContainerItem(ItemStack itemStack)
			{
				return new ItemStack(Items.BUCKET);
			}

			//

			@Override
			public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
			{

				// 使用アイテム
				ItemStack itemStack = playerIn.getHeldItem(handIn);

				// 液体を無視する視線判定
				RayTraceResult rayTraceResult = rayTrace(worldIn, playerIn, false);

				// Forgeオーバーライド
				ActionResult<ItemStack> result = ForgeEventFactory.onBucketUse(playerIn, worldIn, itemStack, rayTraceResult);
				if (result != null) return result;

				// 視線判定でブロックに当たらなかった場合は失敗
				if (rayTraceResult == null) return new ActionResult<>(EnumActionResult.PASS, itemStack);
				if (rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) return new ActionResult<>(EnumActionResult.PASS, itemStack);

				// 視線判定で当たった座標
				BlockPos blockPos = rayTraceResult.getBlockPos();

				// 視線判定で当たった座標が編集不可の場合は失敗
				if (!worldIn.isBlockModifiable(playerIn, blockPos)) return new ActionResult<>(EnumActionResult.FAIL, itemStack);

				// 「雑草とかの上面」以外に対して使った場合は手前の座標を選択する
				// ただし、実際には雑草は視線判定をすり抜ける
				if (!(worldIn.getBlockState(blockPos).getBlock().isReplaceable(worldIn, blockPos) && rayTraceResult.sideHit == EnumFacing.UP)) {
					blockPos = blockPos.offset(rayTraceResult.sideHit);
				}

				// その座標をプレイヤーが編集できない場合は失敗
				if (!playerIn.canPlayerEdit(blockPos, rayTraceResult.sideHit, itemStack)) return new ActionResult<>(EnumActionResult.FAIL, itemStack);

				// 設置試行
				boolean result2 = tryPlaceContainedLiquid(playerIn, itemStack, worldIn, blockPos);

				// 設置できなかった場合は失敗
				if (!result2) return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemStack);

				// 統計関連
				if (playerIn instanceof EntityPlayerMP) {
					CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) playerIn, blockPos, itemStack);
				}
				playerIn.addStat(StatList.getObjectUseStats(this));

				// 使用後アイテム
				ItemStack itemStackResult = !playerIn.capabilities.isCreativeMode
					? new ItemStack(Items.BUCKET)
					: itemStack;

				// 成功
				return new ActionResult<>(EnumActionResult.SUCCESS, itemStackResult);
			}

			public boolean tryPlaceContainedLiquid(@Nullable EntityPlayer player, ItemStack itemStack, World worldIn, BlockPos posIn)
			{

				// 対象ブロックステート
				IBlockState blockStateTarget = worldIn.getBlockState(posIn);
				boolean solid = blockStateTarget.getMaterial().isSolid();
				boolean replaceable = blockStateTarget.getBlock().isReplaceable(worldIn, posIn);

				// 対象座標が埋まっていた場合は失敗
				if (!worldIn.isAirBlock(posIn) && solid && !replaceable) return false;

				// 設置ブロック
				IBlockState blockStateFluid = getVariant(itemStack).map(v -> v.soBlockStateFluid.get()).orElse(Optional.empty()).orElse(null);
				if (blockStateFluid == null) return false;

				if (getVariant(itemStack).map(v -> v.vaporizable).orElse(false) && worldIn.provider.doesWaterVaporize()) {
					// 気化する場合

					// エフェクト
					worldIn.playSound(player, posIn, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);
					for (int i = 0; i < 8; ++i) {
						worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE,
							posIn.getX() + Math.random(),
							posIn.getY() + Math.random(),
							posIn.getZ() + Math.random(),
							0, 0, 0);
					}

					// 成功
					return true;
				} else {
					// 設置可能な場合

					// エフェクト
					worldIn.playSound(player, posIn, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1, 1);

					// 設置
					if (!worldIn.isRemote) {
						if ((!solid || replaceable) && !blockStateTarget.getMaterial().isLiquid()) worldIn.destroyBlock(posIn, true);
					}
					worldIn.setBlockState(posIn, blockStateFluid, 11);

					// 成功
					return true;
				}
			}

			/* TODO
			@Override
			public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable net.minecraft.nbt.NBTTagCompound nbt)
			{
				if (this.getClass() == ItemBucket.class) {
					return new net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper(stack);
				} else {
					return super.initCapabilities(stack, nbt);
				}
			}
			*/

		}

		// 中身入りバケツ
		item(erMod, () -> new ItemFilledBucket(), new ResourceLocation(ModMirageFairy2019.MODID, "filled_bucket"), "filledBucket")
			.bind(setCreativeTab(() -> ApiMain.creativeTab()))
			.peek(c -> {

				// TODO 流体2個追加　ビンにcontainer追加
				itemVariant(c.erMod, c, 0, () -> new ItemVariantFilledBucket("miragium_water_bucket", "bucketMiragiumWater", true, () -> Optional.of(blockFluidMiragiumWater.getDefaultState())))
					.bind(addOreName("bucketMiragiumWater"))
					.bind(addOreName("container1000MiragiumWater"))
					.peek(cv -> {
						MinecraftForge.EVENT_BUS.register(new Object() {
							@SubscribeEvent
							public void accept(FillBucketEvent event)
							{
								if (event.getResult() != Result.DEFAULT) return;
								if (event.getEmptyBucket().getItem() == Items.BUCKET) {

									// 使用アイテム
									ItemStack itemStack = event.getEmptyBucket();

									// 視線判定
									RayTraceResult rayTraceResult = event.getTarget();

									// 視線判定でブロックに当たらなかった場合は失敗
									if (rayTraceResult == null) return;
									if (rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) return;

									// 視線判定で当たった座標
									BlockPos blockPos = rayTraceResult.getBlockPos();

									// 視線判定で当たった座標が編集不可の場合は失敗
									if (!event.getWorld().isBlockModifiable(event.getEntityPlayer(), blockPos)) return;

									// その座標をプレイヤーが編集できない場合は失敗
									if (!event.getEntityPlayer().canPlayerEdit(blockPos.offset(rayTraceResult.sideHit), rayTraceResult.sideHit, itemStack)) return;

									// 対象が指定フルイドブロックでない場合は失敗
									IBlockState blockState = event.getWorld().getBlockState(blockPos);
									if (!blockState.equals(blockFluidMiragiumWater.getDefaultState())) return;

									// エフェクト
									event.getEntityPlayer().playSound(SoundEvents.ITEM_BUCKET_FILL, 1, 1);

									// 統計
									event.getEntityPlayer().addStat(StatList.getObjectUseStats(c.get()));

									// 破壊
									event.getWorld().setBlockState(blockPos, Blocks.AIR.getDefaultState(), 11);

									// 成功
									event.setFilledBucket(cv.get().createItemStack());
									event.setResult(Result.ALLOW);

								}
							}
						});
					});
				itemVariant(c.erMod, c, 1, () -> new ItemVariantFilledBucket("mirage_flower_extract_bucket", "bucketMirageFlowerExtract", true, () -> Optional.empty()))
					.bind(addOreName("bucketMirageFlowerExtract"))
					.bind(addOreName("container1000MirageFlowerExtract"));
				itemVariant(c.erMod, c, 2, () -> new ItemVariantFilledBucket("mirage_flower_oil_bucket", "bucketMirageFlowerOil", true, () -> Optional.empty()))
					.bind(addOreName("bucketMirageFlowerOil"))
					.bind(addOreName("container1000MirageFlowerOil"));
				/*
				itemVariant(c.erMod, c, 3, () -> new ItemVariantFilledBucket("water_bucket", "bucketWater", true, () -> Optional.empty()))
					.bind(addOreName("bucketWater"))
					.bind(addOreName("container1000Water"));
					*/

				erMod.registerItem.register(b -> {
					if (ApiMain.side().isClient()) c.get().setCustomModelResourceLocations();
				});

			});

		erMod.registerBlock.register(b -> {

			// 妖水フルイド
			ApiOre.fluidMiragiumWater = fluidMiragiumWater = new Fluid(
				"miragium_water",
				new ResourceLocation(ModMirageFairy2019.MODID, "blocks/miragium_water_still"),
				new ResourceLocation(ModMirageFairy2019.MODID, "blocks/miragium_water_flow"),
				new ResourceLocation(ModMirageFairy2019.MODID, "blocks/miragium_water_overlay"));
			fluidMiragiumWater.setViscosity(500);
			FluidRegistry.registerFluid(fluidMiragiumWater);
			FluidRegistry.addBucketForFluid(fluidMiragiumWater);

			//

			// 鉱石1
			ApiOre.blockOre1 = blockOre1 = new BlockOre<>(EnumVariantOre1.variantList);
			blockOre1.setRegistryName(ModMirageFairy2019.MODID, "ore1");
			blockOre1.setCreativeTab(ApiMain.creativeTab());
			ForgeRegistries.BLOCKS.register(blockOre1);

			// 鉱石2
			ApiOre.blockOre2 = blockOre2 = new BlockOre<>(EnumVariantOre2.variantList);
			blockOre2.setRegistryName(ModMirageFairy2019.MODID, "ore2");
			blockOre2.setCreativeTab(ApiMain.creativeTab());
			ForgeRegistries.BLOCKS.register(blockOre2);

			// ブロック
			ApiOre.blockMaterials1 = blockMaterials1 = new BlockMaterials<>(EnumVariantMaterials1.variantList);
			blockMaterials1.setRegistryName(ModMirageFairy2019.MODID, "materials1");
			blockMaterials1.setCreativeTab(ApiMain.creativeTab());
			ForgeRegistries.BLOCKS.register(blockMaterials1);

			// 妖水ブロック
			ApiOre.blockFluidMiragiumWater = blockFluidMiragiumWater = new BlockFluidMiragiumWater(fluidMiragiumWater);
			blockFluidMiragiumWater.setRegistryName(ModMirageFairy2019.MODID, "miragium_water");
			blockFluidMiragiumWater.setUnlocalizedName("miragiumWater");
			blockFluidMiragiumWater.setCreativeTab(ApiMain.creativeTab());
			ForgeRegistries.BLOCKS.register(blockFluidMiragiumWater);
			if (ApiMain.side().isClient()) {
				new Object() {
					@SideOnly(Side.CLIENT)
					public void run()
					{
						ModelLoader.setCustomStateMapper(blockFluidMiragiumWater, new StateMapperBase() {
							@Override
							protected ModelResourceLocation getModelResourceLocation(IBlockState var1)
							{
								return new ModelResourceLocation(new ResourceLocation(ModMirageFairy2019.MODID, "miragium_water"), "fluid");
							}
						});
					}
				}.run();
			}

		});
		erMod.registerItem.register(b -> {

			// 鉱石1
			ApiOre.itemBlockOre1 = itemBlockOre1 = new ItemBlockOre<>(blockOre1);
			itemBlockOre1.setRegistryName(ModMirageFairy2019.MODID, "ore1");
			itemBlockOre1.setUnlocalizedName("ore1");
			itemBlockOre1.setCreativeTab(ApiMain.creativeTab());
			ForgeRegistries.ITEMS.register(itemBlockOre1);
			if (ApiMain.side().isClient()) {
				for (IBlockVariantOre variant : blockOre1.variantList) {
					ModelLoader.setCustomModelResourceLocation(
						itemBlockOre1,
						variant.getMetadata(),
						new ModelResourceLocation(new ResourceLocation(itemBlockOre1.getRegistryName().getResourceDomain(), variant.getResourceName()), null));
				}
			}

			// 鉱石2
			ApiOre.itemBlockOre2 = itemBlockOre2 = new ItemBlockOre<>(blockOre2);
			itemBlockOre2.setRegistryName(ModMirageFairy2019.MODID, "ore2");
			itemBlockOre2.setUnlocalizedName("ore2");
			itemBlockOre2.setCreativeTab(ApiMain.creativeTab());
			ForgeRegistries.ITEMS.register(itemBlockOre2);
			if (ApiMain.side().isClient()) {
				for (IBlockVariantOre variant : blockOre2.variantList) {
					ModelLoader.setCustomModelResourceLocation(
						itemBlockOre2,
						variant.getMetadata(),
						new ModelResourceLocation(new ResourceLocation(itemBlockOre2.getRegistryName().getResourceDomain(), variant.getResourceName()), null));
				}
			}

			// ブロック
			ApiOre.itemBlockMaterials1 = itemBlockMaterials1 = new ItemBlockMaterials<>(blockMaterials1);
			itemBlockMaterials1.setRegistryName(ModMirageFairy2019.MODID, "materials1");
			itemBlockMaterials1.setUnlocalizedName("materials1");
			itemBlockMaterials1.setCreativeTab(ApiMain.creativeTab());
			ForgeRegistries.ITEMS.register(itemBlockMaterials1);
			if (ApiMain.side().isClient()) {
				for (IBlockVariant variant : blockMaterials1.variantList) {
					ModelLoader.setCustomModelResourceLocation(
						itemBlockMaterials1,
						variant.getMetadata(),
						new ModelResourceLocation(new ResourceLocation(itemBlockMaterials1.getRegistryName().getResourceDomain(), variant.getResourceName()), null));
				}
			}

			// 妖水アイテム
			ApiOre.itemFluidMiragiumWater = itemFluidMiragiumWater = new ItemBlock(blockFluidMiragiumWater) {
				public void getSubItems(CreativeTabs p_getSubItems_1_, NonNullList<ItemStack> p_getSubItems_2_)
				{
					if (this.isInCreativeTab(p_getSubItems_1_)) {
						this.block.getSubBlocks(p_getSubItems_1_, p_getSubItems_2_);
					}
				}
			};
			itemFluidMiragiumWater.setRegistryName(ModMirageFairy2019.MODID, "miragium_water");
			itemFluidMiragiumWater.setUnlocalizedName("miragiumWater");
			itemFluidMiragiumWater.setCreativeTab(ApiMain.creativeTab());
			ForgeRegistries.ITEMS.register(itemFluidMiragiumWater);
			if (ApiMain.side().isClient()) {
				new Object() {
					@SideOnly(Side.CLIENT)
					public void run()
					{
						ModelLoader.setCustomMeshDefinition(itemFluidMiragiumWater, new ItemMeshDefinition() {
							@Override
							public ModelResourceLocation getModelLocation(ItemStack var1)
							{
								return new ModelResourceLocation(new ResourceLocation(ModMirageFairy2019.MODID, "miragium_water"), "fluid");
							}
						});
					}
				}.run();
			}

		});
		erMod.createItemStack.register(ic -> {
			for (EnumVariantMaterials1 variant : EnumVariantMaterials1.values()) {
				OreDictionary.registerOre(variant.oreName, new ItemStack(itemBlockMaterials1, 1, variant.metadata));
			}
			OreDictionary.registerOre("container1000Water", Items.WATER_BUCKET);
			OreDictionary.registerOre("container1000Lava", Items.LAVA_BUCKET);
			OreDictionary.registerOre("wool", new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE));
			OreDictionary.registerOre("ice", new ItemStack(Blocks.ICE));
			OreDictionary.registerOre("gemCoal", new ItemStack(Items.COAL, 1, 0));
		});
		erMod.addRecipe.register(() -> {

			// 製錬
			//GameRegistry.addSmelting(ApiOre.itemStackDustMiragium, ApiOre.itemStackIngotMiragium, 0);

		});
	}

}
