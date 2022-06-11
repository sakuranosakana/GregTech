package gregtech.api.worldgen2.stonelayer;

import com.google.common.collect.ImmutableSet;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.ore.StoneType;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class StoneLayer {

    /**
     * List of Stone and Ore Blocks, that can simply be replaced by the Stone Layers.
     */
    public static final Set<Block> REPLACEABLE_BLOCKS = new ObjectOpenHashSet<>();

    static {
        REPLACEABLE_BLOCKS.addAll(ImmutableSet.of(Blocks.STONE, Blocks.COAL_ORE, Blocks.IRON_ORE, Blocks.GOLD_ORE, Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE, Blocks.LAPIS_ORE, Blocks.REDSTONE_ORE, Blocks.LIT_REDSTONE_ORE));
    }

    /**
     * List of generateable Stone Layers, via ItemStack of the Stone Block, so that MetaData is usable.
     */
    public static final List<StoneLayer> LAYERS = new ObjectArrayList<>();

    /**
     * Slate Special Case.
     */
    public static StoneLayer SLATE = null;

    /**
     * Whenever two Rock Types hit each other in WorldGen an Ore from the returned List will spawn.
     * The first ones mentioned inside the List can override the chances for others by spawning before, so insert the lowest chances first and then the high chances.
     */
    public static final Map<Material, Map<Material, List<StoneLayerOres>>> MAP = new Object2ObjectOpenHashMap<>();

    /**
     * List of random Small Ore Materials that can generate between Layers.
     */
    public static final List<Material> RANDOM_SMALL_GEM_ORES = new ArrayList<>();

    public static Material top = null;
    public static Material bottom = null;
    public static List<StoneLayerOres> list = Collections.emptyList();

    public final IBlockState stone;
    public final IBlockState cobble;
    public final IBlockState mossy;

    public final Material material;

    public final ItemStack stack;

    public final List<StoneLayerOres> ores;

    public StoneType stoneType;

    public boolean useSlate = false;

    public StoneLayer(@Nullable StoneType stoneType, @Nonnull IBlockState stone, Material material, @Nonnull StoneLayerOres... oreChances) {
        this(stoneType, stone, stone, stone, material, oreChances);
    }

    public StoneLayer(@Nullable StoneType stoneType, @Nonnull IBlockState stone, IBlockState cobble, Material material, @Nonnull StoneLayerOres... oreChances) {
        this(stoneType, stone, cobble, cobble, material, oreChances);
    }

    public StoneLayer(@Nullable StoneType stoneType, @Nonnull IBlockState stone, IBlockState cobble, IBlockState mossy, Material material, @Nonnull StoneLayerOres... oreChances) {
        this.stoneType = stoneType;
        this.stone = stone;
        this.cobble = cobble;
        this.mossy = mossy;
        this.material = material;
        this.stack = new ItemStack(stone.getBlock(), 1, stone.getBlock().getMetaFromState(stone));
        this.ores = new ObjectArrayList<>();
        for (StoneLayerOres ore : oreChances) {
            if (ore != null) ores.add(ore);
        }
    }

    public StoneLayer setSlate() {
        this.useSlate = true;
        return this;
    }

    public static boolean bothSides(Material material1, Material material2, @Nonnull StoneLayerOres... oreChances) {
        return topBottom(material1, material2, oreChances) && topBottom(material2, material1, oreChances);
    }

    public static boolean topBottom(Material top, Material bottom, @Nonnull StoneLayerOres... oreChances) {
        if (oreChances.length <= 0) return false;
        Map<Material, List<StoneLayerOres>> map = MAP.get(top);
        if (map == null) MAP.put(top, map = new Object2ObjectOpenHashMap<>());
        List<StoneLayerOres> list = map.computeIfAbsent(bottom, k -> new ObjectArrayList<>(oreChances.length));
        for (StoneLayerOres ore : oreChances) {
            //TODO check worldgen jsons here
            if (ore != null && ore.material != null) list.add(ore);
        }
        return true;
    }

    public static List<StoneLayerOres> get(@Nonnull StoneLayer top, @Nonnull StoneLayer bottom) {
        return get(top.material, bottom.material);
    }

    public static List<StoneLayerOres> get(@Nonnull Material top, @Nonnull Material bottom) {
        if (top == StoneLayer.top && bottom == StoneLayer.bottom) return StoneLayer.list;
        StoneLayer.top = top;
        StoneLayer.bottom = bottom;

        Map<Material, List<StoneLayerOres>> map = MAP.get(top);
        if (map == null) return StoneLayer.list = Collections.emptyList();

        List<StoneLayerOres> list = map.get(bottom);
        if (list == null) return StoneLayer.list = Collections.emptyList();
        return StoneLayer.list = list;
    }
}
