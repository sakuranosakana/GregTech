package gregtech.api.worldgen2.generator;

import gregtech.api.unification.material.Material;
import gregtech.api.util.IBlockOre;
import gregtech.api.worldgen2.GTWorldGenerator;
import gregtech.api.worldgen2.WorldgenUtil;
import gregtech.common.blocks.BlockOre;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WorldgenMixed extends WorldgenObject implements IChunkGridAligned {

    private final IBlockState[] ores;
    private final int[] oreWeights;

    private final int veinWeight;
    private final int minY;
    private final int maxY;
    private final int density;
    private final int distance;
    private final int size;

    private final IBlockState indicator;

    /**
     * @see gregtech.api.worldgen2.builder.MixedOreVeinBuilder
     */
    @SafeVarargs
    public WorldgenMixed(@Nonnull String name, @Nonnull String modid, boolean isDefault, @Nonnull IBlockState[] ores, @Nonnull int[] oreWeights,
                         int minY, int maxY, int veinWeight, int density, int distance, int size,
                         Material indicator, IBlockState indicatorState, List<IWorldgenObject>... generators) {
        super(name, modid, isDefault, generators);
        this.ores = ores;
        if (ores.length != oreWeights.length)
            throw new IllegalArgumentException("Mixed Vein " + modid + ":" + name + " ores and weights must be of equal amounts.");
        this.oreWeights = oreWeights;
        if (minY < 0)
            throw new IllegalArgumentException("Mixed Ore Vein " + modid + ":" + name + " minimum y cannot be less than 0");
        this.minY = minY;
        if (maxY <= minY)
            throw new IllegalArgumentException("Mixed Ore Vein " + modid + ":" + name + " maximum y cannot be less than the minimum y");
        this.maxY = maxY;
        if (veinWeight < 1)
            throw new IllegalArgumentException("Mixed Ore Vein " + modid + ":" + name + " vein weight cannot be less than 1");
        this.veinWeight = veinWeight;
        if (density < 1)
            throw new IllegalArgumentException("Mixed Ore Vein " + modid + ":" + name + " density cannot be less than 1");
        this.density = density;
        if (distance < 0)
            throw new IllegalArgumentException("Mixed Ore Vein " + modid + ":" + name + " distance cannot be less than 0");
        this.distance = distance;
        if (size < 1)
            throw new IllegalArgumentException("Mixed Ore Vein " + modid + ":" + name + " size cannot be less than 1");
        this.size = size;
        if (indicator != null) {
            this.indicator = MetaBlocks.SURFACE_ROCK.get(indicator).getBlock(indicator);
        } else {
            this.indicator = indicatorState;
        }
    }

    @Override
    public boolean generateChunkAligned(World world, Chunk chunk, int minX, int maxX, int minZ, int maxZ, int originX, int originZ, Random random) {
        // do not generate if we are generating special worldgen
        if (GTWorldGenerator.GENERATING_SPECIAL) return false;

        // if we have a minimum spawn distance, do not generate if we are not far enough away
        if (distance > 0 && Math.abs(minX) <= distance && Math.abs(minZ) <= distance) return false;

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(minX, 0, minZ);
        int veinHeight = maxY - minY;

        // only use the chunk aligned random for consistent y
        int veinMinY = minY;

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

        int veinMaxY = Math.min(surface - 5,  veinMinY + veinHeight);

        int totalWeight = Arrays.stream(this.oreWeights).sum();

        // the ore vein for the current chunk
        for (int x = startX; x < endX; x++) {
            for (int z = startZ; z < endZ; z++) {
                pos.setPos(x, 0, z);

                for (int y = veinMinY; y < veinMaxY; y++) {
                    int weightX = Math.max(1, Math.max(Math.abs(startX - x), Math.abs(endX - x)) / density);
                    int weightZ = Math.max(1, Math.max(Math.abs(startZ - z), Math.abs(endZ - z)) / density);
                    if (veinRandom.nextInt(weightZ) == 0 || veinRandom.nextInt(weightX) == 0) {
                        pos.setY(y);

                        // pick a random weighted ore from the list
                        int randomWeight = random.nextInt(totalWeight);
                        for (int i = 0; i < this.oreWeights.length; i++) {
                            randomWeight -= this.oreWeights[i];
                            if (randomWeight <= 0) {
                                if (this.ores[i].getBlock() instanceof BlockOre) {
                                    IBlockOre.placeOreBlock(world, pos, ((BlockOre) this.ores[i].getBlock()).material);
                                } else {
                                    world.setBlockState(pos, this.ores[i]);
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    @Override
    public int getWeight() {
        return this.veinWeight;
    }
}
