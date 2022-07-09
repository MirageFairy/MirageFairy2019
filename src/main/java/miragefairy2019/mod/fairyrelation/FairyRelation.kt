package miragefairy2019.mod.fairyrelation

import miragefairy2019.lib.modinitializer.module
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.ingredient
import miragefairy2019.mod.artifacts.MirageFlower
import miragefairy2019.mod.fairy.FairyCard
import miragefairy2019.mod.fairy.FairyTypes
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

private class RegistrantScope(val fairyCard: () -> FairyCard, val relevance: Double, val weight: Double)
private typealias Registrant = RegistrantScope.() -> Unit

private fun fairy(fairySelector: FairyTypes.() -> FairyCard): () -> FairyCard = { fairySelector(FairyTypes.instance) }
private fun (() -> FairyCard).register(relevance: Double = 1.0, weight: Double = 1.0, actionGetter: () -> Registrant) = actionGetter()(RegistrantScope(this, relevance, weight))
private fun <T> FairyRelationRegistry<T>.register(fairyCard: () -> FairyCard, keySupplier: () -> T, relevance: Double, weight: Double) {
    entries += FairyRelationEntry(fairyCard, keySupplier, relevance, weight)
}


private inline fun <reified E : Entity> entity(): Registrant = {
    FairyRelationRegistries.entity.register(fairyCard, { { it is E } }, relevance, weight)
}

private inline fun <reified E : Entity> entity(crossinline predicate: E.() -> Boolean): Registrant = {
    FairyRelationRegistries.entity.register(fairyCard, { { it is E && predicate(it) } }, relevance, weight)
}

private fun biomeType(vararg biomeTypeGetters: () -> BiomeDictionary.Type): Registrant = {
    biomeTypeGetters.forEach { FairyRelationRegistries.biomeType.register(fairyCard, it, relevance, weight) }
}

private fun block(vararg blockGetters: () -> Block): Registrant = {
    blockGetters.forEach { FairyRelationRegistries.block.register(fairyCard, it, relevance, weight) }
}

private fun blockState(vararg blockStateGetter: () -> IBlockState): Registrant = {
    blockStateGetter.forEach { FairyRelationRegistries.blockState.register(fairyCard, it, relevance, weight) }
}

private fun item(vararg itemGetter: () -> Item): Registrant = {
    itemGetter.forEach { FairyRelationRegistries.item.register(fairyCard, it, relevance, weight) }
}

private fun itemStack(predicate: ItemStack.() -> Boolean): Registrant = {
    FairyRelationRegistries.itemStack.register(fairyCard, { { predicate(it) } }, relevance, weight)
}

private fun ingredient(vararg ingredientGetter: () -> Ingredient): Registrant = {
    ingredientGetter.forEach { FairyRelationRegistries.ingredient.register(fairyCard, it, relevance, weight) }
}

private fun ore(vararg oreName: String): Registrant = {
    oreName.forEach { FairyRelationRegistries.ingredient.register(fairyCard, { OreIngredient(it) }, relevance, weight) }
}

private fun material(material: String): Registrant = {
    listOf("ingot" to 1.0, "nugget" to 0.5, "gem" to 1.0, "dust" to 1.0, "dustTiny" to 0.5, "block" to 0.5, "rod" to 0.5, "plate" to 0.5, "ore" to 0.5).forEach {
        FairyRelationRegistries.ingredient.register(fairyCard, { OreIngredient("${it.first}$material") }, relevance * it.second, weight)
    }
}


val fairyRelationModule = module {

    // バイオーム
    fairy { PLAINS }.register(weight = 0.1) { biomeType({ BiomeDictionary.Type.PLAINS }) }
    fairy { FOREST }.register(weight = 0.1) { biomeType({ BiomeDictionary.Type.FOREST }) }
    fairy { OCEAN }.register(weight = 0.1) { biomeType({ BiomeDictionary.Type.OCEAN }) }
    fairy { TAIGA }.register(weight = 0.1) { biomeType({ BiomeDictionary.Type.CONIFEROUS }) }
    fairy { DESERT }.register(weight = 0.1) { biomeType({ BiomeDictionary.Type.SANDY }) }
    fairy { MOUNTAIN }.register(weight = 0.1) { biomeType({ BiomeDictionary.Type.MOUNTAIN }) }


    // エンティティ

    // 常時反応するエンティティ
    fairy { PLAYER }.register(weight = 0.1) { entity<EntityPlayer>() }

    // 長生きするエンティティ
    fairy { CHICKEN }.register(weight = 2.0) { entity<EntityChicken>() }
    fairy { COW }.register(weight = 2.0) { entity<EntityCow>() }
    fairy { PIG }.register(weight = 2.0) { entity<EntityPig>() }
    fairy { VILLAGER }.register(weight = 2.0) { entity<EntityVillager>() }
    fairy { LIBRARIAN }.register(relevance = 2.0, weight = 2.0) { entity<EntityVillager> { professionForge.registryName == ResourceLocation("minecraft:librarian") } }
    fairy { GOLEM }.register(weight = 2.0) { entity<EntityIronGolem>() }

    // 持続的に湧かせられるエンティティ
    fairy { SKELETON }.register(weight = 5.0) { entity<EntitySkeleton>() }
    fairy { ZOMBIE }.register(weight = 5.0) { entity<EntityZombie>() }
    fairy { SPIDER }.register(weight = 5.0) { entity<EntitySpider>() }
    fairy { BLAZE }.register(weight = 5.0) { entity<EntityBlaze>() }
    fairy { ENDERMAN }.register(weight = 5.0) { entity<EntityEnderman>() }

    // 滅多に会えないエンティティ
    fairy { CREEPER }.register(weight = 10.0) { entity<EntityCreeper>() }
    fairy { CHARGED_CREEPER }.register(relevance = 2.0, weight = 10.0) { entity<EntityCreeper> { powered } }
    fairy { SLIME }.register(weight = 10.0) { entity<EntitySlime>() }
    fairy { MAGMA_CUBE }.register(relevance = 2.0 /* スライムのサブクラスのため */, weight = 10.0) { entity<EntityMagmaCube>() }
    fairy { WITHER_SKELETON }.register(weight = 10.0) { entity<EntityWitherSkeleton>() }
    fairy { SHULKER }.register(weight = 10.0) { entity<EntityShulker>() }
    fairy { WITHER }.register(weight = 10.0) { entity<EntityWither>() }
    fairy { BAT }.register(weight = 10.0) { entity<EntityBat>() }

    // 滅多に地上に降りてこないエンティティ
    fairy { ENDER_DRAGON }.register(weight = 20.0) { entity<EntityDragon>() }


    // アイテム・ブロック

    // 液体
    fairy { WATER }.register { block({ Blocks.WATER }, { Blocks.FLOWING_WATER }) }
    fairy { WATER }.register(relevance = 0.5) { item({ Items.WATER_BUCKET }) }
    fairy { LAVA }.register { block({ Blocks.LAVA }, { Blocks.FLOWING_LAVA }) }
    fairy { LAVA }.register(relevance = 0.5) { item({ Items.LAVA_BUCKET }) }

    // 土壌
    fairy { STONE }.register { block({ Blocks.STONE }) }
    fairy { STONE }.register(relevance = 0.5) { block({ Blocks.COBBLESTONE }) }
    fairy { DIRT }.register { block({ Blocks.DIRT }) }
    fairy { DIRT }.register(relevance = 0.5) { block({ Blocks.GRASS }) }
    fairy { SAND }.register { block({ Blocks.SAND }) }
    fairy { SAND }.register(relevance = 0.5) { block({ Blocks.SANDSTONE }, { Blocks.RED_SANDSTONE }) }
    fairy { GRAVEL }.register { block({ Blocks.GRAVEL }) }
    fairy { BEDROCK }.register { block({ Blocks.BEDROCK }) }

    // マテリアル
    fairy { IRON }.register { block({ Blocks.IRON_BLOCK }) }
    fairy { IRON }.register { material("Iron") }
    fairy { GOLD }.register { block({ Blocks.GOLD_BLOCK }) }
    fairy { GOLD }.register { material("Gold") }
    fairy { COAL }.register { block({ Blocks.COAL_BLOCK }) }
    fairy { COAL }.register { material("Coal") }
    fairy { COAL }.register { ingredient({ Ingredient.fromStacks(ItemStack(Items.COAL, 1, 0)) }) }
    fairy { REDSTONE }.register { block({ Blocks.REDSTONE_BLOCK }) }
    fairy { REDSTONE }.register { material("Redstone") }
    fairy { GLOWSTONE }.register { block({ Blocks.GLOWSTONE }) }
    fairy { GLOWSTONE }.register { material("Glowstone") }
    fairy { GLOWSTONE }.register { ore("glowstone") }
    fairy { OBSIDIAN }.register { block({ Blocks.OBSIDIAN }) }
    fairy { OBSIDIAN }.register { ore("obsidian") }
    fairy { ICE }.register { block({ Blocks.ICE }) }
    fairy { PACKED_ICE }.register { block({ Blocks.PACKED_ICE }) }
    fairy { DIAMOND }.register { block({ Blocks.DIAMOND_BLOCK }) }
    fairy { DIAMOND }.register { material("Diamond") }
    fairy { LAPISLAZULI }.register { block({ Blocks.LAPIS_BLOCK }) }
    fairy { LAPISLAZULI }.register { material("Lapis") }
    fairy { EMERALD }.register { block({ Blocks.EMERALD_BLOCK }) }
    fairy { EMERALD }.register { material("Emerald") }
    fairy { MAGNETITE }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.MAGNETITE_BLOCK) }) }
    fairy { MAGNETITE }.register { material("Magnetite") }
    fairy { APATITE }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.APATITE_BLOCK) }) }
    fairy { APATITE }.register { material("Apatite") }
    fairy { FLUORITE }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.FLUORITE_BLOCK) }) }
    fairy { FLUORITE }.register { material("Fluorite") }
    fairy { SULFUR }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.SULFUR_BLOCK) }) }
    fairy { SULFUR }.register { material("Sulfur") }
    fairy { CINNABAR }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.CINNABAR_BLOCK) }) }
    fairy { CINNABAR }.register { material("Cinnabar") }
    fairy { MOONSTONE }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.MOONSTONE_BLOCK) }) }
    fairy { MOONSTONE }.register { material("Moonstone") }
    fairy { PYROPE }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.PYROPE_BLOCK) }) }
    fairy { PYROPE }.register { material("Pyrope") }
    fairy { SMITHSONITE }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.SMITHSONITE_BLOCK) }) }
    fairy { SMITHSONITE }.register { material("Smithsonite") }
    fairy { NEPHRITE }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.NEPHRITE_BLOCK) }) }
    fairy { NEPHRITE }.register { material("Nephrite") }
    fairy { TOURMALINE }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.TOURMALINE_BLOCK) }) }
    fairy { TOURMALINE }.register { material("Tourmaline") }
    fairy { TOPAZ }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.TOPAZ_BLOCK) }) }
    fairy { TOPAZ }.register { material("Topaz") }
    fairy { PYRITE }.register { material("Pyrite") }
    fairy { GLASS }.register { block({ Blocks.GLASS }) }
    fairy { GLASS }.register { material("Glass") }

    // 動物
    fairy { ROTTEN_FLESH }.register { item({ Items.ROTTEN_FLESH }) }
    fairy { NETHER_STAR }.register { item({ Items.NETHER_STAR }) }
    fairy { SPIDER_EYE }.register { item({ Items.SPIDER_EYE }) }
    fairy { RAW_RABBIT }.register { item({ Items.RABBIT }) }
    fairy { FISH }.register { item({ Items.FISH }) }
    fairy { COD }.register(relevance = 2.0) { ingredient({ Ingredient.fromStacks(ItemStack(Items.FISH, 1, 0)) }) }
    fairy { SALMON }.register(relevance = 2.0) { ingredient({ Ingredient.fromStacks(ItemStack(Items.FISH, 1, 1)) }) }
    fairy { PUFFERFISH }.register(relevance = 2.0) { ingredient({ Ingredient.fromStacks(ItemStack(Items.FISH, 1, 3)) }) }
    fairy { CLOWNFISH }.register(relevance = 2.0) { ingredient({ Ingredient.fromStacks(ItemStack(Items.FISH, 1, 2)) }) }

    // 植物
    fairy { CHORUS_FRUIT }.register { item({ Items.CHORUS_FRUIT }) }
    fairy { WHEAT }.register { block({ Blocks.WHEAT }) }
    fairy { WHEAT }.register(relevance = 0.5) { block({ Blocks.HAY_BLOCK }) }
    fairy { WHEAT }.register { item({ Items.WHEAT }) }
    fairy { SEED }.register { item({ Items.WHEAT_SEEDS }) }
    fairy { LILAC }.register { blockState({ Blocks.DOUBLE_PLANT.defaultState.withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.SYRINGA) }) }
    fairy { LILAC }.register { ingredient({ Ingredient.fromStacks(ItemStack(Blocks.DOUBLE_PLANT, 1, 1)) }) }
    fairy { PEONY }.register { blockState({ Blocks.DOUBLE_PLANT.defaultState.withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.PAEONIA) }) }
    fairy { PEONY }.register { ingredient({ Ingredient.fromStacks(ItemStack(Blocks.DOUBLE_PLANT, 1, 5)) }) }
    fairy { APPLE }.register { item({ Items.APPLE }) }
    fairy { APPLE }.register(relevance = 0.5) { item({ Items.GOLDEN_APPLE }) }
    fairy { MELON }.register { block({ Blocks.MELON_BLOCK }) }
    fairy { MELON }.register(relevance = 0.5) { block({ Blocks.MELON_STEM }) }
    fairy { MELON }.register { item({ Items.MELON }) }
    fairy { CARROT }.register { block({ Blocks.CARROTS }) }
    fairy { CARROT }.register { item({ Items.CARROT }) }
    fairy { CARROT }.register(relevance = 0.5) { item({ Items.CARROT_ON_A_STICK }, { Items.GOLDEN_CARROT }) }
    fairy { POISONOUS_POTATO }.register { item({ Items.POISONOUS_POTATO }) }
    fairy { BEETROOT }.register { block({ Blocks.BEETROOTS }) }
    fairy { BEETROOT }.register { item({ Items.BEETROOT }) }
    fairy { CACTUS }.register { block({ Blocks.CACTUS }) }
    fairy { SPRUCE }.register {
        blockState(
            { Blocks.LOG.defaultState.withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE) },
            { Blocks.PLANKS.defaultState.withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE) },
            { Blocks.LEAVES.defaultState.withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE) }
        )
    }
    fairy { SPRUCE }.register {
        ingredient(
            { Ingredient.fromStacks(ItemStack(Blocks.LOG, 1, 1)) },
            { Ingredient.fromStacks(ItemStack(Blocks.PLANKS, 1, 1)) },
            { Ingredient.fromStacks(ItemStack(Blocks.SAPLING, 1, 1)) }
        )
    }
    fairy { MIRAGE_FLOWER }.register { block({ MirageFlower.blockMirageFlower() }) }
    fairy { SUGAR_CANE }.register { block({ Blocks.REEDS }) }
    fairy { SUGAR_CANE }.register { item({ Items.REEDS }) }
    fairy { POTATO }.register { block({ Blocks.POTATOES }) }
    fairy { POTATO }.register { item({ Items.POTATO }) }

    // 料理
    fairy { BREAD }.register { item({ Items.BREAD }) }
    fairy { COOKIE }.register { item({ Items.COOKIE }) }
    fairy { CAKE }.register { block({ Blocks.CAKE }) }
    fairy { CAKE }.register { item({ Items.CAKE }) }
    fairy { BAKED_POTATO }.register { item({ Items.BAKED_POTATO }) }
    fairy { COOKED_CHICKEN }.register { item({ Items.COOKED_CHICKEN }) }
    fairy { COOKED_COD }.register { ingredient({ Items.COOKED_FISH.createItemStack(metadata = 0).ingredient }) }
    fairy { COOKED_SALMON }.register { ingredient({ Ingredient.fromStacks(ItemStack(Items.COOKED_FISH, 1, 1)) }) }
    fairy { STEAK }.register { item({ Items.COOKED_BEEF }) }
    fairy { PUMPKIN_PIE }.register { item({ Items.PUMPKIN_PIE }) }
    fairy { BEETROOT_SOUP }.register { item({ Items.BEETROOT_SOUP }) }
    fairy { MUSHROOM_STEW }.register { item({ Items.MUSHROOM_STEW }) }
    fairy { GOLDEN_APPLE }.register { ingredient({ Ingredient.fromStacks(ItemStack(Items.GOLDEN_APPLE, 1, 0)) }) }
    fairy { ENCHANTED_GOLDEN_APPLE }.register { ingredient({ Ingredient.fromStacks(ItemStack(Items.GOLDEN_APPLE, 1, 1)) }) }

    // 素材
    fairy { SUGAR }.register { item({ Items.SUGAR }) }
    fairy { COAL_DUST }.register(relevance = 2.0) { ore("dustCoal") }
    fairy { DIAMOND_DUST }.register(relevance = 2.0) { ore("dustDiamond") }
    fairy { BOOK }.register { item({ Items.BOOK }) }
    fairy { BOOK }.register(relevance = 0.5) { item({ Items.WRITABLE_BOOK }, { Items.WRITTEN_BOOK }, { Items.ENCHANTED_BOOK }) }

    // 道具
    fairy { POTION }.register { item({ Items.POTIONITEM }) }
    fairy { POTION }.register(relevance = 0.5) { item({ Items.LINGERING_POTION }, { Items.SPLASH_POTION }) }

    // 設置物
    fairy { TORCH }.register { block({ Blocks.TORCH }) }
    fairy { DOOR }.register { block({ Blocks.OAK_DOOR }, { Blocks.SPRUCE_DOOR }, { Blocks.BIRCH_DOOR }, { Blocks.JUNGLE_DOOR }, { Blocks.ACACIA_DOOR }, { Blocks.DARK_OAK_DOOR }) }
    fairy { DOOR }.register { item({ Items.OAK_DOOR }, { Items.SPRUCE_DOOR }, { Items.BIRCH_DOOR }, { Items.JUNGLE_DOOR }, { Items.ACACIA_DOOR }, { Items.DARK_OAK_DOOR }) }
    fairy { IRON_DOOR }.register { block({ Blocks.IRON_DOOR }) }
    fairy { IRON_DOOR }.register { item({ Items.IRON_DOOR }) }
    fairy { IRON_BARS }.register { block({ Blocks.IRON_BARS }) }
    fairy { CHEST }.register { block({ Blocks.CHEST }) }
    fairy { HOPPER }.register { block({ Blocks.HOPPER }) }
    fairy { CRAFTING_TABLE }.register { block({ Blocks.CRAFTING_TABLE }) }
    fairy { FURNACE }.register { block({ Blocks.FURNACE }) }
    fairy { ANVIL }.register { block({ Blocks.ANVIL }) }
    fairy { BREWING_STAND }.register { block({ Blocks.BREWING_STAND }) }
    fairy { BREWING_STAND }.register { item({ Items.BREWING_STAND }) }
    fairy { REDSTONE_REPEATER }.register { block({ Blocks.UNPOWERED_REPEATER }, { Blocks.POWERED_REPEATER }) }
    fairy { REDSTONE_REPEATER }.register { item({ Items.REPEATER }) }
    fairy { REDSTONE_COMPARATOR }.register { block({ Blocks.UNPOWERED_COMPARATOR }, { Blocks.POWERED_COMPARATOR }) }
    fairy { REDSTONE_COMPARATOR }.register { item({ Items.COMPARATOR }) }
    fairy { DISPENSER }.register { block({ Blocks.DISPENSER }) }
    fairy { ACTIVATOR_RAIL }.register { block({ Blocks.ACTIVATOR_RAIL }) }
    fairy { MAGENTA_GLAZED_TERRACOTTA }.register { block({ Blocks.MAGENTA_GLAZED_TERRACOTTA }) }
    fairy { NOTE }.register { block({ Blocks.NOTEBLOCK }) }
    fairy { JUKEBOX }.register { block({ Blocks.JUKEBOX }) }
    fairy { NETHER_PORTAL }.register { block({ Blocks.PORTAL }) }
    fairy { BUTTON }.register { block({ Blocks.WOODEN_BUTTON }) }

    // ツール
    fairy { AXE }.register { item({ Items.WOODEN_AXE }, { Items.STONE_AXE }, { Items.IRON_AXE }, { Items.GOLDEN_AXE }, { Items.DIAMOND_AXE }) }
    fairy { SWORD }.register { item({ Items.WOODEN_SWORD }, { Items.STONE_SWORD }, { Items.IRON_SWORD }, { Items.GOLDEN_SWORD }, { Items.DIAMOND_SWORD }) }
    fairy { HOE }.register { item({ Items.WOODEN_HOE }, { Items.STONE_HOE }, { Items.IRON_HOE }, { Items.GOLDEN_HOE }, { Items.DIAMOND_HOE }) }
    fairy { SHIELD }.register { item({ Items.SHIELD }) }

    // エンチャント
    fairy { FORTUNE }.register { itemStack { EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, this) > 0 } }
    fairy { CURSE_OF_VANISHING }.register { itemStack { EnchantmentHelper.getEnchantmentLevel(Enchantments.VANISHING_CURSE, this) > 0 } }

    // レコード
    fairy { ELEVEN }.register { item({ Items.RECORD_11 }) }

    // その他
    fairy { FIRE }.register { block({ Blocks.FIRE }) }
    fairy { ENCHANT }.register { itemStack { isItemEnchanted } }
    fairy { ENCHANT }.register(relevance = 0.5) { block({ Blocks.ENCHANTING_TABLE }) }
    fairy { ENCHANT }.register(relevance = 0.5) { item({ Items.ENCHANTED_BOOK }) }
    fairy { GRAVITY }.register(relevance = 0.1, weight = 0.3) { item({ Items.APPLE }) }

}
