package gregtech.material;

import gregtech.api.events.MaterialEvent;
import gregtech.api.modules.GregTechModule;
import gregtech.apiOld.GTValues;
import gregtech.modules.BaseGregTechModule;
import gregtech.modules.GregTechModules;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

@GregTechModule(
        moduleID = GregTechModules.MODULE_MATERIALS,
        containerID = GTValues.MODID,
        name = "GregTech Materials",
        descriptionKey = "gregtech.module.materials.description"
)
public class MaterialsModule extends BaseGregTechModule {

    public static final Logger logger = LogManager.getLogger("GregTech Materials");

    @Nonnull
    @Override
    public Logger getLogger() {
        return logger;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void registerMaterials(MaterialEvent event) {
        if (event.stage == MaterialEvent.Stage.LOADING) {
            Materials.register();
        }
    }
}
