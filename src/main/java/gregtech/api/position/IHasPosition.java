package gregtech.api.position;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public interface IHasPosition {

    /**
     * Do not change the XYZ of the returned BlockPos Object!
     */
    BlockPos getPos();

    BlockPos getOffset(EnumFacing facing, int multiplier);
}
