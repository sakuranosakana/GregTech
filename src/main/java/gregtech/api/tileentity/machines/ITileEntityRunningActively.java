package gregtech.api.tileentity.machines;

public interface ITileEntityRunningActively extends ITileEntityRunningPassively {

    /**
     * @return true if the Machine is processing something or emitting energy, etc.
     */
    boolean getStateRunningActively();
}
