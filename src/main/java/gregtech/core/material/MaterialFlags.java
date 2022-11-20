package gregtech.core.material;

import gregtech.api.material.IMaterialFlag;
import gregtech.core.material.internal.MaterialFlag;

public class MaterialFlags {

    /////////////////
    //   GENERIC   //
    /////////////////

    /**
     * Add to material to disable it's unification fully
     */
    public static final IMaterialFlag NO_UNIFICATION = new MaterialFlag.Builder("no_unification").build();

    /**
     * Enables electrolyzer decomposition recipe generation
     */
    public static final IMaterialFlag DECOMPOSITION_BY_ELECTROLYZING = new MaterialFlag.Builder("decomposition_by_electrolyzing").build();

    /**
     * Enables centrifuge decomposition recipe generation
     */
    public static final IMaterialFlag DECOMPOSITION_BY_CENTRIFUGING = new MaterialFlag.Builder("decomposition_by_centrifuging").build();

    /**
     * Disables decomposition recipe generation for this material
     */
    public static final IMaterialFlag DISABLE_DECOMPOSITION = new MaterialFlag.Builder("disable_decomposition").build();

    /**
     * Add to material if it is some kind of explosive
     */
    public static final IMaterialFlag EXPLOSIVE = new MaterialFlag.Builder("explosive").build();

    /**
     * Add to material if it is some kind of flammable
     */
    public static final IMaterialFlag FLAMMABLE = new MaterialFlag.Builder("flammable").build();

    /**
     * Add to material if it is some kind of sticky
     */
    public static final IMaterialFlag STICKY = new MaterialFlag.Builder("sticky").build();

    //////////////////
    //     DUST     //
    //////////////////

    /**
     * Generate a plate for this material
     * If it's dust material, dust compressor recipe into plate will be generated
     * If it's metal material, bending machine recipes will be generated
     * If block is found, cutting machine recipe will be also generated
     */
    public static final IMaterialFlag GENERATE_PLATE = new MaterialFlag.Builder("generate_plate")
            .requireProps(MaterialProperties.DUST)
            .build();

    public static final IMaterialFlag GENERATE_ROD = new MaterialFlag.Builder("generate_rod")
            .requireProps(MaterialProperties.DUST)
            .build();

    public static final IMaterialFlag GENERATE_BOLT_SCREW = new MaterialFlag.Builder("generate_bolt_screw")
            .requireFlags(GENERATE_ROD)
            .requireProps(MaterialProperties.DUST)
            .build();

    public static final IMaterialFlag GENERATE_FRAME = new MaterialFlag.Builder("generate_frame")
            .requireFlags(GENERATE_ROD)
            .requireProps(MaterialProperties.DUST)
            .build();

    public static final IMaterialFlag GENERATE_GEAR = new MaterialFlag.Builder("generate_gear")
            .requireFlags(GENERATE_PLATE, GENERATE_ROD)
            .requireProps(MaterialProperties.DUST)
            .build();

    public static final IMaterialFlag GENERATE_LONG_ROD = new MaterialFlag.Builder("generate_long_rod")
            .requireFlags(GENERATE_ROD)
            .requireProps(MaterialProperties.DUST)
            .build();

    public static final IMaterialFlag FORCE_GENERATE_BLOCK = new MaterialFlag.Builder("force_generate_block")
            .requireProps(MaterialProperties.DUST)
            .build();

    /**
     * This will prevent material from creating Shapeless recipes for dust to block and vice versa
     * Also preventing extruding and alloy smelting recipes via SHAPE_EXTRUDING/MOLD_BLOCK
     */
    public static final IMaterialFlag EXCLUDE_BLOCK_CRAFTING_RECIPES = new MaterialFlag.Builder("exclude_block_crafting_recipes")
            .requireProps(MaterialProperties.DUST)
            .build();

    public static final IMaterialFlag EXCLUDE_PLATE_COMPRESSOR_RECIPE = new MaterialFlag.Builder("exclude_plate_compressor_recipe")
            .requireFlags(GENERATE_PLATE)
            .requireProps(MaterialProperties.DUST)
            .build();

    /**
     * This will prevent material from creating Shapeless recipes for dust to block and vice versa
     */
    public static final IMaterialFlag EXCLUDE_BLOCK_CRAFTING_BY_HAND_RECIPES = new MaterialFlag.Builder("exclude_block_crafting_by_hand_recipes")
            .requireProps(MaterialProperties.DUST)
            .build();

    public static final IMaterialFlag MORTAR_GRINDABLE = new MaterialFlag.Builder("mortar_grindable")
            .requireProps(MaterialProperties.DUST)
            .build();

    /**
     * Add to material if it cannot be worked by any other means, than smashing or smelting. This is used for coated Materials.
     */
    public static final IMaterialFlag NO_WORKING = new MaterialFlag.Builder("no_working")
            .requireProps(MaterialProperties.DUST)
            .build();

    /**
     * Add to material if it cannot be used for regular Metal working techniques since it is not possible to bend it.
     */
    public static final IMaterialFlag NO_SMASHING = new MaterialFlag.Builder("no_smashing")
            .requireProps(MaterialProperties.DUST)
            .build();

    /**
     * Add to material if it's impossible to smelt it
     */
    public static final IMaterialFlag NO_SMELTING = new MaterialFlag.Builder("no_smelting")
            .requireProps(MaterialProperties.DUST)
            .build();

    /**
     * Add this to your Material if you want to have its Ore Calcite heated in a Blast Furnace for more output. Already listed are:
     * Iron, Pyrite, PigIron, WroughtIron.
     */
    public static final IMaterialFlag BLAST_FURNACE_CALCITE_DOUBLE = new MaterialFlag.Builder("blast_furnace_calcite_double")
            .requireProps(MaterialProperties.DUST)
            .build();

    public static final IMaterialFlag BLAST_FURNACE_CALCITE_TRIPLE = new MaterialFlag.Builder("blast_furnace_calcite_triple")
            .requireProps(MaterialProperties.DUST)
            .build();

    /////////////////
    //    FLUID    //
    /////////////////

    public static final IMaterialFlag SOLDER_MATERIAL = new MaterialFlag.Builder("solder_material")
            .requireProps(MaterialProperties.FLUID)
            .build();

    public static final IMaterialFlag SOLDER_MATERIAL_BAD = new MaterialFlag.Builder("solder_material_bad")
            .requireProps(MaterialProperties.FLUID)
            .build();

    public static final IMaterialFlag SOLDER_MATERIAL_GOOD = new MaterialFlag.Builder("solder_material_good")
            .requireProps(MaterialProperties.FLUID)
            .build();

    /////////////////
    //    INGOT    //
    /////////////////

    public static final IMaterialFlag GENERATE_FOIL = new MaterialFlag.Builder("generate_foil")
            .requireFlags(GENERATE_PLATE)
            .requireProps(MaterialProperties.INGOT)
            .build();

    public static final IMaterialFlag GENERATE_RING = new MaterialFlag.Builder("generate_ring")
            .requireFlags(GENERATE_ROD)
            .requireProps(MaterialProperties.INGOT)
            .build();

    public static final IMaterialFlag GENERATE_SPRING = new MaterialFlag.Builder("generate_spring")
            .requireFlags(GENERATE_LONG_ROD)
            .requireProps(MaterialProperties.INGOT)
            .build();

    public static final IMaterialFlag GENERATE_SPRING_SMALL = new MaterialFlag.Builder("generate_spring_small")
            .requireFlags(GENERATE_ROD)
            .requireProps(MaterialProperties.INGOT)
            .build();

    public static final IMaterialFlag GENERATE_SMALL_GEAR = new MaterialFlag.Builder("generate_small_gear")
            .requireFlags(GENERATE_PLATE, GENERATE_ROD)
            .requireProps(MaterialProperties.INGOT)
            .build();

    public static final IMaterialFlag GENERATE_FINE_WIRE = new MaterialFlag.Builder("generate_fine_wire")
            .requireFlags(GENERATE_FOIL)
            .requireProps(MaterialProperties.INGOT)
            .build();

    public static final IMaterialFlag GENERATE_ROTOR = new MaterialFlag.Builder("generate_rotor")
            .requireFlags(GENERATE_BOLT_SCREW, GENERATE_RING, GENERATE_PLATE)
            .requireProps(MaterialProperties.INGOT)
            .build();

    public static final IMaterialFlag GENERATE_DENSE = new MaterialFlag.Builder("generate_dense")
            .requireFlags(GENERATE_PLATE)
            .requireProps(MaterialProperties.INGOT)
            .build();

    public static final IMaterialFlag GENERATE_ROUND = new MaterialFlag.Builder("generate_round")
            .requireProps(MaterialProperties.INGOT)
            .build();

    /**
     * Add this to your Material if it is a magnetized form of another Material.
     */
    public static final IMaterialFlag IS_MAGNETIC = new MaterialFlag.Builder("is_magnetic")
            .requireProps(MaterialProperties.INGOT)
            .build();

    /////////////////
    //     GEM     //
    /////////////////

    /**
     * If this material can be crystallized.
     */
    public static final IMaterialFlag CRYSTALLIZABLE = new MaterialFlag.Builder("crystallizable")
            .requireProps(MaterialProperties.GEM)
            .build();

    public static final IMaterialFlag GENERATE_LENS = new MaterialFlag.Builder("generate_lens")
            .requireFlags(GENERATE_PLATE)
            .requireProps(MaterialProperties.GEM)
            .build();

    /////////////////
    //     ORE     //
    /////////////////

    public static final IMaterialFlag HIGH_SIFTER_OUTPUT = new MaterialFlag.Builder("high_sifter_output")
            .requireProps(MaterialProperties.GEM, MaterialProperties.ORE)
            .build();
}
