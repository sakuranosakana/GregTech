package gregtech.api.multitileentity;

import codechicken.lib.texture.TextureUtils;
import com.google.common.base.Predicate;
import gregtech.api.GTValues;
import gregtech.api.block.BlockCustomParticle;
import gregtech.api.multitileentity.IMultiTileEntity.*;
import gregtech.api.pipenet.IBlockAppearance;
import gregtech.api.util.GTUtility;
import gregtech.integration.ctm.IFacadeWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.ForgeEventFactory;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static gregtech.api.GTValues.LAST_BROKEN_TILEENTITY;

@SuppressWarnings("ALL")
public class MultiTileEntityBlock extends BlockCustomParticle implements ITileEntityProvider, IFacadeWrapper, IBlockAppearance {

    // todo register these in the event
    private static final Map<String, MultiTileEntityBlock> MULTI_TILE_ENTITY_BLOCK_MAP = new HashMap<>();

    private final int harvestLevel;
    private final String nameInternal, harvestTool;
    private final boolean opaque, normalCube;

    // todo
    public static String getName() {return "";}

    //public static MultiTileEntityBlock getOrCreate(String modID, String name, Material material, SoundType ) {}

    private MultiTileEntityBlock(String modID, String name, Material material, SoundType stepOnSound, String harvestTool, int harvestLevel, boolean opaque, boolean normalCube) {
        super(material);
        // todo check stage?

        nameInternal = "";
        MULTI_TILE_ENTITY_BLOCK_MAP.put(modID + ":" + nameInternal, this);

        setSoundType(stepOnSound);
        this.opaque = opaque;
        this.normalCube = normalCube;

        this.harvestTool = harvestTool.toLowerCase();
        this.harvestLevel = harvestLevel;
        this.lightOpacity = opaque ? 255 : 0;
        // ST.hide(this);
    }

    @Override
    public final void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te != null) LAST_BROKEN_TILEENTITY.set(te);
        if (te == null || !te.shouldRefresh(worldIn, pos, state, state)) return;
        if (te instanceof IMTEBreakBlock && ((IMTEBreakBlock) te).breakBlock()) return;
        worldIn.removeTileEntity(pos);
    }

    private static boolean LOCK = false;

    @Override
    public boolean isFullBlock(IBlockState state) {
        return super.isFullBlock(state);
    }

    @Override
    public boolean canEntitySpawn(IBlockState state, Entity entityIn) {
        return super.canEntitySpawn(state, entityIn);
    }

    @Override
    public boolean isTranslucent(IBlockState state) {
        return super.isTranslucent(state);
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return super.getMapColor(state, worldIn, pos);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return super.getStateFromMeta(meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return super.getMetaFromState(state);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return super.getActualState(state, worldIn, pos);
    }

    @Override public final boolean isBlockNormalCube(IBlockState state) {return normalCube;}
    @Override public final boolean isNormalCube(IBlockState state) {return normalCube;}
    @Override public boolean isFullCube(IBlockState state) {return !opaque;}
    @Override public final boolean isPassable(IBlockAccess worldIn, BlockPos pos) {TileEntity te = worldIn.getTileEntity(pos); return !(te instanceof IMTEIsPassable) || ((IMTEIsPassable) te).isPassable();}

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return super.getRenderType(state);
    }

    @Override public final boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {TileEntity te = worldIn.getTileEntity(pos); return te instanceof IMTEIsReplaceable ? ((IMTEIsReplaceable) te).isReplaceable() : material.isReplaceable();}
    @Override public final float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {TileEntity te = worldIn.getTileEntity(pos); return te instanceof IMTEGetBlockHardness ? ((IMTEGetBlockHardness) te).getBlockHardness() : 1.0F;}
    @Override public final boolean hasTileEntity() {return true;}
    @Override public final boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {TileEntity te = blockAccess.getTileEntity(pos.offset(side)); return te instanceof IMTEShouldSideBeRendered ? ((IMTEShouldSideBeRendered) te).shouldSideBeRendered(side) : super.shouldSideBeRendered(blockState, blockAccess, pos, side);}
    @Override public final BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {TileEntity te = worldIn.getTileEntity(pos); return te instanceof IMTEGetBlockFaceShape ? ((IMTEGetBlockFaceShape) te).getBlockFaceShape(face) : BlockFaceShape.SOLID;}
    @Override public final void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean isActualState) {TileEntity te = worldIn.getTileEntity(pos); if (te instanceof IMTEAddCollisionBoxToList) ((IMTEAddCollisionBoxToList) te).addCollisionBoxToList(entityBox, collidingBoxes, entityIn); else if (te != null) super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);}
    @Override public final AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {TileEntity te = worldIn.getTileEntity(pos); return te instanceof IMTEGetCollisionBoundingBox ? ((IMTEGetCollisionBoundingBox) te).getCollisionBoundingBox() : te == null ? null : new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX()+1, pos.getY()+1, pos.getZ()+1);}
    @Override public final AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {TileEntity te = worldIn.getTileEntity(pos); return te == null ? new AxisAlignedBB(-999, -999, -999, -998, -998, -998) : te instanceof IMTEGetSelectedBoundingBox ? ((IMTEGetSelectedBoundingBox) te).getSelectedBoundingBox() : new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX()+1, pos.getY()+1, pos.getZ()+1);}
    @Override public final boolean isOpaqueCube(IBlockState state) {return opaque;}

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
        super.randomTick(worldIn, pos, state, random);
    }

    @Override public final void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {TileEntity te = worldIn.getTileEntity(pos); if (te instanceof IMTEUpdateTick) ((IMTEUpdateTick) te).updateTick(rand);}
    @Override public final void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {TileEntity te = worldIn.getTileEntity(pos); if (te instanceof IMTERandomDisplayTick) ((IMTERandomDisplayTick) te).randomDisplayTick(rand); else super.randomDisplayTick(stateIn, worldIn, pos, rand);}
    @Override public final void onPlayerDestroy(World worldIn, BlockPos pos, IBlockState state) {TileEntity te = GTUtility.getTileEntity(worldIn, pos, true); if (te instanceof IMTEOnPlayerDestroy) ((IMTEOnPlayerDestroy) te).onPlayerDestroy();}
    @Override public final void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {TileEntity te = worldIn.getTileEntity(pos); if (te instanceof IMTEOnBlockAdded) ((IMTEOnBlockAdded) te).onBlockAdded();}
    @Override public final float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos) {TileEntity te = worldIn.getTileEntity(pos); return te instanceof IMTEGetPlayerRelativeBlockHardness ? ((IMTEGetPlayerRelativeBlockHardness) te).getPlayerRelativeBlockHardness(player, super.getPlayerRelativeBlockHardness(state, player, worldIn, pos)) : super.getPlayerRelativeBlockHardness(state, player, worldIn, pos);}
    @Override public final void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {TileEntity te = GTUtility.getTileEntity(worldIn, pos, true); if (te instanceof IMTEGetDrops) {NonNullList<ItemStack> stacks = NonNullList.create(); ((IMTEGetDrops) te).getDrops(stacks, fortune, false); chance = ForgeEventFactory.fireBlockHarvesting(stacks, worldIn, pos, state, fortune, chance, false, harvesters.get()); for (ItemStack stack : stacks) if (GTValues.RNG.nextFloat() <= chance) spawnAsEntity(worldIn, pos, stack);}}
    @Override public final void dropXpOnBlockBreak(World worldIn, BlockPos pos, int amount) {TileEntity te = worldIn.getTileEntity(pos); if (te instanceof IMTEDropXpOnBlockBreak) ((IMTEDropXpOnBlockBreak) te).dropXpOnBlockBreak(amount); else super.dropXpOnBlockBreak(worldIn, pos, amount);}
    @Override public final RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {TileEntity te = worldIn.getTileEntity(pos); return te instanceof IMTECollisionRayTrace ? ((IMTECollisionRayTrace) te).collisionRayTrace(start, end) : super.collisionRayTrace(blockState, worldIn, pos, start, end);}
    @Override public final boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {TileEntity te = worldIn.getTileEntity(pos); return te instanceof IMTEOnBlockActivated && ((IMTEOnBlockActivated) te).onBlockActivated(playerIn, facing, hitX, hitY, hitZ);}
    @Override public final void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {TileEntity te = worldIn.getTileEntity(pos); if (te instanceof IMTEOnEntityWalk) ((IMTEOnEntityWalk) te).onEntityWalk(entityIn);}
    @Override public final void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {TileEntity te = worldIn.getTileEntity(pos); if (te instanceof IMTEOnBlockClicked) ((IMTEOnBlockClicked) te).onBlockClicked(playerIn);}
    @Override public final Vec3d modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3d motion) {TileEntity te = worldIn.getTileEntity(pos); return te instanceof IMTEModifyAcceleration ? ((IMTEModifyAcceleration) te).modifyAcceleration(entityIn, motion) : super.modifyAcceleration(worldIn, pos, entityIn, motion);}
    @Override public final int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {TileEntity te = blockAccess.getTileEntity(pos); return te instanceof IMTEGetWeakPower ? ((IMTEGetWeakPower) te).getWeakPower(side) : 0;}
    @Override public final boolean canProvidePower(IBlockState state) {return !normalCube;}
    @Override public final void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {TileEntity te = worldIn.getTileEntity(pos); if (te instanceof IMTEOnEntityCollision) ((IMTEOnEntityCollision) te).onEntityCollision(entityIn);}
    @Override public final int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {TileEntity te = blockAccess.getTileEntity(pos); return te instanceof IMTEGetStrongPower ? ((IMTEGetStrongPower) te).getStrongPower(side) : 0;}
    @Override public final void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {if (player == null) player = harvesters.get(); player.addStat(StatList.MINE_BLOCK_STATS.get(getIdFromBlock(this)), 1); player.addExhaustion(0.025F); boolean silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player.getHeldItemMainhand()) > 0; int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, player.getHeldItemMainhand()); te = GTUtility.getTileEntity(worldIn, pos, true); if (te instanceof IMTEGetDrops) {NonNullList<ItemStack> stacks = NonNullList.create(); ((IMTEGetDrops) te).getDrops(stacks, fortune, silkTouch); float chance = ForgeEventFactory.fireBlockHarvesting(stacks, worldIn, pos, state, fortune, 1.0F, silkTouch, player); for (ItemStack stack1 : stacks) if (GTValues.RNG.nextFloat() <= chance) spawnAsEntity(worldIn, pos, stack1);}}
    @Override protected final boolean canSilkHarvest() {return false;}

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public String getLocalizedName() {
        return super.getLocalizedName();
    }

    @Override public final String getTranslationKey() {return nameInternal;}
    @Override public final boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {TileEntity te = worldIn.getTileEntity(pos); return te == null || te.receiveClientEvent(id, param);}
    @Override public final void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {TileEntity te = worldIn.getTileEntity(pos); if (te instanceof IMTEOnFallenUpon) ((IMTEOnFallenUpon) te).onFallenUpon(entityIn, fallDistance); else super.onFallenUpon(worldIn, pos, entityIn, fallDistance);}
    @Override public final void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {/**/}
    @Override public final void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {TileEntity te = worldIn.getTileEntity(pos); if (te instanceof IMTEOnBlockHarvested) ((IMTEOnBlockHarvested) te).onBlockHarvested(state, player);}
    @Override public final void fillWithRain(World worldIn, BlockPos pos) {TileEntity te = worldIn.getTileEntity(pos); if (te instanceof IMTEFillWithRain) ((IMTEFillWithRain) te).fillWithRain();}
    @Override public final boolean hasComparatorInputOverride(IBlockState state) {return true;}

    //@Override
    //public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
   //     TileEntity te = worldIn.getTileEntity(pos);
    //    if (te instanceof IMTEGetComparatorInputOverride) {

    //    } else if (te instanceof IMTEIsProvidingWeakPower)
    //    return super.getComparatorInputOverride(blockState, worldIn, pos);
    //}

    @Override
    protected BlockStateContainer createBlockState() {
        return super.createBlockState();
    }

    @Override
    public BlockStateContainer getBlockState() {
        return super.getBlockState();
    }

    @Override
    public Vec3d getOffset(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return super.getOffset(state, worldIn, pos);
    }

    //@Override
    //public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    //    super.addInformation(stack, worldIn, tooltip, flagIn);
    //}

    @Override public final int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEGetLightValue ? Math.max(0, Math.min(((IMTEGetLightValue) te).getLightValue(), 15)) : super.getLightValue(state, world, pos);}
    @Override public final boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEIsLadder && ((IMTEIsLadder) te).isLadder(entity);}
    @Override public final boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEIsNormalCube ? ((IMTEIsNormalCube) te).isNormalCube() : normalCube;}

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return super.doesSideBlockRendering(state, world, pos, face);
    }

    @Override public final boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEIsSideSolid ? ((IMTEIsSideSolid) te).isSideSolid(side) : opaque;}
    @Override public final boolean isBurning(IBlockAccess world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEIsBurning && ((IMTEIsBurning) te).isBurning();}
    @Override public final boolean isAir(IBlockState state, IBlockAccess world, BlockPos pos) {if (world == null) return false; TileEntity te = world.getTileEntity(pos); return te instanceof IMTEIsAir && ((IMTEIsAir) te).isAir();}
    @Override public final boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {TileEntity te = GTUtility.getTileEntity(world, pos, true); if (te != null) LAST_BROKEN_TILEENTITY.set(te); return te instanceof IMTERemovedByPlayer ? ((IMTERemovedByPlayer) te).removedByPlayer(world, player, willHarvest) : super.removedByPlayer(state, world, pos, player, willHarvest);}
    @Override public final int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEGetFlammability ? ((IMTEGetFlammability) te).getFlammability(face, getMaterial(null).getCanBurn()) : getMaterial(null).getCanBurn() ? 150 : 0;}
    @Override public final int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEGetFireSpreadSpeed ? ((IMTEGetFireSpreadSpeed) te).getFireSpreadSpeed(face, getMaterial(null).getCanBurn()) : getMaterial(null).getCanBurn() ? 150 : 0;}
    @Override public final boolean isFireSource(World world, BlockPos pos, EnumFacing side) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEIsFireSource && ((IMTEIsFireSource) te).isFireSource(side);}
    @Override public final boolean hasTileEntity(IBlockState state) {return true;}
    @Override public final TileEntity createTileEntity(World world, IBlockState state) {return null;}
    @Override public final void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {TileEntity te = GTUtility.getTileEntityForced(world, pos); if (te instanceof IMTEGetDrops) ((IMTEGetDrops) te).getDrops(drops, fortune, false);}
    @Override public final boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {return false;}
    @Override public final boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTECanCreatureSpawn && ((IMTECanCreatureSpawn) te).canCreatureSpawn(type);}
    @Override public final void beginLeavesDecay(IBlockState state, World world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); if (te instanceof IMTEBeginLeavesDecay) ((IMTEBeginLeavesDecay) te).beginLeavesDecay();}
    @Override public final boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTECanSustainLeaves && ((IMTECanSustainLeaves) te).canSustainLeaves();}
    @Override public final boolean isLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEIsLeaves && ((IMTEIsLeaves) te).isLeaves();}
    @Override public final boolean canBeReplacedByLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTECanBeReplacedByLeaves && ((IMTECanBeReplacedByLeaves) te).canBeReplacedByLeaves();}
    @Override public final boolean isWood(IBlockAccess world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEIsWood && ((IMTEIsWood) te).isWood();}

    @Override
    public final boolean isReplaceableOreGen(IBlockState state, IBlockAccess world, BlockPos pos, Predicate<IBlockState> target) {
        //if (GAPI.mStartedServerStarted < 1) return false; TODO
        TileEntity te = world.getTileEntity(pos);
        return te instanceof IMTEIsReplaceableOreGen
                ? ((IMTEIsReplaceableOreGen) te).isReplaceableOreGen(target)
                : super.isReplaceableOreGen(state, world, pos, target);
    }

    @Override public final float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEGetExplosionResistance ? ((IMTEGetExplosionResistance) te).getExplosionResistance(exploder, explosion) : 1.0F;}
    @Override public final void onBlockExploded(World world, BlockPos pos, Explosion explosion) {if (world.isRemote) return; TileEntity te = GTUtility.getTileEntity(world, pos, true); if (te != null) LAST_BROKEN_TILEENTITY.set(te); if (te instanceof IMTEOnBlockExploded) ((IMTEOnBlockExploded) te).onBlockExploded(explosion); else world.setBlockToAir(pos);}
    @Override public final boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTECanConnectRedstone ? ((IMTECanConnectRedstone) te).canConnectRedstone(side) : super.canConnectRedstone(state, world, pos, side);}
    @Override public final boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTECanPlaceTorchOnTop ? ((IMTECanPlaceTorchOnTop) te).canPlaceTorchOnTop() : isSideSolid(state, world, pos, EnumFacing.UP);}
    @Override public final ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEGetPickBlock ? ((IMTEGetPickBlock) te).getPickBlock(target) : null;}
    @Override public final boolean isFoliage(IBlockAccess world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEIsFoliage && ((IMTEIsFoliage) te).isFoliage();}
    @Override public final boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTECanSustainPlant ? ((IMTECanSustainPlant) te).canSustainPlant(direction, plantable) : super.canSustainPlant(state, world, pos, direction, plantable);}
    @Override public final void onPlantGrow(IBlockState state, World world, BlockPos pos, BlockPos source) {TileEntity te = world.getTileEntity(pos); if (te instanceof IMTEOnPlantGrow) ((IMTEOnPlantGrow) te).onPlantGrow(source);}
    @Override public final boolean isFertile(World world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEIsFertile && ((IMTEIsFertile) te).isFertile();}
    @Override public final int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEGetLightOpacity ? ((IMTEGetLightOpacity) te).getLightOpacity() : opaque ? 255 : 0;}
    @Override public final boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {TileEntity te = world.getTileEntity(pos); return !(te instanceof IMTECanEntityDestroy) || ((IMTECanEntityDestroy) te).canEntityDestroy(entity);}
    @Override public final boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {TileEntity te = worldObj.getTileEntity(pos); return te instanceof IMTEIsBeaconBase && ((IMTEIsBeaconBase) te).isBeaconBase(pos);}
    @Override public final boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTERotateBlock && ((IMTERotateBlock) te).rotateBlock(axis);}
    @Override public final EnumFacing[] getValidRotations(World world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEGetValidRotations ? ((IMTEGetValidRotations) te).getValidRotations() : null;}
    @Override public final float getEnchantPowerBonus(World world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEGetEnchantPowerBonus ? ((IMTEGetEnchantPowerBonus) te).getEnchantPowerBonus() : 0;}
    @Override public final boolean recolorBlock(World world, BlockPos pos, EnumFacing side, EnumDyeColor color) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTERecolorBlock && ((IMTERecolorBlock) te).recolorBlock(side, color);}

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
    }

    @Override
    public void observedNeighborChange(IBlockState observerState, World world, BlockPos observerPos, Block changedBlock, BlockPos changedBlockPos) {
        super.observedNeighborChange(observerState, world, observerPos, changedBlock, changedBlockPos);
    }

    @Override public final boolean shouldCheckWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEShouldCheckWeakPower ? ((IMTEShouldCheckWeakPower) te).shouldCheckWeakPower(side) : isNormalCube(state, world, pos);}
    @Override public final boolean getWeakChanges(IBlockAccess world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEGetWeakChanges && ((IMTEGetWeakChanges) te).getWeakChanges();}
    @Override public final String getHarvestTool(IBlockState state) {return harvestTool;}
    @Override public final int getHarvestLevel(IBlockState state) {return harvestLevel;}
    @Override public final boolean isToolEffective(String type, IBlockState state) {return type.equals(getHarvestTool(state));}

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return super.getExtendedState(state, world, pos);
    }

    @Override public final boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {return true;}
    @Override public final float[] getBeaconColorMultiplier(IBlockState state, World world, BlockPos pos, BlockPos beaconPos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEGetBeaconColorMultiplier ? ((IMTEGetBeaconColorMultiplier) te).getBeaconColorMultiplier(beaconPos) : null;}
    @Override public final boolean supportsVisualConnections() {return true;}
    @Override public final IBlockState getFacade(IBlockAccess world, BlockPos pos, EnumFacing side) {TileEntity te = world.getTileEntity(pos); if (te instanceof IMTEGetFacade) return ((IMTEGetFacade) te).getFacade(side); return null;}
    @Override public final IBlockState getVisualState(IBlockAccess world, BlockPos pos, EnumFacing side) {return getFacade(world, pos, side);}
    @Override public final IBlockState getFacade(IBlockAccess world, BlockPos pos, EnumFacing side, BlockPos connection) {return getFacade(world, pos, side);}
    @Override public final TileEntity createNewTileEntity(World world, int i) {return null;}
    @Override protected final Pair<TextureAtlasSprite, Integer> getParticleTexture(World world, BlockPos blockPos) {TileEntity te = world.getTileEntity(blockPos); return te instanceof IMTEParticle ? ((IMTEParticle) te).getParticleTexture() : Pair.of(TextureUtils.getMissingSprite(), 0xFFFFFF);}
}
