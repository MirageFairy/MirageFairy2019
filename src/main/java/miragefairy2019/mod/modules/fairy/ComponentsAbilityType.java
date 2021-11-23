package miragefairy2019.mod.modules.fairy;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.fairy.IComponentAbilityType;
import miragefairy2019.mod3.erg.ErgKt;
import miragefairy2019.mod3.erg.api.IErgType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.HashMap;
import java.util.Map;

public class ComponentsAbilityType {

    private static Map<IErgType, IComponentAbilityType> fairyAbilityTypes = new HashMap<>();

    public static IComponentAbilityType getComponentAbilityType(IErgType abilityType) {
        return fairyAbilityTypes.computeIfAbsent(abilityType, k -> new ComponentAbilityType(abilityType));
    }

    private static class ComponentAbilityType implements IComponentAbilityType {

        private IErgType abilityType;

        private ComponentAbilityType(IErgType abilityType) {
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
        public IErgType getAbilityType() {
            return abilityType;
        }

    }

}
