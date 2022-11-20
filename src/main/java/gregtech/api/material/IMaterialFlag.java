package gregtech.api.material;

import java.util.Set;

public interface IMaterialFlag {

    Set<IMaterialFlag> getRequiredFlags();

    Set<PropertyKey<?>> getRequiredProperties();

    Set<IMaterialFlag> verifyFlag(IMaterial material);
}
