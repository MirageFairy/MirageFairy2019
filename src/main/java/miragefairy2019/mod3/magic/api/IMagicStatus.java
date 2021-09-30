package miragefairy2019.mod3.magic.api;

public interface IMagicStatus<T> {

    public String getName();

    public IMagicStatusFunction<T> getFunction();

    public IMagicStatusFormatter<T> getFormatter();

}
