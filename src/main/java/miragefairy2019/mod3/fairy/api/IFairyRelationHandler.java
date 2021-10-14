package miragefairy2019.mod3.fairy.api;

import javax.annotation.Nullable;

public interface IFairyRelationHandler<T> {

    /**
     * @return 関連しない場合、null。
     */
    @Nullable
    public FairyDropEntry getDrop(T t);

}
