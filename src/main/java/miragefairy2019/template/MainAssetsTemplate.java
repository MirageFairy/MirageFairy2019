package miragefairy2019.template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import miragefairy2019.mod.lib.Utils;
import miragefairy2019.mod.modules.ore.EnumVariantMaterials1;

public class MainAssetsTemplate
{

	public static void main(String[] args) throws Exception
	{
		File base = new File("./src/main/resources/assets/miragefairy2019");

		String[] materialNames = {
			"apatite",
			"fluorite",
			"sulfur",
			"cinnabar",
			"moonstone",
			"magnetite",
		};
		int[] gemVariantIds = { 0, 1, 2, 6, 7, 8 };

		for (int i = 0; i < materialNames.length; i++) {
			String material = materialNames[i];

			put(new File(base, "recipes/" + material + ".json"), String.join("\r\n", new String[] {
				"{",
				"    \"type\": \"forge:ore_shaped\",",
				"    \"group\": \"" + material + "\",",
				"    \"pattern\": [",
				"        \"#\"",
				"    ],",
				"    \"key\": {",
				"        \"#\": {",
				"            \"type\": \"forge:ore_dict\",",
				"            \"ore\": \"block" + Utils.toUpperCaseHead(material) + "\"",
				"        }",
				"    },",
				"    \"result\": {",
				"        \"item\": \"miragefairy2019:materials\",",
				"        \"count\": 9,",
				"        \"data\": " + gemVariantIds[i],
				"    }",
				"}",
			}));

			put(new File(base, "recipes/" + material + "_block.json"), String.join("\r\n", new String[] {
				"{",
				"    \"type\": \"forge:ore_shaped\",",
				"    \"group\": \"" + material + "_block\",",
				"    \"pattern\": [",
				"        \"###\",",
				"        \"###\",",
				"        \"###\"",
				"    ],",
				"    \"key\": {",
				"        \"#\": {",
				"            \"type\": \"forge:ore_dict\",",
				"            \"ore\": \"gem" + Utils.toUpperCaseHead(material) + "\"",
				"        }",
				"    },",
				"    \"result\": {",
				"        \"item\": \"miragefairy2019:materials1\",",
				"        \"data\": " + EnumVariantMaterials1.valueOf(material.toUpperCase() + "_BLOCK").metadata,
				"    }",
				"}",
			}));

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
