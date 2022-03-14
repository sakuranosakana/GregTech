package gregtech.api.tileentity;

import net.minecraft.tileentity.TileEntity;

public interface ITileEntityAdjacentInventoryUpdatable extends ITileEntityUnloadable {

    /**
     * Gets called by some GT things like Pipes to notify about changes inside the Inventory of the caller.
     *
     * This is only for important Inventory Updates, like when a Pipe has more free space than before.
     */
    void adjacentInventoryUpdated(byte aSide, TileEntity tileEntity);
}
