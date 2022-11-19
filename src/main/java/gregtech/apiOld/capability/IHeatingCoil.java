package gregtech.apiOld.capability;

/**
 * intended for use in conjunction with {@link gregtech.apiOld.capability.impl.HeatingCoilRecipeLogic}
 * use with temperature-based multiblocks
 */
public interface IHeatingCoil {

    /**
     *
     * @return the current temperature of the multiblock in Kelvin
     */
    int getCurrentTemperature();
}
