package gregtech.api.tileentity;

import gregtech.api.net.INetworkHandler;
import net.minecraft.entity.player.EntityPlayerMP;

public interface ITileEntitySynchronising extends ITileEntityUnloadable {

    /**
     * Called when the passed Player starts watching this Chunk.
     */
    void sendUpdateToPlayer(EntityPlayerMP playerMP);

    /**
     * Called by Packets.
     */
    void processPacket(INetworkHandler networkHandler);
}
