package gregtech.core.material.internal;

import com.google.common.base.Preconditions;
import gregtech.api.material.IMaterialIconSet;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MaterialIconSet implements IMaterialIconSet {

    private static final Map<String, MaterialIconSet> ICON_SETS = new HashMap<>();
    private static int idCounter = 0;

    public final String name;
    public final int id;

    public MaterialIconSet(String name) {
        this.name = name.toLowerCase(Locale.ENGLISH);
        Preconditions.checkArgument(!ICON_SETS.containsKey(this.name), "MaterialIconSet " + this.name + " already registered!");
        this.id = idCounter++;
        ICON_SETS.put(this.name, this);
    }

    public String getName() {
        return name;
    }

    public MaterialIconSet getByName(String name) {
        return get(name);
    }

    public static MaterialIconSet get(String name) {
        return ICON_SETS.get(name.toLowerCase(Locale.ENGLISH));
    }

    @Override
    public String toString() {
        return getName();
    }
}
