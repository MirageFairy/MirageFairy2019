package miragefairy2019.mod.modules.ore

import miragefairy2019.mod.modules.ore.ore.EnumVariantOre1
import miragefairy2019.mod.modules.ore.ore.EnumVariantOre1.*
import miragefairy2019.mod.modules.ore.ore.EnumVariantOre2
import miragefairy2019.mod.modules.ore.ore.EnumVariantOre2.TOURMALINE_ORE
import miragefairy2019.modkt.api.oreseeddrop.ApiOreSeedDrop
import miragefairy2019.modkt.api.oreseeddrop.EnumOreSeedShape
import miragefairy2019.modkt.api.oreseeddrop.EnumOreSeedShape.*
import miragefairy2019.modkt.api.oreseeddrop.EnumOreSeedType.NETHERRACK
import miragefairy2019.modkt.api.oreseeddrop.EnumOreSeedType.STONE
import miragefairy2019.modkt.api.oreseeddrop.IOreSeedDropRequirement
import miragefairy2019.modkt.impl.oreseeddrop.OreSeedDropRegistryScope
import miragefairy2019.modkt.impl.oreseeddrop.OreSeedDropRequirements.maxY
import miragefairy2019.modkt.impl.oreseeddrop.OreSeedDropRequirements.minY
import miragefairy2019.modkt.impl.oreseeddrop.Vein
import miragefairy2019.modkt.impl.oreseeddrop.invoke
import miragefairy2019.modkt.impl.oreseeddrop.register
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks.*
import net.minecraft.item.ItemStack
import miragefairy2019.modkt.impl.oreseeddrop.Elements as el

/*
 * 暗視
 *   /effect @p minecraft:night_vision 99999 1
 * 垂直　Z自分
 *   /fill ~-120 10 ~ ~120 80 ~ minecraft:air
 * 水平　Y自分
 *   /fill ~-90 ~ ~-90 ~90 ~ ~90 minecraft:air
 * 水平　Y自分　幅2マス　狭い
 *   /fill ~-60 ~ ~-60 ~60 ~1 ~60 minecraft:air
 * 水平　Y45
 *   /fill ~-90 45 ~-90 ~90 45 ~90 minecraft:air
 * 水平　Y11
 *   /fill ~-90 11 ~-90 ~90 11 ~90 minecraft:air
 */

object LoaderOreSeedDrop {
    @JvmStatic // TODO jvm
    fun loadOreSeedDrop() {
        ApiOreSeedDrop.oreSeedDropRegistry {

            fun ore1(variant: EnumVariantOre1) = Pair({ ModuleOre.blockOre1.getState(variant) }, { ItemStack(ModuleOre.blockOre1, 1, variant.metadata) })
            fun ore2(variant: EnumVariantOre2) = Pair({ ModuleOre.blockOre2.getState(variant) }, { ItemStack(ModuleOre.blockOre2, 1, variant.metadata) })
            fun block(block: Block, meta: Int = 0) = Pair({ block.defaultState }, { ItemStack(block) })

            fun OreSeedDropRegistryScope.TypedOreSeedDropRegistryScope.vein(shape: EnumOreSeedShape, weight: Double, output: Pair<() -> IBlockState, () -> ItemStack>, vararg generationConditions: IOreSeedDropRequirement) {
                registry.register(type, POINT, weight / 2, output, *generationConditions)
                registry.register(type, TINY, weight / 2, output, *generationConditions)
                registry.register(type, LAPIS, weight / 2, output, *generationConditions)
                registry.register(type, DIAMOND, weight / 2, output, *generationConditions)
                registry.register(type, IRON, weight / 2, output, *generationConditions)
                registry.register(type, MEDIUM, weight / 2, output, *generationConditions)
                registry.register(type, shape, weight, output, *generationConditions)
            }

            //

            // まばら天然石
            STONE {
                register(POINT, 0.05, ore1(APATITE_ORE))
                register(POINT, 0.05, ore1(FLUORITE_ORE))
                register(POINT, 0.05, ore1(SULFUR_ORE), maxY(15))
                register(POINT, 0.05, ore1(CINNABAR_ORE), maxY(15))
                register(POINT, 0.05, ore1(MAGNETITE_ORE))
                register(POINT, 0.05, ore1(MOONSTONE_ORE), minY(40), maxY(50))
            }

            // 鉱脈天然石
            STONE {
                vein(LARGE, 1.00, ore1(APATITE_ORE), Vein(97063327, 32, 8, 0.02, el.FLUORINE, el.CALCIUM, el.PHOSPHORUS))
                vein(PYRAMID, 1.00, ore1(FLUORITE_ORE), Vein(63503821, 32, 8, 0.02, el.FLUORINE, el.CALCIUM))
                vein(STAR, 1.00, ore1(SULFUR_ORE), Vein(34153177, 32, 8, 0.02, el.SULFUR), maxY(15))
                vein(POINT, 1.00, ore1(CINNABAR_ORE), Vein(27826567, 32, 8, 0.02, el.SULFUR, el.MERCURY), maxY(15))
                vein(COAL, 1.00, ore1(MAGNETITE_ORE), Vein(16287001, 64, 16, 0.02, el.FERRUM))
                vein(TINY, 1.00, ore1(MOONSTONE_ORE), Vein(78750461, 16, 4, 0.02, el.KALIUM, el.ALUMINIUM), minY(40), maxY(50))
            }

            // 鉱脈宝石
            STONE {
                vein(POINT, 0.50, ore1(PYROPE_ORE), Vein(39250117, 16, 4, 0.01, el.MAGNESIUM, el.ALUMINIUM), maxY(50))
                vein(LARGE, 0.75, ore1(SMITHSONITE_ORE), Vein(32379601, 32, 8, 0.01, el.ZINC, el.CARBON), minY(30))
                vein(MEDIUM, 0.75, ore1(NEPHRITE_ORE), Vein(50393467, 64, 16, 0.01, el.CALCIUM, el.MAGNESIUM, el.FERRUM))
                vein(HORIZONTAL, 0.50, ore1(TOPAZ_ORE), Vein(58068649, 16, 4, 0.01, el.ALUMINIUM, el.FLUORINE))
                vein(HORIZONTAL, 0.50, ore2(TOURMALINE_ORE), Vein(25988519, 16, 4, 0.01, el.NATRIUM, el.LITHIUM, el.ALUMINIUM, el.BORON))
                vein(POINT, 0.50, block(EMERALD_ORE), Vein(54693454, 16, 4, 0.02, el.BERYLLIUM, el.ALUMINIUM))
                vein(LAPIS, 0.50, block(LAPIS_ORE), Vein(60410682, 32, 8, 0.005, el.NATRIUM, el.ALUMINIUM))
                vein(DIAMOND, 0.50, block(DIAMOND_ORE), Vein(20741887, 8, 2, 0.003, el.CARBON))
                vein(COAL, 0.50, block(COAL_ORE), Vein(25197329, 64, 16, 0.003, el.CARBON))
                vein(DIAMOND, 0.50, block(REDSTONE_ORE), Vein(95298700, 64, 16, 0.003, el.FERRUM, el.CUPRUM, el.ALUMINIUM, el.MERCURY))
                vein(IRON, 0.50, block(IRON_ORE), Vein(34443884, 64, 16, 0.003, el.FERRUM))
                vein(IRON, 0.50, block(GOLD_ORE), Vein(93307749, 16, 4, 0.003, el.AURUM))
            }

            // ネザー鉱石
            NETHERRACK {
                register(LARGE, 0.10, ore1(NETHERRACK_APATITE_ORE), minY(90))
                register(PYRAMID, 0.10, ore1(NETHERRACK_FLUORITE_ORE), minY(90))
                register(TINY, 0.30, ore1(NETHERRACK_SULFUR_ORE), minY(20), maxY(40))
                register(IRON, 0.10, ore1(NETHERRACK_CINNABAR_ORE))
                register(COAL, 0.10, ore1(NETHERRACK_MAGNETITE_ORE))
                register(TINY, 0.10, ore1(NETHERRACK_MOONSTONE_ORE), maxY(32))
            }

        }
    }
}
