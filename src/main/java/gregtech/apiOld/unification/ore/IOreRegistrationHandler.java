package gregtech.apiOld.unification.ore;

import gregtech.core.material.internal.Material;

@FunctionalInterface
public interface IOreRegistrationHandler {

    void processMaterial(OrePrefix orePrefix, Material material);

}
