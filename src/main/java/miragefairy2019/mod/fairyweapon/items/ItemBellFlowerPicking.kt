package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.api.Erg
import miragefairy2019.api.Erg.WARP
import miragefairy2019.api.Mana
import miragefairy2019.api.PickHandlerRegistry
import miragefairy2019.lib.MagicSelector
import miragefairy2019.lib.blocks
import miragefairy2019.lib.circle
import miragefairy2019.lib.doEffect
import miragefairy2019.lib.position
import miragefairy2019.lib.rayTraceBlock
import miragefairy2019.libkt.createItemStack
import miragefairy2019.libkt.drop
import miragefairy2019.libkt.randomInt
import miragefairy2019.mod.artifacts.MirageFlower
import miragefairy2019.mod.fairyweapon.magic4.EnumVisibility
import miragefairy2019.mod.fairyweapon.magic4.MagicHandler
import miragefairy2019.mod.fairyweapon.magic4.boolean
import miragefairy2019.mod.fairyweapon.magic4.duration
import miragefairy2019.mod.fairyweapon.magic4.float2
import miragefairy2019.mod.fairyweapon.magic4.integer
import miragefairy2019.mod.fairyweapon.magic4.magic
import miragefairy2019.mod.fairyweapon.magic4.percent1
import miragefairy2019.mod.fairyweapon.magic4.percent2
import miragefairy2019.mod.fairyweapon.magic4.positive
import miragefairy2019.mod.fairyweapon.magic4.status
import miragefairy2019.mod.fairyweapon.magic4.world
import miragefairy2019.mod.fairyweapon.playSound
import miragefairy2019.mod.fairyweapon.spawnParticleTargets
import miragefairy2019.mod.skill.EnumMastery
import mirrg.kotlin.hydrogen.atMost
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.item.EntityXPOrb
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvent
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow

class ItemBellFlowerPicking(additionalBaseStatus: Double, extraItemDropRateFactor: Double, maxExtraItemDropRate: Double) : ItemFairyWeaponBase3(Mana.DARK, EnumMastery.flowerPicking) {
    val strength = createStrengthStatus(additionalBaseStatus, Erg.SOUND)
    val extent = createExtentStatus(additionalBaseStatus, Erg.SPACE)
    val endurance = createEnduranceStatus(additionalBaseStatus, Erg.SLASH)
    val production = createProductionStatus(additionalBaseStatus, Erg.HARVEST)
    val cost = createCostStatus()

    val pitch = status("pitch", { -(cost / 50.0 - 1) * 12 }, { float2 }) { setRange(-12.0..12.0).setVisibility(EnumVisibility.DETAIL) }
    val maxTargetCount = status("maxTargetCount", { 2 + floor(+!strength * 0.1).toInt() }, { integer }) { setRange(1..100).setVisibility(EnumVisibility.DETAIL) }
    val fortune = status("fortune", { 3 + !production * 0.1 }, { float2 }) { setRange(0.0..100.0).setVisibility(EnumVisibility.DETAIL) }
    val additionalReach = status("additionalReach", { !extent * 0.1 }, { float2 }) { setRange(0.0..10.0).setVisibility(EnumVisibility.DETAIL) }
    val radius = status("radius", { 4 + !extent * 0.05 }, { float2 }) { setRange(0.0..10.0).setVisibility(EnumVisibility.DETAIL) }
    val wear = status("wear", { 1.0 / (1 + !endurance * 0.03) }, { percent2 }) { setVisibility(EnumVisibility.DETAIL) }
    val coolTime = status("coolTime", { cost * 0.5 }, { duration }) { setVisibility(EnumVisibility.DETAIL) }
    val collection = status("collection", { !WARP >= 10 }, { boolean.positive }) { setVisibility(EnumVisibility.ALWAYS) }
    val extraItemDropRate = status("extraItemDropRate", { 0.1 + extraItemDropRateFactor * !mastery atMost maxExtraItemDropRate }, { percent1 }) { setVisibility(EnumVisibility.ALWAYS) }

    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = "右クリックでミラージュフラワーを収穫" // TODO translate

    override fun getMagic() = magic {

        // 視線判定
        val magicSelectorRayTrace = MagicSelector.rayTraceBlock(world, player, additionalReach())

        // 視点判定
        val magicSelectorPosition = magicSelectorRayTrace.position

        // 妖精を持っていない場合、中止
        if (!hasPartnerFairy) return@magic object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                magicSelectorPosition.item.doEffect(0xFF00FF) // 視点
            }
        }

        // 範囲判定
        val magicSelectorCircle = magicSelectorPosition.circle(radius())
        val magicSelectorBlocks = magicSelectorCircle.blocks()

        // 対象計算
        val listTarget = magicSelectorBlocks.item
            .sortedBy { it.second } // 近い順にソート
            .map { it.first } // BlockPosだけを抽出
            .asSequence() // 先頭から順番に判定
            .mapNotNull a@{ blockPos -> // Executorを取得
                val pickExecutor = PickHandlerRegistry.pickHandlers.asSequence()
                    .map { pickHandler -> pickHandler.getExecutor(world, blockPos, player) }
                    .filterNotNull()
                    .firstOrNull() ?: return@a null
                Pair(blockPos, pickExecutor)
            }
            .take(maxTargetCount()) // 最大個数を制限
            .toList() // リストにする

        // 資源がない場合、中止
        if (weaponItemStack.itemDamage + ceil(wear()).toInt() > weaponItemStack.maxDamage) return@magic object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                magicSelectorPosition.item.doEffect(0xFF0000) // 視点
                magicSelectorCircle.item.doEffect() // 範囲
            }
        }

        // 発動対象がない場合、中止
        if (listTarget.isEmpty()) return@magic object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                magicSelectorPosition.item.doEffect(0x00FFFF) // 視点
                magicSelectorCircle.item.doEffect() // 範囲
            }
        }

        // クールタイムが残っている場合、中止
        if (player.cooldownTracker.hasCooldown(this@ItemBellFlowerPicking)) return@magic object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                magicSelectorPosition.item.doEffect(0xFFFF00) // 視点
                magicSelectorCircle.item.doEffect() // 範囲
                spawnParticleTargets(world, listTarget, { Vec3d(it.first).addVector(0.5, 0.5, 0.5) }, { 0xFFFF00 }) // 対象
            }
        }

        // 魔法成立
        object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                magicSelectorPosition.item.doEffect(0x00FF00) // 視点
                magicSelectorCircle.item.doEffect() // 範囲
                spawnParticleTargets(world, listTarget, { Vec3d(it.first).addVector(0.5, 0.5, 0.5) }, { 0x00FF00 }) // 対象
            }

            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                var breakSound: SoundEvent? = null
                var collected = false
                var targetCount = 0

                run targets@{
                    for (pair in listTarget) {
                        val blockPos = pair.first
                        val pickExecutor = pair.second

                        if (weaponItemStack.itemDamage + ceil(wear()).toInt() > weaponItemStack.maxDamage) return@targets // 耐久が足りないので中止
                        if (targetCount + 1 > maxTargetCount()) return@targets // パワーが足りないので中止

                        // 成立

                        // 資源消費
                        weaponItemStack.damageItem(world.rand.randomInt(wear()), player)
                        targetCount++

                        // 音取得
                        run {
                            val blockState = world.getBlockState(blockPos)
                            breakSound = blockState.block.getSoundType(blockState, world, blockPos, player).breakSound
                        }

                        // 収穫
                        run {

                            // 収穫試行
                            val result = pickExecutor.tryPick(world.rand.randomInt(fortune()))
                            if (!result) return@targets

                            // 種の追加ドロップ
                            if (!world.isRemote) {
                                val count = world.rand.randomInt(extraItemDropRate())
                                if (count > 0) MirageFlower.itemMirageFlowerSeeds().createItemStack(count = count).drop(world, Vec3d(blockPos).addVector(0.5, 0.5, 0.5)).setNoPickupDelay()
                            }

                            // 破壊したばかりのブロックの周辺のアイテムを集める
                            if (collection()) {
                                world.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB(blockPos)).forEach {
                                    collected = true
                                    it.setPosition(player.posX, player.posY, player.posZ)
                                    it.setNoPickupDelay()
                                }
                                world.getEntitiesWithinAABB(EntityXPOrb::class.java, AxisAlignedBB(blockPos)).forEach {
                                    collected = true
                                    it.setPosition(player.posX, player.posY, player.posZ)
                                }
                            }

                        }

                        // エフェクト
                        world.spawnParticle(
                            EnumParticleTypes.SPELL_MOB,
                            blockPos.x + 0.5,
                            blockPos.y + 0.5,
                            blockPos.z + 0.5,
                            (color shr 16 and 0xFF) / 255.0,
                            (color shr 8 and 0xFF) / 255.0,
                            (color shr 0 and 0xFF) / 255.0
                        )

                    }
                }

                if (targetCount >= 1) {

                    // エフェクト
                    playSound(world, player, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 2.0.pow(pitch() / 12.0).toFloat())
                    world.playSound(null, player.posX, player.posY, player.posZ, breakSound!!, SoundCategory.PLAYERS, 1.0f, 1.0f)

                    // クールタイム
                    val ratio = targetCount / (maxTargetCount()).toDouble()
                    player.cooldownTracker.setCooldown(this@ItemBellFlowerPicking, (coolTime() * ratio.pow(0.5)).toInt())

                }
                if (collected) {

                    // エフェクト
                    world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f)

                }

                return EnumActionResult.SUCCESS
            }
        }
    }
}
