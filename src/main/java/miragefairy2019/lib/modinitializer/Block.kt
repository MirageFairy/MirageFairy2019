package miragefairy2019.lib.modinitializer

import miragefairy2019.common.ResourceName
import miragefairy2019.libkt.resourceLocation
import miragefairy2019.mod.Main
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.artifacts.FluidStateMapper
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.registry.ForgeRegistries

class BlockScope<out B : Block>(override val modScope: ModScope, override val resourceName: ResourceName, getter: () -> B) : ObjectScope<B>(getter), NamedScope {
    val block get() = initializingObject
}

fun <B : Block> ModScope.block(creator: () -> B, registryName: String, initializer: (BlockScope<B>.() -> Unit)? = null): BlockScope<B> {
    lateinit var block: B
    onRegisterBlock {
        block = creator()
        block.setRegistryName(ModMirageFairy2019.MODID, registryName)
        ForgeRegistries.BLOCKS.register(block)
    }
    return BlockScope(this, ResourceName(modId, registryName)) { block }.also {
        if (initializer != null) it.initializer()
    }
}


fun <B : Block> BlockScope<B>.setUnlocalizedName(unlocalizedName: String) = modScope.onRegisterItem { block.unlocalizedName = unlocalizedName }
fun <B : Block> BlockScope<B>.setCreativeTab(creativeTab: () -> CreativeTabs) = modScope.onRegisterItem { block.setCreativeTab(creativeTab()) }

fun <B : Block> BlockScope<B>.setFluidStateMapper() = modScope.onRegisterBlock {
    if (Main.side.isClient) {
        ModelLoader.setCustomStateMapper(block, FluidStateMapper(resourceName.resourceLocation))
    }
}
