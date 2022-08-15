package gregtech.api.worldgen2.builder;

import com.google.common.primitives.Ints;
import gregtech.api.GTValues;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.ore.StoneTypes;
import gregtech.api.worldgen.config.OreConfigUtils;
import gregtech.api.worldgen2.generator.IWorldgenObject;
import gregtech.api.worldgen2.generator.WorldgenMixed;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class MixedOreVeinBuilder implements IHeightOreGenBuilder<MixedOreVeinBuilder>,
        IIndicatorOreGenBuilder<MixedOreVeinBuilder>,
        IScaleOreGenBuilder<MixedOreVeinBuilder> {

    private final String name;
    private final String modid;
    private final boolean isDefault;

    private int minY = 30;
    private int maxY = 70;
    private int weight = 1;
    private int density = 1;
    private int distance = 0;
    private int size = 1;

    private final Map<IBlockState, Integer> ores = new Object2IntOpenHashMap<>();
    private Material indicator = null;
    private IBlockState indicatorState = null;

    /**
     * Create a new Layered Vein Builder with this vein set as a mod default
     *
     * @param name the name of this vein
     * @return a new vein builder
     */
    @Nonnull
    public static MixedOreVeinBuilder builder(@Nonnull String name) {
        return builder(name, true);
    }

    /**
     * Create a new Layered Vein Builder
     *
     * @param name      the name of this vein
     * @param isDefault true if this vein is provided by a mod by default, otherwise false
     * @return a new vein builder
     */
    @Nonnull
    public static MixedOreVeinBuilder builder(@Nonnull String name, boolean isDefault) {
        ModContainer container = Loader.instance().activeModContainer();
        return new MixedOreVeinBuilder(name, container == null ? GTValues.MODID : container.getModId().toLowerCase(), isDefault); //TODO automatically grab other modids
    }

    private MixedOreVeinBuilder(@Nonnull String name, @Nonnull String modid, boolean isDefault) {
        this.name = name;
        this.modid = modid;
        this.isDefault = isDefault;
    }

    /**
     * Set the y range of this vein
     * @param min the minimum y value
     * @param max the maximum y value
     * @return this
     */
    @Override
    public MixedOreVeinBuilder yRange(int min, int max) {
        this.minY = min;
        this.maxY = max;
        return this;
    }

    /**
     * Set the weight of this vein
     * @param weight the weighting of this vein.
     * @return this
     */
    @Override
    public MixedOreVeinBuilder weight(int weight) {
        this.weight = weight;
        return this;
    }

    /**
     * Set the density of this vein
     * @param density the density of this vein.
     * @return this
     */
    @Override
    public MixedOreVeinBuilder density(int density) {
        this.density = density;
        return this;
    }

    /**
     * Set the minimum distance away from spawn this vein must be for generation
     * Using 0 means there is no minimum
     *
     * @param distance the distance
     * @return this
     */
    @Override
    public MixedOreVeinBuilder distance(int distance) {
        this.distance = distance;
        return this;
    }

    /**
     * Set the size of the vein
     * @param size the size
     * @return this
     */
    @Override
    public MixedOreVeinBuilder size(int size) {
        this.size = size;
        return this;
    }

    /**
     * Add an ore to this vein
     * @param state the blockstate to add
     * @param weight the weight of this blockstate
     * @return this
     */
    public MixedOreVeinBuilder ore(@Nonnull IBlockState state, int weight) {
        this.ores.put(state, weight);
        return this;
    }

    /**
     * Add an ore to this vein
     * @param material the material whose ore block to add
     * @param weight the weight of this blockstate
     * @return this
     */
    public MixedOreVeinBuilder ore(@Nonnull Material material, int weight) {
        this.ores.put(OreConfigUtils.getOreForMaterial(material).get(StoneTypes.STONE), weight);
        return this;
    }

    /**
     * Set the indicator material of the vein for surface rocks
     * @param indicator the indicator material
     * @return this
     */
    @Override
    public MixedOreVeinBuilder indicator(@Nullable Material indicator) {
        this.indicator = indicator;
        return this;
    }

    /**
     * Set the indicator {@link IBlockState} of the vein for surface indicators
     * Cannot be used in conjunction with {@link MixedOreVeinBuilder#indicator(Material)}
     *
     * @param indicator the indicator block state
     * @return this
     */
    @Override
    public MixedOreVeinBuilder indicator(@Nullable IBlockState indicator) {
        this.indicatorState = indicator;
        return this;
    }

    /**
     * Build a new {@link WorldgenMixed} and add it to the generation lists
     * @param generationLists the list of {@link IWorldgenObject}s to add this vein to
     */
    @Override
    @Nonnull
    public final WorldgenMixed build(@Nonnull List<List<IWorldgenObject>> generationLists) {
            return new WorldgenMixed(name, modid, isDefault, ores.keySet().toArray(new IBlockState[0]),
                    Ints.toArray(ores.values()),
                    minY, maxY, weight, density, distance, size,
                    indicator, indicatorState, generationLists);
    }
}
