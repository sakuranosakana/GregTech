package gregtech.core.material.internal;

import gregtech.api.material.*;
import gregtech.apiOld.GregTechAPI;
import gregtech.apiOld.unification.Element;
import gregtech.apiOld.unification.Elements;
import gregtech.core.material.MaterialFlags;
import gregtech.core.material.MaterialIconSets;
import gregtech.core.material.MaterialProperties;
import gregtech.core.material.properties.*;
import gregtech.apiOld.util.GTLog;
import gregtech.apiOld.util.GTUtility;
import gregtech.apiOld.util.LocalizationUtils;
import gregtech.apiOld.util.SmallDigits;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class Material implements IMaterial {

    /**
     * Basic Info of this Material.
     *
     * @see MaterialInfo
     */
    @Nonnull
    private final MaterialInfo materialInfo;

    /**
     * Properties of this Material.
     *
     * @see MaterialPropertyStorage
     */
    @Nonnull
    private final MaterialPropertyStorage properties;

    /**
     * Generation flags of this material
     *
     * @see MaterialFlags
     */
    @Nonnull
    private final MaterialFlagStorage flags;

    /**
     * Chemical formula of this material
     */
    private String chemicalFormula;

    public Material(@Nonnull MaterialInfo materialInfo, @Nonnull MaterialPropertyStorage properties, @Nonnull MaterialFlagStorage flags) {
        this.materialInfo = materialInfo;
        this.properties = properties;
        this.flags = flags;
        this.properties.setMaterial(this);
        registerMaterial();
    }

    // thou shall not call
    protected Material(String name) {
        materialInfo = new MaterialInfo(0, name);
        materialInfo.iconSet = MaterialIconSets.NONE;
        properties = new MaterialPropertyStorage();
        flags = new MaterialFlagStorage();
    }


    // BASIC INFO

    @Override
    public int getId() {
        return materialInfo.metaItemSubId;
    }

    @Override
    public String getName() {
        return materialInfo.name;
    }

    @Override
    public String toCamelCaseString() {
        return GTUtility.lowerUnderscoreToUpperCamel(toString());
    }

    @Override
    public String getUnlocalizedName() {
        return "material." + materialInfo.name;
    }

    @Override
    public String getLocalizedName() {
        return LocalizationUtils.format(getUnlocalizedName());
    }


    // CHEMICAL FORMULA

    @Override
    public String getChemicalFormula() {
        return chemicalFormula;
    }

    @Override
    public Material setFormula(String formula, boolean withFormatting) {
        this.chemicalFormula = withFormatting ? SmallDigits.toSmallDownNumbers(formula) : formula;
        return this;
    }


    // MATERIAL COMPONENTS
    @Override
    public Collection<MaterialStack> getMaterialComponents() {
        return materialInfo.componentList;
    }


    // MATERIAL FLAGS

    @Nonnull
    @Override
    public Collection<IMaterialFlag> getFlags() {
        return flags.getFlags();
    }

    @Override
    public void addFlags(IMaterialFlag... flags) {
        if (GregTechAPI.MATERIAL_REGISTRY.isFrozen())
            throw new IllegalStateException("Cannot add flag to material when registry is frozen!");
        this.flags.addFlags(flags).verify(this);
    }

    @Override
    public void addFlags(String... names) {
        addFlags(Arrays.stream(names)
                .map(MaterialFlag::getByName)
                .filter(Objects::nonNull)
                .toArray(IMaterialFlag[]::new));
    }

    @Override
    public boolean hasFlag(IMaterialFlag flag) {
        return flags.hasFlag(flag);
    }

    @Override
    public boolean hasFlags(IMaterialFlag... flags) {
        return Arrays.stream(flags).allMatch(this::hasFlag);
    }

    @Override
    public boolean hasAnyOfFlags(IMaterialFlag... flags) {
        return Arrays.stream(flags).anyMatch(this::hasFlag);
    }


    // MATERIAL PROPERTIES

    @Nonnull
    @Override
    public Collection<IMaterialProperty> getProperties() {
        return properties.getProperties();
    }

    @Override
    public <T extends IMaterialProperty> boolean hasProperty(PropertyKey<T> key) {
        return getProperty(key) != null;
    }

    @Override
    public <T extends IMaterialProperty> T getProperty(PropertyKey<T> key) {
        return properties.getProperty(key);
    }

    @Override
    public <T extends IMaterialProperty> void setProperty(PropertyKey<T> key, IMaterialProperty property) {
        if (GregTechAPI.MATERIAL_REGISTRY.isFrozen()) {
            throw new IllegalStateException("Cannot add properties to a Material when registry is frozen!");
        }
        properties.setProperty(key, property);
        properties.verify();
    }

    @Override
    public <T extends IMaterialProperty> void ensureSet(PropertyKey<T> key, boolean verify) {
        properties.ensureSet(key, verify);
    }


    // HARVEST LEVEL

    @Override
    public int getToolHarvestLevel() {
        if (!hasProperty(MaterialProperties.DUST))
            throw new IllegalArgumentException("Material " + materialInfo.name + " does not have a harvest level! Is probably a Fluid");
        return getProperty(MaterialProperties.DUST).getHarvestLevel();
    }

    @Override
    public int getBlockHarvestLevel() {
        int harvestLevel = getToolHarvestLevel();
        return harvestLevel > 0 ? harvestLevel - 1 : harvestLevel;
    }


    // MATERIAL RGB

    @Override
    public void setMaterialRGB(int materialRGB) {
        materialInfo.color = materialRGB;
    }

    @Override
    public int getMaterialRGB() {
        return materialInfo.color;
    }

    @Override
    public boolean hasFluidColor() {
        return materialInfo.hasFluidColor;
    }


    // ICON SET

    @Override
    public void setMaterialIconSet(MaterialIconSet materialIconSet) {
        materialInfo.iconSet = materialIconSet;
    }

    @Override
    public MaterialIconSet getMaterialIconSet() {
        return materialInfo.iconSet;
    }


    // ELEMENTAL DATA

    @Nullable
    @Override
    public Element getElement() {
        return materialInfo.element;
    }

    @Override
    public boolean isRadioactive() {
        if (materialInfo.element != null)
            return materialInfo.element.halfLifeSeconds >= 0;
        for (MaterialStack material : materialInfo.componentList)
            if (material.material.isRadioactive()) return true;
        return false;
    }

    @Override
    public long getProtons() {
        if (materialInfo.element != null)
            return materialInfo.element.getProtons();
        if (materialInfo.componentList.isEmpty())
            return Math.max(1, Elements.Tc.getProtons());
        long totalProtons = 0, totalAmount = 0;
        for (MaterialStack material : materialInfo.componentList) {
            totalAmount += material.amount;
            totalProtons += material.amount * material.material.getProtons();
        }
        return totalProtons / totalAmount;
    }

    @Override
    public long getNeutrons() {
        if (materialInfo.element != null)
            return materialInfo.element.getNeutrons();
        if (materialInfo.componentList.isEmpty())
            return Elements.Tc.getNeutrons();
        long totalNeutrons = 0, totalAmount = 0;
        for (MaterialStack material : materialInfo.componentList) {
            totalAmount += material.amount;
            totalNeutrons += material.amount * material.material.getNeutrons();
        }
        return totalNeutrons / totalAmount;
    }

    @Override
    public long getMass() {
        if (materialInfo.element != null)
            return materialInfo.element.getMass();
        if (materialInfo.componentList.size() <= 0)
            return Elements.Tc.getMass();
        long totalMass = 0, totalAmount = 0;
        for (MaterialStack material : materialInfo.componentList) {
            totalAmount += material.amount;
            totalMass += material.amount * material.material.getMass();
        }
        return totalMass / totalAmount;
    }


    // END INTERFACE

    public boolean hasFluid() {
        return hasProperty(MaterialProperties.FLUID);
    }

    public Fluid getFluid() {
        FluidProperty prop = getProperty(MaterialProperties.FLUID);
        if (prop == null)
            throw new IllegalArgumentException("Material " + materialInfo.name + " does not have a Fluid!");

        Fluid fluid = prop.getFluid();
        if (fluid == null)
            GTLog.logger.warn("Material {} Fluid was null!", this);

        return fluid;
    }

    public FluidStack getFluid(int amount) {
        return new FluidStack(getFluid(), amount);
    }

    public int getBlastTemperature() {
        BlastProperty prop = properties.getProperty(MaterialProperties.BLAST);
        return prop == null ? 0 : prop.getBlastTemperature();
    }

    public FluidStack getPlasma(int amount) {
        PlasmaProperty prop = properties.getProperty(MaterialProperties.PLASMA);
        return prop == null ? null : prop.getPlasma(amount);
    }

    @Override
    public String toString() {
        return getName();
    }

    // TODO Fix isotope tooltips being set toSmallDownNumbers
    private String calculateChemicalFormula() {
        if (chemicalFormula != null) return this.chemicalFormula;
        if (materialInfo.element != null) {
            return materialInfo.element.getSymbol();
        }
        if (!materialInfo.componentList.isEmpty()) {
            StringBuilder components = new StringBuilder();
            for (MaterialStack component : materialInfo.componentList)
                components.append(component.toString());
            return components.toString();
        }
        return "";
    }

    protected void calculateDecompositionType() {
        if (!materialInfo.componentList.isEmpty() &&
                !hasFlag(MaterialFlags.DECOMPOSITION_BY_CENTRIFUGING) &&
                !hasFlag(MaterialFlags.DECOMPOSITION_BY_ELECTROLYZING) &&
                !hasFlag(MaterialFlags.DISABLE_DECOMPOSITION)) {
            boolean onlyMetalMaterials = true;
            for (MaterialStack materialStack : materialInfo.componentList) {
                IMaterial material = materialStack.material;
                onlyMetalMaterials &= material.hasProperty(MaterialProperties.INGOT);
            }
            //allow centrifuging of alloy materials only
            if (onlyMetalMaterials) {
                flags.addFlags(MaterialFlags.DECOMPOSITION_BY_CENTRIFUGING);
            } else {
                flags.addFlags(MaterialFlags.DECOMPOSITION_BY_ELECTROLYZING);
            }
        }
    }

    protected void registerMaterial() {
        verifyMaterial();
        GregTechAPI.MATERIAL_REGISTRY.register(this);
    }

    public void verifyMaterial() {
        properties.verify();
        flags.verify(this);
        this.chemicalFormula = calculateChemicalFormula();
        calculateDecompositionType();
    }
}
