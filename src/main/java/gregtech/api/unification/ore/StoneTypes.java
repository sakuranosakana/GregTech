package gregtech.api.unification.ore;

import gregtech.api.unification.material.Materials;
import gregtech.common.blocks.BlockStoneSmooth;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStone.EnumType;
import net.minecraft.block.SoundType;
import net.minecraft.init.Blocks;

public class StoneTypes {

    // Real Types that drop custom Ores

    public static final StoneType STONE = new StoneType(0, "stone", SoundType.STONE, OrePrefix.ore, Materials.Stone,
            () -> Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, EnumType.STONE),
            state -> state.getBlock() instanceof BlockStone && state.getValue(BlockStone.VARIANT) == BlockStone.EnumType.STONE, true);

    public static StoneType NETHERRACK = new StoneType(1, "netherrack", SoundType.STONE, OrePrefix.oreNetherrack, Materials.Netherrack,
            Blocks.NETHERRACK::getDefaultState,
            state -> state.getBlock() == Blocks.NETHERRACK, true);

    public static StoneType ENDSTONE = new StoneType(2, "endstone", SoundType.STONE, OrePrefix.oreEndstone, Materials.Endstone,
            Blocks.END_STONE::getDefaultState,
            state -> state.getBlock() == Blocks.END_STONE, true);


    // Dummy Types used for better world generation

    public static StoneType SANDSTONE = new StoneType(3, "sandstone", SoundType.STONE, OrePrefix.oreSand, Materials.SiliconDioxide,
            () -> Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.DEFAULT),
            state -> state.getBlock() instanceof BlockSandStone && state.getValue(BlockSandStone.TYPE) == BlockSandStone.EnumType.DEFAULT, false);

    public static StoneType RED_SANDSTONE = new StoneType(4, "red_sandstone", SoundType.STONE, OrePrefix.oreRedSand, Materials.SiliconDioxide,
            () -> Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.DEFAULT),
            state -> state.getBlock() instanceof BlockRedSandstone && state.getValue(BlockRedSandstone.TYPE) == BlockRedSandstone.EnumType.DEFAULT, false);

    public static StoneType GRANITE = new StoneType(5, "granite", SoundType.STONE, OrePrefix.oreGranite, Materials.Granite,
            () -> Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, EnumType.GRANITE),
            state -> state.getBlock() instanceof BlockStone && state.getValue(BlockStone.VARIANT) == EnumType.GRANITE, false);

    public static StoneType DIORITE = new StoneType(6, "diorite", SoundType.STONE, OrePrefix.oreDiorite, Materials.Diorite,
            () -> Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, EnumType.DIORITE),
            state -> state.getBlock() instanceof BlockStone && state.getValue(BlockStone.VARIANT) == EnumType.DIORITE, false);

    public static StoneType ANDESITE = new StoneType(7, "andesite", SoundType.STONE, OrePrefix.oreAndesite, Materials.Andesite,
            () -> Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE),
            state -> state.getBlock() instanceof BlockStone && state.getValue(BlockStone.VARIANT) == EnumType.ANDESITE, false);

    public static StoneType BLACK_GRANITE = new StoneType(8, "black_granite", SoundType.STONE, OrePrefix.oreBlackgranite, Materials.GraniteBlack,
            () -> MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.BLACK_GRANITE),
            state -> state.getBlock() instanceof BlockStoneSmooth && ((BlockStoneSmooth) state.getBlock()).getVariant(state) == BlockStoneSmooth.BlockType.BLACK_GRANITE, false);

    public static StoneType RED_GRANITE = new StoneType(9, "red_granite", SoundType.STONE, OrePrefix.oreRedgranite, Materials.GraniteRed,
            () -> MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.RED_GRANITE),
            state -> state.getBlock() instanceof BlockStoneSmooth && ((BlockStoneSmooth) state.getBlock()).getVariant(state) == BlockStoneSmooth.BlockType.RED_GRANITE, false);

    public static StoneType MARBLE = new StoneType(10, "marble", SoundType.STONE, OrePrefix.oreMarble, Materials.Marble,
            () -> MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.MARBLE),
            state -> state.getBlock() instanceof BlockStoneSmooth && ((BlockStoneSmooth) state.getBlock()).getVariant(state) == BlockStoneSmooth.BlockType.MARBLE, false);

    public static StoneType BASALT = new StoneType(11, "basalt", SoundType.STONE, OrePrefix.oreBasalt, Materials.Basalt,
            () -> MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.BASALT),
            state -> state.getBlock() instanceof BlockStoneSmooth && ((BlockStoneSmooth) state.getBlock()).getVariant(state) == BlockStoneSmooth.BlockType.BASALT, false);

    public static StoneType SLATE = new StoneType(12, "slate", SoundType.STONE, OrePrefix.oreSlate, Materials.Slate,
            () -> MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.SLATE),
            state -> state.getBlock() instanceof BlockStoneSmooth && ((BlockStoneSmooth) state.getBlock()).getVariant(state) == BlockStoneSmooth.BlockType.SLATE, false);

    public static StoneType KOMATIITE = new StoneType(13, "komatiite", SoundType.STONE, OrePrefix.oreKomatiite, Materials.Komatiite,
            () -> MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.KOMATIITE),
            state -> state.getBlock() instanceof BlockStoneSmooth && ((BlockStoneSmooth) state.getBlock()).getVariant(state) == BlockStoneSmooth.BlockType.KOMATIITE, false);

    public static StoneType KIMBERLITE = new StoneType(14, "kimberlite", SoundType.STONE, OrePrefix.oreKimberlite, Materials.Kimberlite,
            () -> MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.KIMBLERLITE),
            state -> state.getBlock() instanceof BlockStoneSmooth && ((BlockStoneSmooth) state.getBlock()).getVariant(state) == BlockStoneSmooth.BlockType.KIMBLERLITE, false);

    public static StoneType LIMESTONE = new StoneType(15, "limestone", SoundType.STONE, OrePrefix.oreLimestone, Materials.Limestone,
            () -> MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.LIMESTONE),
            state -> state.getBlock() instanceof BlockStoneSmooth && ((BlockStoneSmooth) state.getBlock()).getVariant(state) == BlockStoneSmooth.BlockType.LIMESTONE, false);

    public static StoneType QUARTZITE = new StoneType(16, "quartzite", SoundType.STONE, OrePrefix.oreQuartzite, Materials.Quartzite,
            () -> MetaBlocks.STONE_SMOOTH.getState(BlockStoneSmooth.BlockType.QUARTZITE),
            state -> state.getBlock() instanceof BlockStoneSmooth && ((BlockStoneSmooth) state.getBlock()).getVariant(state) == BlockStoneSmooth.BlockType.QUARTZITE, false);
}
