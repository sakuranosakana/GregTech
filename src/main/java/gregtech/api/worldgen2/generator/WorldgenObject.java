package gregtech.api.worldgen2.generator;

import it.unimi.dsi.fastutil.ints.Int2BooleanOpenHashMap;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

public abstract class WorldgenObject implements IWorldgenObject{

    public boolean isEnabled = true;
    public boolean isInvalid = false;

    public final String name;
    public final String modid;
    public final boolean isDefault;

    public final Map<Integer, Boolean> allowedDimensions = new Int2BooleanOpenHashMap();

    /**
     * @param name       the name of the WorldgenObject
     * @param modid      the modid of the mod adding this WorldgenObject
     * @param isDefault  true if this WorldgenObject generates by default, otherwise false
     * @param generators the groups of world generators to use this WorldgenObject in
     */
    public WorldgenObject(@Nonnull String name, @Nonnull String modid, boolean isDefault, List<List<IWorldgenObject>> generators) {
        if (name.isEmpty()) throw new IllegalArgumentException("Worldgen Object name must not be empty");
        if (modid.isEmpty()) throw new IllegalArgumentException("Worldgen Object modid must not be empty");
        this.name = name;
        this.modid = modid;
        this.isDefault = isDefault;
        for (List<IWorldgenObject> worldgenObjects : generators) {
            worldgenObjects.add(this);
        }
    }

    /**
     * @param world     the world to check
     * @param dimension the dimension to check
     * @return true if generation is allowed, otherwise false
     */
    @Override
    public boolean isEnabled(World world, int dimension) {
        if (this.isInvalid) return false;
        Boolean isAllowed = allowedDimensions.get(dimension);
        if (isAllowed != null) return isAllowed && isEnabled;
        //TODO dimensions from vein config
//        isAllowed = getWorldgeneratorFromConfig().getAllowedDimensions().contains(world.provider.getDimension());
        isAllowed = true; // temporary until configs
        allowedDimensions.put(dimension, isAllowed);
        return isAllowed && isEnabled;
    }
}
