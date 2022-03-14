package gregtech.api.tileentity.base;

import gregtech.api.GTValues;
import gregtech.api.util.GTUtility;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

import static gregtech.api.multitileentity.IMultiTileEntity.IMTEBreakBlock;
import static gregtech.api.multitileentity.IMultiTileEntity.IMTEOnBlockExploded;

public abstract class TileEntityItemInventory extends TileEntityBaseCoverable implements IMTEOnBlockExploded, IMTEBreakBlock {

    public static final String INVENTORY_TAG = "Inventory";

    protected IItemHandler inventory;

    public boolean inventoryChanged = false;

    public TileEntityItemInventory() {
        super();
        initializeInventory();
    }

    protected void initializeInventory() {
        this.inventory = new ItemStackHandler(getMinimumInventorySize());
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound data) {
        super.writeToNBT(data);
        inventory = getDefaultInventory(data);
        GTUtility.writeItems(inventory, INVENTORY_TAG, data);

        return data;
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound data) {
        super.readFromNBT(data);
        if (inventory instanceof IItemHandlerModifiable) {
            GTUtility.readItems((IItemHandlerModifiable) inventory, INVENTORY_TAG, data);
        }
    }

    public ItemStackHandler getDefaultInventory(@Nonnull NBTTagCompound data) {
        return new ItemStackHandler(Math.max(getMinimumInventorySize(), data.getTagList(INVENTORY_TAG, 0).tagList.size()));
    }

    public int getMinimumInventorySize() {
        return 0;
    }

    @Override
    public void onTickResetChecks(long timer, boolean isServerSide) {
        super.onTickResetChecks(timer, isServerSide);
        inventoryChanged = false;
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
            for (int i = 0; i < inventory.getSlots(); i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (!stack.isEmpty()) drops.add(stack);
            }
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        updateInventory();
    }

    public void updateInventory() {
        inventoryChanged = true;
    }

    public boolean doExplosionsVoidItems() {
        return false;
    }

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
        if (inventory instanceof IItemHandlerModifiable) {
            for (int i = 0; i < inventory.getSlots(); i++) {
                if (doExplosionsVoidItems() && GTValues.RNG.nextInt(3) != 0) {
                    ((IItemHandlerModifiable) inventory).setStackInSlot(i, ItemStack.EMPTY);
                }
            }
        }
        setToAir();
    }

    public IItemHandler getInventory() {
        return this.inventory;
    }

    public void setInventory(IItemHandler handler) {
        this.inventory = handler;
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
}
