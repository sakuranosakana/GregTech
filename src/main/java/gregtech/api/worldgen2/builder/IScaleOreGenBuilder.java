package gregtech.api.worldgen2.builder;

public interface IScaleOreGenBuilder<T> extends IOreGenBuilder<T> {


    /**
     * Set the weight of this vein
     *
     * @param weight the weighting of this vein.
     * @return this
     */
    T weight(int weight);

    /**
     * Set the density of this vein
     *
     * @param density the density of this vein.
     * @return this
     */
    T density(int density);

    /**
     * Set the minimum distance away from spawn this vein must be for generation
     * Using 0 means there is no minimum
     *
     * @param distance the distance
     * @return this
     */
    T distance(int distance);

    /**
     * Set the size of the vein
     *
     * @param size the size
     * @return this
     */
    T size(int size);
}
