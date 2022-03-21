package gregtech.api.metatileentity.interfaces;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

import java.util.function.Consumer;

public interface IDataSyncable {

    void writeInitialSyncData(PacketBuffer buf);

    void receiveInitialSyncData(PacketBuffer buf);

    void writeCustomData(int discriminator, Consumer<PacketBuffer> dataWriter);

    void receiveCustomData(int discriminator, PacketBuffer buf);

    void readFromNBT(NBTTagCompound data);

    NBTTagCompound writeToNBT(NBTTagCompound data);
}
