package gregtech.api.metatileentity.interfaces;

import gregtech.api.gui.IUIHolder;
import net.minecraft.network.PacketBuffer;

import java.util.function.Consumer;

/**
 * A simple compound Interface for all my TileEntities.
 * <p/>
 * Also delivers most of the Informations about TileEntities.
 * <p/>
 */
public interface IGregTechTileEntity extends IHasWorldObjectAndCoords, IUIHolder {

    IMetaTileEntity getMetaTileEntity();

    IMetaTileEntity setMetaTileEntity(IMetaTileEntity metaTileEntity);

    void writeCustomData(int discriminator, Consumer<PacketBuffer> dataWriter);

    long getOffsetTimer();
}
