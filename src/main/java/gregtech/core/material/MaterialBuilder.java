package gregtech.core.material;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import gregtech.api.material.IMaterialFlag;
import gregtech.core.material.internal.MaterialIconSet;
import gregtech.api.material.MaterialStack;
import gregtech.apiOld.fluids.fluidType.FluidType;
import gregtech.apiOld.fluids.fluidType.FluidTypes;
import gregtech.apiOld.unification.Element;
import gregtech.core.material.internal.Material;
import gregtech.core.material.internal.MaterialFlagStorage;
import gregtech.core.material.internal.MaterialInfo;
import gregtech.core.material.internal.MaterialPropertyStorage;
import gregtech.core.material.properties.*;
import net.minecraft.enchantment.Enchantment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MaterialBuilder {

    private final MaterialInfo materialInfo;
    private final MaterialPropertyStorage properties;
    private final MaterialFlagStorage flags;

    /*
     * The temporary list of components for this Material.
     */
    private List<MaterialStack> composition = new ArrayList<>();

    /*
     * Temporary value to use to determine how to calculate default RGB
     */
    private boolean averageRGB = false;

    /**
     * Constructs a {@link Material}. This Builder replaces the old constructors, and
     * no longer uses a class hierarchy, instead using a {@link MaterialPropertyStorage} system.
     *
     * @param id   The MetaItemSubID for this Material. Must be unique.
     * @param name The Name of this Material. Will be formatted as
     *             "material.<name>" for the Translation Key.
     * @since GTCEu 2.0.0
     */
    public MaterialBuilder(int id, String name) {
        if (name.charAt(name.length() - 1) == '_')
            throw new IllegalArgumentException("Material name cannot end with a '_'!");
        materialInfo = new MaterialInfo(id, name);
        properties = new MaterialPropertyStorage();
        flags = new MaterialFlagStorage();
    }

    /*
     * Material Types
     */

    /**
     * Add a {@link FluidProperty} to this Material.<br>
     * Will be created as a {@link FluidTypes#LIQUID}, without a Fluid Block.
     *
     * @throws IllegalArgumentException If a {@link FluidProperty} has already been added to this Material.
     */
    public MaterialBuilder fluid() {
        properties.ensureSet(MaterialProperties.FLUID);
        return this;
    }

    /**
     * Add a {@link FluidProperty} to this Material.<br>
     * Will be created without a Fluid Block.
     *
     * @param type The {@link FluidType} of this Material, either Fluid or Gas.
     * @throws IllegalArgumentException If a {@link FluidProperty} has already been added to this Material.
     */
    public MaterialBuilder fluid(FluidType type) {
        return fluid(type, false);
    }

    /**
     * Add a {@link FluidProperty} to this Material.
     *
     * @param type     The {@link FluidType} of this Material.
     * @param hasBlock If true, create a Fluid Block for this Material.
     * @throws IllegalArgumentException If a {@link FluidProperty} has already been added to this Material.
     */
    public MaterialBuilder fluid(FluidType type, boolean hasBlock) {
        properties.setProperty(MaterialProperties.FLUID, new FluidProperty(type, hasBlock));
        return this;
    }

    /**
     * Add a {@link PlasmaProperty} to this Material.<br>
     * Is not required to have a {@link FluidProperty}, and will not automatically apply one.
     *
     * @throws IllegalArgumentException If a {@link PlasmaProperty} has already been added to this Material.
     */
    public MaterialBuilder plasma() {
        properties.ensureSet(MaterialProperties.PLASMA);
        return this;
    }

    /**
     * Add a {@link DustProperty} to this Material.<br>
     * Will be created with a Harvest Level of 2 and no Burn Time (Furnace Fuel).
     *
     * @throws IllegalArgumentException If a {@link DustProperty} has already been added to this Material.
     */
    public MaterialBuilder dust() {
        properties.ensureSet(MaterialProperties.DUST);
        return this;
    }

    /**
     * Add a {@link DustProperty} to this Material.<br>
     * Will be created with no Burn Time (Furnace Fuel).
     *
     * @param harvestLevel The Harvest Level of this block for Mining.<br>
     *                     If this Material also has a {@link ToolProperty}, this value will
     *                     also be used to determine the tool's Mining Level.
     * @throws IllegalArgumentException If a {@link DustProperty} has already been added to this Material.
     */
    public MaterialBuilder dust(int harvestLevel) {
        return dust(harvestLevel, 0);
    }

    /**
     * Add a {@link DustProperty} to this Material.
     *
     * @param harvestLevel The Harvest Level of this block for Mining.<br>
     *                     If this Material also has a {@link ToolProperty}, this value will
     *                     also be used to determine the tool's Mining Level.
     * @param burnTime     The Burn Time (in ticks) of this Material as a Furnace Fuel.
     * @throws IllegalArgumentException If a {@link DustProperty} has already been added to this Material.
     */
    public MaterialBuilder dust(int harvestLevel, int burnTime) {
        properties.setProperty(MaterialProperties.DUST, new DustProperty(harvestLevel, burnTime));
        return this;
    }

    /**
     * Add an {@link IngotProperty} to this Material.<br>
     * Will be created with a Harvest Level of 2 and no Burn Time (Furnace Fuel).<br>
     * Will automatically add a {@link DustProperty} to this Material if it does not already have one.
     *
     * @throws IllegalArgumentException If an {@link IngotProperty} has already been added to this Material.
     */
    public MaterialBuilder ingot() {
        properties.ensureSet(MaterialProperties.INGOT);
        return this;
    }

    /**
     * Add an {@link IngotProperty} to this Material.<br>
     * Will be created with no Burn Time (Furnace Fuel).<br>
     * Will automatically add a {@link DustProperty} to this Material if it does not already have one.
     *
     * @param harvestLevel The Harvest Level of this block for Mining. 2 will make it require a iron tool.<br>
     *                     If this Material also has a {@link ToolProperty}, this value will
     *                     also be used to determine the tool's Mining level (-1). So 2 will make the tool harvest diamonds.<br>
     *                     If this Material already had a Harvest Level defined, it will be overridden.
     * @throws IllegalArgumentException If an {@link IngotProperty} has already been added to this Material.
     */
    public MaterialBuilder ingot(int harvestLevel) {
        return ingot(harvestLevel, 0);
    }

    /**
     * Add an {@link IngotProperty} to this Material.<br>
     * Will automatically add a {@link DustProperty} to this Material if it does not already have one.
     *
     * @param harvestLevel The Harvest Level of this block for Mining. 2 will make it require a iron tool.<br>
     *                     If this Material also has a {@link ToolProperty}, this value will
     *                     also be used to determine the tool's Mining level (-1). So 2 will make the tool harvest diamonds.<br>
     *                     If this Material already had a Harvest Level defined, it will be overridden.
     * @param burnTime     The Burn Time (in ticks) of this Material as a Furnace Fuel.<br>
     *                     If this Material already had a Burn Time defined, it will be overridden.
     * @throws IllegalArgumentException If an {@link IngotProperty} has already been added to this Material.
     */
    public MaterialBuilder ingot(int harvestLevel, int burnTime) {
        DustProperty prop = properties.getProperty(MaterialProperties.DUST);
        if (prop == null) dust(harvestLevel, burnTime);
        else {
            if (prop.getHarvestLevel() == 2) prop.setHarvestLevel(harvestLevel);
            if (prop.getBurnTime() == 0) prop.setBurnTime(burnTime);
        }
        properties.ensureSet(MaterialProperties.INGOT);
        return this;
    }

    /**
     * Add a {@link GemProperty} to this Material.<br>
     * Will be created with a Harvest Level of 2 and no Burn Time (Furnace Fuel).<br>
     * Will automatically add a {@link DustProperty} to this Material if it does not already have one.
     *
     * @throws IllegalArgumentException If a {@link GemProperty} has already been added to this Material.
     */
    public MaterialBuilder gem() {
        properties.ensureSet(MaterialProperties.GEM);
        return this;
    }

    /**
     * Add a {@link GemProperty} to this Material.<br>
     * Will be created with no Burn Time (Furnace Fuel).<br>
     * Will automatically add a {@link DustProperty} to this Material if it does not already have one.
     *
     * @param harvestLevel The Harvest Level of this block for Mining.<br>
     *                     If this Material also has a {@link ToolProperty}, this value will
     *                     also be used to determine the tool's Mining level.<br>
     *                     If this Material already had a Harvest Level defined, it will be overridden.
     * @throws IllegalArgumentException If a {@link GemProperty} has already been added to this Material.
     */
    public MaterialBuilder gem(int harvestLevel) {
        return gem(harvestLevel, 0);
    }

    /**
     * Add a {@link GemProperty} to this Material.<br>
     * Will automatically add a {@link DustProperty} to this Material if it does not already have one.
     *
     * @param harvestLevel The Harvest Level of this block for Mining.<br>
     *                     If this Material also has a {@link ToolProperty}, this value will
     *                     also be used to determine the tool's Mining level.<br>
     *                     If this Material already had a Harvest Level defined, it will be overridden.
     * @param burnTime     The Burn Time (in ticks) of this Material as a Furnace Fuel.<br>
     *                     If this Material already had a Burn Time defined, it will be overridden.
     */
    public MaterialBuilder gem(int harvestLevel, int burnTime) {
        DustProperty prop = properties.getProperty(MaterialProperties.DUST);
        if (prop == null) dust(harvestLevel, burnTime);
        else {
            if (prop.getHarvestLevel() == 2) prop.setHarvestLevel(harvestLevel);
            if (prop.getBurnTime() == 0) prop.setBurnTime(burnTime);
        }
        properties.ensureSet(MaterialProperties.GEM);
        return this;
    }

    public MaterialBuilder burnTime(int burnTime) {
        DustProperty prop = properties.getProperty(MaterialProperties.DUST);
        if (prop == null) {
            dust();
            prop = properties.getProperty(MaterialProperties.DUST);
        }
        prop.setBurnTime(burnTime);
        return this;
    }

    /**
     * Set the Color of this Material.<br>
     * Defaults to 0xFFFFFF unless {@link MaterialBuilder#colorAverage()} was called, where
     * it will be a weighted average of the components of the Material.
     *
     * @param color The RGB-formatted Color.
     */
    public MaterialBuilder color(int color) {
        color(color, true);
        return this;
    }

    /**
     * Set the Color of this Material.<br>
     * Defaults to 0xFFFFFF unless {@link MaterialBuilder#colorAverage()} was called, where
     * it will be a weighted average of the components of the Material.
     *
     * @param color         The RGB-formatted Color.
     * @param hasFluidColor Whether the fluid should be colored or not.
     */
    public MaterialBuilder color(int color, boolean hasFluidColor) {
        this.materialInfo.color = color;
        this.materialInfo.hasFluidColor = hasFluidColor;
        return this;
    }

    public MaterialBuilder colorAverage() {
        this.averageRGB = true;
        return this;
    }

    /**
     * Set the {@link MaterialIconSet} of this Material.<br>
     * Defaults vary depending on if the Material has a:<br>
     * <ul>
     * <li> {@link GemProperty}, it will default to {@link gregtech.core.material.MaterialIconSets#GEM_VERTICAL}
     * <li> {@link IngotProperty} or {@link DustProperty}, it will default to {@link gregtech.core.material.MaterialIconSets#DULL}
     * <li> {@link FluidProperty}, it will default to either {@link gregtech.core.material.MaterialIconSets#FLUID}
     *      or {@link gregtech.core.material.MaterialIconSets#GAS}, depending on the {@link FluidType}
     * <li> {@link PlasmaProperty}, it will default to {@link gregtech.core.material.MaterialIconSets#FLUID}
     * </ul>
     * Default will be determined by first-found Property in this order, unless specified.
     *
     * @param iconSet The {@link MaterialIconSet} of this Material.
     */
    public MaterialBuilder iconSet(MaterialIconSet iconSet) {
        materialInfo.iconSet = iconSet;
        return this;
    }

    public MaterialBuilder components(Object... components) {
        Preconditions.checkArgument(
                components.length % 2 == 0,
                "Material Components list malformed!"
        );

        for (int i = 0; i < components.length; i += 2) {
            if (components[i] == null) {
                throw new IllegalArgumentException("Material in Components List is null for Material "
                        + this.materialInfo.name);
            }
            composition.add(new MaterialStack(
                    (Material) components[i],
                    (Integer) components[i + 1]
            ));
        }
        return this;
    }

    public MaterialBuilder components(ImmutableList<MaterialStack> components) {
        composition = components;
        return this;
    }

    /**
     * Add {@link MaterialFlags} to this Material.<br>
     * Dependent Flags (for example, {@link MaterialFlags#GENERATE_LONG_ROD} requiring
     * {@link MaterialFlags#GENERATE_ROD}) will be automatically applied.
     */
    public MaterialBuilder flags(IMaterialFlag... flags) {
        this.flags.addFlags(flags);
        return this;
    }

    /**
     * Add {@link MaterialFlags} to this Material.<br>
     * Dependent Flags (for example, {@link MaterialFlags#GENERATE_LONG_ROD} requiring
     * {@link MaterialFlags#GENERATE_ROD}) will be automatically applied.
     *
     * @param f1 A {@link Collection} of {@link IMaterialFlag}. Provided this way for easy Flag presets to be applied.
     * @param f2 An Array of {@link IMaterialFlag}. If no {@link Collection} is required, use {@link MaterialBuilder#flags(IMaterialFlag...)}.
     */
    public MaterialBuilder flags(Collection<IMaterialFlag> f1, IMaterialFlag... f2) {
        this.flags.addFlags(f1.toArray(new IMaterialFlag[0]));
        this.flags.addFlags(f2);
        return this;
    }

    public MaterialBuilder element(Element element) {
        this.materialInfo.element = element;
        return this;
    }

    public MaterialBuilder toolStats(float speed, float damage, int durability, int enchantability) {
        return toolStats(speed, damage, durability, enchantability, false);
    }

    public MaterialBuilder toolStats(float speed, float damage, int durability, int enchantability, boolean ignoreCraftingTools) {
        properties.setProperty(MaterialProperties.TOOL, new ToolProperty(speed, damage, durability, enchantability, ignoreCraftingTools));
        return this;
    }

    public MaterialBuilder blastTemp(int temp) {
        properties.setProperty(MaterialProperties.BLAST, new BlastProperty(temp));
        return this;
    }

    public MaterialBuilder blastTemp(int temp, BlastProperty.GasTier gasTier) {
        properties.setProperty(MaterialProperties.BLAST, new BlastProperty(temp, gasTier, -1, -1));
        return this;
    }

    public MaterialBuilder blastTemp(int temp, BlastProperty.GasTier gasTier, int eutOverride) {
        properties.setProperty(MaterialProperties.BLAST, new BlastProperty(temp, gasTier, eutOverride, -1));
        return this;
    }

    public MaterialBuilder blastTemp(int temp, BlastProperty.GasTier gasTier, int eutOverride, int durationOverride) {
        properties.setProperty(MaterialProperties.BLAST, new BlastProperty(temp, gasTier, eutOverride, durationOverride));
        return this;
    }

    public MaterialBuilder ore() {
        properties.ensureSet(MaterialProperties.ORE);
        return this;
    }

    public MaterialBuilder ore(boolean emissive) {
        properties.setProperty(MaterialProperties.ORE, new OreProperty(1, 1, emissive));
        return this;
    }

    public MaterialBuilder ore(int oreMultiplier, int byproductMultiplier) {
        properties.setProperty(MaterialProperties.ORE, new OreProperty(oreMultiplier, byproductMultiplier));
        return this;
    }

    public MaterialBuilder ore(int oreMultiplier, int byproductMultiplier, boolean emissive) {
        properties.setProperty(MaterialProperties.ORE, new OreProperty(oreMultiplier, byproductMultiplier, emissive));
        return this;
    }

    public MaterialBuilder fluidTemp(int temp) {
        properties.ensureSet(MaterialProperties.FLUID);
        properties.getProperty(MaterialProperties.FLUID).setFluidTemperature(temp);
        return this;
    }

    public MaterialBuilder washedIn(Material m) {
        properties.ensureSet(MaterialProperties.ORE);
        properties.getProperty(MaterialProperties.ORE).setWashedIn(m);
        return this;
    }

    public MaterialBuilder washedIn(Material m, int washedAmount) {
        properties.ensureSet(MaterialProperties.ORE);
        properties.getProperty(MaterialProperties.ORE).setWashedIn(m, washedAmount);
        return this;
    }

    public MaterialBuilder separatedInto(Material... m) {
        properties.ensureSet(MaterialProperties.ORE);
        properties.getProperty(MaterialProperties.ORE).setSeparatedInto(m);
        return this;
    }

    public MaterialBuilder oreSmeltInto(Material m) {
        properties.ensureSet(MaterialProperties.ORE);
        properties.getProperty(MaterialProperties.ORE).setDirectSmeltResult(m);
        return this;
    }

    public MaterialBuilder polarizesInto(Material m) {
        properties.ensureSet(MaterialProperties.INGOT);
        properties.getProperty(MaterialProperties.INGOT).setMagneticMaterial(m);
        return this;
    }

    public MaterialBuilder arcSmeltInto(Material m) {
        properties.ensureSet(MaterialProperties.INGOT);
        properties.getProperty(MaterialProperties.INGOT).setArcSmeltingInto(m);
        return this;
    }

    public MaterialBuilder macerateInto(Material m) {
        properties.ensureSet(MaterialProperties.INGOT);
        properties.getProperty(MaterialProperties.INGOT).setMacerateInto(m);
        return this;
    }

    public MaterialBuilder ingotSmeltInto(Material m) {
        properties.ensureSet(MaterialProperties.INGOT);
        properties.getProperty(MaterialProperties.INGOT).setSmeltingInto(m);
        return this;
    }

    public MaterialBuilder addOreByproducts(Material... byproducts) {
        properties.ensureSet(MaterialProperties.ORE);
        properties.getProperty(MaterialProperties.ORE).setOreByProducts(byproducts);
        return this;
    }

    public MaterialBuilder cableProperties(long voltage, int amperage, int loss) {
        cableProperties((int) voltage, amperage, loss, false);
        return this;
    }

    public MaterialBuilder cableProperties(long voltage, int amperage, int loss, boolean isSuperCon) {
        properties.ensureSet(MaterialProperties.DUST);
        properties.setProperty(MaterialProperties.WIRE, new WireProperties((int) voltage, amperage, loss, isSuperCon));
        return this;
    }

    public MaterialBuilder cableProperties(long voltage, int amperage, int loss, boolean isSuperCon, int criticalTemperature) {
        properties.ensureSet(MaterialProperties.DUST);
        properties.setProperty(MaterialProperties.WIRE, new WireProperties((int) voltage, amperage, loss, isSuperCon, criticalTemperature));
        return this;
    }

    public MaterialBuilder fluidPipeProperties(int maxTemp, int throughput, boolean gasProof) {
        return fluidPipeProperties(maxTemp, throughput, gasProof, false, false, false);
    }

    public MaterialBuilder fluidPipeProperties(int maxTemp, int throughput, boolean gasProof, boolean acidProof, boolean cryoProof, boolean plasmaProof) {
        properties.ensureSet(MaterialProperties.INGOT);
        properties.setProperty(MaterialProperties.FLUID_PIPE, new FluidPipeProperties(maxTemp, throughput, gasProof, acidProof, cryoProof, plasmaProof));
        return this;
    }

    public MaterialBuilder itemPipeProperties(int priority, float stacksPerSec) {
        properties.ensureSet(MaterialProperties.INGOT);
        properties.setProperty(MaterialProperties.ITEM_PIPE, new ItemPipeProperties(priority, stacksPerSec));
        return this;
    }

    public MaterialBuilder addDefaultEnchant(Enchantment enchant, int level) {
        if (!properties.hasProperty(MaterialProperties.TOOL)) // cannot assign default here
            throw new IllegalArgumentException("Material cannot have an Enchant without Tools!");
        properties.getProperty(MaterialProperties.TOOL).addEnchantmentForTools(enchant, level);
        return this;
    }

    public Material build() {
        materialInfo.componentList = ImmutableList.copyOf(composition);
        materialInfo.verifyInfo(properties, averageRGB);
        return new Material(materialInfo, properties, flags);
    }
}
