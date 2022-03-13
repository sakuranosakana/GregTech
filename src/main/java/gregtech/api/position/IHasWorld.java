package gregtech.api.position;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

/**
 * Contains simple Utility Functions based just on the World of the Implementor.
 */
public interface IHasWorld {

    World getWorld();

    boolean isServerSide();

    boolean isClientSide();

    int random(int range);

    TileEntity getTileEntity(BlockPos pos);

    IBlockState getState(BlockPos pos);

    Block getBlock(BlockPos pos);

    int getMetaData(BlockPos pos);

    int getLightLevel(BlockPos pos);

    boolean getOpacity(BlockPos pos);

    boolean canSeeSky(BlockPos pos);

    boolean isRainedOn(BlockPos pos);

    boolean isAir(BlockPos pos);

    Biome getBiome(BlockPos pos);
}
