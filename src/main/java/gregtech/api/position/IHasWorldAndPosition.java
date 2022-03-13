package gregtech.api.position;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public interface IHasWorldAndPosition extends IHasWorld, IHasPosition {

    TileEntity getTileEntityAtSideAndDistance(EnumFacing facing, int distance);

    boolean hasRedstoneIncoming();

    int getRedstoneIncoming(EnumFacing facing);

    int getComparatorIncoming(EnumFacing facing);
}
