package gregtech.api.worldgen2;

import com.google.common.collect.ImmutableSet;
import gregtech.api.worldgen2.builder.LayeredOreVeinBuilder;
import gregtech.api.worldgen2.generator.IWorldgenObject;
import gregtech.common.ConfigHolder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static gregtech.api.unification.material.Materials.*;
import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.*;

@Mod.EventBusSubscriber
public class GregTechWorldgen implements IWorldGenerator {

    public static final GregTechWorldgen INSTANCE = new GregTechWorldgen();

    public static final List<IWorldgenObject> WORLDGEN_OVERWORLD = new ObjectArrayList<>();
    public static final List<IWorldgenObject> WORLDGEN_GREGTECH = new ObjectArrayList<>();
    public static final List<IWorldgenObject> WORLDGEN_NETHER = new ObjectArrayList<>();
    public static final List<IWorldgenObject> WORLDGEN_END = new ObjectArrayList<>();

    public static final List<IWorldgenObject> ORES_OVERWORLD = new ObjectArrayList<>();
    public static final List<IWorldgenObject> ORES_NETHER = new ObjectArrayList<>();
    public static final List<IWorldgenObject> ORES_END = new ObjectArrayList<>();

    private static final Set<OreGenEvent.GenerateMinable.EventType> ORE_EVENT_TYPES = ImmutableSet.of(COAL, DIAMOND, GOLD, IRON, LAPIS, REDSTONE, QUARTZ, EMERALD);

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator iChunkGenerator, IChunkProvider iChunkProvider) {
        GTWorldGenerator.generate(world, chunkX << 4, chunkZ << 4);
    }

    public static void init() {
        LayeredOreVeinBuilder.builder("tetrahedrite").yRange(70, 120).weight(150).density(4).size(24).top(Tetrahedrite).bottom(Bornite).between(Copper).spread(Stibnite).indicator(Copper).build(ORES_OVERWORLD);
        LayeredOreVeinBuilder.builder("magnetite").yRange(30, 70).weight(100).density(2).size(32).top(Magnetite, 10).bottom(VanadiumMagnetite, 2).between(Iron).spread(Gold).indicator(Iron).build(ORES_OVERWORLD);
        LayeredOreVeinBuilder.builder("apatite").yRange(40, 60).weight(50).size(16).top(Apatite, 2).bottom(TricalciumPhosphate, 10).between(Realgar, 7).spread(Tin).indicator(Apatite).build(ORES_OVERWORLD);

        GregTechStoneLayers.init();
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onOreGenerate(@Nonnull OreGenEvent.GenerateMinable event) {
        OreGenEvent.GenerateMinable.EventType eventType = event.getType();
        if (ConfigHolder.worldgen.disableVanillaOres && ORE_EVENT_TYPES.contains(eventType)) {
            event.setResult(Event.Result.DENY);
        }
    }
}
