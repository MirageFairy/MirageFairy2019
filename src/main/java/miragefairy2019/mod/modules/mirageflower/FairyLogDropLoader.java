package miragefairy2019.mod.modules.mirageflower;

import miragefairy2019.mod.api.fairylogdrop.IFairyLogDropRegistry;
import miragefairy2019.mod.common.fairylogdrop.FairyLogDropConditionCanRain;
import miragefairy2019.mod.common.fairylogdrop.FairyLogDropConditionHasBiomeType;
import miragefairy2019.mod.common.fairylogdrop.FairyLogDropConditionOverworld;
import miragefairy2019.mod.common.fairylogdrop.FairyLogDropRecipe;
import miragefairy2019.mod.lib.Monad;
import miragefairy2019.mod.modules.fairy.VariantFairy;
import net.minecraftforge.common.BiomeDictionary;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static miragefairy2019.mod.modules.fairy.ModuleFairy.FairyTypes.*;
import static net.minecraftforge.common.BiomeDictionary.Type.*;

public class FairyLogDropLoader {

    private IFairyLogDropRegistry registry;

    public FairyLogDropLoader(IFairyLogDropRegistry registry) {
        this.registry = registry;
    }

    public void init() {

        // 時間帯
        mrfld(0.1, () -> daytime).peek(o());
        mrfld(0.1, () -> night).peek(o());
        mrfld(0.1, () -> morning).peek(o());

        // 天候
        mrfld(0.1, () -> fine).peek(o());
        mrfld(0.1, () -> rain).peek(o()).peek(r());
        mrfld(0.02, () -> thunder).peek(o()).peek(r());

        // バイオーム
        mrfld(1, () -> plains).peek(b(PLAINS));
        mrfld(1, () -> forest).peek(b(FOREST));
        mrfld(1, () -> taiga).peek(b(CONIFEROUS));
        mrfld(1, () -> desert).peek(b(SANDY));
        mrfld(1, () -> mountain).peek(b(MOUNTAIN));

    }

    private Monad<FairyLogDropRecipe> mrfld(double rate, Supplier<VariantFairy[]> sArrayVariantFairy) {
        return Monad.of(new FairyLogDropRecipe(rate, () -> sArrayVariantFairy.get()[0].createItemStack()))
                .peek(recipe -> registry.addRecipe(recipe));
    }

    private Consumer<FairyLogDropRecipe> b(BiomeDictionary.Type biome) {
        return recipe -> recipe.addCondition(new FairyLogDropConditionHasBiomeType(biome));
    }

    private Consumer<FairyLogDropRecipe> o() {
        return recipe -> recipe.addCondition(new FairyLogDropConditionOverworld());
    }

    private Consumer<FairyLogDropRecipe> r() {
        return recipe -> recipe.addCondition(new FairyLogDropConditionCanRain());
    }

}
