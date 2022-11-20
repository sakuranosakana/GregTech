package gregtech.core.material.properties;

import gregtech.api.material.IMaterial;
import gregtech.api.material.IMaterialProperty;
import gregtech.core.material.MaterialProperties;

public class GemProperty implements IMaterialProperty {

    @Override
    public void verifyProperty(IMaterial material) {
        material.ensureSet(MaterialProperties.DUST, true);
        if (material.hasProperty(MaterialProperties.INGOT)) {
            throw new IllegalStateException(
                    "Material " + material +
                            " has both Ingot and Gem Property, which is not allowed!");
        }
    }
}
