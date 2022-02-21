package miragefairy2019.mod.modules.fairyweapon.item

import miragefairy2019.libkt.norm1
import miragefairy2019.mod.common.magic.MagicSelectorRayTrace
import miragefairy2019.mod.formula4.boolean
import miragefairy2019.mod.formula4.float2
import miragefairy2019.mod.formula4.integer
import miragefairy2019.mod.formula4.percent0
import miragefairy2019.mod.formula4.percent2
import miragefairy2019.mod.formula4.positive
import miragefairy2019.mod.formula4.status
import miragefairy2019.mod.magic4.api.MagicArguments
import miragefairy2019.mod.magic4.api.MagicHandler
import miragefairy2019.mod.magic4.magic
import miragefairy2019.mod.magic4.world
import miragefairy2019.mod3.artifacts.FairyLog
import miragefairy2019.mod3.erg.api.EnumErgType.CRAFT
import miragefairy2019.mod3.erg.api.EnumErgType.DESTROY
import miragefairy2019.mod3.erg.api.EnumErgType.HARVEST
import miragefairy2019.mod3.erg.api.EnumErgType.LEVITATE
import miragefairy2019.mod3.erg.api.EnumErgType.LIFE
import miragefairy2019.mod3.erg.api.EnumErgType.SLASH
import miragefairy2019.mod3.erg.api.EnumErgType.WARP
import miragefairy2019.mod3.mana.api.EnumManaType.AQUA
import miragefairy2019.mod3.mana.api.EnumManaType.DARK
import miragefairy2019.mod3.mana.api.EnumManaType.FIRE
import miragefairy2019.mod3.mana.api.EnumManaType.GAIA
import miragefairy2019.mod3.mana.api.EnumManaType.SHINE
import miragefairy2019.mod3.mana.api.EnumManaType.WIND
import miragefairy2019.mod3.skill.EnumMastery.harvest
import mirrg.boron.util.UtilsMath
import mirrg.kotlin.atLeast
import mirrg.kotlin.atMost
import net.minecraft.block.BlockLeaves
import net.minecraft.block.BlockLog
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

class ItemMiragiumAxe : ItemFairyWeaponFormula4() {
    val additionalReach = status("additionalReach", { 0.0 + (!WIND + +LEVITATE) / 10.0 atMost 30.0 }, { float2 })
    val range = status("range", { (3.0 + (!GAIA + +HARVEST) / 5.0).toInt() atMost 100 }, { integer })
    val power = status("power", { 27.0 + (!DARK + +DESTROY) / 1.0 }, { float2 })
    val breakSpeed = status("breakSpeed", { 2.0 + (!AQUA + +SLASH) / 30.0 }, { float2 })
    val speedBoost = status("speedBoost", { 1.0 + !harvest / 100.0 }, { percent0 })
    val fortune = status("fortune", { 0.0 + (!SHINE + +LIFE) / 20.0 }, { float2 })
    val wear = status("wear", { 0.1 / (1.0 + (!FIRE + +CRAFT) / 20.0) * (cost / 50.0) }, { percent2 })
    val collection = status("collection", { !WARP >= 10 }, { boolean.positive })

    init {
        setHarvestLevel("axe", 2) // 鉄相当
        destroySpeed = 6.0f // 鉄相当
    }

    @SideOnly(Side.CLIENT)
    override fun getMagicDescription(itemStack: ItemStack) = "右クリックでブロックを破壊" // TODO translate

    override fun getMagic() = magic {
        val magicSelectorRayTrace = MagicSelectorRayTrace.createIgnoreEntity(world, player, additionalReach()) // 視線判定
        val magicSelectorPosition = magicSelectorRayTrace.magicSelectorPosition // 視点判定

        fun createCursor(color: Int) = object : MagicHandler() {
            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) = magicSelectorPosition.doEffect(color)
        }

        if (!hasPartnerFairy) return@magic createCursor(0xFF00FF) // パートナー妖精判定
        if (weaponItemStack.itemDamage + ceil(wear()).toInt() > weaponItemStack.maxDamage) return@magic createCursor(0xFF0000) // 耐久判定
        val blockPos = magicSelectorRayTrace.hitBlockPos ?: return@magic createCursor(0xFF8800) // 対象判定
        val targets = getTargets(blockPos)
        if (targets.isEmpty()) return@magic createCursor(0xFF8800) // 対象判定
        if (player.cooldownTracker.hasCooldown(weaponItem)) return@magic createCursor(0xFFFF00) // クールタイム判定

        // 魔法成立
        object : MagicHandler() {
            override fun onItemRightClick(hand: EnumHand): EnumActionResult {
                if (world.isRemote) return EnumActionResult.SUCCESS
                val breakSounds = mutableListOf<SoundEvent>()
                var remainingPower = power()
                var nextCoolTime = 0.0

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
                        weaponItemStack.damageItem(UtilsMath.randomInt(world.rand, wear()), player) // 耐久消費

                        // 音取得
                        blockState.block.getSoundType(blockState, world, blockPos, player).breakSound.let {
                            if (it !in breakSounds) breakSounds += it
                        }

                        nextCoolTime += 20.0 * powerCost / breakSpeed() / speedBoost() // クールタイム加算

                        // 破壊
                        FairyWeaponUtils.breakBlock(
                            world,
                            player,
                            magicSelectorRayTrace.rayTraceResult!!.sideHit,
                            weaponItemStack,
                            targetBlockPos,
                            UtilsMath.randomInt(world.rand, fortune()),
                            collection()
                        )

                    }
                }

                breakSounds.take(4).forEach { world.playSound(null, player.posX, player.posY, player.posZ, it, SoundCategory.PLAYERS, 1.0f, 1.0f) } // 効果音
                player.cooldownTracker.setCooldown(this@ItemMiragiumAxe, ceil(nextCoolTime).toInt() atLeast 20) // クールタイム発生

                return EnumActionResult.SUCCESS
            }

            override fun onClientUpdate(itemSlot: Int, isSelected: Boolean) {
                magicSelectorPosition.doEffect(0xFFFFFF)
                spawnParticleTargets(world, targets, { Vec3d(it).addVector(0.5, 0.5, 0.5) }, { 0x00FF00 })
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
        FairyLog.blockFairyLog() -> true
        else -> false
    }

    private fun MagicArguments.isLeaves(blockPos: BlockPos) = when (world.getBlockState(blockPos).block) {
        is BlockLeaves -> true
        else -> false
    }

    /*
        override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack> {

            run {

                // 基点検索
                var yMax = blockPos.y
                for (yi in 1 until maxHeight[fairy.y]) {
                    yMax = if (canBreak(world, blockPos.add(0, yi, 0))) {
                        blockPos.y + yi
                    } else {
                        break
                    }
                }

                // 破壊
                var successed = 0
                var power2 = power[fairy.y]
                for (y in yMax downTo yMin) {
                    val blockPos2 = BlockPos(blockPos.x, y, blockPos.z)
                    val blockState = world.getBlockState(blockPos2)
                    val blockHardness = blockState.getBlockHardness(world, blockPos2)

                    // 岩盤を見つけた
                    if (blockHardness < 0) break

                    // パワーが足りないので破壊をやめる
                    if (power2 < blockHardness) break

                    // 耐久が0のときは破壊をやめる
                    if (weaponItemStack.itemDamage >= weaponItemStack.maxDamage) break
                    if (world.rand.nextDouble() < wear[fairy.y]) weaponItemStack.damageItem(1, player)
                    power2 -= blockHardness.toDouble()
                    FairyWeaponUtils.breakBlock(world, player, rayTraceResult.sideHit, weaponItemStack, blockPos2, fortune[fairy.y], collection[fairy.y])
                    successed++
                }
                if (successed > 0) {

                    // エフェクト
                    player.playSound(breakSound, 1.0f, 1.0f)

                    // クールタイム
                    player.cooldownTracker.setCooldown(this, (coolTime[fairy.y] * (1 - power2 / power[fairy.y])).toInt())
                }
            }

            return ActionResult(EnumActionResult.SUCCESS, weaponItemStack)
        }
    */

}

/** [predicate]にマッチするような[next]の要素のリストを返しつつ、マッチしたものを[result]に追加します。 */
private fun gain(result: MutableList<BlockPos>, next: List<BlockPos>, predicate: (BlockPos) -> Boolean): List<BlockPos> {
    val next2 = mutableListOf<BlockPos>()
    next.forEach { blockPos2 ->
        if (predicate(blockPos2)) {
            next2 += blockPos2
            result += blockPos2
        }
    }
    return next2
}

/** [next]の各要素に隣接する未訪問のブロックのリスト返しつつ、訪問済みにします。 */
private fun extend(visited: MutableList<BlockPos>, next: List<BlockPos>): List<BlockPos> {
    val next2 = mutableListOf<BlockPos>()
    next.forEach { blockPos2 ->
        (-1..1).forEach { x ->
            (-1..1).forEach { y ->
                (-1..1).forEach { z ->
                    val blockPos3 = blockPos2.add(x, y, z)
                    if (blockPos3 !in visited) {
                        next2 += blockPos3
                        visited += blockPos3
                    }
                }
            }
        }
    }
    return next2
}

/** 最初に[zero]に回収判定を行い、[range]の分だけ幅優先探索を行います。 */
private fun search(range: Int, visited: MutableList<BlockPos>, zero: List<BlockPos>, predicate: (BlockPos) -> Boolean): List<BlockPos> {
    val result = mutableListOf<BlockPos>()
    var next = zero

    if (next.isEmpty()) return result
    next = gain(result, next, predicate)
    repeat(range) {
        if (next.isEmpty()) return result
        next = extend(visited, next)
        if (next.isEmpty()) return result
        next = gain(result, next, predicate)
    }
    return result
}

/** 最初に[zero]に回収判定を行わず、[range]の分だけ幅優先探索を行います。 */
private fun extendSearch(range: Int, visited: MutableList<BlockPos>, zero: List<BlockPos>, predicate: (BlockPos) -> Boolean): List<BlockPos> {
    val result = mutableListOf<BlockPos>()
    var next = zero

    repeat(range) {
        if (next.isEmpty()) return result
        next = extend(visited, next)
        if (next.isEmpty()) return result
        next = gain(result, next, predicate)
    }
    return result
}

/*
        // 対象が原木でない場合は不発
        if (!isLog(world, blockPos)) return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStack);

        // きこり発動
        {

            // 音取得
            SoundEvent breakSound;
            {
                IBlockState blockState = world.getBlockState(blockPos);
                breakSound = blockState.getBlock().getSoundType(blockState, world, blockPos, player).getBreakSound();
            }

            int yMin = blockPos.getY();

            // 基点検索
            int yMax = blockPos.getY();
            for (int yi = 1; yi < maxHeight.get(fairy.y); yi++) {
                if (isLog(world, blockPos.add(0, yi, 0))) {
                    yMax = blockPos.getY() + yi;
                } else {
                    break;
                }
            }

            // 破壊
            int successed = 0;
            double power2 = power.get(fairy.y);
            for (int y = yMax; y >= yMin; y--) {
                BlockPos blockPos2 = new BlockPos(blockPos.getX(), y, blockPos.getZ());

                IBlockState blockState = world.getBlockState(blockPos2);
                float blockHardness = blockState.getBlockHardness(world, blockPos2);

                // 岩盤を見つけた
                if (blockHardness < 0) break;

                // パワーが足りないので破壊をやめる
                if (power2 < blockHardness) break;

                // 耐久が0のときは破壊をやめる
                if (itemStack.getItemDamage() >= itemStack.getMaxDamage()) break;

                if (world.rand.nextDouble() < wear.get(fairy.y)) itemStack.damageItem(1, player);
                power2 -= blockHardness;
                FairyWeaponUtils.breakBlock(world, player, rayTraceResult.sideHit, itemStack, blockPos2, fortune.get(fairy.y), collection.get(fairy.y));
                successed++;

            }

            if (successed > 0) {

                // エフェクト
                player.playSound(breakSound, 1.0F, 1.0F);

                // クールタイム
                player.getCooldownTracker().setCooldown(this, (int) (coolTime.get(fairy.y) * (1 - power2 / power.get(fairy.y))));

            }

        }

*/