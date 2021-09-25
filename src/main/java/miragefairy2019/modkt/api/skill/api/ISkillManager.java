package miragefairy2019.modkt.api.skill.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ISkillManager {

    /**
     * Client World Only
     */
    @SideOnly(Side.CLIENT)
    public ISkillContainer getClientSkillContainer();

    /**
     * Server World Only
     */
    public ISkillContainer getServerSkillContainer(EntityPlayer player);

    /**
     * Client World Only
     */
    public void receive(String json);

    /**
     * Server World Only
     */
    public void resetServer();

}
