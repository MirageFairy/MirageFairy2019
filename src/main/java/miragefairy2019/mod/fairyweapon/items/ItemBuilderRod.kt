package miragefairy2019.mod.fairyweapon.items

import miragefairy2019.api.Erg
import miragefairy2019.api.Mana
import miragefairy2019.lib.BlockRayTraceWrapper
import miragefairy2019.lib.MagicSelector
import miragefairy2019.lib.TooLargeBehaviour
import miragefairy2019.lib.doEffect
import miragefairy2019.lib.position
import miragefairy2019.lib.rayTraceBlock
import miragefairy2019.lib.treeSearch
import miragefairy2019.libkt.copy
import miragefairy2019.libkt.randomInt
import miragefairy2019.mod.fairyweapon.MagicMessage
import miragefairy2019.mod.fairyweapon.addCoolTimeToFairyWeapon
import miragefairy2019.mod.fairyweapon.displayText
import miragefairy2019.mod.fairyweapon.findAllItems
import miragefairy2019.mod.fairyweapon.magic4.FormulaArguments
import miragefairy2019.mod.fairyweapon.magic4.MagicArguments
import miragefairy2019.mod.fairyweapon.magic4.MagicHandler
import miragefairy2019.mod.fairyweapon.magic4.boost
import miragefairy2019.mod.fairyweapon.magic4.float2
import miragefairy2019.mod.fairyweapon.magic4.integer
import miragefairy2019.mod.fairyweapon.magic4.magic
import miragefairy2019.mod.fairyweapon.magic4.percent2
import miragefairy2019.mod.fairyweapon.magic4.status
import miragefairy2019.mod.fairyweapon.magic4.world
import miragefairy2019.mod.fairyweapon.spawnParticleTargets
import miragefairy2019.mod.skill.Mastery
import mirrg.kotlin.hydrogen.atLeast
import mirrg.kotlin.hydrogen.atMost
import mirrg.kotlin.hydrogen.ceilToInt
import mirrg.kotlin.hydrogen.floorToInt
import mirrg.kotlin.hydrogen.max
import net.minecraft.block.SoundType
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import kotlin.math.abs

open class ItemBuilderRod : ItemFairyWeaponMagic4() {
    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = listOf("右クリックでブロックを設置") // TRANSLATE

    override fun getMagic() = magic {
        val rayTraceMagicSelector = MagicSelector.rayTraceBlock(world, player, getAdditionalReach(this)) // 視線判定
        val cursorMagicSelector = rayTraceMagicSelector.position // 視点判定

        fun fail(color: Int, magicMessage: MagicMessage) = object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                cursorMagicSelector.item.doEffect(color)
            }

            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                if (!world.isRemote) player.sendStatusMessage(magicMessage.displayText, true)
                return EnumActionResult.FAIL
            }
        }

        if (!hasPartnerFairy) return@magic fail(0xFF00FF, MagicMessage.NO_FAIRY) // パートナー妖精判定

        if (weaponItemStack.itemDamage + getDurabilityCostPerBlock(this).ceilToInt() > weaponItemStack.maxDamage) return@magic fail(0xFF0000, MagicMessage.INSUFFICIENT_DURABILITY) // 耐久判定

        if (rayTraceMagicSelector.item.rayTraceWrapper !is BlockRayTraceWrapper) return@magic fail(0x00FFFF, MagicMessage.NO_TARGET) // ブロックにヒットしていないならターゲット無し扱い

        val sampleItemStack: ItemStack = run {
            val blockPos = rayTraceMagicSelector.item.rayTraceWrapper.blockPos
            val blockState = world.getBlockState(blockPos)
            blockState.block.getPickBlock(blockState, rayTraceMagicSelector.item.rayTraceWrapper.rayTraceResult, world, blockPos, player)
        } // 標本アイテム
        val sampleItem = sampleItemStack.item
        if (sampleItem !is ItemBlock) return@magic fail(0x00FFFF, MagicMessage.NO_TARGET) // 非対応ブロックはターゲットなし扱い

        val resourceItemStacks = if (player.isCreative) mutableListOf() else findAllItems(player, sampleItemStack).toMutableList()
        if (!player.isCreative && resourceItemStacks.isEmpty()) return@magic fail(0xFF00FF, MagicMessage.INSUFFICIENT_RESOURCE) // 素材がない

        val targets = getTargetBlockPoses(this@magic, rayTraceMagicSelector.item.rayTraceWrapper) // ターゲット決定
        if (!targets.hasNext()) return@magic fail(0x00FFFF, MagicMessage.NO_TARGET) // ターゲットなし判定
        if (player.cooldownTracker.hasCooldown(weaponItem)) return@magic fail(0xFFFF00, MagicMessage.COOL_TIME) // クールタイム判定

        // 魔法成立
        object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                cursorMagicSelector.item.doEffect(0xFFFFFF)
                spawnParticleTargets(world, targets.asSequence().toList().map { Vec3d(it).addVector(0.5, 0.5, 0.5) })
            }

            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                if (world.isRemote) { // クライアントワールドの場合、腕を振るだけ
                    player.swingArm(hand) // エフェクト
                    return EnumActionResult.SUCCESS
                }

                var blockCount = 0
                var soundType: SoundType? = null

                // 行使
                run finish@{
                    targets.forEach next@{ target ->
                        fun pull(): ItemStack? {
                            if (player.isCreative) return sampleItemStack.copy(count = 1)
                            while (resourceItemStacks.isNotEmpty() && resourceItemStacks.first().isEmpty) {
                                resourceItemStacks.removeAt(0)
                            }
                            return resourceItemStacks.firstOrNull()
                        }

                        val damage = world.rand.randomInt(getDurabilityCostPerBlock(this@magic)) // 耐久コスト
                        if (weaponItemStack.itemDamage + damage > weaponItemStack.maxDamage) return@finish // 耐久が尽きた
                        val resourceItemStack = pull() ?: return@finish // 資材が尽きた
                        if (!world.getBlockState(target).block.isReplaceable(world, target)) return@next // 設置先が占有済み
                        val side = rayTraceMagicSelector.item.rayTraceWrapper.side
                        if (!world.isBlockModifiable(player, target)) return@next // 改変権限無し
                        if (!player.canPlayerEdit(target, side, resourceItemStack)) return@next // 改変権限無し
                        if (!world.mayPlace(sampleItem.block, target, false, side, player)) return@next // 設置不可能
                        val hitX = (rayTraceMagicSelector.item.rayTraceWrapper.targetPosition.x - rayTraceMagicSelector.item.rayTraceWrapper.blockPos.x).toFloat()
                        val hitY = (rayTraceMagicSelector.item.rayTraceWrapper.targetPosition.y - rayTraceMagicSelector.item.rayTraceWrapper.blockPos.y).toFloat()
                        val hitZ = (rayTraceMagicSelector.item.rayTraceWrapper.targetPosition.z - rayTraceMagicSelector.item.rayTraceWrapper.blockPos.z).toFloat()
                        val metadata = sampleItem.getMetadata(resourceItemStack.metadata)
                        val blockState = sampleItem.block.getStateForPlacement(world, target, side, hitX, hitY, hitZ, metadata, player, EnumHand.MAIN_HAND)

                        val result = sampleItem.placeBlockAt(resourceItemStack, player, world, target, side, hitX, hitY, hitZ, blockState) // 設置

                        if (result) { // 設置成功
                            blockCount++
                            weaponItemStack.damageItem(damage, player)
                            resourceItemStack.shrink(1)
                            if (soundType == null) {
                                val newBlockState = world.getBlockState(target)
                                soundType = newBlockState.block.getSoundType(newBlockState, world, target, player)
                            }
                        }
                    }
                }

                // 成立時
                if (!player.isCreative) addCoolTimeToFairyWeapon(this@ItemBuilderRod, player, (20.0 * blockCount.toDouble() / getBlocksPerSecond(this@magic)).ceilToInt()) // クールタイム

                // エフェクト
                soundType?.let {
                    world.playSound(null, rayTraceMagicSelector.item.rayTraceWrapper.surfaceBlockPos, it.placeSound, SoundCategory.BLOCKS, (it.volume + 1.0f) / 2.0f, it.pitch * 0.8f)
                }

                return EnumActionResult.SUCCESS
            }
        }
    }


    open fun getAdditionalReach(a: MagicArguments) = 0.0


    val wear = status("wear", { 0.1 / (1.0 + !Mana.FIRE / 20.0 + !Erg.WARP / 10.0) }, { percent2 })

    open fun getDurabilityCostPerBlock(a: FormulaArguments) = wear(a)


    val speed = status("speed", { 10.0 + !Mana.GAIA / 4.0 + !Erg.CRAFT / 2.0 }, { float2 })

    val speedBoost = status("speedBoost", { 1.0 + !Mastery.fabrication / 100.0 }, { boost })

    open fun getBlocksPerSecond(a: FormulaArguments) = speed(a) * speedBoost(a)


    val range = status("range", { (50.0 * costFactor / 3 - 2.0).floorToInt() atMost 32 atLeast 1 }, { integer })

    open fun getRange(a: MagicArguments) = range(a)

    /** このイテレータは処理中に逐次的に呼び出されるパターンと、処理前に一括で呼び出されるパターンがあります。 */
    open fun getTargetBlockPoses(a: MagicArguments, rayTraceWrapper: BlockRayTraceWrapper): Iterator<BlockPos> = iterator {
        val originBlockPos = rayTraceWrapper.blockPos
        val originBlockState = a.world.getBlockState(originBlockPos)
        val orientations: Set<EnumFacing> = EnumFacing.VALUES.toSet() - rayTraceWrapper.side - rayTraceWrapper.side.opposite
        val range = getRange(a)

        // 探索
        val result = treeSearch(
            a.world,
            listOf(rayTraceWrapper.surfaceBlockPos),
            mutableSetOf(),
            includeZero = true,
            neighborhoodType = { orientations.map { orientation -> it.offset(orientation) }.iterator() },
            tooLargeBehaviour = TooLargeBehaviour.IGNORE
        ) { it, _ ->
            val distance = abs(it.x - originBlockPos.x) max abs(it.y - originBlockPos.y) max abs(it.z - originBlockPos.z)
            if (distance > range) return@treeSearch null // 範囲を超える
            if (!a.world.getBlockState(it).block.isReplaceable(a.world, it)) return@treeSearch null // 埋まっている
            if (a.world.getBlockState(it.offset(rayTraceWrapper.side.opposite)) != originBlockState) return@treeSearch null // ブロックが連続でない
            Unit
        }

        result.forEach {
            yield(it.blockPos)
        }
    }

}
