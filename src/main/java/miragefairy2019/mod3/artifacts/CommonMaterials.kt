package miragefairy2019.mod3.artifacts

import miragefairy2019.libkt.DataBlockState
import miragefairy2019.libkt.DataBlockStates
import miragefairy2019.libkt.Module
import miragefairy2019.libkt.addOreName
import miragefairy2019.libkt.block
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.item
import miragefairy2019.libkt.makeBlockStates
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setCustomModelResourceLocation
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.lib.BlockMulti
import miragefairy2019.mod.lib.BlockVariantList
import miragefairy2019.mod.lib.IBlockVariant
import miragefairy2019.mod.lib.ItemBlockMulti
import miragefairy2019.mod3.main.api.ApiMain
import net.minecraft.block.Block
import net.minecraft.block.BlockFalling
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityFallingBlock
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.IStringSerializable
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.Explosion
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.Random

object CommonMaterials {
    lateinit var blockMaterials1: () -> BlockMaterials<EnumVariantMaterials1>
    lateinit var itemBlockMaterials1: () -> ItemBlockMaterials<EnumVariantMaterials1>
    val module: Module = {

        blockMaterials1 = block({ BlockMaterials(EnumVariantMaterials1.variantList) }, "materials1") {
            setCreativeTab { ApiMain.creativeTab }
            makeBlockStates {
                DataBlockStates(
                    variants = listOf(
                        "miragefairy2019:apatite_block",
                        "miragefairy2019:fluorite_block",
                        "miragefairy2019:sulfur_block",
                        "miragefairy2019:cinnabar_block",
                        "miragefairy2019:moonstone_block",
                        "miragefairy2019:magnetite_block",
                        "miragefairy2019:pyrope_block",
                        "miragefairy2019:smithsonite_block",
                        "miragefairy2019:charcoal_block",
                        "miragefairy2019:mirage_flower_leaf_block",
                        "miragefairy2019:miragium_ingot_block",
                        "miragefairy2019:miragium_dust_block",
                        "minecraft:stone",
                        "minecraft:stone",
                        "minecraft:stone",
                        "minecraft:stone"
                    ).mapIndexed { i, model -> "variant=$i" to DataBlockState(model = model) }.toMap()
                )
            }
        }
        itemBlockMaterials1 = item({ ItemBlockMaterials(blockMaterials1()) }, "materials1") {
            onRegisterItem {
                blockMaterials1().variantList.blockVariants.forEach {
                    item.setCustomModelResourceLocation(it.metadata, model = ResourceLocation(ModMirageFairy2019.MODID, it.resourceName))
                }
            }
            onCreateItemStack {
                blockMaterials1().variantList.blockVariants.forEach {
                    item.addOreName(it.oreName, it.metadata)
                }
            }
        }
        onMakeLang {
            enJa("tile.blockApatite.name", "Block of Apatite", "燐灰石ブロック")
            enJa("tile.blockFluorite.name", "Block of Fluorite", "蛍石ブロック")
            enJa("tile.blockSulfur.name", "Block of Sulfur", "硫黄ブロック")
            enJa("tile.blockCinnabar.name", "Block of Cinnabar", "辰砂ブロック")
            enJa("tile.blockMoonstone.name", "Block of Moonstone", "月長石ブロック")
            enJa("tile.blockMagnetite.name", "Block of Magnetite", "磁鉄鉱ブロック")
            enJa("tile.blockPyrope.name", "Block of Pyrope", "パイロープブロック")
            enJa("tile.blockSmithsonite.name", "Block of Smithsonite", "スミソナイトブロック")
            enJa("tile.blockCharcoal.name", "Block of Charcoal", "木炭ブロック")
            enJa("tile.blockLeafMirageFlower.name", "Block of Mirage Flower Leaf", "ミラージュフラワーの葉ブロック")
            enJa("tile.blockMiragium.name", "Block of Miragium", "ミラジウムブロック")
            enJa("tile.blockDustMiragium.name", "Block of Miragium Dust", "ミラジウムの粉ブロック")
        }

    }
}

class BlockMaterials<V : IBlockVariantMaterials>(variantList: BlockVariantList<V>) : BlockMulti<V>(Material.IRON, variantList) {
    init {
        variantList.blockVariants.forEach { it -> setHarvestLevel(it.harvestTool, it.harvestLevel, getState(it)) }
    }


    // 一般
    override fun getMaterial(blockState: IBlockState) = getVariant(blockState).material
    override fun getBlockHardness(blockState: IBlockState, world: World, blockPos: BlockPos) = getVariant(blockState).blockHardness
    override fun getExplosionResistance(world: World, blockPos: BlockPos, exploder: Entity?, explosion: Explosion) = getVariant(world.getBlockState(blockPos)).blockHardness * 5
    override fun getSoundType(blockState: IBlockState, world: World, blockPos: BlockPos, entity: Entity?) = getVariant(blockState).soundType


    // ドロップ系
    override fun canSilkHarvest(world: World, blockPos: BlockPos, blockState: IBlockState, player: EntityPlayer) = true


    // 落下判定

    override fun onBlockAdded(world: World, blockPos: BlockPos, blockState: IBlockState) {
        if (!getVariant(blockState).isFallable) return
        world.scheduleUpdate(blockPos, this, tickRate(world))
    }

    override fun neighborChanged(blockState: IBlockState, world: World, blockPos: BlockPos, block: Block, fromBlockPos: BlockPos) {
        if (!getVariant(blockState).isFallable) return
        world.scheduleUpdate(blockPos, this, tickRate(world))
    }

    override fun updateTick(world: World, blockPos: BlockPos, blockState: IBlockState, random: Random) {
        if (world.isRemote) return
        if (!getVariant(blockState).isFallable) return
        checkFallable(world, blockPos)
    }

    private fun checkFallable(world: World, blockPos: BlockPos) {
        fun canFallThrough(blockPos: BlockPos) = world.isAirBlock(blockPos) || BlockFalling.canFallThrough(world.getBlockState(blockPos))

        val downBlockPos = blockPos.down()
        if (!(canFallThrough(downBlockPos) && blockPos.y >= 0)) return // 現在奈落よりも上にあり、かつ床が無い　でないなら落下しない

        if (!BlockFalling.fallInstantly && world.isAreaLoaded(blockPos.add(-32, -32, -32), blockPos.add(32, 32, 32))) {
            // 即時落下状態でなく、周辺が読み込まれているならば、落下ブロックエンティティになる

            if (!world.isRemote) {
                world.spawnEntity(
                    EntityFallingBlock(
                        world,
                        blockPos.x + 0.5,
                        blockPos.y.toDouble(),
                        blockPos.z + 0.5,
                        world.getBlockState(blockPos)
                    )
                )
            }
        } else {
            // 即時落下状態、もしくは周辺が部分的に読み込まれていないならば、即落下する

            val blockState = world.getBlockState(blockPos)
            world.setBlockToAir(blockPos)

            var groundBlockPos = downBlockPos
            while (canFallThrough(groundBlockPos) && groundBlockPos.y > 0) { // 地面かY=0になるまでループ
                groundBlockPos = groundBlockPos.down()
            }

            if (groundBlockPos.y > 0) { // Y=0の位置にある床には着地できない
                world.setBlockState(groundBlockPos.up(), blockState)
            }
        }
    }

    override fun tickRate(world: World) = 2

    @SideOnly(Side.CLIENT)
    override fun randomDisplayTick(blockState: IBlockState, world: World, blockPos: BlockPos, random: Random) {
        if (!getVariant(blockState).isFallable) return
        if (random.nextInt(16) == 0) {
            val downBlockPos = blockPos.down()
            if (BlockFalling.canFallThrough(world.getBlockState(downBlockPos))) {
                val x = (downBlockPos.x + random.nextFloat()).toDouble()
                val y = downBlockPos.y - 0.05
                val z = (downBlockPos.z + random.nextFloat()).toDouble()
                world.spawnParticle(EnumParticleTypes.FALLING_DUST, x, y, z, 0.0, 0.0, 0.0, getStateId(blockState))
            }
        }
    }


    // ビーコン基礎ブロック判定
    override fun isBeaconBase(world: IBlockAccess, blockPos: BlockPos, beaconBlockPos: BlockPos) = getVariant(world.getBlockState(blockPos)).isBeaconBase
}

class HardnessClass(val blockHardness: Float, val harvestTool: String, val harvestLevel: Int) {
    companion object {
        val SOFT = HardnessClass(3.0f, "pickaxe", 0) // 硬度3程度、カルサイト級
        val HARD = HardnessClass(5.0f, "pickaxe", 0) // 硬度5程度、石級
        val VERY_HARD = HardnessClass(5.0f, "pickaxe", 1) // 硬度7程度、クリスタル級
        val SUPER_HARD = HardnessClass(5.0f, "pickaxe", 2) // 硬度9程度、ダイヤモンド級
    }
}

enum class EnumVariantMaterials1(
    override val metadata: Int,
    override val resourceName: String,
    override val unlocalizedName: String,
    val oreName: String,
    val hardnessClass: HardnessClass,
    override val burnTime: Int,
    override val soundType: SoundType,
    override val isFallable: Boolean,
    override val material: Material,
    override val isBeaconBase: Boolean
) : IStringSerializable, IBlockVariantMaterials {
    APATITE_BLOCK(0, "apatite_block", "blockApatite", "blockApatite", HardnessClass.HARD, 0, SoundType.STONE, false, Material.IRON, true),
    FLUORITE_BLOCK(1, "fluorite_block", "blockFluorite", "blockFluorite", HardnessClass.HARD, 0, SoundType.STONE, false, Material.IRON, true),
    SULFUR_BLOCK(2, "sulfur_block", "blockSulfur", "blockSulfur", HardnessClass.HARD, 0, SoundType.STONE, false, Material.IRON, true),
    CINNABAR_BLOCK(3, "cinnabar_block", "blockCinnabar", "blockCinnabar", HardnessClass.HARD, 0, SoundType.STONE, false, Material.IRON, true),
    MOONSTONE_BLOCK(4, "moonstone_block", "blockMoonstone", "blockMoonstone", HardnessClass.VERY_HARD, 0, SoundType.STONE, false, Material.IRON, true),
    MAGNETITE_BLOCK(5, "magnetite_block", "blockMagnetite", "blockMagnetite", HardnessClass.HARD, 0, SoundType.STONE, false, Material.IRON, true),
    PYROPE_BLOCK(6, "pyrope_block", "blockPyrope", "blockPyrope", HardnessClass.VERY_HARD, 0, SoundType.STONE, false, Material.IRON, true),
    SMITHSONITE_BLOCK(7, "smithsonite_block", "blockSmithsonite", "blockSmithsonite", HardnessClass.SOFT, 0, SoundType.STONE, false, Material.IRON, true),
    CHARCOAL_BLOCK(8, "charcoal_block", "blockCharcoal", "blockCharcoal", HardnessClass(5.0f, "pickaxe", 0), 20 * 10 * 8 * 9, SoundType.STONE, false, Material.ROCK, false),
    MIRAGE_FLOWER_LEAF_BLOCK(9, "mirage_flower_leaf_block", "blockLeafMirageFlower", "blockLeafMirageFlower", HardnessClass(2.0f, "axe", 0), 0, SoundType.GLASS, false, Material.LEAVES, false),
    MIRAGIUM_INGOT_BLOCK(10, "miragium_ingot_block", "blockMiragium", "blockMiragium", HardnessClass(5.0f, "pickaxe", 1), 0, SoundType.METAL, false, Material.IRON, false),
    MIRAGIUM_DUST_BLOCK(11, "miragium_dust_block", "blockDustMiragium", "blockDustMiragium", HardnessClass(0.5f, "shovel", 0), 0, SoundType.SNOW, true, Material.SAND, false),
    ;

    override fun toString() = resourceName
    override fun getName() = resourceName
    override val blockHardness = hardnessClass.blockHardness
    override val harvestTool = hardnessClass.harvestTool
    override val harvestLevel = hardnessClass.harvestLevel

    companion object {
        val variantList = BlockVariantList(values().toList())
    }
}

interface IBlockVariantMaterials : IBlockVariant {
    val blockHardness: Float
    val harvestTool: String
    val harvestLevel: Int
    val burnTime: Int
    val soundType: SoundType
    val isFallable: Boolean
    val material: Material
    val isBeaconBase: Boolean
}

class ItemBlockMaterials<V : IBlockVariantMaterials>(block: BlockMaterials<V>) : ItemBlockMulti<BlockMaterials<V>, V>(block) {
    override fun getItemBurnTime(itemStack: ItemStack) = block2.getVariant(itemStack.metadata).burnTime
}
