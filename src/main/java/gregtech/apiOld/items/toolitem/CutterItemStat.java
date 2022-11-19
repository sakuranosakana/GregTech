package gregtech.apiOld.items.toolitem;

import gregtech.api.capability.GregtechCapabilities;
import gregtech.apiOld.capability.tool.ICutterItem;
import gregtech.apiOld.items.metaitem.stats.IItemCapabilityProvider;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class CutterItemStat implements IItemCapabilityProvider {

    @Override
    public ICapabilityProvider createProvider(ItemStack itemStack) {
        return new CutterItemStat.CapabilityProvider(itemStack);
    }

    private static class CapabilityProvider extends AbstractToolItemCapabilityProvider<ICutterItem> implements ICutterItem {

        public CapabilityProvider(ItemStack itemStack) {
            super(itemStack);
        }

        @Override
        protected Capability<ICutterItem> getCapability() {
            return GregtechCapabilities.CAPABILITY_CUTTER;
        }
    }
}
