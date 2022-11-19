package gregtech.apiOld.worldgen.populator;

import gregtech.apiOld.worldgen.config.OreDepositDefinition;
import gregtech.apiOld.worldgen.generator.GridEntryInfo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public interface VeinBufferPopulator extends IVeinPopulator {

    void populateBlockBuffer(Random random, GridEntryInfo gridEntryInfo, IBlockModifierAccess modifier, OreDepositDefinition depositDefinition);

    IBlockState getBlockByIndex(World world, BlockPos pos, int index);
}
