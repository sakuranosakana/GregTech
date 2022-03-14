package gregtech.api.tileentity.base;

import gregtech.api.GTValues;
import gregtech.api.util.GTUtility;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static gregtech.api.multitileentity.IMultiTileEntity.IMTEBreakBlock;
import static gregtech.api.multitileentity.IMultiTileEntity.IMTEOnBlockExploded;

public abstract class TileEntityCombinedInventory extends TileEntityBaseCoverable implements IMTEOnBlockExploded, IMTEBreakBlock {

    public static final String ITEM_INVENTORY_TAG = "Inventory";
    public static final String FLUID_INVENTORY_TAG = "FluidInventory";

    protected IItemHandler itemInventory;
    protected IFluidHandler fluidInventory;

    public boolean itemInventoryChanged = false;
    public boolean fluidInventoryChanged = false;

    public TileEntityCombinedInventory() {
        super();
        initializeInventory();
    }

    protected void initializeInventory() {
        this.itemInventory = new ItemStackHandler(getMinimumItemInventorySize());
        this.fluidInventory = new FluidTank(getMinimumFluidInventorySize());
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound data) {
        super.writeToNBT(data);
        itemInventory = getDefaultItemInventory(data);
        GTUtility.writeItems(itemInventory, ITEM_INVENTORY_TAG, data);

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
        if (itemInventory instanceof IItemHandlerModifiable) {
            GTUtility.readItems((IItemHandlerModifiable) itemInventory, ITEM_INVENTORY_TAG, data);
        }
        if (fluidInventory instanceof FluidTank) {
            ((FluidTank) fluidInventory).readFromNBT(data.getCompoundTag(FLUID_INVENTORY_TAG));
        }
    }

    @Nonnull
    public ItemStackHandler getDefaultItemInventory(@Nonnull NBTTagCompound data) {
        return new ItemStackHandler(Math.max(getMinimumItemInventorySize(), data.getTagList(ITEM_INVENTORY_TAG, 0).tagList.size()));
    }

    @Nonnull
    public FluidTank getDefaultFluidInventory(@Nonnull NBTTagCompound data) {
        return new FluidTank(Math.max(getMinimumFluidInventorySize(), data.getCompoundTag(FLUID_INVENTORY_TAG).getInteger("Amount")));
    }

    public int getMinimumItemInventorySize() {
        return 0;
    }


    public int getMinimumFluidInventorySize() {
        return 0;
    }

    @Override
    public void onTickResetChecks(long timer, boolean isServerSide) {
        super.onTickResetChecks(timer, isServerSide);
        itemInventoryChanged = false;
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

    /**
     * Override this if the MTE will keep its Item inventory on-break.
     * If this is overridden to return True, you MUST take care to handle
     * the ItemStacks in the MTE's inventory otherwise they will be voided on break.
     *
     * @return True if MTE inventory is kept as an ItemStack, false otherwise
     */
    public boolean keepsInventory() {
        return false;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        super.getDrops(drops, world, pos, state, fortune);
        if (keepsInventory()) {
            for (int i = 0; i < itemInventory.getSlots(); i++) {
                ItemStack stack = itemInventory.getStackInSlot(i);
                if (!stack.isEmpty()) drops.add(stack);
            }
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        updateItemInventory();
        updateFluidInventory();
    }

    public void updateFluidInventory() {
        fluidInventoryChanged = true;
    }

    public void updateItemInventory() {
        itemInventoryChanged = true;
    }

    public boolean doExplosionsVoidItems() {
        return false;
    }

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
        if (itemInventory instanceof IItemHandlerModifiable) {
            for (int i = 0; i < itemInventory.getSlots(); i++) {
                if (doExplosionsVoidItems() && GTValues.RNG.nextInt(3) != 0) {
                    ((IItemHandlerModifiable) itemInventory).setStackInSlot(i, ItemStack.EMPTY);
                }
            }
        }
        setToAir();
    }

    public IItemHandler getItemInventory() {
        return this.itemInventory;
    }

    public void setItemInventory(IItemHandler handler) {
        this.itemInventory = handler;
    }

    public IFluidHandler getFluidInventory() {
        return this.fluidInventory;
    }

    public void setFluidInventory(IFluidHandler handler) {
        this.fluidInventory = handler;
    }

    public static void clearInventory(NonNullList<ItemStack> itemBuffer, @Nonnull IItemHandlerModifiable inventory) {
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stackInSlot = inventory.getStackInSlot(i);
            if (!stackInSlot.isEmpty()) {
                inventory.setStackInSlot(i, ItemStack.EMPTY);
                itemBuffer.add(stackInSlot);
            }
        }
    }

    public static void clearFluidInventory(@Nonnull NonNullList<FluidStack> fluidBuffer, @Nonnull IFluidHandler fluidInventory) {
        fluidBuffer.add(fluidInventory.drain(Integer.MAX_VALUE, true));
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        T result = super.getCapability(capability, facing);
        if (result != null) return result;
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && getItemInventory().getSlots() > 0) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getItemInventory());
        }
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && getFluidInventory().getTankProperties().length > 0) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getFluidInventory());
        }
        return null;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        if (super.hasCapability(capability, facing))
            return true;
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && getItemInventory().getSlots() > 0)
            return true;
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && getFluidInventory().getTankProperties().length > 0;
    }
}
