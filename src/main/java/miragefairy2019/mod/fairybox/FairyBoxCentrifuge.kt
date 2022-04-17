package miragefairy2019.mod.fairybox

import miragefairy2019.lib.compound
import miragefairy2019.lib.compoundOrCreate
import miragefairy2019.lib.container
import miragefairy2019.lib.get
import miragefairy2019.lib.nbtProvider
import miragefairy2019.lib.readFromNBT
import miragefairy2019.lib.readInventory
import miragefairy2019.lib.writeToNBT
import miragefairy2019.mod.GuiId
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.util.InventoryTileEntity
import mirrg.kotlin.atLeast
import mirrg.kotlin.atMost
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand

class TileEntityFairyBoxCentrifuge : TileEntityFairyBoxBase() {

    // Inventory

    val fairyInventory = InventoryTileEntity(this, "tile.fairyCentrifuge.name", false, 3)
    val inputInventory = InventoryTileEntity(this, "tile.fairyCentrifuge.name", false, 9)
    var resultInventory = InventoryTileEntity(this, "tile.fairyCentrifuge.name", false, 0)
    val outputInventory = InventoryTileEntity(this, "tile.fairyCentrifuge.name", false, 9)

    override fun readFromNBT(nbt: NBTTagCompound) {
        super.readFromNBT(nbt)
        fairyInventory.readFromNBT(nbt.nbtProvider["fairy"].compound ?: NBTTagCompound())
        inputInventory.readFromNBT(nbt.nbtProvider["input"].compound ?: NBTTagCompound())
        resultInventory = (nbt.nbtProvider["result"].compound ?: NBTTagCompound()).readInventory { size ->
            InventoryTileEntity(this, "tile.fairyCentrifuge.name", false, size)
        }
        outputInventory.readFromNBT(nbt.nbtProvider["output"].compound ?: NBTTagCompound())
    }

    override fun writeToNBT(nbt: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(nbt)
        fairyInventory.writeToNBT(nbt.nbtProvider["fairy"].compoundOrCreate)
        inputInventory.writeToNBT(nbt.nbtProvider["input"].compoundOrCreate)
        resultInventory.writeToNBT(nbt.nbtProvider["result"].compoundOrCreate)
        outputInventory.writeToNBT(nbt.nbtProvider["output"].compoundOrCreate)
        return nbt
    }


    // Tree

    fun getLeaves() = try {
        compileFairyTree(world, pos)
    } catch (e: TreeCompileException) {
        null
    }

    fun getFolia(times: Int): Double {
        return getAuraCollectionSpeed(world, getLeaves() ?: return 0.0, times) atMost 120.0
    }

    fun getFoliaSpeedFactor(folia: Double) = (folia - 30.0) / 30.0 atLeast 0.0


    // Action

    override fun getExecutor(): IFairyBoxExecutor {
        return object : IFairyBoxExecutor {
            override fun onBlockActivated(player: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
                if (world.isRemote) return true
                player.openGui(ModMirageFairy2019.instance, GuiId.fairyBoxCentrifuge, world, pos.x, pos.y, pos.z)
                return true
            }

            override fun onUpdateTick() {

            }
        }
    }


    // Gui

    fun createContainer(player: EntityPlayer) = container {
        width = 100
        height = 100
    }

}
