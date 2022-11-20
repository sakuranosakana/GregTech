package gregtech.core.material;

import gregtech.api.material.IMaterialIconSet;
import gregtech.core.material.internal.MaterialIconSet;

public class MaterialIconSets {

    public static IMaterialIconSet getByName(String name) {
        return MaterialIconSet.g(name);
    }

    public static final IMaterialIconSet NONE = new MaterialIconSet("none");
    public static final IMaterialIconSet METALLIC = new MaterialIconSet("metallic");
    public static final IMaterialIconSet DULL = new MaterialIconSet("dull");
    public static final IMaterialIconSet MAGNETIC = new MaterialIconSet("magnetic");
    public static final IMaterialIconSet QUARTZ = new MaterialIconSet("quartz");
    public static final IMaterialIconSet DIAMOND = new MaterialIconSet("diamond");
    public static final IMaterialIconSet EMERALD = new MaterialIconSet("emerald");
    public static final IMaterialIconSet SHINY = new MaterialIconSet("shiny");
    public static final IMaterialIconSet ROUGH = new MaterialIconSet("rough");
    public static final IMaterialIconSet FINE = new MaterialIconSet("fine");
    public static final IMaterialIconSet SAND = new MaterialIconSet("sand");
    public static final IMaterialIconSet FLINT = new MaterialIconSet("flint");
    public static final IMaterialIconSet RUBY = new MaterialIconSet("ruby");
    public static final IMaterialIconSet LAPIS = new MaterialIconSet("lapis");
    public static final IMaterialIconSet FLUID = new MaterialIconSet("fluid");
    public static final IMaterialIconSet GAS = new MaterialIconSet("gas");
    public static final IMaterialIconSet LIGNITE = new MaterialIconSet("lignite");
    public static final IMaterialIconSet OPAL = new MaterialIconSet("opal");
    public static final IMaterialIconSet GLASS = new MaterialIconSet("glass");
    public static final IMaterialIconSet WOOD = new MaterialIconSet("wood");
    public static final IMaterialIconSet GEM_HORIZONTAL = new MaterialIconSet("gem_horizontal");
    public static final IMaterialIconSet GEM_VERTICAL = new MaterialIconSet("gem_vertical");
    public static final IMaterialIconSet PAPER = new MaterialIconSet("paper");
    public static final IMaterialIconSet NETHERSTAR = new MaterialIconSet("netherstar");
    public static final IMaterialIconSet BRIGHT = new MaterialIconSet("bright");
}
