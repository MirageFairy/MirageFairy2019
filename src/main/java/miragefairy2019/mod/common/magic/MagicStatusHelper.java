package miragefairy2019.mod.common.magic;

import static net.minecraft.util.text.TextFormatting.*;

import java.util.function.Function;
import java.util.function.Supplier;

import miragefairy2019.mod.api.magic.IMagicFactorProvider;
import miragefairy2019.mod.api.magic.IMagicStatus;
import mirrg.boron.util.UtilsMath;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public final class MagicStatusHelper {

    private MagicStatusHelper() {

    }

    //

    public static Function<Integer, ITextComponent> getFormatterInteger() {
        return value -> new TextComponentString(String.format("%d", value));
    }

    public static Function<Double, ITextComponent> getFormatterTick() {
        return value -> new TextComponentString(String.format("%.0ft = %.2fs", value, value / 20.0));
    }

    public static Function<Double, ITextComponent> getFormatterDouble0() {
        return value -> new TextComponentString(String.format("%.0f", value));
    }

    public static Function<Double, ITextComponent> getFormatterDouble1() {
        return value -> new TextComponentString(String.format("%.1f", value));
    }

    public static Function<Double, ITextComponent> getFormatterDouble2() {
        return value -> new TextComponentString(String.format("%.2f", value));
    }

    public static Function<Double, ITextComponent> getFormatterDouble3() {
        return value -> new TextComponentString(String.format("%.3f", value));
    }

    public static Function<Double, ITextComponent> getFormatterPercentage0() {
        return value -> new TextComponentString(String.format("%.0f%%", value * 100));
    }

    public static Function<Double, ITextComponent> getFormatterPercentage1() {
        return value -> new TextComponentString(String.format("%.1f%%", value * 100));
    }

    public static Function<Double, ITextComponent> getFormatterPercentage2() {
        return value -> new TextComponentString(String.format("%.2f%%", value * 100));
    }

    public static Function<Double, ITextComponent> getFormatterPercentage3() {
        return value -> new TextComponentString(String.format("%.3f%%", value * 100));
    }

    public static Function<Boolean, ITextComponent> getFormatterBoolean() {
        return value -> new TextComponentString("")
                .appendSibling(new TextComponentString(value ? "Yes" : "No").setStyle(new Style().setColor(value ? GREEN : RED)));
    }

    //

    public static <T> IMagicStatusRangeProvider<T> getRangeProvider() {
        return new IMagicStatusRangeProvider<T>() {
            @Override
            public T trim(T value) {
                return value;
            }

            @Override
            public ITextComponent decorate(T value, Function<T, ITextComponent> formatter) {
                return formatter.apply(value);
            }
        };
    }

    public static IMagicStatusRangeProvider<Integer> getRangeProvider(int min, int max) {
        return new IMagicStatusRangeProvider<Integer>() {
            @Override
            public Integer trim(Integer value) {
                return UtilsMath.trim(value, min, max);
            }

            @Override
            public ITextComponent decorate(Integer value, Function<Integer, ITextComponent> formatter) {
                boolean bold;
                TextFormatting color;
                if (value == max) {
                    bold = true;
                    color = GREEN;
                } else if (value > min) {
                    bold = false;
                    color = GREEN;
                } else {
                    bold = false;
                    color = WHITE;
                }
                return new TextComponentString("")
                        .appendSibling(formatter.apply(value)
                                .setStyle(new Style().setBold(bold).setColor(color)));
            }
        };
    }

    public static IMagicStatusRangeProvider<Integer> getRangeProviderInvert(int min, int max) {
        return new IMagicStatusRangeProvider<Integer>() {
            @Override
            public Integer trim(Integer value) {
                return UtilsMath.trim(value, min, max);
            }

            @Override
            public ITextComponent decorate(Integer value, Function<Integer, ITextComponent> formatter) {
                boolean bold;
                TextFormatting color;
                if (value == min) {
                    bold = true;
                    color = GREEN;
                } else if (value < max) {
                    bold = false;
                    color = GREEN;
                } else {
                    bold = false;
                    color = WHITE;
                }
                return new TextComponentString("")
                        .appendSibling(formatter.apply(value)
                                .setStyle(new Style().setBold(bold).setColor(color)));
            }
        };
    }

    public static IMagicStatusRangeProvider<Integer> getRangeProvider(int min, int def, int max) {
        return new IMagicStatusRangeProvider<Integer>() {
            @Override
            public Integer trim(Integer value) {
                return UtilsMath.trim(value, min, max);
            }

            @Override
            public ITextComponent decorate(Integer value, Function<Integer, ITextComponent> formatter) {
                boolean bold;
                TextFormatting color;
                if (value == max) {
                    bold = true;
                    color = GREEN;
                } else if (value > def) {
                    bold = false;
                    color = GREEN;
                } else if (value == min) {
                    bold = true;
                    color = RED;
                } else if (value < def) {
                    bold = false;
                    color = RED;
                } else {
                    bold = false;
                    color = WHITE;
                }
                return new TextComponentString("")
                        .appendSibling(formatter.apply(value)
                                .setStyle(new Style().setBold(bold).setColor(color)));
            }
        };
    }

    public static IMagicStatusRangeProvider<Double> getRangeProvider(double min, double max) {
        return new IMagicStatusRangeProvider<Double>() {
            @Override
            public Double trim(Double value) {
                return UtilsMath.trim(value, min, max);
            }

            @Override
            public ITextComponent decorate(Double value, Function<Double, ITextComponent> formatter) {
                boolean bold;
                TextFormatting color;
                if (value == max) {
                    bold = true;
                    color = GREEN;
                } else if (value > min) {
                    bold = false;
                    color = GREEN;
                } else {
                    bold = false;
                    color = WHITE;
                }
                return new TextComponentString("")
                        .appendSibling(formatter.apply(value)
                                .setStyle(new Style().setBold(bold).setColor(color)));
            }
        };
    }

    public static IMagicStatusRangeProvider<Double> getRangeProviderInvert(double min, double max) {
        return new IMagicStatusRangeProvider<Double>() {
            @Override
            public Double trim(Double value) {
                return UtilsMath.trim(value, min, max);
            }

            @Override
            public ITextComponent decorate(Double value, Function<Double, ITextComponent> formatter) {
                boolean bold;
                TextFormatting color;
                if (value == min) {
                    bold = true;
                    color = GREEN;
                } else if (value < max) {
                    bold = false;
                    color = GREEN;
                } else {
                    bold = false;
                    color = WHITE;
                }
                return new TextComponentString("")
                        .appendSibling(formatter.apply(value)
                                .setStyle(new Style().setBold(bold).setColor(color)));
            }
        };
    }

    public static IMagicStatusRangeProvider<Double> getRangeProvider(double min, double def, double max) {
        return new IMagicStatusRangeProvider<Double>() {
            @Override
            public Double trim(Double value) {
                return UtilsMath.trim(value, min, max);
            }

            @Override
            public ITextComponent decorate(Double value, Function<Double, ITextComponent> formatter) {
                boolean bold;
                TextFormatting color;
                if (value == max) {
                    bold = true;
                    color = GREEN;
                } else if (value > def) {
                    bold = false;
                    color = GREEN;
                } else if (value == min) {
                    bold = true;
                    color = RED;
                } else if (value < def) {
                    bold = false;
                    color = RED;
                } else {
                    bold = false;
                    color = WHITE;
                }
                return new TextComponentString("")
                        .appendSibling(formatter.apply(value)
                                .setStyle(new Style().setBold(bold).setColor(color)));
            }
        };
    }

    //

    public static IMagicStatus<Integer> getMagicStatusMaxTargetCount(Supplier<Integer> getter, Function<IMagicFactorProvider, ITextComponent> getterFormula, int min, int max) {
        return new MagicStatusRanged<>("maxTargetCount", getter, getterFormula, getFormatterInteger(), getRangeProvider(min, max));
    }

    public static IMagicStatus<Double> getMagicStatusWear(Supplier<Double> getter, Function<IMagicFactorProvider, ITextComponent> getterFormula, double min, double max) {
        return new MagicStatusRanged<>("wear", getter, getterFormula, getFormatterPercentage2(), getRangeProviderInvert(min, max));
    }

    public static IMagicStatus<Double> getMagicStatusCoolTime(Supplier<Double> getter, Function<IMagicFactorProvider, ITextComponent> getterFormula, double min, double max) {
        return new MagicStatusRanged<>("coolTime", getter, getterFormula, getFormatterTick(), getRangeProviderInvert(min, max));
    }

    public static IMagicStatus<Double> getMagicStatusFortune(Supplier<Double> getter, Function<IMagicFactorProvider, ITextComponent> getterFormula, double min, double max) {
        return new MagicStatusRanged<>("fortune", getter, getterFormula, getFormatterDouble2(), getRangeProvider(min, max));
    }

    public static IMagicStatus<Double> getMagicStatusAdditionalReach(Supplier<Double> getter, Function<IMagicFactorProvider, ITextComponent> getterFormula, double min, double max) {
        return new MagicStatusRanged<>("additionalReach", getter, getterFormula, getFormatterDouble2(), getRangeProvider(min, max));
    }

    public static IMagicStatus<Double> getMagicStatusRadius(Supplier<Double> getter, Function<IMagicFactorProvider, ITextComponent> getterFormula, double min, double max) {
        return new MagicStatusRanged<>("radius", getter, getterFormula, getFormatterDouble2(), getRangeProvider(min, max));
    }

    public static IMagicStatus<Double> getMagicStatusPitch(Supplier<Double> getter, Function<IMagicFactorProvider, ITextComponent> getterFormula, double min, double def, double max) {
        return new MagicStatusRanged<>("pitch", getter, getterFormula, getFormatterDouble2(), getRangeProvider(min, def, max));
    }

    public static IMagicStatus<Boolean> getMagicStatusCollection(Supplier<Boolean> getter, Function<IMagicFactorProvider, ITextComponent> getterFormula) {
        return new MagicStatusRanged<>("collection", getter, getterFormula, getFormatterBoolean(), getRangeProvider());
    }

}
