package gregtech.api.material;

import crafttweaker.annotations.ZenRegister;
import gregtech.apiOld.unification.Element;
import gregtech.core.material.internal.MaterialIconSet;
import stanhebben.zenscript.annotations.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

@ZenClass("mods.gregtech.material.Material")
@ZenRegister
public interface IMaterial extends Comparable<IMaterial> {

    // BASIC INFO

    int getId();

    @ZenGetter("name")
    String getName();

    @ZenGetter("camelCaseName")
    String toCamelCaseString();

    @ZenGetter("unlocalizedName")
    String getUnlocalizedName();

    @ZenGetter("localizedName")
    String getLocalizedName();


    // CHEMICAL FORMULA

    @ZenGetter
    String getChemicalFormula();

    @ZenMethod
    IMaterial setFormula(String formula, boolean withFormatting);

    @ZenMethod
    default IMaterial setFormula(String formula) {
        return setFormula(formula, false);
    }


    // MATERIAL COMPONENTS

    Collection<MaterialStack> getMaterialComponents();


    // MATERIAL FLAGS

    @Nonnull
    Collection<IMaterialFlag> getFlags();

    void addFlags(IMaterialFlag... flags);

    @ZenMethod
    void addFlags(String... names);

    boolean hasFlag(IMaterialFlag flag);

    boolean hasFlags(IMaterialFlag... flags);

    boolean hasAnyOfFlags(IMaterialFlag... flags);


    // MATERIAL PROPERTIES

    @Nonnull
    Collection<IMaterialProperty> getProperties();

    <T extends IMaterialProperty> boolean hasProperty(PropertyKey<T> key);

    <T extends IMaterialProperty> T getProperty(PropertyKey<T> key);

    <T extends IMaterialProperty> void setProperty(PropertyKey<T> key, IMaterialProperty property);

    <T extends IMaterialProperty> void ensureSet(PropertyKey<T> key, boolean verify);

    default <T extends IMaterialProperty> void ensureSet(PropertyKey<T> key) {
        ensureSet(key, false);
    }


    // HARVEST LEVEL

    int getToolHarvestLevel();

    int getBlockHarvestLevel();


    // MATERIAL RGB

    @ZenMethod
    void setMaterialRGB(int materialRGB);

    @ZenGetter("materialRGB")
    int getMaterialRGB();

    @ZenGetter("hasFluidColor")
    boolean hasFluidColor();


    // ICON SET

    void setMaterialIconSet(MaterialIconSet materialIconSet);

    MaterialIconSet getMaterialIconSet();


    // ELEMENTAL DATA

    @ZenGetter("element")
    @Nullable
    Element getElement();

    @ZenGetter("radioactive")
    boolean isRadioactive();

    @ZenGetter("protons")
    long getProtons();

    @ZenGetter("neutrons")
    long getNeutrons();

    @ZenGetter("mass")
    long getMass();


    // CRAFTTWEAKER HELPERS

    @ZenOperator(OperatorType.MUL)
    default MaterialStack createMaterialStack(long amount) {
        return new MaterialStack(this, amount);
    }

    @ZenGetter("components")
    default MaterialStack[] getMaterialComponentsCt() {
        return getMaterialComponents().toArray(new MaterialStack[0]);
    }

    @Override
    @ZenMethod
    default int compareTo(IMaterial m) {
        return toString().compareTo(m.toString());
    }
}
