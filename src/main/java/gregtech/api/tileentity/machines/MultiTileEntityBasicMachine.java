package gregtech.api.tileentity.machines;

import gregtech.api.metatileentity.IDataInfoProvider;
import gregtech.api.metatileentity.ITieredMetaTileEntity;
import gregtech.api.tileentity.ITileEntityAdjacentInventoryUpdatable;
import gregtech.api.tileentity.ITileEntityEnergy;
import gregtech.api.tileentity.base.TileEntityCombinedInventoryDirectional;
import gregtech.api.tileentity.data.ITileEntityProgress;

import static gregtech.api.multitileentity.IMultiTileEntity.IMTEAddTooltips;

public class MultiTileEntityBasicMachine extends TileEntityCombinedInventoryDirectional implements ITileEntitySwitchableOnOff, ITileEntityAdjacentInventoryUpdatable, ITileEntityRunningSuccessfully, ITileEntityEnergy, ITileEntityProgress, ITieredMetaTileEntity, IMTEAddTooltips, IDataInfoProvider {

}
