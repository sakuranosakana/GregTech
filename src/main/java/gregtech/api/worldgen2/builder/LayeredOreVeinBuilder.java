package gregtech.api.worldgen2.builder;

import gregtech.api.GTValues;
import gregtech.api.unification.material.Material;
import gregtech.api.worldgen2.generator.IWorldgenObject;
import gregtech.api.worldgen2.generator.WorldgenOresLayered;
import net.minecraft.block.state.IBlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class LayeredOreVeinBuilder {

    private final String name;
    private final String modid;
    private final boolean isDefault;

    private int minY = 30;
    private int maxY = 70;
    private int weight = 1;
    private int density = 1;
    private int distance = 0;
    private int size = 1;

    private Material top = null;
    private int topHeight = 4;
    private Material bottom = null;
    private int bottomHeight = 3;
    private Material between = null;
    private int betweenHeight = 3;
    private Material spread = null;
    private Material indicator = null;
    private IBlockState indicatorState = null;

    /**
     * Create a new Layered Vein Builder with this vein set as a mod default
     *
     * @param name the name of this vein
     * @return a new vein builder
     */
    @Nonnull
    public static LayeredOreVeinBuilder builder(@Nonnull String name) {
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
    public static LayeredOreVeinBuilder builder(@Nonnull String name, boolean isDefault) {
        return new LayeredOreVeinBuilder(name, GTValues.MODID, isDefault); //TODO automatically grab other modids
    }

    private LayeredOreVeinBuilder(@Nonnull String name, @Nonnull String modid, boolean isDefault) {
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
    public LayeredOreVeinBuilder yRange(int min, int max) {
        this.minY = min;
        this.maxY = max;
        return this;
    }

    /**
     * Set the weight of this vein
     * @param weight the weighting of this vein.
     * @return this
     */
    public LayeredOreVeinBuilder weight(int weight) {
        this.weight = weight;
        return this;
    }

    /**
     * Set the density of this vein
     * @param density the density of this vein.
     * @return this
     */
    public LayeredOreVeinBuilder density(int density) {
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
    public LayeredOreVeinBuilder distance(int distance) {
        this.distance = distance;
        return this;
    }

    /**
     * Set the size of the vein
     * @param size the size
     * @return this
     */
    public LayeredOreVeinBuilder size(int size) {
        this.size = size;
        return this;
    }

    /**
     * Set the top material of the vein
     * @param top the top material
     * @return this
     */
    public LayeredOreVeinBuilder top(@Nullable Material top) {
        return top(top, 4);
    }

    /**
     * Set the top material of the vein
     * @param top the top material
     * @param height the amount of y levels the top material is present from the top
     * @return this
     */
    public LayeredOreVeinBuilder top(@Nullable Material top, int height) {
        this.top = top;
        this.topHeight = height;
        return this;
    }

    /**
     * Set the bottom material of the vein
     * @param bottom the bottom material
     * @return this
     */
    public LayeredOreVeinBuilder bottom(@Nullable Material bottom) {
        return bottom(bottom, 3);
    }

    /**
     * Set the bottom material of the vein
     * @param bottom the bottom material
     * @param height the amount of y levels the bottom material is present from the bottom
     * @return this
     */
    public LayeredOreVeinBuilder bottom(@Nullable Material bottom, int height) {
        this.bottom = bottom;
        this.bottomHeight = height;
        return this;
    }

    /**
     * Set the between material of the vein
     * @param between the bottom material
     * @return this
     */
    public LayeredOreVeinBuilder between(@Nullable Material between) {
        return between(between, 3);
    }

    /**
     * Set the between material of the vein
     * @param between the between material
     * @param height the amount of y levels the between material is present from the middle of the top
     * @return this
     */
    public LayeredOreVeinBuilder between(@Nullable Material between, int height) {
        this.between = between;
        this.betweenHeight = height;
        return this;
    }

    /**
     * Set the spread material of the vein
     * @param spread the spread material
     * @return this
     */
    public LayeredOreVeinBuilder spread(@Nullable Material spread) {
        this.spread = spread;
        return this;
    }

    /**
     * Set the indicator material of the vein for surface rocks
     * @param indicator the indicator material
     * @return this
     */
    public LayeredOreVeinBuilder indicator(@Nullable Material indicator) {
        this.indicator = indicator;
        return this;
    }

    /**
     * Set the indicator {@link IBlockState} of the vein for surface indicators
     * Cannot be used in conjunction with {@link LayeredOreVeinBuilder#indicator(Material)}
     *
     * @param indicator the indicator block state
     * @return this
     */
    public LayeredOreVeinBuilder indicator(@Nullable IBlockState indicator) {
        this.indicatorState = indicator;
        return this;
    }

    /**
     * Build a new {@link WorldgenOresLayered} and add it to the generation lists
     * @param generationLists the list of {@link IWorldgenObject}s to add this vein to
     */
    @SafeVarargs
    public final void build(List<IWorldgenObject>... generationLists) {
            new WorldgenOresLayered(name, modid, isDefault, minY, maxY, weight, density, distance, size, top,
                    topHeight, bottom, bottomHeight, between, betweenHeight, spread,
                    indicator, indicatorState, generationLists);
    }
}
