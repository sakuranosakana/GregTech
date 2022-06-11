package gregtech.api.util;

import gregtech.api.GregTechAPI;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.ore.StoneType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Map;

public interface IBlockOre {

    IBlockState getOreBlock(StoneType stoneType);

    static boolean placeOreBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull Material material) {
        return placeOreBlock(world, pos, material, false);
    }

    static boolean placeOreBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull Material material, boolean isSmallOre) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block == Blocks.AIR) return false;

        StoneType stoneType = StoneType.computeStoneType(state, world, pos);
        if (stoneType == null) return false;
        return placeOreBlock(world, pos, material, stoneType, isSmallOre);
    }

    static boolean placeOreBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull Material material, @Nonnull StoneType stoneType) {
        return placeOreBlock(world, pos, material, stoneType, false);
    }

    static boolean placeOreBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull Material material, @Nonnull StoneType stoneType, boolean isSmallOre) {
        Map<StoneType, IBlockOre> ore = isSmallOre ? GregTechAPI.oreBlockSmallTable.get(material) : GregTechAPI.oreBlockTable.get(material);
        if (ore == null) return false;
        IBlockOre block = ore.get(stoneType);
        if (block == null) return false;
        return world.setBlockState(pos, block.getOreBlock(stoneType), 2);
    }
}
