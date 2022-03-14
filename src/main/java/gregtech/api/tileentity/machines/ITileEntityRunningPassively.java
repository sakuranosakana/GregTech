package gregtech.api.tileentity.machines;

public interface ITileEntityRunningPassively extends ITileEntityRunningPossible {

    /**
     * @return true if the Machine is running or not, regardless if it processes something or not.
     */
    boolean getStateRunningPassively();
}
