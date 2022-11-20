package gregtech.core.material.internal;

import com.google.common.collect.ImmutableList;
import gregtech.api.material.MaterialStack;
import gregtech.apiOld.fluids.fluidType.FluidType;
import gregtech.apiOld.unification.Element;
import gregtech.apiOld.util.GTUtility;
import gregtech.core.material.MaterialIconSets;
import gregtech.core.material.MaterialProperties;

/**
 * Holds the basic info for a Material, like the name, color, id, etc..
 */
public class MaterialInfo {

    /**
     * The unlocalized name of this Material.
     * <p>
     * Required.
     */
    public final String name;

    /**
     * The MetaItem ID of this Material.
     * <p>
     * Required.
     */
    public final int metaItemSubId;

    /**
     * The color of this Material.
     * <p>
     * Default: 0xFFFFFF if no Components, otherwise it will be the average of Components.
     */
    public int color = -1;

    /**
     * The color of this Material.
     * <p>
     * Default: 0xFFFFFF if no Components, otherwise it will be the average of Components.
     */
    public boolean hasFluidColor = true;

    /**
     * The IconSet of this Material.
     * <p>
     * Default: - GEM_VERTICAL if it has GemProperty.
     * - DULL if has DustProperty or IngotProperty.
     * - FLUID or GAS if only has FluidProperty or PlasmaProperty, depending on {@link FluidType}.
     */
    public MaterialIconSet iconSet;

    /**
     * The components of this Material.
     * <p>
     * Default: none.
     */
    public ImmutableList<MaterialStack> componentList;

    /**
     * The Element of this Material, if it is a direct Element.
     * <p>
     * Default: none.
     */
    public Element element;

    public MaterialInfo(int metaItemSubId, String name) {
        this.metaItemSubId = metaItemSubId;
        if (!GTUtility.toLowerCaseUnderscore(GTUtility.lowerUnderscoreToUpperCamel(name)).equals(name))
            throw new IllegalStateException("Cannot add materials with names like 'materialnumber'! Use 'material_number' instead.");
        this.name = name;
    }

    public void verifyInfo(MaterialPropertyStorage p, boolean averageRGB) {

        // Verify IconSet
        if (iconSet == null) {
            if (p.hasProperty(MaterialProperties.GEM)) {
                iconSet = MaterialIconSets.GEM_VERTICAL;
            } else if (p.hasProperty(MaterialProperties.DUST) || p.hasProperty(MaterialProperties.INGOT)) {
                iconSet = MaterialIconSets.DULL;
            } else if (p.hasProperty(MaterialProperties.FLUID)) {
                if (p.getProperty(MaterialProperties.FLUID).isGas()) {
                    iconSet = MaterialIconSets.GAS;
                } else iconSet = MaterialIconSets.FLUID;
            } else if (p.hasProperty(MaterialProperties.PLASMA))
                iconSet = MaterialIconSets.FLUID;
            else iconSet = MaterialIconSets.DULL;
        }

        // Verify MaterialRGB
        if (color == -1) {
            if (!averageRGB || componentList.isEmpty())
                color = 0xFFFFFF;
            else {
                long colorTemp = 0;
                int divisor = 0;
                for (MaterialStack stack : componentList) {
                    colorTemp += stack.material.getMaterialRGB() * stack.amount;
                    divisor += stack.amount;
                }
                color = (int) (colorTemp / divisor);
            }
        }
    }
}
