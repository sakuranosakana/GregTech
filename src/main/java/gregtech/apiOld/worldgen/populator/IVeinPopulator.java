package gregtech.apiOld.worldgen.populator;

import com.google.gson.JsonObject;
import gregtech.apiOld.worldgen.config.OreDepositDefinition;

public interface IVeinPopulator {

    void loadFromConfig(JsonObject object);

    void initializeForVein(OreDepositDefinition definition);

}
