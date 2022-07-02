package miragefairy2019.mod.fairybox

import miragefairy2019.api.Erg
import miragefairy2019.api.IFairyCentrifugeCraftArguments
import miragefairy2019.api.IFairyCentrifugeCraftProcess
import miragefairy2019.api.IFairyCentrifugeCraftRecipe
import miragefairy2019.api.Mana
import miragefairy2019.common.toOreName
import miragefairy2019.lib.Alignment
import miragefairy2019.lib.ComponentBackgroundImage
import miragefairy2019.lib.ComponentLabel
import miragefairy2019.lib.ComponentSlot
import miragefairy2019.lib.ComponentTooltip
import miragefairy2019.lib.ContainerComponent
import miragefairy2019.lib.GuiComponent
import miragefairy2019.lib.GuiFactory
import miragefairy2019.lib.IComponent
import miragefairy2019.lib.SlotResult
import miragefairy2019.lib.Symbols
import miragefairy2019.lib.WindowProperty
import miragefairy2019.lib.compound
import miragefairy2019.lib.compoundOrCreate
import miragefairy2019.lib.container
import miragefairy2019.lib.div
import miragefairy2019.lib.factors
import miragefairy2019.lib.fairyCentrifugeCraftHandler
import miragefairy2019.lib.fairyType
import miragefairy2019.lib.get
import miragefairy2019.lib.getFairyCentrifugeCraftRecipe
import miragefairy2019.lib.itemStacks
import miragefairy2019.lib.merge
import miragefairy2019.lib.modinitializer.block
import miragefairy2019.lib.modinitializer.item
import miragefairy2019.lib.modinitializer.module
import miragefairy2019.lib.modinitializer.setCreativeTab
import miragefairy2019.lib.modinitializer.setCustomModelResourceLocation
import miragefairy2019.lib.modinitializer.setUnlocalizedName
import miragefairy2019.lib.modinitializer.tileEntity
import miragefairy2019.lib.nbtProvider
import miragefairy2019.lib.readFromNBT
import miragefairy2019.lib.readInventory
import miragefairy2019.lib.resourcemaker.DataBlockState
import miragefairy2019.lib.resourcemaker.DataBlockStates
import miragefairy2019.lib.resourcemaker.DataElement
import miragefairy2019.lib.resourcemaker.DataFace
import miragefairy2019.lib.resourcemaker.DataFaces
import miragefairy2019.lib.resourcemaker.DataModel
import miragefairy2019.lib.resourcemaker.DataOreIngredient
import miragefairy2019.lib.resourcemaker.DataPoint
import miragefairy2019.lib.resourcemaker.DataResult
import miragefairy2019.lib.resourcemaker.DataShapedRecipe
import miragefairy2019.lib.resourcemaker.DataSimpleIngredient
import miragefairy2019.lib.resourcemaker.makeBlockModel
import miragefairy2019.lib.resourcemaker.makeBlockStates
import miragefairy2019.lib.resourcemaker.makeRecipe
import miragefairy2019.lib.set
import miragefairy2019.lib.writeToNBT
import miragefairy2019.libkt.EMPTY_ITEM_STACK
import miragefairy2019.libkt.GuiHandlerContext
import miragefairy2019.libkt.ISimpleGuiHandler
import miragefairy2019.libkt.RectangleInt
import miragefairy2019.libkt.copyItemStack
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.darkBlue
import miragefairy2019.libkt.enJa
import miragefairy2019.libkt.equalsItemDamageTag
import miragefairy2019.libkt.flatten
import miragefairy2019.libkt.formattedText
import miragefairy2019.libkt.guiHandler
import miragefairy2019.libkt.ingredient
import miragefairy2019.libkt.oreIngredient
import miragefairy2019.libkt.randomInt
import miragefairy2019.libkt.sandwich
import miragefairy2019.libkt.textComponent
import miragefairy2019.libkt.tileEntity
import miragefairy2019.libkt.underline
import miragefairy2019.mod.GuiId
import miragefairy2019.mod.Main
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.artifacts.EnumFairyMaterial
import miragefairy2019.mod.artifacts.FairyMaterials
import miragefairy2019.mod.artifacts.MirageFlower
import miragefairy2019.mod.artifacts.get
import miragefairy2019.mod.artifacts.itemFertilizer
import miragefairy2019.mod.artifacts.oreName
import miragefairy2019.mod.artifacts.sphereType
import miragefairy2019.util.InventoryTileEntity
import miragefairy2019.util.SmartSlot
import mirrg.kotlin.hydrogen.atLeast
import mirrg.kotlin.hydrogen.atMost
import mirrg.kotlin.hydrogen.castOrNull
import mirrg.kotlin.hydrogen.formatAs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.ISidedInventory
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.wrapper.SidedInvWrapper

lateinit var blockFairyCentrifuge: () -> BlockFairyBoxBase
lateinit var itemBlockFairyCentrifuge: () -> ItemBlock

val fairyCentrifugeModule = module {

    // ブロック登録
    blockFairyCentrifuge = block({ BlockFairyBoxBase(4) { TileEntityFairyCentrifuge() } }, "fairy_centrifuge") {
        setUnlocalizedName("fairyCentrifuge")
        setCreativeTab { Main.creativeTab }
        makeBlockStates {
            DataBlockStates(
                variants = listOf("north" to null, "south" to 180, "west" to 270, "east" to 90).associate { facing ->
                    "facing=${facing.first}" to DataBlockState("miragefairy2019:fairy_centrifuge", y = facing.second)
                }
            )
        }
        makeBlockModel {
            DataModel(
                parent = "block/block",
                elements = listOf(
                    DataElement(
                        from = DataPoint(0.0, 0.0, 0.0),
                        to = DataPoint(16.0, 16.0, 16.0),
                        faces = DataFaces(
                            down = DataFace(texture = "#end", cullface = "down"),
                            up = DataFace(texture = "#end", cullface = "up"),
                            north = DataFace(texture = "#side", cullface = "north"),
                            south = DataFace(texture = "#side", cullface = "south"),
                            west = DataFace(texture = "#side", cullface = "west"),
                            east = DataFace(texture = "#side", cullface = "east")
                        )
                    ),
                    DataElement(
                        from = DataPoint(0.0, 0.0, 0.0),
                        to = DataPoint(16.0, 16.0, 16.0),
                        faces = DataFaces(
                            down = DataFace(texture = "#vertical_output", cullface = "down"),
                            up = DataFace(texture = "#vertical_input", cullface = "up"),
                            north = DataFace(texture = "#entrance", cullface = "north"),
                            south = DataFace(texture = "#input", cullface = "south"),
                            west = DataFace(texture = "#output", cullface = "west"),
                            east = DataFace(texture = "#input", cullface = "east")
                        )
                    )
                ),
                textures = mapOf(
                    "particle" to "miragefairy2019:blocks/fairy_wood_log",
                    "end" to "miragefairy2019:blocks/fairy_wood_log_top",
                    "side" to "miragefairy2019:blocks/fairy_wood_log",
                    "entrance" to "miragefairy2019:blocks/fairy_centrifuge_entrance",
                    "input" to "miragefairy2019:blocks/fairy_centrifuge_input",
                    "output" to "miragefairy2019:blocks/fairy_centrifuge_output",
                    "vertical_input" to "miragefairy2019:blocks/fairy_box_input",
                    "vertical_output" to "miragefairy2019:blocks/fairy_box_output2"
                )
            )
        }
    }

    // アイテム登録
    itemBlockFairyCentrifuge = item({ ItemBlock(blockFairyCentrifuge()) }, "fairy_centrifuge") {
        setCustomModelResourceLocation(variant = "facing=north")
        makeRecipe {
            DataShapedRecipe(
                pattern = listOf(
                    "GbG",
                    "cBc",
                    "P#P"
                ),
                key = mapOf(
                    "#" to DataSimpleIngredient(item = "miragefairy2019:fairy_box"),
                    "G" to DataSimpleIngredient(item = "miragefairy2019:gravity_rod"),
                    "P" to DataSimpleIngredient(item = "miragefairy2019:pot"),
                    "B" to DataSimpleIngredient(item = "minecraft:bucket"),
                    "b" to DataSimpleIngredient(item = "minecraft:brewing_stand"),
                    "c" to DataOreIngredient(ore = "mirageFairy2019SphereChemical")
                ),
                result = DataResult(item = "miragefairy2019:fairy_centrifuge")
            )
        }
    }

    // タイルエンティティ登録
    tileEntity("fairy_centrifuge", TileEntityFairyCentrifuge::class.java)

    // Gui登録
    onInit {
        Main.registerGuiHandler(GuiId.fairyBoxCentrifuge, object : ISimpleGuiHandler {
            override fun GuiHandlerContext.onServer() = tileEntity?.castOrNull<TileEntityFairyCentrifuge>()?.createContainer(player)
            override fun GuiHandlerContext.onClient() = tileEntity?.castOrNull<TileEntityFairyCentrifuge>()?.createContainer(player)?.createGui()
        }.guiHandler)
    }

    // 翻訳生成
    onMakeLang {
        enJa("tile.fairyCentrifuge.name", "Fairy Centrifuge", "錬金術師グラヴィーチャの家")
        enJa("tile.fairyCentrifuge.poem", "`Super Strange Theory'", "“超変理論”")
    }

    // 対応レシピ登録
    onAddRecipe {

        // 錬金術

        // グロウストーンダスト10＋ボウル→金3
        fairyCentrifugeCraftHandler(2.0) {
            process { !Mana.WIND + !Erg.FLAME }
            process { !Mana.DARK + !Erg.SPACE }
            process { !Mana.GAIA + !Erg.FREEZE }
            input("dustGlowstone".oreIngredient, 10)
            input(Items.BOWL.ingredient, 1)
            output("ingotGold".toOreName().copyItemStack() ?: cancel(), 3.0)
        }

        // 硫黄3＋赤石10→辰砂6
        fairyCentrifugeCraftHandler(2.0) {
            process { !Mana.WIND + !Erg.FLAME }
            process { !Mana.FIRE + !Erg.CHEMICAL }
            process { !Mana.GAIA + !Erg.WATER }
            input("dustSulfur".oreIngredient, 3)
            input("dustRedstone".oreIngredient, 10)
            output("dustCinnabar".toOreName().copyItemStack() ?: cancel(), 6.0)
        }

        // 石炭の粉64＋フリント8＋黒曜石→ダイヤモンド
        fairyCentrifugeCraftHandler(10.0) {
            process { !Mana.GAIA + !Erg.CRAFT }
            process { !Mana.FIRE + !Erg.SPACE }
            process { !Mana.SHINE + !Erg.CRYSTAL }
            input("dustCoal".oreIngredient, 64)
            input(Items.FLINT.ingredient, 8)
            input("obsidian".oreIngredient, 1)
            output("gemDiamond".toOreName().copyItemStack() ?: cancel(), 1.0)
        }


        // 樹液蒸留

        // 樹液16＋木炭＋ガラス瓶→シロップ
        fairyCentrifugeCraftHandler(2.0) {
            process { !Mana.DARK + !Erg.WATER }
            process { !Mana.GAIA + !Erg.FLAME }
            process { !Mana.AQUA + !Erg.LIFE }
            input("mirageFairyWoodResin".oreIngredient, 16)
            input("gemCharcoal".oreIngredient, 1)
            input(Items.GLASS_BOTTLE.ingredient, 1)
            output(FairyMaterials.itemFairyMaterials[EnumFairyMaterial.fairySyrup].createItemStack(), 1.0)
        }

        // 樹液＋砂糖→砂糖3
        fairyCentrifugeCraftHandler(1.0) {
            process { !Mana.DARK + !Erg.WATER }
            process { !Mana.WIND + !Erg.CRYSTAL }
            process { !Mana.GAIA + !Erg.DESTROY }
            input("mirageFairyWoodResin".oreIngredient, 1)
            input(Items.SUGAR.ingredient, 1)
            output(Items.SUGAR.createItemStack(), 3.0, 1.0)
        }

        // 樹液64＋燐灰石の粉2＋空き瓶→ミラージュエキス入り瓶
        fairyCentrifugeCraftHandler(3.0) {
            process { !Mana.GAIA + !Erg.FLAME }
            process { !Mana.GAIA + !Erg.SPACE }
            process { !Mana.SHINE + !Erg.ATTACK }
            input("mirageFairyWoodResin".oreIngredient, 64)
            input("dustApatite".oreIngredient, 2)
            input(Items.GLASS_BOTTLE.ingredient, 1)
            output(FairyMaterials.itemFairyMaterials[EnumFairyMaterial.bottleMirageFlowerExtract].createItemStack(), 1.0)
        }

        // 樹液64＋辰砂の粉2＋空き瓶→ミラージュオイル入り瓶
        fairyCentrifugeCraftHandler(3.0) {
            process { !Mana.GAIA + !Erg.FLAME }
            process { !Mana.GAIA + !Erg.SPACE }
            process { !Mana.SHINE + !Erg.LEVITATE }
            input("mirageFairyWoodResin".oreIngredient, 64)
            input("dustCinnabar".oreIngredient, 2)
            input(Items.GLASS_BOTTLE.ingredient, 1)
            output(FairyMaterials.itemFairyMaterials[EnumFairyMaterial.bottleMirageFlowerOil].createItemStack(), 1.0)
        }

        // 樹液64＋骨粉8＋黒曜石→妖精のプラスチック
        fairyCentrifugeCraftHandler(5.0) {
            process { !Mana.GAIA + !Erg.FLAME }
            process { !Mana.FIRE + !Erg.SPACE }
            process { !Mana.FIRE + !Erg.CHEMICAL }
            input("mirageFairyWoodResin".oreIngredient, 64)
            input(Items.DYE.createItemStack(metadata = 15).ingredient, 8)
            input("obsidian".oreIngredient, 1)
            output(FairyMaterials.itemFairyMaterials[EnumFairyMaterial.fairyPlastic].createItemStack(), 1.0, 1.0)
            output(FairyMaterials.itemFairyMaterials[EnumFairyMaterial.fairyPlasticWithFairy].createItemStack(), 0.01)
        }


        // マグマクリーム分解

        // マグマクリーム2＋雪玉→スライムボール
        fairyCentrifugeCraftHandler(1.0) {
            process { !Mana.GAIA + !Erg.DESTROY }
            process { !Mana.WIND + !Erg.CHEMICAL }
            process { !Mana.GAIA + !Erg.CRAFT }
            input(Items.MAGMA_CREAM.ingredient, 2)
            input(Items.SNOWBALL.ingredient, 1)
            output(Items.SLIME_BALL.createItemStack(), 1.0)
        }

        // マグマクリーム2＋コンクリートパウダー→ブレイズパウダー
        fairyCentrifugeCraftHandler(1.0) {
            process { !Mana.GAIA + !Erg.DESTROY }
            process { !Mana.WIND + !Erg.CHEMICAL }
            process { !Mana.GAIA + !Erg.CRAFT }
            input(Items.MAGMA_CREAM.ingredient, 2)
            input(Blocks.CONCRETE_POWDER.ingredient!!, 1)
            output(Items.BLAZE_POWDER.createItemStack(), 1.0)
        }


        // 妖精変成

        // R2蜘蛛精＋ミラジウムウォーター＋土→蜘蛛の目
        fairyCentrifugeCraftHandler(1.0) {
            process { !Mana.SHINE + !Erg.KNOWLEDGE }
            process { !Mana.WIND + !Erg.CHEMICAL }
            process { !Mana.SHINE + !Erg.LIFE }
            input("mirageFairy2019FairySpiderRank2".oreIngredient, 1)
            input("container1000MiragiumWater".oreIngredient, 1)
            input("dirt".oreIngredient, 1)
            output(Items.SPIDER_EYE.createItemStack(), 1.0, 1.0)
        }

        // R2ウィザースケルトン精＋ミラジウムウォーター＋スケルトンの頭＋石炭の粉10→ウィザースケルトンの頭
        fairyCentrifugeCraftHandler(10.0) {
            process { !Mana.SHINE + !Erg.KNOWLEDGE }
            process { !Mana.WIND + !Erg.CHEMICAL }
            process { !Mana.SHINE + !Erg.LIFE }
            input("mirageFairy2019FairyWitherSkeletonRank2".oreIngredient, 1)
            input("container1000MiragiumWater".oreIngredient, 1)
            input(Items.SKULL.createItemStack(metadata = 0).ingredient, 1)
            input("dustCoal".oreIngredient, 10)
            output(Items.SKULL.createItemStack(metadata = 1), 1.0)
        }

        // シュルカー精＋ミラジウムウォーター＋エンドストーン16＋ラブラドライト＋空間のスフィア→シュルカーの殻
        fairyCentrifugeCraftHandler(5.0) {
            process { !Mana.SHINE + !Erg.KNOWLEDGE }
            process { !Mana.WIND + !Erg.CHEMICAL }
            process { !Mana.SHINE + !Erg.LIFE }
            input("mirageFairy2019FairyShulkerRank1".oreIngredient, 1)
            input("container1000MiragiumWater".oreIngredient, 1)
            input("endstone".oreIngredient, 16)
            input("gemLabradorite".oreIngredient, 3)
            input(Erg.SPACE.sphereType.oreName.oreIngredient, 1)
            output(Items.SHULKER_SHELL.createItemStack(), 1.0, 0.5)
        }

        // ウィザー精＋ミラジウムウォーター＋ウィザースケルトンの頭3＋ソウルサンド4＋鉄格子32＋ダイヤの剣→ネザースター
        fairyCentrifugeCraftHandler(10.0) {
            process { !Mana.FIRE + !Erg.LIFE }
            process { !Mana.WIND + !Erg.SUBMISSION }
            process { !Mana.FIRE + !Erg.ATTACK }
            input("mirageFairy2019FairyWitherRank1".oreIngredient, 1)
            input("container1000MiragiumWater".oreIngredient, 1)
            input(Items.SKULL.createItemStack(metadata = 1).ingredient, 3)
            input(Blocks.SOUL_SAND.ingredient ?: cancel(), 4)
            input(Blocks.IRON_BARS.ingredient ?: cancel(), 32)
            input(Items.DIAMOND_SWORD.createItemStack().ingredient, 1)
            output(Items.NETHER_STAR.createItemStack(), 1.0)
        }


        // 選鉱

        // 砂利＋ボウル＋水→色々
        fairyCentrifugeCraftHandler(0.5) {
            process { !Mana.DARK + !Erg.SENSE }
            process { !Mana.GAIA + !Erg.WATER }
            process { !Mana.GAIA + !Erg.FLAME }
            input("gravel".oreIngredient, 1)
            input(Items.BOWL.ingredient, 1)
            input("container1000Water".oreIngredient, 1)
            output(Items.FLINT.createItemStack(), 1.0) // 100% フリント
            output(Blocks.SAND.createItemStack(), 0.1, 1.0) // 10% 砂
            output(Blocks.STONE.createItemStack(metadata = 0), 0.1, 1.0) // 10% 石
            output(Blocks.STONE.createItemStack(metadata = 1), 0.1, 1.0) // 10% 花崗岩
            output(Blocks.STONE.createItemStack(metadata = 3), 0.1, 1.0) // 10% 閃緑岩
            output(Blocks.STONE.createItemStack(metadata = 5), 0.1, 1.0) // 10% 安山岩
            output("dustMagnetite".toOreName().copyItemStack() ?: cancel(), 0.05, 2.0) // 5% 磁鉄鉱
            output("crystalCertusQuartz".toOreName().copyItemStack() ?: EMPTY_ITEM_STACK, 0.02, 2.0) // 2% ケルタスクォーツ
            output("ingotGold".toOreName().copyItemStack() ?: cancel(), 0.001, 2.0) // 0.1% 金
        }

        // 土＋ボウル＋水→色々
        fairyCentrifugeCraftHandler(0.5) {
            process { !Mana.DARK + !Erg.SENSE }
            process { !Mana.GAIA + !Erg.WATER }
            process { !Mana.GAIA + !Erg.FLAME }
            input("dirt".oreIngredient, 1)
            input(Items.BOWL.ingredient, 1)
            input("container1000Water".oreIngredient, 1)
            output(Blocks.DIRT.createItemStack(metadata = 1), 1.0) // 100% 荒い土
            output(Blocks.SAND.createItemStack(), 0.1, 1.0) // 10% 砂
            output(Items.CLAY_BALL.createItemStack(), 0.1, 1.0) // 10% 粘土
            output(itemFertilizer().createItemStack(), 0.1, 1.0) // 10% 肥料
            output(Items.WHEAT_SEEDS.createItemStack(), 0.05, 2.0) // 5% 小麦の種
            output(Items.MELON_SEEDS.createItemStack(), 0.01, 2.0) // 1% スイカの種
            output(Items.PUMPKIN_SEEDS.createItemStack(), 0.01, 2.0) // 1% カボチャの種
            output(Items.BEETROOT_SEEDS.createItemStack(), 0.01, 2.0) // 1% ビートルートの種
            output(MirageFlower.itemMirageFlowerSeeds().createItemStack(), 0.01, 2.0) // 1% ミラージュフラワーの種
        }

    }
}

class TileEntityFairyCentrifuge : TileEntityFairyBoxBase(), IInventory, ISidedInventory {

    // Inventory

    fun createInventory(size: Int) = InventoryTileEntity(this, "tile.fairyCentrifuge.name", false, size)

    val fairyInventory = createInventory(3).apply { filter = { it.fairyType != null } }.apply { inventoryStackLimit = 1 }
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

    override fun getDropItemStacks() = listOf(fairyInventory, inputInventory, resultInventory, outputInventory).flatMap { it.itemStacks }


    // IWorldNameable

    override fun getName() = "tile.fairyCentrifuge.name"
    override fun hasCustomName() = false
    override fun getDisplayName() = textComponent { translate(name) }


    // IInventory

    override fun getSizeInventory() = 18
    override fun isEmpty() = inputInventory.isEmpty && outputInventory.isEmpty
    override fun getStackInSlot(index: Int): ItemStack = if (index < 9) inputInventory.getStackInSlot(index) else outputInventory.getStackInSlot(index - 9)
    override fun decrStackSize(index: Int, count: Int): ItemStack = if (index < 9) inputInventory.decrStackSize(index, count) else outputInventory.decrStackSize(index - 9, count)
    override fun removeStackFromSlot(index: Int): ItemStack = if (index < 9) inputInventory.removeStackFromSlot(index) else outputInventory.removeStackFromSlot(index - 9)
    override fun setInventorySlotContents(index: Int, itemStack: ItemStack) = if (index < 9) inputInventory.setInventorySlotContents(index, itemStack) else outputInventory.setInventorySlotContents(index - 9, itemStack)
    override fun getInventoryStackLimit() = 64

    override fun markDirty() = super.markDirty()

    override fun isUsableByPlayer(player: EntityPlayer) = inputInventory.isUsableByPlayer(player) && outputInventory.isUsableByPlayer(player)

    override fun openInventory(player: EntityPlayer) = Unit
    override fun closeInventory(player: EntityPlayer) = Unit

    override fun isItemValidForSlot(index: Int, itemStack: ItemStack) = when (index) {
        in 0 until 9 -> inputInventory.isItemValidForSlot(index - 0, itemStack)
        else -> false
    }

    override fun getField(id: Int) = 0
    override fun setField(id: Int, value: Int) = Unit
    override fun getFieldCount() = 0

    override fun clear() {
        inputInventory.clear()
        outputInventory.clear()
    }


    // ISidedInventory

    override fun getSlotsForFace(facing: EnumFacing) = when (facing / getFacing()) {
        EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.UP -> (0 until 9).toList().toIntArray()
        EnumFacing.EAST, EnumFacing.DOWN -> (9 until 18).toList().toIntArray()
        else -> intArrayOf()
    }

    override fun canInsertItem(index: Int, itemStack: ItemStack, facing: EnumFacing): Boolean {
        if (!isItemValidForSlot(index, itemStack)) return false
        if (index >= 9) return true
        if (!inputInventory[index].isEmpty) return true
        (0 until 9).forEach { i ->
            if (i != index) {
                if (inputInventory[i] equalsItemDamageTag itemStack) return false
            }
        }
        return true
    }

    override fun canExtractItem(index: Int, itemStack: ItemStack, facing: EnumFacing) = true

    @Suppress("UNCHECKED_CAST")
    override fun <T> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing != null) {
                return SidedInvWrapper(this, facing) as T
            }
        }
        return super.getCapability(capability, facing)
    }

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?) = capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing)


    // Tree

    fun getLeaves() = try {
        compileFairyTree(world, pos)
    } catch (e: TreeCompileException) {
        null
    }

    fun getFolia(times: Int): Double {
        return getAuraCollectionSpeed(world, getLeaves() ?: return 0.0, times) atMost 120.0
    }

    fun getFoliaSpeedFactor(folia: Double) = ((folia atMost 300.0) - 30.0) / 30.0 atLeast 0.0


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
        val processesResults = (0 until 3).mapNotNull { index ->
            val process = recipe.getProcess(index) ?: return@mapNotNull null
            val factors = textComponent { process.factors.map { it() }.sandwich { "+"() }.flatten() } // TODO icon
            val arguments = getArguments(fairyInventory[index]) ?: return@mapNotNull ProcessResult(process, factors, false, 0.0)
            ProcessResult(process, factors, true, process.getScore(arguments))
        }

        val ready get() = processesResults.all { it.ready }
        val speed get() = processesResults.map { it.speed }.min() ?: 0.0
        val fortune get() = processesResults.map { it.fortune }.sum()

        inner class ProcessResult(
            val process: IFairyCentrifugeCraftProcess,
            val factors: ITextComponent,
            val ready: Boolean,
            val score: Double
        ) {
            val speed = score * 0.01 / recipe.handler.cost
            val fortune = score * 0.01
        }
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

    fun createContainer(player: EntityPlayer) = container(GuiFactory { GuiContainerFairyCentrifuge(it) }) {

        // Guiを開いたときにサーバー側でフォリアを計測する機能
        val propertyMilliFolia = WindowProperty()
        if (!world.isRemote) propertyMilliFolia.value = (getFolia(10000) * 10.0).toInt() // TODO 桁数を増やす（内部的に通信時にshortにされるので普通にやるとオーバーフローする）
        windowProperties[0] = propertyMilliFolia
        fun getFolia() = propertyMilliFolia.value / 10.0 // ローカル変数にすると計測したフォリアを同期できない
        fun getFoliaSpeedFactor() = getFoliaSpeedFactor(getFolia())


        // レシピ判定機能
        var recipeMatchResult: RecipeMatchResult? = null
        components += object : IComponent {
            override fun drawGuiContainerForegroundLayer(gui: GuiComponent, mouseX: Int, mouseY: Int) {
                recipeMatchResult = match()
            }
        }


        val IN = ContainerComponent.SlotGroup()
        val FAIRY = ContainerComponent.SlotGroup()
        val OUT = ContainerComponent.SlotGroup()
        val PLAYER_HOTBAR = ContainerComponent.SlotGroup()
        val PLAYER = ContainerComponent.SlotGroup()


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
            components += ComponentSlot(this, 3 + 4 + 18 * c, yi) { x, y -> Slot(inputInventory, c, x, y) } belongs IN
            components += ComponentBackgroundImage(3 + 4 + 18 * c + 1, yi + 1, 0x60FFFFFF) { TEXTURE_INPUT_SLOT }
        }
        yi += 18

        // 妖精
        fun defineProcess(index: Int) {
            fun getProcessResult() = recipeMatchResult?.processesResults?.getOrNull(index)
            val c = 1 + 3 * index
            // TODO 名前が消えた分の整頓
            components += ComponentLabel(3 + 4 + 18 * c + 9, yi + 18 * 0 + 9, Alignment.CENTER) { getProcessResult()?.factors }
            components += ComponentSlot(this, 3 + 4 + 18 * c, yi + 18 * 1) { x, y -> SmartSlot(fairyInventory, index, x, y) } belongs FAIRY
            components += ComponentBackgroundImage(3 + 4 + 18 * c + 1, yi + 18 * 1 + 1, 0x60FFFFFF) { TEXTURE_FAIRY_SLOT }
            components += ComponentLabel(3 + 4 + 18 * c + 9, yi + 18 * 2, Alignment.CENTER) {
                val processResult = getProcessResult() ?: return@ComponentLabel null
                textComponent { (processResult.score formatAs "%.0f")().darkBlue.underline }
            }
            components += ComponentTooltip(RectangleInt(3 + 4 + 18 * c, yi + 18 * 2, 18, 9)) {
                val processResult = getProcessResult() ?: return@ComponentTooltip null
                listOf(
                    formattedText { "速度: ${processResult.speed * getFoliaSpeedFactor() formatAs "%.2f"}"() }, // TODO translate
                    formattedText { "幸運: ${processResult.fortune formatAs "%+.2f"}"() } // TODO translate
                )
            }
            // TODO 幸運が消えた分の整頓
        }
        defineProcess(0)
        defineProcess(1)
        defineProcess(2)
        yi += 18 * 3

        // 出力
        repeat(9) { c ->
            components += ComponentSlot(this, 3 + 4 + 18 * c, yi) { x, y -> SlotResult(player, outputInventory, c, x, y) } belongs OUT
            components += ComponentBackgroundImage(3 + 4 + 18 * c + 1, yi + 1, 0x60FFFFFF) { TEXTURE_OUTPUT_SLOT }
        }
        yi += 18

        // 速度
        components += ComponentLabel(3 + 4 + 18 * 1 + 9, yi, Alignment.CENTER) {
            val recipeMatchResult = recipeMatchResult ?: return@ComponentLabel null
            textComponent { (recipeMatchResult.speed * getFoliaSpeedFactor() formatAs "%.2f/分")() } // TODO translate
        }
        components += ComponentLabel(3 + 4 + 18 * 4 + 9, yi, Alignment.CENTER) {
            val recipeMatchResult = recipeMatchResult ?: return@ComponentLabel null
            textComponent { (recipeMatchResult.fortune formatAs "%.2f" + Symbols.FORTUNE)() } // TODO translate
        }
        yi += 9

        // フォリア表示
        components += ComponentLabel(width - 3 - 4, yi, Alignment.RIGHT) {
            textComponent { "${getFolia() formatAs "%.1f"} Folia, 加工速度: ${getFoliaSpeedFactor() * 100.0 formatAs "%.2f"}%"() } // TODO translate
        }
        yi += 9


        // プレイヤーインベントリタイトル
        yi += 2
        components += ComponentLabel(3 + 4, yi, Alignment.LEFT) { player.inventory.displayName }
        yi += 8
        yi += 2


        // プレイヤーインベントリメイン
        repeat(3) { r -> repeat(9) { c -> components += ComponentSlot(this, 3 + 4 + 18 * c, yi + 18 * r) { x, y -> Slot(player.inventory, 9 + 9 * r + c, x, y) } belongs PLAYER } }
        yi += 18 * 3

        yi += 4

        // プレイヤーインベントリ最下段
        repeat(9) { c -> components += ComponentSlot(this, 3 + 4 + 18 * c, yi) { x, y -> Slot(player.inventory, c, x, y) } belongs PLAYER_HOTBAR }
        yi += 18

        yi += 4

        yi += 3
        height = yi


        interactInventories += inputInventory
        interactInventories += outputInventory


        addSlotTransferMapping(IN, PLAYER_HOTBAR, true)
        addSlotTransferMapping(IN, PLAYER, true)
        addSlotTransferMapping(FAIRY, PLAYER_HOTBAR, true)
        addSlotTransferMapping(FAIRY, PLAYER, true)
        addSlotTransferMapping(OUT, PLAYER_HOTBAR, true)
        addSlotTransferMapping(OUT, PLAYER, true)
        addSlotTransferMapping(PLAYER_HOTBAR, FAIRY)
        addSlotTransferMapping(PLAYER_HOTBAR, IN)
        addSlotTransferMapping(PLAYER, FAIRY)
        addSlotTransferMapping(PLAYER, IN)

    }

}

@SideOnly(Side.CLIENT)
class GuiContainerFairyCentrifuge(container: ContainerComponent) : GuiComponent(container)
