package gregtech.api.tileentity.base;

import gregtech.api.GTValues;
import gregtech.api.net.INetworkHandler;
import gregtech.api.net.IPacket;
import gregtech.api.tileentity.ITileEntitySynchronising;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.UUID;

public abstract class TileEntityTicksAndSync extends TileEntityAdjacentBuffer implements ITileEntitySynchronising {

    /**
     * Gets set if/when needed.
     */
    public UUID owner = null;

    /**
     * Variable for seeing if the Tick Function is called right now.
     */
    public boolean isRunningTick = false;

    /**
     * Variable for updating Data to the Client
     */
    private boolean sendClientData = false;

    /**
     * Gets set to true when the Block received a Block Update.
     */
    public boolean blockUpdated = false;

    /**
     * @return a Packet containing all Data which has to be synchronised to the Client
     */
    public abstract IPacket getClientDataPacket(boolean shouldSendAll);

    /**
     * Sends all Data to the Clients in Range
     */
    public void sendClientData(boolean shouldSendAll, EntityPlayerMP playerMP) {
        if (playerMP == null) {
            IPacket tPacket = getClientDataPacket(shouldSendAll);
            if (owner == null) {
                getNetworkHandler().sendToAllPlayersInRange(tPacket, world, getPos());
            } else {
                getNetworkHandler().sendToPlayerIfInRange(tPacket, owner, world, getPos());
                getNetworkHandlerNonOwned().sendToAllPlayersInRangeExcept(tPacket, owner, world, getPos());
            }
        } else if (!sendClientData) {
            IPacket tPacket = getClientDataPacket(shouldSendAll);
            if (owner == null) {
                getNetworkHandler().sendToPlayer(tPacket, playerMP);
            } else {
                if (owner.equals(playerMP.getUniqueID())) {
                    getNetworkHandler().sendToPlayer(tPacket, playerMP);
                } else {
                    getNetworkHandlerNonOwned().sendToPlayer(tPacket, playerMP);
                }
            }
        }
    }

    @Override
    public void processPacket(INetworkHandler networkHandler) {
        if (isClientSide()) owner = (networkHandler == getNetworkHandlerNonOwned() ? GTValues.NOT_THE_PLAYER : null);
    }

    /**
     * @return the used Network Handler. Defaults to the API Handler.
     */
    public INetworkHandler getNetworkHandler() {
        return NW_API;
    }

    public INetworkHandler getNetworkHandlerNonOwned() {
        return NW_AP2;
    }

    /**
     * Called to send all Data to the close Clients
     */
    public void updateClientData() {
        sendClientData = true;
    }

    @Override
    public void onCoordinateChange() {
        super.onCoordinateChange();
        updateClientData();
    }

    @Override
    public void validate() {
        super.validate();
        updateClientData();
    }

    @Override
    public final void sendUpdateToPlayer(EntityPlayerMP playerMP) {
        sendClientData(true, playerMP);
    }

    @Override
    public boolean allowInteraction(Entity entity) {
        return owner == null || (entity != null && owner.equals(entity.getUniqueID()));
    }

    @Override
    public void update() {
        isRunningTick = true;
        boolean isServerSide = isServerSide();
        try {
            if (getTimer() == 0) {
                markDirty();
                world.getChunk(getPos()).setModified(true);
                onTickFirst(isServerSide);
            }
            if (!isDead()) onTickStart(getTimer(), isServerSide);
            if (!isDead()) super.update();
            if (!isDead()) onTick(getTimer(), isServerSide);
            if (!isDead() && isServerSide && getTimer() > 2 && (sendClientData || onTickCheck(getTimer()))) {
                sendClientData(sendClientData, null);
                sendClientData = false;
                onTickChecked(getTimer());
            }
            if (!isDead()) onTickResetChecks(getTimer(), isServerSide);
            if (!isDead()) onTickEnd(getTimer(), isServerSide);
        } catch (Throwable e) {
            e.printStackTrace();
            try {
                onTickFailed(getTimer(), isServerSide);
            } catch (Throwable e2) {
                e2.printStackTrace();
            }
        }
        isRunningTick = false;
    }

    /**
     * Used to reset all Variables which have something to do with the detection of Changes. A super Call is important for this one!
     */
    public void onTickResetChecks(long timer, boolean isServerSide) {
        blockUpdated = false;
    }

    /**
     * The very first Tick happening to this TileEntity
     */
    public void onTickFirst(boolean isServerSide) {

    }

    /**
     * The first Part of the Tick.
     */
    public void onTickStart(long timer, boolean isServerSide) {

    }

    /**
     * The regular Tick.
     */
    public void onTick(long timer, boolean isServerSide) {

    }

    /**
     * Use this to check if it is required to send an update to the Clients. If you want you can call "updateClientData", but then you need to return true in order for it to work.
     */
    public boolean onTickCheck(long timer) {
        return false;
    }

    /**
     * Called when onTickCheck returns true. A super Call is important for this one!
     */
    public void onTickChecked(long timer) {

    }

    /**
     * The absolutely last Part of the Tick.
     */
    public void onTickEnd(long timer, boolean isServerSide) {

    }

    /**
     * Gets called when there is an Exception happening during one of the Tick Functions.
     */
    public void onTickFailed(long timer, boolean isServerSide) {

    }
}
