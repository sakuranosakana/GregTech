package gregtech.api.pipenet.longdist;

import gregtech.api.util.GTLog;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Objects;

/**
 * This class defines a long distance pipe type. This class MUST be a singleton class!
 */
public abstract class LongDistancePipeType {

    private static final Object2ObjectOpenHashMap<String, LongDistancePipeType> PIPE_TYPES = new Object2ObjectOpenHashMap<>();

    public static LongDistancePipeType getPipeType(String name) {
        return PIPE_TYPES.get(name);
    }

    private final String name;

    protected LongDistancePipeType(String name) {
        this.name = Objects.requireNonNull(name);
        if (PIPE_TYPES.containsKey(name)) {
            throw new IllegalStateException("Pipe Type with name " + name + " already exists!");
        }
        for (LongDistancePipeType pipeType : PIPE_TYPES.values()) {
            if (this.getClass() == pipeType.getClass()) {
                throw new IllegalStateException("Duplicate Pipe Type " + name + " and " + pipeType.name);
            }
        }
        PIPE_TYPES.put(name, this);
    }

    public abstract boolean isValidBlock(IBlockState blockState);

    public abstract boolean isValidEndpoint(MetaTileEntityLongDistanceEndpoint endpoint);

    /**
     * Not yet implemented
     */
    @Deprecated
    private boolean allowOnlyStraight() {
        return false;
    }

    @Nonnull
    public LongDistanceNetwork createNetwork(LongDistanceNetwork.WorldData worldData) {
        return new LongDistanceNetwork(this, worldData);
    }

    public final LongDistanceNetwork createNetwork(World world) {
        return createNetwork(LongDistanceNetwork.WorldData.get(world));
    }

    public final LongDistanceNetwork getOrCreate(World world, BlockPos pos, boolean calculate) {
        LongDistanceNetwork.WorldData worldData = LongDistanceNetwork.WorldData.get(world);
        LongDistanceNetwork network = worldData.getNetwork(pos);
        if (network == null) {
            network = createNetwork(worldData);
            GTLog.logger.info("Created LD Item network");
            if (calculate) {
                network.recalculateNetwork(Collections.singleton(pos));
            }
        } else if (network.getPipeType() != this) {
            throw new IllegalStateException();
        }
        return network;
    }

    public final String getName() {
        return name;
    }
}
