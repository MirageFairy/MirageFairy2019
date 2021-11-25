package miragefairy2019.mod3.fairy.relation.api;

import net.minecraft.entity.Entity;

import java.util.List;

public interface IFairyRelationManager {

    public List<FairyRelationEntry<Class<? extends Entity>>> getEntityClassRelationRegistry();

    public List<IFairyRelationHandler<Entity>> getEntityRelationRegistry();

}
