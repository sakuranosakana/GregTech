package gregtech.api.metatileentity;

import gregtech.api.GregTechAPI;
import gregtech.api.cover.CoverBehavior;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public interface IMetaTileEntity {

    // Needed
    void setHolder(MetaTileEntityHolder holder);

    // TODO Try to remove this?
    MetaTileEntityHolder getHolder();

    // TODO Can potentially refactor holder out of this, and should return IMetaTileEntity
    MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder);

    // TODO Separate these out into their own ifaces? Maybe, maybe not
    void writeInitialSyncData(PacketBuffer buf);
    void receiveInitialSyncData(PacketBuffer buf);
    void receiveCustomData(int discriminator, PacketBuffer buf);
    void readFromNBT(NBTTagCompound data);
    NBTTagCompound writeToNBT(NBTTagCompound data);

    // Needed
    ResourceLocation getMetaTileEntityId();

    // Needed
    String getMetaFullName();

    // Needed
    default ItemStack getStackForm() {
        return getStackForm(1);
    }

    // Needed
    default ItemStack getStackForm(int amount) {
        int metaTileEntityId = GregTechAPI.MTE_REGISTRY.getIdByObjectName(getMetaTileEntityId());
        return new ItemStack(GregTechAPI.MACHINE, amount, metaTileEntityId);
    }

    // TODO Try to refactor off to block?
    boolean isOpaqueCube();

    default void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        items.add(getStackForm());
    }

    // TODO Pull out into iface like, "IMTECovers" (or just stick in ICoverable)
    CoverBehavior getCoverAtSide(EnumFacing facing);
    <T> T getCoverCapability(Capability<T> capability, EnumFacing facing);

    // TODO Pull out into iface like, "IMTEFacing"
    void setFrontFacing(EnumFacing facing);
    EnumFacing getFrontFacing();
    boolean isValidFrontFacing(EnumFacing facing);

    // Hooks into the TileEntity Class. Implement them in order to overwrite the Default Behaviours.
    // TODO more here
    interface IMTEOnLoad        extends IMetaTileEntity {void onLoad();}
    interface IMTEOnChunkUnload extends IMetaTileEntity {void onChunkUnload();}
    interface IMTEInvalidate    extends IMetaTileEntity {void invalidate();}
    interface IMTEUpdate        extends IMetaTileEntity {void update();}

    // Hooks into the Block Class. Implement them in order to overwrite the Default Behaviours.
    // TODO

    // Custom interfaces that add new behavior
    // TODO Javadoc these!!
    interface IMTEOnAttached    extends IMetaTileEntity {void onAttached(Object... data);}

    /**
     * Called from ItemBlock to initialize this MTE with data contained in ItemStack
     */
    interface IMTEItemStackData extends IMetaTileEntity {
        void writeItemStackData(NBTTagCompound data);

        void initFromItemStackData(NBTTagCompound data);
    }

    /**
     *
     */
    interface IMTEItemStackCapability extends IMetaTileEntity {
        ICapabilityProvider initItemStackCapabilities(ItemStack stack);
    }
}
