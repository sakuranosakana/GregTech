package gregtech.api.metatileentity;

import gregtech.api.cover.CoverBehavior;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

public interface IMetaTileEntity {

    void setHolder(MetaTileEntityHolder holder);

    // TODO Can potentially refactor holder out of this, and should return IMetaTileEntity
    MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder);

    // TODO Separate these out into their own ifaces?
    void writeInitialSyncData(PacketBuffer buf);
    void receiveInitialSyncData(PacketBuffer buf);
    void receiveCustomData(int discriminator, PacketBuffer buf);
    void readFromNBT(NBTTagCompound data);
    NBTTagCompound writeToNBT(NBTTagCompound data);

    ResourceLocation getMetaTileEntityId();

    String getMetaFullName();

    // TODO Clean this up later
    ItemStack getStackForm();

    // TODO Try to fix this?
    MetaTileEntityHolder getHolder();

    // TODO The methods below here should be extracted to their own interfaces
    void onLoad();
    void onUnload();
    void onAttached(Object... data);
    void invalidate();
    void update();
    void setFrontFacing(EnumFacing facing);
    EnumFacing getFrontFacing();
    boolean isValidFrontFacing(EnumFacing facing);
    CoverBehavior getCoverAtSide(EnumFacing facing);
    boolean isOpaqueCube();
    <T> T getCoverCapability(Capability<T> capability, EnumFacing facing);
    void initFromItemStackData(NBTTagCompound tag);
    void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items);
}
