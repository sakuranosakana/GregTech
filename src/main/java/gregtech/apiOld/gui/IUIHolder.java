package gregtech.apiOld.gui;

import gregtech.apiOld.util.IDirtyNotifiable;

public interface IUIHolder extends IDirtyNotifiable {

    boolean isValid();

    boolean isRemote();

    void markAsDirty();

}
