package gregtech.api.metatileentity.interfaces;

import net.minecraft.util.EnumFacing;

public interface ITurnable {

    EnumFacing getFrontFacing();

    void setFrontFacing(EnumFacing facing);

    boolean isValidFrontFacing(EnumFacing facing);

    // TODO Remove this
    boolean hasFrontFacing();
}
