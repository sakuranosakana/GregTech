package gregtech.api.worldgen2.builder;

import gregtech.api.worldgen2.generator.IWorldgenObject;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * An interface defining builder classes for world generation
 *
 * @param <R> the type of the builder
 */
public interface IOreGenBuilder<R> {

    /**
     * Set the weight of this vein
     *
     * @param weight the weighting of this vein.
     * @return this
     */
    R weight(int weight);

    Object build(@Nonnull List<List<IWorldgenObject>> generationLists);
}
