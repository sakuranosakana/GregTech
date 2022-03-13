package gregtech.api.tileentity;

import gregtech.api.position.IHasWorldAndPosition;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public interface ITickableTileEntity extends IHasWorldAndPosition, ITileEntityUnloadable {

    /**
     * Sends a Block Event to the Client TileEntity, the byte Parameters are only for validation as Minecraft doesn't properly write Packet Data.
     * <p>
     * This should be used to send Block Sound Effects to the Client, as it is much less Network Heavy to have 2 Bytes rather than a String.
     */
    void sendBlockEvent(byte ID, byte value);

    /**
     * @return the amount of Time this TileEntity has been loaded.
     */
    long getTimer();

    /**
     * YOU MUST HAVE THIS INSIDE YOUR BLOCK CODE!!!
     * <p>
     * public void onNeighborChange(IBlockAccess aWorld, int aX, int aY, int aZ, int aTileX, int aTileY, int aTileZ) {
     * TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
     * if (tTileEntity instanceof ITileEntity) ((ITileEntity)tTileEntity).onAdjacentBlockChange(aTileX, aTileY, aTileZ);
     * }
     * <p>
     * public void onNeighborBlockChange(World aWorld, int aX, int aY, int aZ, Block aBlock) {
     * TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
     * if (tTileEntity instanceof ITileEntity) ((ITileEntity)tTileEntity).onAdjacentBlockChange(aX, aY, aZ);
     * }
     */
    void onAdjacentBlockChange(BlockPos pos);

    /**
     * Called after the TileEntity has been placed and set up.
     */
    void onTileEntityPlaced();

    boolean allowInteraction(Entity entity);
}
