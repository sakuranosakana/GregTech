package gregtech.core.material.internal;

import gregtech.api.material.IMaterial;
import gregtech.api.material.IMaterialFlag;
import gregtech.api.material.PropertyKey;
import gregtech.core.CoreModule;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MaterialFlag implements IMaterialFlag {

    private static final Set<IMaterialFlag> FLAG_REGISTRY = new HashSet<>();

    private final String name;

    private final Set<IMaterialFlag> requiredFlags;
    private final Set<PropertyKey<?>> requiredProperties;

    private MaterialFlag(String name, Set<IMaterialFlag> requiredFlags, Set<PropertyKey<?>> requiredProperties) {
        this.name = name;
        this.requiredFlags = requiredFlags;
        this.requiredProperties = requiredProperties;
        FLAG_REGISTRY.add(this);
    }

    @Override
    public Set<IMaterialFlag> getRequiredFlags() {
        return null;
    }

    @Override
    public Set<PropertyKey<?>> getRequiredProperties() {
        return null;
    }

    public Set<IMaterialFlag> verifyFlag(IMaterial material) {
        requiredProperties.forEach(key -> {
            if (!material.hasProperty(key)) {
                CoreModule.logger.warn("Material {} does not have required property {} for flag {}!", material.getUnlocalizedName(), key.toString(), this.name);
            }
        });

        Set<IMaterialFlag> thisAndDependencies = new HashSet<>(requiredFlags);
        thisAndDependencies.addAll(requiredFlags.stream()
                .map(f -> f.verifyFlag(material))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet()));

        return thisAndDependencies;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MaterialFlag) {
            return ((MaterialFlag) o).name.equals(this.name);
        }
        return false;
    }

    public static IMaterialFlag getByName(String name) {
        return FLAG_REGISTRY.stream().filter(f -> f.toString().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static class Builder {

        final String name;

        final Set<IMaterialFlag> requiredFlags = new HashSet<>();
        final Set<PropertyKey<?>> requiredProperties = new HashSet<>();

        public Builder(String name) {
            this.name = name;
        }

        public Builder requireFlags(IMaterialFlag... flags) {
            requiredFlags.addAll(Arrays.asList(flags));
            return this;
        }

        public Builder requireProps(PropertyKey<?>... propertyKeys) {
            requiredProperties.addAll(Arrays.asList(propertyKeys));
            return this;
        }

        public MaterialFlag build() {
            return new MaterialFlag(name, requiredFlags, requiredProperties);
        }
    }
}
