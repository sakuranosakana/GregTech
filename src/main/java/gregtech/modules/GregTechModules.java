package gregtech.modules;

import gregtech.api.modules.IModuleContainer;

public class GregTechModules implements IModuleContainer {

    public static final String MODULE_CORE = "core";

    @Override
    public String getID() {
        return "gregtech";
    }
}
