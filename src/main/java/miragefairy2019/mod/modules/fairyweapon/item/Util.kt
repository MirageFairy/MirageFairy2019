package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.libkt.equalsItemDamageTag
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

fun playSound(world: World, player: EntityPlayer, soundEvent: SoundEvent, volume: Float = 1.0f, pitch: Float = 1.0f) {
    world.playSound(null, player.posX, player.posY, player.posZ, soundEvent, SoundCategory.PLAYERS, volume, pitch)
}

fun <T> spawnParticleTargets(
    world: World,
    targets: List<T>,
    fPosition: (T) -> Vec3d,
    fColor: (T) -> Int
) {
    val rate = 5 / targets.size.coerceAtLeast(5).toDouble()
    targets.forEach { target ->
        if (Math.random() < 0.2 * rate) {
            val position = fPosition(target)
            val color = fColor(target)
            world.spawnParticle(
                EnumParticleTypes.SPELL_MOB,
                position.x,
                position.y,
                position.z,
                (color shr 16 and 0xFF) / 255.0,
                (color shr 8 and 0xFF) / 255.0,
                (color shr 0 and 0xFF) / 255.0
            )
        }
    }
}

/**
 * 考慮する：アイテム・メタ・タグ
 */
fun findItem(player: EntityPlayer, itemStackTarget: ItemStack): ItemStack? {
    player.getHeldItem(EnumHand.OFF_HAND).let { if (itemStackTarget.equalsItemDamageTag(it)) return it }
    player.getHeldItem(EnumHand.MAIN_HAND).let { if (itemStackTarget.equalsItemDamageTag(it)) return it }
    (0 until player.inventory.sizeInventory).forEach { i ->
        player.inventory.getStackInSlot(i).let { if (itemStackTarget.equalsItemDamageTag(it)) return it }
    }
    return null
}
