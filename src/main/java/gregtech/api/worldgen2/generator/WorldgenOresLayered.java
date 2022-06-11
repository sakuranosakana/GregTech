package gregtech.api.worldgen2.generator;

import gregtech.api.unification.material.Material;
import gregtech.api.util.IBlockOre;
import gregtech.api.worldgen2.GTWorldGenerator;
import gregtech.api.worldgen2.WorldgenUtil;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class WorldgenOresLayered extends WorldgenObject implements IChunkGridAligned {

    public final int minY;
    public final int maxY;
    public final int weight;
    public final int density;
    public final int distance;
    public final int size;

    @Nullable
    public final Material top;
    public final int topHeight;
    @Nullable
    public final Material bottom;
    public final int bottomHeight;
    @Nullable
    public final Material between;
    public final int betweenStartHeight;
    public final int betweenHeight;
    @Nullable
    public final Material spread;
    @Nullable
    public final IBlockState indicator;

    /**
     * {@link gregtech.api.worldgen2.builder.LayeredOreVeinBuilder}
     */
    @SafeVarargs
    public WorldgenOresLayered(@Nonnull String name, @Nonnull String modid, boolean isDefault, int minY, int maxY, int weight, int density, int distance, int size,
                               @Nullable Material top, int topHeight, @Nullable Material bottom, int bottomHeight, @Nullable Material between, int betweenHeight,
                               @Nullable Material spread, @Nullable Material indicator, @Nullable IBlockState indicatorState, List<IWorldgenObject>... generators) {
        super(name, modid, isDefault, generators);
        if (minY < 0)
            throw new IllegalArgumentException("Layered Ore Vein " + modid + ":" + name + " minimum y cannot be less than 0");
        this.minY = minY;
        if (maxY <= minY)
            throw new IllegalArgumentException("Layered Ore Vein " + modid + ":" + name + " maximum y cannot be less than the minimum y");
        this.maxY = maxY;
        if (weight < 1)
            throw new IllegalArgumentException("Layered Ore Vein " + modid + ":" + name + " weight cannot be less than 1");
        this.weight = weight;
        if (density < 1)
            throw new IllegalArgumentException("Layered Ore Vein " + modid + ":" + name + " density cannot be less than 1");
        this.density = density;
        if (distance < 0)
            throw new IllegalArgumentException("Layered Ore Vein " + modid + ":" + name + " distance cannot be less than 0");
        this.distance = distance;
        if (size < 1)
            throw new IllegalArgumentException("Layered Ore Vein " + modid + ":" + name + " size cannot be less than 1");
        this.size = size;
        this.top = top;
        this.topHeight = topHeight;
        this.bottom = bottom;
        this.bottomHeight = bottomHeight;
        this.between = between;
        this.betweenStartHeight = topHeight / 2;
        if (this.betweenStartHeight + betweenHeight > this.topHeight + this.bottomHeight)
            throw new IllegalArgumentException("Layered Ore Vein " + modid + ":" + name + "between height cannot be bigger than the vein total height");
        this.betweenHeight = betweenHeight;
        this.spread = spread;
        if (indicator != null) {
            this.indicator = MetaBlocks.SURFACE_ROCK.get(indicator).getBlock(indicator);
        } else {
            this.indicator = indicatorState;
        }

        //TODO automatic ore block generation by inclusion in veins
//        if (this.isEnabled) {
//            if (top != null) top.addOreBlock();
//            if (bottom != null) bottom.addOreBlock();
//            if (between != null) between.addOreBlock();
//            if (spread != null) spread.addOreBlock();
//        }
    }

    @Override
    public boolean generateChunkAligned(World world, Chunk chunk, int minX, int maxX, int minZ, int maxZ, int originX, int originZ, Random random) {
        // do not generate if we are generating special worldgen
        if (GTWorldGenerator.GENERATING_SPECIAL) return false;

        // if we have a minimum spawn distance, do not generate if we are not far enough away
        if (distance > 0 && Math.abs(minX) <= distance && Math.abs(minZ) <= distance) return false;

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(minX, 0, minZ);
        int veinHeight = topHeight + bottomHeight;

        // only use the chunk aligned random for consistent y
        int veinMinY = Math.min(maxY, minY + random.nextInt(maxY - minY - veinHeight));

        // if the vein is going to generate entirely in the sky, move it down
        // using 5 to get out of surface grass/dirt/sand
        int surface = world.getTopSolidOrLiquidBlock(pos).getY();
        if (veinMinY > surface) veinMinY = Math.max(minY, surface - 5 - veinHeight);

        // new random for varied ore and indicator placement
        Random veinRandom = WorldgenUtil.worldRandom(world, minX, minZ);

        // randomize the outer edge distances of the vein with our consistent random
        // using this ensures the edges are continuous
        int startX = Math.max(minX, originX - random.nextInt(size));
        int endX = Math.min(maxX, originX + 16 + random.nextInt(size));
        int startZ = Math.max(minZ, originZ - random.nextInt(size));
        int endZ = Math.min(maxZ, originZ + 16 + random.nextInt(size));

        // indicator rocks
        int rocksToPlace = veinRandom.nextInt(5);

        // TODO future GT6 world generation options
        if (indicator != null /*&& (!(ConfigHolder.worldgen.generateStreets && world.provider.getDimension() == Dimensions.OVERWORLD_ID))*/) {
            for (int i = 0; i < rocksToPlace; i++) {
                // using the actual vein boundaries here so rocks don't generate where ore is guaranteed not to be
                int x = Math.min(endX, startX + veinRandom.nextInt(16));
                int z = Math.min(endZ, startZ + veinRandom.nextInt(16));
                for (int y = surface; y > veinMinY + veinHeight; y--) {
                    pos.setPos(x, y, z);
                    if (WorldgenUtil.isReplaceable(world, pos) && !WorldgenUtil.isReplaceable(world, pos.down())) {
                        world.setBlockState(pos, indicator, 2);
                    } else {
                        pos.move(EnumFacing.DOWN);
                    }
                }
            }
        }

        // the ore vein for the current chunk
        int betweenRealHeight = bottomHeight + betweenStartHeight;
        for (int x = startX; x < endX; x++) {
            for (int z = startZ; z < endZ; z++) {
                pos.setPos(x, 0, z);

                int weightX = Math.max(1, Math.max(Math.abs(startX - x), Math.abs(endX - x)) / density);
                int weightZ = Math.max(1, Math.max(Math.abs(startZ - z), Math.abs(endZ - z)) / density);

                // place the bottom ores
                if (bottom != null) {
                    for (int y = veinMinY; y < veinMinY + bottomHeight; y++) {
                        if (veinRandom.nextInt(weightZ) == 0 || veinRandom.nextInt(weightX) == 0) {
                            pos.setY(y);
                            IBlockOre.placeOreBlock(world, pos, bottom);
                        }
                    }
                }
                // place the top ores
                if (top != null) {
                    for (int y = veinMinY + bottomHeight; y < veinMinY + veinHeight; y++) {
                        if (veinRandom.nextInt(weightZ) == 0 || veinRandom.nextInt(weightX) == 0) {
                            pos.setY(y);
                            IBlockOre.placeOreBlock(world, pos, top);
                        }
                    }
                }
                // place the between ores
                if (between != null) {
                    for (int y = veinMinY + betweenRealHeight - betweenHeight + 1; y < veinMinY + betweenRealHeight; y++) {
                        if (veinRandom.nextInt(weightZ) == 0 || veinRandom.nextInt(weightX) == 0) {
                            pos.setY(y);
                            IBlockOre.placeOreBlock(world, pos, between);
                        }
                    }
                }
                // place the spread ores
                if (spread != null) {
                    if (veinRandom.nextInt(weightZ) == 0 || veinRandom.nextInt(weightX) == 0) {
                        pos.setY(veinMinY + veinRandom.nextInt(veinHeight));
                        IBlockOre.placeOreBlock(world, pos, spread);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public int getWeight() {
        return weight;
    }
}
