package gregtech.api.tileentity.base;

import gregtech.api.GTValues;
import gregtech.api.capability.impl.FluidHandlerProxy;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.capability.impl.ItemHandlerProxy;
import gregtech.api.capability.impl.NotifiableItemStackHandler;
import gregtech.api.util.GTUtility;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public abstract class TileEntityCombinedInventoryIO extends TileEntityCombinedInventory {

    public static final String INVENTORY_INPUTS_TAG = "ImportInventory";
    public static final String INVENTORY_OUTPUTS_TAG = "ExportInventory";
    public static final String INVENTORY_FLUID_INPUTS_TAG = "ImportFluidInventory";
    public static final String INVENTORY_FLUID_OUTPUTS_TAG = "ExportFluidInventory";

    protected IItemHandlerModifiable importItemInventory;
    protected IItemHandlerModifiable exportItemInventory;
    protected FluidTankList importFluidInventory;
    protected FluidTankList exportFluidInventory;

    @Override
    protected void initializeInventory() {
        this.importItemInventory = new NotifiableItemStackHandler(getMinimumItemInventorySize(), this, false);
        this.exportItemInventory = new NotifiableItemStackHandler(getMinimumItemInventorySize(), this, true);
        this.itemInventory = new ItemHandlerProxy(importItemInventory, exportItemInventory);

        this.importFluidInventory = new FluidTankList(false);
        this.exportFluidInventory = new FluidTankList(false);
        this.fluidInventory = new FluidHandlerProxy(importFluidInventory, exportFluidInventory);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound data) {
        super.writeToNBT(data);
        GTUtility.readItems(importItemInventory, INVENTORY_INPUTS_TAG, data);
        GTUtility.readItems(exportItemInventory, INVENTORY_OUTPUTS_TAG, data);
        data.setTag(INVENTORY_FLUID_INPUTS_TAG, importFluidInventory.serializeNBT());
        data.setTag(INVENTORY_FLUID_OUTPUTS_TAG, exportFluidInventory.serializeNBT());
        return data;
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound data) {
        super.readFromNBT(data);
        GTUtility.readItems(importItemInventory, INVENTORY_INPUTS_TAG, data);
        GTUtility.readItems(exportItemInventory, INVENTORY_OUTPUTS_TAG, data);
        importFluidInventory.deserializeNBT(data.getCompoundTag(INVENTORY_FLUID_INPUTS_TAG));
        exportFluidInventory.deserializeNBT(data.getCompoundTag(INVENTORY_FLUID_OUTPUTS_TAG));
    }

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
        for (int i = 0; i < importItemInventory.getSlots(); i++) {
            if (doExplosionsVoidItems() && GTValues.RNG.nextInt(3) != 0) {
                importItemInventory.setStackInSlot(i, ItemStack.EMPTY);
            }
        }
        for (int i = 0; i < exportItemInventory.getSlots(); i++) {
            if (doExplosionsVoidItems() && GTValues.RNG.nextInt(3) != 0) {
                exportItemInventory.setStackInSlot(i, ItemStack.EMPTY);
            }
        }
        setToAir();
    }
}
