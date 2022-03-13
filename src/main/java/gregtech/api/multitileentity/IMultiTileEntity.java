package gregtech.api.multitileentity;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
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
import net.minecraft.nbt.NBTTagCompound;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

/**
 * Interfaces to override {@link net.minecraft.block.Block} functionality
 */
public interface IMultiTileEntity {

    /**
     * This ID MUST be saved inside the NBT of the TileEntity itself. It gets set by the Registry when the TileEntity is placed.
     */
    ResourceLocation getMultiTileEntityId();

    /**
     * This ID MUST be saved inside the NBT of the TileEntity itself. It gets set by the Registry when the TileEntity is placed.
     */
    short getItemStackMeta();

    /**
     * Called by the Registry with the default NBT Parameters and the two IDs to save when the TileEntity is created.
     */
    void initFromNBT(@Nullable NBTTagCompound nbtTagCompound, ResourceLocation multiTileEntityID, short itemStackMeta);

    /**
     * Writes eventual Item Data to the NBT.
     */
    NBTTagCompound writeItemNBT(NBTTagCompound nbtTagCompound);

    /**
     * Sets the Item Display Name. Use null to reset it.
     */
    void setCustomName(@Nullable String name);

    /**
     * @return the Item Display Name.
     */
    String getCustomName();

    void setShouldRefresh(boolean shouldRefresh);


    /*
     * Hooks into the Block Class. Implement them in order to overwrite the Default Behaviours.
     */

    interface IMTEIsTopSolid                     extends IMultiTileEntity {boolean isTopSolid(IBlockState state);}
    interface IMTEIsFullBlock                    extends IMultiTileEntity {boolean isFullBlock(IBlockState state);}
    interface IMTECanEntitySpawn                 extends IMultiTileEntity {boolean canEntitySpawn(IBlockState state, Entity entityIn);}
    interface IMTEGetLightOpacity                extends IMultiTileEntity {int getLightOpacity(IBlockState state); default int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {return getLightOpacity(state);}}
    interface IMTEIsTranslucent                  extends IMultiTileEntity {boolean isTranslucent(IBlockState state);}
    interface IMTEGetLightValue                  extends IMultiTileEntity {int getLightValue(IBlockState state); int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos);}
    interface IMTECausesSuffocation              extends IMultiTileEntity {boolean causesSuffocation(IBlockState state);}
    interface IMTEHasCustomBreakingProgress      extends IMultiTileEntity {boolean hasCustomBreakingProgress(IBlockState state);}
    interface IMTEIsPassable                     extends IMultiTileEntity {boolean isPassable(IBlockAccess worldIn, BlockPos pos);}
    interface IMTEGetRenderType                  extends IMultiTileEntity {EnumBlockRenderType getRenderType(IBlockState state);}
    interface IMTEIsReplaceable                  extends IMultiTileEntity {boolean isReplaceable(IBlockAccess worldIn, BlockPos pos);}
    interface IMTESetHardness                    extends IMultiTileEntity {Block setHardness(float hardness);}
    interface IMTEGetBlockHardness               extends IMultiTileEntity {float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos);}
    interface IMTEGetBoundingBox                 extends IMultiTileEntity {AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos);}
    interface IMTEGetLightmapPackedCoords        extends IMultiTileEntity {int getPackedLightmapCoords(IBlockState state, IBlockAccess source, BlockPos pos);}
    interface IMTEShouldSideBeRendered           extends IMultiTileEntity {boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side);}
    interface IMTEGetBlockFaceShape              extends IMultiTileEntity {BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face);}
    interface IMTEGetCollisionBoundingBox        extends IMultiTileEntity {AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos);}
    interface IMTECanCollideCheck                extends IMultiTileEntity {boolean canCollideCheck(IBlockState state, boolean hitIfLiquid);}
    interface IMTEIsCollidable                   extends IMultiTileEntity {boolean isCollidable();}
    interface IMTERandomTick                     extends IMultiTileEntity {void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random);}
    interface IMTEUpdateTick                     extends IMultiTileEntity {void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand);}
    interface IMTEOnPlayerDestroy                extends IMultiTileEntity {void onPlayerDestroy(World worldIn, BlockPos pos, IBlockState state);}
    interface IMTEOnNeighborChanged              extends IMultiTileEntity {void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos);}
    interface IMTETickRate                       extends IMultiTileEntity {int tickRate(World worldIn);}
    interface IMTEOnBlockAdded                   extends IMultiTileEntity {void onBlockAdded(World worldIn, BlockPos pos, IBlockState state);}
    interface IMTEBreakBlock                     extends IMultiTileEntity {boolean breakBlock();}
    interface IMTEQuantityDropped                extends IMultiTileEntity {int quantityDropped(Random random); int quantityDropped(IBlockState state, int fortune, Random random);}
    interface IMTEGetItemDropped                 extends IMultiTileEntity {Item getItemDropped(IBlockState state, Random rand, int fortune);}
    interface IMTEGetPlayerRelativeBlockHardness extends IMultiTileEntity {float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos);}
    interface IMTEDropBlockAsItemWithChance      extends IMultiTileEntity {void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune);}
    interface IMTEDropXpOnBlockBreak             extends IMultiTileEntity {void dropXpOnBlockBreak(World worldIn, BlockPos pos, int amount);}
    interface IMTEDamageDropped                  extends IMultiTileEntity {int damageDropped(IBlockState state);}
    interface IMTEGetExplosionResistance         extends IMultiTileEntity {float getExplosionResistance(Entity exploder); default float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {return getExplosionResistance(exploder);}}
    interface IMTECollisionRayTrace              extends IMultiTileEntity {RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end);}
    interface IMTERayTrace                       extends IMultiTileEntity {RayTraceResult rayTrace(BlockPos pos, Vec3d start, Vec3d end, AxisAlignedBB boundingBox);}
    interface IMTEOnExplosionDestroy             extends IMultiTileEntity {void onExplosionDestroy(World worldIn, BlockPos pos, Explosion explosionIn);}
    interface IMTECanPlaceBlockOnSide            extends IMultiTileEntity {boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side);}
    interface IMTECanPlaceBlockAt                extends IMultiTileEntity {boolean canPlaceBlockAt(World worldIn, BlockPos pos);}
    interface IMTEOnBlockActivated               extends IMultiTileEntity {boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ);}
    interface IMTEOnEntityWalk                   extends IMultiTileEntity {void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn);}
    interface IMTEGetStateForPlacement           extends IMultiTileEntity {IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer); default IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) { return getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer);}}
    interface IMTEOnBlockClicked                 extends IMultiTileEntity {void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn);}
    interface IMTEModifyAcceleration             extends IMultiTileEntity {Vec3d modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3d motion);}
    interface IMTEGetWeakPower                   extends IMultiTileEntity {int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side);}
    interface IMTECanProvidePower                extends IMultiTileEntity {boolean canProvidePower(IBlockState state);}
    interface IMTEOnEntityCollision              extends IMultiTileEntity {void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn);}
    interface IMTEGetStrongPower                 extends IMultiTileEntity {int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side);}
    interface IMTEHarvestBlock                   extends IMultiTileEntity {void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack);}
    interface IMTEQuantityDroppedWithBonus       extends IMultiTileEntity {int quantityDroppedWithBonus(int fortune, Random random);}
    interface IMTEOnBlockPlacedBy                extends IMultiTileEntity {void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack);}
    interface IMTECanSpawnInBlock                extends IMultiTileEntity {boolean canSpawnInBlock();}
    interface IMTEEventReceived                  extends IMultiTileEntity {boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param);}
    interface IMTEGetPushReaction                extends IMultiTileEntity {EnumPushReaction getPushReaction(IBlockState state);}
    interface IMTEOnBlockFallenUpon              extends IMultiTileEntity {void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance);}
    interface IMTEOnLanded                       extends IMultiTileEntity {void onLanded(World worldIn, Entity entityIn);}
    interface IMTEGetItem                        extends IMultiTileEntity {ItemStack getItem(World worldIn, BlockPos pos, IBlockState state);}
    interface IMTEGetSubBlocks                   extends IMultiTileEntity {void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items);}
    interface IMTEOnBlockHarvested               extends IMultiTileEntity {void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player);}
    interface IMTEFillWithRain                   extends IMultiTileEntity {void fillWithRain(World worldIn, BlockPos pos);}
    interface IMTERequiresUpdates                extends IMultiTileEntity {boolean requiresUpdates();}
    interface IMTECanDropFromExplosion           extends IMultiTileEntity {boolean canDropFromExplosion(Explosion explosionIn);}
    interface IMTEIsAssociatedBlock              extends IMultiTileEntity {boolean isAssociatedBlock(Block other);}
    interface IMTEGetComparatorInputOverride     extends IMultiTileEntity {int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos);}
    interface IMTECreateBlockState               extends IMultiTileEntity {BlockStateContainer createBlockState();}
    interface IMTEGetBlockState                  extends IMultiTileEntity {BlockStateContainer getBlockState();}
    interface IMTEGetOffsetType                  extends IMultiTileEntity {Block.EnumOffsetType getOffsetType();}
    interface IMTEGetOffset                      extends IMultiTileEntity {Vec3d getOffset(IBlockState state, IBlockAccess worldIn, BlockPos pos);}
    interface IMTEGetSoundType                   extends IMultiTileEntity {SoundType getSoundType(); default SoundType getSoundType(IBlockState state, World world, BlockPos pos, Entity entity) {return getSoundType();}}
    interface IMTEGetSlipperiness                extends IMultiTileEntity {float getSlipperiness(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity);}
    interface IMTESetDefaultSlipperiness         extends IMultiTileEntity {void setDefaultSlipperiness(float slipperiness);}
    interface IMTEIsLadder                       extends IMultiTileEntity {boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity);}
    interface IMTEDoesSideBlockRendering         extends IMultiTileEntity {boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face);}
    interface IMTEIsSideSolid                    extends IMultiTileEntity {boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side);}
    interface IMTEIsBurning                      extends IMultiTileEntity {boolean isBurning(IBlockAccess world, BlockPos pos);}
    interface IMTEIsAir                          extends IMultiTileEntity {boolean isAir(IBlockState state, IBlockAccess world, BlockPos pos);}
    interface IMTECanHarvestBlock                extends IMultiTileEntity {boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player);}
    interface IMTERemovedByPlayer                extends IMultiTileEntity {boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest);}
    interface IMTEGetFlammability                extends IMultiTileEntity {int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face);}
    interface IMTEIsFlammable                    extends IMultiTileEntity {boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face);}
    interface IMTEGetFireSpreadSpeed             extends IMultiTileEntity {int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face);}
    interface IMTEIsFireSource                   extends IMultiTileEntity {boolean isFireSource(World world, BlockPos pos, EnumFacing side);}
    interface IMTEGetDrops                       extends IMultiTileEntity {List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune); void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune);}
    interface IMTECanCreatureSpawn               extends IMultiTileEntity {boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type);}
    interface IMTEBeginLeavesDecay               extends IMultiTileEntity {void beginLeavesDecay(IBlockState state, World world, BlockPos pos);}
    interface IMTECanSustainLeaves               extends IMultiTileEntity {boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos);}
    interface IMTEIsLeaves                       extends IMultiTileEntity {boolean isLeaves(IBlockState state, IBlockAccess world, BlockPos pos);}
    interface IMTECanBeReplacedByLeaves          extends IMultiTileEntity {boolean canBeReplacedByLeaves(IBlockState state, IBlockAccess world, BlockPos pos);}
    interface IMTEIsWood                         extends IMultiTileEntity {boolean isWood(IBlockAccess world, BlockPos pos);}
    interface IMTEIsReplaceableByOreGen          extends IMultiTileEntity {boolean isReplaceableOreGen(IBlockState state, IBlockAccess world, BlockPos pos, Predicate<IBlockState> target);}
    interface IMTEOnBlockExploded                extends IMultiTileEntity {void onBlockExploded(World world, BlockPos pos, Explosion explosion);}
    interface IMTECanConnectRedstone             extends IMultiTileEntity {boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side);}
    interface IMTECanPlaceTorchOnTop             extends IMultiTileEntity {boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos);}
    interface IMTEGetPickBlock                   extends IMultiTileEntity {ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player);}
    interface IMTEIsFoliage                      extends IMultiTileEntity {boolean isFoliage(IBlockAccess world, BlockPos pos);}
    interface IMTEAddLandingEffects              extends IMultiTileEntity {boolean addLandingEffects(IBlockState state, WorldServer worldObj, BlockPos blockPosition, IBlockState iblockstate, EntityLivingBase entity, int numberOfParticles);}
    interface IMTEAddRunningEffects              extends IMultiTileEntity {boolean addRunningEffects(IBlockState state, World world, BlockPos pos, Entity entity);}
    interface IMTECanSustainPlant                extends IMultiTileEntity {boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable);}
    interface IMTEOnPlantGrow                    extends IMultiTileEntity {void onPlantGrow(IBlockState state, World world, BlockPos pos, BlockPos source);}
    interface IMTEIsFertile                      extends IMultiTileEntity {boolean isFertile(World world, BlockPos pos);}
    interface IMTECanEntityDestroy               extends IMultiTileEntity {boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity);}
    interface IMTEIsBeaconBase                   extends IMultiTileEntity {boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon);}
    interface IMTERotateBlock                    extends IMultiTileEntity {boolean rotateBlock(World world, BlockPos pos, EnumFacing axis);}
    interface IMTEGetValidRotations              extends IMultiTileEntity {EnumFacing[] getValidRotations(World world, BlockPos pos);}
    interface IMTEGetEnchantPowerBonus           extends IMultiTileEntity {float getEnchantPowerBonus(World world, BlockPos pos);}
    interface IMTERecolorBlock                   extends IMultiTileEntity {boolean recolorBlock(World world, BlockPos pos, EnumFacing side, EnumDyeColor color);}
    interface IMTEGetExpDrop                     extends IMultiTileEntity {int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune);}
    interface IMTEOnNeighborChange               extends IMultiTileEntity {void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor);}
    interface IMTEObservedNeighborChange         extends IMultiTileEntity {void observedNeighborChange(IBlockState observerState, World world, BlockPos observerPos, Block changedBlock, BlockPos changedBlockPos);}
    interface IMTEShouldCheckWeakPower           extends IMultiTileEntity {boolean shouldCheckWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side);}
    interface IMTEGetWeakChanges                 extends IMultiTileEntity {boolean getWeakChanges(IBlockAccess world, BlockPos pos);}
    interface IMTEGetHarvestTool                 extends IMultiTileEntity {String getHarvestTool(IBlockState state);}
    interface IMTEGetHarvestLevel                extends IMultiTileEntity {int getHarvestLevel(IBlockState state);}
    interface IMTEIsToolEffective                extends IMultiTileEntity {boolean isToolEffective(String type, IBlockState state);}
    interface IMTEGetExtendedState               extends IMultiTileEntity {IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos);}
    interface IMTEIsEntityInsideMaterial         extends IMultiTileEntity {Boolean isEntityInsideMaterial(IBlockAccess world, BlockPos blockpos, IBlockState iblockstate, Entity entity, double yToTest, Material materialIn, boolean testingHead);}
    interface IMTEIsAABBInsideMaterial           extends IMultiTileEntity {Boolean isAABBInsideMaterial(World world, BlockPos pos, AxisAlignedBB boundingBox, Material materialIn);}
    interface IMTEIsAABBInsideLiquid             extends IMultiTileEntity {Boolean isAABBInsideLiquid(World world, BlockPos pos, AxisAlignedBB boundingBox);}
    interface IMTECanRenderInLayer               extends IMultiTileEntity {boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer);}
    interface IMTECaptureDrops                   extends IMultiTileEntity {NonNullList<ItemStack> captureDrops(boolean start);}
    interface IMTEGetBeaconColorMultiplier       extends IMultiTileEntity {float[] getBeaconColorMultiplier(IBlockState state, World world, BlockPos pos, BlockPos beaconPos);}
    interface IMTEGetStateAtViewpoint            extends IMultiTileEntity {IBlockState getStateAtViewpoint(IBlockState state, IBlockAccess world, BlockPos pos, Vec3d viewpoint);}
    interface IMTECanBeConnectedTo               extends IMultiTileEntity {boolean canBeConnectedTo(IBlockAccess world, BlockPos pos, EnumFacing facing);}
    interface IMTEGetAIPathNodeType              extends IMultiTileEntity {PathNodeType getAiPathNodeType(IBlockState state, IBlockAccess world, BlockPos pos); default PathNodeType getAiPathNodeType(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving entity) {return getAiPathNodeType(state, world, pos);}}
    interface IMTEDoesSideBlockChestOpening      extends IMultiTileEntity {boolean doesSideBlockChestOpening(IBlockState blockState, IBlockAccess world, BlockPos pos, EnumFacing side);}
    interface IMTEIsStickyBlock                  extends IMultiTileEntity {boolean isStickyBlock(IBlockState state);}
    interface IMTEGetSelectedBoundingBox         extends IMultiTileEntity {@SideOnly(Side.CLIENT) AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos);}
    interface IMTERandomDisplayTick              extends IMultiTileEntity {@SideOnly(Side.CLIENT) void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand);}
    interface IMTEGetRenderLayer                 extends IMultiTileEntity {@SideOnly(Side.CLIENT) BlockRenderLayer getRenderLayer();}
    interface IMTEAddHitEffects                  extends IMultiTileEntity {@SideOnly(Side.CLIENT) boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, ParticleManager manager);}
    interface IMTEAddDestroyEffects              extends IMultiTileEntity {@SideOnly(Side.CLIENT) boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager);}
    interface IMTEPaintable                      extends IMultiTileEntity {boolean onPaint(EnumFacing facing, int rgb);}

    interface IMTEAddTooltips extends IMultiTileEntity {
        /** Adds ToolTips to the Item */
        void addTooltips(List<String> list, ItemStack stack, boolean advanced);
    }

    interface IMTEGetDebugInfo extends IMultiTileEntity {
        List<String> getDebugInfo(int scanLevel);
    }

    interface IMTEOnRegistrationFirst extends IMultiTileEntity {
        /** Called when a TileEntity of this particular Class is being registered first at any MultiTileEntity Registry. So basically one call per Class. */
        void onRegistrationFirst(MultiTileEntityRegistry registry, short ID);
    }
}
