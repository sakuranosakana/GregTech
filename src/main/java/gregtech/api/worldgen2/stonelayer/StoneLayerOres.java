package gregtech.api.worldgen2.stonelayer;

import gregtech.api.GTValues;
import gregtech.api.unification.material.Material;
import gregtech.api.util.IBlockOre;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Random;
import java.util.Set;

public class StoneLayerOres {

    public int minY;
    public int maxY;

    @Nullable
    public Material material;
    @Nullable
    public IBlockState blockState;

    /**
     * The Material Amount will determine the chance in the form of an X of M Chance.
     */
    public long chance;

    public Set<String> targetBiomes = new ObjectOpenHashSet<>();

    @SafeVarargs
    public StoneLayerOres(@Nullable Material material, long chance, int minY, int maxY, @Nonnull Collection<String>... biomes) {
        this(material, chance, minY, maxY, null, biomes);
    }

    @SafeVarargs
    public StoneLayerOres(@Nullable Material material, long chance, int minY, int maxY, @Nullable IBlockState blockState, @Nonnull Collection<String>... biomes) {
        this.material = material;
        this.chance = Math.max(1, Math.min(chance, GTValues.M));
        this.blockState = blockState;
        for (Collection<String> biome : biomes) {
            targetBiomes.addAll(biome);
        }
        if (minY > maxY) {
            this.minY = maxY;
            this.maxY = minY;
        } else {
            this.minY = minY;
            this.maxY = maxY;
        }
    }

    public boolean check(StoneLayer layer, World world, @Nonnull BlockPos pos, String biome, int randomNumber) {
        return pos.getY() >= minY && pos.getY() <= maxY && randomNumber < chance && (targetBiomes.isEmpty() || targetBiomes.contains(biome));
    }

    public boolean check(StoneLayer layer, World world, @Nonnull BlockPos pos, String biome, Random random) {
        return pos.getY() >= minY && pos.getY() <= maxY && random.nextInt((int) GTValues.M) < chance && (targetBiomes.isEmpty() || targetBiomes.contains(biome));
    }

    public boolean check(StoneLayer layer, World world, @Nonnull BlockPos pos, String biome) {
        return pos.getY() >= minY && pos.getY() <= maxY && GTValues.RNG.nextInt((int) GTValues.M) < chance && (targetBiomes.isEmpty() || targetBiomes.contains(biome));
    }

    public boolean set(StoneLayer layer, World world, BlockPos pos, String biome, Random random) {
        if (blockState != null) return world.setBlockState(pos, blockState, 2);
        return pos.getY() == minY || pos.getY() == maxY || random.nextBoolean() ? small(layer, world, pos, biome) : normal(layer, world, pos, biome);
    }

    public boolean set(StoneLayer layer, World world, BlockPos pos, String biome) {
        if (blockState != null) return world.setBlockState(pos, blockState, 2);
        return pos.getY() == minY || pos.getY() == maxY || GTValues.RNG.nextBoolean() ? small(layer, world, pos, biome) : normal(layer, world, pos, biome);
    }

    public boolean normal(StoneLayer layer, World world, BlockPos pos, String biome) {
        if (blockState != null) return world.setBlockState(pos, blockState, 2);
        return layer.stoneType != null && material != null && IBlockOre.placeOreBlock(world, pos, material, layer.stoneType);
    }

    public boolean small(StoneLayer layer, World world, BlockPos pos, String biome) {
        if (blockState != null) return world.setBlockState(pos, blockState, 2);
        return layer.stoneType != null && material != null && IBlockOre.placeOreBlock(world, pos, material, layer.stoneType, true); //TODO small ore
    }
}
