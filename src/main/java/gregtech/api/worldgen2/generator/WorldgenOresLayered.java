package gregtech.api.worldgen2.generator;

import gregtech.api.unification.material.Material;
import gregtech.api.util.GTLog;
import gregtech.api.worldgen2.GTWorldGenerator;
import gregtech.api.worldgen2.WorldgenUtil;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class WorldgenOresLayered extends WorldgenObject {

    public final int weight;
    public final int distance;
    public final short minY;
    public final short maxY;
    public final short density;
    public final short size;

    public final Material top;
    public final Material bottom;
    public final Material between;
    public final Material spread;
    @Nullable
    public final Material indicator;

    /**
     * @param name       the name of the vein
     * @param modid      the modid of the mod adding the vein
     * @param isDefault  whether this is a default vein
     * @param minY       the minimum Y of this vein
     * @param maxY       the maximum Y of this vein
     * @param weight     the weight of this vein
     * @param density    the density of this vein
     * @param distance   the minimum distance from spawn of this vein
     * @param size       the size of this vein
     * @param top        the top layer material
     * @param bottom     the bottom layer mater
     * @param between    the between layer material
     * @param spread     the spread material
     * @param indicator  the indicator rock material, null for no rocks
     * @param generators the groups of world generators to use this vein in
     */
    @SafeVarargs
    public WorldgenOresLayered(@Nonnull String name, @Nonnull String modid, boolean isDefault, int minY, int maxY, int weight, int density, int distance, int size, Material top, Material bottom, Material between, Material spread, @Nullable Material indicator, List<WorldgenObject>... generators) {
        super(name, modid, isDefault, generators);
        this.minY = (short) Math.max(0, minY);
        this.maxY = (short) Math.max(minY + 5, maxY);
        this.weight = Math.max(1, weight);
        this.density = (short) Math.max(1, density);
        this.distance = Math.max(0, distance);
        this.size = (short) Math.max(1, size);
        this.top = top;
        this.bottom = bottom;
        this.between = between;
        this.spread = spread;
        this.indicator = indicator;

        //TODO automatic ore block generation by inclusion in veins
//        if (this.isEnabled) {
//            if (top != null) top.addOreBlock();
//            if (bottom != null) bottom.addOreBlock();
//            if (between != null) between.addOreBlock();
//            if (spread != null) spread.addOreBlock();
//        }

        if (top == null) {
            GTLog.logger.warn("Top material in Layered Vein {} from {} was null!", name, modid);
            this.isInvalid = true;
        }
        if (bottom == null) {
            GTLog.logger.warn("bottom material in Layered Vein {} from {} was null!", name, modid);
            this.isInvalid = true;
        }
        if (between == null) {
            GTLog.logger.warn("between material in Layered Vein {} from {} was null!", name, modid);
            this.isInvalid = true;
        }
        if (spread == null) {
            GTLog.logger.warn("spread material in Layered Vein {} from {} was null!", name, modid);
            this.isInvalid = true;
        }
    }

    public boolean generate(World world, Chunk chunk, int minX, int maxX, int minZ, int maxZ, int originX, int originZ, Random random) {
        // do not generate if we are generating special worldgen
        if (GTWorldGenerator.GENERATING_SPECIAL) return false;

        // if we have a minimum spawn distance, do not generate if we are not far enough away
        if (distance > 0 && Math.abs(minX) <= distance && Math.abs(minZ) <= distance) return false;

        int veinMinY = minY + WorldgenUtil.worldRandom(world, originX, originZ).nextInt(Math.max(1, maxY - minY - 5));

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(0, 0, 0);

        // indicator rocks
        // TODO future GT6 world generation options
        if (indicator != null /*&& (!(ConfigHolder.worldgen.generateStreets && world.provider.getDimension() == Dimensions.OVERWORLD_ID))*/) {
            for (int i = 0, j = 1 + random.nextInt(3); i < j; i++) {
                int x = minX + random.nextInt(16);
                int z = minZ + random.nextInt(16);
                for (int y = Math.min(world.getHeight(), veinMinY + 25); y >= veinMinY - 10 && y > 0; y--) {
                    IBlockState contactState = chunk.getBlockState(pos.setPos(x & 15, y, z & 15));
                    if (contactState.getMaterial().isLiquid()) break;
                    if (!contactState.isOpaqueCube()) continue;
                    net.minecraft.block.material.Material material = contactState.getMaterial();
                    if (material == net.minecraft.block.material.Material.GRASS ||
                            material == net.minecraft.block.material.Material.GROUND ||
                            material == net.minecraft.block.material.Material.SAND ||
                            material == net.minecraft.block.material.Material.ROCK) {
                        if (WorldgenUtil.easyIsReplaceable(world, pos.setPos(x, y + 1, z))) {
                            world.setBlockState(pos, MetaBlocks.SURFACE_ROCK.get(indicator).getBlock(indicator));
                        }
                        break;
                    }
                }
            }
        }

        // the ore vein itself

        int startX = Math.max(originX - random.nextInt(size), minX);
        int endX = Math.min(originX + 16 + random.nextInt(size), maxX);
        int startZ = Math.max(originZ - random.nextInt(size), minZ);
        int endZ = Math.min(originZ + 16 + random.nextInt(size), maxZ);

        // this can sometimes happen, causing veins not to generate caused by startX = minX, and being larger than endX
        // TODO better solution
        if (startX > endX) endX = maxX;
        if (startZ > endZ) endZ = maxZ;

        for (int x = startX; x <= endX; x++) {
            for (int z = startZ; z <= endZ; z++) {
                int weightX = Math.max(1, Math.max(Math.abs(startX - x), Math.abs(endX - x)) / density);
                int weightZ = Math.max(1, Math.max(Math.abs(startZ - z), Math.abs(endZ - z)) / density);
                pos.setPos(x, 0, z);

                // place the bottom ores
                if (bottom != null) {
                    for (int y = veinMinY - 1; y < veinMinY + 2; y++) {
                        if (random.nextInt(weightZ) == 0 || random.nextInt(weightX) == 0) {
                            pos.setY(y);
                            WorldgenUtil.placeOre(world, pos, bottom);
                        }
                    }
                }
                // place the top ores
                if (top != null) {
                    for (int y = veinMinY + 3; y < veinMinY + 6; y++) {
                        if (random.nextInt(weightZ) == 0 || random.nextInt(weightX) == 0) {
                            pos.setY(y);
                            WorldgenUtil.placeOre(world, pos, top);
                        }
                    }
                }
                // place the between ores
                if (between != null) {
                    if (random.nextInt(weightZ) == 0 || random.nextInt(weightX) == 0) {
                        pos.setY(veinMinY + 2 + random.nextInt(2));
                        WorldgenUtil.placeOre(world, pos, between);
                    }
                }
                // place the spread ores
                if (spread != null) {
                    if (random.nextInt(weightZ) == 0 || random.nextInt(weightX) == 0) {
                        pos.setY(veinMinY - 1 + random.nextInt(7));
                        WorldgenUtil.placeOre(world, pos, spread);
                    }
                }
            }
        }
        return true;
    }
}
