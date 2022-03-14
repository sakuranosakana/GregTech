package gregtech.api.tileentity.machines;

import gregtech.api.tileentity.ITileEntityUnloadable;

public interface ITileEntitySwitchableOnOff extends ITileEntityUnloadable {

    /**
     * @param isOnOff true for ON, false for OFF.
     * @return the state of the Machine after it has switched, {@see getStateOnOff()}.
     */
    boolean setStateOnOff(boolean isOnOff);

    /**
     * @return the ON/OFF State of the Machine. true for ON false for OFF.
     */
    boolean getStateOnOff();
}
