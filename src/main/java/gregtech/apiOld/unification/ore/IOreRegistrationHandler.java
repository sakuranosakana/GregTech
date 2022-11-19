package gregtech.apiOld.unification.ore;

import gregtech.apiOld.unification.material.Material;

@FunctionalInterface
public interface IOreRegistrationHandler {

    void processMaterial(OrePrefix orePrefix, Material material);

}
