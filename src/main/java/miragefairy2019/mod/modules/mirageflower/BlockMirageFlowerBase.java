package miragefairy2019.mod.modules.mirageflower;

import miragefairy2019.mod.api.ApiMirageFlower;
import miragefairy2019.mod.api.pickable.IPickable;
import miragefairy2019.mod.lib.UtilsMinecraft;
import miragefairy2019.mod.modules.fairycrystal.ModuleFairyCrystal;
import miragefairy2019.mod.modules.materialsfairy.ModuleMaterialsFairy;
import mirrg.boron.util.UtilsMath;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Random;

public class BlockMirageFlowerBase extends BlockBush implements IGrowable {

    public BlockMirageFlowerBase(Material material) {
        super(material);
    }

}
