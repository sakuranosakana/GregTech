package gregtech.integration.screeninfo.waila;

import gregtech.integration.screeninfo.ICapabilityProvider;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CapabilityDataProvider implements IWailaDataProvider {

    private final List<ICapabilityProvider<?>> providers = new ArrayList<>();

    protected void addProvider(ICapabilityProvider<?> provider) {
        providers.add(provider);
    }

    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getTileEntity() != null) {
            providers.forEach(p -> testProvider(p, tooltip, accessor.getTileEntity(), accessor.getSide()));
        }
        return tooltip;
    }

    private <T> void testProvider(ICapabilityProvider<T> provider, List<String> tooltip, TileEntity tileEntity, EnumFacing sideHit) {
        Capability<T> capability = provider.getCapability();
        T tileCapability = tileEntity.getCapability(capability, null);
        if (tileCapability != null && provider.allowDisplaying(tileCapability)) {
            provider.addWailaInfo(tileCapability, tooltip, tileEntity, sideHit);
        }
    }
}
