package gregtech.api.worldgen.generator;

import com.google.common.collect.ImmutableSet;
import gregtech.common.ConfigHolder;
import gregtech.common.worldgen.WorldGenRubberTree;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;
import java.util.Set;

import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.*;

public class WorldGeneratorImpl implements IWorldGenerator {

    public static final WorldGeneratorImpl INSTANCE = new WorldGeneratorImpl();

    private static final Set<EventType> ORE_EVENT_TYPES = ImmutableSet.of(COAL, DIAMOND, GOLD, IRON, LAPIS, REDSTONE, QUARTZ, EMERALD);
    public static final int GRID_SIZE_X = 3;
    public static final int GRID_SIZE_Z = 3;

    private WorldGeneratorImpl() { }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onOreGenerate(OreGenEvent.GenerateMinable event) {
        EventType eventType = event.getType();
        if (ConfigHolder.worldgen.disableVanillaOres && ORE_EVENT_TYPES.contains(eventType)) {
            event.setResult(Result.DENY);
        }
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        int selfGridX = Math.floorDiv(chunkX, GRID_SIZE_X);
        int selfGridZ = Math.floorDiv(chunkZ, GRID_SIZE_Z);
        generateInternal(world, selfGridX, selfGridZ, chunkX, chunkZ, random);
    }

    private void generateInternal(World world, int selfGridX, int selfGridZ, int chunkX, int chunkZ, Random random) {
        int halfSizeX = (GRID_SIZE_X - 1) / 2;
        int halfSizeZ = (GRID_SIZE_Z - 1) / 2;
        for (int gridX = -halfSizeX; gridX <= halfSizeX; gridX++) {
            for (int gridZ = -halfSizeZ; gridZ <= halfSizeZ; gridZ++) {
                CachedGridEntry cachedGridEntry = CachedGridEntry.getOrCreateEntry(world, selfGridX + gridX, selfGridZ + gridZ, chunkX, chunkZ);
                cachedGridEntry.populateChunk(world, chunkX, chunkZ, random);
            }
        }
    }
}
