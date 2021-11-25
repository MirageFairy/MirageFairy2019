package miragefairy2019.mod.modules.fairycrystal;

import miragefairy2019.mod.api.fairycrystal.DropCategory;
import miragefairy2019.mod.api.fairycrystal.IDrop;
import miragefairy2019.mod.api.fairycrystal.IRightClickDrop;
import miragefairy2019.mod.lib.WeightedRandom;
import miragefairy2019.mod3.fairy.FairyTypes;
import mirrg.boron.util.struct.Tuple;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class FairyCrystalDropper {

    /**
     * このメソッドはサーバーワールドのスレッドからしか呼び出せません。
     */
    public abstract ISuppliterator<IRightClickDrop> getDropList();

    /**
     * このメソッドはサーバーワールドのスレッドからしか呼び出せません。
     */
    public List<WeightedRandom.Item<ItemStack>> getDropTable(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ, int rank, double rareBoost) {
        BlockPos pos2 = world.getBlockState(pos).isFullBlock() ? pos.offset(facing) : pos;

        Set<Block> blocks = new HashSet<>();
        Set<Tuple<IBlockState, BlockPos>> blockStates = new HashSet<>();
        Set<Item> items = new HashSet<>();
        Set<ItemStack> itemStacks = new HashSet<>();
        Set<Biome> biomes = new HashSet<>();
        Set<BiomeDictionary.Type> biomeTypes = new HashSet<>();

        // ワールドブロック
        for (int xi = -2; xi <= 2; xi++) {
            for (int yi = -2; yi <= 2; yi++) {
                for (int zi = -2; zi <= 2; zi++) {
                    BlockPos pos3 = pos.add(xi, yi, zi);
                    IBlockState blockState = world.getBlockState(pos3);
                    blockStates.add(Tuple.of(blockState, pos3));
                    blocks.add(blockState.getBlock());
                    TileEntity tileEntity = world.getTileEntity(pos3);
                    if (tileEntity instanceof IInventory) {
                        for (int i = 0; i < ((IInventory) tileEntity).getSizeInventory(); i++) {
                            ItemStack itemStack = ((IInventory) tileEntity).getStackInSlot(i);
                            if (!itemStack.isEmpty()) {
                                itemStacks.add(itemStack);
                                items.add(itemStack.getItem());
                                Block block = Block.getBlockFromItem(itemStack.getItem());
                                if (block != Blocks.AIR) blocks.add(block);
                            }
                        }
                    }
                }
            }
        }

        // インベントリ
        for (ItemStack itemStack : player.inventory.mainInventory) {
            if (!itemStack.isEmpty()) {
                itemStacks.add(itemStack);
                items.add(itemStack.getItem());
                Block block = Block.getBlockFromItem(itemStack.getItem());
                if (block != Blocks.AIR) blocks.add(block);
            }
        }
        for (ItemStack itemStack : player.inventory.armorInventory) {
            if (!itemStack.isEmpty()) {
                itemStacks.add(itemStack);
                items.add(itemStack.getItem());
                Block block = Block.getBlockFromItem(itemStack.getItem());
                if (block != Blocks.AIR) blocks.add(block);
            }
        }
        for (ItemStack itemStack : player.inventory.offHandInventory) {
            if (!itemStack.isEmpty()) {
                itemStacks.add(itemStack);
                items.add(itemStack.getItem());
                Block block = Block.getBlockFromItem(itemStack.getItem());
                if (block != Blocks.AIR) blocks.add(block);
            }
        }

        // バイオーム
        {
            Biome biome = world.getBiome(pos2);
            biomes.add(biome);
            biomeTypes.addAll(BiomeDictionary.getTypes(biome));
        }

        // エンティティ
        List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(
                player.posX - 10,
                player.posY - 10,
                player.posZ - 10,
                player.posX + 10,
                player.posY + 10,
                player.posZ + 10));
        Set<Class<? extends Entity>> classEntities = new HashSet<>();
        for (Entity entity : entities) {
            classEntities.add(entity.getClass());
        }

        // リスト作成
        List<WeightedRandom.Item<ItemStack>> dropTable = getDropList()
                .mapIfNotNull(d -> {
                    IDrop drop = d.getDrop();

                    if (d.testUseItem(player, world, pos, hand, facing, hitX, hitY, hitZ)) return drop;
                    if (d.testWorld(world, pos2)) return drop;

                    for (Block block : blocks) {
                        if (d.testBlock(block)) return drop;
                    }
                    for (Tuple<IBlockState, BlockPos> blockState : blockStates) {
                        if (d.testBlockState(world, blockState.y, blockState.x)) return drop;
                    }
                    for (Item item : items) {
                        if (d.testItem(item)) return drop;
                    }
                    for (ItemStack itemStack : itemStacks) {
                        if (d.testItemStack(itemStack)) return drop;
                    }
                    for (Biome biome : biomes) {
                        if (d.testBiome(biome)) return drop;
                    }
                    for (BiomeDictionary.Type biomeType : biomeTypes) {
                        if (d.testBiomeType(biomeType)) return drop;
                    }
                    for (Class<? extends Entity> classEntity : classEntities) {
                        if (d.testClassEntity(classEntity)) return drop;
                    }
                    for (Entity entity : entities) {
                        if (d.testEntity(entity)) return drop;
                    }

                    return null;
                })
                .map(d -> new WeightedRandom.Item<>(d.getItemStack(rank), d.getWeight() * (d.getDropCategory() == DropCategory.RARE ? rareBoost : 1)))
                .toList();
        dropTable = WeightedRandom.unique(dropTable, (a, b) -> ItemStack.areItemStacksEqualUsingNBTShareTag(a, b));

        // 1に満たない場合はairを入れて詰める
        double totalWeight = WeightedRandom.getTotalWeight(dropTable);
        if (totalWeight < 1) dropTable.add(new WeightedRandom.Item<>(FairyTypes.instance.getAir().getMain().createItemStack(), 1 - totalWeight));

        return dropTable;
    }

    /**
     * このメソッドはサーバーワールドのスレッドからしか呼び出せません。
     */
    public Optional<ItemStack> drop(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ, int rank, double rareBoost) {

        // ガチャリスト取得
        List<WeightedRandom.Item<ItemStack>> dropTable = getDropTable(player, world, pos, hand, facing, hitX, hitY, hitZ, rank, rareBoost);

        // ガチャを引く
        Optional<ItemStack> oItemStack = WeightedRandom.getRandomItem(world.rand, dropTable);

        return oItemStack;
    }

}
