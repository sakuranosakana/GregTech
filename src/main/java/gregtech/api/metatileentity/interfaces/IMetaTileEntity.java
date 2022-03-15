package gregtech.api.metatileentity.interfaces;

import codechicken.lib.raytracer.CuboidRayTraceResult;
import gregtech.api.GregTechAPI;
import gregtech.api.cover.CoverBehavior;
import gregtech.api.gui.ModularUI;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

public interface IMetaTileEntity {

    // Needed
    void setTileEntity(IGregTechTileEntity tileEntity);

    // TODO Try to remove this?
    IGregTechTileEntity getTileEntity();

    void scheduleRenderUpdate();

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

    void onLeftClick(EntityPlayer player, CuboidRayTraceResult result);

    /**
     * Creates a UI instance for player opening inventory of this meta tile entity
     *
     * @param player player opening inventory
     * @return freshly created UI instance
     */
    ModularUI createUI(EntityPlayer player);

    // TODO Try to refactor off to block?
    boolean isOpaqueCube();

    @SideOnly(Side.CLIENT)
    Pair<TextureAtlasSprite, Integer> getParticleTexture();

    // TODO
    boolean onRightClick(EntityPlayer player, EnumHand hand, EnumFacing facing, CuboidRayTraceResult hitResult);

    // TODO Pull out into iface like, "IMTECovers" (or just stick in ICoverable)
    CoverBehavior getCoverAtSide(EnumFacing facing);
    <T> T getCoverCapability(Capability<T> capability, EnumFacing facing);

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
    interface IMTEHardnessResistance         extends IMetaTileEntity {float getBlockHardness(); float getExplosionResistance();}
    interface IMTEGetBlockFaceShape          extends IMetaTileEntity {BlockFaceShape getBlockFaceShape(EnumFacing facing);}
    interface IMTECanCreatureSpawn           extends IMetaTileEntity {boolean canCreatureSpawn(SpawnPlacementType type);}
    interface IMTERecolorBlock               extends IMetaTileEntity {boolean recolorBlock(EnumDyeColor color);}

    // Custom interfaces that add new behavior
    // TODO Javadoc these!!
    interface IMTEOnAttached    extends IMetaTileEntity {void onAttached();}

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
