package gregtech.modules;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import gregtech.api.GTValues;
import gregtech.api.modules.GregTechModule;
import gregtech.api.modules.IGregTechModule;
import gregtech.api.modules.IModuleContainer;
import gregtech.api.modules.IModuleManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.*;

public class ModuleManager implements IModuleManager {

    public enum Stage {
        C_SETUP,         // Initializing Module Containers
        M_SETUP,         // Initializing Modules
        PRE_INIT,        // MC PreInitialization stage
        INIT,            // MC Initialization stage
        POST_INIT,       // MC PostInitialization stage
        FINISHED,        // MC LoadComplete stage
        SERVER_STARTING, // MC ServerStarting stage
        SERVER_STARTED   // MC ServerStarted stage
    }

    private static final ModuleManager INSTANCE = new ModuleManager();
    private static final String MODULE_CFG_FILE_NAME = "modules.cfg";
    private static final String MODULE_CFG_CATEGORY_NAME = "modules";
    private static File configFolder;

    private final Map<String, IModuleContainer> containers = new HashMap<>();
    private final Map<ResourceLocation, IGregTechModule> sortedModules = new LinkedHashMap<>();
    private final Set<IGregTechModule> loadedModules = new LinkedHashSet<>();

    private IModuleContainer currentContainer;

    private Stage currentStage = Stage.C_SETUP;
    private final Logger logger = LogManager.getLogger("GregTech Module Loader");
    private Configuration config;

    private ModuleManager() {
    }

    public static ModuleManager getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isModuleEnabled(ResourceLocation id) {
        return sortedModules.containsKey(id);
    }

    public boolean isModuleEnabled(IGregTechModule module) {
        GregTechModule annotation = module.getClass().getAnnotation(GregTechModule.class);
        String comment = getComment(module);
        Property prop = getConfiguration().get(MODULE_CFG_CATEGORY_NAME, annotation.containerID() + ":" + annotation.moduleID(), true, comment);
        return prop.getBoolean();
    }

    @Override
    public IModuleContainer getLoadedContainer() {
        return currentContainer;
    }

    @Override
    public void registerContainer(IModuleContainer container) {
        if (currentStage != Stage.C_SETUP) {
            logger.error("Failed to register module container {}, as module loading has already begun", container);
            return;
        }
        Preconditions.checkNotNull(container);
        containers.put(container.getID(), container);
    }

    public void setup(FMLPreInitializationEvent event) {
        currentStage = Stage.M_SETUP;
        configFolder = new File(event.getModConfigurationDirectory(), GTValues.MODID);
        Map<String, List<IGregTechModule>> modules = getModules(event.getAsmData());
        configureModules(modules);

        for (IGregTechModule module : loadedModules) {
            for (Class<?> clazz : module.getEventBusSubscribers()) {
                MinecraftForge.EVENT_BUS.register(clazz);
            }
        }
    }

    public void onPreInit(Side side) {
        currentStage = Stage.PRE_INIT;
        for (IGregTechModule module : loadedModules) {
            module.getLogger().debug("Pre-init start");
            currentContainer = containers.get(getContainerID(module));
            registerPackets(module, side);
            module.preInit();
            module.getLogger().debug("Pre-init complete");
        }
    }

    private void registerPackets(IGregTechModule module, Side side) {
        module.getLogger().debug("Registering packets");
        module.registerServerPackets();
        if (side == Side.CLIENT) {
            module.registerClientPackets();
        }
    }

    public void onInit() {
        currentStage = Stage.INIT;
        for (IGregTechModule module : loadedModules) {
            module.getLogger().debug("Init start");
            currentContainer = containers.get(getContainerID(module));
            module.init();
            module.getLogger().debug("Init complete");
        }
    }

    public void onPostInit() {
        currentStage = Stage.POST_INIT;
        for (IGregTechModule module : loadedModules) {
            module.getLogger().debug("Post-init start");
            currentContainer = containers.get(getContainerID(module));
            module.postInit();
            module.getLogger().debug("Post-init complete");
        }
    }

    public void onLoadComplete() {
        currentStage = Stage.FINISHED;
        for (IGregTechModule module : loadedModules) {
            module.getLogger().debug("Load-complete start");
            currentContainer = containers.get(getContainerID(module));
            module.loadComplete();
            module.getLogger().debug("Load-complete complete");
        }
    }

    public void onServerStarting() {
        currentStage = Stage.SERVER_STARTING;
        for (IGregTechModule module : loadedModules) {
            module.getLogger().debug("Server-starting start");
            module.serverStarting();
            module.getLogger().debug("Server-starting complete");
        }
    }

    public void onServerStarted() {
        currentStage = Stage.SERVER_STARTED;
        for (IGregTechModule module : loadedModules) {
            module.getLogger().debug("Server-started start");
            currentContainer = containers.get(getContainerID(module));
            module.serverStarted();
            module.getLogger().debug("Server-started complete");
        }
    }

    public void onServerStopped() {
        for (IGregTechModule module : loadedModules) {
            currentContainer = containers.get(getContainerID(module));
            module.serverStopped();
        }
    }

    public void processIMC(ImmutableList<FMLInterModComms.IMCMessage> messages) {
        for (FMLInterModComms.IMCMessage message : messages) {
            for (IGregTechModule module : loadedModules) {
                if (module.processIMC(message)) {
                    break;
                }
            }
        }
    }

    private void configureModules(Map<String, List<IGregTechModule>> modules) {
        Locale locale = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);
        Set<ResourceLocation> toLoad = new HashSet<>();
        Set<IGregTechModule> modulesToLoad = new HashSet<>();
        Configuration config = getConfiguration();

        for (IModuleContainer container : containers.values()) {
            String containerID = container.getID();
            List<IGregTechModule> containerModules = modules.get(containerID);
            config.load();
            config.addCustomCategoryComment(MODULE_CFG_CATEGORY_NAME,
                    "Module configuration file. Can individually enable/disable modules from GregTech and its addons");
            IGregTechModule coreModule = getCoreModule(containerModules);
            if (coreModule == null) {
                throw new IllegalStateException("Could not find core module for module container " + containerID);
            } else {
                containerModules.remove(coreModule);
                containerModules.add(0, coreModule);
            }

            // Remove disabled modules and gather potential modules to load
            Iterator<IGregTechModule> iterator = containerModules.iterator();
            while (iterator.hasNext()) {
                IGregTechModule module = iterator.next();
                if (!isModuleEnabled(module)) {
                    iterator.remove();
                }
                GregTechModule annotation = module.getClass().getAnnotation(GregTechModule.class);
                toLoad.add(new ResourceLocation(containerID, annotation.moduleID()));
                modulesToLoad.add(module);
            }
        }

        // Check any module dependencies
        Iterator<IGregTechModule> iterator;
        boolean changed;
        do {
            changed = false;
            iterator = modulesToLoad.iterator();
            while (iterator.hasNext()) {
                IGregTechModule module = iterator.next();

                // Check module dependencies
                Set<ResourceLocation> dependencies = module.getDependencyUids();
                if (!toLoad.containsAll(dependencies)) {
                    iterator.remove();
                    changed = true;
                    GregTechModule annotation = module.getClass().getAnnotation(GregTechModule.class);
                    String moduleID = annotation.moduleID();
                    toLoad.remove(new ResourceLocation(moduleID));
                    logger.info("Module {} is missing at least one of module dependencies: {}, skipping loading...", moduleID, dependencies);
                }

                // Check mod dependencies
                Set<String> modDependencies = module.getModDependencyIDs();
                for (String modid : modDependencies) {
                    if (!Loader.isModLoaded(modid)) {
                        iterator.remove();
                        changed = true;
                        GregTechModule annotation = module.getClass().getAnnotation(GregTechModule.class);
                        String moduleID = annotation.moduleID();
                        toLoad.remove(new ResourceLocation(moduleID));
                        logger.info("Module {} is missing at least one of mod dependencies: {}, skipping loading...", moduleID, modDependencies);
                    }
                }
            }
        } while (changed);

        // Sort modules
        do {
            changed = false;
            iterator = modulesToLoad.iterator();
            while (iterator.hasNext()) {
                IGregTechModule module = iterator.next();
                if (sortedModules.keySet().containsAll(module.getDependencyUids())) {
                    iterator.remove();
                    GregTechModule annotation = module.getClass().getAnnotation(GregTechModule.class);
                    sortedModules.put(new ResourceLocation(annotation.containerID(), annotation.moduleID()), module);
                    changed = true;
                    break;
                }
            }
        } while (changed);

        loadedModules.addAll(sortedModules.values());

        if (config.hasChanged()) {
            config.save();
        }
        Locale.setDefault(locale);
    }

    private static IGregTechModule getCoreModule(List<IGregTechModule> modules) {
        for (IGregTechModule module : modules) {
            GregTechModule annotation = module.getClass().getAnnotation(GregTechModule.class);
            if (annotation.coreModule()) {
                return module;
            }
        }
        return null;
    }

    private static String getContainerID(IGregTechModule module) {
        GregTechModule annotation = module.getClass().getAnnotation(GregTechModule.class);
        return annotation.containerID();
    }

    private Map<String, List<IGregTechModule>> getModules(ASMDataTable table) {
        List<IGregTechModule> instances = getInstances(table);
        Map<String, List<IGregTechModule>> modules = new LinkedHashMap<>();
        for (IGregTechModule module : instances) {
            GregTechModule info = module.getClass().getAnnotation(GregTechModule.class);
            modules.computeIfAbsent(info.containerID(), k -> new ArrayList<>()).add(module);
        }
        return modules;
    }

    private List<IGregTechModule> getInstances(ASMDataTable table) {
        Set<ASMDataTable.ASMData> dataSet = table.getAll(GregTechModule.class.getCanonicalName());
        List<IGregTechModule> instances = new ArrayList<>();
        for (ASMDataTable.ASMData data : dataSet) {
            try {
                Class<?> clazz = Class.forName(data.getClassName());
                instances.add((IGregTechModule) clazz.newInstance());
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                logger.error("Could not initialize module {}", data.getAnnotationInfo().get("moduleID"));
                e.printStackTrace();
            }
        }
        return instances;
    }

    @SuppressWarnings("deprecation")
    private String getComment(IGregTechModule module) {
        GregTechModule annotation = module.getClass().getAnnotation(GregTechModule.class);

        String comment = I18n.translateToLocal(annotation.descriptionKey());
        Set<ResourceLocation> dependencies = module.getDependencyUids();
        if (!dependencies.isEmpty()) {
            Iterator<ResourceLocation> iterator = dependencies.iterator();
            StringBuilder builder = new StringBuilder(comment);
            builder.append("\n");
            builder.append("Module Dependencies: [ ");
            builder.append(iterator.next());
            while (iterator.hasNext()) {
                builder.append(", ").append(iterator.next());
            }
            builder.append(" ]");
            comment = builder.toString();
        }
        Set<String> modDependencies = module.getModDependencyIDs();
        if (!modDependencies.isEmpty()) {
            Iterator<String> iterator = modDependencies.iterator();
            StringBuilder builder = new StringBuilder(comment);
            builder.append("\n");
            builder.append("Mod Dependencies: [ ");
            builder.append(iterator.next());
            while (iterator.hasNext()) {
                builder.append(", ").append(iterator.next());
            }
            builder.append(" ]");
            comment = builder.toString();
        }
        return comment;
    }

    private Configuration getConfiguration() {
        if (config == null) {
            config = new Configuration(new File(configFolder, MODULE_CFG_FILE_NAME));
        }
        return config;
    }
}
