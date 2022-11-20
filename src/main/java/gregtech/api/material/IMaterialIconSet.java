package gregtech.api.material;

import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.gregtech.material.MaterialIconSet")
@ZenRegister
public interface IMaterialIconSet {

    @ZenGetter("name")
    String getName();

    @ZenMethod("get")
    IMaterialIconSet getByName(String name);

    @ZenMethod
    String toString();
}
