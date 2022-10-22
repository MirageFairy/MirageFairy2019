package miragefairy2019.mod.fairyrelation

import miragefairy2019.lib.modinitializer.module
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.ingredient
import miragefairy2019.mod.fairy.FairyCard
import miragefairy2019.mod.magicplant.blockMandrake
import miragefairy2019.mod.magicplant.blockMirageFlower
import miragefairy2019.mod.magicplant.blockVelopeda
import miragefairy2019.mod.material.BuildingMaterialCard
import miragefairy2019.mod.material.blockMaterials1
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

private class RegistrantScope(val fairyCard: FairyCard, val relevance: Double, val weight: Double)
private typealias Registrant = RegistrantScope.() -> Unit

private fun FairyCard.register(relevance: Double = 1.0, weight: Double = 1.0, actionGetter: () -> Registrant) = actionGetter()(RegistrantScope(this, relevance, weight))
private fun <T> FairyRelationRegistry<T>.register(fairyCard: FairyCard, keySupplier: () -> T, relevance: Double, weight: Double) {
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
    FairyCard.PLAINS.register(weight = 0.1) { biomeType({ BiomeDictionary.Type.PLAINS }) }
    FairyCard.FOREST.register(weight = 0.1) { biomeType({ BiomeDictionary.Type.FOREST }) }
    FairyCard.OCEAN.register(weight = 0.1) { biomeType({ BiomeDictionary.Type.OCEAN }) }
    FairyCard.TAIGA.register(weight = 0.1) { biomeType({ BiomeDictionary.Type.CONIFEROUS }) }
    FairyCard.DESERT.register(weight = 0.1) { biomeType({ BiomeDictionary.Type.SANDY }) }
    FairyCard.MOUNTAIN.register(weight = 0.1) { biomeType({ BiomeDictionary.Type.MOUNTAIN }) }


    // エンティティ

    // 常時反応するエンティティ
    FairyCard.PLAYER.register(weight = 0.1) { entity<EntityPlayer>() }

    // 長生きするエンティティ
    FairyCard.CHICKEN.register(weight = 2.0) { entity<EntityChicken>() }
    FairyCard.COW.register(weight = 2.0) { entity<EntityCow>() }
    FairyCard.PIG.register(weight = 2.0) { entity<EntityPig>() }
    FairyCard.VILLAGER.register(weight = 2.0) { entity<EntityVillager>() }
    FairyCard.LIBRARIAN.register(relevance = 2.0, weight = 2.0) { entity<EntityVillager> { professionForge.registryName == ResourceLocation("minecraft:librarian") } }
    FairyCard.GOLEM.register(weight = 2.0) { entity<EntityIronGolem>() }

    // 持続的に湧かせられるエンティティ
    FairyCard.SKELETON.register(weight = 5.0) { entity<EntitySkeleton>() }
    FairyCard.ZOMBIE.register(weight = 5.0) { entity<EntityZombie>() }
    FairyCard.SPIDER.register(weight = 5.0) { entity<EntitySpider>() }
    FairyCard.BLAZE.register(weight = 5.0) { entity<EntityBlaze>() }
    FairyCard.ENDERMAN.register(weight = 5.0) { entity<EntityEnderman>() }

    // 滅多に会えないエンティティ
    FairyCard.CREEPER.register(weight = 10.0) { entity<EntityCreeper>() }
    FairyCard.CHARGED_CREEPER.register(relevance = 2.0, weight = 10.0) { entity<EntityCreeper> { powered } }
    FairyCard.SLIME.register(weight = 10.0) { entity<EntitySlime>() }
    FairyCard.MAGMA_CUBE.register(relevance = 2.0 /* スライムのサブクラスのため */, weight = 10.0) { entity<EntityMagmaCube>() }
    FairyCard.WITHER_SKELETON.register(weight = 10.0) { entity<EntityWitherSkeleton>() }
    FairyCard.SHULKER.register(weight = 10.0) { entity<EntityShulker>() }
    FairyCard.WITHER.register(weight = 10.0) { entity<EntityWither>() }
    FairyCard.BAT.register(weight = 10.0) { entity<EntityBat>() }

    // 滅多に地上に降りてこないエンティティ
    FairyCard.ENDER_DRAGON.register(weight = 20.0) { entity<EntityDragon>() }


    // アイテム・ブロック

    // 液体
    FairyCard.WATER.register { block({ Blocks.WATER }, { Blocks.FLOWING_WATER }) }
    FairyCard.WATER.register(relevance = 0.5) { item({ Items.WATER_BUCKET }) }
    FairyCard.LAVA.register { block({ Blocks.LAVA }, { Blocks.FLOWING_LAVA }) }
    FairyCard.LAVA.register(relevance = 0.5) { item({ Items.LAVA_BUCKET }) }

    // 土壌
    FairyCard.STONE.register { block({ Blocks.STONE }) }
    FairyCard.STONE.register(relevance = 0.5) { block({ Blocks.COBBLESTONE }) }
    FairyCard.DIRT.register { block({ Blocks.DIRT }) }
    FairyCard.DIRT.register(relevance = 0.5) { block({ Blocks.GRASS }) }
    FairyCard.SAND.register { block({ Blocks.SAND }) }
    FairyCard.SAND.register(relevance = 0.5) { block({ Blocks.SANDSTONE }, { Blocks.RED_SANDSTONE }) }
    FairyCard.GRAVEL.register { block({ Blocks.GRAVEL }) }
    FairyCard.BEDROCK.register { block({ Blocks.BEDROCK }) }

    // マテリアル
    FairyCard.IRON.register { block({ Blocks.IRON_BLOCK }) }
    FairyCard.IRON.register { material("Iron") }
    FairyCard.GOLD.register { block({ Blocks.GOLD_BLOCK }) }
    FairyCard.GOLD.register { material("Gold") }
    FairyCard.COAL.register { block({ Blocks.COAL_BLOCK }) }
    FairyCard.COAL.register { material("Coal") }
    FairyCard.COAL.register { ingredient({ Ingredient.fromStacks(ItemStack(Items.COAL, 1, 0)) }) }
    FairyCard.REDSTONE.register { block({ Blocks.REDSTONE_BLOCK }) }
    FairyCard.REDSTONE.register { material("Redstone") }
    FairyCard.GLOWSTONE.register { block({ Blocks.GLOWSTONE }) }
    FairyCard.GLOWSTONE.register { material("Glowstone") }
    FairyCard.GLOWSTONE.register { ore("glowstone") }
    FairyCard.OBSIDIAN.register { block({ Blocks.OBSIDIAN }) }
    FairyCard.OBSIDIAN.register { ore("obsidian") }
    FairyCard.ICE.register { block({ Blocks.ICE }) }
    FairyCard.PACKED_ICE.register { block({ Blocks.PACKED_ICE }) }
    FairyCard.DIAMOND.register { block({ Blocks.DIAMOND_BLOCK }) }
    FairyCard.DIAMOND.register { material("Diamond") }
    FairyCard.LAPISLAZULI.register { block({ Blocks.LAPIS_BLOCK }) }
    FairyCard.LAPISLAZULI.register { material("Lapis") }
    FairyCard.EMERALD.register { block({ Blocks.EMERALD_BLOCK }) }
    FairyCard.EMERALD.register { material("Emerald") }
    FairyCard.MAGNETITE.register { blockState({ blockMaterials1().getState(BuildingMaterialCard.MAGNETITE_BLOCK) }) }
    FairyCard.MAGNETITE.register { material("Magnetite") }
    FairyCard.APATITE.register { blockState({ blockMaterials1().getState(BuildingMaterialCard.APATITE_BLOCK) }) }
    FairyCard.APATITE.register { material("Apatite") }
    FairyCard.FLUORITE.register { blockState({ blockMaterials1().getState(BuildingMaterialCard.FLUORITE_BLOCK) }) }
    FairyCard.FLUORITE.register { material("Fluorite") }
    FairyCard.SULFUR.register { blockState({ blockMaterials1().getState(BuildingMaterialCard.SULFUR_BLOCK) }) }
    FairyCard.SULFUR.register { material("Sulfur") }
    FairyCard.CINNABAR.register { blockState({ blockMaterials1().getState(BuildingMaterialCard.CINNABAR_BLOCK) }) }
    FairyCard.CINNABAR.register { material("Cinnabar") }
    FairyCard.MOONSTONE.register { blockState({ blockMaterials1().getState(BuildingMaterialCard.MOONSTONE_BLOCK) }) }
    FairyCard.MOONSTONE.register { material("Moonstone") }
    FairyCard.PYROPE.register { blockState({ blockMaterials1().getState(BuildingMaterialCard.PYROPE_BLOCK) }) }
    FairyCard.PYROPE.register { material("Pyrope") }
    FairyCard.SMITHSONITE.register { blockState({ blockMaterials1().getState(BuildingMaterialCard.SMITHSONITE_BLOCK) }) }
    FairyCard.SMITHSONITE.register { material("Smithsonite") }
    FairyCard.NEPHRITE.register { blockState({ blockMaterials1().getState(BuildingMaterialCard.NEPHRITE_BLOCK) }) }
    FairyCard.NEPHRITE.register { material("Nephrite") }
    FairyCard.TOURMALINE.register { blockState({ blockMaterials1().getState(BuildingMaterialCard.TOURMALINE_BLOCK) }) }
    FairyCard.TOURMALINE.register { material("Tourmaline") }
    FairyCard.TOPAZ.register { blockState({ blockMaterials1().getState(BuildingMaterialCard.TOPAZ_BLOCK) }) }
    FairyCard.TOPAZ.register { material("Topaz") }
    FairyCard.PYRITE.register { material("Pyrite") }
    FairyCard.GLASS.register { block({ Blocks.GLASS }) }
    FairyCard.GLASS.register { material("Glass") }

    // 動物
    FairyCard.ROTTEN_FLESH.register { item({ Items.ROTTEN_FLESH }) }
    FairyCard.NETHER_STAR.register { item({ Items.NETHER_STAR }) }
    FairyCard.SPIDER_EYE.register { item({ Items.SPIDER_EYE }) }
    FairyCard.RAW_RABBIT.register { item({ Items.RABBIT }) }
    FairyCard.FISH.register { item({ Items.FISH }) }
    FairyCard.COD.register(relevance = 2.0) { ingredient({ Ingredient.fromStacks(ItemStack(Items.FISH, 1, 0)) }) }
    FairyCard.SALMON.register(relevance = 2.0) { ingredient({ Ingredient.fromStacks(ItemStack(Items.FISH, 1, 1)) }) }
    FairyCard.PUFFERFISH.register(relevance = 2.0) { ingredient({ Ingredient.fromStacks(ItemStack(Items.FISH, 1, 3)) }) }
    FairyCard.CLOWNFISH.register(relevance = 2.0) { ingredient({ Ingredient.fromStacks(ItemStack(Items.FISH, 1, 2)) }) }
    FairyCard.RAW_PORKCHOP.register { item({ Items.PORKCHOP }) }
    FairyCard.RAW_BEEF.register { item({ Items.BEEF }) }
    FairyCard.RAW_CHICKEN.register { item({ Items.CHICKEN }) }
    FairyCard.RAW_MUTTON.register { item({ Items.MUTTON }) }

    // 植物
    FairyCard.CHORUS_FRUIT.register { item({ Items.CHORUS_FRUIT }) }
    FairyCard.WHEAT.register { block({ Blocks.WHEAT }) }
    FairyCard.WHEAT.register(relevance = 0.5) { block({ Blocks.HAY_BLOCK }) }
    FairyCard.WHEAT.register { item({ Items.WHEAT }) }
    FairyCard.SEED.register { item({ Items.WHEAT_SEEDS }) }
    FairyCard.LILAC.register { blockState({ Blocks.DOUBLE_PLANT.defaultState.withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.SYRINGA) }) }
    FairyCard.LILAC.register { ingredient({ Ingredient.fromStacks(ItemStack(Blocks.DOUBLE_PLANT, 1, 1)) }) }
    FairyCard.PEONY.register { blockState({ Blocks.DOUBLE_PLANT.defaultState.withProperty(BlockDoublePlant.VARIANT, BlockDoublePlant.EnumPlantType.PAEONIA) }) }
    FairyCard.PEONY.register { ingredient({ Ingredient.fromStacks(ItemStack(Blocks.DOUBLE_PLANT, 1, 5)) }) }
    FairyCard.APPLE.register { item({ Items.APPLE }) }
    FairyCard.APPLE.register(relevance = 0.5) { item({ Items.GOLDEN_APPLE }) }
    FairyCard.MELON.register { block({ Blocks.MELON_BLOCK }) }
    FairyCard.MELON.register(relevance = 0.5) { block({ Blocks.MELON_STEM }) }
    FairyCard.MELON.register { item({ Items.MELON }) }
    FairyCard.CARROT.register { block({ Blocks.CARROTS }) }
    FairyCard.CARROT.register { item({ Items.CARROT }) }
    FairyCard.CARROT.register(relevance = 0.5) { item({ Items.CARROT_ON_A_STICK }, { Items.GOLDEN_CARROT }) }
    FairyCard.POISONOUS_POTATO.register { item({ Items.POISONOUS_POTATO }) }
    FairyCard.BEETROOT.register { block({ Blocks.BEETROOTS }) }
    FairyCard.BEETROOT.register { item({ Items.BEETROOT }) }
    FairyCard.CACTUS.register { block({ Blocks.CACTUS }) }
    FairyCard.SPRUCE.register {
        blockState(
            { Blocks.LOG.defaultState.withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE) },
            { Blocks.PLANKS.defaultState.withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE) },
            { Blocks.LEAVES.defaultState.withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE) }
        )
    }
    FairyCard.SPRUCE.register {
        ingredient(
            { Ingredient.fromStacks(ItemStack(Blocks.LOG, 1, 1)) },
            { Ingredient.fromStacks(ItemStack(Blocks.PLANKS, 1, 1)) },
            { Ingredient.fromStacks(ItemStack(Blocks.SAPLING, 1, 1)) }
        )
    }
    FairyCard.MIRAGE_FLOWER.register { block({ blockMirageFlower() }) }
    FairyCard.MANDRAKE.register(relevance = 0.5, weight = 0.01) { block({ blockMandrake() }) } // TODO クラフト不可属性
    FairyCard.MANDRAKE.register(relevance = 0.5, weight = 0.01) { ore("mirageFairyMandrake") } // TODO クラフト不可属性
    FairyCard.VELOPEDA.register(relevance = 0.5, weight = 0.01) { block({ blockVelopeda() }) } // TODO クラフト不可属性
    FairyCard.VELOPEDA.register(relevance = 0.5, weight = 0.01) { ore("leafMirageFairyVelopeda") } // TODO クラフト不可属性
    FairyCard.SUGAR_CANE.register { block({ Blocks.REEDS }) }
    FairyCard.SUGAR_CANE.register { item({ Items.REEDS }) }
    FairyCard.POTATO.register { block({ Blocks.POTATOES }) }
    FairyCard.POTATO.register { item({ Items.POTATO }) }
    FairyCard.NETHER_WART.register { block({ Blocks.NETHER_WART }) }
    FairyCard.NETHER_WART.register { item({ Items.NETHER_WART }) }
    FairyCard.PUMPKIN.register { block({ Blocks.PUMPKIN }) }
    FairyCard.PUMPKIN.register(relevance = 0.5) { block({ Blocks.PUMPKIN_STEM }) }

    // 料理
    FairyCard.BREAD.register { item({ Items.BREAD }) }
    FairyCard.COOKIE.register { item({ Items.COOKIE }) }
    FairyCard.CAKE.register { block({ Blocks.CAKE }) }
    FairyCard.CAKE.register { item({ Items.CAKE }) }
    FairyCard.BAKED_POTATO.register { item({ Items.BAKED_POTATO }) }
    FairyCard.COOKED_CHICKEN.register { item({ Items.COOKED_CHICKEN }) }
    FairyCard.COOKED_PORKCHOP.register { item({ Items.COOKED_PORKCHOP }) }
    FairyCard.COOKED_MUTTON.register { item({ Items.COOKED_MUTTON }) }
    FairyCard.COOKED_RABBIT.register { item({ Items.COOKED_RABBIT }) }
    FairyCard.RABBIT_STEW.register { item({ Items.RABBIT_STEW }) }
    FairyCard.COOKED_COD.register { ingredient({ Items.COOKED_FISH.createItemStack(metadata = 0).ingredient }) }
    FairyCard.COOKED_SALMON.register { ingredient({ Ingredient.fromStacks(ItemStack(Items.COOKED_FISH, 1, 1)) }) }
    FairyCard.STEAK.register { item({ Items.COOKED_BEEF }) }
    FairyCard.PUMPKIN_PIE.register { item({ Items.PUMPKIN_PIE }) }
    FairyCard.BEETROOT_SOUP.register { item({ Items.BEETROOT_SOUP }) }
    FairyCard.MUSHROOM_STEW.register { item({ Items.MUSHROOM_STEW }) }
    FairyCard.GOLDEN_APPLE.register { ingredient({ Ingredient.fromStacks(ItemStack(Items.GOLDEN_APPLE, 1, 0)) }) }
    FairyCard.ENCHANTED_GOLDEN_APPLE.register { ingredient({ Ingredient.fromStacks(ItemStack(Items.GOLDEN_APPLE, 1, 1)) }) }
    FairyCard.GOLDEN_CARROT.register { item({ Items.GOLDEN_CARROT }) }

    // 素材
    FairyCard.SUGAR.register { item({ Items.SUGAR }) }
    FairyCard.COAL_DUST.register(relevance = 2.0) { ore("dustCoal") }
    FairyCard.DIAMOND_DUST.register(relevance = 2.0) { ore("dustDiamond") }
    FairyCard.BOOK.register { item({ Items.BOOK }) }
    FairyCard.BOOK.register(relevance = 0.5) { item({ Items.WRITABLE_BOOK }, { Items.WRITTEN_BOOK }, { Items.ENCHANTED_BOOK }) }

    // 道具
    FairyCard.POTION.register { item({ Items.POTIONITEM }) }
    FairyCard.POTION.register(relevance = 0.5) { item({ Items.LINGERING_POTION }, { Items.SPLASH_POTION }) }

    // 設置物
    FairyCard.TORCH.register { block({ Blocks.TORCH }) }
    FairyCard.DOOR.register { block({ Blocks.OAK_DOOR }, { Blocks.SPRUCE_DOOR }, { Blocks.BIRCH_DOOR }, { Blocks.JUNGLE_DOOR }, { Blocks.ACACIA_DOOR }, { Blocks.DARK_OAK_DOOR }) }
    FairyCard.DOOR.register { item({ Items.OAK_DOOR }, { Items.SPRUCE_DOOR }, { Items.BIRCH_DOOR }, { Items.JUNGLE_DOOR }, { Items.ACACIA_DOOR }, { Items.DARK_OAK_DOOR }) }
    FairyCard.IRON_DOOR.register { block({ Blocks.IRON_DOOR }) }
    FairyCard.IRON_DOOR.register { item({ Items.IRON_DOOR }) }
    FairyCard.IRON_BARS.register { block({ Blocks.IRON_BARS }) }
    FairyCard.CHEST.register { block({ Blocks.CHEST }) }
    FairyCard.HOPPER.register { block({ Blocks.HOPPER }) }
    FairyCard.CRAFTING_TABLE.register { block({ Blocks.CRAFTING_TABLE }) }
    FairyCard.FURNACE.register { block({ Blocks.FURNACE }) }
    FairyCard.ANVIL.register { block({ Blocks.ANVIL }) }
    FairyCard.BREWING_STAND.register { block({ Blocks.BREWING_STAND }) }
    FairyCard.BREWING_STAND.register { item({ Items.BREWING_STAND }) }
    FairyCard.REDSTONE_REPEATER.register { block({ Blocks.UNPOWERED_REPEATER }, { Blocks.POWERED_REPEATER }) }
    FairyCard.REDSTONE_REPEATER.register { item({ Items.REPEATER }) }
    FairyCard.REDSTONE_COMPARATOR.register { block({ Blocks.UNPOWERED_COMPARATOR }, { Blocks.POWERED_COMPARATOR }) }
    FairyCard.REDSTONE_COMPARATOR.register { item({ Items.COMPARATOR }) }
    FairyCard.DISPENSER.register { block({ Blocks.DISPENSER }) }
    FairyCard.ACTIVATOR_RAIL.register { block({ Blocks.ACTIVATOR_RAIL }) }
    FairyCard.MAGENTA_GLAZED_TERRACOTTA.register { block({ Blocks.MAGENTA_GLAZED_TERRACOTTA }) }
    FairyCard.NOTE.register { block({ Blocks.NOTEBLOCK }) }
    FairyCard.JUKEBOX.register { block({ Blocks.JUKEBOX }) }
    FairyCard.TRINITROTOLUENE.register { block({ Blocks.TNT }) }
    FairyCard.NETHER_PORTAL.register { block({ Blocks.PORTAL }) }
    FairyCard.BUTTON.register { block({ Blocks.WOODEN_BUTTON }) }
    FairyCard.JACK_OLANTERN.register { block({ Blocks.LIT_PUMPKIN }) }
    FairyCard.BEACON.register { block({ Blocks.BEACON }) }

    // ツール
    FairyCard.AXE.register { item({ Items.WOODEN_AXE }, { Items.STONE_AXE }, { Items.IRON_AXE }, { Items.GOLDEN_AXE }, { Items.DIAMOND_AXE }) }
    FairyCard.SWORD.register { item({ Items.WOODEN_SWORD }, { Items.STONE_SWORD }, { Items.IRON_SWORD }, { Items.GOLDEN_SWORD }, { Items.DIAMOND_SWORD }) }
    FairyCard.HOE.register { item({ Items.WOODEN_HOE }, { Items.STONE_HOE }, { Items.IRON_HOE }, { Items.GOLDEN_HOE }, { Items.DIAMOND_HOE }) }
    FairyCard.SHIELD.register { item({ Items.SHIELD }) }

    // エンチャント
    FairyCard.FORTUNE.register { itemStack { EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, this) > 0 } }
    FairyCard.CURSE_OF_VANISHING.register { itemStack { EnchantmentHelper.getEnchantmentLevel(Enchantments.VANISHING_CURSE, this) > 0 } }

    // レコード
    FairyCard.ELEVEN.register { item({ Items.RECORD_11 }) }

    // その他
    FairyCard.FIRE.register { block({ Blocks.FIRE }) }
    FairyCard.ENCHANT.register { itemStack { isItemEnchanted } }
    FairyCard.ENCHANT.register(relevance = 0.5) { block({ Blocks.ENCHANTING_TABLE }) }
    FairyCard.ENCHANT.register(relevance = 0.5) { item({ Items.ENCHANTED_BOOK }) }
    FairyCard.GRAVITY.register(relevance = 0.1, weight = 0.3) { item({ Items.APPLE }) }

}
