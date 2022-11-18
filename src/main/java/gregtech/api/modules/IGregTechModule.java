package gregtech.api.modules;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public interface IGregTechModule {

    @Nonnull
    default Set<ResourceLocation> getDependencyUids() {
        return Collections.emptySet();
    }

    @Nonnull
    default Set<String> getModDependencyIDs() {
        return Collections.emptySet();
    }

    default void preInit() {
    }

    default void init() {
    }

    default void postInit() {
    }

    default void loadComplete() {
    }

    default void serverStarting() {
    }

    default void serverStarted() {
    }

    default void serverStopped() {
    }

    default void registerServerPackets() {
    }

    default void registerClientPackets() {
    }

    @Nonnull
    default List<Class<?>> getEventBusSubscribers() {
        return Collections.emptyList();
    }

    default boolean processIMC(FMLInterModComms.IMCMessage message) {
        return false;
    }

    Logger getLogger();
}
