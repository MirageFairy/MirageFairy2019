package miragefairy2019.mod.fairyrelation

import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.ingredient
import miragefairy2019.libkt.module
import miragefairy2019.mod.artifacts.MirageFlower
import miragefairy2019.mod.fairy.FairyTypes
import miragefairy2019.mod.fairy.RankedFairyTypeBundle
import miragefairy2019.mod.material.CompressedMaterials
import miragefairy2019.mod.material.EnumVariantMaterials1
import net.minecraft.block.Block
import net.minecraft.block.BlockDoublePlant
import net.minecraft.block.BlockOldLeaf
import net.minecraft.block.BlockOldLog
import net.minecraft.block.BlockPlanks
import net.minecraft.block.state.IBlockState
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.boss.EntityDragon
import net.minecraft.entity.boss.EntityWither
import net.minecraft.entity.monster.EntityBlaze
import net.minecraft.entity.monster.EntityCreeper
import net.minecraft.entity.monster.EntityEnderman
import net.minecraft.entity.monster.EntityIronGolem
import net.minecraft.entity.monster.EntityMagmaCube
import net.minecraft.entity.monster.EntityShulker
import net.minecraft.entity.monster.EntitySkeleton
import net.minecraft.entity.monster.EntitySlime
import net.minecraft.entity.monster.EntitySpider
import net.minecraft.entity.monster.EntityWitherSkeleton
import net.minecraft.entity.monster.EntityZombie
import net.minecraft.entity.passive.EntityBat
import net.minecraft.entity.passive.EntityChicken
import net.minecraft.entity.passive.EntityCow
import net.minecraft.entity.passive.EntityPig
import net.minecraft.entity.passive.EntityVillager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Enchantments
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.ResourceLocation
import net.minecraftforge.common.BiomeDictionary
import net.minecraftforge.oredict.OreIngredient

private class RegistrantScope(val fairySupplier: () -> RankedFairyTypeBundle, val relevance: Double, val weight: Double)
private typealias Registrant = RegistrantScope.() -> Unit

private fun fairy(fairySelector: FairyTypes.() -> RankedFairyTypeBundle): () -> RankedFairyTypeBundle = { fairySelector(FairyTypes.instance) }
private fun (() -> RankedFairyTypeBundle).register(relevance: Double = 1.0, weight: Double = 1.0, actionGetter: () -> Registrant) = actionGetter()(RegistrantScope(this, relevance, weight))
private fun <T> FairyRelationRegistry<T>.register(fairySupplier: () -> RankedFairyTypeBundle, keySupplier: () -> T, relevance: Double, weight: Double) {
    entries += FairyRelationEntry(fairySupplier, keySupplier, relevance, weight)
}

private inline fun <reified E : Entity> entity(): Registrant = {
    FairyRelationRegistries.entity.register(fairySupplier, { { it is E } }, relevance, weight)
}

private inline fun <reified E : Entity> entity(crossinline predicate: E.() -> Boolean): Registrant = {
    FairyRelationRegistries.entity.register(fairySupplier, { { it is E && predicate(it) } }, relevance, weight)
}

private fun biomeType(vararg biomeTypeGetters: () -> BiomeDictionary.Type): Registrant = {
    biomeTypeGetters.forEach { FairyRelationRegistries.biomeType.register(fairySupplier, it, relevance, weight) }
}

private fun block(vararg blockGetters: () -> Block): Registrant = {
    blockGetters.forEach { FairyRelationRegistries.block.register(fairySupplier, it, relevance, weight) }
}

private fun blockState(vararg blockStateGetter: () -> IBlockState): Registrant = {
    blockStateGetter.forEach { FairyRelationRegistries.blockState.register(fairySupplier, it, relevance, weight) }
}

private fun item(vararg itemGetter: () -> Item): Registrant = {
    itemGetter.forEach { FairyRelationRegistries.item.register(fairySupplier, it, relevance, weight) }
}

private fun itemStack(predicate: ItemStack.() -> Boolean): Registrant = {
    FairyRelationRegistries.itemStack.register(fairySupplier, { { predicate(it) } }, relevance, weight)
}

private fun ingredient(vararg ingredientGetter: () -> Ingredient): Registrant = {
    ingredientGetter.forEach { FairyRelationRegistries.ingredient.register(fairySupplier, it, relevance, weight) }
}

private fun ore(vararg oreName: String): Registrant = {
    oreName.forEach { FairyRelationRegistries.ingredient.register(fairySupplier, { OreIngredient(it) }, relevance, weight) }
}

private fun material(material: String): Registrant = {
    listOf("ingot" to 1.0, "nugget" to 0.5, "gem" to 1.0, "dust" to 1.0, "dustTiny" to 0.5, "block" to 0.5, "rod" to 0.5, "plate" to 0.5, "ore" to 0.5).forEach {
        FairyRelationRegistries.ingredient.register(fairySupplier, { OreIngredient("${it.first}$material") }, relevance * it.second, weight)
    }
}


object FairyRelation {
    val module = module {

        // バイオーム
        fairy { plains }.register(weight = 0.1) { biomeType({ BiomeDictionary.Type.PLAINS }) }
        fairy { forest }.register(weight = 0.1) { biomeType({ BiomeDictionary.Type.FOREST }) }
        fairy { ocean }.register(weight = 0.1) { biomeType({ BiomeDictionary.Type.OCEAN }) }
        fairy { taiga }.register(weight = 0.1) { biomeType({ BiomeDictionary.Type.CONIFEROUS }) }
        fairy { desert }.register(weight = 0.1) { biomeType({ BiomeDictionary.Type.SANDY }) }
        fairy { mountain }.register(weight = 0.1) { biomeType({ BiomeDictionary.Type.MOUNTAIN }) }


        // エンティティ

        // 常時反応するエンティティ
        fairy { player }.register(weight = 0.1) { entity<EntityPlayer>() }

        // 長生きするエンティティ
        fairy { chicken }.register(weight = 2.0) { entity<EntityChicken>() }
        fairy { cow }.register(weight = 2.0) { entity<EntityCow>() }
        fairy { pig }.register(weight = 2.0) { entity<EntityPig>() }
        fairy { villager }.register(weight = 2.0) { entity<EntityVillager>() }
        fairy { librarian }.register(relevance = 2.0, weight = 2.0) { entity<EntityVillager> { professionForge.registryName == ResourceLocation("minecraft:librarian") } }
        fairy { golem }.register(weight = 2.0) { entity<EntityIronGolem>() }

        // 持続的に湧かせられるエンティティ
        fairy { skeleton }.register(weight = 5.0) { entity<EntitySkeleton>() }
        fairy { zombie }.register(weight = 5.0) { entity<EntityZombie>() }
        fairy { spider }.register(weight = 5.0) { entity<EntitySpider>() }
        fairy { blaze }.register(weight = 5.0) { entity<EntityBlaze>() }
        fairy { enderman }.register(weight = 5.0) { entity<EntityEnderman>() }

        // 滅多に会えないエンティティ
        fairy { creeper }.register(weight = 10.0) { entity<EntityCreeper>() }
        fairy { chargedCreeper }.register(relevance = 2.0, weight = 10.0) { entity<EntityCreeper> { powered } }
        fairy { slime }.register(weight = 10.0) { entity<EntitySlime>() }
        fairy { magmaCube }.register(relevance = 2.0 /* スライムのサブクラスのため */, weight = 10.0) { entity<EntityMagmaCube>() }
        fairy { witherSkeleton }.register(weight = 10.0) { entity<EntityWitherSkeleton>() }
        fairy { shulker }.register(weight = 10.0) { entity<EntityShulker>() }
        fairy { wither }.register(weight = 10.0) { entity<EntityWither>() }
        fairy { bat }.register(weight = 10.0) { entity<EntityBat>() }

        // 滅多に地上に降りてこないエンティティ
        fairy { enderDragon }.register(weight = 20.0) { entity<EntityDragon>() }


        // アイテム・ブロック

        // 液体
        fairy { water }.register { block({ Blocks.WATER }, { Blocks.FLOWING_WATER }) }
        fairy { water }.register(relevance = 0.5) { item({ Items.WATER_BUCKET }) }
        fairy { lava }.register { block({ Blocks.LAVA }, { Blocks.FLOWING_LAVA }) }
        fairy { lava }.register(relevance = 0.5) { item({ Items.LAVA_BUCKET }) }

        // 土壌
        fairy { stone }.register { block({ Blocks.STONE }) }
        fairy { stone }.register(relevance = 0.5) { block({ Blocks.COBBLESTONE }) }
        fairy { dirt }.register { block({ Blocks.DIRT }) }
        fairy { dirt }.register(relevance = 0.5) { block({ Blocks.GRASS }) }
        fairy { sand }.register { block({ Blocks.SAND }) }
        fairy { sand }.register(relevance = 0.5) { block({ Blocks.SANDSTONE }, { Blocks.RED_SANDSTONE }) }
        fairy { gravel }.register { block({ Blocks.GRAVEL }) }
        fairy { bedrock }.register { block({ Blocks.BEDROCK }) }

        // マテリアル
        fairy { iron }.register { block({ Blocks.IRON_BLOCK }) }
        fairy { iron }.register { material("Iron") }
        fairy { gold }.register { block({ Blocks.GOLD_BLOCK }) }
        fairy { gold }.register { material("Gold") }
        fairy { coal }.register { block({ Blocks.COAL_BLOCK }) }
        fairy { coal }.register { material("Coal") }
        fairy { coal }.register { ingredient({ Ingredient.fromStacks(ItemStack(Items.COAL, 1, 0)) }) }
        fairy { redstone }.register { block({ Blocks.REDSTONE_BLOCK }) }
        fairy { redstone }.register { material("Redstone") }
        fairy { glowstone }.register { block({ Blocks.GLOWSTONE }) }
        fairy { glowstone }.register { material("Glowstone") }
        fairy { glowstone }.register { ore("glowstone") }
        fairy { obsidian }.register { block({ Blocks.OBSIDIAN }) }
        fairy { obsidian }.register { ore("obsidian") }
        fairy { ice }.register { block({ Blocks.ICE }) }
        fairy { packedIce }.register { block({ Blocks.PACKED_ICE }) }
        fairy { diamond }.register { block({ Blocks.DIAMOND_BLOCK }) }
        fairy { diamond }.register { material("Diamond") }
        fairy { lapislazuli }.register { block({ Blocks.LAPIS_BLOCK }) }
        fairy { lapislazuli }.register { material("Lapis") }
        fairy { emerald }.register { block({ Blocks.EMERALD_BLOCK }) }
        fairy { emerald }.register { material("Emerald") }
        fairy { magnetite }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.MAGNETITE_BLOCK) }) }
        fairy { magnetite }.register { material("Magnetite") }
        fairy { apatite }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.APATITE_BLOCK) }) }
        fairy { apatite }.register { material("Apatite") }
        fairy { fluorite }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.FLUORITE_BLOCK) }) }
        fairy { fluorite }.register { material("Fluorite") }
        fairy { sulfur }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.SULFUR_BLOCK) }) }
        fairy { sulfur }.register { material("Sulfur") }
        fairy { cinnabar }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.CINNABAR_BLOCK) }) }
        fairy { cinnabar }.register { material("Cinnabar") }
        fairy { moonstone }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.MOONSTONE_BLOCK) }) }
        fairy { moonstone }.register { material("Moonstone") }
        fairy { pyrope }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.PYROPE_BLOCK) }) }
        fairy { pyrope }.register { material("Pyrope") }
        fairy { smithsonite }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.SMITHSONITE_BLOCK) }) }
        fairy { smithsonite }.register { material("Smithsonite") }
        fairy { nephrite }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.NEPHRITE_BLOCK) }) }
        fairy { nephrite }.register { material("Nephrite") }
        fairy { tourmaline }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.TOURMALINE_BLOCK) }) }
        fairy { tourmaline }.register { material("Tourmaline") }
        fairy { topaz }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.TOPAZ_BLOCK) }) }
        fairy { topaz }.register { material("Topaz") }
        fairy { pyrite }.register { material("Pyrite") }
        fairy { glass }.register { block({ Blocks.GLASS }) }
        fairy { glass }.register { material("Glass") }

        // 動物
        fairy { rottenFlesh }.register { item({ Items.ROTTEN_FLESH }) }
        fairy { netherStar }.register { item({ Items.NETHER_STAR }) }
        fairy { spiderEye }.register { item({ Items.SPIDER_EYE }) }
        fairy { rawRabbit }.register { item({ Items.RABBIT }) }
        fairy { fish }.register { item({ Items.FISH }) }
        fairy { cod }.register(relevance = 2.0) { ingredient({ Ingredient.fromStacks(ItemStack(Items.FISH, 1, 0)) }) }
        fairy { salmon }.register(relevance = 2.0) { ingredient({ Ingredient.fromStacks(ItemStack(Items.FISH, 1, 1)) }) }
        fairy { pufferfish }.register(relevance = 2.0) { ingredient({ Ingredient.fromStacks(ItemStack(Items.FISH, 1, 3)) }) }
        fairy { clownfish }.register(relevance = 2.0) { ingredient({ Ingredient.fromStacks(ItemStack(Items.FISH, 1, 2)) }) }

        // 植物
        fairy { chorusFruit }.register { item({ Items.CHORUS_FRUIT }) }
        fairy { wheat }.register { block({ Blocks.WHEAT }) }
        fairy { wheat }.register(relevance = 0.5) { block({ Blocks.HAY_BLOCK }) }
        fairy { wheat }.register { item({ Items.WHEAT }) }
        fairy { seed }.register { item({ Items.WHEAT_SEEDS }) }
        fairy { lilac }.register { blockState({ Blocks.DOUBLE_PLANT.defaultState.withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.SYRINGA) }) }
        fairy { lilac }.register { ingredient({ Ingredient.fromStacks(ItemStack(Blocks.DOUBLE_PLANT, 1, 1)) }) }
        fairy { peony }.register { blockState({ Blocks.DOUBLE_PLANT.defaultState.withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.PAEONIA) }) }
        fairy { peony }.register { ingredient({ Ingredient.fromStacks(ItemStack(Blocks.DOUBLE_PLANT, 1, 5)) }) }
        fairy { apple }.register { item({ Items.APPLE }) }
        fairy { apple }.register(relevance = 0.5) { item({ Items.GOLDEN_APPLE }) }
        fairy { melon }.register { block({ Blocks.MELON_BLOCK }) }
        fairy { melon }.register(relevance = 0.5) { block({ Blocks.MELON_STEM }) }
        fairy { melon }.register { item({ Items.MELON }) }
        fairy { carrot }.register { block({ Blocks.CARROTS }) }
        fairy { carrot }.register { item({ Items.CARROT }) }
        fairy { carrot }.register(relevance = 0.5) { item({ Items.CARROT_ON_A_STICK }, { Items.GOLDEN_CARROT }) }
        fairy { poisonousPotato }.register { item({ Items.POISONOUS_POTATO }) }
        fairy { beetroot }.register { block({ Blocks.BEETROOTS }) }
        fairy { beetroot }.register { item({ Items.BEETROOT }) }
        fairy { cactus }.register { block({ Blocks.CACTUS }) }
        fairy { spruce }.register {
            blockState(
                { Blocks.LOG.defaultState.withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE) },
                { Blocks.PLANKS.defaultState.withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE) },
                { Blocks.LEAVES.defaultState.withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE) }
            )
        }
        fairy { spruce }.register {
            ingredient(
                { Ingredient.fromStacks(ItemStack(Blocks.LOG, 1, 1)) },
                { Ingredient.fromStacks(ItemStack(Blocks.PLANKS, 1, 1)) },
                { Ingredient.fromStacks(ItemStack(Blocks.SAPLING, 1, 1)) }
            )
        }
        fairy { mirageFlower }.register { block({ MirageFlower.blockMirageFlower() }) }
        fairy { sugarCane }.register { block({ Blocks.REEDS }) }
        fairy { sugarCane }.register { item({ Items.REEDS }) }
        fairy { potato }.register { block({ Blocks.POTATOES }) }
        fairy { potato }.register { item({ Items.POTATO }) }

        // 料理
        fairy { bread }.register { item({ Items.BREAD }) }
        fairy { cookie }.register { item({ Items.COOKIE }) }
        fairy { cake }.register { block({ Blocks.CAKE }) }
        fairy { cake }.register { item({ Items.CAKE }) }
        fairy { bakedPotato }.register { item({ Items.BAKED_POTATO }) }
        fairy { cookedChicken }.register { item({ Items.COOKED_CHICKEN }) }
        fairy { cookedCod }.register { ingredient({ Items.COOKED_FISH.createItemStack(metadata = 0).ingredient }) }
        fairy { cookedSalmon }.register { ingredient({ Ingredient.fromStacks(ItemStack(Items.COOKED_FISH, 1, 1)) }) }
        fairy { steak }.register { item({ Items.COOKED_BEEF }) }
        fairy { pumpkinPie }.register { item({ Items.PUMPKIN_PIE }) }
        fairy { beetrootSoup }.register { item({ Items.BEETROOT_SOUP }) }
        fairy { mushroomStew }.register { item({ Items.MUSHROOM_STEW }) }
        fairy { goldenApple }.register { ingredient({ Ingredient.fromStacks(ItemStack(Items.GOLDEN_APPLE, 1, 0)) }) }
        fairy { enchantedGoldenApple }.register { ingredient({ Ingredient.fromStacks(ItemStack(Items.GOLDEN_APPLE, 1, 1)) }) }

        // 素材
        fairy { sugar }.register { item({ Items.SUGAR }) }
        fairy { coalDust }.register(relevance = 2.0) { ore("dustCoal") }
        fairy { diamondDust }.register(relevance = 2.0) { ore("dustDiamond") }
        fairy { book }.register { item({ Items.BOOK }) }
        fairy { book }.register(relevance = 0.5) { item({ Items.WRITABLE_BOOK }, { Items.WRITTEN_BOOK }, { Items.ENCHANTED_BOOK }) }

        // 道具
        fairy { potion }.register { item({ Items.POTIONITEM }) }
        fairy { potion }.register(relevance = 0.5) { item({ Items.LINGERING_POTION }, { Items.SPLASH_POTION }) }

        // 設置物
        fairy { torch }.register { block({ Blocks.TORCH }) }
        fairy { door }.register { block({ Blocks.OAK_DOOR }, { Blocks.SPRUCE_DOOR }, { Blocks.BIRCH_DOOR }, { Blocks.JUNGLE_DOOR }, { Blocks.ACACIA_DOOR }, { Blocks.DARK_OAK_DOOR }) }
        fairy { door }.register { item({ Items.OAK_DOOR }, { Items.SPRUCE_DOOR }, { Items.BIRCH_DOOR }, { Items.JUNGLE_DOOR }, { Items.ACACIA_DOOR }, { Items.DARK_OAK_DOOR }) }
        fairy { ironDoor }.register { block({ Blocks.IRON_DOOR }) }
        fairy { ironDoor }.register { item({ Items.IRON_DOOR }) }
        fairy { ironBars }.register { block({ Blocks.IRON_BARS }) }
        fairy { chest }.register { block({ Blocks.CHEST }) }
        fairy { hopper }.register { block({ Blocks.HOPPER }) }
        fairy { craftingTable }.register { block({ Blocks.CRAFTING_TABLE }) }
        fairy { furnace }.register { block({ Blocks.FURNACE }) }
        fairy { anvil }.register { block({ Blocks.ANVIL }) }
        fairy { brewingStand }.register { block({ Blocks.BREWING_STAND }) }
        fairy { brewingStand }.register { item({ Items.BREWING_STAND }) }
        fairy { redstoneRepeater }.register { block({ Blocks.UNPOWERED_REPEATER }, { Blocks.POWERED_REPEATER }) }
        fairy { redstoneRepeater }.register { item({ Items.REPEATER }) }
        fairy { redstoneComparator }.register { block({ Blocks.UNPOWERED_COMPARATOR }, { Blocks.POWERED_COMPARATOR }) }
        fairy { redstoneComparator }.register { item({ Items.COMPARATOR }) }
        fairy { dispenser }.register { block({ Blocks.DISPENSER }) }
        fairy { activatorRail }.register { block({ Blocks.ACTIVATOR_RAIL }) }
        fairy { magentaGlazedTerracotta }.register { block({ Blocks.MAGENTA_GLAZED_TERRACOTTA }) }
        fairy { note }.register { block({ Blocks.NOTEBLOCK }) }
        fairy { jukebox }.register { block({ Blocks.JUKEBOX }) }
        fairy { netherPortal }.register { block({ Blocks.PORTAL }) }
        fairy { button }.register { block({ Blocks.WOODEN_BUTTON }) }

        // ツール
        fairy { axe }.register { item({ Items.WOODEN_AXE }, { Items.STONE_AXE }, { Items.IRON_AXE }, { Items.GOLDEN_AXE }, { Items.DIAMOND_AXE }) }
        fairy { sword }.register { item({ Items.WOODEN_SWORD }, { Items.STONE_SWORD }, { Items.IRON_SWORD }, { Items.GOLDEN_SWORD }, { Items.DIAMOND_SWORD }) }
        fairy { hoe }.register { item({ Items.WOODEN_HOE }, { Items.STONE_HOE }, { Items.IRON_HOE }, { Items.GOLDEN_HOE }, { Items.DIAMOND_HOE }) }
        fairy { shield }.register { item({ Items.SHIELD }) }

        // エンチャント
        fairy { fortune }.register { itemStack { EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, this) > 0 } }
        fairy { curseOfVanishing }.register { itemStack { EnchantmentHelper.getEnchantmentLevel(Enchantments.VANISHING_CURSE, this) > 0 } }

        // レコード
        fairy { eleven }.register { item({ Items.RECORD_11 }) }

        // その他
        fairy { fire }.register { block({ Blocks.FIRE }) }
        fairy { enchant }.register { itemStack { isItemEnchanted } }
        fairy { enchant }.register(relevance = 0.5) { block({ Blocks.ENCHANTING_TABLE }) }
        fairy { enchant }.register(relevance = 0.5) { item({ Items.ENCHANTED_BOOK }) }
        fairy { gravity }.register(relevance = 0.1, weight = 0.3) { item({ Items.APPLE }) }

    }
}
