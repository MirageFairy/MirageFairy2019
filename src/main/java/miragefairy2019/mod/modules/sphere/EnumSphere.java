package miragefairy2019.mod.modules.sphere;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import miragefairy2019.mod.api.fairy.AbilityTypes;
import miragefairy2019.mod.api.fairy.IAbilityType;
import mirrg.boron.util.UtilsString;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;

public class EnumSphere {

    private static List<EnumSphere> list = new ArrayList<>();
    private static Map<IAbilityType, EnumSphere> map = new HashMap<>();

    //

    public static final EnumSphere attack = new EnumSphere(AbilityTypes.attack.get(), 0xFFA0A0, 0xFF6B6B, 0xC70000, 0xFF0000, () -> Ingredient.fromStacks(new ItemStack(Items.IRON_SWORD)), () -> null);
    public static final EnumSphere craft = new EnumSphere(AbilityTypes.craft.get(), 0xF1B772, 0xD3FDCC, 0x92B56A, 0xFFFFFF, () -> new OreIngredient("workbench"), () -> new OreIngredient("gemNephrite"));
    public static final EnumSphere fell = new EnumSphere(AbilityTypes.fell.get(), 0x00BD00, 0xD09D74, 0x6E4219, 0x2FFF2F, () -> Ingredient.fromStacks(new ItemStack(Items.IRON_AXE)), () -> null);
    public static final EnumSphere light = new EnumSphere(AbilityTypes.light.get(), 0xFF8300, 0xFFC9BC, 0xF1C483, 0xFFFF25, () -> new OreIngredient("torch"), () -> new OreIngredient("gemTopaz"));
    public static final EnumSphere flame = new EnumSphere(AbilityTypes.flame.get(), 0xFF3600, 0xFF9900, 0xCA5B25, 0xFF0000, () -> Ingredient.fromStacks(new ItemStack(Items.FLINT_AND_STEEL)), () -> null);
    public static final EnumSphere water = new EnumSphere(AbilityTypes.water.get(), 0x67E6FF, 0xBDF0FF, 0x00ABFF, 0x83B5FF, () -> Ingredient.fromStacks(new ItemStack(Items.WATER_BUCKET)), () -> null);
    public static final EnumSphere crystal = new EnumSphere(AbilityTypes.crystal.get(), 0xA2FFFF, 0xB6FFFF, 0x36CECE, 0xEBFFFF, () -> new OreIngredient("gemDiamond"), () -> null);
    public static final EnumSphere art = new EnumSphere(AbilityTypes.art.get(), 0xFF5353, 0x41C6FF, 0xFFFF84, 0x00C800, () -> Ingredient.fromStacks(new ItemStack(Items.PAINTING)), () -> null);
    public static final EnumSphere store = new EnumSphere(AbilityTypes.store.get(), 0xDCDCDC, 0xEBA242, 0xC47F25, 0x404040, () -> Ingredient.fromStacks(new ItemStack(Blocks.CHEST)), () -> null);
    public static final EnumSphere warp = new EnumSphere(AbilityTypes.warp.get(), 0x3A00D3, 0x8CF4E2, 0x349988, 0xD004FB, () -> new OreIngredient("enderpearl"), () -> null);
    public static final EnumSphere shoot = new EnumSphere(AbilityTypes.shoot.get(), 0x969696, 0x896727, 0x896727, 0xD8D8D8, () -> Ingredient.fromStacks(new ItemStack(Items.BOW)), () -> null);
    public static final EnumSphere breaking = new EnumSphere(AbilityTypes.breaking.get(), 0xFFFFFF, 0xFF5A35, 0xFF4800, 0x000000, () -> Ingredient.fromStacks(new ItemStack(Blocks.TNT)), () -> null);
    public static final EnumSphere chemical = new EnumSphere(AbilityTypes.chemical.get(), 0x0067FF, 0xC9DFEF, 0xB0C4D7, 0x0755FF, () -> Ingredient.fromStacks(new ItemStack(Items.FERMENTED_SPIDER_EYE)), () -> null);
    public static final EnumSphere slash = new EnumSphere(AbilityTypes.slash.get(), 0xAAAAAA, 0xFFC9B2, 0xD20000, 0xFFFFFF, () -> Ingredient.fromStacks(new ItemStack(Items.SHEARS)), () -> null);
    public static final EnumSphere food = new EnumSphere(AbilityTypes.food.get(), 0xC66000, 0xFFCF86, 0xCB6E00, 0xFFC261, () -> Ingredient.fromStacks(new ItemStack(Items.BREAD)), () -> null);
    public static final EnumSphere knowledge = new EnumSphere(AbilityTypes.knowledge.get(), 0xFFFF00, 0x006200, 0x00A000, 0x50DD00, () -> Ingredient.fromStacks(new ItemStack(Items.BOOK)), () -> null);
    public static final EnumSphere energy = new EnumSphere(AbilityTypes.energy.get(), 0xFFED30, 0xFFF472, 0xFFE84C, 0xBFE7FF, () -> new OreIngredient("gemCoal"), () -> null);
    public static final EnumSphere submission = new EnumSphere(AbilityTypes.submission.get(), 0xFF0000, 0x593232, 0x1E1E1E, 0xA90000, () -> Ingredient.fromStacks(new ItemStack(Blocks.IRON_BARS)), () -> null);
    public static final EnumSphere christmas = new EnumSphere(AbilityTypes.christmas.get(), 0xFF0000, 0xFFD723, 0x00B900, 0xFF0000, () -> Ingredient.fromStacks(new ItemStack(Blocks.SAPLING, 1, 1)), () -> null);
    public static final EnumSphere freeze = new EnumSphere(AbilityTypes.freeze.get(), 0x5AFFFF, 0xFFFFFF, 0xF6FFFF, 0xACFFFF, () -> new OreIngredient("ice"), () -> null);
    public static final EnumSphere thunder = new EnumSphere(AbilityTypes.thunder.get(), 0xFFFFB2, 0x359C00, 0xC370A7, 0xFFFF00, () -> Ingredient.fromStacks(new ItemStack(Items.GOLDEN_SWORD)), () -> new OreIngredient("gemTourmaline"));

    //

    public final IAbilityType abilityType;
    public final int colorCore;
    public final int colorHighlight;
    public final int colorBackground;
    public final int colorPlasma;
    public final Supplier<Ingredient> sIngredientWithFluorite;
    public final Supplier<Ingredient> sIngredientGem;

    private EnumSphere(
            IAbilityType abilityType,
            int colorCore,
            int colorHighlight,
            int colorBackground,
            int colorPlasma,
            Supplier<Ingredient> sIngredientWithFluorite,
            Supplier<Ingredient> sIngredientGem) {
        this.abilityType = abilityType;
        this.colorCore = colorCore;
        this.colorHighlight = colorHighlight;
        this.colorBackground = colorBackground;
        this.colorPlasma = colorPlasma;
        this.sIngredientWithFluorite = sIngredientWithFluorite;
        this.sIngredientGem = sIngredientGem;

        list.add(this);
        map.put(abilityType, this);
    }

    public String getOreName() {
        return "mirageFairy2019Sphere" + UtilsString.toUpperCaseHead(abilityType.getName());
    }

    public static ISuppliterator<EnumSphere> values() {
        return ISuppliterator.ofIterable(list);
    }

    public static Optional<EnumSphere> of(IAbilityType abilityType) {
        return Optional.ofNullable(map.get(abilityType));
    }

}
