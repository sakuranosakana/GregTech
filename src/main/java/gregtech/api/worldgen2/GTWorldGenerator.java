package gregtech.api.worldgen2;

import gregtech.api.worldgen2.generator.WorldgenObject;
import gregtech.api.worldgen2.generator.WorldgenOresLayered;
import gregtech.common.ConfigHolder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GTWorldGenerator {

    public static boolean GENERATING_SPECIAL = false;

    // amount of chunks to move from one ore center to the next is this number
    // we add +1 because it is distance moved instead of distance between
    public static int CHUNK_ALIGNED_SPACING = ConfigHolder.worldgen.veinSpacing + 1;
    public static int CHUNK_ALIGNED_RADIUS = ConfigHolder.worldgen.veinRadius;

    public static class WorldGenContainer implements Runnable {

        public final int minX;
        public final int maxX;
        public final int minZ;
        public final int maxZ;

        public final World world;
        public final int dimension;
        public final Random random;
        public final List<WorldgenObject> normalWorldGeneration;
        public final List<WorldgenObject> layeredVeinWorldGeneration;

        public WorldGenContainer(List<WorldgenObject> normalGeneration, List<WorldgenObject> layeredVeinGeneration, int dimension, World world, int x, int z) {
            this.minX = x + 1;
            this.maxX = x + 15;
            this.minZ = z + 1;
            this.maxZ = z + 15;
            this.world = world;
            this.dimension = dimension;
            this.normalWorldGeneration = normalGeneration;
            this.layeredVeinWorldGeneration = layeredVeinGeneration;
            this.random = WorldgenUtil.worldRandom(world, x, z);
        }

        @Override
        public void run() {
            // regular worldgen
            if (!normalWorldGeneration.isEmpty() || !layeredVeinWorldGeneration.isEmpty()) {
                BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(minX + 7, 0, minZ + 7);
                Chunk chunk = world.getChunk(pos);
                Biome[][] biomes = new Biome[16][16];
                Set<String> biomeNames = new ObjectOpenHashSet<>();
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        biomes[x][z] = chunk.getBiome(pos.setPos(x, 0, z), world.provider.getBiomeProvider());
                        if (biomes[x][z] == null) {
                            biomes[x][z] = (dimension == Dimensions.NETHER_ID ? Biomes.HELL : dimension == Dimensions.END_ID ? Biomes.SKY : Biomes.PLAINS);
                        } else {
                            biomeNames.add(biomes[x][z].getBiomeName());
                        }
                    }
                }

                GENERATING_SPECIAL = false;

                // Yes, it has to be looped twice in a row, this cannot be optimized into one Loop!
                for (WorldgenObject worldGen : normalWorldGeneration) {
                    worldGen.reset(world, chunk, dimension, minX, maxX, minZ, maxZ, random, biomes, biomeNames);
                }

                // regular worldgen
                for (WorldgenObject worldGen : normalWorldGeneration) {
                    if (worldGen.isEnabled(world, dimension)) {
                        worldGen.generate(world, chunk, dimension, minX, maxX, minZ, maxZ, random, biomes, biomeNames);
                    }
                }

                generateChunkGridAligned(chunk);

                // Kill off every single Item Entity that may have dropped during Worldgen.
                for (EntityItem entityItem : world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(minX - 32, 0, minZ - 32, maxX + 48, 256, minZ + 48))) {
                    entityItem.setDead();
                }

                // Prevent Snow Layers from killing Treestumps. I really hope this works...
                Arrays.fill(chunk.precipitationHeightMap, -999);

                // Chunk got modified, duh.
                chunk.setModified(true);
            }
        }

        private void generateChunkGridAligned(Chunk chunk) {
            // layered ore worldgen
            List<WorldgenObject> veinsToGenerate = new ObjectArrayList<>();
            int maxWeight = 0;

            for (WorldgenObject worldGen : layeredVeinWorldGeneration) {
                if (worldGen.isEnabled(world, dimension)) {
                    if (worldGen instanceof WorldgenOresLayered) {
                        maxWeight += ((WorldgenOresLayered) worldGen).weight;
                    }
                    veinsToGenerate.add(worldGen);
                }
            }

            if (maxWeight > 0 && !veinsToGenerate.isEmpty()) {
                for (int x = -CHUNK_ALIGNED_RADIUS; x <= CHUNK_ALIGNED_RADIUS; x++) {
                    for (int z = -CHUNK_ALIGNED_RADIUS; z <= CHUNK_ALIGNED_RADIUS; z++) {
                        int originX = minX + (x * 16);
                        int originZ = minZ + (z * 16);

                        // check if the chunk we're testing is chunk-grid aligned in both the X and Z axes
                        if (((originX >> 4) % CHUNK_ALIGNED_SPACING + CHUNK_ALIGNED_SPACING) % CHUNK_ALIGNED_SPACING == 1 &&
                                ((originZ >> 4) % CHUNK_ALIGNED_SPACING + CHUNK_ALIGNED_SPACING) % CHUNK_ALIGNED_SPACING == 1) {

                            // create a random with a consistent seed based on the world seed and the coordinates of the vein
                            Random random = WorldgenUtil.worldRandom(world, originX, originZ);

                            // weight the veins to choose different ones for each vein
                            // because we have a consistent random, this is always the same for each chunk in a single vein
                            int randomWeight = random.nextInt(maxWeight);
                            for (WorldgenObject worldgen : veinsToGenerate) {
                                if (worldgen instanceof WorldgenOresLayered) {
                                    randomWeight -= ((WorldgenOresLayered) worldgen).weight;
                                }
                                if (randomWeight <= 0) {
                                    // only generate
                                    worldgen.generateChunkAligned(world, chunk, minX, maxX, minZ, maxZ, originX, originZ, random);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static final List<Runnable> LIST = new ObjectArrayList<>();
    private static boolean LOCK = false;

    public static void generate(@Nonnull World world, int x, int z) {
        switch (world.provider.getDimension()) {
            case Integer.MIN_VALUE:
                return;
            case Dimensions.OVERWORLD_ID: {
                generate(new WorldGenContainer(GregTechWorldgen.WORLDGEN_OVERWORLD, GregTechWorldgen.ORES_OVERWORLD, Dimensions.OVERWORLD_ID, world, x, z));
                return;
            }
            case Dimensions.NETHER_ID: {
                generate(new WorldGenContainer(GregTechWorldgen.WORLDGEN_NETHER, GregTechWorldgen.ORES_NETHER, Dimensions.NETHER_ID, world, x, z));
                return;
            }
            case Dimensions.END_ID: {
                generate(new WorldGenContainer(GregTechWorldgen.WORLDGEN_END, GregTechWorldgen.ORES_END, Dimensions.END_ID, world, x, z));
            }
        }
    }

    public static void generate(WorldGenContainer worldgen) {
        LIST.add(worldgen);
        if (!LOCK) {
            LOCK = true;
            while (!LIST.isEmpty()) {
                LIST.remove(LIST.size() - 1).run();
            }
            LOCK = false;
        }
    }
}
