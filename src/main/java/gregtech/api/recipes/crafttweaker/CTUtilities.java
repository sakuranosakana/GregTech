package gregtech.api.recipes.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import gregtech.api.GregTechAPI;
import gregtech.integration.jei.multiblock.MultiblockInfoCategory;
import gregtech.modules.GregTechModules;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.gregtech.general.utils")
@ZenRegister
public class CTUtilities {

    // TODO YEET

    @ZenMethod("RemoveMultiblockPreviewFromJei")
    public static void removeMulti(String name) {
        if (GregTechAPI.moduleManager.isModuleEnabled(GregTechModules.MODULE_JEI)) {
            MultiblockInfoCategory.REGISTER.removeIf(multi -> multi.metaTileEntityId.toString().equals(name));
        }
    }
}
