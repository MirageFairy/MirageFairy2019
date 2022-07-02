package miragefairy2019.mod.fairybox

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import net.minecraft.util.ResourceLocation

val TEXTURE_INPUT_SLOT = ResourceLocation("miragefairy2019", "textures/gui/input_slot.png")
val TEXTURE_OUTPUT_SLOT = ResourceLocation("miragefairy2019", "textures/gui/output_slot.png")
val TEXTURE_FAIRY_SLOT = ResourceLocation("miragefairy2019", "textures/gui/fairy_slot.png")


val TEXTURE_ELEMENTS = ResourceLocation("miragefairy2019", "textures/gui/elements.png")

enum class TextureElement(val column: Int, val row: Int) {
    FIRE(0, 0),
    WIND(0, 1),
    GAIA(0, 2),
    AQUA(0, 3),
    DARK(0, 4),
    SHINE(0, 5),

    ATTACK(1, 0),
    CRAFT(2, 0),
    HARVEST(3, 0),
    LIGHT(4, 0),
    FLAME(5, 0),
    WATER(6, 0),
    CRYSTAL(7, 0),

    SOUND(1, 1),
    SPACE(2, 1),
    WARP(3, 1),
    SHOOT(4, 1),
    DESTROY(5, 1),
    CHEMICAL(6, 1),
    SLASH(7, 1),

    LIFE(1, 2),
    KNOWLEDGE(2, 2),
    ENERGY(3, 2),
    SUBMISSION(4, 2),
    CHRISTMAS(5, 2),
    FREEZE(6, 2),
    THUNDER(7, 2),

    LEVITATE(1, 3),
    SENSE(2, 3),
    ;

    val x = 17.0f * column + 3.0f
    val y = 17.0f * row + 3.0f
    val width = 135.0f
    val height = 135.0f
}

fun Mana.toTextureElement() = when (this) {
    Mana.FIRE -> TextureElement.FIRE
    Mana.WIND -> TextureElement.WIND
    Mana.GAIA -> TextureElement.GAIA
    Mana.AQUA -> TextureElement.AQUA
    Mana.DARK -> TextureElement.DARK
    Mana.SHINE -> TextureElement.SHINE
}

fun Erg.toTextureElement() = when (this) {
    Erg.ATTACK -> TextureElement.ATTACK
    Erg.CRAFT -> TextureElement.CRAFT
    Erg.HARVEST -> TextureElement.HARVEST
    Erg.LIGHT -> TextureElement.LIGHT
    Erg.FLAME -> TextureElement.FLAME
    Erg.WATER -> TextureElement.WATER
    Erg.CRYSTAL -> TextureElement.CRYSTAL
    Erg.SOUND -> TextureElement.SOUND
    Erg.SPACE -> TextureElement.SPACE
    Erg.WARP -> TextureElement.WARP
    Erg.SHOOT -> TextureElement.SHOOT
    Erg.DESTROY -> TextureElement.DESTROY
    Erg.CHEMICAL -> TextureElement.CHEMICAL
    Erg.SLASH -> TextureElement.SLASH
    Erg.LIFE -> TextureElement.LIFE
    Erg.KNOWLEDGE -> TextureElement.KNOWLEDGE
    Erg.ENERGY -> TextureElement.ENERGY
    Erg.SUBMISSION -> TextureElement.SUBMISSION
    Erg.CHRISTMAS -> TextureElement.CHRISTMAS
    Erg.FREEZE -> TextureElement.FREEZE
    Erg.THUNDER -> TextureElement.THUNDER
    Erg.LEVITATE -> TextureElement.LEVITATE
    Erg.SENSE -> TextureElement.SENSE
}
