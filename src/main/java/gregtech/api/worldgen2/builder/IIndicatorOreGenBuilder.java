package gregtech.api.worldgen2.builder;

import gregtech.api.unification.material.Material;
import net.minecraft.block.state.IBlockState;

import javax.annotation.Nullable;

public interface IIndicatorOreGenBuilder<T> extends IOreGenBuilder<T> {

    /**
     * Set the indicator material of the vein for surface rocks
     *
     * @param indicator the indicator material
     * @return this
     */
    T indicator(@Nullable Material indicator);

    /**
     * Set the indicator {@link IBlockState} of the vein for surface indicators
     * Cannot be used in conjunction with {@link IIndicatorOreGenBuilder#indicator(Material)}
     *
     * @param indicator the indicator block state
     * @return this
     */
    T indicator(@Nullable IBlockState indicator);
}
