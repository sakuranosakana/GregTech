package gregtech.core.network;

import gregtech.api.network.IClientExecutor;
import gregtech.api.network.IPacket;
import gregtech.api.network.IPacketHandler;
import gregtech.api.network.IServerExecutor;
import net.minecraft.util.IntIdentityHashBiMap;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class PacketHandler implements IPacketHandler {

    private static final PacketHandler INSTANCE = new PacketHandler(10);

    private final IntIdentityHashBiMap<Class<? extends IPacket>> packetMap;

    @SideOnly(Side.CLIENT)
    private List<Class<? extends IClientExecutor>> clientExecutors;
    private final List<Class<? extends IServerExecutor>> serverExecutors;

    private PacketHandler(int initialCapacity) {
        packetMap = new IntIdentityHashBiMap<>(initialCapacity);
        serverExecutors = new ArrayList<>();
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            clientExecutors = new ArrayList<>();
        }
    }

    public static IPacketHandler getInstance() {
        return INSTANCE;
    }

    private int ID = 1;
    public void registerPacket(Class<? extends IPacket> packetClass) {
        packetMap.put(packetClass, ID++);
    }

    public int getPacketId(Class<? extends IPacket> packetClass) {
        return packetMap.getId(packetClass);
    }

    public Class<? extends IPacket> getPacketClass(int packetId) {
        return packetMap.get(packetId);
    }

    public void registerServerExecutor(Class<? extends IServerExecutor> packetClass) {
        serverExecutors.add(packetClass);
    }

    public boolean hasServerExecutor(Class<? extends IServerExecutor> packetClass) {
        return serverExecutors.contains(packetClass);
    }

    @SideOnly(Side.CLIENT)
    public void registerClientExecutor(Class<? extends IClientExecutor> packetClass) {
        clientExecutors.add(packetClass);
    }

    @SideOnly(Side.CLIENT)
    public boolean hasClientExecutor(Class<? extends IClientExecutor> packetClass) {
        return clientExecutors.contains(packetClass);
    }
}
