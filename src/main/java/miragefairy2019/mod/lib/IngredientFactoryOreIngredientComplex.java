package miragefairy2019.mod.lib;

import com.google.gson.JsonObject;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;

/**
 * 耐久が削れたクラフティングツールを鉱石辞書名にマッチさせるためのIngredient
 */
public class IngredientFactoryOreIngredientComplex implements IIngredientFactory {

    @Override
    public Ingredient parse(JsonContext context, JsonObject json) {
        return new OreIngredientComplex(JsonUtils.getString(json, "ore"));
    }

}
