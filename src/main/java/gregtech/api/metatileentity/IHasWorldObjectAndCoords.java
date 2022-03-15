package gregtech.api.metatileentity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IHasWorldObjectAndCoords {

    World getWorld();

    BlockPos getPos();

    default boolean isServerSide() {
        return getWorld() != null && !getWorld().isRemote;
    }

    default boolean isClientSide() {
        return getWorld() != null && getWorld().isRemote;
    }

    long getOffsetTimer();

    void markDirty();

    void scheduleRenderUpdate();

    void notifyBlockUpdate();
}
