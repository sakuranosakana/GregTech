package gregtech.apiOld.pipenet.block.material;

import gregtech.apiOld.pipenet.block.IPipeType;
import gregtech.apiOld.pipenet.tile.IPipeTile;
import gregtech.apiOld.unification.material.Material;

public interface IMaterialPipeTile<PipeType extends Enum<PipeType> & IPipeType<NodeDataType>, NodeDataType> extends IPipeTile<PipeType, NodeDataType> {

    Material getPipeMaterial();
}
