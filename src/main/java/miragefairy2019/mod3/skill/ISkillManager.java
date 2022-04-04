package miragefairy2019.mod3.skill;

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

    public int getFairyMasterExp(int lv);

    public int getFairyMasterLevel(int exp);

}
