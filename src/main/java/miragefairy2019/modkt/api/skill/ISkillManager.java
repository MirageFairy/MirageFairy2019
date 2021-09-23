package miragefairy2019.modkt.api.skill;

import net.minecraft.entity.player.*;
import net.minecraftforge.fml.relauncher.*;

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
     * Server World Only
     */
    public void resetServer();

}
