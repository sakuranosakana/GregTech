package gregtech.api.tileentity.base;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileEntityFluidInventory extends TileEntityBaseCoverable {

    public static final String FLUID_INVENTORY_TAG = "FluidInventory";

    protected IFluidHandler fluidInventory;

    public boolean fluidInventoryChanged = false;

    public TileEntityFluidInventory() {
        super();
        initializeInventory();
    }

    protected void initializeInventory() {
        this.fluidInventory = new FluidTank(getMinimumFluidInventorySize());
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound data) {
        super.writeToNBT(data);
        fluidInventory = getDefaultFluidInventory(data);

        //noinspection ConstantConditions
        if (fluidInventory instanceof FluidTank) {
            data.setTag(FLUID_INVENTORY_TAG, ((FluidTank) fluidInventory).writeToNBT(new NBTTagCompound()));
        }

        return data;
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound data) {
        super.readFromNBT(data);

        if (fluidInventory instanceof FluidTank) {
            ((FluidTank) fluidInventory).readFromNBT(data.getCompoundTag(FLUID_INVENTORY_TAG));
        }
    }

    @Nonnull
    public FluidTank getDefaultFluidInventory(@Nonnull NBTTagCompound data) {
        return new FluidTank(Math.max(getMinimumFluidInventorySize(), data.getCompoundTag(FLUID_INVENTORY_TAG).getInteger("Amount")));
    }

    public int getMinimumFluidInventorySize() {
        return 0;
    }

    @Override
    public void onTickResetChecks(long timer, boolean isServerSide) {
        super.onTickResetChecks(timer, isServerSide);
        fluidInventoryChanged = false;
    }

    /**
     * Override this if the MTE will keep its Fluid inventory on-break.
     * If this is overridden to return True, you MUST take care to handle
     * the Fluids in the MTE's inventory otherwise they will be voided on break.
     *
     * @return True if MTE fluid inventory is kept as an ItemStack, false otherwise
     */
    public boolean keepsFluidInventory() {
        return false;
    }

    @Override
    public void initFromNBT(NBTTagCompound compound, ResourceLocation multiTileEntityId, short itemStackMeta) {
        super.initFromNBT(compound, multiTileEntityId, itemStackMeta);
        if (fluidInventory instanceof FluidTank && itemStack.hasKey(FluidHandlerItemStack.FLUID_NBT_KEY, Constants.NBT.TAG_COMPOUND)) {
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(itemStack.getCompoundTag(FluidHandlerItemStack.FLUID_NBT_KEY));
            ((FluidTank) fluidInventory).setFluid(fluidStack);
        }
    }

    @Override
    public NBTTagCompound writeItemNBT(NBTTagCompound nbtTagCompound) {
        if (fluidInventory instanceof FluidTank) {
            FluidStack fluidStack = ((FluidTank) fluidInventory).getFluid();
            if (fluidStack != null && fluidStack.amount > 0) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                fluidStack.writeToNBT(tagCompound);
                itemStack.setTag(FluidHandlerItemStack.FLUID_NBT_KEY, tagCompound);
            }
            return nbtTagCompound;
        }
        return nbtTagCompound;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        updateFluidInventory();
    }

    public void updateFluidInventory() {
        fluidInventoryChanged = true;
    }

    public IFluidHandler getFluidInventory() {
        return this.fluidInventory;
    }

    public void setFluidInventory(IFluidHandler handler) {
        this.fluidInventory = handler;
    }

    public static void clearInventory(@Nonnull NonNullList<FluidStack> fluidBuffer, @Nonnull IFluidHandler fluidInventory) {
        fluidBuffer.add(fluidInventory.drain(Integer.MAX_VALUE, true));
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        T result = super.getCapability(capability, facing);
        if (result != null) return result;

        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && getFluidInventory().getTankProperties().length > 0) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getFluidInventory());
        }
        return null;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        if (super.hasCapability(capability, facing))
            return true;
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && getFluidInventory().getTankProperties().length > 0;
    }
}
