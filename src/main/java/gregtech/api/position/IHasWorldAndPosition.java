package gregtech.api.position;

import net.minecraft.util.EnumFacing;

public interface IHasWorldAndPosition extends IHasWorld, IHasPosition {

    boolean hasRedstoneIncoming();

    int getRedstoneIncoming(EnumFacing facing);

    int getComparatorIncoming(EnumFacing facing);
}
