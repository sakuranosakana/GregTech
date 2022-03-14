package gregtech.api.tileentity.base;

import gregtech.api.capability.impl.FluidHandlerProxy;
import gregtech.api.capability.impl.FluidTankList;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public abstract class TileEntityFluidInventoryIO extends TileEntityFluidInventory {

    public static final String INVENTORY_FLUID_INPUTS_TAG = "ImportFluidInventory";
    public static final String INVENTORY_FLUID_OUTPUTS_TAG = "ExportFluidInventory";

    protected FluidTankList importFluidInventory;
    protected FluidTankList exportFluidInventory;

    @Override
    protected void initializeInventory() {
        this.importFluidInventory = new FluidTankList(false);
        this.exportFluidInventory = new FluidTankList(false);
        this.fluidInventory = new FluidHandlerProxy(importFluidInventory, exportFluidInventory);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound data) {
        super.writeToNBT(data);

        data.setTag(INVENTORY_FLUID_INPUTS_TAG, importFluidInventory.serializeNBT());
        data.setTag(INVENTORY_FLUID_OUTPUTS_TAG, exportFluidInventory.serializeNBT());
        return data;
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound data) {
        super.readFromNBT(data);

        importFluidInventory.deserializeNBT(data.getCompoundTag(INVENTORY_FLUID_INPUTS_TAG));
        exportFluidInventory.deserializeNBT(data.getCompoundTag(INVENTORY_FLUID_OUTPUTS_TAG));
    }
}
