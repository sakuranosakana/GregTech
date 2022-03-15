package gregtech.api.metatileentity.interfaces;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;

import java.util.Arrays;

public interface ITurnable {

    EnumFacing getFrontFacing();

    void setFrontFacing(EnumFacing facing);

    boolean isValidFrontFacing(EnumFacing facing);

    default EnumFacing[] getValidRotations() {
        return Arrays.stream(EnumFacing.VALUES)
                .filter(this::isValidFrontFacing)
                .toArray(EnumFacing[]::new);
    }

    default boolean rotateBlock(EnumFacing facing) {
        if (isValidFrontFacing(facing) && getFrontFacing() != facing) {
            setFrontFacing(facing);
            return true;
        }
        return false;
    }

    default void rotate(Rotation rotation) {
        setFrontFacing(rotation.rotate(getFrontFacing()));
    }

    default void mirror(Mirror mirror) {
        rotate(mirror.toRotation(getFrontFacing()));
    }

    // TODO Remove this
    boolean hasFrontFacing();
}
