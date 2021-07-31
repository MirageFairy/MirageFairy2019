package miragefairy2019.mod.modules.ore

import miragefairy2019.mod.modules.ore.ore.EnumVariantOre1.*
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
import net.minecraft.block.state.IBlockState
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

            fun OreSeedDropRegistryScope.TypedOreSeedDropRegistryScope.vein(shape: EnumOreSeedShape, weight: Double, block: () -> IBlockState, vararg generationConditions: IOreSeedDropRequirement) {
                registry.register(type, POINT, weight / 2, block, *generationConditions)
                registry.register(type, TINY, weight / 2, block, *generationConditions)
                registry.register(type, LAPIS, weight / 2, block, *generationConditions)
                registry.register(type, DIAMOND, weight / 2, block, *generationConditions)
                registry.register(type, IRON, weight / 2, block, *generationConditions)
                registry.register(type, MEDIUM, weight / 2, block, *generationConditions)
                registry.register(type, shape, weight, block, *generationConditions)
            }

            //

            // まばら天然石
            STONE {
                register(POINT, 0.03, { ModuleOre.blockOre1.getState(APATITE_ORE) })
                register(POINT, 0.03, { ModuleOre.blockOre1.getState(FLUORITE_ORE) })
                register(POINT, 0.03, { ModuleOre.blockOre1.getState(SULFUR_ORE) }, maxY(15))
                register(POINT, 0.03, { ModuleOre.blockOre1.getState(CINNABAR_ORE) }, maxY(15))
                register(POINT, 0.03, { ModuleOre.blockOre1.getState(MAGNETITE_ORE) })
                register(POINT, 0.03, { ModuleOre.blockOre1.getState(MOONSTONE_ORE) }, minY(40), maxY(50))
            }

            // 鉱脈天然石
            STONE {
                vein(LARGE, 1.00, { ModuleOre.blockOre1.getState(APATITE_ORE) }, Vein(97063327, 32, 8, 0.02, el.FLUORINE, el.CALCIUM, el.PHOSPHORUS))
                vein(PYRAMID, 1.00, { ModuleOre.blockOre1.getState(FLUORITE_ORE) }, Vein(63503821, 32, 8, 0.02, el.FLUORINE, el.CALCIUM))
                vein(STAR, 1.00, { ModuleOre.blockOre1.getState(SULFUR_ORE) }, Vein(34153177, 32, 8, 0.02, el.SULFUR), maxY(15))
                vein(POINT, 1.00, { ModuleOre.blockOre1.getState(CINNABAR_ORE) }, Vein(27826567, 32, 8, 0.02, el.SULFUR, el.MERCURY), maxY(15))
                vein(COAL, 1.00, { ModuleOre.blockOre1.getState(MAGNETITE_ORE) }, Vein(16287001, 64, 16, 0.02, el.FERRUM))
                vein(TINY, 1.00, { ModuleOre.blockOre1.getState(MOONSTONE_ORE) }, Vein(78750461, 16, 4, 0.02, el.KALIUM, el.ALUMINIUM), minY(40), maxY(50))
            }

            // 鉱脈宝石
            STONE {
                vein(POINT, 0.50, { ModuleOre.blockOre1.getState(PYROPE_ORE) }, Vein(39250117, 16, 4, 0.01, el.MAGNESIUM, el.ALUMINIUM), maxY(50))
                vein(LARGE, 0.75, { ModuleOre.blockOre1.getState(SMITHSONITE_ORE) }, Vein(32379601, 32, 8, 0.01, el.ZINC, el.CARBON), minY(30))
                vein(MEDIUM, 0.75, { ModuleOre.blockOre1.getState(NEPHRITE_ORE) }, Vein(50393467, 64, 16, 0.01, el.CALCIUM, el.MAGNESIUM, el.FERRUM))
                vein(HORIZONTAL, 0.50, { ModuleOre.blockOre1.getState(TOPAZ_ORE) }, Vein(58068649, 16, 4, 0.01, el.ALUMINIUM, el.FLUORINE))
                vein(HORIZONTAL, 0.50, { ModuleOre.blockOre2.getState(TOURMALINE_ORE) }, Vein(25988519, 16, 4, 0.01, el.NATRIUM, el.LITHIUM, el.ALUMINIUM, el.BORON))
            }

            // ネザー鉱石
            NETHERRACK {
                register(LARGE, 0.10, { ModuleOre.blockOre1.getState(NETHERRACK_APATITE_ORE) }, minY(90))
                register(PYRAMID, 0.10, { ModuleOre.blockOre1.getState(NETHERRACK_FLUORITE_ORE) }, minY(90))
                register(TINY, 0.30, { ModuleOre.blockOre1.getState(NETHERRACK_SULFUR_ORE) }, minY(20), maxY(40))
                register(IRON, 0.10, { ModuleOre.blockOre1.getState(NETHERRACK_CINNABAR_ORE) })
                register(COAL, 0.10, { ModuleOre.blockOre1.getState(NETHERRACK_MAGNETITE_ORE) })
                register(TINY, 0.10, { ModuleOre.blockOre1.getState(NETHERRACK_MOONSTONE_ORE) }, maxY(32))
            }

        }
    }
}
