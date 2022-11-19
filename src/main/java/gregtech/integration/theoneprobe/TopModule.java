package gregtech.integration.theoneprobe;

import gregtech.apiOld.GTValues;
import gregtech.api.modules.GregTechModule;
import gregtech.api.modules.IGregTechModule;
import gregtech.integration.theoneprobe.provider.*;
import gregtech.modules.GregTechModules;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.ITheOneProbe;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Set;

@GregTechModule(
        moduleID = GregTechModules.MODULE_TOP,
        containerID = GTValues.MODID,
        name = "GregTech Integration: TOP",
        descriptionKey = "gregtech.module.top.description"
)
public class TopModule implements IGregTechModule {

    private final Logger logger = LogManager.getLogger("GregTech Integration: TOP");

    @Nonnull
    @Override
    public Logger getLogger() {
        return logger;
    }

    @Nonnull
    @Override
    public Set<String> getModDependencyIDs() {
        return Collections.singleton(GTValues.MODID_TOP);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        ITheOneProbe oneProbe = TheOneProbe.theOneProbeImp;
        oneProbe.registerProvider(new ElectricContainerInfoProvider());
        oneProbe.registerProvider(new FuelableInfoProvider());
        oneProbe.registerProvider(new WorkableInfoProvider());
        oneProbe.registerProvider(new ControllableInfoProvider());
        oneProbe.registerProvider(new DebugPipeNetInfoProvider());
        oneProbe.registerProvider(new TransformerInfoProvider());
        oneProbe.registerProvider(new DiodeInfoProvider());
        oneProbe.registerProvider(new MultiblockInfoProvider());
        oneProbe.registerProvider(new MaintenanceInfoProvider());
        oneProbe.registerProvider(new MultiRecipeMapInfoProvider());
        oneProbe.registerProvider(new ConverterInfoProvider());
        oneProbe.registerProvider(new RecipeLogicInfoProvider());
        oneProbe.registerProvider(new PrimitivePumpInfoProvider());
        oneProbe.registerProvider(new CoverProvider());
    }
}
