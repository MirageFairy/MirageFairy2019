package miragefairy2019.mod.api.fairystick;

public interface IFairyStickCraftEventBus
{

	public void hookOnCraft(Runnable listener);

	public void hookOnUpdate(Runnable listener);

}
