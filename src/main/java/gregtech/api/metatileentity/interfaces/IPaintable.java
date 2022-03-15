package gregtech.api.metatileentity.interfaces;

import gregtech.api.capability.GregtechDataCodes;
import gregtech.common.ConfigHolder;
import net.minecraft.item.EnumDyeColor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IPaintable extends IHasWorldObjectAndCoords, IDataSyncable {

    /**
     * Used for writing and reading painting color with {@link net.minecraft.nbt.NBTTagCompound}
     */
    String TAG_KEY_PAINTING_COLOR = "PaintingColor";

    /**
     * Value representing no painting color
     */
    int NO_PAINTING_COLOR = -1;

    /**
     * @return The current color the TileEntity is painted with
     */
    int getPaintingColor();

    /**
     * @return The default painting color of the TileEntity
     */
    default int getDefaultPaintingColor() {
        return ConfigHolder.client.defaultPaintingColor;
    }

    /**
     * Use when rendering to prevent using {@value #NO_PAINTING_COLOR} for colorization
     *
     * @return The painting color for use with rendering
     */
    @SideOnly(Side.CLIENT)
    default int getPaintingColorForRendering() {
        return isPainted() ? getPaintingColor() : getDefaultPaintingColor();
    }

    /**
     * Sets the TileEntity's painting color to a new one
     *
     * @param color The color to change to
     */
    void setPaintingColor(int color);

    /**
     * @return {@code true} if the TileEntity is currently painted, {@code false} otherwise
     */
    default boolean isPainted() {
        return getPaintingColor() != NO_PAINTING_COLOR;
    }

    /**
     * Removes the painting color of the TileEntity, setting it to {@value #NO_PAINTING_COLOR}
     */
    default void removePaint() {
        if (isPainted()) setPaintingColor(NO_PAINTING_COLOR);
    }

    /**
     * @param color the {@link EnumDyeColor} to change colors to
     * @return {@code true} if the current color was changed, else {@code false}
     */
    default boolean recolorBlock(EnumDyeColor color) {
        int colorValue = color == null ? NO_PAINTING_COLOR : color.colorValue;
        if (getPaintingColor() != colorValue) {
            setPaintingColor(colorValue);
            if (isServerSide()) {
                notifyBlockUpdate();
                markDirty();
                writeCustomData(GregtechDataCodes.UPDATE_PAINTING_COLOR, buf -> buf.writeInt(getPaintingColor()));
            }
            return true;
        }
        return false;
    }
}
