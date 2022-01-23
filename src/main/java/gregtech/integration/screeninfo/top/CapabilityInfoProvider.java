package gregtech.integration.screeninfo.top;

import gregtech.api.util.GTLog;
import gregtech.integration.screeninfo.ICapabilityProvider;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import java.util.ArrayList;
import java.util.List;

public class CapabilityInfoProvider implements IProbeInfoProvider {

    private final List<ICapabilityProvider<?>> providers = new ArrayList<>();

    public void addProvider(ICapabilityProvider<?> provider) {
        providers.add(provider);
    }

    @Override
    public String getID() {
        return "gregtech:info_provider";
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer entityPlayer, World world, IBlockState blockState, IProbeHitData data) {
        if (blockState.getBlock().hasTileEntity(blockState)) {
            EnumFacing sideHit = data.getSideHit();
            TileEntity tileEntity = world.getTileEntity(data.getPos());
            if (tileEntity == null) return;
            try {
                providers.forEach(p -> testProvider(p, probeInfo, tileEntity, sideHit));
            } catch (ClassCastException ignored) {
            } catch (Throwable e) {
                // TODO Make this logger better
                GTLog.logger.info("Bad One probe Implem: {} {} {}", e.getClass().toGenericString(), e.getMessage(), e.getStackTrace()[0].getClassName());
                e.printStackTrace();
            }
        }
    }

    private <T> void testProvider(ICapabilityProvider<T> provider, IProbeInfo probeInfo, TileEntity tileEntity, EnumFacing sideHit) {
        Capability<T> capability = provider.getCapability();
        T tileCapability = tileEntity.getCapability(capability, null);
        if (tileCapability != null && provider.allowDisplaying(tileCapability)) {
            provider.addProbeInfo(tileCapability, probeInfo, tileEntity, sideHit);
        }
    }
}
