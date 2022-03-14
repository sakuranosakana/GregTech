package gregtech.api.multitileentity;

import com.google.common.base.Predicate;
import gregtech.api.multitileentity.IMultiTileEntity.*;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.IPlantable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@SuppressWarnings("ALL")
public class MultiTileEntityBlock extends Block {

    // todo register these in the event
    private static final Map<String, MultiTileEntityBlock> MULTI_TILE_ENTITY_BLOCK_MAP = new HashMap<>();

    private final int harvestLevel;
    private final String nameInternal, harvestTool;
    private final boolean opaque, normalCube;

    // todo
    public static String getName() {return "";}

    public static MultiTileEntityBlock getOrCreate(String modID, String name, Material material, SoundType ) {}

    private MultiTileEntityBlock(String modID, String name, Material material, SoundType stepOnSound, String harvestTool, int harvestLevel, boolean opaque, boolean normalCube) {
        super(material);
        // todo check stage?

        nameInternal = "";
        MULTI_TILE_ENTITY_BLOCK_MAP.put(modID + ":" + nameInternal, this);

        //setStepSound(stepOnSound);
        this.opaque = opaque;
        this.normalCube = normalCube;

        this.harvestTool = harvestTool.toLowerCase();
        this.harvestLevel = harvestLevel;
        this.lightOpacity = opaque ? 255 : 0;
        // ST.hide(this);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te == null) return; // shouldRefresh?
        if (te instanceof IMTEBreakBlock && ((IMTEBreakBlock) te).breakBlock()) return;
        worldIn.removeTileEntity(pos);
    }

    @Override
    public boolean isTopSolid(IBlockState state) {
        return super.isTopSolid(state);
    }

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
    public boolean getUseNeighborBrightness(IBlockState state) {
        return super.getUseNeighborBrightness(state);
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

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return super.withRotation(state, rot);
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return super.withMirror(state, mirrorIn);
    }

    @Override public boolean isBlockNormalCube(IBlockState state) {return normalCube;}
    @Override public boolean isNormalCube(IBlockState state) {return normalCube;}

    @Override
    public boolean causesSuffocation(IBlockState state) {
        return super.causesSuffocation(state);
    }

    @Override public boolean isFullCube(IBlockState state) {return !opaque;}

    @Override
    public boolean hasCustomBreakingProgress(IBlockState state) {
        return super.hasCustomBreakingProgress(state);
    }

    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return super.isPassable(worldIn, pos);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return super.getRenderType(state);
    }

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
        return super.isReplaceable(worldIn, pos);
    }

    @Override
    public Block setHardness(float hardness) {
        return super.setHardness(hardness);
    }

    @Override
    public Block setBlockUnbreakable() {
        return super.setBlockUnbreakable();
    }

    @Override public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {TileEntity te = worldIn.getTileEntity(pos); return te instanceof IMTEGetBlockHardness ? ((IMTEGetBlockHardness) te).getBlockHardness() : 1.0F;}

    @Override
    public Block setTickRandomly(boolean shouldTick) {
        return super.setTickRandomly(shouldTick);
    }

    @Override
    public boolean getTickRandomly() {
        return super.getTickRandomly();
    }

    @Override public boolean hasTileEntity() {return true;}

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return super.getBoundingBox(state, source, pos);
    }

    @Override
    public int getPackedLightmapCoords(IBlockState state, IBlockAccess source, BlockPos pos) {
        return super.getPackedLightmapCoords(state, source, pos);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return super.getBlockFaceShape(worldIn, state, pos, face);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean isActualState) {
        super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return super.getCollisionBoundingBox(blockState, worldIn, pos);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        return super.getSelectedBoundingBox(state, worldIn, pos);
    }

    @Override public boolean isOpaqueCube(IBlockState state) {return opaque;}

    @Override
    public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
        return super.canCollideCheck(state, hitIfLiquid);
    }

    @Override
    public boolean isCollidable() {
        return super.isCollidable();
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
        super.randomTick(worldIn, pos, state, random);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        super.randomDisplayTick(stateIn, worldIn, pos, rand);
    }

    @Override
    public void onPlayerDestroy(World worldIn, BlockPos pos, IBlockState state) {
        super.onPlayerDestroy(worldIn, pos, state);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }

    @Override
    public int tickRate(World worldIn) {
        return super.tickRate(worldIn);
    }

    @Override public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {TileEntity te = worldIn.getTileEntity(pos); if (te instanceof IMTEOnBlockAdded) ((IMTEOnBlockAdded) te).onBlockAdded();}

    @Override
    public int quantityDropped(Random random) {
        return super.quantityDropped(random);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return super.getItemDropped(state, rand, fortune);
    }

    @Override public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos) {TileEntity te = worldIn.getTileEntity(pos); return te instanceof IMTEGetPlayerRelativeBlockHardness ? ((IMTEGetPlayerRelativeBlockHardness) te).getPlayerRelativeBlockHardness(player, super.getPlayerRelativeBlockHardness(state, player, worldIn, pos)) : super.getPlayerRelativeBlockHardness(state, player, worldIn, pos);}

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
    }

    @Override public void dropXpOnBlockBreak(World worldIn, BlockPos pos, int amount) {TileEntity te = worldIn.getTileEntity(pos); if (te instanceof IMTEDropXpOnBlockBreak) ((IMTEDropXpOnBlockBreak) te).dropXpOnBlockBreak(amount); else super.dropXpOnBlockBreak(worldIn, pos, amount);}
    @Override public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {TileEntity te = worldIn.getTileEntity(pos); return te instanceof IMTECollisionRayTrace ? ((IMTECollisionRayTrace) te).collisionRayTrace(start, end) : super.collisionRayTrace(blockState, worldIn, pos, start, end);}

    @Override
    protected RayTraceResult rayTrace(BlockPos pos, Vec3d start, Vec3d end, AxisAlignedBB boundingBox) {
        return super.rayTrace(pos, start, end, boundingBox);
    }

    @Override
    public void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn) {
        super.onExplosionDestroy(worldIn, pos, explosionIn);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return super.getRenderLayer();
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return super.canPlaceBlockOnSide(worldIn, pos, side);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos);
    }

    @Override public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {TileEntity te = worldIn.getTileEntity(pos); return te instanceof IMTEOnBlockActivated && ((IMTEOnBlockActivated) te).onBlockActivated(playerIn, facing, hitX, hitY, hitZ);}

    @Override public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {TileEntity te = worldIn.getTileEntity(pos); if (te instanceof IMTEOnEntityWalk) ((IMTEOnEntityWalk) te).onEntityWalk(entityIn);}

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
    }

    @Override public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {TileEntity te = worldIn.getTileEntity(pos); if (te instanceof IMTEOnBlockClicked) ((IMTEOnBlockClicked) te).onBlockClicked(playerIn);}

    @Override
    public Vec3d modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3d motion) {
        return super.modifyAcceleration(worldIn, pos, entityIn, motion);
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return super.getWeakPower(blockState, blockAccess, pos, side);
    }

    @Override public boolean canProvidePower(IBlockState state) {return !normalCube;}
    @Override public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {TileEntity te = worldIn.getTileEntity(pos); if (te instanceof IMTEOnEntityCollision) ((IMTEOnEntityCollision) te).onEntityCollision(entityIn);}

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return super.getStrongPower(blockState, blockAccess, pos, side);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }

    @Override protected boolean canSilkHarvest() {return false;}

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return super.getSilkTouchDrop(state);
    }

    @Override
    public int quantityDroppedWithBonus(int fortune, Random random) {
        return super.quantityDroppedWithBonus(fortune, random);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public boolean canSpawnInBlock() {
        return super.canSpawnInBlock();
    }

    @Override
    public String getLocalizedName() {
        return super.getLocalizedName();
    }

    @Override public String getTranslationKey() {return nameInternal;}
    @Override public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {TileEntity te = worldIn.getTileEntity(pos); return te == null || te.receiveClientEvent(id, param);}

    @Override
    public EnumPushReaction getPushReaction(IBlockState state) {
        return super.getPushReaction(state);
    }

    @Override
    public float getAmbientOcclusionLightValue(IBlockState state) {
        return super.getAmbientOcclusionLightValue(state);
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
    }

    @Override
    public void onLanded(World worldIn, Entity entityIn) {
        super.onLanded(worldIn, entityIn);
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return super.getItem(worldIn, pos, state);
    }

    @Override public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {/**/}

    @Override
    public CreativeTabs getCreativeTab() {
        return super.getCreativeTab();
    }

    @Override
    public Block setCreativeTab(CreativeTabs tab) {
        return super.setCreativeTab(tab);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public void fillWithRain(World worldIn, BlockPos pos) {
        super.fillWithRain(worldIn, pos);
    }

    @Override
    public boolean requiresUpdates() {
        return super.requiresUpdates();
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosionIn) {
        return super.canDropFromExplosion(explosionIn);
    }

    @Override
    public boolean isAssociatedBlock(Block other) {
        return super.isAssociatedBlock(other);
    }

    @Override public boolean hasComparatorInputOverride(IBlockState state) {return true;}

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
    public EnumOffsetType getOffsetType() {
        return super.getOffsetType();
    }

    @Override
    public Vec3d getOffset(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return super.getOffset(state, worldIn, pos);
    }

    @Override
    public SoundType getSoundType() {
        return super.getSoundType();
    }

    //@Override
    //public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    //    super.addInformation(stack, worldIn, tooltip, flagIn);
    //}

    @Override public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEGetLightValue ? Math.max(0, Math.min(((IMTEGetLightValue) te).getLightValue(), 15)) : super.getLightValue(state, world, pos);}
    @Override public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEIsLadder && ((IMTEIsLadder) te).isLadder(entity);}
    @Override public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEIsNormalCube ? ((IMTEIsNormalCube) te).isNormalCube() : normalCube;}

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return super.doesSideBlockRendering(state, world, pos, face);
    }

    @Override public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEIsSideSolid ? ((IMTEIsSideSolid) te).isSideSolid(side) : opaque;}
    @Override public boolean isBurning(IBlockAccess world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEIsBurning && ((IMTEIsBurning) te).isBurning();}
    @Override public boolean isAir(IBlockState state, IBlockAccess world, BlockPos pos) {if (world == null) return false; TileEntity te = world.getTileEntity(pos); return te instanceof IMTEIsAir && ((IMTEIsAir) te).isAir();}

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return super.getFlammability(world, pos, face);
    }

    @Override
    public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return super.isFlammable(world, pos, face);
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return super.getFireSpreadSpeed(world, pos, face);
    }

    @Override
    public boolean isFireSource(World world, BlockPos pos, EnumFacing side) {
        return super.isFireSource(world, pos, side);
    }

    @Override public boolean hasTileEntity(IBlockState state) {return true;}
    @Override public TileEntity createTileEntity(World world, IBlockState state) {return null;}

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        return super.quantityDropped(state, fortune, random);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        super.getDrops(drops, world, pos, state, fortune);
    }

    @Override public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {return false;}
    @Override public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTECanCreatureSpawn && ((IMTECanCreatureSpawn) te).canCreatureSpawn(type);}
    @Override public void beginLeavesDecay(IBlockState state, World world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); if (te instanceof IMTEBeginLeavesDecay) ((IMTEBeginLeavesDecay) te).beginLeavesDecay();}
    @Override public boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTECanSustainLeaves && ((IMTECanSustainLeaves) te).canSustainLeaves();}
    @Override public boolean isLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEIsLeaves && ((IMTEIsLeaves) te).isLeaves();}
    @Override public boolean canBeReplacedByLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTECanBeReplacedByLeaves && ((IMTECanBeReplacedByLeaves) te).canBeReplacedByLeaves();}
    @Override public boolean isWood(IBlockAccess world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEIsWood && ((IMTEIsWood) te).isWood();}

    @Override
    public boolean isReplaceableOreGen(IBlockState state, IBlockAccess world, BlockPos pos, Predicate<IBlockState> target) {
        //if (GAPI.mStartedServerStarted < 1) return false; TODO
        TileEntity te = world.getTileEntity(pos);
        return te instanceof IMTEIsReplaceableOreGen
                ? ((IMTEIsReplaceableOreGen) te).isReplaceableOreGen(target)
                : super.isReplaceableOreGen(state, world, pos, target);
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        return super.getExplosionResistance(world, pos, exploder, explosion);
    }

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
        super.onBlockExploded(world, pos, explosion);
    }

    @Override public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTECanConnectRedstone ? ((IMTECanConnectRedstone) te).canConnectRedstone(side) : super.canConnectRedstone(state, world, pos, side);}
    @Override public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTECanPlaceTorchOnTop ? ((IMTECanPlaceTorchOnTop) te).canPlaceTorchOnTop() : isSideSolid(state, world, pos, EnumFacing.UP);}

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return super.getPickBlock(state, target, world, pos, player);
    }

    @Override public boolean isFoliage(IBlockAccess world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEIsFoliage && ((IMTEIsFoliage) te).isFoliage();}

    @Override
    public boolean addLandingEffects(IBlockState state, WorldServer worldObj, BlockPos blockPosition, IBlockState iblockstate, EntityLivingBase entity, int numberOfParticles) {
        return super.addLandingEffects(state, worldObj, blockPosition, iblockstate, entity, numberOfParticles);
    }

    @Override
    public boolean addRunningEffects(IBlockState state, World world, BlockPos pos, Entity entity) {
        return super.addRunningEffects(state, world, pos, entity);
    }

    @Override
    public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, ParticleManager manager) {
        return super.addHitEffects(state, worldObj, target, manager);
    }

    @Override
    public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager) {
        return super.addDestroyEffects(world, pos, manager);
    }

    @Override public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTECanSustainPlant ? ((IMTECanSustainPlant) te).canSustainPlant(direction, plantable) : super.canSustainPlant(state, world, pos, direction, plantable);}
    @Override public void onPlantGrow(IBlockState state, World world, BlockPos pos, BlockPos source) {TileEntity te = world.getTileEntity(pos); if (te instanceof IMTEOnPlantGrow) ((IMTEOnPlantGrow) te).onPlantGrow(source);}
    @Override public boolean isFertile(World world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEIsFertile && ((IMTEIsFertile) te).isFertile();}
    @Override public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEGetLightOpacity ? ((IMTEGetLightOpacity) te).getLightOpacity(state, world, pos) : opaque ? 255 : 0;}

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        return super.canEntityDestroy(state, world, pos, entity);
    }

    @Override public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {TileEntity te = worldObj.getTileEntity(pos); return te instanceof IMTEIsBeaconBase && ((IMTEIsBeaconBase) te).isBeaconBase(pos);}
    @Override public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTERotateBlock && ((IMTERotateBlock) te).rotateBlock(axis);}
    @Override public EnumFacing[] getValidRotations(World world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEGetValidRotations ? ((IMTEGetValidRotations) te).getValidRotations() : null;}
    @Override public float getEnchantPowerBonus(World world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEGetEnchantPowerBonus ? ((IMTEGetEnchantPowerBonus) te).getEnchantPowerBonus() : 0;}
    @Override public boolean recolorBlock(World world, BlockPos pos, EnumFacing side, EnumDyeColor color) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTERecolorBlock && ((IMTERecolorBlock) te).recolorBlock(side, color);}

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
    }

    @Override
    public void observedNeighborChange(IBlockState observerState, World world, BlockPos observerPos, Block changedBlock, BlockPos changedBlockPos) {
        super.observedNeighborChange(observerState, world, observerPos, changedBlock, changedBlockPos);
    }

    @Override public boolean shouldCheckWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEShouldCheckWeakPower ? ((IMTEShouldCheckWeakPower) te).shouldCheckWeakPower(side) : isNormalCube(state, world, pos);}
    @Override public boolean getWeakChanges(IBlockAccess world, BlockPos pos) {TileEntity te = world.getTileEntity(pos); return te instanceof IMTEGetWeakChanges && ((IMTEGetWeakChanges) te).getWeakChanges();}
    @Override public String getHarvestTool(IBlockState state) {return harvestTool;}
    @Override public int getHarvestLevel(IBlockState state) {return harvestLevel;}
    @Override public boolean isToolEffective(String type, IBlockState state) {return type.equals(getHarvestTool(state));}

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return super.getExtendedState(state, world, pos);
    }

    @Override
    public Boolean isEntityInsideMaterial(IBlockAccess world, BlockPos blockpos, IBlockState iblockstate, Entity entity, double yToTest, Material materialIn, boolean testingHead) {
        return super.isEntityInsideMaterial(world, blockpos, iblockstate, entity, yToTest, materialIn, testingHead);
    }

    @Override
    public Boolean isAABBInsideMaterial(World world, BlockPos pos, AxisAlignedBB boundingBox, Material materialIn) {
        return super.isAABBInsideMaterial(world, pos, boundingBox, materialIn);
    }

    @Override
    public Boolean isAABBInsideLiquid(World world, BlockPos pos, AxisAlignedBB boundingBox) {
        return super.isAABBInsideLiquid(world, pos, boundingBox);
    }

    @Override
    public float getBlockLiquidHeight(World world, BlockPos pos, IBlockState state, Material material) {
        return super.getBlockLiquidHeight(world, pos, state, material);
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return super.canRenderInLayer(state, layer);
    }

    @Override
    protected NonNullList<ItemStack> captureDrops(boolean start) {
        return super.captureDrops(start);
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, Entity entity) {
        return super.getSoundType(state, world, pos, entity);
    }

    @Override
    public float[] getBeaconColorMultiplier(IBlockState state, World world, BlockPos pos, BlockPos beaconPos) {
        return super.getBeaconColorMultiplier(state, world, pos, beaconPos);
    }

    @Override
    public IBlockState getStateAtViewpoint(IBlockState state, IBlockAccess world, BlockPos pos, Vec3d viewpoint) {
        return super.getStateAtViewpoint(state, world, pos, viewpoint);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
    }

    @Override
    public boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        return super.canBeConnectedTo(world, pos, facing);
    }

    @Override
    public PathNodeType getAiPathNodeType(IBlockState state, IBlockAccess world, BlockPos pos) {
        return super.getAiPathNodeType(state, world, pos);
    }

    @Override
    public PathNodeType getAiPathNodeType(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving entity) {
        return super.getAiPathNodeType(state, world, pos, entity);
    }

    @Override
    public boolean doesSideBlockChestOpening(IBlockState blockState, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return super.doesSideBlockChestOpening(blockState, world, pos, side);
    }
}
