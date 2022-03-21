package gregtech.api.util;

import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.cover.ICoverable;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.recipes.FluidKey;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class GTFluidUtils {

    public static int transferFluids(@Nonnull IFluidHandler sourceHandler, @Nonnull IFluidHandler destHandler, int transferLimit) {
        return transferFluids(sourceHandler, destHandler, transferLimit, fluidStack -> true);
    }

    public static int transferFluids(@Nonnull IFluidHandler sourceHandler, @Nonnull IFluidHandler destHandler, int transferLimit, @Nonnull Predicate<FluidStack> fluidFilter) {
        int fluidLeftToTransfer = transferLimit;

        for (IFluidTankProperties tankProperties : sourceHandler.getTankProperties()) {
            FluidStack currentFluid = tankProperties.getContents();
            if (currentFluid == null || currentFluid.amount == 0 || !fluidFilter.test(currentFluid)) {
                continue;
            }

            currentFluid.amount = fluidLeftToTransfer;
            FluidStack fluidStack = sourceHandler.drain(currentFluid, false);
            if (fluidStack == null || fluidStack.amount == 0) {
                continue;
            }

            int canInsertAmount = destHandler.fill(fluidStack, false);
            if (canInsertAmount > 0) {
                fluidStack.amount = canInsertAmount;
                fluidStack = sourceHandler.drain(fluidStack, true);
                if (fluidStack != null && fluidStack.amount > 0) {
                    destHandler.fill(fluidStack, true);

                    fluidLeftToTransfer -= fluidStack.amount;
                    if (fluidLeftToTransfer == 0) {
                        break;
                    }
                }
            }
        }
        return transferLimit - fluidLeftToTransfer;
    }

    public static boolean transferExactFluidStack(@Nonnull IFluidHandler sourceHandler, @Nonnull IFluidHandler destHandler, FluidStack fluidStack) {
        int amount = fluidStack.amount;
        FluidStack sourceFluid = sourceHandler.drain(fluidStack, false);
        if (sourceFluid == null || sourceFluid.amount != amount) {
            return false;
        }
        int canInsertAmount = destHandler.fill(sourceFluid, false);
        if (canInsertAmount == amount) {
            sourceFluid = sourceHandler.drain(sourceFluid, true);
            if (sourceFluid != null && sourceFluid.amount > 0) {
                destHandler.fill(sourceFluid, true);
                return true;
            }
        }
        return false;
    }

    /**
     * Simulates the insertion of fluid into a target fluid handler, then optionally performs the insertion.
     * <br /><br />
     * Simulating will not modify any of the input parameters. Insertion will either succeed completely, or fail
     * without modifying anything.
     * This method should be called with {@code simulate} {@code true} first, then {@code simulate} {@code false},
     * only if it returned {@code true}.
     *
     * @param fluidHandler the target inventory
     * @param simulate     whether to simulate ({@code true}) or actually perform the insertion ({@code false})
     * @param fluidStacks  the items to insert into {@code fluidHandler}.
     * @return {@code true} if the insertion succeeded, {@code false} otherwise.
     */
    public static boolean addFluidsToFluidHandler(IMultipleTankHandler fluidHandler,
                                                  boolean simulate,
                                                  List<FluidStack> fluidStacks) {
        if (simulate) {
            OverlayedFluidHandler overlayedFluidHandler = new OverlayedFluidHandler(fluidHandler);
            Map<FluidKey, Integer> fluidKeyMap = GTHashMaps.fromFluidCollection(fluidStacks);
            for (Map.Entry<FluidKey, Integer> entry : fluidKeyMap.entrySet()) {
                int amountToInsert = entry.getValue();
                int inserted = overlayedFluidHandler.insertStackedFluidKey(entry.getKey(), amountToInsert);
                if (inserted != amountToInsert) {
                    return false;
                }
            }
            return true;
        }

        fluidStacks.forEach(fluidStack -> fluidHandler.fill(fluidStack, true));
        return true;
    }

    public static boolean fillTankFromContainer(IItemHandlerModifiable importItems, IItemHandlerModifiable exportItems, int inputSlot, int outputSlot, FluidTankList tanks) {
        ItemStack inputContainerStack = importItems.extractItem(inputSlot, 1, true);
        FluidActionResult result = FluidUtil.tryEmptyContainer(inputContainerStack, tanks, Integer.MAX_VALUE, null, false);
        if (result.isSuccess()) {
            ItemStack remainingItem = result.getResult();
            if (ItemStack.areItemStacksEqual(inputContainerStack, remainingItem))
                return false; //do not fill if item stacks match
            if (!remainingItem.isEmpty() && !exportItems.insertItem(outputSlot, remainingItem, true).isEmpty())
                return false; //do not fill if can't put remaining item
            FluidUtil.tryEmptyContainer(inputContainerStack, tanks, Integer.MAX_VALUE, null, true);
            importItems.extractItem(inputSlot, 1, false);
            exportItems.insertItem(outputSlot, remainingItem, false);
            return true;
        }
        return false;
    }

    public static boolean fillContainerFromTank(IItemHandlerModifiable importItems, IItemHandlerModifiable exportItems, int inputSlot, int outputSlot, FluidTankList tanks) {
        ItemStack emptyContainer = importItems.extractItem(inputSlot, 1, true);
        FluidActionResult result = FluidUtil.tryFillContainer(emptyContainer, tanks, Integer.MAX_VALUE, null, false);
        if (result.isSuccess()) {
            ItemStack remainingItem = result.getResult();
            if (!remainingItem.isEmpty() && !exportItems.insertItem(outputSlot, remainingItem, true).isEmpty())
                return false;
            FluidUtil.tryFillContainer(emptyContainer, tanks, Integer.MAX_VALUE, null, true);
            importItems.extractItem(inputSlot, 1, false);
            exportItems.insertItem(outputSlot, remainingItem, false);
            return true;
        }
        return false;
    }

    // TODO Drop this down to an iface (ICoverable?)
    public static void pushFluidsIntoNearbyHandlers(MetaTileEntity mte, EnumFacing... allowedFaces) {
        BlockPos.PooledMutableBlockPos blockPos = BlockPos.PooledMutableBlockPos.retain();
        for (EnumFacing nearbyFacing : allowedFaces) {
            blockPos.setPos(mte.getPos()).move(nearbyFacing);
            TileEntity tileEntity = mte.getWorld().getTileEntity(blockPos);
            if (tileEntity == null) {
                continue;
            }
            IFluidHandler fluidHandler = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, nearbyFacing.getOpposite());
            //use getCoverCapability so fluid tank index filtering and fluid filtering covers will work properly
            IFluidHandler myFluidHandler = mte.getCoverCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, nearbyFacing);
            if (fluidHandler == null || myFluidHandler == null) {
                continue;
            }
            GTFluidUtils.transferFluids(myFluidHandler, fluidHandler, Integer.MAX_VALUE);
        }
        blockPos.release();
    }

    // TODO Drop this down to an iface (ICoverable?)
    public static void pullFluidsFromNearbyHandlers(MetaTileEntity mte, EnumFacing... allowedFaces) {
        BlockPos.PooledMutableBlockPos blockPos = BlockPos.PooledMutableBlockPos.retain();
        for (EnumFacing nearbyFacing : allowedFaces) {
            blockPos.setPos(mte.getPos()).move(nearbyFacing);
            TileEntity tileEntity = mte.getWorld().getTileEntity(blockPos);
            if (tileEntity == null) {
                continue;
            }
            IFluidHandler fluidHandler = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, nearbyFacing.getOpposite());
            //use getCoverCapability so fluid tank index filtering and fluid filtering covers will work properly
            IFluidHandler myFluidHandler = mte.getCoverCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, nearbyFacing);
            if (fluidHandler == null || myFluidHandler == null) {
                continue;
            }
            GTFluidUtils.transferFluids(fluidHandler, myFluidHandler, Integer.MAX_VALUE);
        }
        blockPos.release();
    }
}
