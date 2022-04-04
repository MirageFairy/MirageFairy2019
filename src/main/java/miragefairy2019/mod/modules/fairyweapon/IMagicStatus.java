package miragefairy2019.mod.modules.fairyweapon;

public interface IMagicStatus<T> {

    public String getName();

    public IMagicStatusFunction<T> getFunction();

    public IMagicStatusFormatter<T> getFormatter();

}
