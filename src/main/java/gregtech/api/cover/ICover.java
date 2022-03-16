package gregtech.api.cover;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Matrix4;
import gregtech.api.GTValues;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.renderer.texture.cube.SimpleSidedCubeRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ICover {

    /**
     * Performs actions upon cover placement
     *
     * @param side the side this cover was placed on
     */
    void onCoverPlaced(EnumFacing side);

    /**
     * Performs actions upon cover removal
     *
     * @param side the side this cover was removed from
     */
    void onCoverRemoved(EnumFacing side);

    /**
     * Intercepts and prevents cover placement
     *
     * @param side   the side the cover will be placed on
     * @param player the player placing the cover
     * @return {@code true} if the cover placement was prevented, otherwise {@code false}
     */
    boolean interceptCoverPlacement(EnumFacing side, EntityPlayer player);

    /**
     * Intercepts and prevents cover removal
     *
     * @param side   the side the cover will be removed from
     * @param player the player removing the cover
     * @return {@code true} if the cover placement was prevented, otherwise {@code false}
     */
    boolean interceptCoverRemoval(EntityPlayer side, EntityPlayer player);

    /**
     * Intercepts and prevents cover connection to the {@link ICoverableTileEntity} placed on
     * Typically used on pipes and the like
     *
     * @param side the side the cover will connect to
     * @return {@code true} if the cover connection was prevented, otherwise {@code false}
     */
    boolean interceptCoverConnection(EnumFacing side);

    /**
     * Intercepts and prevents cover disconnection from the {@link ICoverableTileEntity} placed on
     * Typically used on pipes and the like
     *
     * @param side the side the cover will disconnect from
     * @return {@code true} if the cover disconnect was prevented, otherwise {@code false}
     */
    boolean interceptCoverDisconnect(EnumFacing side);

    /**
     * @param side   the side which was left-clicked
     * @param player the player clicking
     * @param x      the x coordinate of the click
     * @param y      the y coordinate of the click
     * @param z      the z coordinate of the click
     * @return {@code true} if something happened, otherwise {@code false}
     */
    boolean onLeftClick(EnumFacing side, EntityPlayer player, int x, int y, int z);

    /**
     * @param side   the side which was left-clicked
     * @param player the player clicking
     * @param x      the x coordinate of the click
     * @param y      the y coordinate of the click
     * @param z      the z coordinate of the click
     * @return {@code true} if the left-click was prevented, otherwise {@code false}
     */
    boolean interceptLeftClick(EnumFacing side, EntityPlayer player, int x, int y, int z);

    /**
     * @param side   the side which was right-clicked
     * @param player the player clicking
     * @param x      the x coordinate of the click
     * @param y      the y coordinate of the click
     * @param z      the z coordinate of the click
     * @return {@code true} if something happened, otherwise {@code false}
     */
    boolean onRightClick(EnumFacing side, EntityPlayer player, int x, int y, int z);

    /**
     * @param side   the side which was right-clicked
     * @param player the player clicking
     * @param x      the x coordinate of the click
     * @param y      the y coordinate of the click
     * @param z      the z coordinate of the click
     * @return {@code true} if the right-click was prevented, otherwise {@code false}
     */
    boolean interceptRightClick(EnumFacing side, EntityPlayer player, int x, int y, int z);

    /**
     * @param side   the side which was clicked
     * @param player the player clicking
     * @param stack  the {@link ItemStack} of the tool clicked with
     * @param x      the x coordinate of the click
     * @param y      the y coordinate of the click
     * @param z      the z coordinate of the click
     * @return {@code true} if something happened, otherwise {@code false}
     */
    boolean onToolClick(EnumFacing side, EntityPlayer player, ItemStack stack, int x, int y, int z);

    /**
     * is called when a BlockUpdate happens
     *
     * @param side the side updated
     */
    void onBlockUpdate(EnumFacing side);

    /**
     *
     * @return the TileEntity this cover is attached to
     */
    ICoverableTileEntity getAttachedTileEntity();

    /**
     *
     * @param layer the layer to check
     * @return {@code true} if this cover can render in the layer, otherwise {@code false}
     */
    boolean canRenderInLayer(BlockRenderLayer layer);

    /**
     * Used to determine if the back side of this cover can be rendered.
     *
     * @return true if the back side of this cover can be rendered
     */
    boolean canRenderBackSide();

    /**
     * Called on client side to render this cover on the attached TileEntity's face
     * It will be automatically be translated to prevent Z-fighting with machine faces
     */
    @SideOnly(Side.CLIENT)
    void renderCover(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline, Cuboid6 plateBox, BlockRenderLayer layer);

    /**
     * renders this cover's cover plate
     *
     * @param side the side the cover is attached to
     */
    default void renderCoverPlate(EnumFacing side, CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline, Cuboid6 plateBox, BlockRenderLayer layer) {
        TextureAtlasSprite casingSide = getPlateSprite();
        for (EnumFacing coverPlateSide : EnumFacing.VALUES) {
            boolean isAttachedSide = side.getAxis() == coverPlateSide.getAxis();
            if (isAttachedSide || getAttachedTileEntity().getCover(coverPlateSide) == null) {
                Textures.renderFace(renderState, translation, pipeline, coverPlateSide, plateBox, casingSide, BlockRenderLayer.CUTOUT_MIPPED);
            }
        }
    }

    /**
     *
     * @return the cover plate sprite to use for rendering this cover
     */
    @SideOnly(Side.CLIENT)
    default TextureAtlasSprite getPlateSprite() {
        return Textures.VOLTAGE_CASINGS[GTValues.LV].getSpriteOnSide(SimpleSidedCubeRenderer.RenderSide.SIDE);
    }
}
