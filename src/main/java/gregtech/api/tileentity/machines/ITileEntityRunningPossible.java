package gregtech.api.tileentity.machines;

import gregtech.api.tileentity.ITileEntityUnloadable;

public interface ITileEntityRunningPossible extends ITileEntityUnloadable {

    /**
     * @return true if the Machine could be running actively or not.
     *
     * Used to check if there is Recipe compatible content in slots.
     *
     * Should return {@code true} if the Machine is already running ACTIVELY too!
     */
    boolean getStateRunningPossible();

}
