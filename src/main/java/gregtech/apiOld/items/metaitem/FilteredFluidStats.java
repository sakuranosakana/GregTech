package gregtech.apiOld.items.metaitem;

import gregtech.apiOld.capability.impl.GTFluidHandlerItemStack;
import gregtech.apiOld.capability.impl.GTSimpleFluidHandlerItemStack;
import gregtech.apiOld.items.metaitem.stats.IItemCapabilityProvider;
import gregtech.apiOld.items.metaitem.stats.IItemComponent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Function;

public class FilteredFluidStats implements IItemComponent, IItemCapabilityProvider {

    public final int capacity;
    public final boolean allowPartialFill;
    private final Function<FluidStack, Boolean> fillPredicate;

    public FilteredFluidStats(int capacity, boolean allowPartialFill, Function<FluidStack, Boolean> fillPredicate) {
        this.capacity = capacity;
        this.allowPartialFill = allowPartialFill;
        this.fillPredicate = fillPredicate;
    }

    @Override
    public ICapabilityProvider createProvider(ItemStack itemStack) {
        if (allowPartialFill) {
            return new GTFluidHandlerItemStack(itemStack, capacity) {
                @Override
                public boolean canFillFluidType(FluidStack fluid) {
                    return super.canFillFluidType(fluid) && fillPredicate.apply(fluid);
                }
            };
        }
        return new GTSimpleFluidHandlerItemStack(itemStack, capacity) {
            @Override
            public boolean canFillFluidType(FluidStack fluid) {
                return super.canFillFluidType(fluid) && fillPredicate.apply(fluid);
            }
        };
    }
}
