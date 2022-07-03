package miragefairy2019.lib.gui

import miragefairy2019.libkt.RectangleInt
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

abstract class ContainerComponentBase : ContainerComponent2()

@SideOnly(Side.CLIENT)
abstract class GuiComponentBase(container: ContainerComponent2) : GuiComponent2(container)

fun ContainerComponentBase.component(rectangle: RectangleInt, block: ComponentEventDistributor.() -> Unit) {
    components += ComponentEventDistributor(rectangle).apply { block() }
}
