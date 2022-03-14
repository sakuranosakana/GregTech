package gregtech.api.tileentity;

public interface ITileEntityEnergy {

    /**
     *
     * @return true if this emits energy
     */
    boolean isEnergyEmitter();

    /**
     *
     * @return the maximum amperage this MTE deals with
     */
    long getMaxAmperage();
}
