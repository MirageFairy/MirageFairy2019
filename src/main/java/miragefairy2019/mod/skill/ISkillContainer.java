package miragefairy2019.mod.skill;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public interface ISkillContainer {

    public ISkillManager getSkillManager();

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

    public Iterable<String> getMasteryList();

    public int getMasteryLevel(String mastery);

    public void setMasteryLevel(String mastery, int masteryLevel);

    public ISkillVariables getVariables();

}
