package gregtech.apiOld.util;

import gregtech.apiOld.unification.ore.StoneType;
import net.minecraft.block.state.IBlockState;

public interface IBlockOre {

    IBlockState getOreBlock(StoneType stoneType);

}
