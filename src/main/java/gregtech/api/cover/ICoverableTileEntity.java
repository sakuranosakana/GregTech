package gregtech.api.cover;

import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.ColourMultiplier;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.*;
import gregtech.api.metatileentity.interfaces.IHasWorldObjectAndCoords;
import gregtech.api.metatileentity.interfaces.IPaintable;
import gregtech.api.util.GTUtility;
import gregtech.client.utils.RenderUtil;
import gregtech.common.ConfigHolder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public interface ICoverableTileEntity extends IHasWorldObjectAndCoords {

    Transformation REVERSE_HORIZONTAL_ROTATION = new Rotation(Math.PI, new Vector3(0.0, 1.0, 0.0)).at(Vector3.center);
    Transformation REVERSE_VERTICAL_ROTATION = new Rotation(Math.PI, new Vector3(1.0, 0.0, 0.0)).at(Vector3.center);

    /**
     * checks if a cover can be placed on a side of this TileEntity
     *
     * @param side the side to check
     * @param cover the cover to be placed
     * @param player the player placing the cover
     * @return {@code true} if the cover can be placed, otherwies {@code false}
     */
    boolean canPlaceCover(EnumFacing side, ICover cover, EntityPlayer player);

    /**
     * attempts to place a cover on a side of this TileEntity
     *
     * @param side   the side to place the cover on
     * @param cover  the cover to place
     * @param player the player placing the cover
     * @return {@code true} if the cover was placed, otherwise {@code false}
     */
    boolean placeCover(EnumFacing side, ICover cover, EntityPlayer player);

    /**
     * attempts to remove a cover on a side of this TileEntity
     *
     * @param side the side to remove the cover from
     * @return {@code true} if the cover was removed, otherwise {@code false}
     */
    boolean removeCover(EnumFacing side);

    /**
     * retrieves a cover at a given side
     *
     * @param side the side to retrieve the cover from
     * @return the cover retrieved
     */
    ICover getCover(EnumFacing side);

    /**
     * @return {@code true} if this TileEntity has any cover, otherwise {@code false}
     */
    default boolean hasAnyCover() {
        for (EnumFacing facing : EnumFacing.VALUES)
            if (getCover(facing) != null) return true;
        return false;
    }

    /**
     * Used to render the attached covers' baseplate if the attached TileEntity is not a full block in size
     *
     * @return the cover plate thickness
     */
    double getCoverPlateThickness();

    /**
     * Used to determine if the back side of a cover should be rendered. Useful for transparent full-block machines.
     *
     * @return {@code true} if cover back sides should be rendered, otherwise {@code false}
     */
    boolean shouldRenderCoverBackSides();

    @SideOnly(Side.CLIENT)
    default void renderCovers(CCRenderState renderState, Matrix4 translation, BlockRenderLayer layer) {
        renderState.lightMatrix.locate(getWorld(), getPos());
        int paintingColor = GTUtility.convertRGBtoOpaqueRGBA_CL(this instanceof IPaintable ? ((IPaintable) this).getPaintingColorForRendering() : ConfigHolder.client.defaultPaintingColor);
        IVertexOperation[] platePipeline = new IVertexOperation[]{renderState.lightMatrix, new ColourMultiplier(paintingColor)};
        IVertexOperation[] coverPipeline = new IVertexOperation[]{renderState.lightMatrix};

        double coverPlateThickness = getCoverPlateThickness();
        for (EnumFacing sideFacing : EnumFacing.values()) {
            ICover cover = getCover(sideFacing);
            if (cover == null) continue;

            Cuboid6 plateBox = getCoverPlateBox(sideFacing, coverPlateThickness);
            if (cover.canRenderInLayer(layer) && coverPlateThickness > 0) {
                renderState.preRenderWorld(getWorld(), getPos());
                cover.renderCoverPlate(sideFacing, renderState, translation, platePipeline, plateBox, layer);
            }
            if (cover.canRenderInLayer(layer)) {
                cover.renderCover(renderState, RenderUtil.adjustTrans(translation, sideFacing, 1), coverPipeline, plateBox, layer);
                if (coverPlateThickness == 0.0 && shouldRenderCoverBackSides() && cover.canRenderBackSide()) {
                    //machine is full block, but still not opaque - render cover on the back side too
                    Matrix4 backTranslation = translation.copy();
                    if (sideFacing.getAxis().isVertical()) {
                        REVERSE_VERTICAL_ROTATION.apply(backTranslation);
                    } else {
                        REVERSE_HORIZONTAL_ROTATION.apply(backTranslation);
                    }
                    backTranslation.translate(-sideFacing.getXOffset(), -sideFacing.getYOffset(), -sideFacing.getZOffset());
                    cover.renderCover(renderState, backTranslation, coverPipeline, plateBox, layer); // may need to translate the layer here as well
                }
            }
        }
    }

    /**
     * Adds the attached covers to the block collision list of this TileEntity
     *
     * @param collisionList the List to add to
     */
    default void addCoversToCollisionBoundingBox(List<? super IndexedCuboid6> collisionList) {
        double plateThickness = getCoverPlateThickness();
        if (plateThickness > 0.0) {
            for (EnumFacing side : EnumFacing.VALUES) {
                if (getCover(side) != null) {
                    Cuboid6 coverBox = getCoverPlateBox(side, plateThickness);
                    collisionList.add(new IndexedCuboid6(side, coverBox));
                }
            }
        }
    }

    /**
     * @param side           the side the cover is located on
     * @param plateThickness the thickness of the cover plate
     * @return a {@link Cuboid6} corresponding to the cover plate at a given side
     * @throws UnsupportedOperationException if the side was not an {@link EnumFacing} constant
     */
    static Cuboid6 getCoverPlateBox(EnumFacing side, double plateThickness) {
        switch (side) {
            case UP:
                return new Cuboid6(0.0, 1.0 - plateThickness, 0.0, 1.0, 1.0, 1.0);
            case DOWN:
                return new Cuboid6(0.0, 0.0, 0.0, 1.0, plateThickness, 1.0);
            case NORTH:
                return new Cuboid6(0.0, 0.0, 0.0, 1.0, 1.0, plateThickness);
            case SOUTH:
                return new Cuboid6(0.0, 0.0, 1.0 - plateThickness, 1.0, 1.0, 1.0);
            case WEST:
                return new Cuboid6(0.0, 0.0, 0.0, plateThickness, 1.0, 1.0);
            case EAST:
                return new Cuboid6(1.0 - plateThickness, 0.0, 0.0, 1.0, 1.0, 1.0);
            default:
                throw new UnsupportedOperationException();
        }
    }
}
