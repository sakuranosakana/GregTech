package gregtech.apiOld.capability;

public interface IMultiblockController {

    boolean isStructureFormed();

    default boolean isStructureObstructed() {
        return false;
    }
}
