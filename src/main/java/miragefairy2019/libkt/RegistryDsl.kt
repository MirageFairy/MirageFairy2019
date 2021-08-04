package miragefairy2019.libkt

import miragefairy2019.mod.api.main.ApiMain
import miragefairy2019.mod.lib.EventRegistryMod
import net.minecraft.block.Block
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.item.Item
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.function.Consumer

operator fun EventRegistryMod.invoke(block: EventRegistryMod.() -> Unit) = block()

fun <T : Item> EventRegistryMod.item(modid: String, registerName: String, creator: () -> T, block: T.() -> Unit = {}) {
    registerBlock.register(Consumer {
        val value = creator()
        value.setRegistryName(modid, registerName)
        value.block()
        ForgeRegistries.ITEMS.register(value)
    })
}

fun <T : Block> EventRegistryMod.block(modid: String, registerName: String, creator: () -> T, block: T.() -> Unit = {}) {
    registerBlock.register(Consumer {
        val value = creator()
        value.setRegistryName(modid, registerName)
        value.block()
        ForgeRegistries.BLOCKS.register(value)
    })
}

fun <T : TileEntity> EventRegistryMod.tileEntity(modid: String, registerName: String, `class`: Class<T>) {
    init.register(Consumer {
        GameRegistry.registerTileEntity(`class`, ResourceLocation(modid, registerName))
    })
}

fun <T : TileEntity, R : TileEntitySpecialRenderer<T>> EventRegistryMod.tileEntityRenderer(classTileEntity: Class<T>, creatorRenderer: () -> R, block: R.() -> Unit = {}) {
    init.register(Consumer {
        if (ApiMain.side().isClient) {
            object : Any() {
                @SideOnly(Side.CLIENT)
                fun run() {
                    val renderer = creatorRenderer()
                    renderer.block()
                    ClientRegistry.bindTileEntitySpecialRenderer(classTileEntity, renderer)
                }
            }.run()
        }
    })
}
