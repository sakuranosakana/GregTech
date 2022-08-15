package gregtech.api.worldgen2.builder;

public interface IHeightOreGenBuilder<T> extends IOreGenBuilder<T> {

    /**
     * Set the y range of this vein
     *
     * @param min the minimum y value
     * @param max the maximum y value
     * @return this
     */
    T yRange(int min, int max);
}
