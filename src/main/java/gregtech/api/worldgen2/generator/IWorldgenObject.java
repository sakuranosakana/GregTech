package gregtech.api.worldgen2.generator;

import gregtech.api.worldgen2.Dimensions;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.Set;

public interface IWorldgenObject {

    /**
     * Generates this IWorldgenObject in world
     *
     * @param world      the world to generate this in
     * @param chunk      the chunk to generate this in
     * @param dimension  the dimension to generate this in
     * @param minX       the minimum X coordinate of where this should generate
     * @param maxX       the maximum X coordinate of where this should generate
     * @param minZ       the minimum Z coordinate of where this should generate
     * @param maxZ       the maximum Z coordinate of where this should generate
     * @param random     the random number generator to use
     * @param biomes     the biomes
     * @param biomeNames the names of the biomes
     * @return true if this successfully generated, otherwise false
     */
    default boolean generate(World world, Chunk chunk, int dimension, int minX, int maxX, int minZ, int maxZ, Random random, Biome[][] biomes, Set<String> biomeNames) {
        // insert your WorldGen Code here
        return false;
    }

    /**
     * Used to reset the WorldgenObject. Most objects will not need to implement anything with this
     *
     * @param world      the world to generate this in
     * @param chunk      the chunk to generate this in
     * @param dimension  the dimension to generate this in
     * @param minX       the minimum X coordinate of where this should generate
     * @param maxX       the maximum X coordinate of where this should generate
     * @param minZ       the minimum Z coordinate of where this should generate
     * @param maxZ       the maximum Z coordinate of where the should generate
     * @param random     the random number generator to use
     * @param biomes     the biomes
     * @param biomeNames the names of the biomes
     */
    default void reset(World world, Chunk chunk, int dimension, int minX, int maxX, int minZ, int maxZ, Random random, Biome[][] biomes, Set<String> biomeNames) {
        // nothing
    }

    /**
     * @param world     the world to check
     * @param dimension the dimension to check
     * @return true if generation is allowed, otherwise false
     */
    boolean isEnabled(World world, int dimension);

    /**
     * @param world the world to check
     * @param minX  the minimum x to check
     * @param maxX  the maximum x to check
     * @param minZ  the minimum z to check
     * @param maxZ  the maximum z to check
     * @return true if major world generation is happening, otherwise false
     */
    default boolean checkForMajorWorldgen(@Nonnull World world, int minX, int maxX, int minZ, int maxZ) {
        if (world.provider.getDimension() == Dimensions.OVERWORLD_ID) {
            //TODO future GT6 world generation options
//            if (ConfigHolder.worldgen.generateStreets) {
//                if (Math.abs(minX) < 64 || Math.abs(maxX) < 64 || Math.abs(minZ) < 64 || Math.abs(maxZ) < 64) {
//                    return true;
//                }
//            }
//            if (ConfigHolder.worldgen.generateBiomes) {
//                return minX >= -96 && minX <= 80 && minZ >= -96 && minZ <= 80;
//            }
        }
        return false;
    }
}
