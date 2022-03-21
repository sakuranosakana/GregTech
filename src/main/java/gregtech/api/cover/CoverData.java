package gregtech.api.cover;

import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.tuple.Triple;

import java.util.EnumMap;

public class CoverData {

    private EnumMap<EnumFacing, Triple<ICoverable, Boolean, Boolean>> dataMap = new EnumMap<>(EnumFacing.class);
}
