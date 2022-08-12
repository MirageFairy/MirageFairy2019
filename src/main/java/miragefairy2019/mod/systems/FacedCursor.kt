package miragefairy2019.mod.systems

import miragefairy2019.lib.modinitializer.ItemScope
import miragefairy2019.lib.modinitializer.module
import mirrg.kotlin.hydrogen.castOrNull
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.BufferBuilder
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import net.minecraftforge.client.event.DrawBlockHighlightEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.opengl.GL11

interface IFacedCursorHandler {
    fun hasFacedCursor(item: Item, itemStack: ItemStack, world: World, blockPos: BlockPos, player: EntityPlayer, rayTraceResult: RayTraceResult): Boolean
}

interface IFacedCursorBlock {
    fun getFacedCursorHandler(itemStack: ItemStack): IFacedCursorHandler?
}

object ApiFacedCursor {
    val facedCursorHandlers = mutableMapOf<Item, IFacedCursorHandler>()
}


fun <I : Item> ItemScope<I>.setFacedCursor() = modScope.onInit {
    ApiFacedCursor.facedCursorHandlers[item] = object : IFacedCursorHandler {
        override fun hasFacedCursor(item: Item, itemStack: ItemStack, world: World, blockPos: BlockPos, player: EntityPlayer, rayTraceResult: RayTraceResult) = true
    }
}


val facedCursorModule = module {
    onInit {
        if (side.isClient) {
            MinecraftForge.EVENT_BUS.register(object {
                @SubscribeEvent
                fun hook(event: DrawBlockHighlightEvent) {
                    if (event.subID != 0) return // 謎判定
                    if (event.target.typeOfHit != RayTraceResult.Type.BLOCK) return // ブロックをターゲットしている場合のみ

                    // 有効なアイテムを所持している場合のみ
                    fun hasFacedCursor(itemStack: ItemStack): Boolean {
                        val item = itemStack.item
                        val handler = ApiFacedCursor.facedCursorHandlers[item]
                            ?: item.castOrNull<ItemBlock>()?.block?.castOrNull<IFacedCursorBlock>()?.getFacedCursorHandler(itemStack)
                            ?: return false
                        return handler.hasFacedCursor(item, itemStack, event.player.world, event.target.blockPos, event.player, event.target)
                    }
                    if (!(hasFacedCursor(event.player.heldItemMainhand) || hasFacedCursor(event.player.heldItemOffhand))) return

                    GlStateManager.enableBlend()
                    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO)
                    GlStateManager.glLineWidth(2.0f)
                    GlStateManager.disableTexture2D()
                    GlStateManager.depthMask(false)
                    try {
                        val facing: EnumFacing = event.target.sideHit!!
                        val blockPos: BlockPos = event.target.blockPos
                        val blockState: IBlockState = event.player.world.getBlockState(blockPos)

                        val offset = when (facing) {
                            EnumFacing.EAST -> event.target.hitVec.x - blockPos.x
                            EnumFacing.WEST -> 1 - (event.target.hitVec.x - blockPos.x)
                            EnumFacing.UP -> event.target.hitVec.y - blockPos.y
                            EnumFacing.DOWN -> 1 - (event.target.hitVec.y - blockPos.y)
                            EnumFacing.SOUTH -> event.target.hitVec.z - blockPos.z
                            EnumFacing.NORTH -> 1 - (event.target.hitVec.z - blockPos.z)
                        }

                        if (blockState.material == Material.AIR) return // 水中では非表示
                        if (!event.player.world.worldBorder.contains(blockPos)) return // ワールドボーダー外では非表示

                        GlStateManager.pushMatrix()
                        try {

                            GlStateManager.translate(
                                -(event.player.lastTickPosX + (event.player.posX - event.player.lastTickPosX) * event.partialTicks.toDouble()),
                                -(event.player.lastTickPosY + (event.player.posY - event.player.lastTickPosY) * event.partialTicks.toDouble()),
                                -(event.player.lastTickPosZ + (event.player.posZ - event.player.lastTickPosZ) * event.partialTicks.toDouble())
                            )

                            GlStateManager.translate(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble()) // 対象ブロックの座標に移動

                            // 回転させて選択面に合わせる
                            GlStateManager.translate(0.5f, 0.5f, 0.5f)
                            when (facing) {
                                EnumFacing.UP -> Unit
                                EnumFacing.NORTH -> {
                                    GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f)
                                }
                                EnumFacing.EAST -> {
                                    GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f)
                                    GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f)
                                }
                                EnumFacing.SOUTH -> {
                                    GlStateManager.rotate(-180.0f, 0.0f, 1.0f, 0.0f)
                                    GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f)
                                }
                                EnumFacing.WEST -> {
                                    GlStateManager.rotate(-270.0f, 0.0f, 1.0f, 0.0f)
                                    GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f)
                                }
                                EnumFacing.DOWN -> GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f)
                            }
                            GlStateManager.translate(-0.5f, -0.5f, -0.5f)

                            run {
                                val tessellator: Tessellator = Tessellator.getInstance()
                                val bufferBuilder: BufferBuilder = tessellator.buffer
                                bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR)

                                bufferBuilder.pos(0.25, offset + 0.002, 0.0).color(0.0f, 0.0f, 0.0f, 0.4f).endVertex()
                                bufferBuilder.pos(0.25, offset + 0.002, 1.0).color(0.0f, 0.0f, 0.0f, 0.4f).endVertex()

                                bufferBuilder.pos(0.75, offset + 0.002, 0.0).color(0.0f, 0.0f, 0.0f, 0.4f).endVertex()
                                bufferBuilder.pos(0.75, offset + 0.002, 1.0).color(0.0f, 0.0f, 0.0f, 0.4f).endVertex()

                                bufferBuilder.pos(0.0, offset + 0.002, 0.25).color(0.0f, 0.0f, 0.0f, 0.4f).endVertex()
                                bufferBuilder.pos(1.0, offset + 0.002, 0.25).color(0.0f, 0.0f, 0.0f, 0.4f).endVertex()

                                bufferBuilder.pos(0.0, offset + 0.002, 0.75).color(0.0f, 0.0f, 0.0f, 0.4f).endVertex()
                                bufferBuilder.pos(1.0, offset + 0.002, 0.75).color(0.0f, 0.0f, 0.0f, 0.4f).endVertex()

                                tessellator.draw()
                            }

                        } finally {
                            GlStateManager.popMatrix()
                        }
                    } finally {
                        GlStateManager.depthMask(true)
                        GlStateManager.enableTexture2D()
                        GlStateManager.disableBlend()
                    }
                }
            })
        }
    }
}
