package miragefairy2019.modkt.modules.fairy

import miragefairy2019.libkt.Module
import miragefairy2019.libkt.copy
import miragefairy2019.libkt.item
import miragefairy2019.libkt.setCreativeTab
import miragefairy2019.libkt.setUnlocalizedName
import miragefairy2019.mod.ModMirageFairy2019
import miragefairy2019.mod.api.fairy.ApiFairy
import miragefairy2019.mod.api.fairy.IItemFairy
import miragefairy2019.mod.api.main.ApiMain
import miragefairy2019.mod.common.fairy.relation.FairyRelationRegistry
import miragefairy2019.mod.lib.OreIngredientComplex
import miragefairy2019.mod.lib.UtilsMinecraft
import miragefairy2019.mod.modules.main.ModuleMain
import miragefairy2019.modkt.api.erg.ErgTypes
import miragefairy2019.modkt.api.playeraura.IFoodAuraContainer
import miragefairy2019.modkt.impl.div
import miragefairy2019.modkt.impl.fairy.erg
import mirrg.boron.util.UtilsString
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.EntityLivingBase
import net.minecraft.init.Items
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemFood
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.Ingredient
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.NonNullList
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.common.util.Constants.NBT
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.oredict.OreIngredient
import net.minecraftforge.oredict.ShapelessOreRecipe
import net.minecraftforge.registries.IForgeRegistryEntry
import java.util.Optional
import java.util.function.Predicate
import java.util.function.Supplier

lateinit var itemBakedFairy: Supplier<ItemBakedFairy>

object ModuleFairy {
    lateinit var creativeTab: CreativeTabs
    lateinit var listItemFairy: List<ItemFairy>

    val init: Module = {

        // 妖精関係性レジストリ
        onInstantiation {
            ApiFairy.fairyRelationRegistry = FairyRelationRegistry()
        }

        // 妖精クリエイティブタブ
        onInitCreativeTab {
            creativeTab = object : CreativeTabs("mirageFairy2019.fairy") {
                @SideOnly(Side.CLIENT)
                override fun getTabIconItem() = FairyTypes.instance.magentaglazedterracotta.main.createItemStack()

                @SideOnly(Side.CLIENT)
                override fun displayAllRelevantItems(itemStacks: NonNullList<ItemStack>) {
                    FairyTypes.instance.variants.forEach { variant ->
                        listItemFairy.forEachIndexed { i, _ ->
                            itemStacks.add(variant.y[i].createItemStack())
                        }
                    }
                }
            }
        }

        // 妖精アイテム
        onRegisterItem {
            val rankMax = 7

            // 妖精タイプ登録
            FairyTypes.instance = FairyTypes(rankMax)

            // 妖精
            listItemFairy = (1..rankMax).map { rank ->
                val item = ItemFairy()
                item.setRegistryName(ModMirageFairy2019.MODID, if (rank == 1) "mirage_fairy" else "mirage_fairy_r$rank")
                item.unlocalizedName = "mirageFairyR$rank"
                item.creativeTab = creativeTab // TODO 冗長説
                FairyTypes.instance.variants.forEach { item.registerVariant(it.x, it.y[rank - 1]) }
                ForgeRegistries.ITEMS.register(item)
                if (ApiMain.side().isClient) {
                    item.variants.forEach { ModelLoader.setCustomModelResourceLocation(item, it.x, ModelResourceLocation(ResourceLocation(ModMirageFairy2019.MODID, "fairy"), "normal")) }
                }
                item
            }

        }
        onRegisterItemColorHandler {
            object {
                @SideOnly(Side.CLIENT)
                fun run() {

                    // 妖精アイテムのカスタム色
                    listItemFairy.forEachIndexed { i, item ->
                        @SideOnly(Side.CLIENT)
                        class ItemColorImpl : IItemColor {
                            override fun colorMultiplier(itemStack: ItemStack, tintIndex: Int): Int {
                                val variant = item.getVariant(itemStack).orElse(null) ?: return 0xFFFFFF
                                return when (tintIndex) {
                                    0 -> variant.colorSet.skin
                                    1 -> when (i) {
                                        0 -> 0xFF8888
                                        1 -> 0x8888FF
                                        2 -> 0x88FF88
                                        3 -> 0xFFFF88
                                        4 -> 0x111111
                                        5 -> 0xFFFFFF
                                        6 -> 0x88FFFF
                                        else -> 0xFFFFFF
                                    }
                                    2 -> variant.colorSet.dark
                                    3 -> variant.colorSet.bright
                                    4 -> variant.colorSet.hair
                                    else -> 0xFFFFFF
                                }
                            }
                        }
                        Minecraft.getMinecraft().itemColors.registerItemColorHandler(ItemColorImpl(), item)
                    }

                }
            }.run()
        }

        // 妖精の鉱石辞書
        onCreateItemStack {
            FairyTypes.instance.variants.forEach { variant ->
                listItemFairy.forEachIndexed { i, _ ->

                    // 品種別
                    OreDictionary.registerOre("mirageFairy2019Fairy${UtilsString.toUpperCaseHead(variant.y[i].type.breed!!.resourcePath)}Rank${i + 1}", variant.y[i].createItemStack())

                    // エルグ別
                    variant.y[i].type.ergSet.entries.forEach {
                        if (it.power >= 10) {
                            OreDictionary.registerOre("mirageFairy2019FairyAbility${UtilsString.toUpperCaseHead(it.type.name)}", variant.y[i].createItemStack())
                        }
                    }

                }
            }
        }

        // 凝縮・分散レシピ
        onAddRecipe {
            FairyTypes.instance.variants.forEach { variant ->
                (0 until listItemFairy.size - 1).forEach { i ->

                    // 凝縮
                    GameRegistry.addShapelessRecipe(
                            ResourceLocation("${ModMirageFairy2019.MODID}:condense_r${i}_fairy_${variant.y[i].type.breed!!.resourcePath}"),
                            ResourceLocation("${ModMirageFairy2019.MODID}:condense_r${i}_fairy_${variant.y[i].type.breed!!.resourcePath}"),
                            variant.y[i + 1].createItemStack(),
                            Ingredient.fromStacks(variant.y[i].createItemStack()),
                            Ingredient.fromStacks(variant.y[i].createItemStack()),
                            Ingredient.fromStacks(variant.y[i].createItemStack()),
                            Ingredient.fromStacks(variant.y[i].createItemStack()),
                            Ingredient.fromStacks(variant.y[i].createItemStack()),
                            Ingredient.fromStacks(variant.y[i].createItemStack()),
                            Ingredient.fromStacks(variant.y[i].createItemStack()),
                            Ingredient.fromStacks(variant.y[i].createItemStack()))

                    // 分解
                    GameRegistry.addShapelessRecipe(
                            ResourceLocation("${ModMirageFairy2019.MODID}:decondense_r${i}_fairy_${variant.y[i].type.breed!!.resourcePath}"),
                            ResourceLocation("${ModMirageFairy2019.MODID}:decondense_r${i}_fairy_${variant.y[i].type.breed!!.resourcePath}"),
                            variant.y[i].createItemStack(8),
                            Ingredient.fromStacks(variant.y[i + 1].createItemStack()))

                }
            }
        }

        // 確定レシピ
        onAddRecipe {
            var counter = 0
            ApiFairy.fairyRelationRegistry.ingredientFairyRelations.forEach { relation ->
                if (relation.relevance >= 1) {

                    // ダイヤモンド使用
                    GameRegistry.findRegistry(IRecipe::class.java).register(ShapelessOreRecipe(
                            ResourceLocation(ModMirageFairy2019.MODID, "mirage_fairy_from_item_$counter"),
                            relation.itemStackFairy,
                            OreIngredientComplex("mirageFairy2019CraftingToolFairyWandSummoning"),
                            OreIngredient("mirageFairyCrystal"),
                            relation.ingredient).setRegistryName(ModMirageFairy2019.MODID, "mirage_fairy_from_item_$counter"))
                    counter++

                    // 召喚のワンド使用
                    GameRegistry.findRegistry(IRecipe::class.java).register(ShapelessOreRecipe(
                            ResourceLocation(ModMirageFairy2019.MODID, "mirage_fairy_from_summoning_fairy_wand_$counter"),
                            relation.itemStackFairy,
                            OreIngredient("gemDiamond"),
                            OreIngredient("mirageFairyCrystal"),
                            relation.ingredient).setRegistryName(ModMirageFairy2019.MODID, "mirage_fairy_from_summoning_fairy_wand_$counter"))
                    counter++

                }
            }
        }

        // 妖精一覧デバッグアイテム
        item({ ItemDebugFairyList() }, "debug_fairy_list") {
            setUnlocalizedName("debugFairyList")
            setCreativeTab { ModuleMain.creativeTab }
            modInitializer.onRegisterItem {
                if (ApiMain.side().isClient) {
                    ModelLoader.setCustomModelResourceLocation(item, 0, ModelResourceLocation("minecraft:book", "normal"))
                }
            }
        }

        // 焼き妖精
        itemBakedFairy = item({ ItemBakedFairy() }, "baked_fairy") {
            setUnlocalizedName("bakedFairy")
            setCreativeTab { ModuleMain.creativeTab }
            modInitializer.onRegisterItem {
                if (ApiMain.side().isClient) {
                    ModelLoader.setCustomModelResourceLocation(item, 0, ModelResourceLocation(item.registryName!!, "normal"))
                }
            }
        }

        // 焼き妖精のカスタム色
        onRegisterItemColorHandler {
            object {
                @SideOnly(Side.CLIENT)
                fun run() {
                    @SideOnly(Side.CLIENT)
                    class ItemColorImpl : IItemColor {
                        override fun colorMultiplier(itemStack: ItemStack, tintIndex: Int): Int {
                            val fairyItemStack = (ItemBakedFairy.getFairy(itemStack) ?: return 0xFFFFFF)
                            val variant = listItemFairy[0].getVariant(fairyItemStack).orElse(null) ?: return 0xFFFFFF
                            return when (tintIndex) {
                                0 -> 0xFFFFFF
                                1 -> variant.colorSet.skin
                                2 -> 0xFFFFFF
                                3 -> variant.colorSet.dark
                                4 -> variant.colorSet.bright
                                5 -> variant.colorSet.hair
                                6 -> 0xFFFFFF
                                else -> 0xFFFFFF
                            }
                        }
                    }
                    Minecraft.getMinecraft().itemColors.registerItemColorHandler(ItemColorImpl(), itemBakedFairy.get())
                }
            }.run()
        }

        // 焼き妖精レシピ
        onAddRecipe {
            ForgeRegistries.RECIPES.register(RecipeFairyBaking())
        }

    }
}

val ItemStack.fairyType get() = (item as? IItemFairy)?.getMirageFairy2019Fairy(this)?.orElse(null) // TODO 移動

class ItemBakedFairy : ItemFood(0, 0.0f, false), IFoodAuraContainer {
    companion object {
        fun getFairy(itemStack: ItemStack): ItemStack? {
            if (!itemStack.hasTagCompound()) return null
            val nbt = itemStack.tagCompound!!
            if (!nbt.hasKey("Fairy", NBT.TAG_COMPOUND)) return null
            val fairy = nbt.getCompoundTag("Fairy")
            if (!fairy.hasKey("CombinedFairy", NBT.TAG_COMPOUND)) return null
            return ItemStack(fairy.getCompoundTag("CombinedFairy"))
        }

        fun setFairy(itemStack: ItemStack, itemStackFairy: ItemStack) {
            if (!itemStack.hasTagCompound()) itemStack.tagCompound = NBTTagCompound()
            val nbt = itemStack.tagCompound!!
            if (!nbt.hasKey("Fairy", NBT.TAG_COMPOUND)) nbt.setTag("Fairy", NBTTagCompound())
            val fairy = nbt.getCompoundTag("Fairy")
            fairy.setTag("CombinedFairy", itemStackFairy.writeToNBT(NBTTagCompound()))
        }
    }


    override fun getItemStackDisplayName(itemStack: ItemStack): String = getFairy(itemStack)?.fairyType?.let { UtilsMinecraft.translateToLocalFormatted("$unlocalizedName.format", it.displayName.formattedText) } ?: UtilsMinecraft.translateToLocal("$unlocalizedName.name")

    // TODO 専用クリエイティブタブ
    override fun getSubItems(tab: CreativeTabs, items: NonNullList<ItemStack>) {
        if (!isInCreativeTab(tab)) return
        val itemStack = ItemStack(this)
        setFairy(itemStack, FairyTypes.instance.magentaglazedterracotta.main.createItemStack())
        items += itemStack
    }


    override fun getHealAmount(itemStack: ItemStack) = 1 + (getFairy(itemStack)?.fairyType?.let { it.erg(ErgTypes.food).toInt() / 4 } ?: 0)
    override fun getSaturationModifier(itemStack: ItemStack) = getHealAmount(itemStack) * 0.1f
    override fun getMaxItemUseDuration(itemStack: ItemStack) = 16
    override fun onItemUseFinish(itemStack: ItemStack, world: World, entity: EntityLivingBase): ItemStack {
        super.onItemUseFinish(itemStack, world, entity)
        return if (!itemStack.isEmpty) {
            if (!world.isRemote) entity.entityDropItem(ItemStack(Items.BOWL), 0.0f)
            itemStack
        } else {
            ItemStack(Items.BOWL)
        }
    }


    override fun getFoodAura(itemStack: ItemStack) = Optional.ofNullable(getFairy(itemStack)?.fairyType?.let { it.manaSet / (it.cost / 50.0) })
}

class RecipeFairyBaking : IForgeRegistryEntry.Impl<IRecipe>(), IRecipe {
    init {
        registryName = ResourceLocation(ModMirageFairy2019.MODID, "fairy_baking")
    }

    private data class MatchResult(var itemStackWand: ItemStack, var itemStackFairy: ItemStack)

    private fun match(inventoryCrafting: InventoryCrafting): MatchResult? {
        val used = (0 until inventoryCrafting.sizeInventory).map { false }.toMutableList()

        fun <T : Any> pull(function: (ItemStack) -> T?): T? {
            repeat(inventoryCrafting.sizeInventory) { i ->
                if (!used[i]) {
                    val itemStack: ItemStack = inventoryCrafting.getStackInSlot(i)
                    val result = function(itemStack)
                    if (result != null) {
                        used[i] = true
                        return result
                    }
                }
            }
            return null
        }

        fun find(predicate: (ItemStack) -> Boolean) = pull { if (predicate(it)) it else null }
        fun find(predicate: Predicate<ItemStack>) = find { predicate.test(it) }

        // 探索
        val itemStackWand = find(OreIngredientComplex("mirageFairy2019CraftingToolFairyWandMelting")) ?: return null // 紅蓮杖
        val itemStackFairy = find { it.item == ModuleFairy.listItemFairy[0] } ?: return null // 妖精
        find(Ingredient.fromItem(Items.SUGAR)) ?: return null // 砂糖
        find(Ingredient.fromItem(Items.BOWL)) ?: return null // ボウル

        // 余りがあってはならない
        repeat(inventoryCrafting.sizeInventory) { i ->
            if (!used[i]) {
                if (!inventoryCrafting.getStackInSlot(i).isEmpty) {
                    return null
                }
            }
        }

        return MatchResult(itemStackWand, itemStackFairy)
    }


    override fun matches(inventoryCrafting: InventoryCrafting, world: World) = match(inventoryCrafting) != null

    override fun getCraftingResult(inventoryCrafting: InventoryCrafting): ItemStack {
        val result = match(inventoryCrafting) ?: return ItemStack.EMPTY
        val itemStackBakedFairy = ItemStack(itemBakedFairy.get())
        ItemBakedFairy.setFairy(itemStackBakedFairy, result.itemStackFairy.copy(1))
        return itemStackBakedFairy
    }

    override fun getRemainingItems(inventoryCrafting: InventoryCrafting): NonNullList<ItemStack> {
        val result = match(inventoryCrafting) ?: return NonNullList.create()
        val list = NonNullList.withSize(inventoryCrafting.sizeInventory, ItemStack.EMPTY)
        list.forEachIndexed { i, _ ->
            list[i] = ForgeHooks.getContainerItem(inventoryCrafting.getStackInSlot(i))
        }
        return list
    }

    override fun getRecipeOutput(): ItemStack = ItemStack.EMPTY
    override fun isDynamic() = true
    override fun canFit(width: Int, height: Int) = width * height >= 2
}
