package gregtech.integration.screeninfo;

import mcjty.theoneprobe.api.IProbeInfo;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import java.util.List;

public interface ICapabilityProvider<T> {

    Capability<T> getCapability();

    Class<?> getTileEntityClass();

    boolean allowDisplaying(T capability);

    void addProbeInfo(T capability, IProbeInfo info, TileEntity tileEntity, EnumFacing sideHit);

    void addWailaInfo(T capability, List<String> tooltip, TileEntity tileEntity, EnumFacing sideHit);
}
