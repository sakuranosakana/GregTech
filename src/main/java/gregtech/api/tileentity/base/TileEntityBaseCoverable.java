package gregtech.api.tileentity.base;

import codechicken.lib.raytracer.CuboidRayTraceResult;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.vec.Cuboid6;
import com.google.common.base.Preconditions;
import gregtech.api.capability.GregtechTileCapabilities;
import gregtech.api.cover.CoverBehavior;
import gregtech.api.cover.CoverDefinition;
import gregtech.api.cover.ICoverable;
import gregtech.common.advancement.GTTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static gregtech.api.capability.GregtechDataCodes.*;
import static gregtech.api.multitileentity.IMultiTileEntity.*;

public abstract class TileEntityBaseCoverable extends TileEntityBaseMultiTileEntity implements ICoverable, IMTECanConnectRedstone, IMTEGetWeakPower, IMTEGetStrongPower, IMTEIsSideSolid {

    public static final IndexedCuboid6 FULL_CUBE_COLLISION = new IndexedCuboid6(null, Cuboid6.full);

    private final CoverBehavior[] coverBehaviors = new CoverBehavior[6];

    @Override
    public boolean placeCoverOnSide(EnumFacing side, ItemStack itemStack, CoverDefinition coverDefinition, EntityPlayer player) {
        Preconditions.checkNotNull(side, "side");
        Preconditions.checkNotNull(coverDefinition, "coverDefinition");
        CoverBehavior coverBehavior = coverDefinition.createCoverBehavior(this, side);
        if (!canPlaceCoverOnSide(side) || !coverBehavior.canAttach()) {
            return false;
        }
        if (coverBehaviors[side.getIndex()] != null) {
            removeCover(side);
        }
        this.coverBehaviors[side.getIndex()] = coverBehavior;
        coverBehavior.onAttached(itemStack, player);
        writeCustomData(COVER_ATTACHED_MTE, buffer -> {
            buffer.writeByte(side.getIndex());
            buffer.writeVarInt(CoverDefinition.getNetworkIdForCover(coverDefinition));
            coverBehavior.writeInitialSyncData(buffer);
        });
        notifyBlockUpdate();
        markDirty();
        onCoverPlacementUpdate();
        GTTriggers.FIRST_COVER_PLACE.trigger((EntityPlayerMP) player);
        return true;
    }

    @Override
    public final boolean removeCover(EnumFacing side) {
        Preconditions.checkNotNull(side, "side");
        CoverBehavior coverBehavior = getCoverAtSide(side);
        if (coverBehavior == null) {
            return false;
        }
        List<ItemStack> drops = coverBehavior.getDrops();
        coverBehavior.onRemoved();
        this.coverBehaviors[side.getIndex()] = null;
        for (ItemStack dropStack : drops) {
            Block.spawnAsEntity(getWorld(), getPos(), dropStack);
        }
        writeCustomData(COVER_REMOVED_MTE, buffer -> buffer.writeByte(side.getIndex()));
        notifyBlockUpdate();
        markDirty();
        onCoverPlacementUpdate();
        return true;
    }

    @Override
    public boolean isValid() {
        return !isDead();
    }

    public boolean canPlaceCoverOnSide(EnumFacing side) {
        ArrayList<IndexedCuboid6> collisionList = new ArrayList<>();
        addCollisionBoundingBox(collisionList);
        //noinspection RedundantIfStatement
        if (ICoverable.doesCoverCollide(side, collisionList, getCoverPlateThickness())) {
            //cover collision box overlaps with meta tile entity collision box
            return false;
        }
        return true;
    }

    /**
     * Called to obtain list of AxisAlignedBB used for collision testing, highlight rendering
     * and ray tracing this meta tile entity's block in world
     */
    public void addCollisionBoundingBox(@Nonnull List<IndexedCuboid6> collisionList) {
        collisionList.add(FULL_CUBE_COLLISION);
    }

    protected void onCoverPlacementUpdate() {

    }

    @Nullable
    public final CoverBehavior getCoverAtSide(@Nonnull EnumFacing side) {
        return coverBehaviors[side.getIndex()];
    }

    @Override
    public void notifyBlockUpdate() {
        getWorld().notifyNeighborsOfStateChange(getPos(), getBlockType(), false);
    }

    @Override
    public void writeCoverData(CoverBehavior behavior, int id, Consumer<PacketBuffer> writer) {
        writeCustomData(UPDATE_COVER_DATA_MTE, buffer -> {
            buffer.writeByte(behavior.attachedSide.getIndex());
            buffer.writeVarInt(id);
            writer.accept(buffer);
        });
    }

    @Override
    public final int getInputRedstoneSignal(EnumFacing side, boolean ignoreCover) {
        if (!ignoreCover && getCoverAtSide(side) != null) {
            return 0; //covers block input redstone signal for machine
        }
        return Math.max(getWeakPower(side), getStrongPower(side));
    }

    public boolean isSideUsed(EnumFacing face) {
        return getCoverAtSide(face) != null;
    }

    public final void onCoverLeftClick(EntityPlayer playerIn, @Nonnull CuboidRayTraceResult result) {
        CoverBehavior coverBehavior = getCoverAtSide(result.sideHit);
        if (coverBehavior == null || !coverBehavior.onLeftClick(playerIn, result)) {
            onLeftClick(playerIn, result.sideHit, result);
        }
    }

    public final boolean onCoverRightClick(EntityPlayer playerIn, EnumHand hand, @Nonnull CuboidRayTraceResult result) {
        CoverBehavior coverBehavior = getCoverAtSide(result.sideHit);
        EnumActionResult coverResult = coverBehavior == null ? EnumActionResult.PASS :
                coverBehavior.onRightClick(playerIn, hand, result);
        if (coverResult != EnumActionResult.PASS) {
            return coverResult == EnumActionResult.SUCCESS;
        }
        return onRightClick(playerIn, hand, result.sideHit, result);
    }

    public final boolean onCoverScrewdriverClick(EntityPlayer playerIn, EnumHand hand, CuboidRayTraceResult result) {
        EnumFacing hitFacing = ICoverable.determineGridSideHit(result);
        boolean accessingActiveOutputSide = false;
        if (this.getCapability(GregtechTileCapabilities.CAPABILITY_ACTIVE_OUTPUT_SIDE, hitFacing) != null) {
            accessingActiveOutputSide = playerIn.isSneaking();
        }
        EnumFacing coverSide = ICoverable.traceCoverSide(result);
        CoverBehavior coverBehavior = coverSide == null ? null : getCoverAtSide(coverSide);
        EnumActionResult coverResult = coverBehavior == null ? EnumActionResult.PASS :
                accessingActiveOutputSide ? EnumActionResult.PASS : coverBehavior.onScrewdriverClick(playerIn, hand, result);
        if (coverResult != EnumActionResult.PASS) {
            return coverResult == EnumActionResult.SUCCESS;
        }
        return onScrewdriverClick(playerIn, hand, result.sideHit, result);
    }

    /**
     * Called when player clicks on specific side of this meta tile entity
     *
     * @return true if something happened, so animation will be played
     */
    public boolean onRightClick(@Nonnull EntityPlayer playerIn, EnumHand hand, EnumFacing facing, CuboidRayTraceResult hitResult) {
        if (!playerIn.isSneaking()) {
            return true;
        } else if (playerIn.isSneaking() && playerIn.getHeldItemMainhand().isEmpty()) {
            EnumFacing hitFacing = hitResult.sideHit;
            CoverBehavior coverBehavior = hitFacing == null ? null : getCoverAtSide(hitFacing);
            EnumActionResult coverResult = coverBehavior == null ? EnumActionResult.PASS : coverBehavior.onScrewdriverClick(playerIn, hand, hitResult);
            return coverResult == EnumActionResult.SUCCESS;
        }
        return false;
    }

    /**
     * Called when player clicks wrench on specific side of this meta tile entity
     *
     * @return true if something happened, so wrench will get damaged and animation will be played
     */
    public boolean onWrenchClick(EntityPlayer playerIn, EnumHand hand, EnumFacing wrenchSide, CuboidRayTraceResult hitResult) {
        return false;
    }

    /**
     * Called when player clicks screwdriver on specific side of this meta tile entity
     *
     * @return true if something happened, so screwdriver will get damaged and animation will be played
     */
    public boolean onScrewdriverClick(EntityPlayer playerIn, EnumHand hand, EnumFacing facing, CuboidRayTraceResult hitResult) {
        return false;
    }

    /**
     * Called when player clicks on specific side of this meta tile entity
     *
     * @return true if something happened, so animation will be played
     */
    public boolean onLeftClick(EntityPlayer player, EnumFacing facing, CuboidRayTraceResult hitResult) {
        return false;
    }

    public final void dropAllCovers() {
        for (EnumFacing coverSide : EnumFacing.VALUES) {
            CoverBehavior coverBehavior = coverBehaviors[coverSide.getIndex()];
            if (coverBehavior == null) continue;
            List<ItemStack> drops = coverBehavior.getDrops();
            coverBehavior.onRemoved();
            for (ItemStack dropStack : drops) {
                Block.spawnAsEntity(getWorld(), getPos(), dropStack);
            }
        }
    }

    /**
     * @return the cover plate thickness. It is used to render cover's base plate
     * if this meta tile entity is not full block length, and also
     * to check whatever cover placement is possible on specified side,
     * because cover cannot be placed if collision boxes of machine and its plate overlap
     * If zero, it is expected that machine is full block and plate doesn't need to be rendered
     */
    @Override
    public double getCoverPlateThickness() {
        return 0.0;
    }

    /**
     * ItemStack currently being rendered by this multi tile entity
     * Use this to obtain itemstack-specific data like contained fluid, painting color
     * Generally useful in combination with {@link #initFromNBT(NBTTagCompound, ResourceLocation, short)}
     */
    @SideOnly(Side.CLIENT)
    protected ItemStack renderContextStack;

    @SideOnly(Side.CLIENT)
    public void setRenderContextStack(ItemStack itemStack) {
        this.renderContextStack = itemStack;
    }

    @SideOnly(Side.CLIENT)
    public int getPaintingColorForRendering() {
        //noinspection ConstantConditions
        if (getWorld() == null && renderContextStack != null) {
            NBTTagCompound tagCompound = renderContextStack.getTagCompound();
            if (tagCompound != null && tagCompound.hasKey(TAG_KEY_PAINTING_COLOR, Constants.NBT.TAG_INT)) {
                return tagCompound.getInteger(TAG_KEY_PAINTING_COLOR);
            }
        }
        return isPainted() ? getPaintingColor() : getDefaultPaintingColor();
    }

    @Override
    public boolean shouldRenderBackSide() {
        return !isOpaqueCube();
    }

    @Override
    public void scheduleRenderUpdate() {

    }

    @Override
    public int getRedstoneIncoming(EnumFacing facing) {
        if (!hasAnyCover()) return super.getRedstoneIncoming(facing);
        int redstone = 0;
        for (EnumFacing side : EnumFacing.VALUES) {
            CoverBehavior coverBehavior = getCoverAtSide(side);
            if (coverBehavior == null) {
                redstone = (byte) Math.max(redstone, world.getRedstonePower(pos.offset(side), side));
            } else {
                redstone = (byte) Math.max(redstone, coverBehavior.getRedstoneSignalInput());
            }
            if (redstone >= 15) return 15;
        }
        return redstone;
    }

    @Override
    public int getWeakPower(EnumFacing side) {
        EnumFacing actualSide = side.getOpposite();
        if (hasAnyCover()) {
            CoverBehavior coverBehavior = getCoverAtSide(actualSide);
            if (coverBehavior != null) return coverBehavior.getRedstoneSignalOutput();
        }
        return 0;
    }

    @Override
    public int getStrongPower(EnumFacing side) {
        EnumFacing actualSide = side.getOpposite();
        if (hasAnyCover()) {
            CoverBehavior coverBehavior = getCoverAtSide(actualSide);
            if (coverBehavior != null) return coverBehavior.getRedstoneSignalOutput();
        }
        return 0;
    }

    @Override
    public boolean isSideSolid(EnumFacing side) {
        return hasAnyCover() && getCoverAtSide(side) != null;
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound data) {
        super.writeToNBT(data);
        NBTTagList coversList = new NBTTagList();
        for (EnumFacing coverSide : EnumFacing.VALUES) {
            CoverBehavior coverBehavior = coverBehaviors[coverSide.getIndex()];
            if (coverBehavior != null) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                ResourceLocation coverId = coverBehavior.getCoverDefinition().getCoverId();
                tagCompound.setString("CoverId", coverId.toString());
                tagCompound.setByte("Side", (byte) coverSide.getIndex());
                coverBehavior.writeToNBT(tagCompound);
                coversList.appendTag(tagCompound);
            }
        }
        data.setTag("Covers", coversList);
        return data;
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound data) {
        super.readFromNBT(data);
        NBTTagList coversList = data.getTagList("Covers", Constants.NBT.TAG_COMPOUND);
        for (int index = 0; index < coversList.tagCount(); index++) {
            NBTTagCompound tagCompound = coversList.getCompoundTagAt(index);
            if (tagCompound.hasKey("CoverId", Constants.NBT.TAG_STRING)) {
                EnumFacing coverSide = EnumFacing.VALUES[tagCompound.getByte("Side")];
                ResourceLocation coverId = new ResourceLocation(tagCompound.getString("CoverId"));
                CoverDefinition coverDefinition = CoverDefinition.getCoverById(coverId);
                CoverBehavior coverBehavior = coverDefinition.createCoverBehavior(this, coverSide);
                coverBehavior.readFromNBT(tagCompound);
                this.coverBehaviors[coverSide.getIndex()] = coverBehavior;
            }
        }
    }

    @Override
    public boolean hasAnyCover() {
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (getCoverAtSide(facing) != null) return true;
        }
        return false;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        for (EnumFacing facing : EnumFacing.VALUES) {
            CoverBehavior coverBehavior = getCoverAtSide(facing);
            if (coverBehavior != null) drops.addAll(coverBehavior.getDrops());
        }
    }

    @Override
    public boolean canConnectRedstone(EnumFacing side) {
        CoverBehavior coverBehavior = getCoverAtSide(side);
        return coverBehavior != null && coverBehavior.canConnectRedstone();
    }
}
