package gregtech.core.material.properties;

import gregtech.api.material.IMaterial;
import gregtech.api.material.IMaterialProperty;
import gregtech.core.material.MaterialProperties;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OreProperty implements IMaterialProperty {

    /**
     * List of Ore byproducts.
     * <p>
     * Default: none, meaning only this property's Material.
     */
    private final List<IMaterial> oreByProducts = new ArrayList<>();

    /**
     * Crushed Ore output amount multiplier during Maceration.
     * <p>
     * Default: 1 (no multiplier).
     */
    private int oreMultiplier;

    /**
     * Byproducts output amount multiplier during Maceration.
     * <p>
     * Default: 1 (no multiplier).
     */
    private int byProductMultiplier;

    /**
     * Should ore block use the emissive texture.
     * <p>
     * Default: false.
     */
    private boolean emissive;

    /**
     * Material to which smelting of this Ore will result.
     * <p>
     * Material will have a Dust Property.
     * Default: none.
     */
    @Nullable
    private IMaterial directSmeltResult;

    /**
     * Material in which this Ore should be washed to give additional output.
     * <p>
     * Material will have a Fluid Property.
     * Default: none.
     */
    @Nullable
    private IMaterial washedIn;

    /**
     * The amount of Material that the ore should be washed in
     * in the Chemical Bath.
     * <p>
     * Default 100 mb
     */
    private int washedAmount = 100;

    /**
     * During Electromagnetic Separation, this Ore will be separated
     * into this Material and the Material specified by this field.
     * Limit 2 Materials
     * <p>
     * Material will have a Dust Property.
     * Default: none.
     */
    private final List<IMaterial> separatedInto = new ArrayList<>();

    public OreProperty(int oreMultiplier, int byProductMultiplier) {
        this.oreMultiplier = oreMultiplier;
        this.byProductMultiplier = byProductMultiplier;
        this.emissive = false;
    }

    public OreProperty(int oreMultiplier, int byProductMultiplier, boolean emissive) {
        this.oreMultiplier = oreMultiplier;
        this.byProductMultiplier = byProductMultiplier;
        this.emissive = emissive;
    }

    /**
     * Default values constructor.
     */
    public OreProperty() {
        this(1, 1);
    }

    public void setOreMultiplier(int multiplier) {
        this.oreMultiplier = multiplier;
    }

    public int getOreMultiplier() {
        return this.oreMultiplier;
    }

    public void setByProductMultiplier(int multiplier) {
        this.byProductMultiplier = multiplier;
    }

    public int getByProductMultiplier() {
        return this.byProductMultiplier;
    }

    public boolean isEmissive() {
        return emissive;
    }

    public void setEmissive(boolean emissive) {
        this.emissive = emissive;
    }

    public void setDirectSmeltResult(@Nullable IMaterial m) {
        this.directSmeltResult = m;
    }

    @Nullable
    public IMaterial getDirectSmeltResult() {
        return this.directSmeltResult;
    }

    public void setWashedIn(@Nullable IMaterial m) {
        this.washedIn = m;
    }

    public void setWashedIn(@Nullable IMaterial m, int washedAmount) {
        this.washedIn = m;
        this.washedAmount = washedAmount;
    }

    public Pair<IMaterial, Integer> getWashedIn() {
        return Pair.of(this.washedIn, this.washedAmount);
    }

    public void setSeparatedInto(IMaterial... materials) {
        this.separatedInto.addAll(Arrays.asList(materials));
    }

    @Nullable
    public List<IMaterial> getSeparatedInto() {
        return this.separatedInto;
    }

    public void setOreByProducts(IMaterial... materials) {
        this.oreByProducts.addAll(Arrays.asList(materials));
    }

    public List<IMaterial> getOreByProducts() {
        return this.oreByProducts;
    }

    @Override
    public void verifyProperty(IMaterial material) {
        material.ensureSet(MaterialProperties.DUST, true);

        if (directSmeltResult != null) directSmeltResult.ensureSet(MaterialProperties.DUST, true);
        if (washedIn != null) washedIn.ensureSet(MaterialProperties.FLUID, true);
        separatedInto.forEach(m -> m.ensureSet(MaterialProperties.DUST, true));
        oreByProducts.forEach(m -> m.ensureSet(MaterialProperties.DUST, true));
    }
}
