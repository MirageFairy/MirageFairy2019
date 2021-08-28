package miragefairy2019.mod.modules.fairyweapon.item

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

fun playSound(world: World, player: EntityPlayer, pitch: Double) {
    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0f, pitch.toFloat())
}

fun <T> spawnParticleTargets(
        world: World,
        targets: List<T>,
        fPosition: (T) -> Vec3d,
        fColor: (T) -> Int) {
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
                    (color shr 0 and 0xFF) / 255.0)
        }
    }
}
