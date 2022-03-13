package gregtech.api.tileentity;

import net.minecraft.util.EnumFacing;

public interface ITileEntityQuickObstructionCheck extends ITileEntityUnloadable {

    boolean isObstructingBlockAt(EnumFacing facing);
}
