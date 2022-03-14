package gregtech.api.tileentity;

import gregtech.api.cover.ICoverable;
import net.minecraft.util.EnumFacing;

public interface ITileEntityRotatable extends ICoverable {

    EnumFacing getFrontFacing();

    void setFrontFacing(EnumFacing frontFacing);

    /**
     * Called when the front facing is set
     * @param facing the new facing
     */
    default void onFrontFacingSet(EnumFacing facing) {

    }

    /**
     *
     * @param facing the facing to test validity of
     * @return whether the facing is a valid front facing
     */
    default boolean isValidFrontFacing(EnumFacing facing) {
        return facing != EnumFacing.UP && facing != EnumFacing.DOWN;
    }

    /**
     *
     * @param face the face to check
     * @return if the side is used in some way
     */
    default boolean isSideUsed(EnumFacing face) {
        if (getCoverAtSide(face) != null) return true;
        return face == this.getFrontFacing() && this.canRenderFrontFaceX();
    }

    /**
     *
     * @return whether this can render the machine grid X on its front face
     */
    default boolean canRenderFrontFaceX() {
        return false;
    }
}
