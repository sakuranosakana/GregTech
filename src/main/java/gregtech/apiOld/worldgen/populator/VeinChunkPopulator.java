package gregtech.apiOld.worldgen.populator;

import gregtech.apiOld.worldgen.config.OreDepositDefinition;
import gregtech.apiOld.worldgen.generator.GridEntryInfo;
import net.minecraft.world.World;

import java.util.Random;

public interface VeinChunkPopulator extends IVeinPopulator {

    void populateChunk(World world, int chunkX, int chunkZ, Random random, OreDepositDefinition definition, GridEntryInfo gridEntryInfo);
}
