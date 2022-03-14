package gregtech.api.tileentity.data;

import gregtech.api.tileentity.ITileEntityUnloadable;
import net.minecraft.util.EnumFacing;

public interface ITileEntityProgress extends ITileEntityUnloadable {
    /**
     * The Progress this Object has right now. Should always return a >= Zero.
     */
    long getProgressValue(EnumFacing side);

    /**
     * The Progress this Object needs to so its Job. Should always return a Number > Zero.
     */
    long getProgressMax(EnumFacing side);
}
