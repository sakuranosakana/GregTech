package gregtech.core.material;

import gregtech.api.material.PropertyKey;
import gregtech.core.material.properties.*;

public class MaterialProperties {

    public static final PropertyKey<BlastProperty> BLAST = new PropertyKey<>("blast", BlastProperty.class);
    public static final PropertyKey<DustProperty> DUST = new PropertyKey<>("dust", DustProperty.class);
    public static final PropertyKey<FluidPipeProperties> FLUID_PIPE = new PropertyKey<>("fluid_pipe", FluidPipeProperties.class);
    public static final PropertyKey<FluidProperty> FLUID = new PropertyKey<>("fluid", FluidProperty.class);
    public static final PropertyKey<GemProperty> GEM = new PropertyKey<>("gem", GemProperty.class);
    public static final PropertyKey<IngotProperty> INGOT = new PropertyKey<>("ingot", IngotProperty.class);
    public static final PropertyKey<ItemPipeProperties> ITEM_PIPE = new PropertyKey<>("item_pipe", ItemPipeProperties.class);
    public static final PropertyKey<OreProperty> ORE = new PropertyKey<>("ore", OreProperty.class);
    public static final PropertyKey<PlasmaProperty> PLASMA = new PropertyKey<>("plasma", PlasmaProperty.class);
    public static final PropertyKey<ToolProperty> TOOL = new PropertyKey<>("tool", ToolProperty.class);
    public static final PropertyKey<WireProperties> WIRE = new PropertyKey<>("wire", WireProperties.class);
}
