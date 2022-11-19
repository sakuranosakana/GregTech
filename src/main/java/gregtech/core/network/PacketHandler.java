package gregtech.core.network;

import gregtech.api.network.IPacket;
import gregtech.api.network.IPacketHandler;
import net.minecraft.util.IntIdentityHashBiMap;

public class PacketHandler implements IPacketHandler {

    private static final PacketHandler INSTANCE = new PacketHandler(10);

    private final IntIdentityHashBiMap<Class<? extends IPacket>> packetMap;

    private PacketHandler(int initialCapacity) {
        packetMap = new IntIdentityHashBiMap<>(initialCapacity);
    }

    public static IPacketHandler getInstance() {
        return INSTANCE;
    }

    private int ID = 1;
    @Override
    public void registerPacket(Class<? extends IPacket> packetClass) {
        packetMap.put(packetClass, ID++);
    }

    @Override
    public int getPacketId(Class<? extends IPacket> packetClass) {
        return packetMap.getId(packetClass);
    }

    @Override
    public Class<? extends IPacket> getPacketClass(int packetId) {
        return packetMap.get(packetId);
    }
}
