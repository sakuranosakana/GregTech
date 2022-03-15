package gregtech.api.metatileentity.interfaces;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;

import java.util.Arrays;

/**
 * General class used for any TileEntity which can be rotated.
 * <p>
 * Provides useful defaults for commonly used methods on the Block and TileEntity classes.
 */
public interface ITurnable {

    /**
     * Get the front-facing of this Block.
     *
     * @return The front-facing of this Block
     */
    EnumFacing getFrontFacing();

    /**
     * Set the front-facing to a passed Facing.
     *
     * @param facing The Facing to set as front
     */
    void setFrontFacing(EnumFacing facing);

    /**
     * Test if a specific Facing is valid to be the front.
     *
     * @param facing The Facing to test
     * @return true if valid
     */
    boolean isValidFrontFacing(EnumFacing facing);

    /**
     * Get an array of all valid Facings.
     * <p>
     * Sourced from {@link net.minecraft.block.Block#getValidRotations}
     *
     * @return An array of all valid front-facings
     */
    default EnumFacing[] getValidRotations() {
        return Arrays.stream(EnumFacing.VALUES)
                .filter(this::isValidFrontFacing)
                .toArray(EnumFacing[]::new);
    }

    /**
     * Rotate this Block to a passed Facing.
     * <p>
     * Sourced from {@link net.minecraft.block.Block#rotateBlock}
     *
     * @param facing The Facing to rotate towards
     * @return true if successfully rotated, false otherwise
     */
    default boolean rotateBlock(EnumFacing facing) {
        if (isValidFrontFacing(facing) && getFrontFacing() != facing) {
            setFrontFacing(facing);
            return true;
        }
        return false;
    }

    /**
     * Rotate this Block via a passed Rotation.
     * <p>
     * Sourced from: {@link net.minecraft.tileentity.TileEntity#rotate}
     *
     * @param rotation The Rotation to rotate about
     */
    default void rotate(Rotation rotation) {
        setFrontFacing(rotation.rotate(getFrontFacing()));
    }

    /**
     * Mirror this Block via a passed Mirror.
     * <p>
     * Sourced from: {@link net.minecraft.tileentity.TileEntity#rotate}
     *
     * @param mirror The Mirror to flip on
     */
    default void mirror(Mirror mirror) {
        rotate(mirror.toRotation(getFrontFacing()));
    }

    // TODO Remove this
    boolean hasFrontFacing();
}
