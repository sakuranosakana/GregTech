package gregtech.api.metatileentity;

import gregtech.api.gui.IUIHolder;

/**
 * A simple compound Interface for all my TileEntities.
 * <p/>
 * Also delivers most of the Informations about TileEntities.
 * <p/>
 */
public interface IGregTechTileEntity extends IHasWorldObjectAndCoords, IUIHolder {

    IMetaTileEntity getMetaTileEntity();

    IMetaTileEntity setMetaTileEntity(IMetaTileEntity metaTileEntity);
}
