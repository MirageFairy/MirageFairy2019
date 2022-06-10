package miragefairy2019.mod.playeraura

import miragefairy2019.api.ManaSet
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

object ApiPlayerAura {
    lateinit var playerAuraManager: IPlayerAuraManager
}

interface IPlayerAuraManager {
    /** Client World Only */
    @SideOnly(Side.CLIENT)
    fun getClientPlayerAuraHandler(): IClientPlayerAuraHandler

    /** Client World Only */
    @SideOnly(Side.CLIENT)
    fun setClientPlayerAuraModelJson(json: String)

    /** Server World Only */
    fun getServerPlayerAuraHandler(player: EntityPlayerMP): IServerPlayerAuraHandler

    fun getGlobalFoodAura(itemStack: ItemStack): ManaSet?

    /** Server World Only */
    fun unloadAllServerPlayerAuraHandlers();
}

interface IPlayerAuraHandler {
    val playerAura: ManaSet
    fun getLocalFoodAura(itemStack: ItemStack): ManaSet?
    fun getSaturationRate(itemStack: ItemStack): Double
    fun simulatePlayerAura(itemStack: ItemStack, healAmount: Int): ManaSet?

    /** @return 新しい順 */
    val foodHistory: Iterable<IFoodHistoryEntry>
}

interface IFoodHistoryEntry {
    val food: ItemStack
    val baseLocalFoodAura: ManaSet
    val health: Double
}

interface IClientPlayerAuraHandler : IPlayerAuraHandler

interface IServerPlayerAuraHandler : IPlayerAuraHandler {
    fun load()
    fun save()
    fun onReset()
    fun onEat(itemStack: ItemStack, healAmount: Int)
    fun send()
}
