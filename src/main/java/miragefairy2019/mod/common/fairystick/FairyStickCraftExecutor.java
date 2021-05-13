package miragefairy2019.mod.common.fairystick;

import java.util.ArrayList;
import java.util.List;

import miragefairy2019.mod.api.fairystick.IFairyStickCraftExecutor;

public class FairyStickCraftExecutor implements IFairyStickCraftExecutor
{

	private List<Runnable> listenersOnCraft = new ArrayList<>();

	@Override
	public void onUpdate()
	{
		listenersOnUpdate.forEach(Runnable::run);
	}

	@Override
	public void hookOnUpdate(Runnable listener)
	{
		listenersOnUpdate.add(listener);
	}

	//

	private List<Runnable> listenersOnUpdate = new ArrayList<>();

	@Override
	public void hookOnCraft(Runnable listener)
	{
		listenersOnCraft.add(listener);
	}

	@Override
	public void onCraft()
	{
		listenersOnCraft.forEach(Runnable::run);
	}

}
