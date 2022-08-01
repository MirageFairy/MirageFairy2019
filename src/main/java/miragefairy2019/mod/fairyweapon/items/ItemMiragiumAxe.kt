package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.api.Erg.CRAFT
import miragefairy2019.api.Erg.DESTROY
import miragefairy2019.api.Erg.HARVEST
import miragefairy2019.api.Erg.LEVITATE
import miragefairy2019.api.Erg.LIFE
import miragefairy2019.api.Erg.SLASH
import miragefairy2019.api.Erg.WARP
import miragefairy2019.api.Mana.AQUA
import miragefairy2019.api.Mana.DARK
import miragefairy2019.api.Mana.FIRE
import miragefairy2019.api.Mana.GAIA
import miragefairy2019.api.Mana.SHINE
import miragefairy2019.api.Mana.WIND
import miragefairy2019.lib.BlockRayTraceWrapper
import miragefairy2019.lib.MagicSelector
import miragefairy2019.lib.doEffect
import miragefairy2019.lib.position
import miragefairy2019.lib.rayTraceBlock
import miragefairy2019.libkt.norm1
import miragefairy2019.libkt.randomInt
import miragefairy2019.mod.artifacts.blockFairyLog
import miragefairy2019.mod.fairyweapon.MagicMessage
import miragefairy2019.mod.fairyweapon.breakBlock
import miragefairy2019.mod.fairyweapon.displayText
import miragefairy2019.mod.fairyweapon.extendSearch
import miragefairy2019.mod.fairyweapon.magic4.MagicArguments
import miragefairy2019.mod.fairyweapon.magic4.MagicHandler
import miragefairy2019.mod.fairyweapon.magic4.boolean
import miragefairy2019.mod.fairyweapon.magic4.boost
import miragefairy2019.mod.fairyweapon.magic4.float2
import miragefairy2019.mod.fairyweapon.magic4.integer
import miragefairy2019.mod.fairyweapon.magic4.magic
import miragefairy2019.mod.fairyweapon.magic4.percent2
import miragefairy2019.mod.fairyweapon.magic4.positive
import miragefairy2019.mod.fairyweapon.magic4.status
import miragefairy2019.mod.fairyweapon.magic4.world
import miragefairy2019.mod.fairyweapon.search
import miragefairy2019.mod.fairyweapon.spawnParticleTargets
import miragefairy2019.mod.skill.Mastery
import mirrg.kotlin.hydrogen.atLeast
import mirrg.kotlin.hydrogen.atMost
import net.minecraft.block.BlockLeaves
import net.minecraft.block.BlockLog
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.ceil

// TODO -> : miragefairy2019.mod.fairyweapon.items.ItemMiragiumToolBase
class ItemMiragiumAxe : ItemFairyWeaponMagic4() {
    val power = status("power", { 27.0 + (!DARK + !DESTROY) / 1.0 }, { float2 })
    val breakSpeed = status("breakSpeed", { (2.0 + (!AQUA + !SLASH) / 30.0) * costFactor }, { float2 })
    val speedBoost = status("speedBoost", { 1.0 + !Mastery.lumbering / 100.0 }, { boost })
    val additionalReach = status("additionalReach", { 0.0 + (!WIND + !LEVITATE) / 10.0 atMost 30.0 }, { float2 })
    val range = status("range", { (3.0 + (!GAIA + !HARVEST) / 5.0).toInt() atMost 100 }, { integer })
    val fortune = status("fortune", { 0.0 + (!SHINE + !LIFE) / 20.0 }, { float2 })
    val wear = status("wear", { 0.1 / (1.0 + (!FIRE + !CRAFT) / 20.0) * costFactor }, { percent2 })
    val collection = status("collection", { !WARP >= 10 }, { boolean.positive })

    init {
        setHarvestLevel("axe", 2) // 鉄相当
        destroySpeed = 6.0f // 鉄相当
    }

    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = listOf("右クリックでブロックを破壊") // TRANSLATE

    override fun getMagic() = magic {
        val rayTraceMagicSelector = MagicSelector.rayTraceBlock(world, player, additionalReach()) // 視線判定
        val cursorMagicSelector = rayTraceMagicSelector.position // 視点判定

        fun error(color: Int, magicMessage: MagicMessage) = object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) = cursorMagicSelector.item.doEffect(color)
            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                if (!world.isRemote) player.sendStatusMessage(magicMessage.displayText, true)
                return EnumActionResult.FAIL
            }
        }

        if (!hasPartnerFairy) return@magic error(0xFF00FF, MagicMessage.NO_FAIRY) // パートナー妖精判定
        if (weaponItemStack.itemDamage + ceil(wear()).toInt() > weaponItemStack.maxDamage) return@magic error(0xFF0000, MagicMessage.INSUFFICIENT_DURABILITY) // 耐久判定
        if (rayTraceMagicSelector.item.rayTraceWrapper !is BlockRayTraceWrapper) return@magic error(0xFF8800, MagicMessage.NO_TARGET) // 対象判定
        val targets = getTargets(rayTraceMagicSelector.item.rayTraceWrapper.blockPos)
        if (targets.isEmpty()) return@magic error(0xFF8800, MagicMessage.NO_TARGET) // 対象判定
        if (player.cooldownTracker.hasCooldown(weaponItem)) return@magic error(0xFFFF00, MagicMessage.COOL_TIME) // クールタイム判定

        // 魔法成立
        object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                cursorMagicSelector.item.doEffect(0xFFFFFF)
                spawnParticleTargets(world, targets, { Vec3d(it).addVector(0.5, 0.5, 0.5) }, { 0x00FF00 })
            }

            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                if (world.isRemote) return EnumActionResult.SUCCESS
                val breakSounds = mutableListOf<SoundEvent>()
                var remainingPower = power()
                var nextCoolTime = 0.0
                var collected = false

                run finishMining@{
                    targets.forEach { targetBlockPos ->
                        val blockState = world.getBlockState(targetBlockPos)
                        val hardness = blockState.getBlockHardness(world, targetBlockPos)
                        if (hardness < 0) return@finishMining // 岩盤にあたった
                        val powerCost = hardness.toDouble() atLeast 0.2 // 最低パワー0.2（葉と同じ）は消費

                        if (remainingPower < powerCost) return@finishMining // パワーが足りない
                        if (weaponItemStack.itemDamage + ceil(wear()).toInt() > weaponItemStack.maxDamage) return@finishMining // 耐久が足りない

                        // 成立

                        remainingPower -= powerCost // パワー消費
                        weaponItemStack.damageItem(world.rand.randomInt(wear()), player) // 耐久消費

                        // 音取得
                        blockState.block.getSoundType(blockState, world, rayTraceMagicSelector.item.rayTraceWrapper.blockPos, player).breakSound.let {
                            if (it !in breakSounds) breakSounds += it
                        }

                        nextCoolTime += 20.0 * powerCost / breakSpeed() / speedBoost() // クールタイム加算

                        // 破壊
                        breakBlock(
                            world = world,
                            player = player,
                            itemStack = weaponItemStack,
                            blockPos = targetBlockPos,
                            facing = rayTraceMagicSelector.item.rayTraceWrapper.side,
                            fortune = world.rand.randomInt(fortune()),
                            collection = collection()
                        )
                        if (collection()) collected = true

                    }
                }

                // 効果音
                if (collected) world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f)
                breakSounds.take(4).forEach { world.playSound(null, player.posX, player.posY, player.posZ, it, SoundCategory.PLAYERS, 1.0f, 1.0f) }

                player.cooldownTracker.setCooldown(this@ItemMiragiumAxe, ceil(nextCoolTime).toInt() atLeast 20) // クールタイム発生

                return EnumActionResult.SUCCESS
            }
        }
    }

    private fun MagicArguments.getTargets(blockPos: BlockPos): List<BlockPos> {
        val visited = mutableListOf<BlockPos>()
        val logBlockPosList = search(range(), visited, listOf(blockPos)) { isLog(it) } // rangeの分だけ原木を幅優先探索
        visited.clear()
        visited += logBlockPosList
        val leavesBlockPosList = extendSearch(4, visited, logBlockPosList) { isLeaves(it) } // 4マスまで葉を探す
        return (logBlockPosList + leavesBlockPosList).sortedByDescending { it norm1 blockPos } // 遠い順に返す
    }

    private fun MagicArguments.isLog(blockPos: BlockPos) = when (world.getBlockState(blockPos).block) {
        is BlockLog -> true
        blockFairyLog() -> true
        else -> false
    }

    private fun MagicArguments.isLeaves(blockPos: BlockPos) = when (world.getBlockState(blockPos).block) {
        is BlockLeaves -> true
        else -> false
    }

}
