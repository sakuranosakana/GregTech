package gregtech.api.block.machines;

import codechicken.lib.block.property.unlisted.UnlistedIntegerProperty;
import codechicken.lib.block.property.unlisted.UnlistedStringProperty;
import codechicken.lib.raytracer.CuboidRayTraceResult;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.raytracer.RayTracer;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.vec.Cuboid6;
import gregtech.api.GregTechAPI;
import gregtech.api.block.BlockCustomParticle;
import gregtech.api.capability.GregtechCapabilities;
import gregtech.api.capability.tool.IScrewdriverItem;
import gregtech.api.capability.tool.IWrenchItem;
import gregtech.api.cover.CoverBehavior;
import gregtech.api.cover.ICoverable;
import gregtech.api.cover.IFacadeCover;
import gregtech.api.items.toolitem.IToolStats;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.interfaces.IMetaTileEntity;
import gregtech.api.metatileentity.interfaces.IMetaTileEntity.*;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.interfaces.ITurnable;
import gregtech.api.pipenet.IBlockAppearance;
import gregtech.client.renderer.handler.MetaTileEntityRenderer;
import gregtech.common.ConfigHolder;
import gregtech.common.tools.DamageValues;
import gregtech.integration.ctm.IFacadeWrapper;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockMachine extends BlockCustomParticle implements ITileEntityProvider, IFacadeWrapper, IBlockAppearance {

    private static final List<IndexedCuboid6> EMPTY_COLLISION_BOX = Collections.emptyList();
    private static final IUnlistedProperty<String> HARVEST_TOOL = new UnlistedStringProperty("harvest_tool");
    private static final IUnlistedProperty<Integer> HARVEST_LEVEL = new UnlistedIntegerProperty("harvest_level");
    //used for rendering purposes of non-opaque machines like chests and tanks
    public static final PropertyBool OPAQUE = PropertyBool.create("opaque");

    protected final ThreadLocal<IMetaTileEntity> LAST_BROKEN_MTE = new ThreadLocal<>();

    public BlockMachine() {
        super(Material.IRON);
        setCreativeTab(GregTechAPI.TAB_GREGTECH);
        setSoundType(SoundType.METAL);
        setHardness(6.0f);
        setResistance(6.0f);
        setTranslationKey("unnamed");
        setDefaultState(getDefaultState().withProperty(OPAQUE, true));
    }

    @Nullable
    public static IMetaTileEntity getMetaTileEntity(IBlockAccess blockAccess, BlockPos pos) {
        TileEntity tileEntity = blockAccess.getTileEntity(pos);
        return tileEntity instanceof IGregTechTileEntity ? ((IGregTechTileEntity) tileEntity).getMetaTileEntity() : null;
    }

    @Override
    public boolean canHarvestBlock(@Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player) {
        if (ConfigHolder.machines.requireWrenchForMachines) {
            return player.getHeldItemMainhand().hasCapability(GregtechCapabilities.CAPABILITY_WRENCH, null);
        }
        return super.canHarvestBlock(world, pos, player);
    }

    @Nullable
    @Override
    public String getHarvestTool(@Nonnull IBlockState state) {
        String value = ((IExtendedBlockState) state).getValue(HARVEST_TOOL);
        return value == null ? "wrench" : value; //safety check for mods who don't handle state properly
    }

    @Override
    public int getHarvestLevel(@Nonnull IBlockState state) {
        Integer value = ((IExtendedBlockState) state).getValue(HARVEST_LEVEL);
        return value == null ? 0 : value; //safety check for mods who don't handle state properly
    }

    @Override
    public boolean causesSuffocation(IBlockState state) {
        return state.getValue(OPAQUE);
    }

    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        MetaTileEntity metaTileEntity = (MetaTileEntity) getMetaTileEntity(worldIn, pos);
        if (metaTileEntity == null)
            return state;

        return ((IExtendedBlockState) state)
                .withProperty(HARVEST_TOOL, metaTileEntity.getHarvestTool() == null ? "wrench" : metaTileEntity.getHarvestTool())
                .withProperty(HARVEST_LEVEL, metaTileEntity.getHarvestLevel());
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[]{OPAQUE}, new IUnlistedProperty[]{HARVEST_TOOL, HARVEST_LEVEL});
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(OPAQUE, meta % 2 == 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(OPAQUE) ? 0 : 1;
    }

    /** Method passed through to {@link IMTECanCreatureSpawn}. Default false */
    @Override public final boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, SpawnPlacementType type) {IMetaTileEntity mte = getMetaTileEntity(world, pos); return mte instanceof IMTECanCreatureSpawn && ((IMTECanCreatureSpawn) mte).canCreatureSpawn(type);}

    /** Method passed through to {@link IMTEHardnessResistance}. Default 6.0F */
    @Override public final float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {IMetaTileEntity mte = getMetaTileEntity(worldIn, pos); return mte instanceof IMTEHardnessResistance ? ((IMTEHardnessResistance) mte).getBlockHardness() : 6.0F;}
    /** Method passed through to {@link IMTEHardnessResistance}. Default 6.0F */
    @Override public final float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {IMetaTileEntity mte = getMetaTileEntity(world, pos); return mte instanceof IMTEHardnessResistance ? ((IMTEHardnessResistance) mte).getExplosionResistance() : 6.0F;}

    private List<IndexedCuboid6> getCollisionBox(IBlockAccess blockAccess, BlockPos pos) {
        MetaTileEntity metaTileEntity = (MetaTileEntity) getMetaTileEntity(blockAccess, pos);
        if (metaTileEntity == null)
            return EMPTY_COLLISION_BOX;
        ArrayList<IndexedCuboid6> collisionList = new ArrayList<>();
        metaTileEntity.addCollisionBoundingBox(collisionList);
        metaTileEntity.addCoverCollisionBoundingBox(collisionList);
        return collisionList;
    }

    @Override
    public boolean doesSideBlockRendering(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EnumFacing face) {
        return state.isOpaqueCube() && getMetaTileEntity(world, pos) != null;
    }

    @Nonnull
    @Override
    public ItemStack getPickBlock(@Nonnull IBlockState state, @Nonnull RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player) {
        MetaTileEntity metaTileEntity = (MetaTileEntity) getMetaTileEntity(world, pos);
        if (metaTileEntity == null)
            return ItemStack.EMPTY;
        if (target instanceof CuboidRayTraceResult) {
            return metaTileEntity.getPickItem((CuboidRayTraceResult) target, player);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void addCollisionBoxToList(@Nonnull IBlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull AxisAlignedBB entityBox, @Nonnull List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        for (Cuboid6 axisAlignedBB : getCollisionBox(worldIn, pos)) {
            AxisAlignedBB offsetBox = axisAlignedBB.aabb().offset(pos);
            if (offsetBox.intersects(entityBox)) collidingBoxes.add(offsetBox);
        }
    }

    @Nullable
    @Override
    public RayTraceResult collisionRayTrace(@Nonnull IBlockState blockState, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Vec3d start, @Nonnull Vec3d end) {
        return RayTracer.rayTraceCuboidsClosest(start, end, pos, getCollisionBox(worldIn, pos));
    }

    /** Method passed through to {@link ITurnable}. Default false */
    @Override public final boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {IMetaTileEntity mte = getMetaTileEntity(world, pos); return mte instanceof ITurnable && ((ITurnable) mte).rotateBlock(axis);}
    /** Method passed through to {@link ITurnable}. Default null */
    @Override public final EnumFacing[] getValidRotations(@Nonnull World world, @Nonnull BlockPos pos) {IMetaTileEntity mte = getMetaTileEntity(world, pos); return mte instanceof ITurnable ? ((ITurnable) mte).getValidRotations() : null;}

    /** Method passed through to {@link IMTERecolorBlock}. Default false */
    @Override public final boolean recolorBlock(World world, BlockPos pos, EnumFacing side, EnumDyeColor color) {IMetaTileEntity mte = getMetaTileEntity(world, pos); return mte instanceof IMTERecolorBlock && ((IMTERecolorBlock) mte).recolorBlock(color);}

    @Override
    public void onBlockPlacedBy(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityLivingBase placer, ItemStack stack) {
        IGregTechTileEntity holder = (IGregTechTileEntity) worldIn.getTileEntity(pos);
        IMetaTileEntity sampleMetaTileEntity = GregTechAPI.MTE_REGISTRY.getObjectById(stack.getItemDamage());
        if (holder != null && sampleMetaTileEntity != null) {
            //if (stack.hasDisplayName()) {
            //    holder.setCustomName(stack.getDisplayName());
            //}
            IMetaTileEntity metaTileEntity = holder.setMetaTileEntity(sampleMetaTileEntity);
            if (stack.hasTagCompound() && metaTileEntity instanceof IMTEItemStackData) {
                ((IMTEItemStackData) metaTileEntity).initFromItemStackData(stack.getTagCompound());
            }
            if (metaTileEntity instanceof ITurnable) {
                ITurnable turnable = (ITurnable) metaTileEntity;
                if (turnable.isValidFrontFacing(EnumFacing.UP)) {
                    turnable.setFrontFacing(EnumFacing.getDirectionFromEntityLiving(pos, placer));
                } else {
                    turnable.setFrontFacing(placer.getHorizontalFacing().getOpposite());
                }
            }
        }
    }

    @Override
    public void breakBlock(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        MetaTileEntity metaTileEntity = (MetaTileEntity) getMetaTileEntity(worldIn, pos);
        if (metaTileEntity != null) {
            if (!metaTileEntity.keepsInventory()) {
                NonNullList<ItemStack> inventoryContents = NonNullList.create();
                metaTileEntity.clearMachineInventory(inventoryContents);
                for (ItemStack itemStack : inventoryContents) {
                    Block.spawnAsEntity(worldIn, pos, itemStack);
                }
            }
            metaTileEntity.dropAllCovers();
            metaTileEntity.onRemoval();

            LAST_BROKEN_MTE.set(metaTileEntity);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void getDrops(@Nonnull NonNullList<ItemStack> drops, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull IBlockState state, int fortune) {
        MetaTileEntity metaTileEntity = LAST_BROKEN_MTE.get() == null ? (MetaTileEntity) getMetaTileEntity(world, pos) : (MetaTileEntity) LAST_BROKEN_MTE.get();
        if (metaTileEntity == null) return;
        ItemStack itemStack = metaTileEntity.getStackForm();
        if (metaTileEntity instanceof IMTEItemStackData) {
            NBTTagCompound tag = new NBTTagCompound();
            ((IMTEItemStackData) metaTileEntity).writeItemStackData(tag);
            itemStack.setTagCompound(tag);
        }
        //if (metaTileEntity.getTileEntity().hasCustomName()) {
        //    itemStack.setStackDisplayName(metaTileEntity.getTileEntity().getName());
        //}
        drops.add(itemStack);
        if (metaTileEntity instanceof IMTEGetDrops) ((IMTEGetDrops) metaTileEntity).getDrops(drops, harvesters.get());
    }

    @Override
    public boolean onBlockActivated(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer playerIn, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        MetaTileEntity metaTileEntity = (MetaTileEntity) getMetaTileEntity(worldIn, pos);
        CuboidRayTraceResult rayTraceResult = (CuboidRayTraceResult) RayTracer.retraceBlock(worldIn, playerIn, pos);
        ItemStack itemStack = playerIn.getHeldItem(hand);
        if (metaTileEntity == null || rayTraceResult == null) {
            return false;
        }

        if (itemStack.hasCapability(GregtechCapabilities.CAPABILITY_SCREWDRIVER, null)) {
            IScrewdriverItem screwdriver = itemStack.getCapability(GregtechCapabilities.CAPABILITY_SCREWDRIVER, null);

            if (screwdriver.damageItem(DamageValues.DAMAGE_FOR_SCREWDRIVER, true) &&
                    metaTileEntity.onCoverScrewdriverClick(playerIn, hand, rayTraceResult)) {
                screwdriver.damageItem(DamageValues.DAMAGE_FOR_SCREWDRIVER, false);
                IToolStats.onOtherUse(itemStack, worldIn, pos);
                return true;
            }
            return false;
        }

        if (itemStack.hasCapability(GregtechCapabilities.CAPABILITY_WRENCH, null)) {
            IWrenchItem wrenchItem = itemStack.getCapability(GregtechCapabilities.CAPABILITY_WRENCH, null);
            EnumFacing wrenchDirection = ICoverable.determineGridSideHit(rayTraceResult);

            if (wrenchItem.damageItem(DamageValues.DAMAGE_FOR_WRENCH, true) &&
                    metaTileEntity.onWrenchClick(playerIn, hand, wrenchDirection, rayTraceResult)) {

                wrenchItem.damageItem(DamageValues.DAMAGE_FOR_WRENCH, false);
                IToolStats.onOtherUse(itemStack, worldIn, pos);
                return true;
            }
            return false;
        }

        return metaTileEntity.onCoverRightClick(playerIn, hand, rayTraceResult);
    }

    /** Method passed through to {@link IMetaTileEntity}. Default no action */
    @Override public final void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {IMetaTileEntity mte = getMetaTileEntity(worldIn, pos); if (mte != null) mte.onLeftClick(playerIn, (CuboidRayTraceResult) RayTracer.retraceBlock(worldIn, playerIn, pos));}

    @Override
    public boolean canConnectRedstone(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nullable EnumFacing side) {
        MetaTileEntity metaTileEntity = (MetaTileEntity) getMetaTileEntity(world, pos);
        return metaTileEntity != null && metaTileEntity.canConnectRedstone(side == null ? null : side.getOpposite());
    }

    @Override
    public boolean shouldCheckWeakPower(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
        // The check in World::getRedstonePower in the vanilla code base is reversed. Setting this to false will
        // actually cause getWeakPower to be called, rather than prevent it.
        return false;
    }

    @Override
    public int getWeakPower(@Nonnull IBlockState blockState, @Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
        MetaTileEntity metaTileEntity = (MetaTileEntity) getMetaTileEntity(blockAccess, pos);
        return metaTileEntity == null ? 0 : metaTileEntity.getOutputRedstoneSignal(side == null ? null : side.getOpposite());
    }

    @Override
    public void neighborChanged(@Nonnull IBlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Block blockIn, @Nonnull BlockPos fromPos) {
        MetaTileEntity mte = (MetaTileEntity) getMetaTileEntity(worldIn, pos);
        if (mte != null) {
            mte.updateInputRedstoneSignals();
            if (mte instanceof IMTENeighborChanged) ((IMTENeighborChanged) mte).neighborChanged();
        }
    }

    /** Method passed through to {@link IMTEGetComparatorInputOverride}. Default 0 */
    @Override public final int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {IMetaTileEntity mte = getMetaTileEntity(worldIn, pos); return mte instanceof IMTEGetComparatorInputOverride ? ((IMTEGetComparatorInputOverride) mte).getComparatorInputOverride() : 0;}
    // No way to check if it has an override without access to world and pos, so must return true always
    @Override public final boolean hasComparatorInputOverride(IBlockState state) {return true;}

    @Override
    public void harvestBlock(@Nonnull World worldIn, @Nonnull EntityPlayer player, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nullable TileEntity te, @Nonnull ItemStack stack) {
        LAST_BROKEN_MTE.set(te == null ? LAST_BROKEN_MTE.get() : ((IGregTechTileEntity) te).getMetaTileEntity());
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        LAST_BROKEN_MTE.set(null);
    }

    @Override @SideOnly(Side.CLIENT) public final EnumBlockRenderType getRenderType(IBlockState state) {return MetaTileEntityRenderer.BLOCK_RENDER_TYPE;}
    @Override public final boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {return true;}

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return state.getValue(OPAQUE);
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return state.getValue(OPAQUE);
    }

    /** Method passed through to {@link IMTEGetBlockFaceShape}. Default SOLID if opaque, otherwise UNDEFINED */
    @Override public final BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {IMetaTileEntity mte = getMetaTileEntity(worldIn, pos); return mte instanceof IMTEGetBlockFaceShape ? ((IMTEGetBlockFaceShape) mte).getBlockFaceShape(face) : mte != null && mte.isOpaqueCube() ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;}

    /** Method passed through to {@link IMTEGetLightValue}. Default 0 */
    @Override public final int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {IMetaTileEntity mte = getMetaTileEntity(world, pos); return mte instanceof IMTEGetLightValue ? ((IMTEGetLightValue) mte).getLightValue() : 0;}

    /** Method passed through to {@link IMTEGetLightOpacity}. Default 255 */
    @Override public final int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {IMetaTileEntity mte = getMetaTileEntity(world, pos); return mte instanceof IMTEGetLightOpacity ? ((IMTEGetLightOpacity) mte).getLightOpacity() : 255;}

    /** Method passed through to {@link IMTECanEntityDestroy}. Default super value */
    @Override public final boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {IMetaTileEntity mte = getMetaTileEntity(world, pos); return mte instanceof IMTECanEntityDestroy ? ((IMTECanEntityDestroy) mte).canEntityDestroy(entity) : super.canEntityDestroy(state, world, pos, entity);}

    /** Method passed through to {@link IMTEGetSubBlocks}. Default adds {@link IMetaTileEntity#getStackForm()} to the List */
    @Override
    public final void getSubBlocks(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        for (IMetaTileEntity mte : GregTechAPI.MTE_REGISTRY) {
            if (mte instanceof IMTEGetSubBlocks) {
                ((IMTEGetSubBlocks) mte).getSubBlocks(tab, items);
            } else items.add(mte.getStackForm());
        }
    }

    //////////////////////////////
    // ITileEntityProvider impl //
    //////////////////////////////

    @Override
    public final TileEntity createNewTileEntity(@Nullable World worldIn, int meta) {
        return new MetaTileEntityHolder();
    }

    ///////////////////////////////////////////
    // IBlockAppearance, IFacadeWrapper impl //
    ///////////////////////////////////////////

    @Override
    public IBlockState getFacade(IBlockAccess world, BlockPos pos, EnumFacing side) {
        MetaTileEntity metaTileEntity = (MetaTileEntity) getMetaTileEntity(world, pos);
        if (metaTileEntity != null && side != null) {
            CoverBehavior coverBehavior = metaTileEntity.getCoverAtSide(side);
            if (coverBehavior instanceof IFacadeCover) {
                return ((IFacadeCover) coverBehavior).getVisualState();
            }
        }
        return world.getBlockState(pos);
    }

    @Override public final IBlockState getFacade(IBlockAccess world, BlockPos pos, EnumFacing side, BlockPos otherPos) {return getFacade(world, pos, side);}
    @Override public final IBlockState getVisualState(IBlockAccess world, BlockPos pos, EnumFacing side) {return getFacade(world, pos, side);}
    @Override public final boolean supportsVisualConnections() {return true;}

    //////////////////////////////////
    // BlockCustomParticle override //
    //////////////////////////////////

    /** Method passed through to {@link IMetaTileEntity}. Default missing sprite */
    @Override @SideOnly(Side.CLIENT) protected final Pair<TextureAtlasSprite, Integer> getParticleTexture(World world, BlockPos blockPos) {IMetaTileEntity mte = getMetaTileEntity(world, blockPos); return mte == null ? Pair.of(TextureUtils.getMissingSprite(), 0xFFFFFF) : mte.getParticleTexture();}
}
