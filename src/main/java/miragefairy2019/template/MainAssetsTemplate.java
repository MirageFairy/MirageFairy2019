package miragefairy2019.template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainAssetsTemplate
{

	public static void main(String[] args) throws Exception
	{
		File base = new File("./src/main/resources/assets/miragefairy2019");

		for (String material : new String[] {
			"apatite",
			"fluorite",
			"sulfur",
			"cinnabar",
			"moonstone",
			"magnetite",
		}) {

			put(new File(base, "models/block/" + material + "_ore.json"), String.join("\r\n", new String[] {
				"{",
				"    \"parent\": \"miragefairy2019:block/overlay_block\",",
				"    \"textures\": {",
				"        \"particle\": \"blocks/stone\",",
				"        \"base\": \"blocks/stone\",",
				"        \"overlay\": \"miragefairy2019:blocks/" + material + "_ore\"",
				"    }",
				"}",
				"",
			}));
			put(new File(base, "models/block/" + material + "_block.json"), String.join("\r\n", new String[] {
				"{",
				"    \"parent\": \"miragefairy2019:block/overlay_block\",",
				"    \"textures\": {",
				"        \"particle\": \"blocks/stone\",",
				"        \"base\": \"blocks/stone\",",
				"        \"overlay\": \"miragefairy2019:blocks/" + material + "_block\"",
				"    }",
				"}",
				"",
			}));

			put(new File(base, "models/item/" + material + "_block.json"), String.join("\r\n", new String[] {
				"{",
				"    \"parent\": \"miragefairy2019:block/" + material + "_block\"",
				"}",
				"",
			}));
			put(new File(base, "models/item/" + material + "_ore.json"), String.join("\r\n", new String[] {
				"{",
				"    \"parent\": \"miragefairy2019:block/" + material + "_ore\"",
				"}",
				"",
			}));

		}
	}

	private static void put(File file, String content) throws IOException
	{
		try (OutputStream out = new FileOutputStream(file)) {
			out.write(content.getBytes("utf8"));
		}
	}

}
