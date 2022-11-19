package gregtech.apiOld.net.packets;

import gregtech.apiOld.gui.ModularUI;
import gregtech.apiOld.gui.impl.ModularUIContainer;
import gregtech.apiOld.net.IPacket;
import gregtech.apiOld.net.NetworkUtils;
import lombok.NoArgsConstructor;
import net.minecraft.inventory.Container;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;

@NoArgsConstructor
public class CPacketUIClientAction implements IPacket {

    private int windowId;
    private int widgetId;
    private PacketBuffer updateData;

    public CPacketUIClientAction(int windowId, int widgetId, PacketBuffer updateData) {
        this.windowId = windowId;
        this.widgetId = widgetId;
        this.updateData = updateData;
    }

    @Override
    public void encode(PacketBuffer buf) {
        NetworkUtils.writePacketBuffer(buf, updateData);
        buf.writeVarInt(windowId);
        buf.writeVarInt(widgetId);
    }

    @Override
    public void decode(PacketBuffer buf) {
        this.updateData = NetworkUtils.readPacketBuffer(buf);
        this.windowId = buf.readVarInt();
        this.widgetId = buf.readVarInt();
    }

    @Override
    public void executeServer(NetHandlerPlayServer handler) {
        Container openContainer = handler.player.openContainer;
        if (openContainer instanceof ModularUIContainer && openContainer.windowId == windowId) {
            ModularUI modularUI = ((ModularUIContainer) openContainer).getModularUI();
            modularUI.guiWidgets.get(widgetId).handleClientAction(updateData.readVarInt(), updateData);
        }
    }
}
