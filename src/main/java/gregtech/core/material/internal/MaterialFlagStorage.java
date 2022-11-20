package gregtech.core.material.internal;

import gregtech.api.material.IMaterial;
import gregtech.api.material.IMaterialFlag;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MaterialFlagStorage {

    private final Set<IMaterialFlag> flags = new HashSet<>();

    public Collection<IMaterialFlag> getFlags() {
        return flags;
    }

    public MaterialFlagStorage addFlags(IMaterialFlag... flags) {
        this.flags.addAll(Arrays.asList(flags));
        return this;
    }

    public void verify(IMaterial material) {
        flags.addAll(flags.stream()
                .map(f -> f.verifyFlag(material))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet()));
    }

    public boolean hasFlag(IMaterialFlag flag) {
        return flags.contains(flag);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        flags.forEach(f -> sb.append(f.toString()).append("\n"));
        return sb.toString();
    }
}
