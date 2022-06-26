package miragefairy2019.api;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nonnull;

public interface IMortarRecipe {

    /**
     * @return <ul>
     * <li>1: 石</li>
     * <li>2: 鉄</li>
     * <li>3: クォーツ</li>
     * <li>4: ダイヤモンド</li>
     * </ul>
     */
    public int getLevel();

    @Nonnull
    public Ingredient getInput();

    @Nonnull
    public ItemStack getOutput();

}
