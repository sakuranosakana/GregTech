package gregtech.api.worldgen2;

import gregtech.api.util.XSTR;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class WorldgenUtil {

    @Nonnull
    public static Random worldRandom(@Nonnull World world, long blockX, long blockZ) {
        return worldRandom(world.getSeed() ^ world.provider.getDimension(), Math.abs(blockX >> 4), Math.abs(blockZ >> 4));
    }

    @Nonnull
    public static Random worldRandom(long seed, long chunkX, long chunkZ) {
        return new XSTR(pair(Math.abs(seed), pair(chunkX, chunkZ)));
    }

    public static long pair(long chunkX, long chunkZ) {
        return ((chunkX * chunkX) + (3 * chunkX) + (2 * chunkX * chunkZ) + (chunkZ) + (chunkZ * chunkZ)) / 2;
    }

    public static boolean isReplaceable(World world, BlockPos pos) {
        return isReplaceable(world, pos, world.getBlockState(pos));
    }

    public static boolean isReplaceable(World world, BlockPos pos, @Nonnull IBlockState state) {
        return isAir(world, pos, state) || state.getBlock().isReplaceable(world, pos) || state.getBlock().canBeReplacedByLeaves(state, world, pos);
    }

    public static boolean isAir(World world, BlockPos pos) {
        return isAir(world, pos, world.getBlockState(pos));
    }

    public static boolean isAir(World world, BlockPos pos, @Nonnull IBlockState state) {
        return isAir(state) || (state.getBlock().isAir(state, world, pos));
    }

    public static boolean isAir(@Nonnull IBlockState state) {
        return state.getBlock() == Blocks.AIR;
    }
}
