package gregtech.api.worldgen2.generator;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.Random;

/**
 * Used for generating worldgen in world using chunk-grid alignment
 */
public interface IChunkGridAligned extends IWorldgenObject {

    /**
     * @param world   the world to generate this in
     * @param chunk   the chunk to generate this in
     * @param minX    the minimum X coordinate of where this should generate (inclusive)
     * @param maxX    the maximum X coordinate of where this should generate (exclusive)
     * @param minZ    the minimum Z coordinate of where this should generate (inclusive)
     * @param maxZ    the maximum Z coordinate of where this should generate (exclusive)
     * @param originX the X block coordinate of the center, often in another chunk
     * @param originZ the Z block coordinate of the center, often in another chunk
     * @param random  the random number generator to use
     * @return true if this successfully generated, otherwise false
     */
    boolean generateChunkAligned(World world, Chunk chunk, int minX, int maxX, int minZ, int maxZ, int originX, int originZ, Random random);

    /**
     * The weight must be greater than 0
     *
     * @return the weight of this vein
     */
    int getWeight();
}
