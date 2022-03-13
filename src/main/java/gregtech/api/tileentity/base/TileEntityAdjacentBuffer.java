package gregtech.api.tileentity.base;

import gregtech.api.GTValues;
import gregtech.api.tileentity.ITileEntityNeedsSaving;
import gregtech.api.tileentity.ITileEntityUnloadable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;

public abstract class TileEntityAdjacentBuffer extends TileEntityBase implements ITileEntityNeedsSaving {

    /**
     * Timer value
     */
    private long timer = 0L;

    // Create an offset [0,20) to distribute ticks more evenly
    private final int offset = GTValues.RNG.nextInt(20);

    /**
     * Old Coordinates during the previous Tick
     */
    protected BlockPos oldPos = new BlockPos(0, 0, 0);

    /**
     * Buffers adjacent TileEntities for faster access
     * "this" means that there is no TileEntity, while "null" means that it doesn't know if there is even a TileEntity and still needs to check that if needed.
     */
    private final TileEntity[] mBufferedTileEntities = new TileEntity[6];

    public TileEntityAdjacentBuffer() {
        super(true);
    }

    private void clearNullMarkersFromTileEntityBuffer() {
        for (int i = 0; i < mBufferedTileEntities.length; i++)
            if (mBufferedTileEntities[i] == this) mBufferedTileEntities[i] = null;
    }

    private void clearEverythingFromTileEntityBuffer() {
        Arrays.fill(mBufferedTileEntities, null);
    }

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
    @Override
    public final void onAdjacentBlockChangeBase(BlockPos pos) {
        super.onAdjacentBlockChangeBase(pos);
        clearNullMarkersFromTileEntityBuffer();
        onAdjacentBlockChange(pos);
    }

    public void onAdjacentBlockChange(BlockPos pos) {
        //
    }

    /**
     * Highly optimised in order to return adjacent TileEntities much faster.
     */
    public TileEntity getTileEntityAtSideAndDistance(EnumFacing facing, int distance) {
        if (world == null) return null;
        if (distance != 1) return super.getTileEntityAtSideAndDistance(facing, distance);
        int index = facing.ordinal();
        if (mBufferedTileEntities[index] == this) return null;
        BlockPos pos = getOffset(facing, 1);
        boolean tChunksCrossed = crossedChunkBorder(pos);
        if (tChunksCrossed && (!(mBufferedTileEntities[index] instanceof ITileEntityUnloadable) || ((ITileEntityUnloadable) mBufferedTileEntities[index]).isDead())) {
            mBufferedTileEntities[index] = null;
            if (ignoreUnloadedChunks && world.isAirBlock(pos)) return null;
        }
        if (mBufferedTileEntities[index] == null) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity == null) {
                mBufferedTileEntities[index] = this;
                return null;
            }
            if (tChunksCrossed) world.markChunkDirty(pos, tileEntity);
            if (tileEntity.getPos().equals(pos)) return mBufferedTileEntities[index] = tileEntity;
            mBufferedTileEntities[index] = null;
            return tileEntity;
        }
        if (!mBufferedTileEntities[index].getPos().equals(pos)) {
            mBufferedTileEntities[index] = null;
            return getTileEntityAtSideAndDistance(facing, distance);
        }
        if (mBufferedTileEntities[index] instanceof ITileEntityUnloadable) {
            if (((ITileEntityUnloadable) mBufferedTileEntities[index]).isDead()) {
                mBufferedTileEntities[index] = null;
                return getTileEntityAtSideAndDistance(facing, distance);
            }
        } else if (mBufferedTileEntities[index].isInvalid()) {
            mBufferedTileEntities[index] = null;
            return getTileEntityAtSideAndDistance(facing, distance);
        }
        if (tChunksCrossed) world.markChunkDirty(pos, mBufferedTileEntities[index]);
        return mBufferedTileEntities[index];
    }

    @Override
    public void validate() {
        clearNullMarkersFromTileEntityBuffer();
        super.validate();
    }

    @Override
    public void invalidate() {
        clearNullMarkersFromTileEntityBuffer();
        super.invalidate();
    }

    @Override
    public void onChunkUnload() {
        clearNullMarkersFromTileEntityBuffer();
        super.onChunkUnload();
    }

    @Override
    public void update() {
        super.update();

        if (!isDead()) {
            if (timer == 0) {
                onFirstTick();
            }
            timer++;

            if (isServerSide()) {
                if (timer == 1) {
                    oldPos = new BlockPos(pos);
                    doesBlockUpdate = true;
                    clearEverythingFromTileEntityBuffer();
                }
                if (!oldPos.equals(pos)) {
                    clearEverythingFromTileEntityBuffer();
                    onCoordinateChange(world, oldPos);
                    oldPos = new BlockPos(pos);
                }
            }
        }
    }

    @Override
    public long getTimer() {
        return timer;
    }

    /**
     * Replacement for getTimer().
     *
     * @return Timer value with a random offset of [0,20].
     */
    public long getOffsetTimer() {
        return timer + offset;
    }

    public boolean isFirstTick() {
        return timer == 0;
    }

    protected void onFirstTick() {

    }

    @Override
    public void doneMoving() {
        clearEverythingFromTileEntityBuffer();
        onCoordinateChange(world, oldPos);
        oldPos = new BlockPos(pos);
    }
}
