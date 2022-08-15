package gregtech.api.worldgen2.generator;

 import gregtech.api.worldgen2.Dimensions;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WorldgenFluidSpring extends WorldgenObject {

    private final IBlockState fluidBlockState;
    private final int generationChance;
    private final boolean generateSpout;

    /**
     * @param name       the name of the WorldgenObject
     * @param modid      the modid of the mod adding this WorldgenObject
     * @param isDefault  true if this WorldgenObject generates by default, otherwise false
     * @param generators the groups of world generators to use this WorldgenObject in
     * @param fluidBlockState the IBlockState to generate
     * @param generationChance the chance that this chunk will generate a fluid spring
     * @param generateSpout whether to generate a spout upwards from the vein to the surface
     */
    public WorldgenFluidSpring(@Nonnull String name, @Nonnull String modid, boolean isDefault,
                               @Nonnull IBlockState fluidBlockState, int generationChance, boolean generateSpout,
                               List<List<IWorldgenObject>> generators) {
        super(name, modid, isDefault, generators);
        this.fluidBlockState = fluidBlockState;
        this.generationChance = generationChance;
        if (generationChance <= 0)
            throw new IllegalArgumentException("Fluid Spring " + modid + ":" + name + " generation chance cannot be less than or equal to 0");
        this.generateSpout = generateSpout;
    }

    @Override
    public boolean generate(World world, Chunk chunk, int dimension, int minX, int maxX, int minZ, int maxZ, @Nonnull Random random, Biome[][] biomes, Set<String> biomeNames) {
        if (random.nextInt(this.generationChance) != 0) return false;

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(minX + 8, 0, minZ + 8);

        // need to get the surface now for the oil spout,
        // MC can't find it after placing the bottom area...
        int surface = world.getTopSolidOrLiquidBlock(pos).getY();

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() != Blocks.BEDROCK) return false;

        if (dimension == Dimensions.NETHER_ID) state = Blocks.NETHERRACK.getDefaultState();
        else state = Blocks.STONE.getDefaultState();

        // the area at the base
        for (int i = 0; i <= 6; i++) {
            for (int x = minX + i; x < maxX - i; x++) {
                for (int z = minZ + i; z < maxZ - i; z++) {
                    pos.setPos(x, i + 1, z);
                    IBlockState currentState = world.getBlockState(pos);
                    if (!currentState.isOpaqueCube() && !(currentState.getBlock().isLeaves(currentState, world, pos))) {
                        world.setBlockState(pos, state);
                    }

                    if (i > 1) {
                        world.setBlockState(pos, this.fluidBlockState);
                    }
                }
            }
        }

        // no spouts, so end here
        if (!generateSpout) return true;

        // the spout
        pos.setPos(Math.min(maxX, minX + random.nextInt(16)), 7, Math.min(maxZ, minZ + random.nextInt(16)));
        for (int y = 6; y < surface + random.nextInt(6) + 8; y++) {
            pos.setY(y);
            world.setBlockState(pos, fluidBlockState);
            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                world.setBlockState(pos.offset(facing), fluidBlockState);
            }
        }

        // tip of the spout
        pos.move(EnumFacing.UP);
        world.setBlockState(pos, fluidBlockState);

        // generates around 700 buckets
        return true;
    }
}
