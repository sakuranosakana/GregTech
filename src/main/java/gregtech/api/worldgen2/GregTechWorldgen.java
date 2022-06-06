package gregtech.api.worldgen2;

import com.google.common.collect.ImmutableSet;
import gregtech.api.GTValues;
import gregtech.api.unification.material.Materials;
import gregtech.api.worldgen2.generator.WorldgenObject;
import gregtech.api.worldgen2.generator.WorldgenOresLayered;
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

import static net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable.EventType.*;

@Mod.EventBusSubscriber
public class GregTechWorldgen implements IWorldGenerator {

    public static final GregTechWorldgen INSTANCE = new GregTechWorldgen();

    public static final List<WorldgenObject> WORLDGEN_OVERWORLD = new ObjectArrayList<>();
    public static final List<WorldgenObject> WORLDGEN_NETHER = new ObjectArrayList<>();
    public static final List<WorldgenObject> WORLDGEN_END = new ObjectArrayList<>();

    public static final List<WorldgenObject> ORES_OVERWORLD = new ObjectArrayList<>();
    public static final List<WorldgenObject> ORES_NETHER = new ObjectArrayList<>();
    public static final List<WorldgenObject> ORES_END = new ObjectArrayList<>();

    private static final Set<OreGenEvent.GenerateMinable.EventType> ORE_EVENT_TYPES = ImmutableSet.of(COAL, DIAMOND, GOLD, IRON, LAPIS, REDSTONE, QUARTZ, EMERALD);

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator iChunkGenerator, IChunkProvider iChunkProvider) {
        GTWorldGenerator.generate(world, chunkX << 4, chunkZ << 4);
    }

    public static void init() {
        new WorldgenOresLayered("tetra", GTValues.MODID, true, 70, 120, 150, 4, 0, 24, Materials.Tetrahedrite, Materials.Tetrahedrite, Materials.Copper, Materials.Stibnite, Materials.Copper, ORES_OVERWORLD);
        new WorldgenOresLayered("magnetite", GTValues.MODID, true, 30, 120, 100, 2, 0, 32, Materials.Magnetite, Materials.VanadiumMagnetite, Materials.Iron, Materials.Gold, Materials.Magnetite, ORES_OVERWORLD);
        new WorldgenOresLayered("apatite", GTValues.MODID, true, 40, 120, 50, 1, 0, 16, Materials.Apatite, Materials.TricalciumPhosphate, Materials.Realgar, Materials.Tin, Materials.Apatite, ORES_OVERWORLD);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onOreGenerate(@Nonnull OreGenEvent.GenerateMinable event) {
        OreGenEvent.GenerateMinable.EventType eventType = event.getType();
        if (ConfigHolder.worldgen.disableVanillaOres && ORE_EVENT_TYPES.contains(eventType)) {
            event.setResult(Event.Result.DENY);
        }
    }
}
