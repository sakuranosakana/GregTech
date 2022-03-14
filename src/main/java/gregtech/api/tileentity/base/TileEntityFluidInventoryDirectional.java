package gregtech.api.tileentity.base;

import codechicken.lib.raytracer.CuboidRayTraceResult;
import com.google.common.base.Preconditions;
import gregtech.api.tileentity.ITileEntityRotatable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;

import javax.annotation.Nonnull;

import static gregtech.api.capability.GregtechDataCodes.UPDATE_FRONT_FACING;

public abstract class TileEntityFluidInventoryDirectional extends TileEntityFluidInventory implements ITileEntityRotatable {

    protected EnumFacing frontFacing = EnumFacing.NORTH;

    public TileEntityFluidInventoryDirectional(boolean hasSeparateIO) {
        super(hasSeparateIO);
    }

    public EnumFacing getFrontFacing() {
        return frontFacing;
    }

    public void setFrontFacing(EnumFacing frontFacing) {
        Preconditions.checkNotNull(frontFacing, "frontFacing");
        this.frontFacing = frontFacing;
        //noinspection ConstantConditions
        if (getWorld() != null && !getWorld().isRemote) {
            notifyBlockUpdate();
            markDirty();
            writeCustomData(UPDATE_FRONT_FACING, buf -> buf.writeByte(frontFacing.getIndex()));
            onFrontFacingSet(frontFacing);
        }
    }

    /**
     * Called when player clicks wrench on specific side of this meta tile entity
     *
     * @return true if something happened, so wrench will get damaged and animation will be played
     */
    @Override
    public boolean onWrenchClick(@Nonnull EntityPlayer playerIn, EnumHand hand, EnumFacing wrenchSide, CuboidRayTraceResult hitResult) {
        if (playerIn.isSneaking()) {
            if (wrenchSide == getFrontFacing() || !isValidFrontFacing(wrenchSide)) {
                return false;
            }
            if (wrenchSide != null && !getWorld().isRemote) {
                setFrontFacing(wrenchSide);
            }
            return true;
        }
        return false;
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound data) {
        super.writeToNBT(data);
        data.setInteger("FrontFacing", frontFacing.getIndex());
        return data;
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound data) {
        super.readFromNBT(data);
        this.frontFacing = EnumFacing.VALUES[data.getInteger("FrontFacing")];
    }

    @Override
    public void writeInitialSyncData(PacketBuffer buf) {
        super.writeInitialSyncData(buf);
        buf.writeByte(this.frontFacing.getIndex());
    }

    @Override
    public void receiveInitialSyncData(PacketBuffer buf) {
        super.receiveInitialSyncData(buf);
        this.frontFacing = EnumFacing.VALUES[buf.readByte()];
    }

    @Override
    public void receiveCustomData(int discriminator, PacketBuffer buffer) {
        super.receiveCustomData(discriminator, buffer);
        if (discriminator == UPDATE_FRONT_FACING) {
            this.frontFacing = EnumFacing.VALUES[buffer.readByte()];
            scheduleChunkForRenderUpdate();
        }
    }
}
