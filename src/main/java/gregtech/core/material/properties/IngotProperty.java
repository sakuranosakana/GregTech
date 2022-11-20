package gregtech.core.material.properties;

import gregtech.api.material.IMaterial;
import gregtech.api.material.IMaterialProperty;
import gregtech.core.material.MaterialProperties;

import javax.annotation.Nullable;

public class IngotProperty implements IMaterialProperty {

    /**
     * Specifies a material into which this material parts turn when heated
     */
    private IMaterial smeltInto;

    /**
     * Specifies a material into which this material parts turn when heated in arc furnace
     */
    private IMaterial arcSmeltInto;

    /**
     * Specifies a Material into which this Material Macerates into.
     * <p>
     * Default: this Material.
     */
    private IMaterial macerateInto;

    /**
     * Material which obtained when this material is polarized
     */
    @Nullable
    private IMaterial magneticMaterial;

    public void setSmeltingInto(IMaterial smeltInto) {
        this.smeltInto = smeltInto;
    }

    public IMaterial getSmeltingInto() {
        return this.smeltInto;
    }

    public void setArcSmeltingInto(IMaterial arcSmeltingInto) {
        this.arcSmeltInto = arcSmeltingInto;
    }

    public IMaterial getArcSmeltInto() {
        return this.arcSmeltInto;
    }

    public void setMagneticMaterial(@Nullable IMaterial magneticMaterial) {
        this.magneticMaterial = magneticMaterial;
    }

    @Nullable
    public IMaterial getMagneticMaterial() {
        return magneticMaterial;
    }

    public void setMacerateInto(IMaterial macerateInto) {
        this.macerateInto = macerateInto;
    }

    public IMaterial getMacerateInto() {
        return macerateInto;
    }

    @Override
    public void verifyProperty(IMaterial material) {
        material.ensureSet(MaterialProperties.DUST, true);
        if (material.hasProperty(MaterialProperties.GEM)) {
            throw new IllegalStateException(
                    "Material " + material +
                            " has both Ingot and Gem Property, which is not allowed!");
        }

        if (smeltInto == null) smeltInto = material;
        else smeltInto.ensureSet(MaterialProperties.INGOT, true);

        if (arcSmeltInto == null) arcSmeltInto = material;
        else arcSmeltInto.ensureSet(MaterialProperties.INGOT, true);

        if (macerateInto == null) macerateInto = material;
        else macerateInto.ensureSet(MaterialProperties.INGOT, true);

        if (magneticMaterial != null) magneticMaterial.ensureSet(MaterialProperties.INGOT, true);
    }
}
