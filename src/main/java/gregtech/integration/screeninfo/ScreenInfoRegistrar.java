package gregtech.integration.screeninfo;

import gregtech.api.GTValues;
import gregtech.integration.screeninfo.provider.*;
import gregtech.integration.screeninfo.top.GTTopPlugin;
import gregtech.integration.screeninfo.waila.GTWailaPlugin;

public class ScreenInfoRegistrar {

    public static void registerProviders() {
        register(new ElectricContainerInfoProvider());
        register(new FuelableInfoProvider());
        register(new WorkableInfoProvider());
        register(new ControllableInfoProvider());
        register(new TransformerInfoProvider());
        register(new DiodeInfoProvider());
        register(new MultiblockInfoProvider());
        register(new MaintenanceInfoProvider());
        register(new MultiRecipeMapInfoProvider());
    }

    private static void register(ICapabilityProvider<?> provider) {
        if (GTValues.isModLoaded(GTValues.MODID_TOP)) {
            GTTopPlugin.register(provider);
        }
        if (GTValues.isModLoaded(GTValues.MODID_WAILA)) {
            GTWailaPlugin.register(provider);
        }
    }
}
