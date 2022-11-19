package gregtech.apiOld.items.toolitem;

import gregtech.api.capability.GregtechCapabilities;
import gregtech.api.capability.tool.ISoftHammerItem;
import gregtech.apiOld.items.metaitem.stats.IItemCapabilityProvider;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class SoftMalletItemStat implements IItemCapabilityProvider {

    @Override
    public ICapabilityProvider createProvider(ItemStack itemStack) {
        return new CapabilityProvider(itemStack);
    }

    private static class CapabilityProvider extends AbstractToolItemCapabilityProvider<ISoftHammerItem> implements ISoftHammerItem {

        public CapabilityProvider(ItemStack itemStack) {
            super(itemStack);
        }

        @Override
        protected Capability<ISoftHammerItem> getCapability() {
            return GregtechCapabilities.CAPABILITY_MALLET;
        }
    }
}
