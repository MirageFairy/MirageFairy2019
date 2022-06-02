package miragefairy2019.mod.fairyweapon.deprecated;

public interface IMagicStatus<T> {

    public String getName();

    public IMagicStatusFunction<T> getFunction();

    public IMagicStatusFormatter<T> getFormatter();

}
