package gregtech.api.worldgen2.generator;

import gregtech.api.GTValues;
import gregtech.api.unification.material.Material;
import gregtech.api.util.IBlockOre;
import gregtech.api.worldgen2.GTWorldGenerator;
import gregtech.api.worldgen2.NoiseGenerator;
import gregtech.api.worldgen2.WorldgenUtil;
import gregtech.api.worldgen2.stonelayer.StoneLayer;
import gregtech.api.worldgen2.stonelayer.StoneLayerOres;
import gregtech.common.blocks.BlockStoneSmooth;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WorldgenStoneLayers extends WorldgenObject {

    /**
     * @param name       the name of the WorldgenObject
     * @param modid      the modid of the mod adding this WorldgenObject
     * @param isDefault  true if this WorldgenObject generates by default, otherwise false
     * @param generators the groups of world generators to use this WorldgenObject in
     */
    public WorldgenStoneLayers(@Nonnull String name, @Nonnull String modid, boolean isDefault, List<List<IWorldgenObject>> generators) {
        super(name, modid, isDefault, generators);
        GTWorldGenerator.GENERATE_STONE = isEnabled;
    }

    @Override
    public boolean generate(World world, Chunk chunk, int dimension, int minX, int maxX, int minZ, int maxZ, Random random, Biome[][] biomes, Set<String> biomeNames) {
        if (GTWorldGenerator.GENERATING_SPECIAL) return false;

        final NoiseGenerator noise = new NoiseGenerator(world);
        final ExtendedBlockStorage[] storages = chunk.getBlockStorageArray();
        final int listSize = StoneLayer.LAYERS.size();
        final int maxHeight = chunk.getTopFilledSegment() + 15;
        final StoneLayer[] scan = new StoneLayer[7];
        final byte scanMinusOne = (byte) (scan.length - 1);

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int i = 0; i < 16; i++) {
            final int x = minX + i;
            if (x > maxX) break;
            for (int j = 0; j < 16; j++) {
                final int z = minZ + j;
                if (z > maxZ) break;

                final Biome biome = biomes[i][j];
                pos.setPos(x, 1, z);

                for (int k = 0; k < scan.length; k++) {
                    scan[k] = (StoneLayer.LAYERS.get(noise.get(x, k - 2, z, listSize)));
                    if (scan[k].useSlate) scan[k] = StoneLayer.SLATE;
                }

                boolean canPlaceRocks = false;
                Material lastOre = null;

                for (int y = 1; y < maxHeight; y++) {
                    pos.setY(y);
                    final ExtendedBlockStorage storage = storages[y >> 4];
                    final IBlockState state = (storage == null ? Blocks.AIR.getDefaultState() : storage.get(i, y & 15, j));
                    final Block block = state.getBlock();
                    assert storage != null;

                    if (block == Blocks.BEDROCK) {
                        // just mark as opaque ground
                        canPlaceRocks = true;
                    } else if (block == Blocks.AIR) {
                        // Place Rock if on Opaque Surface.
                        if (canPlaceRocks && random.nextInt(128) == 0) {
                            if (lastOre != null) {
                                if (MetaBlocks.SURFACE_ROCK.containsKey(lastOre)) {
                                    if (WorldgenUtil.isReplaceable(world, pos) && !WorldgenUtil.isReplaceable(world, pos.down())) {
                                        world.setBlockState(pos, MetaBlocks.SURFACE_ROCK.get(lastOre).getBlock(lastOre));
                                        lastOre = null;
                                    }
                                }
                            }
                            canPlaceRocks = false;
                        }
                    } else if (block == Blocks.STONE || (block == Blocks.MONSTER_EGG && block.getMetaFromState(state) == 0)) {
                        // Stone and Ore Generation in vanilla Stone.
                        canPlaceRocks = true;
                        boolean temp = true;
                        if (scan[5] == scan[1]) {
                            for (StoneLayerOres ores : scan[3].ores) {
                                if (ores.material != null && ores.check(scan[3], world, pos, biome.getBiomeName(), random) && (scan[6] == scan[0] ? ores.normal(scan[3], world, pos, biome.getBiomeName()) : ores.small(scan[3], world, pos, biome.getBiomeName()))) {
                                    lastOre = ores.material;
                                    temp = false;
                                    break;
                                }
                            }
                        } else {
                            for (StoneLayerOres ores : StoneLayer.get(scan[5], scan[1])) {
                                if (ores.material != null && ores.check(scan[3], world, pos, biome.getBiomeName(), random) && ores.set(scan[3], world, pos, biome.getBiomeName(), random)) {
                                    lastOre = ores.material;
                                    temp = false;
                                    break;
                                }
                            }
                        }
                        if (temp && scan[4] != scan[2] && scan[3].stoneType != null && !StoneLayer.RANDOM_SMALL_GEM_ORES.isEmpty() && random.nextInt(100) == 0) {
                            if (IBlockOre.placeOreBlock(world, pos, StoneLayer.RANDOM_SMALL_GEM_ORES.get(GTValues.RNG.nextInt(StoneLayer.RANDOM_SMALL_GEM_ORES.size())), true)) {
                                temp = false;
                            }
                        }
                        if (temp && state != scan[3].stone) {
                            storage.set(i, y & 15, j, scan[3].stone);
                        }
                    } else if (block == Blocks.COBBLESTONE) {
                        // cobblestone generation
                        canPlaceRocks = true;
                        if (scan[3].cobble != null) {
                            if (state != scan[3].cobble) {
                                storage.set(i, y & 15, j, scan[3].cobble);
                            }
                        }
                    } else if (block == Blocks.MOSSY_COBBLESTONE) {
                        // mossy cobblestone generation
                        canPlaceRocks = true;
                        if (scan[3].mossy != null) {
                            if (state != scan[3].mossy) {
                                storage.set(i, y & 15, j, scan[3].mossy);
                            }
                        }
                    } else if (StoneLayer.REPLACEABLE_BLOCKS.contains(block)) {
                        // stone and ore generation in replaceable blocks
                        canPlaceRocks = true;
                        boolean temp = true;
                        if (scan[5] == scan[1]) {
                            for (StoneLayerOres ores : scan[3].ores) {
                                if (ores.material != null && ores.check(scan[3], world, pos, biome.getBiomeName(), random) && (scan[6] == scan[0] ? ores.normal(scan[3], world, pos, biome.getBiomeName()) : ores.small(scan[3], world, pos, biome.getBiomeName()))) {
                                    lastOre = ores.material;
                                    temp = false;
                                    break;
                                }
                            }
                        } else {
                            for (StoneLayerOres ores : StoneLayer.get(scan[5], scan[1])) {
                                if (ores.material != null && ores.check(scan[3], world, pos, biome.getBiomeName(), random) && ores.set(scan[3], world, pos, biome.getBiomeName(), random)) {
                                    lastOre = ores.material;
                                    temp = false;
                                    break;
                                }
                            }
                        }
                        if (temp && scan[4] != scan[2] && scan[3].stoneType != null && !StoneLayer.RANDOM_SMALL_GEM_ORES.isEmpty() && random.nextInt(100) == 0) {
                            if (IBlockOre.placeOreBlock(world, pos, StoneLayer.RANDOM_SMALL_GEM_ORES.get(GTValues.RNG.nextInt(StoneLayer.RANDOM_SMALL_GEM_ORES.size())), true)) {
                                temp = false;
                            }
                        }
                        if (temp && state != scan[3].stone) {
                            storage.set(i, y & 15, j, scan[3].stone);
                        }
                    } else if (block instanceof BlockStoneSmooth) { //TODO interface this
                        // Check for the GT Stone being natural
                        // Unlikely case due to GT Stone being the thing that is supposed to generate this very moment and not before.
                        canPlaceRocks = storage.get(i, y & 15, j).getBlock() instanceof BlockStoneSmooth;
                    } else if (WorldgenUtil.isReplaceable(world, pos)) {
                        // Place Rock if on Opaque Surface
                        if (canPlaceRocks && !state.getMaterial().isLiquid() && random.nextInt(128) == 0) {
                            if (lastOre != null) {
                                if (MetaBlocks.SURFACE_ROCK.containsKey(lastOre)) {
                                    if (WorldgenUtil.isReplaceable(world, pos) && !WorldgenUtil.isReplaceable(world, pos.down())) {
                                        world.setBlockState(pos, MetaBlocks.SURFACE_ROCK.get(lastOre).getBlock(lastOre));
                                        lastOre = null;
                                    }
                                }
                            }
                            canPlaceRocks = false;
                        }
                    } else {
                        // Just check if the last Block was Opaque and of the right kind of Material
                        if (state.isOpaqueCube()) {
                            net.minecraft.block.material.Material mat = state.getMaterial();
                            canPlaceRocks = mat == net.minecraft.block.material.Material.CLAY || mat == net.minecraft.block.material.Material.SAND || mat == net.minecraft.block.material.Material.GRASS || mat == net.minecraft.block.material.Material.GROUND;
                        } else {
                            lastOre = null;
                            canPlaceRocks = false;
                        }
                    }

                    // And scan for next Block on the Stone Layer Type
                    //noinspection ManualArrayCopy
                    for (int t = 0; t < scanMinusOne; t++) {
                        scan[t] = scan[t + 1];
                    }
                    scan[scanMinusOne] = StoneLayer.LAYERS.get(noise.get(x, y - 2 + scanMinusOne, z, listSize));

                    // Ores that should not generate too deeply will be replaced by Slate. This prevents flammable Ores near Lava in most cases.
                    if (y - 2 + scanMinusOne < 24 && scan[scanMinusOne].useSlate) scan[scanMinusOne] = StoneLayer.SLATE;
                }
            }
        }
        return true;
    }
}
