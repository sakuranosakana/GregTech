package gregtech.api.network;

public interface IPacketHandler {

    void registerPacket(Class<? extends IPacket> packetClass);

    int getPacketId(Class<? extends IPacket> packetClass);

    Class<? extends IPacket> getPacketClass(int packetId);
}
