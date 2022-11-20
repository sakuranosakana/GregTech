package gregtech.apiOld.pipenet.block.material;

import gregtech.apiOld.pipenet.block.ItemBlockPipe;
import gregtech.core.material.internal.Material;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemBlockMaterialPipe<PipeType extends Enum<PipeType> & IMaterialPipeType<NodeDataType>, NodeDataType> extends ItemBlockPipe<PipeType, NodeDataType> {

    public ItemBlockMaterialPipe(BlockMaterialPipe<PipeType, NodeDataType, ?> block) {
        super(block);
    }

    @Nonnull
    @SuppressWarnings("unchecked")
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        PipeType pipeType = blockPipe.getItemPipeType(stack);
        Material material = ((BlockMaterialPipe<PipeType, NodeDataType, ?>) blockPipe).getItemMaterial(stack);
        return material == null ? " " : pipeType.getOrePrefix().getLocalNameForItem(material);
    }
}
