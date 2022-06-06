package gregtech.api.worldgen2.generator;

import gregtech.api.worldgen2.Dimensions;
import it.unimi.dsi.fastutil.ints.Int2BooleanOpenHashMap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public abstract class WorldgenObject {

    public boolean isEnabled = true;
    public boolean isInvalid = false;

    public final String name;
    public final String category;

    public final Map<Integer, Boolean> allowedDimensions = new Int2BooleanOpenHashMap();

    /**
     * @param name       the name of the WorldgenObject
     * @param modid      the modid of the mod adding this WorldgenObject
     * @param isDefault  true if this WorldgenObject generates by default, otherwise false
     * @param generators the groups of world generators to use this WorldgenObject in
     */
    @SafeVarargs
    public WorldgenObject(@Nonnull String name, @Nonnull String modid, boolean isDefault, List<WorldgenObject>... generators) {
        if (name.isEmpty()) throw new IllegalArgumentException("Worldgen Object name must not be empty");
        if (modid.isEmpty()) throw new IllegalArgumentException("Worldgen Object modid must not be empty");
        this.name = name;
        this.category = String.format("%s.world_generator.%s", modid, name);
        for (List<WorldgenObject> worldgenObjects : generators) {
            worldgenObjects.add(this);
        }
    }

    /**
     * Generates this WorldgenObject in world
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
     * @return true if this successfully generated, otherwise false
     */
    public boolean generate(World world, Chunk chunk, int dimension, int minX, int maxX, int minZ, int maxZ, Random random, Biome[][] biomes, Set<String> biomeNames) {
        // insert your WorldGen Code here
        return false;
    }

    /**
     * @param world     the world to check
     * @param dimension the dimension to generate in
     * @return true if generation is allowed, otherwise false
     */
    public boolean isEnabled(World world, int dimension) {
        if (this.isInvalid) return false;
        Boolean isAllowed = allowedDimensions.get(world.provider.getDimension());
        if (isAllowed != null) return isAllowed && isEnabled;
        //TODO dimensions from vein config
//        isAllowed = getWorldgeneratorFromConfig().getAllowedDimensions().contains(world.provider.getDimension());
        isAllowed = true; // temporary until configs
        allowedDimensions.put(world.provider.getDimension(), isAllowed);
        return isAllowed && isEnabled;
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
    public void reset(World world, Chunk chunk, int dimension, int minX, int maxX, int minZ, int maxZ, Random random, Biome[][] biomes, Set<String> biomeNames) {
        // nothing
    }

    /**
     *
     * @param world the world to check
     * @param minX the minimum x to check
     * @param maxX the maximum x to check
     * @param minZ the minimum z to check
     * @param maxZ the maximum z to check
     * @return true if major world generation is happening, otherwise false
     */
    public boolean checkForMajorWorldgen(@Nonnull World world, int minX, int maxX, int minZ, int maxZ) {
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
