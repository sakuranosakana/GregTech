package gregtech.api.worldgen2;

import gregtech.api.unification.material.Material;
import gregtech.api.unification.ore.StoneType;
import gregtech.api.util.XSTR;
import gregtech.api.worldgen.config.OreConfigUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class WorldgenUtil {

    @Nonnull
    public static Random worldRandom(@Nonnull World world, long blockX, long blockZ) {
        return worldRandom(world.getSeed() ^ world.provider.getDimension(), blockX >> 4, blockZ >> 4);
    }

    @Nonnull
    public static Random worldRandom(long seed, long chunkX, long chunkZ) {
        Random random = new XSTR(seed);
        for (int i = 0; i < 50; i++) random.nextInt(0x00FFFFFF);
        random = new XSTR(seed ^ ((random.nextLong() >> 2 + 1L) * chunkX + (random.nextLong() >> 2 + 1L) * chunkZ));
        for (int i = 0; i < 50; i++) random.nextInt(0x00FFFFFF);
        return random;
    }

    public static boolean easyIsReplaceable(World world, BlockPos pos) {
        return easyIsReplaceable(world, pos, world.getBlockState(pos));
    }

    public static boolean easyIsReplaceable(World world, BlockPos pos, @Nonnull IBlockState state) {
        Block block = state.getBlock();
        return isAir(state) || block instanceof BlockBush || block instanceof BlockSnow ||
                block instanceof BlockFire || block.isLeaves(state, world, pos) ||
                block.canBeReplacedByLeaves(state, world, pos);
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

    public static boolean placeOre(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull Material material) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block == Blocks.AIR) return false;

        StoneType stoneType = StoneType.computeStoneType(state, world, pos);
        if (stoneType == null) return false;

        IBlockState ore = OreConfigUtils.getOreForMaterial(material).get(stoneType);
        if (ore == null) return false;
        return world.setBlockState(pos, ore);
    }
}
