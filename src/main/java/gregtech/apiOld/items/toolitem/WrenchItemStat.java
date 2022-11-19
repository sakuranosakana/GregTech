package gregtech.apiOld.items.toolitem;

import gregtech.apiOld.capability.GregtechCapabilities;
import gregtech.apiOld.capability.tool.IWrenchItem;
import gregtech.apiOld.items.metaitem.stats.IItemCapabilityProvider;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class WrenchItemStat implements IItemCapabilityProvider {

    @Override
    public ICapabilityProvider createProvider(ItemStack itemStack) {
        return new CapabilityProvider(itemStack);
    }

    private static class CapabilityProvider extends AbstractToolItemCapabilityProvider<IWrenchItem> implements IWrenchItem {

        public CapabilityProvider(ItemStack itemStack) {
            super(itemStack);
        }

        @Override
        protected Capability<IWrenchItem> getCapability() {
            return GregtechCapabilities.CAPABILITY_WRENCH;
        }
    }
}
