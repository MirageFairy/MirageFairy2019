package miragefairy2019.mod.modules.composite;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.composite.IComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class Component implements IComponent, Comparable<IComponent>
{

	public static final Component apatite = new Component("apatite");
	public static final Component fluorite = new Component("fluorite");
	public static final Component sulfur = new Component("sulfur");
	public static final Component cinnabar = new Component("cinnabar");
	public static final Component moonstone = new Component("moonstone");
	public static final Component magnetite = new Component("magnetite");
	public static final Component miragium = new Component("miragium");
	public static final Component iron = new Component("iron");
	public static final Component gold = new Component("gold");
	public static final Component wood = new Component("wood");
	public static final Component stone = new Component("stone");
	public static final Component obsidian = new Component("obsidian");
	public static final Component emerald = new Component("emerald");
	public static final Component pyrope = new Component("pyrope");
	public static final Component smithsonite = new Component("smithsonite");
	public static final Component saltpeter = new Component("saltpeter");
	public static final Component nephrite = new Component("nephrite");
	public static final Component topaz = new Component("topaz");
	public static final Component tourmaline = new Component("tourmaline");
	public static final Component quartz = new Component("quartz");

	private String name;

	public Component(String name)
	{
		this.name = name;
	}

	@Override
	public ResourceLocation getName()
	{
		return new ResourceLocation(ModMirageFairy2019.MODID, name);
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentTranslation("mirageFairy2019.component." + name + ".name");
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + "[" + name + "]";
	}

}
