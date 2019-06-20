package miragefairy2019.colormaker.core;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Optional;
import java.util.function.Supplier;

public class ColorConstants
{

	private Hashtable<String, Supplier<Color>> table = new Hashtable<>();

	public void addConstant(String colorName, Supplier<Color> sColor)
	{
		table.put(colorName, sColor);
	}

	public Color getColor(ColorIdentifier colorIdentifier)
	{
		return Optional.ofNullable(table.get(colorIdentifier.string))
			.map(Supplier::get)
			.orElseGet(() -> colorIdentifier.decode());
	}

}
