package gregtech.api.tileentity.machines;

public interface ITileEntityRunningSuccessfully extends ITileEntityRunningActively {

    /**
     * @return true if the Machine has just processed something successfully.
     *
     * Can be a constant true for fast Machines that produce every tick.
     */
    boolean getStateRunningSuccessfully();
}
