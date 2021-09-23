package miragefairy2019.modkt.api.skill;

import net.minecraft.entity.player.*;

public interface ISkillContainer {

    /**
     * Server World Only
     */
    public void load(EntityPlayer player);

    /**
     * Server World Only
     */
    public void save(EntityPlayer player);

    /**
     * Server World Only
     */
    public void send(EntityPlayerMP player);

    //

    public int getMasteryLevel(IMastery mastery);

    public void setMasteryLevel(IMastery mastery, int masteryLevel);

}
