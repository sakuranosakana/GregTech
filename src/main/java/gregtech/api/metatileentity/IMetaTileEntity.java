package gregtech.api.metatileentity;

import codechicken.lib.raytracer.CuboidRayTraceResult;
import gregtech.api.GregTechAPI;
import gregtech.api.cover.CoverBehavior;
import gregtech.api.gui.ModularUI;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public interface IMetaTileEntity {

    // Needed
    void setTileEntity(IGregTechTileEntity tileEntity);

    // TODO Try to remove this?
    IGregTechTileEntity getTileEntity();

    // TODO Can potentially refactor holder out of this, and should return IMetaTileEntity
    IMetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity);

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

    /**
     * Creates a UI instance for player opening inventory of this meta tile entity
     *
     * @param player player opening inventory
     * @return freshly created UI instance
     */
    ModularUI createUI(EntityPlayer player);

    // TODO Try to refactor off to block?
    boolean isOpaqueCube();

    // TODO
    boolean onRightClick(EntityPlayer player, EnumHand hand, EnumFacing facing, CuboidRayTraceResult hitResult);

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

    // Hooks into the Block Class. Implement them in order to overwrite the Default Behaviours.
    // TODO more here
    interface IMTEGetDrops                   extends IMetaTileEntity {void getDrops(NonNullList<ItemStack> drops, EntityPlayer harvester);}
    interface IMTECanEntityDestroy           extends IMetaTileEntity {boolean canEntityDestroy(Entity entity);}
    interface IMTENeighborChanged            extends IMetaTileEntity {void neighborChanged();}
    interface IMTEGetLightValue              extends IMetaTileEntity {int getLightValue();}
    interface IMTEGetLightOpacity            extends IMetaTileEntity {int getLightOpacity();}
    interface IMTEGetSubBlocks               extends IMetaTileEntity {void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> blocks);}
    interface IMTEGetComparatorInputOverride extends IMetaTileEntity {int getComparatorInputOverride();}

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

    interface IMTETickable extends IMetaTileEntity {

        /**
         * The First processed Tick which was passed to this MetaTileEntity. This will get called when block was placed as well as on world load
         */
        void onFirstTick();

        void update();

        void onTickFailed(boolean isServerSide);
    }
}
