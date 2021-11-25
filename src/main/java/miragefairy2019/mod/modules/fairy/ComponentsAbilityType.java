package miragefairy2019.mod.modules.fairy;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.fairy.IComponentAbilityType;
import miragefairy2019.mod3.erg.ErgKt;
import miragefairy2019.mod3.erg.api.EnumErgType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.HashMap;
import java.util.Map;

public class ComponentsAbilityType {

    private static Map<EnumErgType, IComponentAbilityType> fairyAbilityTypes = new HashMap<>();

    public static IComponentAbilityType getComponentAbilityType(EnumErgType abilityType) {
        return fairyAbilityTypes.computeIfAbsent(abilityType, k -> new ComponentAbilityType(abilityType));
    }

    private static class ComponentAbilityType implements IComponentAbilityType {

        private EnumErgType abilityType;

        private ComponentAbilityType(EnumErgType abilityType) {
            this.abilityType = abilityType;
        }

        @Override
        public ResourceLocation getName() {
            return new ResourceLocation(ModMirageFairy2019.MODID, "cuticle_" + abilityType.getName());
        }

        @Override
        public ITextComponent getDisplayName() {
            return new TextComponentTranslation("mirageFairy2019.component.cuticle.format", ErgKt.getDisplayName(abilityType));
        }

        @Override
        public EnumErgType getAbilityType() {
            return abilityType;
        }

    }

}
