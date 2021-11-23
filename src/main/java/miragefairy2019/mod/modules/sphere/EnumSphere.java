package miragefairy2019.mod.modules.sphere;

import miragefairy2019.modkt.api.erg.ErgTypes;
import miragefairy2019.modkt.api.erg.IErgType;
import mirrg.boron.util.UtilsString;
import mirrg.boron.util.suppliterator.ISuppliterator;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class EnumSphere {

    private static List<EnumSphere> list = new ArrayList<>();
    private static Map<IErgType, EnumSphere> map = new HashMap<>();

    //

    public static final EnumSphere attack = new EnumSphere(ErgTypes.attack, 0xFFA0A0, 0xFF6B6B, 0xC70000, 0xFF0000, () -> Ingredient.fromStacks(new ItemStack(Items.IRON_SWORD)), () -> null);
    public static final EnumSphere craft = new EnumSphere(ErgTypes.craft, 0xF1B772, 0xD3FDCC, 0x92B56A, 0xFFFFFF, () -> new OreIngredient("workbench"), () -> new OreIngredient("gemNephrite"));
    public static final EnumSphere harvest = new EnumSphere(ErgTypes.harvest, 0x00BD00, 0xD09D74, 0x6E4219, 0x2FFF2F, () -> Ingredient.fromStacks(new ItemStack(Items.IRON_AXE)), () -> null);
    public static final EnumSphere light = new EnumSphere(ErgTypes.light, 0xFF8300, 0xFFC9BC, 0xF1C483, 0xFFFF25, () -> new OreIngredient("torch"), () -> new OreIngredient("gemTopaz"));
    public static final EnumSphere flame = new EnumSphere(ErgTypes.flame, 0xFF9F68, 0xFF6800, 0xE2713F, 0xBF1805, () -> Ingredient.fromStacks(new ItemStack(Items.FIRE_CHARGE)), () -> new OreIngredient("gemHeliolite"));
    public static final EnumSphere water = new EnumSphere(ErgTypes.water, 0x67E6FF, 0xBDF0FF, 0x00ABFF, 0x83B5FF, () -> Ingredient.fromStacks(new ItemStack(Items.WATER_BUCKET)), () -> null);
    public static final EnumSphere crystal = new EnumSphere(ErgTypes.crystal, 0xA2FFFF, 0xB6FFFF, 0x36CECE, 0xEBFFFF, () -> new OreIngredient("gemDiamond"), () -> null);
    public static final EnumSphere sound = new EnumSphere(ErgTypes.sound, 0x98ACE7, 0xD8DDFF, 0xBFC9D8, 0xC9D0ED, () -> Ingredient.fromStacks(new ItemStack(Blocks.NOTEBLOCK)), () -> null);
    public static final EnumSphere space = new EnumSphere(ErgTypes.space, 0x000000, 0x4D0065, 0x67009D, 0x001E74, () -> Ingredient.fromStacks(new ItemStack(Blocks.CHEST)), () -> null);
    public static final EnumSphere warp = new EnumSphere(ErgTypes.warp, 0x3A00D3, 0x8CF4E2, 0x349988, 0xD004FB, () -> new OreIngredient("enderpearl"), () -> null);
    public static final EnumSphere shoot = new EnumSphere(ErgTypes.shoot, 0x969696, 0x896727, 0x896727, 0xD8D8D8, () -> Ingredient.fromStacks(new ItemStack(Items.BOW)), () -> null);
    public static final EnumSphere destroy = new EnumSphere(ErgTypes.destroy, 0xFFFFFF, 0xFF5A35, 0xFF4800, 0x000000, () -> Ingredient.fromStacks(new ItemStack(Blocks.TNT)), () -> null);
    public static final EnumSphere chemical = new EnumSphere(ErgTypes.chemical, 0x0067FF, 0xC9DFEF, 0xB0C4D7, 0x0755FF, () -> Ingredient.fromStacks(new ItemStack(Items.FERMENTED_SPIDER_EYE)), () -> null);
    public static final EnumSphere slash = new EnumSphere(ErgTypes.slash, 0xAAAAAA, 0xFFC9B2, 0xD20000, 0xFFFFFF, () -> Ingredient.fromStacks(new ItemStack(Items.SHEARS)), () -> null);
    public static final EnumSphere life = new EnumSphere(ErgTypes.life, 0xFF0033, 0xFFC9DE, 0xFF8EB2, 0xFF3F67, () -> Ingredient.fromStacks(new ItemStack(Items.BEEF)), () -> new OreIngredient("gemPyrope"));
    public static final EnumSphere knowledge = new EnumSphere(ErgTypes.knowledge, 0xFFFF00, 0x006200, 0x00A000, 0x50DD00, () -> Ingredient.fromStacks(new ItemStack(Items.BOOK)), () -> null);
    public static final EnumSphere energy = new EnumSphere(ErgTypes.energy, 0xFFED30, 0xFFF472, 0xFFE84C, 0xBFE7FF, () -> new OreIngredient("gemCoal"), () -> null);
    public static final EnumSphere submission = new EnumSphere(ErgTypes.submission, 0xFF0000, 0x593232, 0x1E1E1E, 0xA90000, () -> Ingredient.fromStacks(new ItemStack(Blocks.IRON_BARS)), () -> null);
    public static final EnumSphere christmas = new EnumSphere(ErgTypes.christmas, 0xFF0000, 0xFFD723, 0x00B900, 0xFF0000, () -> Ingredient.fromStacks(new ItemStack(Blocks.SAPLING, 1, 1)), () -> null);
    public static final EnumSphere freeze = new EnumSphere(ErgTypes.freeze, 0x5AFFFF, 0xFFFFFF, 0xF6FFFF, 0xACFFFF, () -> new OreIngredient("ice"), () -> null);
    public static final EnumSphere thunder = new EnumSphere(ErgTypes.thunder, 0xFFFFB2, 0x359C00, 0xC370A7, 0xFFFF00, () -> Ingredient.fromStacks(new ItemStack(Items.GOLDEN_SWORD)), () -> new OreIngredient("gemTourmaline"));
    public static final EnumSphere levitate = new EnumSphere(ErgTypes.levitate, 0x00A2FF, 0xB7ECFF, 0x35366B, 0x8CD0FF, () -> Ingredient.fromStacks(new ItemStack(Items.FEATHER)), () -> new OreIngredient("gemLabradorite"));

    //

    public final IErgType abilityType;
    public final int colorCore;
    public final int colorHighlight;
    public final int colorBackground;
    public final int colorPlasma;
    public final Supplier<Ingredient> sIngredientWithFluorite;
    public final Supplier<Ingredient> sIngredientGem;

    private EnumSphere(
            IErgType abilityType,
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

    public static Optional<EnumSphere> of(IErgType abilityType) {
        return Optional.ofNullable(map.get(abilityType));
    }

}
