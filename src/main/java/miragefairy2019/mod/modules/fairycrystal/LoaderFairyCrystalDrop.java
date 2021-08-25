package miragefairy2019.mod.modules.fairycrystal;

import miragefairy2019.mod.api.ApiFairyCrystal;
import miragefairy2019.mod.api.fairycrystal.DropFixed;
import miragefairy2019.mod.api.fairycrystal.IDrop;
import miragefairy2019.mod.api.fairycrystal.IRightClickDrop;
import miragefairy2019.mod.api.fairycrystal.RightClickDrops;
import miragefairy2019.mod.api.main.ApiMain;
import miragefairy2019.mod.modules.fairy.FairyTypes;
import miragefairy2019.mod.modules.fairy.VariantFairy;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockDoublePlant.EnumPlantType;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class LoaderFairyCrystalDrop {

    public static void loadFairyCrystalDrop() {
        List<IRightClickDrop> d = ApiFairyCrystal.dropsFairyCrystal;

        // コモン
        {
            d.add(RightClickDrops.world(r(FairyTypes.instance.getWater().getMain()), (w, p) -> w.provider.isSurfaceWorld()));

            d.add(RightClickDrops.world(r(FairyTypes.instance.getStone().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getDirt().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getSand().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getGravel().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getIron().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getGold().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getDiamond().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getEmerald().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getMagnetite().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getApatite().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getFluorite().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getSulfur().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getCinnabar().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getMoonstone().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getPyrope().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getSmithsonite().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getRedstone().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getLapislazuli().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getObsidian().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getCoal().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getIce().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getNephrite().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getTourmaline().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getTopaz().getMain()), (w, p) -> w.provider.isSurfaceWorld()));

            d.add(RightClickDrops.world(r(FairyTypes.instance.getSpider().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getChicken().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getSkeleton().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getZombie().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getCreeper().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getFish().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getCod().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getSalmon().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getPufferfish().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getClownfish().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getVillager().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getCow().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getPig().getMain()), (w, p) -> w.provider.isSurfaceWorld()));

            d.add(RightClickDrops.world(r(FairyTypes.instance.getWheat().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getLilac().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getApple().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getCarrot().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getCactus().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getSpruce().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getSeed().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getPoisonouspotato().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
            d.add(RightClickDrops.world(r(FairyTypes.instance.getMelon().getMain()), (w, p) -> w.provider.isSurfaceWorld()));
        }
        {
            d.add(RightClickDrops.biomeTypes(r(FairyTypes.instance.getLava().getMain()), BiomeDictionary.Type.NETHER));
            d.add(RightClickDrops.biomeTypes(r(FairyTypes.instance.getFire().getMain()), BiomeDictionary.Type.NETHER));

            d.add(RightClickDrops.biomeTypes(r(FairyTypes.instance.getGlowstone().getMain()), BiomeDictionary.Type.NETHER));
        }
        {
            d.add(RightClickDrops.biomeTypes(r(FairyTypes.instance.getEnderman().getMain()), BiomeDictionary.Type.END));
            d.add(RightClickDrops.biomeTypes(r(FairyTypes.instance.getEnderdragon().getMain()), BiomeDictionary.Type.END));
        }

        // 限定高確率ドロップ
        {
            d.add(RightClickDrops.fixed(d(FairyTypes.instance.getAir().getMain(), 1)));

            d.add(RightClickDrops.blocks(d(FairyTypes.instance.getWater().getMain(), 0.3), Blocks.WATER, Blocks.FLOWING_WATER));
            d.add(RightClickDrops.items(d(FairyTypes.instance.getWater().getMain(), 0.3), Items.WATER_BUCKET));
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.getLava().getMain(), 0.1), Blocks.LAVA, Blocks.FLOWING_LAVA));
            d.add(RightClickDrops.items(d(FairyTypes.instance.getLava().getMain(), 0.3), Items.LAVA_BUCKET));
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.getFire().getMain(), 0.1), Blocks.FIRE));

            d.add(RightClickDrops.world(d(FairyTypes.instance.getThunder().getMain(), 0.01), (w, p) -> w.provider.isSurfaceWorld() && w.canSeeSky(p) && w.isRainingAt(p) && w.isThundering()));
            d.add(RightClickDrops.world(d(FairyTypes.instance.getSun().getMain(), 0.0001), (w, p) -> w.provider.isSurfaceWorld() && w.canSeeSky(p) && time(w, 6000, 18000) && !w.isRainingAt(p)));
            d.add(RightClickDrops.world(d(FairyTypes.instance.getMoon().getMain(), 0.0001), (w, p) -> w.provider.isSurfaceWorld() && w.canSeeSky(p) && (time(w, 19000, 24000) || time(w, 0, 5000)) && !w.isRainingAt(p)));
            d.add(RightClickDrops.world(d(FairyTypes.instance.getStar().getMain(), 0.0003), (w, p) -> w.provider.isSurfaceWorld() && w.canSeeSky(p) && (time(w, 19000, 24000) || time(w, 0, 5000)) && !w.isRainingAt(p)));

            d.add(RightClickDrops.blocks(d(FairyTypes.instance.getStone().getMain(), 0.3), Blocks.STONE, Blocks.COBBLESTONE));
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.getDirt().getMain(), 0.3), Blocks.DIRT, Blocks.GRASS));
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.getSand().getMain(), 0.3), Blocks.SAND, Blocks.SANDSTONE, Blocks.RED_SANDSTONE));
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.getGravel().getMain(), 0.1), Blocks.GRAVEL));
            d.add(RightClickDrops.ores(d(FairyTypes.instance.getIron().getMain(), 0.1), m("Iron")));
            d.add(RightClickDrops.ores(d(FairyTypes.instance.getGold().getMain(), 0.03), m("Gold")));
            d.add(RightClickDrops.ores(d(FairyTypes.instance.getDiamond().getMain(), 0.01), m("Diamond")));
            d.add(RightClickDrops.ores(d(FairyTypes.instance.getEmerald().getMain(), 0.03), m("Emerald")));
            d.add(RightClickDrops.ores(d(FairyTypes.instance.getMagnetite().getMain(), 0.1), m("Magnetite")));
            d.add(RightClickDrops.ores(d(FairyTypes.instance.getApatite().getMain(), 0.03), m("Apatite")));
            d.add(RightClickDrops.ores(d(FairyTypes.instance.getFluorite().getMain(), 0.01), m("Fluorite")));
            d.add(RightClickDrops.ores(d(FairyTypes.instance.getSulfur().getMain(), 0.03), m("Sulfur")));
            d.add(RightClickDrops.ores(d(FairyTypes.instance.getCinnabar().getMain(), 0.01), m("Cinnabar")));
            d.add(RightClickDrops.ores(d(FairyTypes.instance.getMoonstone().getMain(), 0.003), m("Moonstone")));
            d.add(RightClickDrops.ores(d(FairyTypes.instance.getPyrope().getMain(), 0.01), m("Pyrope")));
            d.add(RightClickDrops.ores(d(FairyTypes.instance.getSmithsonite().getMain(), 0.1), m("Smithsonite")));
            d.add(RightClickDrops.ores(d(FairyTypes.instance.getRedstone().getMain(), 0.1), m("Redstone")));
            d.add(RightClickDrops.ores(d(FairyTypes.instance.getLapislazuli().getMain(), 0.1), m("Lapis")));
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.getObsidian().getMain(), 0.1), Blocks.OBSIDIAN));
            d.add(RightClickDrops.ores(d(FairyTypes.instance.getGlowstone().getMain(), 0.1), m("Glowstone")));
            d.add(RightClickDrops.ores(d(FairyTypes.instance.getGlowstone().getMain(), 0.1), "glowstone"));
            d.add(RightClickDrops.ingredients(d(FairyTypes.instance.getCoal().getMain(), 0.1), Ingredient.fromStacks(new ItemStack(Items.COAL))));
            d.add(RightClickDrops.ores(d(FairyTypes.instance.getCoal().getMain(), 0.1), m("Coal")));
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.getIce().getMain(), 0.3), Blocks.ICE));
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.getPackedice().getMain(), 0.01), Blocks.PACKED_ICE));
            d.add(RightClickDrops.ores(d(FairyTypes.instance.getNephrite().getMain(), 0.03), m("Nephrite")));
            d.add(RightClickDrops.ores(d(FairyTypes.instance.getTourmaline().getMain(), 0.01), m("Tourmaline")));
            d.add(RightClickDrops.ores(d(FairyTypes.instance.getTopaz().getMain(), 0.01), m("Topaz")));

            d.add(RightClickDrops.classEntities(d(FairyTypes.instance.getEnderman().getMain(), 0.03), EntityEnderman.class));
            d.add(RightClickDrops.classEntities(d(FairyTypes.instance.getSpider().getMain(), 0.1), EntitySpider.class));
            d.add(RightClickDrops.classEntities(d(FairyTypes.instance.getEnderdragon().getMain(), 0.1), EntityDragon.class));
            d.add(RightClickDrops.classEntities(d(FairyTypes.instance.getChicken().getMain(), 0.1), EntityChicken.class));
            d.add(RightClickDrops.classEntities(d(FairyTypes.instance.getSkeleton().getMain(), 0.3), EntitySkeleton.class));
            d.add(RightClickDrops.classEntities(d(FairyTypes.instance.getZombie().getMain(), 0.3), EntityZombie.class));
            d.add(RightClickDrops.classEntities(d(FairyTypes.instance.getWitherskeleton().getMain(), 0.03), EntityWitherSkeleton.class));
            d.add(RightClickDrops.classEntities(d(FairyTypes.instance.getWither().getMain(), 0.01), EntityWither.class));
            d.add(RightClickDrops.classEntities(d(FairyTypes.instance.getCreeper().getMain(), 0.1), EntityCreeper.class));
            d.add(RightClickDrops.items(d(FairyTypes.instance.getFish().getMain(), 0.3), Items.FISH));
            d.add(RightClickDrops.ingredients(d(FairyTypes.instance.getCod().getMain(), 0.1), Ingredient.fromStacks(new ItemStack(Items.FISH, 1, 0))));
            d.add(RightClickDrops.ingredients(d(FairyTypes.instance.getSalmon().getMain(), 0.1), Ingredient.fromStacks(new ItemStack(Items.FISH, 1, 1))));
            d.add(RightClickDrops.ingredients(d(FairyTypes.instance.getPufferfish().getMain(), 0.03), Ingredient.fromStacks(new ItemStack(Items.FISH, 1, 3))));
            d.add(RightClickDrops.ingredients(d(FairyTypes.instance.getClownfish().getMain(), 0.03), Ingredient.fromStacks(new ItemStack(Items.FISH, 1, 2))));
            d.add(RightClickDrops.entity(d(FairyTypes.instance.getVillager().getMain(), 0.3), EntityVillager.class, e -> true));
            d.add(RightClickDrops.entity(d(FairyTypes.instance.getLibrarian().getMain(), 0.1), EntityVillager.class, e -> e.getProfessionForge().getRegistryName().equals(new ResourceLocation("minecraft:librarian"))));
            d.add(RightClickDrops.items(d(FairyTypes.instance.getNetherstar().getMain(), 0.01), Items.NETHER_STAR));
            d.add(RightClickDrops.classEntities(d(FairyTypes.instance.getGolem().getMain(), 0.1), EntityIronGolem.class));
            d.add(RightClickDrops.classEntities(d(FairyTypes.instance.getCow().getMain(), 0.1), EntityCow.class));
            d.add(RightClickDrops.classEntities(d(FairyTypes.instance.getPig().getMain(), 0.1), EntityPig.class));

            d.add(RightClickDrops.blocks(d(FairyTypes.instance.getWheat().getMain(), 0.1), Blocks.WHEAT, Blocks.HAY_BLOCK));
            d.add(RightClickDrops.blockStates(d(FairyTypes.instance.getLilac().getMain(), 0.03), Blocks.DOUBLE_PLANT.getDefaultState().withProperty(BlockDoublePlant.VARIANT, EnumPlantType.SYRINGA)));
            d.add(RightClickDrops.items(d(FairyTypes.instance.getApple().getMain(), 0.1), Items.APPLE, Items.GOLDEN_APPLE));
            d.add(RightClickDrops.items(d(FairyTypes.instance.getCarrot().getMain(), 0.03), Items.CARROT, Items.CARROT_ON_A_STICK, Items.GOLDEN_CARROT));
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.getCactus().getMain(), 0.1), Blocks.CACTUS));
            d.add(RightClickDrops.blockStates(d(FairyTypes.instance.getSpruce().getMain(), 0.1),
                    Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE),
                    Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE),
                    Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE)));
            d.add(RightClickDrops.items(d(FairyTypes.instance.getSeed().getMain(), 0.1), Items.WHEAT_SEEDS));
            d.add(RightClickDrops.items(d(FairyTypes.instance.getPoisonouspotato().getMain(), 0.01), Items.POISONOUS_POTATO));
            d.add(RightClickDrops.items(d(FairyTypes.instance.getMelon().getMain(), 0.03), Items.MELON));

            d.add(RightClickDrops.blocks(d(FairyTypes.instance.getTorch().getMain(), 0.3), Blocks.TORCH));
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.getFurnace().getMain(), 0.1), Blocks.FURNACE));
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.getMagentaglazedterracotta().getMain(), 0.03), Blocks.MAGENTA_GLAZED_TERRACOTTA));
            d.add(RightClickDrops.items(d(FairyTypes.instance.getAxe().getMain(), 0.1), Items.WOODEN_AXE, Items.STONE_AXE, Items.IRON_AXE, Items.GOLDEN_AXE, Items.DIAMOND_AXE));
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.getChest().getMain(), 0.1), Blocks.CHEST));
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.getCraftingtable().getMain(), 0.1), Blocks.CRAFTING_TABLE));
            d.add(RightClickDrops.items(d(FairyTypes.instance.getPotion().getMain(), 0.1), Items.POTIONITEM, Items.LINGERING_POTION, Items.SPLASH_POTION));
            d.add(RightClickDrops.items(d(FairyTypes.instance.getSword().getMain(), 0.1), Items.WOODEN_SWORD, Items.STONE_SWORD, Items.IRON_SWORD, Items.GOLDEN_SWORD, Items.DIAMOND_SWORD));
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.getDispenser().getMain(), 0.1), Blocks.DISPENSER));
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.getAnvil().getMain(), 0.1), Blocks.ANVIL));
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.getEnchant().getMain(), 0.03), Blocks.ENCHANTING_TABLE));
            d.add(RightClickDrops.items(d(FairyTypes.instance.getEnchant().getMain(), 0.03), Items.ENCHANTED_BOOK));
            d.add(RightClickDrops.ingredients(d(FairyTypes.instance.getEnchant().getMain(), 0.03), itemStack -> itemStack.isItemEnchanted()));
            d.add(RightClickDrops.items(d(FairyTypes.instance.getBrewingstand().getMain(), 0.03), Items.BREWING_STAND));
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.getBrewingstand().getMain(), 0.03), Blocks.BREWING_STAND));
            d.add(RightClickDrops.items(d(FairyTypes.instance.getHoe().getMain(), 0.3), Items.WOODEN_HOE, Items.STONE_HOE, Items.IRON_HOE, Items.GOLDEN_HOE, Items.DIAMOND_HOE));
            d.add(RightClickDrops.items(d(FairyTypes.instance.getShield().getMain(), 0.1), Items.SHIELD));
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.getHopper().getMain(), 0.1), Blocks.HOPPER));
            d.add(RightClickDrops.ores(d(FairyTypes.instance.getGlass().getMain(), 0.1), "blockGlass"));
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.getActivatorrail().getMain(), 0.03), Blocks.ACTIVATOR_RAIL));
            d.add(RightClickDrops.blocks(d(FairyTypes.instance.getIronbars().getMain(), 0.1), Blocks.IRON_BARS));

            d.add(RightClickDrops.items(d(FairyTypes.instance.getBread().getMain(), 0.1), Items.BREAD));
            d.add(RightClickDrops.items(d(FairyTypes.instance.getCookie().getMain(), 0.1), Items.COOKIE));
            d.add(RightClickDrops.items(d(FairyTypes.instance.getCake().getMain(), 0.03), Items.CAKE));
            d.add(RightClickDrops.ingredients(d(FairyTypes.instance.getEnchantedgoldenapple().getMain(), 0.003), Ingredient.fromStacks(new ItemStack(Items.GOLDEN_APPLE, 1, 1))));
            d.add(RightClickDrops.items(d(FairyTypes.instance.getSugar().getMain(), 0.3), Items.SUGAR));
            if ("".equals(1)) {
                d.add(RightClickDrops.items(d(FairyTypes.instance.getDarkchocolate().getMain(), 0.001), Items.COOKIE));
                d.add(RightClickDrops.ingredients(d(FairyTypes.instance.getDarkchocolate().getMain(), 0.001), Ingredient.fromStacks(new ItemStack(Items.DYE, 1, 3))));
            }
            d.add(RightClickDrops.items(d(FairyTypes.instance.getRottenflesh().getMain(), 0.1), Items.ROTTEN_FLESH));
            d.add(RightClickDrops.items(d(FairyTypes.instance.getBakedpotato().getMain(), 0.03), Items.BAKED_POTATO));
            d.add(RightClickDrops.items(d(FairyTypes.instance.getCookedchicken().getMain(), 0.1), Items.COOKED_CHICKEN));
            d.add(RightClickDrops.ingredients(d(FairyTypes.instance.getCookedsalmon().getMain(), 0.03), Ingredient.fromStacks(new ItemStack(Items.COOKED_FISH, 1, 1))));
            d.add(RightClickDrops.items(d(FairyTypes.instance.getSteak().getMain(), 0.1), Items.COOKED_BEEF));
            d.add(RightClickDrops.ingredients(d(FairyTypes.instance.getGoldenapple().getMain(), 0.03), Ingredient.fromStacks(new ItemStack(Items.GOLDEN_APPLE, 1, 0))));

            d.add(RightClickDrops.world(d(FairyTypes.instance.getDaytime().getMain(), 0.001), (w, p) -> time(w, 6000, 18000)));
            d.add(RightClickDrops.world(d(FairyTypes.instance.getNight().getMain(), 0.001), (w, p) -> time(w, 19000, 24000) || time(w, 0, 5000)));
            d.add(RightClickDrops.world(d(FairyTypes.instance.getMorning().getMain(), 0.001), (w, p) -> time(w, 5000, 9000)));
            d.add(RightClickDrops.world(d(FairyTypes.instance.getFine().getMain(), 0.01), (w, p) -> w.provider.isSurfaceWorld() && w.canSeeSky(p) && !w.isRainingAt(p)));
            d.add(RightClickDrops.world(d(FairyTypes.instance.getRain().getMain(), 0.01), (w, p) -> w.provider.isSurfaceWorld() && w.canSeeSky(p) && w.isRainingAt(p)));

            d.add(RightClickDrops.biomeTypes(d(FairyTypes.instance.getPlains().getMain(), 0.01), BiomeDictionary.Type.PLAINS));
            d.add(RightClickDrops.biomeTypes(d(FairyTypes.instance.getForest().getMain(), 0.01), BiomeDictionary.Type.FOREST));
            d.add(RightClickDrops.biomeTypes(d(FairyTypes.instance.getOcean().getMain(), 0.01), BiomeDictionary.Type.OCEAN));
            d.add(RightClickDrops.biomeTypes(d(FairyTypes.instance.getTaiga().getMain(), 0.01), BiomeDictionary.Type.CONIFEROUS));
            d.add(RightClickDrops.biomeTypes(d(FairyTypes.instance.getDesert().getMain(), 0.01), BiomeDictionary.Type.SANDY));
            d.add(RightClickDrops.biomeTypes(d(FairyTypes.instance.getMountain().getMain(), 0.01), BiomeDictionary.Type.MOUNTAIN));

            d.add(RightClickDrops.ingredients(d(FairyTypes.instance.getFortune().getMain(), 0.01), itemStack -> EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemStack) > 0));

            d.add(RightClickDrops.fixed(d(FairyTypes.instance.getTime().getMain(), 0.0001)));
            if (!"".equals(1)) {
                ApiMain.logger().info("Limited Fairy: cupid");
                long epochSecondNow = Instant.now().getEpochSecond();
                long epochSecondLimit = LocalDateTime.of(2021, 7, 1, 0, 0, 0).toInstant(ZoneOffset.ofHours(9)).getEpochSecond();
                ApiMain.logger().info("Now  : " + epochSecondNow);
                ApiMain.logger().info("Limit: " + epochSecondLimit);
                if (epochSecondNow < epochSecondLimit) {
                    d.add(RightClickDrops.fixed(d(FairyTypes.instance.getCupid().getMain(), 0.001)));
                }
            }

        }

    }

    private static String[] m(String material) {
        return ISuppliterator.of("ingot", "nugget", "gem", "dust", "dustTiny", "block", "rod", "plate", "ore")
                .map(s -> s + material)
                .toArray(String[]::new);
    }

    private static IDrop r(VariantFairy variantFairy) {
        return new DropFixed(variantFairy.createItemStack(), 0.1 * Math.pow(0.1, variantFairy.type.rare - 1));
    }

    private static IDrop d(VariantFairy variantFairy, double weight) {
        return new DropFixed(variantFairy.createItemStack(), weight);
    }

    private static boolean time(World world, int min, int max) {
        return world.provider.isSurfaceWorld() && min <= (world.getWorldTime() + 6000) % 24000 && (world.getWorldTime() + 6000) % 24000 <= max;
    }

}
