package gregtech.apiOld.pipenet.block.material;

import gregtech.apiOld.GregTechAPI;
import gregtech.apiOld.pipenet.PipeNet;
import gregtech.apiOld.pipenet.WorldPipeNet;
import gregtech.apiOld.pipenet.block.BlockPipe;
import gregtech.apiOld.pipenet.block.IPipeType;
import gregtech.apiOld.pipenet.tile.IPipeTile;
import gregtech.apiOld.pipenet.tile.TileEntityPipeBase;
import gregtech.apiOld.unification.material.Material;
import gregtech.apiOld.unification.ore.OrePrefix;
import net.minecraft.item.ItemStack;

public abstract class BlockMaterialPipe<PipeType extends Enum<PipeType> & IPipeType<NodeDataType> & IMaterialPipeType<NodeDataType>, NodeDataType, WorldPipeNetType extends WorldPipeNet<NodeDataType, ? extends PipeNet<NodeDataType>>> extends BlockPipe<PipeType, NodeDataType, WorldPipeNetType> {

    protected final PipeType pipeType;

    public BlockMaterialPipe(PipeType pipeType) {
        this.pipeType = pipeType;
    }

    @Override
    public NodeDataType createProperties(IPipeTile<PipeType, NodeDataType> pipeTile) {
        PipeType pipeType = pipeTile.getPipeType();
        Material material = ((IMaterialPipeTile<PipeType, NodeDataType>) pipeTile).getPipeMaterial();
        if (pipeType == null || material == null) {
            return getFallbackType();
        }
        return createProperties(pipeType, material);
    }

    @Override
    public NodeDataType createItemProperties(ItemStack itemStack) {
        Material material = getItemMaterial(itemStack);
        if (pipeType == null || material == null) {
            return getFallbackType();
        }
        return createProperties(pipeType, material);
    }

    public ItemStack getItem(Material material) {
        if (material == null) return ItemStack.EMPTY;
        int materialId = GregTechAPI.MATERIAL_REGISTRY.getIDForObject(material);
        return new ItemStack(this, 1, materialId);
    }

    public Material getItemMaterial(ItemStack itemStack) {
        return GregTechAPI.MATERIAL_REGISTRY.getObjectById(itemStack.getMetadata());
    }

    @Override
    public void setTileEntityData(TileEntityPipeBase<PipeType, NodeDataType> pipeTile, ItemStack itemStack) {
        ((TileEntityMaterialPipeBase<PipeType, NodeDataType>) pipeTile).setPipeData(this, pipeType, getItemMaterial(itemStack));
    }

    @Override
    public ItemStack getDropItem(IPipeTile<PipeType, NodeDataType> pipeTile) {
        Material material = ((IMaterialPipeTile<PipeType, NodeDataType>) pipeTile).getPipeMaterial();
        return getItem(material);
    }

    protected abstract NodeDataType createProperties(PipeType pipeType, Material material);

    public OrePrefix getPrefix() {
        return pipeType.getOrePrefix();
    }

    public PipeType getItemPipeType(ItemStack is) {
        return pipeType;
    }
}
