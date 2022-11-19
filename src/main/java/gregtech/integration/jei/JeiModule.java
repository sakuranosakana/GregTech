package gregtech.integration.jei;

import gregtech.apiOld.GTValues;
import gregtech.apiOld.modules.GregTechModule;
import gregtech.apiOld.modules.IGregTechModule;
import gregtech.integration.jei.utils.MultiblockInfoRecipeFocusShower;
import gregtech.modules.GregTechModules;
import mezz.jei.Internal;
import mezz.jei.JustEnoughItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.input.IShowsRecipeFocuses;
import mezz.jei.input.InputHandler;
import mezz.jei.startup.ProxyCommonClient;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@GregTechModule(
        moduleID = GregTechModules.MODULE_JEI,
        containerID = GTValues.MODID,
        name = "GregTech Integration: JEI",
        descriptionKey = "gregtech.module.jei.description"
)
public class JeiModule implements IGregTechModule {

    private final Logger logger = LogManager.getLogger("GregTech Integration: JEI");

    @Nonnull
    @Override
    public Logger getLogger() {
        return logger;
    }

    @Nonnull
    @Override
    public Set<String> getModDependencyIDs() {
        return Collections.singleton(GTValues.MODID_JEI);
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        if (event.getSide() == Side.CLIENT) {
            registerPlugin();
        }
    }

    @Override
    public void loadComplete(FMLLoadCompleteEvent event) {
        if (event.getSide() == Side.CLIENT) {
            setupInputHandler();
        }
    }

    private void registerPlugin() {
        IModPlugin plugin = new GTJeiPlugin();
        List<IModPlugin> plugins = ObfuscationReflectionHelper.getPrivateValue(ProxyCommonClient.class, (ProxyCommonClient) JustEnoughItems.getProxy(), "plugins");
        plugins.add(plugin);
    }

    private void setupInputHandler() {
        try {
            Field inputHandlerField = Internal.class.getDeclaredField("inputHandler");
            inputHandlerField.setAccessible(true);
            InputHandler inputHandler = (InputHandler) inputHandlerField.get(null);
            List<IShowsRecipeFocuses> showsRecipeFocuses = ObfuscationReflectionHelper.getPrivateValue(InputHandler.class, inputHandler, "showsRecipeFocuses");

            showsRecipeFocuses.add(new MultiblockInfoRecipeFocusShower());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
