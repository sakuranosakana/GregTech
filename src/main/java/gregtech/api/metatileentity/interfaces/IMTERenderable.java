package gregtech.api.metatileentity.interfaces;

import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.ColourMultiplier;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Matrix4;
import gregtech.api.block.machines.BlockMachine;
import gregtech.api.cover.ICoverable;
import gregtech.api.util.GTUtility;
import gregtech.client.renderer.texture.Textures;
import gregtech.client.utils.BloomEffectUtil;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

public interface IMTERenderable extends IHasWorldObjectAndCoords {

    IndexedCuboid6 FULL_CUBE_COLLISION = new IndexedCuboid6(null, Cuboid6.full);

    /**
     * Renders this TileEntity
     * Note that you shouldn't refer to world-related information in this method, because it will be called on ItemStacks too
     *
     * @param renderState render state (either chunk batched or item)
     * @param pipeline    default set of pipeline transformations
     */
    @SideOnly(Side.CLIENT)
    default void renderTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        TextureAtlasSprite atlasSprite = TextureUtils.getMissingSprite();
        IVertexOperation[] renderPipeline;
        if (this instanceof IPaintable) {
            int paintingColor = ((IPaintable) this).getPaintingColorForRendering();
            renderPipeline = ArrayUtils.add(pipeline, new ColourMultiplier(GTUtility.convertRGBtoOpaqueRGBA_CL(paintingColor)));
        } else renderPipeline = ArrayUtils.clone(pipeline);
        for (EnumFacing face : EnumFacing.VALUES) {
            Textures.renderFace(renderState, translation, renderPipeline, face, Cuboid6.full, atlasSprite, BlockRenderLayer.CUTOUT_MIPPED);
        }
    }

    /**
     * @param renderLayer the {@link BlockRenderLayer} to check
     * @return whether this TileEntity can render in the render layer
     */
    @SideOnly(Side.CLIENT)
    default boolean canRenderInLayer(BlockRenderLayer renderLayer) {
        return renderLayer == BlockRenderLayer.CUTOUT_MIPPED || renderLayer == BloomEffectUtil.getRealBloomLayer() ||
                (renderLayer == BlockRenderLayer.TRANSLUCENT && !getWorld().getBlockState(getPos()).getValue(BlockMachine.OPAQUE));
    }

    /**
     * Called to obtain list of AxisAlignedBB used for collision testing, highlight rendering, and ray tracing this TileEntity's block in-world
     */
    default void addCollisionBoundingBox(List<IndexedCuboid6> collisionList) {
        collisionList.add(FULL_CUBE_COLLISION);
    }

    /**
     * Intended for use with {@link IRotatable} and/or {@link ICoverable}
     *
     * @return {@code true} if this TileEntity can render an X on the front face, otherwise {@code false}
     */
    default boolean canRenderFrontFaceX() {
        return false;
    }

    /**
     * Intended for use with {@link IRotatable} and/or {@link ICoverable}
     *
     * @param side the side to check
     * @return {@code true} if the side checked is in use, otherwise {@code false}
     */
    default boolean isSideUsed(EnumFacing side) {
        return canRenderFrontFaceX();
    }

    /**
     * @return whether the machine grid can be rendered on this TileEntity
     */
    default boolean canRenderMachineGrid() {
        return true;
    }
}
