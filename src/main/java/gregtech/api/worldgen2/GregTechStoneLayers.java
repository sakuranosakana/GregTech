package gregtech.api.worldgen2;

import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.StoneTypes;
import gregtech.api.worldgen2.generator.WorldgenStoneLayers;
import gregtech.api.worldgen2.stonelayer.StoneLayer;
import gregtech.api.worldgen2.stonelayer.StoneLayerOres;
import gregtech.common.blocks.BlockStoneCobble;
import gregtech.common.blocks.BlockStoneCobbleMossy;
import gregtech.common.blocks.BlockStoneSmooth;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;

import java.util.Collections;

import static gregtech.api.GTValues.M;
import static gregtech.api.GTValues.MODID;
import static gregtech.api.worldgen2.BiomeSets.*;

public class GregTechStoneLayers {

    public static void init() {
        // this needs to go first
        new WorldgenStoneLayers("stonelayers", MODID, true, Collections.singletonList(GregTechWorldgen.WORLDGEN_GREGTECH));

        StoneLayer.SLATE = new StoneLayer(StoneTypes.SLATE, MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.SLATE), MetaBlocks.STONE_COBBLE.getState(BlockStoneCobble.BlockType.SLATE), MetaBlocks.STONE_COBBLE_MOSSY.getState(BlockStoneCobbleMossy.BlockType.SLATE), Materials.Slate,
                new StoneLayerOres(Materials.Emerald, M / 64, 0, 32, MOUNTAINS),
                new StoneLayerOres(Materials.Diamond, M / 64, 0, 12, JUNGLE, VOLCANIC),
                new StoneLayerOres(Materials.Lapis, M / 12, 16, 24, FROZEN, TAIGA),
                new StoneLayerOres(Materials.Redstone, M / 16, 0, 20),
                new StoneLayerOres(Materials.Gold, M / 32, 0, 16, MESA),
                new StoneLayerOres(Materials.Copper, M / 16, 0, 32, DESERT, SAVANNA),
                new StoneLayerOres(Materials.Iron, M / 16, 0, 32, SWAMP, WOODS),
                new StoneLayerOres(Materials.Coal, M / 16, 0, 32, PLAINS, SHROOM),
                new StoneLayerOres(Materials.Electrotine, M / 32, 0, 20)
        );

        // vanilla stone
        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.STONE, Blocks.STONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), Blocks.MOSSY_COBBLESTONE.getDefaultState(), Materials.Stone,
                new StoneLayerOres(Materials.Emerald, M / 48, 16, 60, MOUNTAINS),
                new StoneLayerOres(Materials.Diamond, M / 128, 8, 24)
                , new StoneLayerOres(Materials.Lapis, M / 48, 16, 48)
                , new StoneLayerOres(Materials.Redstone, M / 32, 8, 24)
                , new StoneLayerOres(Materials.Cinnabar, M / 128, 8, 24)
                , new StoneLayerOres(Materials.Gold, M / 64, 8, 32)
                , new StoneLayerOres(Materials.Gold, M / 32, 33, 64, MESA)
                , new StoneLayerOres(Materials.Copper, M / 16, 20, 50, MESA, DESERT, SAVANNA)
                , new StoneLayerOres(Materials.Iron, M / 16, 40, 80)
                , new StoneLayerOres(Materials.Coal, M / 8, 60, 100)
                , new StoneLayerOres(Materials.Stone, M / 48, 0, 255, Blocks.MONSTER_EGG.getDefaultState())
        ));

        // basaltics
        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.KOMATIITE, MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.KOMATIITE), MetaBlocks.STONE_COBBLE.getState(BlockStoneCobble.BlockType.KOMATIITE), MetaBlocks.STONE_COBBLE_MOSSY.getState(BlockStoneCobbleMossy.BlockType.KOMATIITE), Materials.Komatiite,
                new StoneLayerOres(Materials.Magnesite, M / 16, 20, 50),
                new StoneLayerOres(Materials.Cinnabar, M / 12, 0, 32),
                new StoneLayerOres(Materials.Redstone, M / 8, 0, 30),
                new StoneLayerOres(Materials.Pyrite, M / 12, 0, 50)
        ));

        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.KIMBERLITE, MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.KIMBLERLITE), MetaBlocks.STONE_COBBLE.getState(BlockStoneCobble.BlockType.KIMBLERLITE), MetaBlocks.STONE_COBBLE_MOSSY.getState(BlockStoneCobbleMossy.BlockType.KIMBLERLITE), Materials.Kimberlite));
        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.KIMBERLITE, MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.KIMBLERLITE), MetaBlocks.STONE_COBBLE.getState(BlockStoneCobble.BlockType.KIMBLERLITE), MetaBlocks.STONE_COBBLE_MOSSY.getState(BlockStoneCobbleMossy.BlockType.KIMBLERLITE), Materials.Kimberlite,
                new StoneLayerOres(Materials.Diamond, M / 48, 0, 12),
                new StoneLayerOres(Materials.Ruby, M / 48, 24, 48, MOUNTAINS, JUNGLE)
        ));

        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.BASALT, MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.BASALT), MetaBlocks.STONE_COBBLE.getState(BlockStoneCobble.BlockType.BASALT), MetaBlocks.STONE_COBBLE_MOSSY.getState(BlockStoneCobbleMossy.BlockType.BASALT), Materials.Basalt));
        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.BASALT, MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.BASALT), MetaBlocks.STONE_COBBLE.getState(BlockStoneCobble.BlockType.BASALT), MetaBlocks.STONE_COBBLE_MOSSY.getState(BlockStoneCobbleMossy.BlockType.BASALT), Materials.Basalt,
                new StoneLayerOres(Materials.Asbestos, M / 16, 16, 48),
                new StoneLayerOres(Materials.Sulfur, M / 8, 0, 32)
        ));
        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.BASALT, MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.BASALT), MetaBlocks.STONE_COBBLE.getState(BlockStoneCobble.BlockType.BASALT), MetaBlocks.STONE_COBBLE_MOSSY.getState(BlockStoneCobbleMossy.BlockType.BASALT), Materials.Basalt,
                new StoneLayerOres(Materials.Bastnasite, M / 24, 24, 32),
                new StoneLayerOres(Materials.Monazite, M / 32, 24, 32),
                new StoneLayerOres(Materials.Pyrolusite, M / 8, 16, 48)
        ));
        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.BASALT, MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.BASALT), MetaBlocks.STONE_COBBLE.getState(BlockStoneCobble.BlockType.BASALT), MetaBlocks.STONE_COBBLE_MOSSY.getState(BlockStoneCobbleMossy.BlockType.BASALT), Materials.Basalt,
                new StoneLayerOres(Materials.Olivine, M / 32, 0, 32),
                new StoneLayerOres(Materials.Uvarovite, M / 32, 8, 40),
                new StoneLayerOres(Materials.Grossular, M / 32, 16, 48),
                new StoneLayerOres(Materials.Chromite, M / 8, 0, 32)
        ));

        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.ANDESITE, Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE), Materials.Andesite));
        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.ANDESITE, Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE), Materials.Andesite,
                new StoneLayerOres(Materials.Gold, M / 12, 0, 32),
                new StoneLayerOres(Materials.Gold, M / 8, 33, 64, MESA)
        ));

        // chalky stuff
        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.MARBLE, MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.MARBLE), MetaBlocks.STONE_COBBLE.getState(BlockStoneCobble.BlockType.MARBLE), MetaBlocks.STONE_COBBLE_MOSSY.getState(BlockStoneCobbleMossy.BlockType.MARBLE), Materials.Marble));
        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.MARBLE, MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.MARBLE), MetaBlocks.STONE_COBBLE.getState(BlockStoneCobble.BlockType.MARBLE), MetaBlocks.STONE_COBBLE_MOSSY.getState(BlockStoneCobbleMossy.BlockType.MARBLE), Materials.Marble,
                new StoneLayerOres(Materials.Cassiterite, M / 16, 40, 80),
                new StoneLayerOres(Materials.Sphalerite, M / 8, 10, 30),
                new StoneLayerOres(Materials.Chalcopyrite, M / 8, 0, 20),
                new StoneLayerOres(Materials.Pyrite, M / 12, 0, 30)
        ));

        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.LIMESTONE, MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.LIMESTONE), MetaBlocks.STONE_COBBLE.getState(BlockStoneCobble.BlockType.LIMESTONE), MetaBlocks.STONE_COBBLE_MOSSY.getState(BlockStoneCobbleMossy.BlockType.LIMESTONE), Materials.Limestone));
        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.LIMESTONE, MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.LIMESTONE), MetaBlocks.STONE_COBBLE.getState(BlockStoneCobble.BlockType.LIMESTONE), MetaBlocks.STONE_COBBLE_MOSSY.getState(BlockStoneCobbleMossy.BlockType.LIMESTONE), Materials.Limestone,
                new StoneLayerOres(Materials.Salt, M / 8, 40, 80)
        ));
        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.LIMESTONE, MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.LIMESTONE), MetaBlocks.STONE_COBBLE.getState(BlockStoneCobble.BlockType.LIMESTONE), MetaBlocks.STONE_COBBLE_MOSSY.getState(BlockStoneCobbleMossy.BlockType.LIMESTONE), Materials.Limestone,
                new StoneLayerOres(Materials.Stibnite, M / 24, 10, 30),
                new StoneLayerOres(Materials.Stibnite, M / 8, 30, 120),
                new StoneLayerOres(Materials.Lead, M / 16, 50, 70)
        ));
        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.LIMESTONE, MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.LIMESTONE), MetaBlocks.STONE_COBBLE.getState(BlockStoneCobble.BlockType.LIMESTONE), MetaBlocks.STONE_COBBLE_MOSSY.getState(BlockStoneCobbleMossy.BlockType.LIMESTONE), Materials.Limestone,
                new StoneLayerOres(Materials.Pyrite, M / 16, 0, 30),
                new StoneLayerOres(Materials.Galena, M / 8, 5, 25),
                new StoneLayerOres(Materials.Galena, M / 8, 80, 120),
                new StoneLayerOres(Materials.Wulfenite, M / 32, 30, 45),
                new StoneLayerOres(Materials.Powellite, M / 32, 35, 50),
                new StoneLayerOres(Materials.Molybdenite, M / 128, 30, 50),
                new StoneLayerOres(Materials.Tetrahedrite, M / 8, 40, 80),
                new StoneLayerOres(Materials.Copper, M / 16, 40, 80)
        ));
        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.LIMESTONE, MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.LIMESTONE), MetaBlocks.STONE_COBBLE.getState(BlockStoneCobble.BlockType.LIMESTONE), MetaBlocks.STONE_COBBLE_MOSSY.getState(BlockStoneCobbleMossy.BlockType.LIMESTONE), Materials.Limestone,
                new StoneLayerOres(Materials.Scheelite, M / 64, 0, 16),
                new StoneLayerOres(Materials.Tungstate, M / 64, 0, 16),
                new StoneLayerOres(Materials.YellowLimonite, M / 8, 16, 48),
                new StoneLayerOres(Materials.BrownLimonite, M / 8, 32, 64),
                new StoneLayerOres(Materials.Malachite, M / 12, 16, 64)
        ));

        // granites
        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.BLACK_GRANITE, MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.BLACK_GRANITE), MetaBlocks.STONE_COBBLE.getState(BlockStoneCobble.BlockType.BLACK_GRANITE), MetaBlocks.STONE_COBBLE_MOSSY.getState(BlockStoneCobbleMossy.BlockType.BLACK_GRANITE), Materials.GraniteBlack));
        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.BLACK_GRANITE, MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.BLACK_GRANITE), MetaBlocks.STONE_COBBLE.getState(BlockStoneCobble.BlockType.BLACK_GRANITE), MetaBlocks.STONE_COBBLE_MOSSY.getState(BlockStoneCobbleMossy.BlockType.BLACK_GRANITE), Materials.GraniteBlack,
                new StoneLayerOres(Materials.Cooperite, M / 32, 0, 16),
                new StoneLayerOres(Materials.Emerald, M / 64, 24, 48)
        ));

        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.RED_GRANITE, MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.RED_GRANITE), MetaBlocks.STONE_COBBLE.getState(BlockStoneCobble.BlockType.RED_GRANITE), MetaBlocks.STONE_COBBLE_MOSSY.getState(BlockStoneCobbleMossy.BlockType.RED_GRANITE), Materials.GraniteRed));
        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.RED_GRANITE, MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.RED_GRANITE), MetaBlocks.STONE_COBBLE.getState(BlockStoneCobble.BlockType.RED_GRANITE), MetaBlocks.STONE_COBBLE_MOSSY.getState(BlockStoneCobbleMossy.BlockType.RED_GRANITE), Materials.GraniteRed,
                new StoneLayerOres(Materials.Pitchblende, M / 32, 0, 18),
                new StoneLayerOres(Materials.Uraninite, M / 32, 0, 16),
                new StoneLayerOres(Materials.Tantalite, M / 64, 30, 40)
        ));

        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.GRANITE, Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), Materials.Granite));
        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.GRANITE, Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), Materials.Granite,
                new StoneLayerOres(Materials.Sulfur, M / 12, 0, 32)).setSlate());
        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.GRANITE, Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), Materials.Granite,
                new StoneLayerOres(Materials.Saltpeter, M / 12, 16, 48)).setSlate());
        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.GRANITE, Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), Materials.Granite,
                new StoneLayerOres(Materials.BlueTopaz, M / 64, 8, 32, OCEAN_BEACH),
                new StoneLayerOres(Materials.Topaz, M / 64, 24, 48, FROZEN)
        ));
        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.GRANITE, Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), Materials.Granite,
                new StoneLayerOres(Materials.Apatite, M / 8, 32, 64),
                new StoneLayerOres(Materials.TricalciumPhosphate, M / 24, 36, 60)
        ));

        // diorite
        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.DIORITE, Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), Materials.Diorite));
        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.DIORITE, Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), Materials.Diorite,
                new StoneLayerOres(Materials.Sapphire, M / 64, 24, 48, FROZEN),
                new StoneLayerOres(Materials.GreenSapphire, M / 64, 24, 48, JUNGLE, MOUNTAINS, SAVANNA, OCEAN_BEACH, TAIGA, DESERT),
                new StoneLayerOres(Materials.Ruby, M / 64, 24, 48, DESERT)
        ));
        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.DIORITE, Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), Materials.Diorite,
                new StoneLayerOres(Materials.Garnierite, M / 8, 24, 56),
                new StoneLayerOres(Materials.Pentlandite, M / 8, 24, 56),
                new StoneLayerOres(Materials.Cobaltite, M / 8, 32, 64),
                new StoneLayerOres(Materials.Amethyst, M / 64, 24, 48, TAIGA)
        ));

        // quartzite
        StoneLayer.LAYERS.add(new StoneLayer(StoneTypes.QUARTZITE, MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.QUARTZITE), MetaBlocks.STONE_COBBLE.getState(BlockStoneCobble.BlockType.QUARTZITE), MetaBlocks.STONE_COBBLE_MOSSY.getState(BlockStoneCobbleMossy.BlockType.QUARTZITE), Materials.Quartzite,
                new StoneLayerOres(Materials.CertusQuartz, M / 16, 16, 48),
                new StoneLayerOres(Materials.Barite, M / 32, 0, 32)
        ));

        // others
        StoneLayer.bothSides(Materials.Komatiite, Materials.Basalt,
                new StoneLayerOres(Materials.Perlite, M / 4, 0, 16)
        );

        StoneLayer.bothSides(Materials.Limestone, Materials.Basalt,
                new StoneLayerOres(Materials.Opal, M / 64, 48, 64)
        );

        StoneLayer.bothSides(Materials.Limestone, Materials.Basalt,
                new StoneLayerOres(Materials.Ilmenite, M / 8, 0, 32),
                new StoneLayerOres(Materials.Rutile, M / 12, 0, 32)
        );

        StoneLayer.bothSides(Materials.Limestone, Materials.Quartzite,
                new StoneLayerOres(Materials.Kyanite, M / 16, 32, 72),
                new StoneLayerOres(Materials.Lepidolite, M / 32, 16, 48),
                new StoneLayerOres(Materials.Spodumene, M / 32, 32, 64),
                new StoneLayerOres(Materials.Tantalite, M / 128, 16, 48)
        );

        StoneLayer.bothSides(Materials.Talc, Materials.Salt,
                new StoneLayerOres(Materials.Borax, M / 8, 16, 48)
        );

        StoneLayer.bothSides(Materials.Granite, Materials.Salt,
                new StoneLayerOres(Materials.Zeolite, M / 8, 16, 48)
        );

        StoneLayer.bothSides(Materials.Granite, Materials.RockSalt,
                new StoneLayerOres(Materials.Pollucite, M / 8, 16, 48)
        );

        StoneLayer.bothSides(Materials.GraniteBlack, Materials.Marble,
                new StoneLayerOres(Materials.Lapis, M / 8, 0, 48),
                new StoneLayerOres(Materials.Sodalite, M / 16, 0, 48),
                new StoneLayerOres(Materials.Lazurite, M / 16, 0, 48),
                new StoneLayerOres(Materials.Pyrite, M / 16, 0, 48)
        );

        StoneLayer.bothSides(Materials.GraniteBlack, Materials.Basalt,
                new StoneLayerOres(Materials.Diamond, M / 64, 0, 32),
                new StoneLayerOres(Materials.Graphite, M / 8, 0, 32)
        );
    }
}
