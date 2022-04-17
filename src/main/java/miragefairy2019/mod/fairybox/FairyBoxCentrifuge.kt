package miragefairy2019.mod.fairybox

import miragefairy2019.api.Erg
import miragefairy2019.api.IFairyCentrifugeCraftArguments
import miragefairy2019.api.IFairyCentrifugeCraftProcess
import miragefairy2019.api.IFairyCentrifugeCraftRecipe
import miragefairy2019.api.Mana
import miragefairy2019.lib.compound
import miragefairy2019.lib.compoundOrCreate
import miragefairy2019.lib.container
import miragefairy2019.lib.factors
import miragefairy2019.lib.fairyType
import miragefairy2019.lib.get
import miragefairy2019.lib.getFairyCentrifugeCraftRecipe
import miragefairy2019.lib.nbtProvider
import miragefairy2019.lib.readFromNBT
import miragefairy2019.lib.readInventory
import miragefairy2019.lib.writeToNBT
import miragefairy2019.libkt.flatten
import miragefairy2019.libkt.sandwich
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.GuiId
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.util.InventoryTileEntity
import mirrg.kotlin.atLeast
import mirrg.kotlin.atMost
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.text.ITextComponent

class TileEntityFairyBoxCentrifuge : TileEntityFairyBoxBase() {

    // Inventory

    fun createInventory(size: Int) = InventoryTileEntity(this, "tile.fairyCentrifuge.name", false, size)

    val fairyInventory = createInventory(3)
    val inputInventory = createInventory(9)
    var resultInventory = createInventory(0)
    val outputInventory = createInventory(9)

    override fun readFromNBT(nbt: NBTTagCompound) {
        super.readFromNBT(nbt)
        fairyInventory.readFromNBT(nbt.nbtProvider["fairy"].compound ?: NBTTagCompound())
        inputInventory.readFromNBT(nbt.nbtProvider["input"].compound ?: NBTTagCompound())
        resultInventory = (nbt.nbtProvider["result"].compound ?: NBTTagCompound()).readInventory { createInventory(it) }
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


    // Recipe

    fun getArguments(fairyItemStack: ItemStack): IFairyCentrifugeCraftArguments? {
        val fairyType = fairyItemStack.fairyType ?: return null
        return object : IFairyCentrifugeCraftArguments {
            override fun getMana(mana: Mana): Double = fairyType.manaSet[mana] / (fairyType.cost / 50.0)
            override fun getErg(erg: Erg): Double = fairyType.ergSet[erg]
        }
    }

    fun match(): RecipeMatchResult? {
        val recipe = getFairyCentrifugeCraftRecipe(inputInventory) ?: return null
        return RecipeMatchResult(recipe)
    }

    inner class RecipeMatchResult(val recipe: IFairyCentrifugeCraftRecipe) {
        val processeResults = (0 until 3).mapNotNull { index ->
            val process = recipe.getProcess(index) ?: return@mapNotNull null
            val factors = textComponent { process.factors.map { it() }.sandwich { "+"() }.flatten() }
            val arguments = getArguments(fairyInventory[index]) ?: return@mapNotNull ProcessResult(process, factors, false, 0.0, 0.0)
            ProcessResult(
                process,
                factors,
                true,
                process.getScore(arguments) / process.norma,
                0.01 * process.getScore(arguments)
            )
        }

        val ready get() = processeResults.all { it.ready }
        val speed get() = processeResults.map { it.speed }.min() ?: 0.0
        val fortune get() = processeResults.map { it.fortune }.sum()

        inner class ProcessResult(
            val process: IFairyCentrifugeCraftProcess,
            val factors: ITextComponent,
            val ready: Boolean,
            val speed: Double,
            val fortune: Double
        )
    }


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
