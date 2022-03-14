package gregtech.api.tileentity.base;

import appeng.api.movable.IMovableTile;
import gregtech.api.GTValues;
import gregtech.api.multitileentity.IMultiTileEntity;
import gregtech.api.tileentity.ITickableTileEntity;
import gregtech.api.tileentity.ITileEntityUnloadable;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTUtility;
import gregtech.common.ConfigHolder;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.Nonnull;

@Optional.Interface(iface = "appeng.api.movable.IMovableTile", modid = GTValues.MODID_APPENG)
public abstract class TileEntityBase extends TileEntity implements ITickableTileEntity, IMovableTile, ITickable {

    /**
     * If this TileEntity checks for the Chunk to be loaded before returning World based values. If this is set to true, this TileEntity will not cause orphaned Chunks.
     */
    public boolean ignoreUnloadedChunks = true;

    /**
     * This Variable checks if this TileEntity is dead, because Minecraft is too stupid to have proper TileEntity unloading.
     */
    public boolean isDead = false;

    /**
     * This Variable checks if this TileEntity should refresh when the Block is being set. That way you can turn this check off any time you need it.
     */
    public boolean shouldRefresh = true;

    /**
     * This Variable is for a buffered Block Update.
     */
    public boolean doesBlockUpdate = false;

    /**
     * If this TileEntity is ticking at all
     */
    public final boolean isTicking;

    public TileEntityBase(boolean isTicking) {
        this.isTicking = isTicking;
    }

    @Override
    public void onTileEntityPlaced() {
        //
    }

    @Override
    public void onAdjacentBlockChangeBase(BlockPos pos) {
        //
    }

    @Override
    public boolean isServerSide() {
        return world == null ? FMLCommonHandler.instance().getEffectiveSide().isServer() : !world.isRemote;
    }

    @Override
    public boolean isClientSide() {
        return world == null ? FMLCommonHandler.instance().getEffectiveSide().isClient() : world.isRemote;
    }

    @Override
    public int random(int range) {
        return GTValues.RNG.nextInt(range);
    }

    @Override
    public Biome getBiome(BlockPos pos) {
        return world == null ? Biomes.PLAINS : world.getBiome(pos);
    }

    @Override
    public Block getBlock(BlockPos pos) {
        return getState(pos).getBlock();
    }

    @Nonnull
    public IBlockState getState(BlockPos pos) {
        if (world == null) return Blocks.AIR.getDefaultState();
        if (ignoreUnloadedChunks && crossedChunkBorder(pos) && world.isAirBlock(pos))
            return Blocks.AIR.getDefaultState();
        return world.getBlockState(pos);
    }

    @Override
    public int getMetaData(BlockPos pos) {
        if (world == null) return 0;
        if (ignoreUnloadedChunks && crossedChunkBorder(pos) && world.isAirBlock(pos)) return 0;
        IBlockState state = getState(pos);
        return state.getBlock().getMetaFromState(state);
    }

    @Override
    public int getLightLevel(BlockPos pos) {
        if (world == null) return 14;
        if (ignoreUnloadedChunks && crossedChunkBorder(pos) && world.isAirBlock(pos)) return 0;
        return (int) (world.getLightBrightness(pos) * 15);
    }

    @Override
    public boolean canSeeSky(BlockPos pos) {
        if (world == null) return true;
        if (ignoreUnloadedChunks && crossedChunkBorder(pos) && world.isAirBlock(pos)) return true;
        return world.canSeeSky(pos);
    }

    @Override
    public boolean isRainedOn(BlockPos pos) {
        if (world == null) return true;
        if (ignoreUnloadedChunks && crossedChunkBorder(pos) && world.isAirBlock(pos)) return true;
        return world.getPrecipitationHeight(pos).getY() <= pos.getY();
    }

    @Override
    public boolean getOpacity(BlockPos pos) {
        if (world == null) return false;
        if (ignoreUnloadedChunks && crossedChunkBorder(pos) && world.isAirBlock(pos)) return false;
        return world.getBlockState(pos).isOpaqueCube();
    }

    @Override
    public boolean isAir(BlockPos pos) {
        if (world == null) return true;
        if (ignoreUnloadedChunks && crossedChunkBorder(pos) && world.isAirBlock(pos)) return true;
        return world.isAirBlock(pos);
    }

    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        if (world == null) return null;
        if (ignoreUnloadedChunks && crossedChunkBorder(pos) && world.isAirBlock(pos)) return null;
        if (ignoreUnloadedChunks || world.isAirBlock(pos)) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof ITileEntityUnloadable && ((ITileEntityUnloadable) tileEntity).isDead())
                return null;
            return tileEntity;
        }
        return null;
    }

    @Override
    public boolean isDead() {
        return isDead;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        setDead();
    }

    @Override
    public void validate() {
        super.validate();
        setAlive();
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        setDead();
    }

    protected void setDead() {
        if (!isDead) isDead = true;
    }

    protected void setAlive() {
        isDead = true;
    }

    @Override
    public void update() {
        // Well, if the TileEntity gets ticked, it is alive.
        if (isDead()) setAlive();

        if (doesBlockUpdate) doBlockUpdate();
    }

    @Override
    public long getTimer() {
        return 0;
    }

    @Override
    public boolean shouldRefresh(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newState) {
        return shouldRefresh || oldState != newState;
    }

    /**
     * Simple Function to prevent Block Updates from happening multiple times within the same Tick.
     */
    public final void causeBlockUpdate() {
        if (isTicking) doesBlockUpdate = true;
        else doBlockUpdate();
    }

    public void doBlockUpdate() {
        world.notifyBlockUpdate(pos, getState(pos), getState(pos), 0);
        if (this instanceof IMultiTileEntity.IMTEGetStrongPower) for (EnumFacing facing : EnumFacing.VALUES) {
            BlockPos sidePos = pos.offset(facing);
            if (getBlock(sidePos).isNormalCube(world.getBlockState(sidePos), world, pos)) {
                world.notifyBlockUpdate(sidePos, getState(sidePos), getState(sidePos), 0);
            }
        }
        doesBlockUpdate = false;
    }

    public final boolean crossedChunkBorder(@Nonnull BlockPos pos) {
        return pos.getX() >> 4 != getPos().getX() >> 4 || pos.getZ() >> 4 != getPos().getZ() >> 4;
    }

    public float explosionStrength = 0;

    public final void explode(double strength) {
        explode(!isTicking, strength);
    }

    public void explode(boolean isInstant) {
        // Seems to be a reasonable Default Explosion. This Function can of course be overridden.
        explode(isInstant, 4);
    }

    public void explode(boolean isInstant, double strength) {
        explosionStrength = (float) Math.max(strength, explosionStrength);
        if (isInstant) {
            setToAir();
            if (explosionStrength < 1) {
                world.playSound(null, getPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            } else {
                world.createExplosion(null, getPos().getX(), getPos().getY(), getPos().getZ(), explosionStrength, true);
            }
        }
    }

    public void overcharge(long voltage) {
        // Only explode if allowed
        if (ConfigHolder.machines.doExplosions) explode(GTUtility.getExplosionPower(voltage));
        else setToAir();

        GTLog.logger.info("Machine overcharged with: " + voltage + " EU/t");
    }

    public float getExplosionResistance(Entity exploder, BlockPos pos) {
        return explosionStrength > 0 ? 0 : getExplosionResistance();
    }

    public float getExplosionResistance() {
        return 0;
    }

    public void updateLightValue() {
        if (this instanceof IMultiTileEntity.IMTEGetLightValue) {
            world.setLightFor(EnumSkyBlock.BLOCK, pos, ((IMultiTileEntity.IMTEGetLightValue) this).getLightValue());
            for (EnumFacing facing : EnumFacing.HORIZONTALS)
                world.checkLightFor(EnumSkyBlock.BLOCK, pos.offset(facing));
        }
    }

    public boolean shouldCheckWeakPower(EnumFacing facing) {
        return false;
    }

    @Override
    public boolean hasRedstoneIncoming() {
        return hasRedstoneIncoming(EnumFacing.VALUES);
    }

    public boolean hasRedstoneIncoming(@Nonnull EnumFacing[] facings) {
        for (EnumFacing facing : facings) if (getRedstoneIncoming(facing) > 0) return true;
        return false;
    }

    @Override
    public int getRedstoneIncoming(EnumFacing facing) {
        if (world == null) return 0;
        byte redstone = 0;
        for (EnumFacing enumFacing : EnumFacing.VALUES) {
            redstone = (byte) Math.max(redstone, world.getRedstonePower(pos.offset(enumFacing), facing));
            if (redstone >= 15) return 15;
        }
        return redstone;
    }

    @Override
    public int getComparatorIncoming(EnumFacing facing) {
        if (world == null) return 0;
        Block block = getBlock(pos.offset(facing));
        if (block.hasComparatorInputOverride(getState(pos)))
            return block.getComparatorInputOverride(getState(getPos().offset(facing)), world, getPos().offset(facing.getOpposite()));
        return getRedstoneIncoming(facing);
    }

    public boolean isFireProof(EnumFacing facing) {
        return false;
    }

    public boolean isRainProof(EnumFacing facing) {
        return false;
    }

    public boolean isWaterProof(EnumFacing facing) {
        return false;
    }

    public boolean isLightningProof(EnumFacing facing) {
        return false;
    }

    /**
     * Old Coordinate containing Variant of onCoordinateChange, use only if you really need the Coordinates, as there is also a No-Parameter variant in use for some TileEntity Types!
     */
    public void onCoordinateChange(World world, BlockPos oldPos) {
        onCoordinateChange();
    }

    public void onCoordinateChange() {/**/}

    // AE Stuff

    @Override
    public boolean prepareToMove() {
        return true;
    }

    @Override
    public void doneMoving() {
        onCoordinateChange();
    }

    // Fire Stuff

    public int getFireSpreadSpeed(byte aSide, boolean aDefault) {
        return aDefault ? 150 : 0;
    }

    public int getFlammability(byte aSide, boolean aDefault) {
        return aDefault ? 150 : 0;
    }

    public void setFire(BlockPos pos) {
        IBlockState state = getState(pos);
        if (state.getMaterial() == Material.LAVA || state.getMaterial() == Material.FIRE) return;
        if (state.getMaterial() == Material.CARPET || state.getCollisionBoundingBox(world, pos) == null) {
            world.setBlockState(pos, Blocks.FIRE.getDefaultState());
        }
    }

    public void setOnFire() {
        setFire(getPos());
    }

    public boolean setToFire() {
        return world.setBlockState(pos, Blocks.FIRE.getDefaultState());
    }

    public void setToAir() {
        world.setBlockToAir(pos);
    }

    // Useful to check if a Player or any other Entity is actually allowed to access or interact with this Block.

    @Override
    public boolean allowInteraction(Entity entity) {
        return true;
    }

    public boolean allowRightClick(Entity entity) {
        return allowInteraction(entity);
    }

    public float getPlayerRelativeBlockHardness(EntityPlayer aPlayer, float aOriginal) {
        return allowInteraction(aPlayer) ? Math.max(aOriginal, 0.0001F) : 0;
    }

    @Override
    public TileEntity getTileEntityAtSideAndDistance(EnumFacing facing, int distance) {
        return getTileEntity(getPos().offset(facing).add(distance, distance, distance));
    }

    @Override
    public BlockPos getOffset(@Nonnull EnumFacing facing, int multiplier) {
        return pos.add(facing.getXOffset() * multiplier, facing.getYOffset() * multiplier, facing.getZOffset() * multiplier);
    }
}
