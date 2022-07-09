package miragefairy2019.mod.fairyrelation

import miragefairy2019.lib.modinitializer.module
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.ingredient
import miragefairy2019.mod.artifacts.MirageFlower
import miragefairy2019.mod.fairy.FairyCard
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

private fun fairy(fairySelector: () -> FairyCard): () -> FairyCard = { fairySelector() }
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
    fairy { FairyCard.PLAINS }.register(weight = 0.1) { biomeType({ BiomeDictionary.Type.PLAINS }) }
    fairy { FairyCard.FOREST }.register(weight = 0.1) { biomeType({ BiomeDictionary.Type.FOREST }) }
    fairy { FairyCard.OCEAN }.register(weight = 0.1) { biomeType({ BiomeDictionary.Type.OCEAN }) }
    fairy { FairyCard.TAIGA }.register(weight = 0.1) { biomeType({ BiomeDictionary.Type.CONIFEROUS }) }
    fairy { FairyCard.DESERT }.register(weight = 0.1) { biomeType({ BiomeDictionary.Type.SANDY }) }
    fairy { FairyCard.MOUNTAIN }.register(weight = 0.1) { biomeType({ BiomeDictionary.Type.MOUNTAIN }) }


    // エンティティ

    // 常時反応するエンティティ
    fairy { FairyCard.PLAYER }.register(weight = 0.1) { entity<EntityPlayer>() }

    // 長生きするエンティティ
    fairy { FairyCard.CHICKEN }.register(weight = 2.0) { entity<EntityChicken>() }
    fairy { FairyCard.COW }.register(weight = 2.0) { entity<EntityCow>() }
    fairy { FairyCard.PIG }.register(weight = 2.0) { entity<EntityPig>() }
    fairy { FairyCard.VILLAGER }.register(weight = 2.0) { entity<EntityVillager>() }
    fairy { FairyCard.LIBRARIAN }.register(relevance = 2.0, weight = 2.0) { entity<EntityVillager> { professionForge.registryName == ResourceLocation("minecraft:librarian") } }
    fairy { FairyCard.GOLEM }.register(weight = 2.0) { entity<EntityIronGolem>() }

    // 持続的に湧かせられるエンティティ
    fairy { FairyCard.SKELETON }.register(weight = 5.0) { entity<EntitySkeleton>() }
    fairy { FairyCard.ZOMBIE }.register(weight = 5.0) { entity<EntityZombie>() }
    fairy { FairyCard.SPIDER }.register(weight = 5.0) { entity<EntitySpider>() }
    fairy { FairyCard.BLAZE }.register(weight = 5.0) { entity<EntityBlaze>() }
    fairy { FairyCard.ENDERMAN }.register(weight = 5.0) { entity<EntityEnderman>() }

    // 滅多に会えないエンティティ
    fairy { FairyCard.CREEPER }.register(weight = 10.0) { entity<EntityCreeper>() }
    fairy { FairyCard.CHARGED_CREEPER }.register(relevance = 2.0, weight = 10.0) { entity<EntityCreeper> { powered } }
    fairy { FairyCard.SLIME }.register(weight = 10.0) { entity<EntitySlime>() }
    fairy { FairyCard.MAGMA_CUBE }.register(relevance = 2.0 /* スライムのサブクラスのため */, weight = 10.0) { entity<EntityMagmaCube>() }
    fairy { FairyCard.WITHER_SKELETON }.register(weight = 10.0) { entity<EntityWitherSkeleton>() }
    fairy { FairyCard.SHULKER }.register(weight = 10.0) { entity<EntityShulker>() }
    fairy { FairyCard.WITHER }.register(weight = 10.0) { entity<EntityWither>() }
    fairy { FairyCard.BAT }.register(weight = 10.0) { entity<EntityBat>() }

    // 滅多に地上に降りてこないエンティティ
    fairy { FairyCard.ENDER_DRAGON }.register(weight = 20.0) { entity<EntityDragon>() }


    // アイテム・ブロック

    // 液体
    fairy { FairyCard.WATER }.register { block({ Blocks.WATER }, { Blocks.FLOWING_WATER }) }
    fairy { FairyCard.WATER }.register(relevance = 0.5) { item({ Items.WATER_BUCKET }) }
    fairy { FairyCard.LAVA }.register { block({ Blocks.LAVA }, { Blocks.FLOWING_LAVA }) }
    fairy { FairyCard.LAVA }.register(relevance = 0.5) { item({ Items.LAVA_BUCKET }) }

    // 土壌
    fairy { FairyCard.STONE }.register { block({ Blocks.STONE }) }
    fairy { FairyCard.STONE }.register(relevance = 0.5) { block({ Blocks.COBBLESTONE }) }
    fairy { FairyCard.DIRT }.register { block({ Blocks.DIRT }) }
    fairy { FairyCard.DIRT }.register(relevance = 0.5) { block({ Blocks.GRASS }) }
    fairy { FairyCard.SAND }.register { block({ Blocks.SAND }) }
    fairy { FairyCard.SAND }.register(relevance = 0.5) { block({ Blocks.SANDSTONE }, { Blocks.RED_SANDSTONE }) }
    fairy { FairyCard.GRAVEL }.register { block({ Blocks.GRAVEL }) }
    fairy { FairyCard.BEDROCK }.register { block({ Blocks.BEDROCK }) }

    // マテリアル
    fairy { FairyCard.IRON }.register { block({ Blocks.IRON_BLOCK }) }
    fairy { FairyCard.IRON }.register { material("Iron") }
    fairy { FairyCard.GOLD }.register { block({ Blocks.GOLD_BLOCK }) }
    fairy { FairyCard.GOLD }.register { material("Gold") }
    fairy { FairyCard.COAL }.register { block({ Blocks.COAL_BLOCK }) }
    fairy { FairyCard.COAL }.register { material("Coal") }
    fairy { FairyCard.COAL }.register { ingredient({ Ingredient.fromStacks(ItemStack(Items.COAL, 1, 0)) }) }
    fairy { FairyCard.REDSTONE }.register { block({ Blocks.REDSTONE_BLOCK }) }
    fairy { FairyCard.REDSTONE }.register { material("Redstone") }
    fairy { FairyCard.GLOWSTONE }.register { block({ Blocks.GLOWSTONE }) }
    fairy { FairyCard.GLOWSTONE }.register { material("Glowstone") }
    fairy { FairyCard.GLOWSTONE }.register { ore("glowstone") }
    fairy { FairyCard.OBSIDIAN }.register { block({ Blocks.OBSIDIAN }) }
    fairy { FairyCard.OBSIDIAN }.register { ore("obsidian") }
    fairy { FairyCard.ICE }.register { block({ Blocks.ICE }) }
    fairy { FairyCard.PACKED_ICE }.register { block({ Blocks.PACKED_ICE }) }
    fairy { FairyCard.DIAMOND }.register { block({ Blocks.DIAMOND_BLOCK }) }
    fairy { FairyCard.DIAMOND }.register { material("Diamond") }
    fairy { FairyCard.LAPISLAZULI }.register { block({ Blocks.LAPIS_BLOCK }) }
    fairy { FairyCard.LAPISLAZULI }.register { material("Lapis") }
    fairy { FairyCard.EMERALD }.register { block({ Blocks.EMERALD_BLOCK }) }
    fairy { FairyCard.EMERALD }.register { material("Emerald") }
    fairy { FairyCard.MAGNETITE }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.MAGNETITE_BLOCK) }) }
    fairy { FairyCard.MAGNETITE }.register { material("Magnetite") }
    fairy { FairyCard.APATITE }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.APATITE_BLOCK) }) }
    fairy { FairyCard.APATITE }.register { material("Apatite") }
    fairy { FairyCard.FLUORITE }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.FLUORITE_BLOCK) }) }
    fairy { FairyCard.FLUORITE }.register { material("Fluorite") }
    fairy { FairyCard.SULFUR }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.SULFUR_BLOCK) }) }
    fairy { FairyCard.SULFUR }.register { material("Sulfur") }
    fairy { FairyCard.CINNABAR }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.CINNABAR_BLOCK) }) }
    fairy { FairyCard.CINNABAR }.register { material("Cinnabar") }
    fairy { FairyCard.MOONSTONE }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.MOONSTONE_BLOCK) }) }
    fairy { FairyCard.MOONSTONE }.register { material("Moonstone") }
    fairy { FairyCard.PYROPE }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.PYROPE_BLOCK) }) }
    fairy { FairyCard.PYROPE }.register { material("Pyrope") }
    fairy { FairyCard.SMITHSONITE }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.SMITHSONITE_BLOCK) }) }
    fairy { FairyCard.SMITHSONITE }.register { material("Smithsonite") }
    fairy { FairyCard.NEPHRITE }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.NEPHRITE_BLOCK) }) }
    fairy { FairyCard.NEPHRITE }.register { material("Nephrite") }
    fairy { FairyCard.TOURMALINE }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.TOURMALINE_BLOCK) }) }
    fairy { FairyCard.TOURMALINE }.register { material("Tourmaline") }
    fairy { FairyCard.TOPAZ }.register { blockState({ CompressedMaterials.blockMaterials1().getState(EnumVariantMaterials1.TOPAZ_BLOCK) }) }
    fairy { FairyCard.TOPAZ }.register { material("Topaz") }
    fairy { FairyCard.PYRITE }.register { material("Pyrite") }
    fairy { FairyCard.GLASS }.register { block({ Blocks.GLASS }) }
    fairy { FairyCard.GLASS }.register { material("Glass") }

    // 動物
    fairy { FairyCard.ROTTEN_FLESH }.register { item({ Items.ROTTEN_FLESH }) }
    fairy { FairyCard.NETHER_STAR }.register { item({ Items.NETHER_STAR }) }
    fairy { FairyCard.SPIDER_EYE }.register { item({ Items.SPIDER_EYE }) }
    fairy { FairyCard.RAW_RABBIT }.register { item({ Items.RABBIT }) }
    fairy { FairyCard.FISH }.register { item({ Items.FISH }) }
    fairy { FairyCard.COD }.register(relevance = 2.0) { ingredient({ Ingredient.fromStacks(ItemStack(Items.FISH, 1, 0)) }) }
    fairy { FairyCard.SALMON }.register(relevance = 2.0) { ingredient({ Ingredient.fromStacks(ItemStack(Items.FISH, 1, 1)) }) }
    fairy { FairyCard.PUFFERFISH }.register(relevance = 2.0) { ingredient({ Ingredient.fromStacks(ItemStack(Items.FISH, 1, 3)) }) }
    fairy { FairyCard.CLOWNFISH }.register(relevance = 2.0) { ingredient({ Ingredient.fromStacks(ItemStack(Items.FISH, 1, 2)) }) }

    // 植物
    fairy { FairyCard.CHORUS_FRUIT }.register { item({ Items.CHORUS_FRUIT }) }
    fairy { FairyCard.WHEAT }.register { block({ Blocks.WHEAT }) }
    fairy { FairyCard.WHEAT }.register(relevance = 0.5) { block({ Blocks.HAY_BLOCK }) }
    fairy { FairyCard.WHEAT }.register { item({ Items.WHEAT }) }
    fairy { FairyCard.SEED }.register { item({ Items.WHEAT_SEEDS }) }
    fairy { FairyCard.LILAC }.register { blockState({ Blocks.DOUBLE_PLANT.defaultState.withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.SYRINGA) }) }
    fairy { FairyCard.LILAC }.register { ingredient({ Ingredient.fromStacks(ItemStack(Blocks.DOUBLE_PLANT, 1, 1)) }) }
    fairy { FairyCard.PEONY }.register { blockState({ Blocks.DOUBLE_PLANT.defaultState.withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.PAEONIA) }) }
    fairy { FairyCard.PEONY }.register { ingredient({ Ingredient.fromStacks(ItemStack(Blocks.DOUBLE_PLANT, 1, 5)) }) }
    fairy { FairyCard.APPLE }.register { item({ Items.APPLE }) }
    fairy { FairyCard.APPLE }.register(relevance = 0.5) { item({ Items.GOLDEN_APPLE }) }
    fairy { FairyCard.MELON }.register { block({ Blocks.MELON_BLOCK }) }
    fairy { FairyCard.MELON }.register(relevance = 0.5) { block({ Blocks.MELON_STEM }) }
    fairy { FairyCard.MELON }.register { item({ Items.MELON }) }
    fairy { FairyCard.CARROT }.register { block({ Blocks.CARROTS }) }
    fairy { FairyCard.CARROT }.register { item({ Items.CARROT }) }
    fairy { FairyCard.CARROT }.register(relevance = 0.5) { item({ Items.CARROT_ON_A_STICK }, { Items.GOLDEN_CARROT }) }
    fairy { FairyCard.POISONOUS_POTATO }.register { item({ Items.POISONOUS_POTATO }) }
    fairy { FairyCard.BEETROOT }.register { block({ Blocks.BEETROOTS }) }
    fairy { FairyCard.BEETROOT }.register { item({ Items.BEETROOT }) }
    fairy { FairyCard.CACTUS }.register { block({ Blocks.CACTUS }) }
    fairy { FairyCard.SPRUCE }.register {
        blockState(
            { Blocks.LOG.defaultState.withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE) },
            { Blocks.PLANKS.defaultState.withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE) },
            { Blocks.LEAVES.defaultState.withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE) }
        )
    }
    fairy { FairyCard.SPRUCE }.register {
        ingredient(
            { Ingredient.fromStacks(ItemStack(Blocks.LOG, 1, 1)) },
            { Ingredient.fromStacks(ItemStack(Blocks.PLANKS, 1, 1)) },
            { Ingredient.fromStacks(ItemStack(Blocks.SAPLING, 1, 1)) }
        )
    }
    fairy { FairyCard.MIRAGE_FLOWER }.register { block({ MirageFlower.blockMirageFlower() }) }
    fairy { FairyCard.SUGAR_CANE }.register { block({ Blocks.REEDS }) }
    fairy { FairyCard.SUGAR_CANE }.register { item({ Items.REEDS }) }
    fairy { FairyCard.POTATO }.register { block({ Blocks.POTATOES }) }
    fairy { FairyCard.POTATO }.register { item({ Items.POTATO }) }

    // 料理
    fairy { FairyCard.BREAD }.register { item({ Items.BREAD }) }
    fairy { FairyCard.COOKIE }.register { item({ Items.COOKIE }) }
    fairy { FairyCard.CAKE }.register { block({ Blocks.CAKE }) }
    fairy { FairyCard.CAKE }.register { item({ Items.CAKE }) }
    fairy { FairyCard.BAKED_POTATO }.register { item({ Items.BAKED_POTATO }) }
    fairy { FairyCard.COOKED_CHICKEN }.register { item({ Items.COOKED_CHICKEN }) }
    fairy { FairyCard.COOKED_COD }.register { ingredient({ Items.COOKED_FISH.createItemStack(metadata = 0).ingredient }) }
    fairy { FairyCard.COOKED_SALMON }.register { ingredient({ Ingredient.fromStacks(ItemStack(Items.COOKED_FISH, 1, 1)) }) }
    fairy { FairyCard.STEAK }.register { item({ Items.COOKED_BEEF }) }
    fairy { FairyCard.PUMPKIN_PIE }.register { item({ Items.PUMPKIN_PIE }) }
    fairy { FairyCard.BEETROOT_SOUP }.register { item({ Items.BEETROOT_SOUP }) }
    fairy { FairyCard.MUSHROOM_STEW }.register { item({ Items.MUSHROOM_STEW }) }
    fairy { FairyCard.GOLDEN_APPLE }.register { ingredient({ Ingredient.fromStacks(ItemStack(Items.GOLDEN_APPLE, 1, 0)) }) }
    fairy { FairyCard.ENCHANTED_GOLDEN_APPLE }.register { ingredient({ Ingredient.fromStacks(ItemStack(Items.GOLDEN_APPLE, 1, 1)) }) }

    // 素材
    fairy { FairyCard.SUGAR }.register { item({ Items.SUGAR }) }
    fairy { FairyCard.COAL_DUST }.register(relevance = 2.0) { ore("dustCoal") }
    fairy { FairyCard.DIAMOND_DUST }.register(relevance = 2.0) { ore("dustDiamond") }
    fairy { FairyCard.BOOK }.register { item({ Items.BOOK }) }
    fairy { FairyCard.BOOK }.register(relevance = 0.5) { item({ Items.WRITABLE_BOOK }, { Items.WRITTEN_BOOK }, { Items.ENCHANTED_BOOK }) }

    // 道具
    fairy { FairyCard.POTION }.register { item({ Items.POTIONITEM }) }
    fairy { FairyCard.POTION }.register(relevance = 0.5) { item({ Items.LINGERING_POTION }, { Items.SPLASH_POTION }) }

    // 設置物
    fairy { FairyCard.TORCH }.register { block({ Blocks.TORCH }) }
    fairy { FairyCard.DOOR }.register { block({ Blocks.OAK_DOOR }, { Blocks.SPRUCE_DOOR }, { Blocks.BIRCH_DOOR }, { Blocks.JUNGLE_DOOR }, { Blocks.ACACIA_DOOR }, { Blocks.DARK_OAK_DOOR }) }
    fairy { FairyCard.DOOR }.register { item({ Items.OAK_DOOR }, { Items.SPRUCE_DOOR }, { Items.BIRCH_DOOR }, { Items.JUNGLE_DOOR }, { Items.ACACIA_DOOR }, { Items.DARK_OAK_DOOR }) }
    fairy { FairyCard.IRON_DOOR }.register { block({ Blocks.IRON_DOOR }) }
    fairy { FairyCard.IRON_DOOR }.register { item({ Items.IRON_DOOR }) }
    fairy { FairyCard.IRON_BARS }.register { block({ Blocks.IRON_BARS }) }
    fairy { FairyCard.CHEST }.register { block({ Blocks.CHEST }) }
    fairy { FairyCard.HOPPER }.register { block({ Blocks.HOPPER }) }
    fairy { FairyCard.CRAFTING_TABLE }.register { block({ Blocks.CRAFTING_TABLE }) }
    fairy { FairyCard.FURNACE }.register { block({ Blocks.FURNACE }) }
    fairy { FairyCard.ANVIL }.register { block({ Blocks.ANVIL }) }
    fairy { FairyCard.BREWING_STAND }.register { block({ Blocks.BREWING_STAND }) }
    fairy { FairyCard.BREWING_STAND }.register { item({ Items.BREWING_STAND }) }
    fairy { FairyCard.REDSTONE_REPEATER }.register { block({ Blocks.UNPOWERED_REPEATER }, { Blocks.POWERED_REPEATER }) }
    fairy { FairyCard.REDSTONE_REPEATER }.register { item({ Items.REPEATER }) }
    fairy { FairyCard.REDSTONE_COMPARATOR }.register { block({ Blocks.UNPOWERED_COMPARATOR }, { Blocks.POWERED_COMPARATOR }) }
    fairy { FairyCard.REDSTONE_COMPARATOR }.register { item({ Items.COMPARATOR }) }
    fairy { FairyCard.DISPENSER }.register { block({ Blocks.DISPENSER }) }
    fairy { FairyCard.ACTIVATOR_RAIL }.register { block({ Blocks.ACTIVATOR_RAIL }) }
    fairy { FairyCard.MAGENTA_GLAZED_TERRACOTTA }.register { block({ Blocks.MAGENTA_GLAZED_TERRACOTTA }) }
    fairy { FairyCard.NOTE }.register { block({ Blocks.NOTEBLOCK }) }
    fairy { FairyCard.JUKEBOX }.register { block({ Blocks.JUKEBOX }) }
    fairy { FairyCard.NETHER_PORTAL }.register { block({ Blocks.PORTAL }) }
    fairy { FairyCard.BUTTON }.register { block({ Blocks.WOODEN_BUTTON }) }

    // ツール
    fairy { FairyCard.AXE }.register { item({ Items.WOODEN_AXE }, { Items.STONE_AXE }, { Items.IRON_AXE }, { Items.GOLDEN_AXE }, { Items.DIAMOND_AXE }) }
    fairy { FairyCard.SWORD }.register { item({ Items.WOODEN_SWORD }, { Items.STONE_SWORD }, { Items.IRON_SWORD }, { Items.GOLDEN_SWORD }, { Items.DIAMOND_SWORD }) }
    fairy { FairyCard.HOE }.register { item({ Items.WOODEN_HOE }, { Items.STONE_HOE }, { Items.IRON_HOE }, { Items.GOLDEN_HOE }, { Items.DIAMOND_HOE }) }
    fairy { FairyCard.SHIELD }.register { item({ Items.SHIELD }) }

    // エンチャント
    fairy { FairyCard.FORTUNE }.register { itemStack { EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, this) > 0 } }
    fairy { FairyCard.CURSE_OF_VANISHING }.register { itemStack { EnchantmentHelper.getEnchantmentLevel(Enchantments.VANISHING_CURSE, this) > 0 } }

    // レコード
    fairy { FairyCard.ELEVEN }.register { item({ Items.RECORD_11 }) }

    // その他
    fairy { FairyCard.FIRE }.register { block({ Blocks.FIRE }) }
    fairy { FairyCard.ENCHANT }.register { itemStack { isItemEnchanted } }
    fairy { FairyCard.ENCHANT }.register(relevance = 0.5) { block({ Blocks.ENCHANTING_TABLE }) }
    fairy { FairyCard.ENCHANT }.register(relevance = 0.5) { item({ Items.ENCHANTED_BOOK }) }
    fairy { FairyCard.GRAVITY }.register(relevance = 0.1, weight = 0.3) { item({ Items.APPLE }) }

}
