package gregtech.api.tileentity.base;

import codechicken.lib.raytracer.CuboidRayTraceResult;
import codechicken.lib.raytracer.IndexedCuboid6;
import gregtech.api.GregTechAPI;
import gregtech.api.block.machines.BlockMachine;
import gregtech.api.cover.ICoverable;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.net.IPacket;
import gregtech.api.sound.GTSoundManager;
import gregtech.api.tileentity.ITileEntityQuickObstructionCheck;
import gregtech.api.util.GTLog;
import gregtech.common.ConfigHolder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static gregtech.api.capability.GregtechDataCodes.*;
import static gregtech.api.multitileentity.IMultiTileEntity.*;

public abstract class TileEntityBaseMultiTileEntity extends TileEntityTicksAndSync implements IMTEPaintable, IMTEOnNeighborChange, IMTEGetPickBlock, IMTEOnRegistrationFirst, IMTERecolorBlock, IMTEGetDrops, IMTEOnBlockActivated, IMTEShouldSideBeRendered, IMTEGetFlammability, IMTEGetFireSpreadSpeed, IWorldNameable {

    public static final String TAG_KEY_PAINTING_COLOR = "PaintingColor";
    public static final String TAG_KEY_FRAGILE = "Fragile";
    public static final String TAG_KEY_MUFFLED = "Muffled";

    private static class UpdateEntry {
        private final int discriminator;
        private final byte[] updateData;

        public UpdateEntry(int discriminator, byte[] updateData) {
            this.discriminator = discriminator;
            this.updateData = updateData;
        }
    }

    protected final List<UpdateEntry> updateEntries = new ArrayList<>();

    private ResourceLocation multiTileEntityId;
    private short itemStackMeta;

    private String customName = null;

    private int paintingColor = -1;
    private int cachedLightValue;
    private boolean needToUpdateLighting = false;

    protected boolean isFragile = false;
    protected boolean isMuffled = false;

    /**
     * return the internal Name of this TileEntity to be registered.
     */
    public abstract String getTileEntityName();

    @Nonnull
    @Override
    public String getName() {
        return this.customName == null ? "" : this.customName;
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && !this.customName.isEmpty();
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(getMetaFullName());
    }

    public final String getMetaName() {
        return String.format("%s.machine.%s", multiTileEntityId.getNamespace(), multiTileEntityId.getPath());
    }

    @Nonnull
    public final String getMetaFullName() {
        return getMetaName() + ".name";
    }

    @Override
    public void onRegistrationFirst(MultiTileEntityRegistry registry, short ID) {
        GameRegistry.registerTileEntity(getClass(), getTileEntityName());
    }

    @Override
    public IPacket getClientDataPacket(boolean shouldSendAll) {
        return shouldSendAll ? new PacketSyncdiscriminators(getPos(), getItemStackMeta(), getMultiTileEntityID()) : null;
    }

    public IPacket getClientDataPacketByte(boolean shouldSendAll, byte aByte) {
        return shouldSendAll ? new PacketSyncDataByteAndIDs(getPos(), getItemStackMeta(), getMultiTileEntityID(), aByte) : new PacketSyncDataByte(getPos(), aByte);
    }

    public IPacket getClientDataPacketShort(boolean shouldSendAll, short aShort) {
        return shouldSendAll ? new PacketSyncDataShortAndIDs(getPos(), getItemStackMeta(), getMultiTileEntityID(), aShort) : new PacketSyncDataShort(getPos(), aShort);
    }

    public IPacket getClientDataPacketInteger(boolean shouldSendAll, int aInteger) {
        return shouldSendAll ? new PacketSyncDataIntegerAndIDs(getPos(), getItemStackMeta(), getMultiTileEntityID(), aInteger) : new PacketSyncDataInteger(getPos(), aInteger);
    }

    public IPacket getClientDataPacketLong(boolean shouldSendAll, long aLong) {
        return shouldSendAll ? new PacketSyncDataLongAndIDs(getPos(), getItemStackMeta(), getMultiTileEntityID(), aLong) : new PacketSyncDataLong(getPos(), aLong);
    }

    public IPacket getClientDataPacketByteArray(boolean shouldSendAll, byte... aByteArray) {
        return shouldSendAll ? new PacketSyncDataByteArrayAndIDs(getPos(), getItemStackMeta(), getMultiTileEntityID(), aByteArray) : new PacketSyncDataByteArray(getPos(), aByteArray);
    }

    @Override
    public final void initFromNBT(NBTTagCompound compound, ResourceLocation multiTileEntityId, short itemStackMeta) {
        // Set ID and Registry ID.
        this.multiTileEntityId = multiTileEntityId;
        this.itemStackMeta = itemStackMeta;
        // Read the Default Parameters from NBT.
        readFromNBT(compound == null ? new NBTTagCompound() : compound);
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound data) {
        super.readFromNBT(data);
        customName = data.getString("CustomName");
        if (data.hasKey("MetaId", Constants.NBT.TAG_STRING)) {
            String metaTileEntityIdRaw = data.getString("MetaId");
            ResourceLocation metaTileEntityId = new ResourceLocation(metaTileEntityIdRaw);
            MetaTileEntity sampleMetaTileEntity = GregTechAPI.MTE_REGISTRY.getObject(metaTileEntityId);
            if (sampleMetaTileEntity != null) {
                if (data.hasKey(TAG_KEY_PAINTING_COLOR)) {
                    this.paintingColor = data.getInteger(TAG_KEY_PAINTING_COLOR);
                }
                this.cachedLightValue = data.getInteger("CachedLightValue");

                this.isFragile = data.getBoolean(TAG_KEY_FRAGILE);
                this.isMuffled = data.getBoolean(TAG_KEY_MUFFLED);
            } else {
                GTLog.logger.error("Failed to load MultiTileEntity with invalid ID " + metaTileEntityIdRaw);
            }
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound data) {
        super.writeToNBT(data);
        data.setString("CustomName", getName());
        data.setString("MetaId", multiTileEntityId.toString());

        if (isPainted()) {
            data.setInteger(TAG_KEY_PAINTING_COLOR, paintingColor);
        }
        data.setInteger("CachedLightValue", cachedLightValue);
        data.setBoolean(TAG_KEY_FRAGILE, isFragile);
        data.setBoolean(TAG_KEY_MUFFLED, isMuffled);
        return data;
    }

    public void sendInitialSyncData() {
        writeCustomData(INITIALIZE_MTE, buffer -> {
            buffer.writeVarInt(GregTechAPI.MTE_REGISTRY.getIdByObjectName(multiTileEntityId));
            writeInitialSyncData(buffer);
        });
    }

    public void writeInitialSyncData(PacketBuffer buf) {
        buf.writeString(getName());
        buf.writeBoolean(true);
        buf.writeVarInt(GregTechAPI.MTE_REGISTRY.getIdByObjectName(multiTileEntityId));
        buf.writeInt(this.paintingColor);
        buf.writeBoolean(isFragile);
        buf.writeBoolean(isMuffled);
    }

    public void receiveInitialSyncData(PacketBuffer buf) {
        setCustomName(buf.readString(Short.MAX_VALUE));
        if (buf.readBoolean()) {
            int metaTileEntityId = buf.readVarInt();
            if (hasWorld() && !getWorld().isRemote) {
                updateBlockOpacity();
                sendInitialSyncData();
                //just to update neighbours so cables and other things will work properly
                this.needToUpdateLighting = true;
                world.neighborChanged(getPos(), getBlockType(), getPos());
                markDirty();
            }
            this.paintingColor = buf.readInt();
            this.isFragile = buf.readBoolean();
            this.isMuffled = buf.readBoolean();
            scheduleChunkForRenderUpdate();
            this.needToUpdateLighting = true;
        }
    }

    public void writeCustomData(int discriminator, Consumer<PacketBuffer> dataWriter) {
        ByteBuf backedBuffer = Unpooled.buffer();
        dataWriter.accept(new PacketBuffer(backedBuffer));
        byte[] updateData = Arrays.copyOfRange(backedBuffer.array(), 0, backedBuffer.writerIndex());
        updateEntries.add(new UpdateEntry(discriminator, updateData));
        @SuppressWarnings("deprecation")
        IBlockState blockState = getBlockType().getStateFromMeta(getBlockMetadata());
        world.notifyBlockUpdate(getPos(), blockState, blockState, 0);
    }

    public void receiveCustomData(int discriminator, PacketBuffer buffer) {
        if (discriminator == INITIALIZE_MTE) {
            int metaTileEntityId = buffer.readVarInt();
            receiveInitialSyncData(buffer);
            scheduleChunkForRenderUpdate();
            this.needToUpdateLighting = true;
        } else if (discriminator == UPDATE_PAINTING_COLOR) {
            this.paintingColor = buffer.readInt();
            scheduleChunkForRenderUpdate();
        } else if (discriminator == UPDATE_IS_FRAGILE) {
            this.isFragile = buffer.readBoolean();
            scheduleChunkForRenderUpdate();
        } else if (discriminator == UPDATE_SOUND_MUFFLED) {
            this.isMuffled = buffer.readBoolean();
            if (isMuffled) {
                GTSoundManager.stopTileSound(getPos());
            }
        }
    }

    @Override
    public void sendClientData(boolean shouldSendAll, EntityPlayerMP playerMP) {
        super.sendClientData(shouldSendAll, playerMP);
        if (shouldSendAll && hasCustomName()) {
            if (playerMP == null) {
                getNetworkHandler().sendToAllPlayersInRange(new PacketSyncDataName(getPos(), customName), world, getPos());
            } else {
                getNetworkHandler().sendToPlayer(new PacketSyncDataName(getPos(), customName), playerMP);
            }
        }
    }

    protected void scheduleChunkForRenderUpdate() {
        BlockPos pos = getPos();
        getWorld().markBlockRangeForRenderUpdate(
                pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1,
                pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
    }

    private void updateBlockOpacity() {
        IBlockState currentState = world.getBlockState(getPos());
        boolean isMetaTileEntityOpaque = isOpaqueCube();
        if (currentState.getValue(BlockMachine.OPAQUE) != isMetaTileEntityOpaque) {
            world.setBlockState(getPos(), currentState.withProperty(BlockMachine.OPAQUE, isMetaTileEntityOpaque));
        }
    }

    @Override
    public void update() {
        super.update();

        if (this.needToUpdateLighting) {
            getWorld().checkLight(getPos());
            this.needToUpdateLighting = false;
        }
    }

    @Override
    public boolean onBlockActivated(EntityPlayer playerIn, EnumFacing facing, float hitX, float hitY, float hitZ) {
        try {
            return allowRightClick(playerIn) && checkObstruction(playerIn, facing, hitX, hitY, hitZ);
        } catch (Throwable e) {
            e.printStackTrace();
            return true;
        }
    }

    public boolean checkObstruction(EntityPlayer player, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return !(player == null || player instanceof FakePlayer || !isBlockObstructed(world, getPos(), facing));
    }

    public static boolean isBlockObstructed(@Nonnull World world, @Nonnull BlockPos pos, EnumFacing facing) {
        pos = new BlockPos(pos).offset(facing);
        TileEntity tTileEntity = world.getTileEntity(pos);
        if (tTileEntity != null) {
            if (tTileEntity instanceof ITileEntityQuickObstructionCheck)
                return ((ITileEntityQuickObstructionCheck) tTileEntity).isObstructingBlockAt(facing.getOpposite());
        }
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof BlockTrapDoor || block instanceof BlockDoor) return false;
        AxisAlignedBB boundingBox = state.getCollisionBoundingBox(world, pos);
        if (boundingBox == null) return false;

        return boundingBox.contains(new Vec3d(pos.getX(), pos.getY(), pos.getZ()));
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        MultiTileEntityRegistry registry = MultiTileEntityRegistry.getRegistry(itemStackMeta);
        if (registry != null) drops.add(registry.getItem(itemStackMeta, writeItemNBT(new NBTTagCompound())));
    }

    public void popOff() {
        if (isDead()) return;
        for (ItemStack stack : getDrops(getWorld(), getPos(), getState(getPos()), 0)) {
            Block.spawnAsEntity(getWorld(), getPos(), stack);
        }
        setToAir();
    }

    public void popOff(Entity entity) {
        if (isDead()) return;
        for (ItemStack stack : getDrops(getWorld(), getPos(), getState(getPos()), 0))
            Block.spawnAsEntity(entity.world, entity.getPosition(), stack);
        setToAir();
    }

    public void popOff(World world, double x, double y, double z) {
        if (isDead()) return;
        BlockPos pos = new BlockPos(x, y, z);
        for (ItemStack stack : getDrops(getWorld(), getPos(), getState(getPos()), 0))
            Block.spawnAsEntity(world, pos, stack);
        setToAir();
    }

    public void popOff(World world, BlockPos pos) {
        if (isDead()) return;
        for (ItemStack stack : getDrops(world, pos, getState(getPos()), 0))
            Block.spawnAsEntity(world, pos, stack);
        setToAir();
    }

    public void burnOff() {
        if (isDead()) return;
        for (ItemStack stack : getDrops(getWorld(), getPos(), getState(getPos()), 0)) {
            Block.spawnAsEntity(getWorld(), getPos(), stack);
        }
        setToFire();
    }

    public void burnOff(Entity entity) {
        if (isDead()) return;
        for (ItemStack stack : getDrops(getWorld(), getPos(), getState(getPos()), 0))
            Block.spawnAsEntity(entity.world, entity.getPosition(), stack);
        setToFire();
    }

    public void burnOff(World world, double x, double y, double z) {
        if (isDead()) return;
        BlockPos pos = new BlockPos(x, y, z);
        for (ItemStack stack : getDrops(getWorld(), getPos(), getState(getPos()), 0))
            Block.spawnAsEntity(world, pos, stack);
        setToFire();
    }

    public void burnOff(World world, BlockPos aCoords) {
        if (isDead()) return;
        for (ItemStack stack : getDrops(world, pos, getState(getPos()), 0))
            Block.spawnAsEntity(world, pos, stack);
        setToFire();
    }

    @Nonnull
    public ItemStack getStackForm() {
        MultiTileEntityRegistry registry = MultiTileEntityRegistry.getRegistry(itemStackMeta);
        if (registry != null) return registry.getItem(itemStackMeta, new NBTTagCompound());
        return ItemStack.EMPTY;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        blockUpdated = true;
    }

    public int getDefaultPaintingColor() {
        return ConfigHolder.client.defaultPaintingColor;
    }

    public int getPaintingColor() {
        return paintingColor;
    }

    @Override
    public boolean recolorBlock(EnumFacing side, @Nonnull EnumDyeColor color) {
        if (getPaintingColor() == color.colorValue)
            return false;
        setPaintingColor(color.colorValue);
        return true;
    }

    public void setPaintingColor(int paintingColor) {
        this.paintingColor = paintingColor;
        //noinspection ConstantConditions
        if (getWorld() != null && !getWorld().isRemote) {
            causeBlockUpdate();
            markDirty();
            writeCustomData(UPDATE_PAINTING_COLOR, buf -> buf.writeInt(paintingColor));
        }
    }

    public boolean isPainted() {
        return this.paintingColor != -1;
    }

    @Override
    public String getCustomName() {
        return hasCustomName() ? customName : null;
    }

    @Override
    public void setCustomName(String name) {
        customName = name;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        if (target instanceof CuboidRayTraceResult) {
            IndexedCuboid6 hitCuboid = ((CuboidRayTraceResult) (target)).cuboid6;
            if (hitCuboid.data == null || hitCuboid.data instanceof ICoverable.PrimaryBoxData) {
                return getStackForm();
            } else {
                return ItemStack.EMPTY;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getMultiTileEntityId() {
        return multiTileEntityId;
    }

    @Override
    public short getItemStackMeta() {
        return itemStackMeta;
    }

    @Override
    public void setShouldRefresh(boolean shouldRefresh) {
        this.shouldRefresh = shouldRefresh;
    }
}
