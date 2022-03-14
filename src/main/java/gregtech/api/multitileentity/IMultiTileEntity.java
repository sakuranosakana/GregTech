package gregtech.api.multitileentity;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

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

    interface IMTEGetLightOpacity                extends IMultiTileEntity {int getLightOpacity();}
    interface IMTEGetLightValue                  extends IMultiTileEntity {int getLightValue();}
    interface IMTEIsPassable                     extends IMultiTileEntity {boolean isPassable();}
    interface IMTEIsReplaceable                  extends IMultiTileEntity {boolean isReplaceable();}
    interface IMTEGetBlockHardness               extends IMultiTileEntity {float getBlockHardness();}
    interface IMTEGetLightmapPackedCoords        extends IMultiTileEntity {int getPackedLightmapCoords(IBlockState state, IBlockAccess source, BlockPos pos);}
    interface IMTEShouldSideBeRendered           extends IMultiTileEntity {boolean shouldSideBeRendered(EnumFacing side);}
    interface IMTEGetBlockFaceShape              extends IMultiTileEntity {BlockFaceShape getBlockFaceShape(EnumFacing face);}
    interface IMTEAddCollisionBoxToList          extends IMultiTileEntity {void addCollisionBoxToList(AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity);}
    interface IMTEGetCollisionBoundingBox        extends IMultiTileEntity {AxisAlignedBB getCollisionBoundingBox();}
    interface IMTERandomTick                     extends IMultiTileEntity {void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random);}
    interface IMTEUpdateTick                     extends IMultiTileEntity {void updateTick(Random rand);}
    interface IMTEOnPlayerDestroy                extends IMultiTileEntity {void onPlayerDestroy();}
    interface IMTEOnNeighborChanged              extends IMultiTileEntity {void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos);}
    interface IMTEOnBlockAdded                   extends IMultiTileEntity {void onBlockAdded();}
    interface IMTEBreakBlock                     extends IMultiTileEntity {boolean breakBlock();}
    interface IMTEGetPlayerRelativeBlockHardness extends IMultiTileEntity {float getPlayerRelativeBlockHardness(EntityPlayer player, float original);}
    interface IMTEDropXpOnBlockBreak             extends IMultiTileEntity {void dropXpOnBlockBreak(int amount);}
    interface IMTEGetExplosionResistance         extends IMultiTileEntity {float getExplosionResistance(Entity exploder, Explosion explosion);}
    interface IMTECollisionRayTrace              extends IMultiTileEntity {RayTraceResult collisionRayTrace(Vec3d start, Vec3d end);}
    interface IMTEOnBlockActivated               extends IMultiTileEntity {boolean onBlockActivated(EntityPlayer playerIn, EnumFacing facing, float hitX, float hitY, float hitZ);}
    interface IMTEOnEntityWalk                   extends IMultiTileEntity {void onEntityWalk(Entity entityIn);}
    interface IMTEOnBlockClicked                 extends IMultiTileEntity {void onBlockClicked(EntityPlayer playerIn);}
    interface IMTEModifyAcceleration             extends IMultiTileEntity {Vec3d modifyAcceleration(Entity entityIn, Vec3d motion);}
    interface IMTEGetWeakPower                   extends IMultiTileEntity {int getWeakPower(EnumFacing side);}
    interface IMTEOnEntityCollision              extends IMultiTileEntity {void onEntityCollision(Entity entityIn);}
    interface IMTEGetStrongPower                 extends IMultiTileEntity {int getStrongPower(EnumFacing side);}
    interface IMTEOnBlockPlacedBy                extends IMultiTileEntity {void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack);}
    interface IMTEOnFallenUpon                   extends IMultiTileEntity {void onFallenUpon(Entity entityIn, float fallDistance);}
    interface IMTEOnBlockHarvested               extends IMultiTileEntity {void onBlockHarvested(IBlockState state, EntityPlayer player);}
    interface IMTEFillWithRain                   extends IMultiTileEntity {void fillWithRain();}
    interface IMTEGetComparatorInputOverride     extends IMultiTileEntity {int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos);}
    interface IMTEGetOffset                      extends IMultiTileEntity {Vec3d getOffset(IBlockState state, IBlockAccess worldIn, BlockPos pos);}
    interface IMTEIsLadder                       extends IMultiTileEntity {boolean isLadder(EntityLivingBase entity);}
    interface IMTEIsNormalCube                   extends IMultiTileEntity {boolean isNormalCube();}
    interface IMTEDoesSideBlockRendering         extends IMultiTileEntity {boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face);}
    interface IMTEIsSideSolid                    extends IMultiTileEntity {boolean isSideSolid(EnumFacing side);}
    interface IMTEIsBurning                      extends IMultiTileEntity {boolean isBurning();}
    interface IMTEIsAir                          extends IMultiTileEntity {boolean isAir();}
    interface IMTERemovedByPlayer                extends IMultiTileEntity {boolean removedByPlayer(World world, EntityPlayer player, boolean willHarvest);}
    interface IMTEGetFlammability                extends IMultiTileEntity {int getFlammability(EnumFacing face, boolean original);}
    interface IMTEGetFireSpreadSpeed             extends IMultiTileEntity {int getFireSpreadSpeed(EnumFacing face, boolean original);}
    interface IMTEIsFireSource                   extends IMultiTileEntity {boolean isFireSource(EnumFacing side);}
    interface IMTEGetDrops                       extends IMultiTileEntity {void getDrops(NonNullList<ItemStack> drops, int fortune, boolean silkTouch);}
    interface IMTECanCreatureSpawn               extends IMultiTileEntity {boolean canCreatureSpawn(EntityLiving.SpawnPlacementType type);}
    interface IMTEBeginLeavesDecay               extends IMultiTileEntity {void beginLeavesDecay();}
    interface IMTECanSustainLeaves               extends IMultiTileEntity {boolean canSustainLeaves();}
    interface IMTEIsLeaves                       extends IMultiTileEntity {boolean isLeaves();}
    interface IMTECanBeReplacedByLeaves          extends IMultiTileEntity {boolean canBeReplacedByLeaves();}
    interface IMTEIsWood                         extends IMultiTileEntity {boolean isWood();}
    interface IMTEIsReplaceableOreGen            extends IMultiTileEntity {boolean isReplaceableOreGen(Predicate<IBlockState> target);}
    interface IMTEOnBlockExploded                extends IMultiTileEntity {void onBlockExploded(Explosion explosion);}
    interface IMTECanConnectRedstone             extends IMultiTileEntity {boolean canConnectRedstone(EnumFacing side);}
    interface IMTECanPlaceTorchOnTop             extends IMultiTileEntity {boolean canPlaceTorchOnTop();}
    interface IMTEGetPickBlock                   extends IMultiTileEntity {ItemStack getPickBlock(RayTraceResult target);}
    interface IMTEIsFoliage                      extends IMultiTileEntity {boolean isFoliage();}
    interface IMTECanSustainPlant                extends IMultiTileEntity {boolean canSustainPlant(EnumFacing direction, IPlantable plantable);}
    interface IMTEOnPlantGrow                    extends IMultiTileEntity {void onPlantGrow(BlockPos source);}
    interface IMTEIsFertile                      extends IMultiTileEntity {boolean isFertile();}
    interface IMTECanEntityDestroy               extends IMultiTileEntity {boolean canEntityDestroy(Entity entity);}
    interface IMTEIsBeaconBase                   extends IMultiTileEntity {boolean isBeaconBase(BlockPos pos);}
    interface IMTERotateBlock                    extends IMultiTileEntity {boolean rotateBlock(EnumFacing facing);}
    interface IMTEGetValidRotations              extends IMultiTileEntity {EnumFacing[] getValidRotations();}
    interface IMTEGetEnchantPowerBonus           extends IMultiTileEntity {float getEnchantPowerBonus();}
    interface IMTERecolorBlock                   extends IMultiTileEntity {boolean recolorBlock(EnumFacing side, EnumDyeColor color);}
    interface IMTEOnNeighborChange               extends IMultiTileEntity {void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor);}
    interface IMTEObservedNeighborChange         extends IMultiTileEntity {void observedNeighborChange(IBlockState observerState, World world, BlockPos observerPos, Block changedBlock, BlockPos changedBlockPos);}
    interface IMTEShouldCheckWeakPower           extends IMultiTileEntity {boolean shouldCheckWeakPower(EnumFacing side);}
    interface IMTEGetWeakChanges                 extends IMultiTileEntity {boolean getWeakChanges();}
    interface IMTEGetExtendedState               extends IMultiTileEntity {IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos);}
    interface IMTEGetBeaconColorMultiplier       extends IMultiTileEntity {float[] getBeaconColorMultiplier(BlockPos beaconPos);}
    interface IMTEGetSelectedBoundingBox         extends IMultiTileEntity {@SideOnly(Side.CLIENT) AxisAlignedBB getSelectedBoundingBox();}
    interface IMTERandomDisplayTick              extends IMultiTileEntity {@SideOnly(Side.CLIENT) void randomDisplayTick(Random rand);}
    interface IMTEPaintable                      extends IMultiTileEntity {boolean onPaint(EnumFacing facing, int rgb);}
    interface IMTEGetFacade                      extends IMultiTileEntity {IBlockState getFacade(EnumFacing side);}
    interface IMTEParticle                       extends IMultiTileEntity {Pair<TextureAtlasSprite, Integer> getParticleTexture();}

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
