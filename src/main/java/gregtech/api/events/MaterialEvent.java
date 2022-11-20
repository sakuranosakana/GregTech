package gregtech.api.events;

import gregtech.core.material.internal.Material;
import net.minecraftforge.fml.common.eventhandler.GenericEvent;

public class MaterialEvent extends GenericEvent<Material> {

    public Stage stage;

    public MaterialEvent(Stage stage) {
        super(Material.class);
        this.stage = stage;
    }

    public static MaterialEvent getRegisterEvent() {
        return new MaterialEvent(Stage.LOADING);
    }

    public static MaterialEvent getRegistryClosedEvent() {
        return new MaterialEvent(Stage.ENDED);
    }

    public enum Stage {
        LOADING,
        ENDED
    }
}
