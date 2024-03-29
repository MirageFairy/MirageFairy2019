package miragefairy2019.mod.skill

@Suppress("EnumEntryName")
enum class Mastery(override val parent: IMastery?, val layer: Int) : IMastery {
    root(null, 0),
    /**/ combat(root, 1),
    /**/ /**/ closeCombat(combat, 2),
    /**/ /**/ rangedCombat(combat, 2),
    /**/ /**/ magicCombat(combat, 2),
    /**/ production(root, 1),
    /**/ /**/ harvest(production, 2),
    /**/ /**/ /**/ agriculture(harvest, 3),
    /**/ /**/ /**/ mining(harvest, 3),
    /**/ /**/ /**/ lumbering(harvest, 3),
    /**/ /**/ /**/ flowerPicking(harvest, 3),
    /**/ /**/ processing(production, 2),
    /**/ /**/ /**/ fairySummoning(processing, 3),
    /**/ /**/ fabrication(production, 2),
    ///**/ /**/ /**/ brewing(fabrication, 3),
    ;

    override val coefficient
        get() = when (layer) {
            0 -> 2
            1 -> 3
            2 -> 5
            3 -> 10
            else -> throw RuntimeException()
        }
}
