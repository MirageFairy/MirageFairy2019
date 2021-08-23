package miragefairy2019.mod.modules.fairy;

import miragefairy2019.mod.ModMirageFairy2019;
import miragefairy2019.mod.api.fairy.IComponentAbilityType;
import miragefairy2019.modkt.api.fairy.IAbilityType;
import miragefairy2019.modkt.impl.fairy.AbilityTypeKt;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.HashMap;
import java.util.Map;

public class ComponentsAbilityType {

    private static Map<IAbilityType, IComponentAbilityType> fairyAbilityTypes = new HashMap<>();

    public static IComponentAbilityType getComponentAbilityType(IAbilityType abilityType) {
        return fairyAbilityTypes.computeIfAbsent(abilityType, k -> new ComponentAbilityType(abilityType));
    }

    private static class ComponentAbilityType implements IComponentAbilityType {

        private IAbilityType abilityType;

        private ComponentAbilityType(IAbilityType abilityType) {
            this.abilityType = abilityType;
        }

        @Override
        public ResourceLocation getName() {
            return new ResourceLocation(ModMirageFairy2019.MODID, "cuticle_" + abilityType.getName());
        }

        @Override
        public ITextComponent getDisplayName() {
            return new TextComponentTranslation("mirageFairy2019.component.cuticle.format", AbilityTypeKt.getDisplayName(abilityType));
        }

        @Override
        public IAbilityType getAbilityType() {
            return abilityType;
        }

    }

}
