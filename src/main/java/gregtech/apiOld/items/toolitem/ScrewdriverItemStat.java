package gregtech.apiOld.items.toolitem;

import gregtech.apiOld.capability.GregtechCapabilities;
import gregtech.apiOld.capability.tool.IScrewdriverItem;
import gregtech.apiOld.items.metaitem.stats.IItemCapabilityProvider;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ScrewdriverItemStat implements IItemCapabilityProvider {

    @Override
    public ICapabilityProvider createProvider(ItemStack itemStack) {
        return new CapabilityProvider(itemStack);
    }

    private static class CapabilityProvider extends AbstractToolItemCapabilityProvider<IScrewdriverItem> implements IScrewdriverItem {

        public CapabilityProvider(ItemStack itemStack) {
            super(itemStack);
        }

        @Override
        protected Capability<IScrewdriverItem> getCapability() {
            return GregtechCapabilities.CAPABILITY_SCREWDRIVER;
        }
    }
}
