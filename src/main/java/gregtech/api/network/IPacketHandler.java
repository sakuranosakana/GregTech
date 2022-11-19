package gregtech.api.network;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IPacketHandler {

    void registerPacket(Class<? extends IPacket> packetClass);

    int getPacketId(Class<? extends IPacket> packetClass);

    Class<? extends IPacket> getPacketClass(int packetId);

    void registerServerExecutor(Class<? extends IServerExecutor> packetClass);

    boolean hasServerExecutor(Class<? extends IServerExecutor> packetClass);

    @SideOnly(Side.CLIENT)
    void registerClientExecutor(Class<? extends IClientExecutor> packetClass);

    @SideOnly(Side.CLIENT)
    boolean hasClientExecutor(Class<? extends IClientExecutor> packetClass);
}
