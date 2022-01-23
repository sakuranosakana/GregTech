package gregtech.integration.screeninfo.top;

import gregtech.integration.screeninfo.ICapabilityProvider;
import gregtech.integration.screeninfo.provider.DebugPipeNetInfoProvider;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.ITheOneProbe;

public class GTTopPlugin {

    private static CapabilityInfoProvider PROBE_PROVIDER;

    public static void register(ICapabilityProvider<?> provider) {
        if (PROBE_PROVIDER == null) {
            PROBE_PROVIDER = new CapabilityInfoProvider();

            ITheOneProbe oneProbe = TheOneProbe.theOneProbeImp;
            oneProbe.registerProvider(PROBE_PROVIDER);

            // Done only in TOP since it is only used in-dev
            oneProbe.registerProvider(new DebugPipeNetInfoProvider());
        }
        PROBE_PROVIDER.addProvider(provider);
    }
}
