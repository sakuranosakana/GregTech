package gregtech.integration.screeninfo.waila;

import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.pipenet.tile.TileEntityPipeBase;
import gregtech.api.util.GTLog;
import gregtech.integration.screeninfo.ICapabilityProvider;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

@WailaPlugin
public class GTWailaPlugin implements IWailaPlugin {

    private static final Map<Class<?>, CapabilityDataProvider> wailaProviders = new HashMap<>();

    @Override
    public void register(IWailaRegistrar registrar) {
        CapabilityDataProvider metaTileEntityProvider = new CapabilityDataProvider();
        registrar.registerBodyProvider(metaTileEntityProvider, MetaTileEntityHolder.class);
        wailaProviders.put(MetaTileEntityHolder.class, metaTileEntityProvider);

        CapabilityDataProvider tileEntityPipeBaseProvider = new CapabilityDataProvider();
        registrar.registerBodyProvider(tileEntityPipeBaseProvider, TileEntityPipeBase.class);
        wailaProviders.put(TileEntityPipeBase.class, tileEntityPipeBaseProvider);
    }

    public static void register(@Nonnull ICapabilityProvider<?> provider) {
        if (wailaProviders.containsKey(provider.getTileEntityClass())) {
            wailaProviders.get(provider.getTileEntityClass()).addProvider(provider);
        } else {
            GTLog.logger.error("Waila capability provider failed to register!");
            GTLog.logger.error("Invalid TileEntity class {} for Capability {}",
                    provider.getTileEntityClass().toGenericString(),
                    provider.getCapability().getClass().toGenericString());
        }
    }
}
