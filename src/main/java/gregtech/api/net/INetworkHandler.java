package gregtech.api.net;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public interface INetworkHandler {

    /**
     * It sends a Packet from Client to Server.
     */
    void sendToServer(IPacket packet);

    /**
     * It sends a Packet to the Player, who is mentioned inside the Parameter.
     */
    void sendToPlayer(IPacket packet, EntityPlayerMP player);

    /**
     * It sends a Packet to all Players, who are in the specified Range.
     */
    void sendToAllAround(IPacket packet, NetworkRegistry.TargetPoint targetPoint);

    /**
     * It sends a Packet to all Players, who watch the Chunk on these X/Z Coordinates.
     */
    void sendToAllPlayersInRange(IPacket packet, World world, int x, int z);

    /**
     * It sends a Packet to all Players, who watch the Chunk on these X/Z Coordinates.
     */
    void sendToAllPlayersInRange(IPacket packet, World world, BlockPos pos);

    /**
     * It sends a Packet to all Players, who watch the Chunk on these X/Z Coordinates.
     */
    void sendToPlayerIfInRange(IPacket packet, UUID player, World world, int x, int z);

    /**
     * It sends a Packet to all Players, who watch the Chunk on these X/Z Coordinates.
     */
    void sendToPlayerIfInRange(IPacket packet, UUID player, World world, BlockPos pos);

    /**
     * It sends a Packet to all Players, who watch the Chunk on these X/Z Coordinates.
     */
    void sendToAllPlayersInRangeExcept(IPacket packet, UUID player, World world, int x, int z);

    /**
     * It sends a Packet to all Players, who watch the Chunk on these X/Z Coordinates.
     */
    void sendToAllPlayersInRangeExcept(IPacket packet, UUID player, World world, BlockPos pos);

    /**
     * For very advanced usage only!
     */
    FMLEmbeddedChannel getChannel(Side side);
}
