package miragefairy2019.mod.fairybox

import miragefairy2019.api.Erg
import miragefairy2019.api.IFairyCentrifugeCraftArguments
import miragefairy2019.api.IFairyCentrifugeCraftProcess
import miragefairy2019.api.IFairyCentrifugeCraftRecipe
import miragefairy2019.api.Mana
import miragefairy2019.lib.Alignment
import miragefairy2019.lib.ComponentBackgroundLabel
import miragefairy2019.lib.ComponentLabel
import miragefairy2019.lib.ComponentSlot
import miragefairy2019.lib.GuiComponent
import miragefairy2019.lib.IComponent
import miragefairy2019.lib.SlotResult
import miragefairy2019.lib.Symbols
import miragefairy2019.lib.WindowProperty
import miragefairy2019.lib.compound
import miragefairy2019.lib.compoundOrCreate
import miragefairy2019.lib.container
import miragefairy2019.lib.factors
import miragefairy2019.lib.fairyType
import miragefairy2019.lib.get
import miragefairy2019.lib.getFairyCentrifugeCraftRecipe
import miragefairy2019.lib.itemStacks
import miragefairy2019.lib.merge
import miragefairy2019.lib.nbtProvider
import miragefairy2019.lib.readFromNBT
import miragefairy2019.lib.readInventory
import miragefairy2019.lib.set
import miragefairy2019.lib.writeToNBT
import miragefairy2019.libkt.flatten
import miragefairy2019.libkt.gray
import miragefairy2019.libkt.randomInt
import miragefairy2019.libkt.sandwich
import miragefairy2019.libkt.textComponent
import miragefairy2019.mod.GuiId
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.util.InventoryTileEntity
import mirrg.kotlin.atLeast
import mirrg.kotlin.atMost
import mirrg.kotlin.formatAs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Slot
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

    override fun getDropItemStacks() = listOf(fairyInventory, inputInventory, resultInventory, outputInventory).map { it.itemStacks }.flatten()


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

                val foliaSpeedFactor = getFoliaSpeedFactor(getFolia(10)) // 妖精の木による速度倍率

                if (!merge(resultInventory, outputInventory)) return // 出力スロットが溢れている場合は中止

                val matchResult = match() ?: return // レシピが無効な場合は中止

                val times = world.rand.randomInt(matchResult.speed * foliaSpeedFactor) // 回数判定

                repeat(times) {

                    val result = matchResult.recipe.craft(world.rand, matchResult.fortune) ?: return // クラフトが失敗した場合は中止

                    // リザルトをインベントリに移す
                    resultInventory = createInventory(result.size)
                    result.forEachIndexed { i, itemStack -> resultInventory[i] = itemStack }

                    if (!merge(resultInventory, outputInventory)) return // 出力スロットが溢れている場合は中止

                }

            }
        }
    }


    // Gui

    fun createContainer(player: EntityPlayer) = container {

        // Guiを開いたときにサーバー側でフォリアを計測する機能
        val propertyMilliFolia = WindowProperty()
        if (!world.isRemote) propertyMilliFolia.value = (getFolia(10000) * 1000.0).toInt()
        windowProperties[0] = propertyMilliFolia
        fun getFolia() = propertyMilliFolia.value / 1000.0 // ローカル変数にすると計測したフォリアを同期できない
        fun getFoliaSpeedFactor() = getFoliaSpeedFactor(getFolia())


        // レシピ判定機能
        var recipeMatchResult: RecipeMatchResult? = null
        components += object : IComponent {
            override fun drawGuiContainerForegroundLayer(gui: GuiComponent, mouseX: Int, mouseY: Int) {
                recipeMatchResult = match()
            }
        }


        width = 3 + 4 + 18 * 9 + 4 + 3


        var yi = 0
        yi += 3

        yi += 2 // 文字マージン


        // Guiタイトル
        components += ComponentLabel(width / 2, yi, Alignment.CENTER) { inputInventory.displayName }
        yi += 8
        yi += 2


        // 入力
        repeat(9) { c ->
            components += ComponentSlot(this, 3 + 4 + 18 * c, yi) { x, y -> Slot(inputInventory, c, x, y) }
            components += ComponentBackgroundLabel(3 + 4 + 18 * c + 9, yi + 9 - 4, Alignment.CENTER) { textComponent { "IN"().gray } }
        }
        yi += 18

        // 妖精
        fun defineProcess(index: Int) {
            fun getProcessResult() = recipeMatchResult?.processeResults?.getOrNull(index)
            val c = 1 + 3 * index
            components += ComponentLabel(3 + 4 + 18 * c + 9, yi + 18 * 0, Alignment.CENTER) { getProcessResult()?.process?.name }
            components += ComponentLabel(3 + 4 + 18 * c + 9, yi + 18 * 0 + 9, Alignment.CENTER) { getProcessResult()?.factors }
            components += ComponentSlot(this, 3 + 4 + 18 * c, yi + 18 * 1) { x, y -> Slot(fairyInventory, index, x, y) }
            components += ComponentBackgroundLabel(3 + 4 + 18 * c + 9, yi + 18 * 1 + 9 - 4, Alignment.CENTER) { textComponent { "F"().gray } }
            components += ComponentLabel(3 + 4 + 18 * c + 9, yi + 18 * 2, Alignment.CENTER) { getProcessResult()?.speed?.let { textComponent { (it * getFoliaSpeedFactor()).formatAs("%.2f/分")() } } } // TODO translate
            components += ComponentLabel(3 + 4 + 18 * c + 9, yi + 18 * 2 + 9, Alignment.CENTER) { getProcessResult()?.fortune?.let { textComponent { it.formatAs("%.02f" + Symbols.FORTUNE)() } } }
        }
        defineProcess(0)
        defineProcess(1)
        defineProcess(2)
        yi += 18 * 3

        // 出力
        repeat(9) { c ->
            components += ComponentSlot(this, 3 + 4 + 18 * c, yi) { x, y -> SlotResult(player, outputInventory, c, x, y) }
            components += ComponentBackgroundLabel(3 + 4 + 18 * c + 9, yi + 9 - 4, Alignment.CENTER) { textComponent { "OUT"().gray } }
        }
        yi += 18

        // 速度
        components += ComponentLabel(3 + 4 + 18 * 1 + 9, yi, Alignment.CENTER) { recipeMatchResult?.speed?.let { textComponent { (it * getFoliaSpeedFactor()).formatAs("%.2f/分")() } } }
        components += ComponentLabel(3 + 4 + 18 * 4 + 9, yi, Alignment.CENTER) { recipeMatchResult?.fortune?.let { textComponent { it.formatAs("%.02f" + Symbols.FORTUNE)() } } }
        yi += 9

        // フォリア表示
        components += ComponentLabel(width - 3 - 4, yi, Alignment.RIGHT) { textComponent { "${getFolia() formatAs "%.2f"} Folia, 速度ブースト: ${getFoliaSpeedFactor() * 100.0 formatAs "%.2f"}%"() } }
        yi += 9


        // プレイヤーインベントリタイトル
        yi += 2
        components += ComponentLabel(3 + 4, yi, Alignment.LEFT) { player.inventory.displayName }
        yi += 8
        yi += 2


        // プレイヤーインベントリメイン
        repeat(3) { r -> repeat(9) { c -> components += ComponentSlot(this, 3 + 4 + 18 * c, yi + 18 * r) { x, y -> Slot(player.inventory, 9 + 9 * r + c, x, y) } } }
        yi += 18 * 3

        yi += 4

        // プレイヤーインベントリ最下段
        repeat(9) { c -> components += ComponentSlot(this, 3 + 4 + 18 * c, yi) { x, y -> Slot(player.inventory, c, x, y) } }
        yi += 18

        yi += 4

        yi += 3
        height = yi


        interactInventories += inputInventory
        interactInventories += outputInventory

    }

}
